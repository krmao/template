package com.xixi.fruitshop.android.module.hybird

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.xixi.library.android.base.FSActivity
import com.xixi.library.android.widget.webview.FSWebFragmentV2
import com.evgenii.jsevaluator.JsEvaluator
import com.evgenii.jsevaluator.interfaces.JsCallback
import com.xixi.library.android.util.FSLogUtil
import com.xixi.library.android.util.FSToastUtil


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

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        jsEvaluator.evaluate("2 * 17", object : JsCallback {
            override fun onResult(result: String) {
                // Process result here.
                // This method is called in the UI thread.
                FSToastUtil.show("onResult:$result")
                FSLogUtil.d("onResult:$result")
            }

            override fun onError(errorMessage: String) {
                // Process JavaScript error here.
                // This method is called in the UI thread.
                FSToastUtil.show("onError:$errorMessage")
                FSLogUtil.e("onError:$errorMessage")
            }
        })
    }

}