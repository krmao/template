package com.smart.library.widget.webview.client

import android.content.Context
import android.util.AttributeSet
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.smart.library.bundle.CXHybird
import com.smart.library.util.CXLogUtil
import com.smart.library.widget.webview.CXWebViewUtil

@Suppress("MemberVisibilityCanPrivate", "unused")
open class CXWebView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : WebView(context, attrs, defStyleAttr) {

    val TAG: String = CXWebView::class.java.name

    var client: WebViewClient? = null
        private set
    var chromeClient: WebChromeClient? = null
        private set

    init {
        CXWebViewUtil.initWebView(this)
    }

    open fun loadURL(url: String?) {
        val start = System.currentTimeMillis()
        CXLogUtil.e(TAG, "loadURL start:$url")
        CXHybird.checkUpdate(url) {
            CXHybird.onWebViewOpenPage(client, url)

            post { loadUrl(url) } //确保在主线程执行

            Unit
        }
        CXLogUtil.e(TAG, "loadURL   end:$url , 耗时:${System.currentTimeMillis() - start}ms")
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
        if (client != null && client is CXWebViewClient) {
            (client as CXWebViewClient).onDestroy()
        }
    }
}
