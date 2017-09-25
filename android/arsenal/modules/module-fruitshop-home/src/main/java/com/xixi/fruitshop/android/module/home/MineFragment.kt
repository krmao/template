package com.xixi.fruitshop.android.module.home

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.xixi.library.android.base.FSBaseFragment
import com.xixi.library.android.util.FSLogUtil
import com.xixi.library.android.util.FSRouteManager

class MineFragment : FSBaseFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val textView: TextView = TextView(activity)
        textView.text = "mine"
        textView.setBackgroundColor(resources.getColor(R.color.fs_pink))
        textView.setOnClickListener {
            FSRouteManager.goToFragment(activity, "com.xixi.fruitshop.android.module.mine.MineFragment") {
                FSLogUtil.w("krmao", it.toString())
            }
        }
        return textView
    }

    override fun onStart() {
        super.onStart()
        Log.w("krmao", "MineFragment:onStart");
    }

    override fun onStop() {
        super.onStop()
        Log.w("krmao", "MineFragment:onStop");
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.w("krmao", "MineFragment:onDestroy");
    }
}
