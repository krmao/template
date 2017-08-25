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

class SettingFragment : FSBaseFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val textView: TextView = TextView(activity)
        textView.text = "setting"
        textView.setTextColor(resources.getColor(R.color.fs_orange))
        textView.setBackgroundColor(Color.DKGRAY)
        textView.setOnClickListener {
            FSRouteManager.goToFragment(activity, "com.xixi.fruitshop.android.module.setting.SettingFragment") {
                FSLogUtil.w("krmao", it.toString())
            }
        }
        return textView
    }

    override fun onStart() {
        super.onStart()
        Log.w("krmao", "SettingFragment:onStart");
    }

    override fun onStop() {
        super.onStop()
        Log.w("krmao", "SettingFragment:onStop");
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.w("krmao", "SettingFragment:onDestroy");
    }
}
