package com.smart.template.module.home

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.smart.library.base.CXBaseFragment
import com.smart.library.util.CXLogUtil
import com.smart.library.util.CXRouteManager

class HybirdFragment : CXBaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val textView: TextView = TextView(activity)
        textView.text = "hybird"
        textView.setBackgroundColor(Color.parseColor("#FF33B5E5"))
        textView.setOnClickListener {
            CXRouteManager.goToFragment(activity, "com.smart.template.module.hybird.HybirdFragment") {
                CXLogUtil.w("krmao", it.toString())
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
