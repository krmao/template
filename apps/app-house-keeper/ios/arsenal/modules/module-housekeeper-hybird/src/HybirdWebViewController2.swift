import UIKit
import WebKit
import SnapKit

class HybirdWebViewController2: UIViewController, WKNavigationDelegate, WKScriptMessageHandler, WKUIDelegate {

    static func goTo(fromViewController: UIViewController, url: String) {
        let toViewController = HybirdWebViewController2.init(url: url)
        if let navigationController = fromViewController as? UINavigationController {
            navigationController.pushViewController(toViewController, animated: true)
        } else if fromViewController.navigationController != nil {
            fromViewController.navigationController?.pushViewController(toViewController, animated: true)
        } else {
            fromViewController.present(toViewController, animated: true)
        }
    }

    var indexPath: String = ""

    init(url: String) {
        super.init(nibName: nil, bundle: nil)
        self.indexPath = url
    }

    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
    }

    lazy var progressView: UIProgressView = {
        let _progressView = UIProgressView()
        _progressView.progressTintColor = UIColor.orange
        _progressView.trackTintColor = UIColor.white
        return _progressView
    }()

    lazy var webView: WKWebView = {
        let _webConfiguration = WKWebViewConfiguration()
        _webConfiguration.preferences.javaScriptEnabled = true
        _webConfiguration.selectionGranularity = WKSelectionGranularity.character
        //_webConfiguration.dataDetectorTypes=WKDataDetectorTypes.all //ios 10.0 自动检测电话、网址和邮箱
        //_webConfiguration.userContentController = WKUserContentController()
        //_webConfiguration.userContentController.add(self, name: "native") // html 直接 调用 native 方法
        var _webView = WKWebView.init(frame: CGRect(x: 0, y: 0, width: 0, height: 0), configuration: _webConfiguration)
        _webView.isMultipleTouchEnabled = true
        _webView.uiDelegate = self
        _webView.navigationDelegate = self
        _webView.allowsBackForwardNavigationGestures = true
        _webView.addObserver(self, forKeyPath: "estimatedProgress", options: .new, context: nil)
        return _webView
    }()

    lazy var barLeftItem: UIBarButtonItem = {
        var _barLeftItem = UIBarButtonItem.init(title: "返回", style: UIBarButtonItemStyle.done, target: self, action: #selector(barClickedEvents))
        return _barLeftItem
    }()
    lazy var barRightItem: UIBarButtonItem = {
        var _barRightItem = UIBarButtonItem.init(title: "测试", style: UIBarButtonItemStyle.done, target: self, action: #selector(barClickedEvents))
        return _barRightItem
    }()
    lazy var barRightItem1: UIBarButtonItem = {
        var _barRightItem1 = UIBarButtonItem.init(title: "登录", style: UIBarButtonItemStyle.done, target: self, action: #selector(barClickedEvents))
        return _barRightItem1
    }()

    override func viewDidLoad() {
        super.viewDidLoad()
        print("viewDidLoad")
        self.view.addSubview(webView)
        self.navigationController?.navigationBar.addSubview(progressView)
        self.navigationItem.leftBarButtonItem = barLeftItem //在此处 webView.canGoBack 已经是最新状态，可以加入逻辑判断
        self.navigationItem.rightBarButtonItems = [barRightItem, barRightItem1]
        self.navigationItem.title = "housekeeper"

        progressView.snp.makeConstraints { (make) in
            make.width.equalTo((self.navigationController?.navigationBar)!)
            make.height.equalTo(2)
            make.top.equalTo(44 - 2)
        }
        webView.snp.makeConstraints { (make) in
            make.width.equalTo(self.view)
            make.height.equalTo(self.view)
        }

        //== ios 11.0 以上 撑满底部 =============================================================
        self.navigationController?.navigationBar.isTranslucent = false
        if #available(iOS 11.0, *) {
            webView.scrollView.contentInsetAdjustmentBehavior = .never
        }
        //== ios 11.0 以上 撑满底部 =============================================================

        if (self.indexPath.starts(with: "file:///")) {
            let allowingReadAccessURL = URL(fileURLWithPath: self.indexPath).deletingLastPathComponent()
            let indexUrl = URL(fileURLWithPath: self.indexPath)
            self.webView.loadFileURL(indexUrl, allowingReadAccessTo: allowingReadAccessURL)
        } else {
            self.webView.load(URLRequest(url: URL(string: self.indexPath)!))
        }
    }

    @objc func barClickedEvents(barItem: UIBarButtonItem) {
        print("barClickedEvents")
        if (barItem == barLeftItem) {
            if (webView.canGoBack) {
                webView.goBack()
            } else {
                //exit(0)
            }
        } else {
        }
    }

    //[webview] protocol ===========================================================================
    
    
    public func userContentController(_ userContentController: WKUserContentController, didReceive message: WKScriptMessage) {
        print("[hybird]", "didReceive", "(类似于 android @JavaInterface)", "message.name:", message.name)
    }

    func webView(_ webview: WKWebView, decidePolicyFor navigationAction: WKNavigationAction, decisionHandler: @escaping (WKNavigationActionPolicy) -> Void) {
        print("[hybird]", "decidePolicyFor", "url=", navigationAction.request.url?.absoluteString.removingPercentEncoding ?? "null")
        decisionHandler(WKNavigationActionPolicy.allow)
    }

    func webView(_ webview: WKWebView, didStartProvisionalNavigation navigation: WKNavigation!) {
        print("[hybird]", "didStartProvisionalNavigation")
    }

    func webView(_ webview: WKWebView, didCommit navigation: WKNavigation!) {
        print("[hybird]", "didCommit")
    }
    
    public func webView(_ webView: WKWebView, didFinish navigation: WKNavigation!){
        print("[hybird]", "didFinish","title:", webView.title ?? "")
        self.navigationItem.title = webView.title
        self.title = webView.title
        progressView.setProgress(0.0, animated: false)
    }

    func webView(_ webview: WKWebView, didFail navigation: WKNavigation!, withError error: Error) {
        print("[hybird]", "didFail")
    }

    override func observeValue(forKeyPath keyPath: String?, of object: Any?, change: [NSKeyValueChangeKey: Any]?, context: UnsafeMutableRawPointer?) {
        if (keyPath == "estimatedProgress") {
            progressView.isHidden = webView.estimatedProgress == 1
            progressView.setProgress(Float(webView.estimatedProgress), animated: true)
        }
    }

    deinit {
        webView.removeObserver(self, forKeyPath: "estimatedProgress")
        progressView.reloadInputViews()
    }

    //[webview] protocol ===========================================================================
}
