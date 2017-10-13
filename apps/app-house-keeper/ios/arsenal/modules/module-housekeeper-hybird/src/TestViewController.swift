//
//  TestViewController.swift
//  housekeeper
//
//  Created by xiayiyong on 2017/10/13.
//  Copyright © 2017年 com.xixi. All rights reserved.
//

import UIKit
import WebKit
import SnapKit

class TestViewController: UIViewController {
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Do any additional setup after loading the view.
        
        
        
        self.navigationItem.leftBarButtonItem = barLeftItem
        
        self.view.addSubview(webView)
        
        self.automaticallyAdjustsScrollViewInsets = false
        
        if #available(iOS 11.0, *) {
            
            webView.scrollView.contentInsetAdjustmentBehavior =  .never
        }
        
        //        webView.snp.makeConstraints { (make) in
        //            make.bottom.equalTo(self.view)
        //            make.right.equalTo(self.view)
        //            make.top.equalTo(self.view)
        //            make.left.equalTo(self.view)
        //        }
        
        self.webView.load(URLRequest(url: URL(string: "https://www.baidu.com")!))
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
    lazy var webView: WKWebView = {
        let _webConfiguration = WKWebViewConfiguration()
        _webConfiguration.preferences.javaScriptEnabled = true
        _webConfiguration.selectionGranularity = WKSelectionGranularity.character
        //_webConfiguration.dataDetectorTypes=WKDataDetectorTypes.all //ios 10.0 自动检测电话、网址和邮箱
        //_webConfiguration.userContentController = WKUserContentController()
        //_webConfiguration.userContentController.add(self, name: "native") // html 直接 调用 native 方法
        var _webView = WKWebView(frame: self.view.bounds, configuration: _webConfiguration)
        //_webView.isMultipleTouchEnabled = true
        //_webView.uiDelegate = self
        //_webView.navigationDelegate = self
        _webView.allowsBackForwardNavigationGestures = true
        //_webView.addObserver(self, forKeyPath: "estimatedProgress", options: .new, context: nil)
        return _webView
    }()
    
    lazy var barLeftItem: UIBarButtonItem = {
        var _barLeftItem = UIBarButtonItem.init(title: "返回", style: UIBarButtonItemStyle.done, target: self, action: #selector(barClickedEvents))
        return _barLeftItem
    }()
    
    @objc func barClickedEvents(barItem: UIBarButtonItem) {
        print("barClickedEvents")
        if (webView.canGoBack) {
            webView.goBack()
        } else {
            
        }
    }
    
}

