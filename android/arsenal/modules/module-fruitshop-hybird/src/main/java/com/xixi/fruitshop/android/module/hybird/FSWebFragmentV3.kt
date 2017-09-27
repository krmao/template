package com.xixi.fruitshop.android.module.hybird

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.View
import com.xixi.library.android.base.FSActivity
import com.xixi.library.android.widget.webview.FSWebFragmentV2
import com.evgenii.jsevaluator.JsEvaluator
import com.xixi.library.android.util.FSLogUtil
import com.xixi.library.android.util.FSToastUtil
import android.webkit.WebView
import com.xixi.fruitshop.android.module.hybird.core.FSJSBridgeManager
import com.xixi.library.android.widget.webview.FSWebViewUtil


class FSWebFragmentV3 : FSWebFragmentV2() {
    companion object {
        fun goTo(activity: Activity, url: String?) {
            goTo(activity, url, false)
        }

        fun goTo(activity: Activity, url: String?, hideBackAtFirstPage: Boolean) {
            val bundle: Bundle = Bundle()
            bundle.putString("url", url)
            bundle.putBoolean("hideBackAtFirstPage", hideBackAtFirstPage)
            FSActivity.start(activity, FSWebFragmentV3::class.java, bundle)
        }
    }

    private val jsEvaluator: JsEvaluator  by lazy {
        JsEvaluator(activity)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.evaluateJavascript("") {

            }
        }
        webView.loadUrl("""
            javascript:function getSum(){
                console.log('loadUrl:getSum: before return 99');
            }
        """.trimIndent())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true)
        }
        webView.setWebViewClient(object : FSWebViewUtil.FSWebViewClient() {
            @Suppress("OverridingDeprecatedMember", "DEPRECATION")
            override fun shouldOverrideUrlLoading(_view: WebView?, _url: String?): Boolean {
                FSLogUtil.d("HTML5", "shouldOverrideUrlLoading:" + _url)
                return FSJSBridgeManager.shouldOverrideUrlLoading(_url)
            }
        })
        titleBar.titleText.visibility = View.VISIBLE
        titleBar.titleText.text = "hybird test"
        titleBar.titleText.setOnClickListener {
            //            FSJSBridgeManager.callJsFunction(webView, "javascript:getSum()") { result: String? ->
//                FSLogUtil.e("HTML5", "on call back now !!!result:" + result + '\n')
//            }
            FSJSBridgeManager.callJsFunction(webView, "javascript:a()") { result: String? ->
                FSLogUtil.e("HTML5", "a call back !!!result:" + result + '\n')
            }
            FSJSBridgeManager.callJsFunction(webView, "javascript:b()") { result: String? ->
                FSLogUtil.e("HTML5", "b call back !!!result:" + result + '\n')
            }
        }
    }
}
