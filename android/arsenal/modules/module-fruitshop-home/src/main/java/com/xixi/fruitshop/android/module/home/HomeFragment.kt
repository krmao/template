package com.xixi.fruitshop.android.module.home

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.xixi.library.android.base.FSBaseFragment

class HomeFragment : FSBaseFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val textView: TextView = TextView(activity)
        textView.text = "home"
        textView.setBackgroundColor(Color.LTGRAY)
        return textView
    }

    override fun onStart() {
        super.onStart()
        Log.w("krmao", "HomeFragment:onStart");
    }

    override fun onStop() {
        super.onStop()
        Log.w("krmao", "HomeFragment:onStop");
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.w("krmao", "HomeFragment:onDestroy");
    }
}
