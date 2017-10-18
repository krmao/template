package com.smart.library.widget.webview

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import com.smart.library.R
import com.smart.library.base.HKActivity
import com.smart.library.base.HKBaseFragment
import com.smart.library.widget.loading.HKFrameLoadingLayout
import kotlinx.android.synthetic.main.hk_fragment_webview.*

open class HKWebFragment : HKBaseFragment(), HKBaseFragment.OnBackPressedListener {
    companion object {
        protected var TAG = "HTML5"
        protected val KEY_URL = "key_url"
        protected val KEY_DATA = "key_data"
        protected val KEY_TITLE = "key_title"
        protected val KEY_HIDE_TITLE = "key_hide_title"

        //可外部静态配置
        var defaultTitleBarBgColor: Int = 0
        var defaultTitleBarBgRes: Int = R.drawable.hk_b_e2_white_normal_shape
        var defaultTitleBarTextColor = Color.parseColor("#333333")
        var defaultTitleBarTextSize = 16

        fun goToCompleteHttpUrl(activity: Activity, url: String) {
            goTo(activity, HKWebViewUtil.wrapUrlSchema(url), false)
        }

        fun goToCompleteHttpUrl(activity: Activity, url: String, title: String) {
            goTo(activity, HKWebViewUtil.wrapUrlSchema(url), title, null)
        }

        fun goToCompleteHttpUrl(activity: Activity, url: String, title: String, failureUrl: String) {
            goTo(activity, HKWebViewUtil.wrapUrlSchema(url), false, title, 0, 0, 0, 0, failureUrl)
        }

        fun goToCompleteHttpUrl(activity: Activity, url: String, isHideTitle: Boolean) {
            goTo(activity, HKWebViewUtil.wrapUrlSchema(url), isHideTitle, null)
        }

        fun goToCompleteHttpUrl(activity: Activity, url: String, isHideTitle: Boolean, title: String,
                                titleBarBgColor: Int,
                                titleBarBgRes: Int,
                                titleBarTextColor: Int,
                                titleBarTextSize: Int,
                                failureUrl: String
        ) {
            goTo(activity, HKWebViewUtil.wrapUrlSchema(url), isHideTitle, title, titleBarBgColor, titleBarBgRes, titleBarTextColor, titleBarTextSize, failureUrl)
        }

        fun goTo(activity: Activity, url: String, title: String, failureUrl: String? = null) {
            goTo(activity, url, false, title, 0, 0, 0, 0, failureUrl)
        }

        fun goTo(activity: Activity, url: String, isHideTitle: Boolean = false, failureUrl: String? = null) {
            goTo(activity, url, isHideTitle, null, 0, 0, 0, 0, failureUrl)
        }

        fun goTo(activity: Activity, url: String, isHideTitle: Boolean, title: String?,
                 titleBarBgColor: Int,
                 titleBarBgRes: Int,
                 titleBarTextColor: Int,
                 titleBarTextSize: Int,
                 failureUrl: String?
        ) {
            if (activity.isFinishing) {
                Log.e(TAG, "上下文无效")
                return
            }
            val bundle = Bundle()
            bundle.putString(KEY_URL, url)
            bundle.putBoolean(KEY_HIDE_TITLE, isHideTitle)
            bundle.putString(KEY_TITLE, title)
            bundle.putString("failureUrl", failureUrl)
            bundle.putInt("titleBarBgColor", titleBarBgColor)
            bundle.putInt("titleBarBgRes", titleBarBgRes)
            bundle.putInt("titleBarTextColor", titleBarTextColor)
            bundle.putInt("titleBarTextSize", titleBarTextSize)
            HKActivity.start(activity, HKWebFragment::class.java, bundle)
        }

        fun goToWithData(activity: Activity, htmlStr: String, isHideTitle: Boolean, title: String,
                         titleBarBgColor: Int,
                         titleBarBgRes: Int,
                         titleBarTextColor: Int,
                         titleBarTextSize: Int,
                         failureUrl: String
        ) {
            if (activity.isFinishing) {
                Log.e(TAG, "上下文无效")
                return
            }
            val bundle = Bundle()
            bundle.putString(KEY_DATA, htmlStr)
            bundle.putBoolean(KEY_HIDE_TITLE, isHideTitle)
            bundle.putString(KEY_TITLE, title)
            bundle.putString("failureUrl", failureUrl)
            bundle.putInt("titleBarBgColor", titleBarBgColor)
            bundle.putInt("titleBarBgRes", titleBarBgRes)
            bundle.putInt("titleBarTextColor", titleBarTextColor)
            bundle.putInt("titleBarTextSize", titleBarTextSize)
            HKActivity.start(activity, HKWebFragment::class.java, bundle)
        }
    }

    protected val SCHEME = "native"
    protected val HOST = "home"

    protected var mUrl: String? = "http://www.baidu.com"
    protected var mHtmlData: String? = null
    protected var mTitle: String? = "美丽人生"
    protected var mFailureUrl: String? = "http://www.baidu.com"
    protected var mIsHideTitle = true

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.hk_fragment_webview, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //set color size start======================================================================
        var titleBarBgColor = 0
        var titleBarBgRes = 0
        var titleBarTextColor = 0
        var titleBarTextSize = 0
        val bundle = arguments
        if (bundle != null) {
            mUrl = bundle.getString(KEY_URL)
            mHtmlData = bundle.getString(KEY_DATA, "")
            mTitle = bundle.getString(KEY_TITLE)
            mFailureUrl = bundle.getString("failureUrl", null)
            mIsHideTitle = bundle.getBoolean(KEY_HIDE_TITLE, true)
            titleBarBgColor = bundle.getInt("titleBarBgColor", defaultTitleBarBgColor)
            titleBarBgRes = bundle.getInt("titleBarBgRes", defaultTitleBarBgRes)
            titleBarTextColor = bundle.getInt("titleBarTextColor", defaultTitleBarTextColor)
            titleBarTextSize = bundle.getInt("titleBarTextSize", defaultTitleBarTextSize)
        }
        if (titleBarBgColor <= 0) titleBarBgColor = defaultTitleBarBgColor
        if (titleBarBgRes <= 0) titleBarBgRes = defaultTitleBarBgRes
        if (titleBarTextColor <= 0) titleBarTextColor = defaultTitleBarTextColor
        if (titleBarTextSize <= 0) titleBarTextSize = defaultTitleBarTextSize
        titleBar.titleText.text = mTitle
        titleBar.visibility = if (mIsHideTitle) View.GONE else View.VISIBLE
        titleBar.titleText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, titleBarTextSize.toFloat())
        titleBar.titleText.setTextColor(titleBarTextColor)
        titleBar.setBackgroundResource(titleBarBgRes)
        if (titleBarBgColor > 0)
            titleBar.setBackgroundColor(titleBarBgColor)
        //set color size end  ======================================================================

        initWebView(web_view)
        addJavascriptInterface(web_view)
        loadUrl(web_view, mUrl, mFailureUrl)
    }

    protected fun initWebView(webView: WebView) {
        HKWebViewUtil.initWebView(webView)

        webView.setWebViewClient(object : WebViewClient() {

            override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError?) {
                super.onReceivedError(view, request, error)
                view.stopLoading()
                if (TextUtils.isEmpty(mFailureUrl)) {
                    webView.goBack()
                    loading_view.showView(HKFrameLoadingLayout.ViewType.NETWORK_EXCEPTION, if (error == null) loading_view.getDefaultText(HKFrameLoadingLayout.ViewType.NETWORK_EXCEPTION) else "error", false, true)
                } else {
                    view.loadUrl(mFailureUrl)
                }
            }

            @Suppress("DEPRECATION", "OverridingDeprecatedMember")
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                Log.d(TAG, "shouldOverrideUrlLoading:" + url)
                return super.shouldOverrideUrlLoading(view, url)
            }

            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap) {
                Log.d(TAG, "onPageStarted:" + url)
                super.onPageStarted(view, url, favicon)
                loading_view.showView(HKFrameLoadingLayout.ViewType.LOADING)
            }

            override fun onPageFinished(view: WebView, url: String) {
                Log.d(TAG, "onPageFinished:" + url)
                loading_view.hideAll()
            }
        })

        webView.setWebChromeClient(object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                Log.d(TAG, "onProgressChanged:" + newProgress)
                loading_view.updateText(HKFrameLoadingLayout.ViewType.LOADING, loading_view.getDefaultText(HKFrameLoadingLayout.ViewType.NODATA) + " " + newProgress + "%", false, true)
            }

            @Suppress("OverridingDeprecatedMember")
            override fun onConsoleMessage(message: String, lineNumber: Int, sourceID: String) {
                Log.w(TAG, "#$lineNumber:$sourceID")
                Log.d(TAG, message)
            }

            override fun onReceivedTitle(view: WebView, title: String) {
                super.onReceivedTitle(view, title)
                Log.d(TAG, "onReceivedTitle:" + title)
                if (!mIsHideTitle && TextUtils.isEmpty(mTitle)) {
                    titleBar.titleText.text = mTitle
                }
            }

            override fun onJsAlert(view: WebView, url: String, message: String, result: JsResult): Boolean {
                Log.d(TAG, "onJsAlert:" + message)
                return super.onJsAlert(view, url, message, result)
            }
        })
    }

    @Suppress("unused_parameter")
    protected fun addJavascriptInterface(webview: WebView) {
    }

    protected fun loadUrl(webView: WebView, url: String?, failureUrl: String?) {
        if (URLUtil.isValidUrl(url)) {
            loading_view.setOnRefreshClickListener(View.OnClickListener { webView.loadUrl(url) })
            /*if (!TextUtils.isEmpty(mHtmlData)) {
                webView.loadDataWithBaseURL(null, mHtmlData, "text/html", "utf-8", null)
            } else*/
            webView.loadUrl(url)
        } else {
            if (URLUtil.isValidUrl(failureUrl))
                webView.loadUrl(failureUrl)
            else
                loading_view.showView(HKFrameLoadingLayout.ViewType.NETWORK_EXCEPTION, "加载失败,Url不正确:\n" + url, false, true)
        }
    }

    override fun onBackPressed(): Boolean {
        if (web_view.canGoBack()) {
            web_view.goBack()
            return true
        }
        return false
    }
}
