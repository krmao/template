import Foundation

class CXWKWebViewURLProtocol: URLProtocol {

    private static let CLASS_STRING_BROWSING_CONTROLLER = "V0tCcm93c2luZ0NvbnRleHRDb250cm9sbGVy".base64Decoded?.string ?? ""
    private static let CLASS_STRING_REGISTER_SCHEME = "cmVnaXN0ZXJTY2hlbWVGb3JDdXN0b21Qcm90b2NvbDo=".base64Decoded?.string ?? ""
    private static let CLASS_STRING_UNREGISTER_SCHEME = "dW5yZWdpc3RlclNjaGVtZUZvckN1c3RvbVByb3RvY29sOg==".base64Decoded?.string ?? ""

    override class func canInit(with request: URLRequest) -> Bool {
        return false
    }

    override class func canonicalRequest(for request: URLRequest) -> URLRequest {
        return request
    }

    override func startLoading() {
    }

    override func stopLoading() {
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
