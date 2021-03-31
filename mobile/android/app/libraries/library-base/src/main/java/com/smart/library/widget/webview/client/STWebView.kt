package com.smart.library.widget.webview.client

import android.content.Context
import android.util.AttributeSet
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.Keep
import com.smart.library.STInitializer
import com.smart.library.bundle.STHybird
import com.smart.library.util.STLogUtil
import com.smart.library.util.STSystemUtil
import com.smart.library.widget.webview.STWebViewUtil

@Keep
@Suppress("MemberVisibilityCanPrivate", "unused", "PropertyName")
open class STWebView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : WebView(context, attrs, defStyleAttr) {

    val TAG: String = STWebView::class.java.name

    var client: WebViewClient? = null
        private set
    var chromeClient: WebChromeClient? = null
        private set

    init {
        STWebViewUtil.initWebView(this, " statusBarHeight/${(STSystemUtil.statusBarHeight() / (STSystemUtil.displayMetrics(STInitializer.application())?.density ?: 1f)).toInt()} ")
    }

    open fun loadURL(url: String?) {
        val start = System.currentTimeMillis()
        STLogUtil.e(TAG, "loadURL start:$url")
        STHybird.checkUpdate(url) {
            STHybird.onWebViewOpenPage(client, url)

            post { loadUrl(url) } //确保在主线程执行

            Unit
        }
        STLogUtil.e(TAG, "loadURL   end:$url , 耗时:${System.currentTimeMillis() - start}ms")
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
        if (client != null && client is STWebViewClient) {
            (client as STWebViewClient).onDestroy()
        }
    }
}
