//
// Created by smart on 2017/10/24.
// Copyright (c) 2017 com.smart. All rights reserved.
//

import Foundation
import UIKit

open class CXUIWebView: UIWebView, UIWebViewDelegate {

    convenience init() {
        self.init(nil)
    }

    init(_ navigationController: UINavigationController?) {
        super.init(frame: CGRect(x: 0, y: 0, width: 0, height: 0))
        self.scalesPageToFit = true
        self.isMultipleTouchEnabled = true
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
        delegate = self
    }

    required public init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    @available(iOS 2.0, *)
    public func webView(_ webView: UIWebView, shouldStartLoadWith request: URLRequest, navigationType: UIWebViewNavigationType) -> Bool {
        let url: String? = request.url?.absoluteString

        CXLogUtil.d("webView:shouldStartLoadWith start->\(url ?? "")")

        if (url != nil && !url!.isEmpty()) {

            CXHybird.checkUpdate(sync: true, url!) {
                CXHybird.onWebViewOpenPage(self.hashCode(), url!)
                CXLogUtil.d("webView:shouldStartLoadWith middle->\(url!)")
                return Void()
            }

        }

        CXLogUtil.d("webView:shouldStartLoadWith end->\(url ?? "")")
        return true
    }

    @available(iOS 2.0, *)
    public func webViewDidStartLoad(_ webView: UIWebView) {
        CXLogUtil.d(TAG, "webViewDidStartLoad")
    }

    @available(iOS 2.0, *)
    public func webViewDidFinishLoad(_ webView: UIWebView) {
        let bodyBGColorString = webView.stringByEvaluatingJavaScript(from: "document.body.style.backgroundColor") ?? ""
        onGetBodyBackgroundListener?(bodyBGColorString)
    }

    private var onGetBodyBackgroundListener: ((_ bodyBGColorString: String?) -> Void)? = nil

    public func setOnGetBodyBackgroundListener(onGetBodyBackgroundListener: ((_ bodyBGColorString: String?) -> Void)?) {
        self.onGetBodyBackgroundListener = onGetBodyBackgroundListener
    }

    @available(iOS 2.0, *)
    public func webView(_ webView: UIWebView, didFailLoadWithError error: Error) {
        CXLogUtil.d(TAG, "didFailLoadWithError", error)
    }

    open func loadURL(_ url: String) {
        let start = System.currentTimeMillis()
        CXLogUtil.e(TAG, "loadURL start:\(url)")
        self.loadRequest(URLRequest(url: URL(string: url)!))
        CXLogUtil.e(TAG, "loadURL   end:\(url) , 耗时:\(System.currentTimeMillis() - start)ms")
    }

    /*open func loadURL(_ url: String) {
        let start = System.currentTimeMillis()
        CXLogUtil.e(TAG, "loadURL start:\(url)")
        CXHybird.checkUpdate(url) {
            CXHybird.onWebViewOpenPage(self.hashCode(), url)
            self.loadRequest(URLRequest(url: URL(string: url)!))
            return Void()
        }
        CXLogUtil.e(TAG, "loadURL   end:\(url) , 耗时:\(System.currentTimeMillis() - start)ms")
    }*/

    deinit {
        CXHybird.onWebViewClose(self.hashCode())
    }
}
