package com.smart.template.home

import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smart.library.base.STBaseFragment
import com.smart.library.map.layer.STMapView
import com.smart.library.util.STStatusBarUtil
import com.smart.template.R
import kotlinx.android.synthetic.main.map_fragment.*


class MapFragment : STBaseFragment() {


    private val mapView: STMapView by lazy { mapBaiduView }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.map_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.fitsSystemWindows = false // 顶到状态栏后面
        activity?.let { STStatusBarUtil.setStatusBarTextColor(it, true) } // 设置状态栏字体深色

        mapView.initialize()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

}
