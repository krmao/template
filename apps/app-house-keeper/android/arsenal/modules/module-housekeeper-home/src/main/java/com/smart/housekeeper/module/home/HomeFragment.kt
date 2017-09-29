package com.smart.housekeeper.module.home

import android.graphics.Color
import android.os.Bundle
import android.taobao.atlas.bundleInfo.AtlasBundleInfoManager
import android.taobao.atlas.framework.Atlas
import android.taobao.atlas.framework.BundleInstaller
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.smart.library.base.HKBaseFragment
import com.smart.library.util.HKLogUtil
import com.smart.library.util.HKRouteManager
import com.smart.library.util.HKToastUtil

class HomeFragment : HKBaseFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val textView: TextView = TextView(context)
        textView.text = "home"
        textView.setBackgroundColor(Color.LTGRAY)
        textView.setOnClickListener {
            HKLogUtil.w("krmao", "所有的Bundles:" + AtlasBundleInfoManager.instance().bundleInfo.bundles.toString())
            HKLogUtil.e("krmao", "当前已安装:" + Atlas.getInstance().bundles.toString())
        }
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
