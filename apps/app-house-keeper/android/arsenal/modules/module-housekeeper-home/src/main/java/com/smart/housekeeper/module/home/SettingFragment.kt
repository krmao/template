package com.smart.housekeeper.module.home

import android.annotation.SuppressLint
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

class SettingFragment : HKBaseFragment() {

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val textView: TextView = TextView(activity)
        textView.text = "setting"
        @Suppress("DEPRECATION")
        textView.setTextColor(resources.getColor(R.color.hk_orange))
        textView.setBackgroundColor(Color.DKGRAY)
        textView.setOnClickListener {
            HKRouteManager.goToFragment(activity, "com.smart.housekeeper.module.setting.SettingFragment") {
                HKLogUtil.w("krmao", it.toString())
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
