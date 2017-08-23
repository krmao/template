package com.xixi.fruitshop.android.module.home

import android.app.Activity
import android.graphics.Bitmap
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.webkit.URLUtil
import android.webkit.WebView
import com.xixi.library.android.base.FSActivity
import com.xixi.library.android.base.FSBaseFragment
import com.xixi.library.android.util.FSLogUtil
import com.xixi.library.android.widget.webview.FSWebViewUtil
import kotlinx.android.synthetic.main.home_fragment_web.*


class FSWebFragment : FSBaseFragment(), FSBaseFragment.OnBackPressedListener {

    companion object {
        fun goTo(activity: Activity, url: String?) {
            goTo(activity, url, false)
        }

        fun goTo(activity: Activity, url: String?, hideBackAtFirstPage: Boolean) {
            val bundle: Bundle = Bundle()
            bundle.putString("url", url)
            bundle.putBoolean("hideBackAtFirstPage", hideBackAtFirstPage)
            FSActivity.start(activity, FSWebFragment::class.java, bundle)
        }
    }

    private val url: String? by lazy {
        arguments?.getString("url", null)
    }
    private val hideBackAtFirstPage: Boolean by lazy {
        arguments?.getBoolean("hideBackAtFirstPage") ?: false
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.home_fragment_web, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        FSWebViewUtil.initWebView(web_view)
        title_bar.left0BgView.visibility = if (hideBackAtFirstPage) GONE else VISIBLE

        web_view.setWebViewClient(object : FSWebViewUtil.FSWebViewClient() {

            @Suppress("OverridingDeprecatedMember", "DEPRECATION")
            override fun shouldOverrideUrlLoading(_view: WebView?, _url: String?): Boolean {
                FSLogUtil.d("krmao", "shouldOverrideUrlLoading:" + _url)
                if (_url != null && url != _url) {//首页
                    if (!TextUtils.isEmpty(_url) && _url.startsWith(HomeActivity.URL_PREFIX)) {
                        FSWebFragment.goTo(activity, _url)
                        return true
                    }
                }
                return super.shouldOverrideUrlLoading(_view, _url)
            }

            override fun onPageStarted(_view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(_view, url, favicon)
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
        })

        web_view.setWebChromeClient(object : FSWebViewUtil.FSWebChromeClient() {
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

            override fun onReceivedTitle(_view: WebView?, title: String?) {
                super.onReceivedTitle(_view, title)
                title_bar?.titleText?.text = title
            }
        })

        if (URLUtil.isValidUrl(url))
            web_view?.loadUrl(url)
    }

    override fun onBackPressed(): Boolean {
        if (web_view != null && web_view.canGoBack()) {
            web_view.goBack()
            return true
        }
        return false
    }
}
