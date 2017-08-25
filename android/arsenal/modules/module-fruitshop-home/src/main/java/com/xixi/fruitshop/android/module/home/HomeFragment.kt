package com.xixi.fruitshop.android.module.home

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
import com.xixi.library.android.base.FSBaseFragment
import com.xixi.library.android.util.FSLogUtil
import com.xixi.library.android.util.FSRouteManager
import com.xixi.library.android.util.FSToastUtil

class HomeFragment : FSBaseFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val textView: TextView = TextView(context)
        textView.text = "home"
        textView.setBackgroundColor(Color.LTGRAY)
        textView.setOnClickListener {
            FSLogUtil.w("krmao", "所有的Bundles:" + AtlasBundleInfoManager.instance().bundleInfo.bundles.toString())
            FSLogUtil.e("krmao", "当前已安装:" + Atlas.getInstance().bundles.toString())
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
