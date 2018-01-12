package com.smart.library.widget.webview.client

import android.content.Context
import android.util.AttributeSet
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.smart.library.bundle.HKHybird
import com.smart.library.util.HKLogUtil
import com.smart.library.widget.webview.HKWebViewUtil

@Suppress("MemberVisibilityCanPrivate", "unused")
open class HKWebView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : WebView(context, attrs, defStyleAttr) {

    val TAG: String = HKWebView::class.java.name

    var client: WebViewClient? = null
        private set
    var chromeClient: WebChromeClient? = null
        private set

    init {
        HKWebViewUtil.initWebView(this)
    }

    open fun loadURL(url: String?) {
        val start = System.currentTimeMillis()
        HKLogUtil.e(TAG, "loadURL start:$url")
        HKHybird.checkUpdate(url) {
            HKHybird.onWebViewOpenPage(client, url)
            loadUrl(url)
        }
        HKLogUtil.e(TAG, "loadURL   end:$url , 耗时:${System.currentTimeMillis() - start}ms")
    }

    override fun setWebViewClient(client: WebViewClient) {
        super.setWebViewClient(client)
        this.client = client
    }

    override fun setWebChromeClient(client: WebChromeClient) {
        super.setWebChromeClient(client)
        this.chromeClient = client
    }

    open fun onDestroy() {
        if (client != null && client is HKWebViewClient) {
            (client as HKWebViewClient).onDestroy()
        }
    }
}
