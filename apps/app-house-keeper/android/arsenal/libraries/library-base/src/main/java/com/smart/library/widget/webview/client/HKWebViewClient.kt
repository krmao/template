package com.smart.library.widget.webview.client

import android.annotation.TargetApi
import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Build
import android.os.Message
import android.webkit.*
import com.smart.library.util.HKLogUtil
import com.smart.library.util.hybird.HKHybirdBridge

/*

android 17 以下存在的 安全问题
function addJsHack(cmdArgs){
    for (var obj in window) {
        try {
            if ("getClass" in window[obj]) {
                try {
                    window[obj].getClass().forName("java.lang.Runtime").
                        getMethod("getRuntime", null).invoke(null, null).exec(cmdArgs);;
                } catch (e) {
                }
            }
        } catch (e) {
        }
    }
}
addJsHack("")

 */
@Suppress("unused", "OverridingDeprecatedMember", "DEPRECATION", "RedundantOverride", "UseExpressionBody")
open class HKWebViewClient : WebViewClient() {

    /**
     * 针对 https 证书校验可以在此拦截通过HttpsURLConnection实现请求验证
     */
    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        HKLogUtil.d(HKHybirdBridge.TAG, "shouldOverrideUrlLoading: $url")
        return HKHybirdBridge.shouldOverrideUrlLoading(view, url) ?: super.shouldOverrideUrlLoading(view, url)
    }

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        HKLogUtil.d(HKHybirdBridge.TAG, "onPageStarted: $url")
        super.onPageStarted(view, url, favicon)
    }

    override fun doUpdateVisitedHistory(view: WebView?, url: String?, isReload: Boolean) {
        HKLogUtil.d(HKHybirdBridge.TAG, "doUpdateVisitedHistory(isReload=$isReload)): $url")
        super.doUpdateVisitedHistory(view, url, isReload)
    }

    override fun onPageFinished(webView: WebView?, url: String?) {
        HKLogUtil.d(HKHybirdBridge.TAG, "onPageFinished: $url")
        super.onPageFinished(webView, url)
    }

    override fun onLoadResource(view: WebView?, url: String?) {
        //HKLogUtil.v(HKHybirdBridge.TAG, "onLoadResource: $url")
        super.onLoadResource(view, url)
    }

    override fun onPageCommitVisible(view: WebView?, url: String?) {
        HKLogUtil.d(HKHybirdBridge.TAG, "onPageCommitVisible: $url")
        super.onPageCommitVisible(view, url)
    }

    override fun onFormResubmission(view: WebView?, dontResend: Message?, resend: Message?) {
        HKLogUtil.d(HKHybirdBridge.TAG, "onPageCommitVisible: dontResend=$dontResend , resend=$resend")
        super.onFormResubmission(view, dontResend, resend)
    }

    /**
     * android 5.0 以上 webView 再次校验 https 证书
     */
    override fun onReceivedClientCertRequest(view: WebView?, request: ClientCertRequest?) {
        HKLogUtil.w(HKHybirdBridge.TAG, "onReceivedClientCertRequest: $request")
        super.onReceivedClientCertRequest(view, request)
    }

    override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
        HKLogUtil.w(HKHybirdBridge.TAG, "onReceivedError: $error")
        super.onReceivedError(view, request, error)
    }

    override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
        HKLogUtil.w(HKHybirdBridge.TAG, "onReceivedSslError:proceed: $error")
        //避免调用proceed忽略证书验证错误信息继续加载页面
        handler?.cancel()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest?): WebResourceResponse? {
        //HKLogUtil.v(HKHybirdBridge.TAG, "shouldInterceptRequest:>=LOLLIPOP: ${request?.url?.toString()}")
        return shouldInterceptRequest(view, request?.url?.toString())
    }

    override fun shouldInterceptRequest(view: WebView?, url: String?): WebResourceResponse? {
        HKLogUtil.d(HKHybirdBridge.TAG, "shouldInterceptRequest: $url")
        return HKHybirdBridge.shouldInterceptRequest(view, url) ?: super.shouldInterceptRequest(view, url)
    }
}
