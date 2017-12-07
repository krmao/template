package com.smart.housekeeper.module.home

import android.os.Bundle
import android.taobao.atlas.bundleInfo.AtlasBundleInfoManager
import android.taobao.atlas.framework.Atlas
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smart.library.base.HKBaseFragment
import com.smart.library.util.HKLogUtil
import kotlinx.android.synthetic.main.home_fragment.*

class HomeFragment : HKBaseFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater?.inflate(R.layout.home_fragment, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        text1.setOnClickListener {

        }
        text2.setOnClickListener {
        }
    }

    override fun onStart() {
        super.onStart()
        Log.w("krmao", "HomeFragment:onStart")
        HKLogUtil.w("krmao", "所有的Bundles:" + AtlasBundleInfoManager.instance().bundleInfo.bundles.toString())
        HKLogUtil.e("krmao", "当前已安装:" + Atlas.getInstance().bundles.toString())
    }

    override fun onStop() {
        super.onStop()
        Log.w("krmao", "HomeFragment:onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.w("krmao", "HomeFragment:onDestroy")
    }
}
