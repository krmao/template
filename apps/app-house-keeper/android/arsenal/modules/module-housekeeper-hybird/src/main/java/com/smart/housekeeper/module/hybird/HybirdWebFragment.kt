package com.smart.housekeeper.module.hybird

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import com.smart.library.base.HKActivity
import com.smart.library.util.HKLogUtil
import com.smart.library.util.hybird.HKHybirdManager
import com.smart.library.widget.webview.HKWebFragmentV2


class HybirdWebFragment : HKWebFragmentV2() {
    companion object {
        fun goTo(activity: Activity, url: String?) {
            goTo(activity, url, false)
        }

        fun goTo(activity: Activity, url: String?, hideBackAtFirstPage: Boolean) {
            val bundle = Bundle()
            bundle.putString("url", url)
            bundle.putBoolean("hideBackAtFirstPage", hideBackAtFirstPage)
            HKActivity.start(activity, HybirdWebFragment::class.java, bundle)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        titleBar.titleText.visibility = View.VISIBLE
        titleBar.titleText.text = "hybird test"
        titleBar.titleText.setOnClickListener {
            HKHybirdManager.callJsFunction(webView, "javascript:hybird.onBackPressed()") { result: String? ->
                HKLogUtil.e(HKHybirdManager.TAG, "a call back !!!result:" + result + '\n')
            }
            HKHybirdManager.callJsFunction(webView, "javascript:hybird.onPause()") { result: String? ->
                HKLogUtil.e(HKHybirdManager.TAG, "a call back !!!result:" + result + '\n')
            }
            HKHybirdManager.callJsFunction(webView, "javascript:hybird.onNetworkStateChanged()") { result: String? ->
                HKLogUtil.e(HKHybirdManager.TAG, "a call back !!!result:" + result + '\n')
            }
            HKHybirdManager.callJsFunction(webView, "javascript:hybird.onResume()") { result: String? ->
                HKLogUtil.e(HKHybirdManager.TAG, "a call back !!!result:" + result + '\n')
            }
            HKHybirdManager.callJsFunction(webView, "javascript:a()") { result: String? ->
                HKLogUtil.e(HKHybirdManager.TAG, "a call back !!!result:" + result + '\n')
            }
        }
    }
}
