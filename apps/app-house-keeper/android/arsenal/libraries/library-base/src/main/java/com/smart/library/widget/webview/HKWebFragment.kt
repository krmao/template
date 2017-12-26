package com.smart.library.widget.webview

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
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

@Suppress("unused", "MemberVisibilityCanPrivate")
open class HKWebFragment : HKBaseFragment(), HKBaseFragment.OnBackPressedListener {
    companion object {

        @JvmStatic protected var TAG = "HKWebFragment"
        @JvmStatic protected val KEY_URL = "key_url"
        @JvmStatic protected val KEY_DATA = "key_data"
        @JvmStatic protected val KEY_TITLE = "key_title"
        @JvmStatic protected val KEY_HIDE_TITLE = "key_hide_title"

        @JvmStatic
        fun goTo(activity: Activity, url: String, isHideTitle: Boolean = false, failureUrl: String? = null) =
            goTo(activity, url, isHideTitle, null, 0, 0, 0, 0, failureUrl)

        @JvmStatic
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
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater?.inflate(R.layout.hk_fragment_webview, container, false)

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        web_view.settings.javaScriptEnabled = true;
        web_view.settings.domStorageEnabled = true;
        web_view.setWebViewClient(object : WebViewClient() {

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                Log.d(TAG, "shouldOverrideUrlLoading:" + request?.url?.toString())
                return super.shouldOverrideUrlLoading(view, request)
            }

            @Suppress("DEPRECATION", "OverridingDeprecatedMember")
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                Log.d(TAG, "shouldOverrideUrlLoading:" + url)
                return super.shouldOverrideUrlLoading(view, url)
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                Log.d(TAG, "onPageStarted:" + url)
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                Log.d(TAG, "onPageFinished:" + url)
                super.onPageFinished(view, url)
            }
        })

        loadUrl(web_view, arguments.getString(KEY_URL))
    }

    protected fun loadUrl(webView: WebView, url: String?) = webView.loadUrl(url)

    override fun onBackPressed(): Boolean {
        if (web_view.canGoBack()) {
            web_view.goBack()
            return true
        }
        return false
    }
}
