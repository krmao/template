import UIKit
import WebKit
import SnapKit
import SSZipArchive
import RxCocoa
import RxSwift

class UIWebViewController: UIViewController , WKNavigationDelegate, WKScriptMessageHandler, WKUIDelegate{
    let subscriptions = CompositeDisposable()
    
    lazy var progressView: UIProgressView = {
        let _progressView = UIProgressView()
        _progressView.progressTintColor = UIColor.orange
        _progressView.trackTintColor = UIColor.white
        return _progressView
    }()
    
    lazy var webView: WKWebView = {
        //[webview] config ===========================================================================
        let _webConfiguration=WKWebViewConfiguration()
        _webConfiguration.userContentController=WKUserContentController()
        _webConfiguration.userContentController.add(self, name: "back")
        _webConfiguration.preferences.javaScriptEnabled=true
        _webConfiguration.selectionGranularity=WKSelectionGranularity.character
        //_webConfiguration.dataDetectorTypes=WKDataDetectorTypes.all //ios 10.0 自动检测电话、网址和邮箱
        //[webview] config ===========================================================================
        
        var _webView=WKWebView.init(frame: CGRect(x:0,y:0,width:0,height:0), configuration: _webConfiguration)
        _webView.isMultipleTouchEnabled=true
        _webView.uiDelegate=self
        _webView.navigationDelegate=self
        _webView.allowsBackForwardNavigationGestures=true
        _webView.addObserver(self, forKeyPath: "estimatedProgress", options: .new, context: nil)
        return _webView
    }()
    
    lazy var barLeftItem:UIBarButtonItem={
        var _barLeftItem=UIBarButtonItem.init(title: "返回", style: UIBarButtonItemStyle.done, target: self, action: #selector(barClickedEvents))
        return _barLeftItem
    }()
    lazy var barRightItem:UIBarButtonItem={
        var _barRightItem=UIBarButtonItem.init(title: "测试", style: UIBarButtonItemStyle.done, target: self, action: #selector(barClickedEvents))
        return _barRightItem
    }()
    lazy var barRightItem1:UIBarButtonItem={
        var _barRightItem1=UIBarButtonItem.init(title: "登录", style: UIBarButtonItemStyle.done, target: self, action: #selector(barClickedEvents))
        return _barRightItem1
    }()
    
    @objc func barClickedEvents(barItem:UIBarButtonItem) {
        print("barClickedEvents")
        if(barItem==barLeftItem){
            if(webView.canGoBack){
                webView.goBack()
            }else{
                //exit(0)
            }
        }else{
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        print("viewDidLoad")
        self.view.addSubview(webView)
        self.navigationController?.navigationBar.addSubview(progressView)
        self.navigationItem.rightBarButtonItems=[barRightItem,barRightItem1]
        self.navigationItem.title="housekeeper"
        
        progressView.snp.makeConstraints { (make) in
            make.width.equalTo((self.navigationController?.navigationBar)!)
            make.height.equalTo(2)
            make.top.equalTo(44-2)
        }
        webView.snp.makeConstraints { (make) in
            make.width.equalTo(self.view)
            make.height.equalTo(self.view)
        }
        
        //== ios 11.0 以上 撑满底部 =============================================================
        self.navigationController?.navigationBar.isTranslucent = false
        if #available(iOS 11.0, *) {webView.scrollView.contentInsetAdjustmentBehavior = .never}
        //== ios 11.0 以上 撑满底部 =============================================================
        
        let docDirs = NSSearchPathForDirectoriesInDomains(.documentDirectory, .userDomainMask, true) as NSArray
        let docDir = docDirs[0] as! String
        
        let fileManager = FileManager.default
        let bundleZipPath = Bundle.main.path(forResource: "bundle", ofType: "zip", inDirectory: "assets") ?? ""
        print("[hybird]","bundleZipPath:",bundleZipPath)
        
        do {
            try fileManager.removeItem(atPath: docDir)
            print("[hybird]","remove dic success")
            let files = fileManager.subpaths(atPath: docDir)
            print("[hybird]","============================================")
            if((files) != nil){
                for file in files! {
                    print("[hybird]","file:",file)
                }
            }
            print("[hybird]","============================================")
        } catch  {
            print("[hybird]","remove dic failure")
        }
        
        _ = subscriptions.insert(Observable<Any>.create{ observer in
            let unzipResult = SSZipArchive.unzipFile(atPath: bundleZipPath, toDestination: docDir)
            print("[hybird]","unzipResult:",unzipResult)
            let files = fileManager.subpaths(atPath: docDir)
            print("[hybird]","============================================")
            for file in files! {
                print("[hybird]","file:",file)
            }
            print("[hybird]","============================================")
            observer.onNext(0)
            return Disposables.create()
            }
            .subscribeOn(MainScheduler.asyncInstance)
            .observeOn(MainScheduler.instance)
            .subscribe(
                onNext: {_ in
                    print("[hybird]","onNext")
                    let allowingReadAccessURL=URL(fileURLWithPath : docDir)
                    let indexPath=URL(fileURLWithPath : docDir+"/index.html")
                    self.webView.loadFileURL(indexPath, allowingReadAccessTo:allowingReadAccessURL)
            },
                onError: { error in
                    print("[hybird]","onError",error)
            },
                onCompleted: nil,
                onDisposed: nil
        ))
    }
    
    //[webview] protocol ===========================================================================
    public func userContentController(_ userContentController: WKUserContentController, didReceive message: WKScriptMessage) {
        print("[hybird] didReceive(类似于javaInterface)")
        //if (message.name == "和web那边一样的方法名") {
        //    print("JavaScript is sending a message \(message.body)")
        //}
    }
    
    func webView(_ webview:  WKWebView, decidePolicyFor navigationAction: WKNavigationAction, decisionHandler: @escaping (WKNavigationActionPolicy) -> Void) {
        print("[hybird]  decidePolicyFor:url=",navigationAction.request.url?.absoluteString.removingPercentEncoding ?? "null")
        //print("[hybird]  decidePolicyFor:host=",navigationAction.request.url?.host?.removingPercentEncoding ?? "null")
        decisionHandler(WKNavigationActionPolicy.allow)
        
        //        if(navigationAction.request.url != nil){
        //            switch navigationAction.request.url!.absoluteString {
        //            default:break;
        //            }
        //        }
        
        //if ("m.car.chexiang.com" == navigationAction.request.url?.host) {
        //    print("decidePolicyFor cancel")
        //    decisionHandler(WKNavigationActionPolicy.cancel)
        //} else {
        //    print("decidePolicyFor allow")
        //    decisionHandler(WKNavigationActionPolicy.allow)
        //}
    }
    
    
    func webView(_ webview:  WKWebView, didStartProvisionalNavigation navigation: WKNavigation!) {
        print("[hybird] didStartProvisionalNavigation")
    }
    
    func webView(webview:  WKWebView, didCommit navigation: WKNavigation!) {
        print("[hybird] didCommit")
        self.navigationItem.leftBarButtonItem=webView.canGoBack ?barLeftItem:nil //在此处 webView.canGoBack 已经是最新状态，可以加入逻辑判断
    }
    
    func webView(webview:  WKWebView, didFinish navigation: WKNavigation!) {
        print("[hybird] didFinish")
        self.navigationItem.title=webView.title
        self.title = webView.title
        progressView.setProgress(0.0, animated: false)
    }
    
    func webView(webview:  WKWebView, didFail navigation: WKNavigation!, withError error: Error) {
        print("[hybird] didFail")
    }
    
    override func observeValue(forKeyPath keyPath: String?, of object: Any?, change: [NSKeyValueChangeKey : Any]?, context: UnsafeMutableRawPointer?) {
        if (keyPath == "estimatedProgress") {
            progressView.isHidden = webView.estimatedProgress == 1
            progressView.setProgress(Float(webView.estimatedProgress), animated: true)
        }
    }
    
    deinit {
        webView.removeObserver(self, forKeyPath: "estimatedProgress")
        progressView.reloadInputViews()
        subscriptions.dispose()
    }
    //[webview] protocol ===========================================================================
}
