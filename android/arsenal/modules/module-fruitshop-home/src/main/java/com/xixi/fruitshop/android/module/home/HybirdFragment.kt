package com.xixi.fruitshop.android.module.home

import android.graphics.Color
import android.os.Bundle
import android.taobao.atlas.framework.Atlas
import android.taobao.atlas.framework.BundleInstaller
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.xixi.library.android.base.FSBaseFragment
import com.xixi.library.android.util.FSLogUtil
import com.xixi.library.android.util.FSRouteManager
import com.xixi.library.android.util.FSToastUtil

class HybirdFragment : FSBaseFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val textView: TextView = TextView(activity)
        textView.text = "hybird"
        textView.setBackgroundColor(Color.parseColor("#FF33B5E5"))
        textView.setOnClickListener {
            FSRouteManager.goToFragment(activity, "com.xixi.fruitshop.android.module.hybird.HybirdFragment") {
                FSLogUtil.w("krmao", it.toString())
            }
        }
        return textView
    }

    override fun onStart() {
        super.onStart()
        Log.w("krmao", "HybirdFragment:onStart")
    }

    override fun onStop() {
        super.onStop()
        Log.w("krmao", "HybirdFragment:onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.w("krmao", "HybirdFragment:onDestroy")
    }
}
