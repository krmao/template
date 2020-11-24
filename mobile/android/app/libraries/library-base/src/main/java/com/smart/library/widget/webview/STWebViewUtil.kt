@file:Suppress("DEPRECATION")

package com.smart.library.widget.webview

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.webkit.CookieManager
import android.webkit.CookieSyncManager
import android.webkit.WebSettings
import android.webkit.WebView
import com.smart.library.STInitializer
import com.smart.library.base.STConfig
import com.smart.library.bundle.STHybird.TAG
import com.smart.library.util.STLogUtil
import com.smart.library.widget.webview.client.STWebChromeClient
import com.smart.library.widget.webview.client.STWebViewClient


@Suppress("unused", "MemberVisibilityCanPrivate")
object STWebViewUtil {

    @SuppressLint("SetJavaScriptEnabled", "ObsoleteSdkInt")
    fun initWebView(webView: WebView?, userAgent: String? = null) {
        if (webView != null) {

            //webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)

            webView.settings.loadWithOverviewMode = false
            webView.settings.builtInZoomControls = false
            webView.settings.allowContentAccess = true
            webView.settings.domStorageEnabled = true
            webView.settings.useWideViewPort = true
            webView.settings.databaseEnabled = true
            webView.settings.javaScriptEnabled = true
            webView.settings.javaScriptCanOpenWindowsAutomatically = true
            webView.settings.cacheMode = WebSettings.LOAD_DEFAULT
            webView.settings.userAgentString = webView.settings.userAgentString + userAgent

            //当手机字体很小的时候，部分H5页面显示不正常
            webView.settings.defaultFontSize = 16
            webView.settings.defaultFixedFontSize = 16

            webView.settings.setSupportZoom(false)
            webView.settings.setSupportMultipleWindows(true)
            webView.settings.setGeolocationEnabled(true)
            webView.settings.setAppCacheEnabled(true)
            webView.settings.setAppCachePath(webView.context.getDir(STConfig.NAME_CACHE_WEB_DIR, Context.MODE_PRIVATE).path)

            /**
             * Android中默认mWebView.setAllowFileAccess(true)，
             * 在File域下，能够执行任意的JavaScript代码，同源策略跨域访问能够对私有目录文件进行访问等。
             * APP对嵌入的WebView未对file:/// 形式的URL做限制，会导致隐私信息泄露，
             * 针对IM类软件会导致聊天信息、联系人等等重要信息泄露，
             * 针对浏览器类软件，则更多的是cookie信息泄露
             */
            webView.settings.allowFileAccess = false

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {

                //使用File协议(加载本地的html 例如：file:///sdcard/index.html )，存在跨源安全威胁
                /**
                 * 在JELLY_BEAN以前默认是 true,
                 * 允许通过file域url中的Javascript读取其他本地文件，
                 * 在JELLY_BEAN及以后默认 false
                 */
                webView.settings.allowFileAccessFromFileURLs = false

                /**
                 * 在JELLY_BEAN以前默认是 true,
                 * 允许通过file域url中的Javascript访问其他的源，包括其他的本地文件和http,https源的数据。
                 * 在JELLY_BEAN及以后默认 false
                 */
                webView.settings.allowUniversalAccessFromFileURLs = false
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                WebView.setWebContentsDebuggingEnabled(STInitializer.debug())
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                /**
                 * MIXED_CONTENT_ALWAYS_ALLOW：允许从任何来源加载内容，即使起源是不安全的；
                 * MIXED_CONTENT_NEVER_ALLOW：不允许Https加载Http的内容，即不允许从安全的起源去加载一个不安全的资源；
                 * MIXED_CONTENT_COMPATIBILITY_MODE：当涉及到混合式内容时，WebView 会尝试去兼容最新Web浏览器的风格。
                 */
                webView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                // 支持跨域
                CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true)
            }
            CookieManager.getInstance().setAcceptCookie(true)

            webView.isVerticalScrollBarEnabled = false
            webView.isHorizontalScrollBarEnabled = false

            webView.webViewClient = STWebViewClient()
            webView.webChromeClient = STWebChromeClient()
            //webView.setDownloadListener()

            // 移除 webView 导出接口，防止被恶意利用
            webView.removeJavascriptInterface("searchBoxJavaBridge_")
            webView.removeJavascriptInterface("accessibilityTraversal")
            webView.removeJavascriptInterface("accessibility")

            STLogUtil.w(TAG, "userAgentString=${webView.settings.userAgentString}")
        }
    }

    @Suppress("DEPRECATION")
    fun removeAllCookie() {
        CookieSyncManager.createInstance(STInitializer.application()?.applicationContext)
        val cookieManager = CookieManager.getInstance()
        cookieManager.removeAllCookie()
        cookieManager.setAcceptCookie(true)
        CookieSyncManager.getInstance().sync()
    }

}
