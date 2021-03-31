package com.smart.library.widget.webview

import android.app.Activity
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.webkit.WebView
import androidx.annotation.Keep
import com.smart.library.R
import com.smart.library.base.STActivity
import com.smart.library.base.STBaseFragment
import com.smart.library.widget.webview.client.STWebChromeClient
import com.smart.library.widget.webview.client.STWebView
import com.smart.library.widget.webview.client.STWebViewClient
import kotlinx.android.synthetic.main.st_fragment_webview.*

@Suppress("unused", "MemberVisibilityCanPrivate")
@Keep
open class STWebFragment : STBaseFragment(), STBaseFragment.OnBackPressedListener {

    companion object {
        @JvmStatic
        fun goTo(activity: Activity?, url: String?) = goTo(activity, url, false, false, false)

        @JvmStatic
        fun goTo(
            activity: Activity?,
            url: String?,
            hideTitleBar: Boolean,
            hideBackAtFirstPage: Boolean,
            fullScreenAndBehindStatusBar: Boolean
        ) {
            val bundle = Bundle()
            bundle.putString("url", url)
            bundle.putBoolean("hideTitleBar", hideTitleBar)
            bundle.putBoolean("hideBackAtFirstPage", hideBackAtFirstPage)
            bundle.putBoolean("fullScreenAndBehindStatusBar", fullScreenAndBehindStatusBar)
            STActivity.startActivity(activity, STWebFragment::class.java, bundle)
        }
    }

    private val url: String? by lazy {
        arguments?.getString("url", null)
    }
    private val hideTitleBar: Boolean by lazy {
        arguments?.getBoolean("hideTitleBar") == true
    }
    /**
     * true  在状态栏的后面
     * false 在状态栏的下面
     */
    private val fullScreenAndBehindStatusBar: Boolean by lazy {
        arguments?.getBoolean("fullScreenAndBehindStatusBar") == true
    }
    private val hideBackAtFirstPage: Boolean by lazy {
        arguments?.getBoolean("hideBackAtFirstPage") == true
    }
    private val webView: STWebView by lazy { web_view }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.st_fragment_webview, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //在状态栏的下面还是后面
        if (!fullScreenAndBehindStatusBar) {
            super.onViewCreated(view, savedInstanceState)
        }

        if (hideTitleBar) {
            title_bar.visibility = View.GONE
        } else {
            title_bar.left0BgView.visibility = if (hideBackAtFirstPage) GONE else VISIBLE
        }

        webView.webViewClient = object : STWebViewClient() {

            @Suppress("OverridingDeprecatedMember", "DEPRECATION")
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (hideTitleBar) {
                    title_bar.right0Btn.text = ""
                }
                return super.shouldOverrideUrlLoading(view, url)
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                if (hideTitleBar) {
                    title_bar?.progressBar?.progress = 0
                    title_bar?.progressBar?.visibility = View.VISIBLE
                }
            }

            override fun onPageFinished(webView: WebView?, url: String?) {
                super.onPageFinished(webView, url)
                if (hideTitleBar) {
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
        }

        webView.setWebChromeClient(object : STWebChromeClient() {
            override fun onProgressChanged(_view: WebView, newProgress: Int) {
                if (hideTitleBar) {
                    title_bar?.progressBar?.progress = newProgress
                    if (newProgress >= 100) {
                        title_bar?.progressBar?.visibility = GONE
                    } else {
                        if (title_bar?.progressBar?.visibility == GONE)
                            title_bar?.progressBar?.visibility = View.VISIBLE
                    }
                }
                super.onProgressChanged(_view, newProgress)
            }

            override fun onReceivedTitle(view: WebView?, title: String?) {
                super.onReceivedTitle(view, title)
                if (hideTitleBar) {
                    title_bar?.titleText?.maxEms = 10
                    title_bar?.titleText?.text = title
                }
            }
        })

        webView.loadURL(url)
    }

    override fun onDestroy() {
        webView.onDestroy()
        super.onDestroy()
    }

    override fun onBackPressed(): Boolean {
        if (webView.canGoBack()) {
            webView.goBack()
            return true
        }
        return false
    }
}
