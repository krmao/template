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
    let HOME_URL: String = "http://damaitemai.techqing.com/mobile/index.php"
    let HOME_CATEGORY_URL: String = "http://damaitemai.techqing.com/mobile/index.php?m=category"
    let HOME_SEARCH_URL: String = "http://damaitemai.techqing.com/mobile/index.php?m=search"
    let HOME_CART_URL: String = "http://damaitemai.techqing.com/mobile/index.php?m=cart"
    let HOME_USER_URL: String = "http://damaitemai.techqing.com/mobile/index.php?m=user"
    

    let URL_PREFIX = "http://damaitemai.techqing.com/mobile/index.php?"
    
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
        //_webView.load(URLRequest(url: URL(string: self.indexUrl)!))
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
    
    lazy var btnHome:UIButtonWithImage = {
        var _btnHome=UIButtonWithImage.init(frame: CGRect(x:0,y:0,width:0,height:0))
        _btnHome.setImage(UIImage.init(named: "home_normal"), for: UIControlState.normal)
        _btnHome.setImage(UIImage.init(named: "home_pressed"), for: UIControlState.selected)
        _btnHome.setTitle("首页", for: UIControlState.normal)
        _btnHome.titleLabel?.font=UIFont.systemFont(ofSize: 12)
        _btnHome.setTitleColor(UIColor.gray, for: UIControlState.normal)
        _btnHome.setTitleColor(UIColor.red, for: UIControlState.selected)
        _btnHome.addTarget(self, action: #selector(itemClicked), for: UIControlEvents.touchUpInside)
        return _btnHome
    }()
    lazy var btnCategory:UIButtonWithImage = {
        var _btnHome=UIButtonWithImage.init(frame: CGRect(x:0,y:0,width:0,height:0))
        _btnHome.setImage(UIImage.init(named: "category_normal"), for: UIControlState.normal)
        _btnHome.setImage(UIImage.init(named: "category_pressed"), for: UIControlState.selected)
        _btnHome.setTitle("分类", for: UIControlState.normal)
        _btnHome.titleLabel?.font=UIFont.systemFont(ofSize: 12)
        _btnHome.setTitleColor(UIColor.gray, for: UIControlState.normal)
        _btnHome.setTitleColor(UIColor.red, for: UIControlState.selected)
        _btnHome.addTarget(self, action: #selector(itemClicked), for: UIControlEvents.touchUpInside)
        return _btnHome
    }()
    lazy var btnSearch:UIButtonWithImage = {
        var _btnHome=UIButtonWithImage.init(frame: CGRect(x:0,y:0,width:0,height:0))
        _btnHome.setImage(UIImage.init(named: "search_normal"), for: UIControlState.normal)
        _btnHome.setImage(UIImage.init(named: "search_pressed"), for: UIControlState.selected)
        _btnHome.setTitle("搜索", for: UIControlState.normal)
        _btnHome.titleLabel?.font=UIFont.systemFont(ofSize: 12)
        _btnHome.setTitleColor(UIColor.gray, for: UIControlState.normal)
        _btnHome.setTitleColor(UIColor.red, for: UIControlState.selected)
        _btnHome.addTarget(self, action: #selector(itemClicked), for: UIControlEvents.touchUpInside)
        return _btnHome
    }()
    lazy var btnCart:UIButtonWithImage = {
        var _btnHome=UIButtonWithImage.init(frame: CGRect(x:0,y:0,width:0,height:0))
        _btnHome.setImage(UIImage.init(named: "cart_normal"), for: UIControlState.normal)
        _btnHome.setImage(UIImage.init(named: "cart_pressed"), for: UIControlState.selected)
        _btnHome.setTitle("购物车", for: UIControlState.normal)
        _btnHome.titleLabel?.font=UIFont.systemFont(ofSize: 12)
        _btnHome.setTitleColor(UIColor.gray, for: UIControlState.normal)
        _btnHome.setTitleColor(UIColor.red, for: UIControlState.selected)
        _btnHome.addTarget(self, action: #selector(itemClicked), for: UIControlEvents.touchUpInside)
        return _btnHome
    }()
    lazy var btnMine:UIButtonWithImage = {
        var _btnHome=UIButtonWithImage.init(frame: CGRect(x:0,y:0,width:0,height:0))
        _btnHome.setImage(UIImage.init(named: "mine_normal"), for: UIControlState.normal)
        _btnHome.setImage(UIImage.init(named: "mine_pressed"), for: UIControlState.selected)
        _btnHome.setTitle("我", for: UIControlState.normal)
        _btnHome.titleLabel?.font=UIFont.systemFont(ofSize: 12)
        _btnHome.setTitleColor(UIColor.gray, for: UIControlState.normal)
        _btnHome.setTitleColor(UIColor.red, for: UIControlState.selected)
        _btnHome.addTarget(self, action: #selector(itemClicked), for: UIControlEvents.touchUpInside)
        return _btnHome
    }()
    
    func itemClicked(button:UIButtonWithImage ) {
        changeItemStatus(button: button)
        
        switch button {
        case btnHome:
            self.webView.load(URLRequest(url: URL(string: HOME_URL)!))
            break
        case btnCategory:
            self.webView.load(URLRequest(url: URL(string: HOME_CATEGORY_URL)!))
            break
        case btnSearch:
            self.webView.load(URLRequest(url: URL(string: HOME_SEARCH_URL)!))
            break
        case btnCart:
            self.webView.load(URLRequest(url: URL(string: HOME_CART_URL)!))
            break
        case btnMine:
            self.webView.load(URLRequest(url: URL(string: HOME_USER_URL)!))
            break
        default:
            self.webView.load(URLRequest(url: URL(string: HOME_URL)!))
            break
        }
    }
    
    func changeItemStatus(button:UIButtonWithImage ){
        btnHome.isSelected=false
        btnCategory.isSelected=false
        btnSearch.isSelected=false
        btnCart.isSelected=false
        btnMine.isSelected=false
        
        switch button {
        case btnHome:
            btnHome.isSelected=true
            break
        case btnCategory:
            btnCategory.isSelected=true
            break
        case btnSearch:
            btnSearch.isSelected=true
            break
        case btnCart:
            btnCart.isSelected=true
            break
        case btnMine:
            btnMine.isSelected=true
            break
        default:
            break
        }
    }

    let barHeight:Int=46+14

    override func viewDidLoad() {
        super.viewDidLoad()
        print("viewDidLoad")
        self.view.addSubview(webView)
        self.view.addSubview(btnHome)
        self.view.addSubview(btnCategory)
        self.view.addSubview(btnSearch)
        self.view.addSubview(btnCart)
        self.view.addSubview(btnMine)
        self.navigationController?.navigationBar.addSubview(progressView)
        //self.navigationItem.rightBarButtonItems=[barRightItem,barRightItem1]
        //self.navigationItem.title="水果商店"
        
        btnHome.sendActions(for: UIControlEvents.touchUpInside)
        
        progressView.snp.makeConstraints { (make) in
            make.width.equalTo((self.navigationController?.navigationBar)!)
            make.height.equalTo(2)
            make.top.equalTo(44-2)
        }

        webView.snp.makeConstraints { (make) in
            make.width.equalTo(self.view)
            make.height.equalTo(self.view)
        }

        btnHome.snp.makeConstraints { (make) in
            make.width.equalTo(self.view).multipliedBy(0.2)
            make.height.equalTo(barHeight)
            make.bottom.equalTo(self.view)
            make.left.equalTo(self.view)
        }
        btnCategory.snp.makeConstraints { (make) in
            make.width.equalTo(btnHome.snp.width)
            make.height.equalTo(btnHome)
            make.bottom.equalTo(btnHome)
            make.left.equalTo(btnHome.snp.right)
        }
        btnSearch.snp.makeConstraints { (make) in
            make.width.equalTo(btnHome.snp.width)
            make.height.equalTo(btnHome)
            make.bottom.equalTo(btnHome)
            make.left.equalTo(btnCategory.snp.right)
        }
        btnCart.snp.makeConstraints { (make) in
            make.width.equalTo(btnHome.snp.width)
            make.height.equalTo(btnHome)
            make.bottom.equalTo(btnHome)
            make.left.equalTo(btnSearch.snp.right)
        }
        btnMine.snp.makeConstraints { (make) in
            make.width.equalTo(btnHome.snp.width)
            make.height.equalTo(btnHome)
            make.bottom.equalTo(btnHome)
            make.left.equalTo(btnCart.snp.right)
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
        
        print("decidePolicyFor:url=",navigationAction.request.url?.absoluteString.removingPercentEncoding ?? "null")
        print("decidePolicyFor:host=",navigationAction.request.url?.host?.removingPercentEncoding ?? "null")
        decisionHandler(WKNavigationActionPolicy.allow)
        
        if(navigationAction.request.url != nil){
            switch navigationAction.request.url!.absoluteString {
            case HOME_URL:
                changeItemStatus(button: btnHome)
                break
            case HOME_CATEGORY_URL:
                changeItemStatus(button: btnCategory)
                break
            case HOME_SEARCH_URL:
                changeItemStatus(button: btnSearch)
                break
            case HOME_CART_URL:
                changeItemStatus(button: btnCart)
                break
            case HOME_USER_URL:
                changeItemStatus(button: btnMine)
                break
            case "http://damaitemai.techqing.com/mobile/":
                 changeItemStatus(button: btnHome)
            default:
                break
            }
        }
        
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



