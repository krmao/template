package com.xixi.fruitshop.android.module.hybird

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.View
import com.xixi.library.android.base.FSActivity
import com.xixi.library.android.util.FSLogUtil
import com.xixi.library.android.util.hybird.FSHybirdManager
import com.xixi.library.android.widget.webview.FSWebFragmentV2


class HybirdWebFragment : FSWebFragmentV2() {
    companion object {
        fun goTo(activity: Activity, url: String?) {
            goTo(activity, url, false)
        }

        fun goTo(activity: Activity, url: String?, hideBackAtFirstPage: Boolean) {
            val bundle: Bundle = Bundle()
            bundle.putString("url", url)
            bundle.putBoolean("hideBackAtFirstPage", hideBackAtFirstPage)
            FSActivity.start(activity, HybirdWebFragment::class.java, bundle)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        titleBar.titleText.visibility = View.VISIBLE
        titleBar.titleText.text = "hybird test"
        titleBar.titleText.setOnClickListener {
            FSHybirdManager.callJsFunction(webView, "javascript:a()") { result: String? ->
                FSLogUtil.e("HTML5", "a call back !!!result:" + result + '\n')
            }
        }
    }
}
