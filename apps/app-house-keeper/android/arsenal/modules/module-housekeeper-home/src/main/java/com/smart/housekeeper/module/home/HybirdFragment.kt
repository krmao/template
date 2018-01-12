package com.smart.housekeeper.module.home

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.smart.library.base.HKBaseFragment
import com.smart.library.util.HKLogUtil
import com.smart.library.util.HKRouteManager

class HybirdFragment : HKBaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val textView: TextView = TextView(activity)
        textView.text = "hybird"
        textView.setBackgroundColor(Color.parseColor("#FF33B5E5"))
        textView.setOnClickListener {
            HKRouteManager.goToFragment(activity, "com.smart.housekeeper.module.hybird.HybirdFragment") {
                HKLogUtil.w("krmao", it.toString())
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
