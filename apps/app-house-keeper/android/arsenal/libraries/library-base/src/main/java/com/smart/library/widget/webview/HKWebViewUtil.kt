package com.smart.library.widget.webview

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.text.TextUtils
import android.webkit.*
import com.smart.library.base.HKBaseApplication
import com.smart.library.base.HKConfig
import com.smart.library.widget.webview.client.HKWebChromeClient


@Suppress("unused", "MemberVisibilityCanPrivate")
object HKWebViewUtil {
    fun getCompleteHttpUrl(url: String): String {
        var completeUrl = url
        if (!TextUtils.isEmpty(url) && !completeUrl.startsWith("http"))
            completeUrl = "http://" + completeUrl
        return completeUrl
    }

    fun getCompleteHttpsUrl(url: String): String {
        var completeUrl = url
        if (!TextUtils.isEmpty(url) && !completeUrl.startsWith("http"))
            completeUrl = "https://" + completeUrl
        return completeUrl
    }

    fun initWebView(webView: WebView?, userAgent: String) {
        if (webView != null) {
            webView.settings.userAgentString = userAgent
            initWebView(webView, userAgent)
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun initWebView(webView: WebView?) {
        if (webView != null) {
            //webView.settings.userAgentString
            webView.settings.useWideViewPort = true
            webView.settings.databaseEnabled = true
            webView.settings.domStorageEnabled = true
            webView.settings.allowContentAccess = true
            webView.settings.builtInZoomControls = false
            webView.settings.loadWithOverviewMode = false
            webView.settings.javaScriptCanOpenWindowsAutomatically = true
            webView.settings.javaScriptEnabled = true
            //webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)

            webView.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
            webView.settings.defaultFontSize = 16//当手机字体很小的时候，部分H5页面显示不正常
            webView.settings.defaultFixedFontSize = 16

            webView.settings.setSupportZoom(false)
            webView.settings.setSupportMultipleWindows(true)
            webView.settings.setGeolocationEnabled(true)
            webView.settings.setAppCacheEnabled(true)
            webView.settings.setAppCachePath(webView.context.getDir(HKConfig.NAME_CACHE_WEB_DIR, Context.MODE_PRIVATE).path)

            webView.isVerticalScrollBarEnabled = false
            webView.isHorizontalScrollBarEnabled = false


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                //使用File协议(加载本地的html 例如：file:///sdcard/index.html )，存在跨源安全威胁
                webView.settings.allowFileAccessFromFileURLs = true
                webView.settings.allowUniversalAccessFromFileURLs = true
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//https + http 混合页面
                webView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            }

            webView.setWebViewClient(WebViewClient())
            webView.setWebChromeClient(HKWebChromeClient())

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                WebView.setWebContentsDebuggingEnabled(HKBaseApplication.DEBUG)
            }
            CookieManager.getInstance().setAcceptCookie(true)
        }
    }

    @Suppress("DEPRECATION")
    fun removeAllCookie() {
        CookieSyncManager.createInstance(HKBaseApplication.INSTANCE.applicationContext)
        val cookieManager = CookieManager.getInstance()
        cookieManager.removeAllCookie()
        cookieManager.setAcceptCookie(true)
        CookieSyncManager.getInstance().sync()
    }

}
