//
// Created by maokangren on 2017/10/26.
// Copyright (c) 2017 com.xixi. All rights reserved.
//

import Foundation
import WebKit
import UIKit

class HKHybirdManager {
    private static let TAG = "hybird"

    private static var classMap = [String: String]()
    private static var schemeMap = [String: (webView: WKWebView?, url: URL?) -> Bool?]()

    static func callNativeMethod(className: String, methodName: String, _ params: Any...) -> Any? {
        return HKReflectUtil.invoke(className, methodName, params)
    }

    /**
     * hybird://native/className/methodName?params=1,2,3,4,5&hashcode=123445
     */
    static func addNativeClass(scheme: String, virtualClassName: String, _ realClassName: String) {
        if (TextUtils.isEmpty(virtualClassName) || TextUtils.isEmpty(realClassName)) {
            HKLogUtil.d(TAG, "[addNativeClass] className:$className or kClass:$kClass is null")
            return
        }
        classMap[virtualClassName] = realClassName

        schemeMap[scheme] = { webView, schemeUrl in
            HKLogUtil.d(TAG, "schemeUrl:", schemeUrl?.absoluteString ?? "nil")
            let pathSegments = schemeUrl?.pathComponents ?? []
            HKLogUtil.d(TAG, "scheme:", schemeUrl?.scheme ?? "nil")
            HKLogUtil.d(TAG, "host:", schemeUrl?.host ?? "nil")
            HKLogUtil.d(TAG, "port:", schemeUrl?.port ?? "nil")
            HKLogUtil.d(TAG, "path:", schemeUrl?.path ?? "nil")
            HKLogUtil.d(TAG, "lastPathComponent:", schemeUrl?.lastPathComponent ?? "nil")
            HKLogUtil.d(TAG, "pathSegments:", pathSegments)
            HKLogUtil.d(TAG, "query:", schemeUrl?.query ?? "nil")
            HKLogUtil.d(TAG, "queryParams:", schemeUrl?.queryParams ?? "nil")
            HKLogUtil.d(TAG, "query:key:params:", schemeUrl?.query("params") ?? "nil")
            HKLogUtil.d(TAG, "queryParamsUnDecoded:", schemeUrl?.queryParamsUnDecoded ?? "nil")
            HKLogUtil.d(TAG, "queryUnDecoded:key:params:", schemeUrl?.queryUnDecoded("params") ?? "nil")

            if (pathSegments.count >= 3) {
                let clazzName = classMap[pathSegments[1]] ?? pathSegments[1]
                let methodName = pathSegments[2]

                let hashcode = schemeUrl?.queryUnDecoded("hashcode") ?? ""
                let params = schemeUrl?.queryUnDecoded("params")

                let paramArray: [String] = params?.split(separator: ",").map {
                    $0.string?.urlDecoded ?? ""
                } ?? []

                HKLogUtil.d(TAG, "clazzName:\(clazzName) , methodName:\(methodName) , params:size:\(String(describing: paramArray.count)):(\(String(describing: paramArray)) , hashCode:\(String(describing: hashcode))")
                HKLogUtil.w(TAG, "native.invoke start --> [\(clazzName).\(methodName)(\(paramArray)]")
//
                var result: Any? = nil
                do {
                    try result = callNativeMethod(className: clazzName, methodName: methodName, paramArray)
                } catch {
                    HKLogUtil.e(TAG, "native.invoke exception", error)
                }
//
                HKLogUtil.w(TAG, "native.invoke end , result:\(String(describing: result))")
//                if (!TextUtils.isEmpty(hashcode)) {
//                    callJsFunction(webView, "javascript:window.hybird.onCallback($hashcode, $result)")
//                }
                return true
            } else {
                HKLogUtil.e(TAG, "schemaUrl:${schemeUrl.toString()} 格式定义错误，请参照 hybird://native/className/methodName?params=1,2,3,4,5&hashcode=123445")
                return false
            }
        }
    }

    /**
     *  scheme://host:port/path?k=v
     *
     *  schema  :   http            Gets the scheme of this URI. Example: "http"
     *  host    :   baidu.com       if the authority is "bob@google.com", this method will return "google.com".
     *  port    :   7777            if the authority is "google.com:80", this method will return 80.
     *  path    :   index.html
     */
    public static func addScheme(_ schemeUrlString: String, _ intercept: @escaping (_ webView: WKWebView?, _ url: URL?) -> Bool) {
        schemeMap[schemeUrlString] = intercept
    }

    public static func removeScheme(schemeUrlString: String) {
        schemeMap.removeValue(forKey: schemeUrlString)
    }

    public static func removeScheme(scheme: String, host: String, port: Int) {
        let schemePrefix = "\(scheme)://\(host):\(port)"
        schemeMap.removeValue(forKey: schemePrefix)
    }

    public static func shouldOverrideUrlLoading(_ webView: WKWebView?, _ userContentController: WKUserContentController, _ message: WKScriptMessage) -> Bool? {
        let schemeUrl = URL(string: String(describing: message.body))
        let schemePrefix = "\(String(describing: schemeUrl?.scheme ?? ""))://\(String(describing: schemeUrl?.host ?? "")):\(String(describing: schemeUrl?.port ?? 0))"
        HKLogUtil.d(HKHybirdManager.TAG, "intercept message.body : \(String(describing: message.body))")
        HKLogUtil.d(HKHybirdManager.TAG, "intercept url : \(String(describing: schemeUrl?.absoluteString ?? ""))")
        HKLogUtil.d(HKHybirdManager.TAG, "intercept schemePrefix : \(schemePrefix)")
        HKLogUtil.d(HKHybirdManager.TAG, "intercept ? \(schemeMap.containsKey(schemePrefix))")
        return schemeMap[schemePrefix]?(webView, schemeUrl)
    }

    public static func callJsFunction(_ webView: WKWebView?, _ javascript: String, _ callback: ((_ result: String?) -> Void?)? = nil) {
        webView?.evaluateJavaScript(javascript) { (any: Any?, error: Error?) in
            HKLogUtil.d(TAG, "any", (any ?? "nil"), "error", (error ?? "nil"))
            let result = error == nil ? (any as? String) : ("error:" + error.debugDescription)
            callback?(result)
        }
    }
}
