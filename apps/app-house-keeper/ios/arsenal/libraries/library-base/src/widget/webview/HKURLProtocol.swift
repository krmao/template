import Foundation

class HKURLProtocol: URLProtocol {

    private static let TAG = "[HKURLProtocol]"
    private static let KEY = TAG + "_REQUEST_KEY"
    public static let HOST = "www.smarttemplate.com"

    override class func canInit(with request: URLRequest) -> Bool {
         var canInit = request.url?.host == HKURLProtocol.HOST
        
        if (URLProtocol.property(forKey: HKURLProtocol.KEY, in: request) != nil) {
            canInit = false
            HKLogUtil.d()
            HKLogUtil.d(HKURLProtocol.TAG, "canInit:repeat", "url:"+(request.url?.absoluteString ?? ""),("request.url?.host:" + (request.url?.host ?? "")) + (" == HOST:" + HKURLProtocol.HOST) , canInit)
            return canInit
        }
        let localFilePath = request.url?.absoluteString
            .replace("http://", "")
            .replace("https://", "")
            .replace(HKURLProtocol.HOST + "/", HKBundleManager.INSTANCE.pathForHybirdDir)
            .replace(HKURLProtocol.HOST, HKBundleManager.INSTANCE.pathForHybirdDir) ?? ""
        HKLogUtil.d(HKURLProtocol.TAG, "startLoading", "localUrl:"+localFilePath)
        
        HKLogUtil.d(HKURLProtocol.TAG, "canInit", "url:"+(request.url?.absoluteString ?? ""),("request.url?.host:" + (request.url?.host ?? "")) + (" == HOST:" + HKURLProtocol.HOST) , canInit)
        return canInit
    }

    override class func canonicalRequest(for request: URLRequest) -> URLRequest {
        //HKLogUtil.d(HKURLProtocol.TAG, "canonicalRequest", request.url?.absoluteString ?? "")
        return request
    }

    override func startLoading() {
        HKLogUtil.d(HKURLProtocol.TAG, "startLoading", "originUrl:"+(self.request.url?.absoluteString ?? ""))

        URLProtocol.setProperty(true, forKey: HKURLProtocol.KEY, in: self.request as! NSMutableURLRequest)

        if self.request.url?.host == HKURLProtocol.HOST {
            let localFilePath = self.request.url?.absoluteString
                    .replace("http://", "")
                    .replace("https://", "")
                    .replace(HKURLProtocol.HOST + "/", HKBundleManager.INSTANCE.pathForHybirdDir)
                    .replace(HKURLProtocol.HOST, HKBundleManager.INSTANCE.pathForHybirdDir) ?? ""
            HKLogUtil.d(HKURLProtocol.TAG, "startLoading", "localUrl:"+localFilePath)
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
}
