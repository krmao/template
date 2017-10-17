package com.smart.housekeeper.module.hybird

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import com.smart.library.base.HKActivity
import com.smart.library.widget.webview.HKWebFragmentV2


class HybirdWebFragment : HKWebFragmentV2() {
    companion object {
        fun goTo(activity: Context, url: String?) {
            goTo(activity, url, false)
        }

        fun goTo(activity: Context, url: String?, hideBackAtFirstPage: Boolean) {
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
    }
}
