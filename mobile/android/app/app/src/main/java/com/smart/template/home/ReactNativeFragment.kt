package com.smart.template.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.smart.library.base.STBaseFragment
import com.smart.library.reactnative.ReactJumper
import com.smart.template.R

class ReactNativeFragment : STBaseFragment() {

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val textView: TextView = TextView(activity)
        textView.text = "react native"
        @Suppress("DEPRECATION")
        textView.setBackgroundColor(resources.getColor(R.color.pink))
        textView.setOnClickListener {
            ReactJumper.goTo(context)
        }
        return textView
    }

    override fun onStart() {
        super.onStart()
        Log.w("krmao", "ReactNativeFragment:onStart");
    }

    override fun onStop() {
        super.onStop()
        Log.w("krmao", "ReactNativeFragment:onStop");
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.w("krmao", "ReactNativeFragment:onDestroy");
    }
}
