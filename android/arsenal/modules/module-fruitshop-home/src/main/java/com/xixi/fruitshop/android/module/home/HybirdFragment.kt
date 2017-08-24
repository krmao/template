package com.xixi.fruitshop.android.module.home

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.xixi.library.android.base.FSBaseFragment

class HybirdFragment : FSBaseFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val textView: TextView = TextView(activity)
        textView.text = "hybird"
        textView.setBackgroundColor(Color.parseColor("#FF33B5E5"))
        return textView
    }

    override fun onStart() {
        super.onStart()
        Log.w("krmao", "HybirdFragment:onStart");
    }

    override fun onStop() {
        super.onStop()
        Log.w("krmao", "HybirdFragment:onStop");
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.w("krmao", "HybirdFragment:onDestroy");
    }
}
