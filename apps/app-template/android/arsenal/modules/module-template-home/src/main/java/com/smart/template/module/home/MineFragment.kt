package com.smart.template.module.home

import android.annotation.SuppressLint
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

class MineFragment : CXBaseFragment() {

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val textView: TextView = TextView(activity)
        textView.text = "mine"
        @Suppress("DEPRECATION")
        textView.setBackgroundColor(resources.getColor(R.color.cx_pink))
        textView.setOnClickListener {
            CXRouteManager.goToFragment(activity, "com.smart.template.module.mine.MineFragment") {
                CXLogUtil.w("krmao", it.toString())
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
