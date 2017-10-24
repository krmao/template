//
// Created by maokangren on 2017/10/24.
// Copyright (c) 2017 com.xixi. All rights reserved.
//

import Foundation
import UIKit

open class HKUIWebView: UIWebView, UIWebViewDelegate {

    var TAG = "[hybird]"
    

    init() {
        super.init(frame: CGRect(x: 0, y: 0, width: 0, height: 0))
        self.scalesPageToFit = true
        delegate = self
    }

    required public init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    @available(iOS 2.0, *)
    public func webView(_ webView: UIWebView, shouldStartLoadWith request: URLRequest, navigationType: UIWebViewNavigationType) -> Bool {
        HKLogUtil.d(TAG, "shouldStartLoadWith", "URL=\((request.url?.absoluteString ?? ""))", "path=\((request.url?.path ?? ""))")
        return true
    }

    @available(iOS 2.0, *)
    public func webViewDidStartLoad(_ webView: UIWebView) {
        HKLogUtil.d(TAG, "webViewDidStartLoad")
    }

    @available(iOS 2.0, *)
    public func webViewDidFinishLoad(_ webView: UIWebView) {
        let bodyBGColorString = webView.stringByEvaluatingJavaScript(from: "document.body.style.backgroundColor")
        HKLogUtil.d(TAG, "webViewDidFinishLoad","bodyColorString", bodyBGColorString)
        onGetBodyBackgroundListener?(bodyBGColorString)
    }
    
    private var onGetBodyBackgroundListener:(( _ bodyBGColorString : String? ) -> Void)? = nil
    
    public func setOnGetBodyBackgroundListener( onGetBodyBackgroundListener:(( _ bodyBGColorString : String? ) -> Void)? ){
        self.onGetBodyBackgroundListener = onGetBodyBackgroundListener
    }

    @available(iOS 2.0, *)
    public func webView(_ webView: UIWebView, didFailLoadWithError error: Error) {
        HKLogUtil.d(TAG, "didFailLoadWithError", error)
    }
}
