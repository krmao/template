package com.smart.template.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.smart.library.base.CXBaseFragment
import com.smart.template.R
import com.smart.template.module.flutter.FlutterActivity

class FlutterFragment : CXBaseFragment() {

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val textView: TextView = TextView(activity)
        textView.text = "flutter"
        @Suppress("DEPRECATION")
        textView.setBackgroundColor(resources.getColor(R.color.pink))
        textView.setOnClickListener {

            (1..1).forEach {
                FlutterActivity.goTo(activity, "route2")
            }

            /*CXRouteManager.goToActivity(activity, "com.smart.template.module.flutter.FlutterActivity") {
                CXLogUtil.w("krmao", it.toString())
            }*/
        }
        return textView
    }

    override fun onStart() {
        super.onStart()
        Log.w("krmao", "FlutterFragment:onStart");
    }

    override fun onStop() {
        super.onStop()
        Log.w("krmao", "FlutterFragment:onStop");
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.w("krmao", "FlutterFragment:onDestroy");
    }
}
