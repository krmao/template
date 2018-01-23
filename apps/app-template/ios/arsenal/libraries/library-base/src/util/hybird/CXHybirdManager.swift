//
// Created by maokangren on 2017/10/26.
// Copyright (c) 2017 com.xixi. All rights reserved.
//

import Foundation
import WebKit
import UIKit

class CXHybirdManager {
    private static let TAG = "hybird"

    private static var classMap = [String: String]()
    private static var schemeMap = [String: (webView: WKWebView?, url: URL?) -> Bool?]()

    static func callNativeMethod(className: String, methodName: String, _ params: [String]) -> String? {
        return CXReflectUtil.invoke(className, methodName, params)
    }

    /**
     * hybird://native/className/methodName?params=1,2,3,4,5&hashcode=123445
     */
    static func addNativeClass(_ scheme: String,_ virtualClassName: String, _ realClassName: String) {
        if (TextUtils.isEmpty(virtualClassName) || TextUtils.isEmpty(realClassName)) {
            CXLogUtil.d(TAG, "[addNativeClass] className:$className or kClass:$kClass is null")
            return
        }

        classMap[virtualClassName] = realClassName

        schemeMap[scheme] = { webView, schemeUrl in
            CXLogUtil.d(TAG, "schemeUrl:", schemeUrl?.absoluteString ?? "nil")
            let pathSegments = schemeUrl?.pathComponents ?? []
            CXLogUtil.d(TAG, "scheme:", schemeUrl?.scheme ?? "nil")
            CXLogUtil.d(TAG, "host:", schemeUrl?.host ?? "nil")
            CXLogUtil.d(TAG, "port:", schemeUrl?.port ?? "nil")
            CXLogUtil.d(TAG, "path:", schemeUrl?.path ?? "nil")
            CXLogUtil.d(TAG, "lastPathComponent:", schemeUrl?.lastPathComponent ?? "nil")
            CXLogUtil.d(TAG, "pathSegments:", pathSegments)
            CXLogUtil.d(TAG, "query:", schemeUrl?.query ?? "nil")
            CXLogUtil.d(TAG, "queryParams:", schemeUrl?.queryParams ?? "nil")
            CXLogUtil.d(TAG, "query:key:params:", schemeUrl?.query("params") ?? "nil")
            CXLogUtil.d(TAG, "queryParamsUnDecoded:", schemeUrl?.queryParamsUnDecoded ?? "nil")
            CXLogUtil.d(TAG, "queryUnDecoded:key:params:", schemeUrl?.queryUnDecoded("params") ?? "nil")

            if (pathSegments.count >= 3) {
                let clazzName = classMap[pathSegments[1]] ?? pathSegments[1]
                let methodName = pathSegments[2]

                let hashcode:Int = Int((schemeUrl?.queryUnDecoded("hashcode")) ?? "-1") ?? -1
                let params = schemeUrl?.queryUnDecoded("params")

                let paramArray: [String] = params?.split(separator: ",").map {
                    $0.string?.urlDecoded ?? ""
                } ?? []

                CXLogUtil.d(TAG, "clazzName:\(clazzName) , methodName:\(methodName) , params:size:\(String(describing: paramArray.count)):(\(String(describing: paramArray)) , hashCode:\(String(describing: hashcode))")
                
                let result = callNativeMethod(className: clazzName, methodName: methodName, paramArray) ?? ""
                if (hashcode != -1) {
                    callJsFunction(webView, "javascript:window.hybird.onCallback(\(hashcode), '\(result)')")
                }
                return true
            } else {
                CXLogUtil.e(TAG, "schemaUrl:${schemeUrl.toString()} 格式定义错误，请参照 hybird://native/className/methodName?params=1,2,3,4,5&hashcode=123445")
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
        CXLogUtil.d(CXHybirdManager.TAG, "intercept message.body : \(String(describing: message.body))")
        CXLogUtil.d(CXHybirdManager.TAG, "intercept url : \(String(describing: schemeUrl?.absoluteString ?? ""))")
        CXLogUtil.d(CXHybirdManager.TAG, "intercept schemePrefix : \(schemePrefix)")
        CXLogUtil.d(CXHybirdManager.TAG, "intercept ? \(schemeMap.containsKey(schemePrefix))")
        return schemeMap[schemePrefix]?(webView, schemeUrl)
    }

    public static func callJsFunction(_ webView: WKWebView?, _ javascript: String, _ callback: ((_ result: String?) -> Void?)? = nil) {
        webView?.evaluateJavaScript(javascript.replace("javascript:", "")) { (any: Any?, error: Error?) in
            CXLogUtil.d(TAG, "any", (any ?? "nil"), "error", (error ?? "nil"))
            let result = error == nil ? (any as? String) : ("error:" + error.debugDescription)
            callback?(result)
        }
    }
}
