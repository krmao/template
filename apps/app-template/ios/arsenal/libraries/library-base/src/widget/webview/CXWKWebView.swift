import UIKit
import WebKit
import SnapKit

open class CXWKWebView: WKWebView, WKNavigationDelegate, WKScriptMessageHandler, WKUIDelegate {

    let TAG: String = "\(type(of: self))"

    private var navigationController: UINavigationController? = nil

    private lazy var progressView: UIProgressView = {
        let _progressView = UIProgressView()
        _progressView.progressTintColor = UIColor.orange
        _progressView.trackTintColor = UIColor.white
        return _progressView
    }()

    required public init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    convenience init() {
        self.init(nil)
    }

    init(_ navigationController: UINavigationController?) {
        self.navigationController = navigationController
        let _webConfiguration = WKWebViewConfiguration()
        _webConfiguration.preferences.javaScriptEnabled = true
        _webConfiguration.selectionGranularity = WKSelectionGranularity.character
        //_webConfiguration.dataDetectorTypes=WKDataDetectorTypes.all //ios 10.0 自动检测电话、网址和邮箱
        //_webConfiguration.userContentController = WKUserContentController()
        //_webConfiguration.userContentController.add(self, name: "native") // html 直接 调用 native 方法

        super.init(frame: CGRect(x: 0, y: 0, width: 0, height: 0), configuration: _webConfiguration)

        _webConfiguration.userContentController.add(self, name: "native")

        self.isMultipleTouchEnabled = true
        self.uiDelegate = self
        self.navigationDelegate = self
        self.allowsBackForwardNavigationGestures = true
        self.addObserver(self, forKeyPath: "estimatedProgress", options: .new, context: nil)

        //== ios 11.0 以上 撑满底部 =============================================================
        // 适配ios11 navigationBar 隐藏的情况下
        // self.navigationController?.navigationBar.isTranslucent = false
        // self.webView.scrollView.contentInsetAdjustmentBehavior = .never
        // and set webView.backgroundColor after webViewDidFinishLoad

        // 适配ios11 navigationBar 显示的情况下
        // self.navigationController?.navigationBar.isTranslucent = false
        // self.webView.scrollView.contentInsetAdjustmentBehavior = .never
        if #available(iOS 11.0, *) {
            self.scrollView.contentInsetAdjustmentBehavior = .never
        }
        navigationController?.navigationBar.isTranslucent = false
        navigationController?.navigationBar.addSubview(self.progressView)
        //== navigationBar.isHidden:true 状态栏颜色及 marginTop =================================
        /**
         if navigationController?.navigationBar.isHidden ?? false {
         self.scrollView.contentInset = UIEdgeInsets(top: CXSystemUtil.statusBarHeight, left: 0, bottom: 0, right: 0)
         self.backgroundColor = .white
         self.setOnGetBodyBackgroundListener { ( _ bodyBGColorString: String? ) -> Void in
         CXLogUtil.d(self.TAG, "onGetBodyColorString", bodyBGColorString ?? "")
         self.backgroundColor = UIColor.init(name: bodyBGColorString ?? "") ?? .white
         }
         }
         */
        //== navigationBar.isHidden:true 状态栏颜色及 marginTop =================================
        //== ios 11.0 以上 撑满底部 =============================================================

        self.progressView.snp.makeConstraints { (make) in
            make.width.equalTo((navigationController?.navigationBar)!)
            make.height.equalTo(2)
            make.top.equalTo(44 - 2)
        }
    }

    public func load(_ indexPath: String) {
        if (indexPath.starts(with: "file:///")) {
            let allowingReadAccessURL = URL(fileURLWithPath: indexPath).deletingLastPathComponent()
            let indexUrl = URL(fileURLWithPath: indexPath)
            self.loadFileURL(indexUrl, allowingReadAccessTo: allowingReadAccessURL)
        } else {
            self.load(URLRequest(url: URL(string: indexPath)!))
        }
    }

    public func userContentController(_ userContentController: WKUserContentController, didReceive message: WKScriptMessage) {
        CXLogUtil.d(TAG, "didReceive \(message.name) \(message.body)")
        _ = CXHybirdBridge.shouldOverrideUrlLoading(self, userContentController, message)
    }

    public func webView(_ webview: WKWebView, decidePolicyFor navigationAction: WKNavigationAction, decisionHandler: @escaping (WKNavigationActionPolicy) -> Void) {
        CXLogUtil.d(TAG, "decidePolicyFor url= \(navigationAction.request.url?.absoluteString ?? "nil")")
        decisionHandler(WKNavigationActionPolicy.allow)
    }

    public func webView(_ webview: WKWebView, didStartProvisionalNavigation navigation: WKNavigation!) {
        CXLogUtil.d(TAG, "didStartProvisionalNavigation")
    }

    public func webView(_ webview: WKWebView, didCommit navigation: WKNavigation!) {
        CXLogUtil.d(TAG, "didCommit")
    }

    public func webView(_ webView: WKWebView, didFinish navigation: WKNavigation!) {
        CXLogUtil.d(TAG, "didFinish title:\(webView.title)")
        self.navigationController?.title = webView.title
        self.progressView.setProgress(0.0, animated: false)

        CXHybirdBridge.callJsFunction(self, "console.log('hello html')") { result in
            CXLogUtil.d("hybird", "onResultCallback \(result)")
        }
    }

    public func webView(_ webview: WKWebView, didFail navigation: WKNavigation!, withError error: Error) {
        CXLogUtil.d(TAG, "didFail")
    }

    override open func observeValue(forKeyPath keyPath: String?, of object: Any?, change: [NSKeyValueChangeKey: Any]?, context: UnsafeMutableRawPointer?) {
        if (keyPath == "estimatedProgress") {
            let newProgress = (change?[.newKey] as AnyObject).floatValue ?? 0
            let oldProgress = (change?[.oldKey] as AnyObject).floatValue ?? 0
            CXLogUtil.d(TAG, "observeValue: newProgress: \(newProgress), oldProgress:\(oldProgress)")

            if (newProgress >= 1) {
                self.progressView.isHidden = true
                self.progressView.setProgress(0, animated: false)
            } else if (newProgress > oldProgress) {
                self.progressView.isHidden = false
                self.progressView.setProgress(Float(self.estimatedProgress), animated: true)
            }
        }
    }

    deinit {
        self.removeObserver(self, forKeyPath: "estimatedProgress")
        self.progressView.reloadInputViews()
    }
}
