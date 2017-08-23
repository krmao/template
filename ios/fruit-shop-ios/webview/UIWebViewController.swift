//
//  UIWebViewController.swift
//  fruit-shop-ios
//
//  Created by maokangren on 2017/7/25.
//  Copyright © 2017年 com.xixi. All rights reserved.
//

import UIKit
import WebKit
import SnapKit

class UIWebViewController: UIViewController , WKNavigationDelegate, WKScriptMessageHandler, WKUIDelegate{
    
    let indexUrl:String="http://damaitemai.techqing.com"
    
    lazy var progressView: UIProgressView = {
        let _progressView = UIProgressView()
        _progressView.progressTintColor = UIColor.orange
        _progressView.trackTintColor = UIColor.white
        return _progressView
    }()
    
    lazy var webView:WKWebView = {
        //[webview] config ===========================================================================
        let _webConfiguration=WKWebViewConfiguration()
        _webConfiguration.userContentController=WKUserContentController()
        //_webConfiguration.userContentController.add(self, name: "back")
        _webConfiguration.preferences.javaScriptEnabled=true
        _webConfiguration.selectionGranularity=WKSelectionGranularity.character
        //_webConfiguration.dataDetectorTypes=WKDataDetectorTypes.all //ios 10.0
        //[webview] config ===========================================================================
        
        var _webView=WKWebView.init(frame: CGRect(x:0,y:0,width:0,height:0), configuration: _webConfiguration)
        _webView.isMultipleTouchEnabled=true
        _webView.uiDelegate=self
        _webView.navigationDelegate=self
        _webView.allowsBackForwardNavigationGestures=true
        _webView.load(URLRequest(url: URL(string: self.indexUrl)!))
        _webView.addObserver(self, forKeyPath: "estimatedProgress", options: .new, context: nil)
        return _webView
    }()
    
    lazy var webView2:UIWebView = {
        var _webView2=UIWebView.init(frame: CGRect(x:0,y:0,width:320,height:480))
       
        return _webView2
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
    func barClickedEvents(barItem:UIBarButtonItem) {
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
        //self.navigationItem.rightBarButtonItems=[barRightItem,barRightItem1]
        //self.navigationItem.title="水果商店"
        
        progressView.snp.makeConstraints { (make) in
            make.width.equalTo((self.navigationController?.navigationBar)!)
            make.height.equalTo(2)
            make.top.equalTo(44-2)
        }

        webView.snp.makeConstraints { (make) in
            make.width.equalTo(self.view)
            make.height.equalTo(self.view)
        }
    }
    
    //[webview] protocol ===========================================================================
    public func userContentController(_ userContentController: WKUserContentController, didReceive message: WKScriptMessage) {
        print("webview:didReceive(类似于javaInterface)")
        //if (message.name == "和web那边一样的方法名") {
        //    print("JavaScript is sending a message \(message.body)")
        //}
    }
    
    func webView(_ webView: WKWebView, decidePolicyFor navigationAction: WKNavigationAction, decisionHandler: @escaping (WKNavigationActionPolicy) -> Void) {
        
        print("decidePolicyFor:url=",navigationAction.request.url ?? "null")
        print("decidePolicyFor:host=",navigationAction.request.url?.host ?? "null")
        decisionHandler(WKNavigationActionPolicy.allow)
        
        //if ("m.car.chexiang.com" == navigationAction.request.url?.host) {
        //    print("decidePolicyFor cancel")
        //    decisionHandler(WKNavigationActionPolicy.cancel)
        //} else {
        //    print("decidePolicyFor allow")
        //    decisionHandler(WKNavigationActionPolicy.allow)
        //}
    }
    
    func webView(_ webView: WKWebView, didStartProvisionalNavigation navigation: WKNavigation!) {
        print("webview:didStartProvisionalNavigation")
    }
    
    func webView(_ webView: WKWebView, didCommit navigation: WKNavigation!) {
        print("webview:didCommit")
        self.navigationItem.leftBarButtonItem=webView.canGoBack ?barLeftItem:nil //在此处 webView.canGoBack 已经是最新状态，可以加入逻辑判断
    }
    
    func webView(_ webView: WKWebView, didFinish navigation: WKNavigation!) {
        print("webview:didFinish")
        self.title = webView.title
        progressView.setProgress(0.0, animated: false)
    }
    
    func webView(_ webView: WKWebView, didFail navigation: WKNavigation!, withError error: Error) {
        print("webview:didFail")
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
    }
    //[webview] protocol ===========================================================================
    
}


