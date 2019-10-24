package com.smart.template.home

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smart.library.base.STActivity
import com.smart.library.base.STBaseFragment
import com.smart.library.map.layer.STPanelFragment
import com.smart.library.map.layer.STMapView
import com.smart.library.map.model.STMapType
import com.smart.library.util.STStatusBarUtil
import com.smart.template.R
import kotlinx.android.synthetic.main.map_fragment.*


class MapFragment : STBaseFragment() {

    companion object {
        @JvmStatic
        fun goTo(activity: Activity?, useBaidu: Boolean) {
            val bundle = Bundle()
            bundle.putBoolean("useBaidu", useBaidu)
            STActivity.start(activity, MapFragment::class.java, bundle)
        }
    }

    private val mapView: STMapView by lazy { mapBaiduView }
    private val useBaidu: Boolean by lazy { arguments?.getBoolean("useBaidu") ?: true }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.map_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.fitsSystemWindows = false // 顶到状态栏后面
        activity?.let { STStatusBarUtil.setStatusBarTextColor(it, false) } // 设置状态栏字体深色

        mapView.initialize(mapType = if (useBaidu) STMapType.BAIDU else STMapType.GAODE)
        mapView.onCreate(context, savedInstanceState)

        val mContainerFragment = STPanelFragment()
        childFragmentManager.beginTransaction()
                .add(R.id.main_frame_layout, mContainerFragment)
                .commit()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

}
