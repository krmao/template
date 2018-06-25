package com.smart.template.home

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
import com.smart.template.R

class ImFragment : CXBaseFragment() {

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val textView: TextView = TextView(activity)
        textView.text = "im"
        @Suppress("DEPRECATION")
        textView.setTextColor(resources.getColor(R.color.orange))
        textView.setBackgroundColor(Color.DKGRAY)
        textView.setOnClickListener {
            CXRouteManager.goToFragment(activity, "com.smart.template.module.im.IMFragment") {
                CXLogUtil.w("krmao", it.toString())
            }
        }
        return textView
    }

    override fun onStart() {
        super.onStart()
        Log.w("krmao", "IMFragment:onStart");
    }

    override fun onStop() {
        super.onStop()
        Log.w("krmao", "IMFragment:onStop");
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.w("krmao", "IMFragment:onDestroy");
    }
}
