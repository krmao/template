package com.smart.library.widget.webview.client

import android.annotation.TargetApi
import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Build
import android.webkit.*
import com.smart.library.util.HKLogUtil
import com.smart.library.util.hybird.HKHybirdManager

@Suppress("unused", "OverridingDeprecatedMember", "DEPRECATION", "RedundantOverride")
open class HKWebViewClient : WebViewClient() {

    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        HKLogUtil.v(HKHybirdManager.TAG, "shouldOverrideUrlLoading: $url")
        return HKHybirdManager.shouldOverrideUrlLoading(view, url) ?: super.shouldOverrideUrlLoading(view, url)
    }

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        HKLogUtil.v(HKHybirdManager.TAG, "onPageStarted: $url")
        super.onPageStarted(view, url, favicon)
    }

    override fun onPageFinished(webView: WebView?, url: String?) {
        HKLogUtil.v(HKHybirdManager.TAG, "onPageFinished: $url")
        super.onPageFinished(webView, url)
    }

    override fun onLoadResource(view: WebView?, url: String?) {
        HKLogUtil.v(HKHybirdManager.TAG, "onLoadResource: $url")
        super.onLoadResource(view, url)
    }

    override fun onPageCommitVisible(view: WebView?, url: String?) {
        HKLogUtil.v(HKHybirdManager.TAG, "onPageCommitVisible: $url")
        super.onPageCommitVisible(view, url)
    }

    override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
        HKLogUtil.w(HKHybirdManager.TAG, "onReceivedError: $error")
        super.onReceivedError(view, request, error)
    }

    override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
        HKLogUtil.w(HKHybirdManager.TAG, "onReceivedSslError:proceed: $error")
        handler?.proceed()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest?): WebResourceResponse? {
        HKLogUtil.v(HKHybirdManager.TAG, "shouldInterceptRequest:>=LOLLIPOP: ${request?.url?.toString()}")
        return shouldInterceptRequest(view, request?.url?.toString())
    }

    override fun shouldInterceptRequest(view: WebView?, url: String?): WebResourceResponse? {
        HKLogUtil.v(HKHybirdManager.TAG, "shouldInterceptRequest:<LOLLIPOP $url")
        return HKHybirdManager.shouldInterceptRequest(view, url) ?: super.shouldInterceptRequest(view, url)
    }
}
