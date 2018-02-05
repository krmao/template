import Foundation

class CXUIWebViewURLProtocol: URLProtocol {

    private static let KEY = "CXUIWebViewURLProtocol_REQUEST_KEY"

    override class func canInit(with request: URLRequest) -> Bool {
        let url = request.url?.absoluteString
        CXLogUtil.i("canInit(false) start: url: \(url ?? "")")

        if (url == nil || url!.isEmpty()) {
            CXLogUtil.i("canInit(false) end  : invalid url: \(url ?? "")")
            return false
        }

        let moduleManager: CXHybirdModuleManager? = CXHybird.getModule(url)

        if moduleManager == nil {
            CXLogUtil.i("canInit(false) end  : moduleManager is nil isLocalFileExists:false url: \(url ?? "")")
            return false
        }
        if CXHybirdUtil.getLocalFile(moduleManager!.currentConfig, url)?.exists() != true {
            CXLogUtil.i("canInit(false) end  : isLocalFileExists:false url: \(url ?? "")")
            return false
        }

        if (URLProtocol.property(forKey: CXUIWebViewURLProtocol.KEY, in: request) != nil) {
            CXLogUtil.i("canInit(false) end  : isLocalFileExists:true, repeatRequest url: \(url ?? "")")
            return false
        }

        CXLogUtil.i("canInit(true) end  : isLocalFileExists:true 即将调用本地资源 url: \(url ?? "")")
        return true
    }


    class func getLocalFilePath(_ request: URLRequest) -> String {
        let localFilePath = String(CXBundleManager.INSTANCE.pathForHybirdDir.dropLast(1) + (request.url?.path ?? ""))
        return localFilePath
    }

    class func isLocalFileExists(_ request: URLRequest) -> Bool {
        return CXFileUtil.fileExists(CXUIWebViewURLProtocol.getLocalFilePath(request))
    }

    override class func canonicalRequest(for request: URLRequest) -> URLRequest {
        //CXLogUtil.d(CXURLProtocol.TAG, "canonicalRequest", request.url?.absoluteString ?? "")
        return request
    }

    override func startLoading() {
        CXLogUtil.d("startLoading originUrl:" + (self.request.url?.absoluteString ?? ""))

        URLProtocol.setProperty(true, forKey: CXUIWebViewURLProtocol.KEY, in: self.request as! NSMutableURLRequest)

        if self.request.url?.host == "" {
            //let routeIndex = request.url?.absoluteString.index(of: "#")
            //let route = routeIndex == nil ? "" : request.url?.absoluteString.substring(from: routeIndex!)

            let localFilePath = request.url?.absoluteString
//                    .replace("https://" + CXUIWebViewURLProtocol.HOST, String(CXBundleManager.INSTANCE.pathForHybirdDir.dropLast(1)))
//                    .replace("http://" + CXUIWebViewURLProtocol.HOST, String(CXBundleManager.INSTANCE.pathForHybirdDir.dropLast(1))) ?? ""
            //let localFilePath = String( CXBundleManager.INSTANCE.pathForHybirdDir.dropLast(1) + (self.request.url?.path ?? ""))
            CXLogUtil.d("startLoading localUrl:\(localFilePath)")
            guard let url = URL(string: localFilePath!) else {
                return
            }
            guard let data = NSData(contentsOfFile: localFilePath!) else {
                return
            }
            let response = URLResponse(url: url, mimeType: "text/html", expectedContentLength: data.length, textEncodingName: "UTF-8")

            self.client?.urlProtocol(self, didReceive: response, cacheStoragePolicy: URLCache.StoragePolicy.notAllowed)
            self.client?.urlProtocol(self, didLoad: data as Data)
            self.client?.urlProtocolDidFinishLoading(self)
        }
    }

    override func stopLoading() {
    }


}
