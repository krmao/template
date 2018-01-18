import Foundation

class HKURLProtocol: URLProtocol {

    public static let HOST = "www.smarttemplate.com"

    private static let TAG = "[HKURLProtocol]"
    private static let KEY = TAG + "_REQUEST_KEY"
    private static let CLASS_STRING_BROWSING_CONTROLLER = "V0tCcm93c2luZ0NvbnRleHRDb250cm9sbGVy".base64Decoded?.string ?? ""
    private static let CLASS_STRING_REGISTER_SCHEME = "cmVnaXN0ZXJTY2hlbWVGb3JDdXN0b21Qcm90b2NvbDo=".base64Decoded?.string ?? ""
    private static let CLASS_STRING_UNREGISTER_SCHEME = "dW5yZWdpc3RlclNjaGVtZUZvckN1c3RvbVByb3RvY29sOg==".base64Decoded?.string ?? ""

    override class func canInit(with request: URLRequest) -> Bool {
        HKLogUtil.d()
        var canInit = request.url?.host == HKURLProtocol.HOST

        if !canInit {
            HKLogUtil.d(HKURLProtocol.TAG, "canInit:false, host:false, isLocalFileExists:false", "url:" + (request.url?.absoluteString ?? ""), "correctHost:\(HKURLProtocol.HOST)")
            return false
        }

        canInit = canInit && HKURLProtocol.isLocalFileExists(request)

        if !canInit {
            HKLogUtil.d(HKURLProtocol.TAG, "canInit:false, host:true, isLocalFileExists:false", "url:" + (request.url?.absoluteString ?? ""))
            return false
        }

        if (URLProtocol.property(forKey: HKURLProtocol.KEY, in: request) != nil) {
            canInit = false
            HKLogUtil.d(HKURLProtocol.TAG, "canInit:false, host:true, isLocalFileExists:true, repeatRequest", "url:" + (request.url?.absoluteString ?? ""))
            return canInit
        }

        HKLogUtil.d(HKURLProtocol.TAG, "canInit:false, host:true, isLocalFileExists:true", "url:" + (request.url?.absoluteString ?? ""))
        return canInit
    }


    class func getLocalFilePath(_ request: URLRequest) -> String {
        let localFilePath = String(HKBundleManager.INSTANCE.pathForHybirdDir.dropLast(1) + (request.url?.path ?? ""))
        return localFilePath
    }

    class func isLocalFileExists(_ request: URLRequest) -> Bool {
        return HKFileUtil.fileExists(HKURLProtocol.getLocalFilePath(request))
    }

    override class func canonicalRequest(for request: URLRequest) -> URLRequest {
        //HKLogUtil.d(HKURLProtocol.TAG, "canonicalRequest", request.url?.absoluteString ?? "")
        return request
    }

    override func startLoading() {
        HKLogUtil.d(HKURLProtocol.TAG, "startLoading", "originUrl:" + (self.request.url?.absoluteString ?? ""))

        URLProtocol.setProperty(true, forKey: HKURLProtocol.KEY, in: self.request as! NSMutableURLRequest)

        if self.request.url?.host == HKURLProtocol.HOST {
            //let routeIndex = request.url?.absoluteString.index(of: "#")
            //let route = routeIndex == nil ? "" : request.url?.absoluteString.substring(from: routeIndex!)

            let localFilePath = request.url?.absoluteString
                    .replace("https://" + HKURLProtocol.HOST, String(HKBundleManager.INSTANCE.pathForHybirdDir.dropLast(1)))
                    .replace("http://" + HKURLProtocol.HOST, String(HKBundleManager.INSTANCE.pathForHybirdDir.dropLast(1))) ?? ""
            //let localFilePath = String( HKBundleManager.INSTANCE.pathForHybirdDir.dropLast(1) + (self.request.url?.path ?? ""))
            HKLogUtil.d(HKURLProtocol.TAG, "startLoading", "localUrl:" + localFilePath)
            guard let url = URL(string: localFilePath) else {
                return
            }
            guard let data = NSData(contentsOfFile: localFilePath) else {
                return
            }
            let response = URLResponse(url: url, mimeType: "text/html", expectedContentLength: data.length, textEncodingName: "UTF-8")

            self.client?.urlProtocol(self, didReceive: response, cacheStoragePolicy: URLCache.StoragePolicy.notAllowed)
            self.client?.urlProtocol(self, didLoad: data as Data)
            self.client?.urlProtocolDidFinishLoading(self)
        }
    }

    override func stopLoading() {
        HKLogUtil.d(HKURLProtocol.TAG, "stopLoading", request.url?.absoluteString ?? "")
    }


    class func registerSchemeForWKWebView(_ schemes: String...) {
        if let cls = NSClassFromString(CLASS_STRING_BROWSING_CONTROLLER) as? NSObject.Type {
            let sel = Selector(CLASS_STRING_REGISTER_SCHEME)
            if cls.responds(to: sel) {
                for scheme in schemes {
                    cls.perform(sel, with: scheme)
                }
            }
        }
    }

    class func unregisterSchemeForWKWebView(_ schemes: String...) {
        if let cls = NSClassFromString(CLASS_STRING_BROWSING_CONTROLLER) as? NSObject.Type {
            let sel = Selector(CLASS_STRING_UNREGISTER_SCHEME)
            if cls.responds(to: sel) {
                for scheme in schemes {
                    cls.perform(sel, with: scheme)
                }
            }
        }
    }
}