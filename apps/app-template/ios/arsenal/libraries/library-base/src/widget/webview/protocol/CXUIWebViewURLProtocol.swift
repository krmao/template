import Foundation

class CXUIWebViewURLProtocol: URLProtocol {

    private static let KEY = "CXUIWebViewURLProtocol_REQUEST_KEY"

    override class func canInit(with request: URLRequest) -> Bool {
        let url = request.url?.absoluteString

        if (url == nil || url!.isEmpty()) {
            CXLogUtil.d("URLProtocol canInit(false) end  : invalid url: \(url ?? "")")
            return false
        }

        let moduleManager: CXHybirdModuleManager? = CXHybird.getModule(url)

        if moduleManager == nil {
            CXLogUtil.d("URLProtocol canInit(false): moduleManager is nil isLocalFileExists:false url: \(url ?? "")")
            return false
        }
        if CXHybirdUtil.getLocalFile(moduleManager!.currentConfig, url)?.exists() != true {
            CXLogUtil.d("URLProtocol canInit(false): isLocalFileExists:false url: \(url ?? "")")
            return false
        }

        if (URLProtocol.property(forKey: CXUIWebViewURLProtocol.KEY, in: request) != nil) {
            CXLogUtil.d("URLProtocol canInit(false): isLocalFileExists:true, repeatRequest url: \(url ?? "")")
            return false
        }

        CXLogUtil.d("URLProtocol canInit(true): isLocalFileExists:true 即将调用本地资源 url: \(url ?? "")")
        return true
    }

    override func startLoading() {
        let urlString: String = self.request.url?.absoluteString ?? ""

        CXLogUtil.d("URLProtocol startLoading originUrl:" + (urlString))

        URLProtocol.setProperty(true, forKey: CXUIWebViewURLProtocol.KEY, in: self.request as! NSMutableURLRequest)

        let moduleManager: CXHybirdModuleManager? = CXHybird.getModule(self.request.url?.absoluteString)

        if moduleManager == nil {
            CXLogUtil.d("URLProtocol startLoading: moduleManager is nil urlString: \(urlString)")
            return
        }

        let localFile = CXHybirdUtil.getLocalFile(moduleManager!.currentConfig, urlString)

        if localFile?.exists() != true {
            CXLogUtil.d("URLProtocol startLoading: isLocalFileExists:false urlString: \(urlString)")
            return
        }

        let localFilePath: String = localFile!.path

        let mimeType: String

        if (localFilePath.contains(".css")) {
            mimeType = "text/css"
        } else if (localFilePath.contains(".png")) {
            mimeType = "image/png"
        } else if (localFilePath.contains(".js")) {
            mimeType = "application/x-javascript"
        } else if (localFilePath.contains(".woff")) {
            mimeType = "application/x-font-woff"
        } else if (localFilePath.contains(".html")) {
            mimeType = "text/html"
        } else if (localFilePath.contains(".shtml")) {
            mimeType = "text/html"
        } else {
            mimeType = "text/html"
        }

        CXLogUtil.d("URLProtocol startLoading: mimeType:\(mimeType) urlString: \(urlString) localFilePath:\(localFilePath)")
        guard let url = URL(string: localFilePath) else {
            CXLogUtil.d("URLProtocol startLoading: URL init failure ! return !!! urlString: \(urlString) localFilePath:\(localFilePath)")
            return
        }
        guard let data = NSData(contentsOfFile: localFilePath) else {
            CXLogUtil.d("URLProtocol startLoading: NSData init failure ! return !!! urlString: \(urlString) localFilePath:\(localFilePath)")
            return
        }
        let response = URLResponse(url: url, mimeType: mimeType, expectedContentLength: data.length, textEncodingName: "UTF-8")

        self.client?.urlProtocol(self, didReceive: response, cacheStoragePolicy: URLCache.StoragePolicy.notAllowed)
        self.client?.urlProtocol(self, didLoad: data as Data)
        self.client?.urlProtocolDidFinishLoading(self)

        CXLogUtil.d("URLProtocol startLoading: use local URLResponse success !!! urlString: \(urlString) localFilePath:\(localFilePath)")
    }

    override class func canonicalRequest(for request: URLRequest) -> URLRequest {
        return request
    }

    override func stopLoading() {
    }

}
