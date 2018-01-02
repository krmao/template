package com.smart.library.widget.webview

import android.app.Activity
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.webkit.URLUtil
import android.webkit.WebView
import com.smart.library.R
import com.smart.library.base.HKActivity
import com.smart.library.base.HKBaseFragment
import com.smart.library.bundle.HKHybird
import com.smart.library.util.HKLogUtil
import com.smart.library.util.hybird.HKHybirdBridge
import com.smart.library.widget.titlebar.HKTitleBar
import com.smart.library.widget.webview.client.HKWebChromeClient
import com.smart.library.widget.webview.client.HKWebViewClient
import kotlinx.android.synthetic.main.hk_fragment_webview_v2.*

@Suppress("unused", "MemberVisibilityCanPrivate")
open class HKWebFragmentV2 : HKBaseFragment(), HKBaseFragment.OnBackPressedListener {

    companion object {
        @JvmStatic
        fun goTo(activity: Activity, url: String?) = goTo(activity, url, false)

        @JvmStatic
        fun goTo(activity: Activity, url: String?, hideBackAtFirstPage: Boolean) {
            val bundle = Bundle()
            bundle.putString("url", url)
            bundle.putBoolean("hideBackAtFirstPage", hideBackAtFirstPage)
            HKActivity.start(activity, HKWebFragmentV2::class.java, bundle)
        }
    }

    private val url: String? by lazy {
        arguments?.getString("url", null)
    }
    private val hideBackAtFirstPage: Boolean by lazy {
        arguments?.getBoolean("hideBackAtFirstPage") == true
    }
    protected val webView: WebView by lazy {
        web_view
    }
    protected val titleBar: HKTitleBar by lazy {
        title_bar
    }
    protected var webViewClient: HKWebViewClient = object : HKWebViewClient() {

        @Suppress("OverridingDeprecatedMember", "DEPRECATION")
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            titleBar.right0Btn.text = ""
            return super.shouldOverrideUrlLoading(view, url)
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            title_bar?.progressBar?.progress = 0
            title_bar?.progressBar?.visibility = View.VISIBLE
        }

        override fun onPageFinished(webView: WebView?, url: String?) {
            super.onPageFinished(webView, url)
            title_bar?.progressBar?.progress = 100
            title_bar?.progressBar?.visibility = View.GONE

            if (hideBackAtFirstPage) {
                if (web_view.canGoBack())
                    title_bar?.left0BgView?.visibility = View.VISIBLE
                else
                    title_bar?.left0BgView?.visibility = GONE
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater?.inflate(R.layout.hk_fragment_webview_v2, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        HKWebViewUtil.initWebView(web_view)
        titleBar.left0BgView.visibility = if (hideBackAtFirstPage) GONE else VISIBLE

        webView.setWebViewClient(webViewClient)

        webView.setWebChromeClient(object : HKWebChromeClient() {
            override fun onProgressChanged(_view: WebView, newProgress: Int) {
                title_bar?.progressBar?.progress = newProgress
                if (newProgress >= 100) {
                    title_bar?.progressBar?.visibility = GONE
                } else {
                    if (title_bar?.progressBar?.visibility == GONE)
                        title_bar?.progressBar?.visibility = View.VISIBLE
                }
                super.onProgressChanged(_view, newProgress)
            }

            override fun onReceivedTitle(view: WebView?, title: String?) {
                super.onReceivedTitle(view, title)
                title_bar?.titleText?.maxEms = 10
                title_bar?.titleText?.text = title
            }
        })

        HKLogUtil.w(HKHybirdBridge.TAG, "indexUrl:$url , isValidUrl?${URLUtil.isValidUrl(url)}")

        if (URLUtil.isValidUrl(url)) {
            HKHybird.checkUpdate(url)
            web_view.loadUrl(url)
        }
    }

    override fun onDestroy() {
        webViewClient.onDestroy()
        super.onDestroy()
    }

    override fun onBackPressed(): Boolean {
        if (web_view != null && web_view.canGoBack()) {
            web_view.goBack()
            return true
        }
        return false
    }
}
