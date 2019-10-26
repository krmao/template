package com.smart.template.home

import android.os.Bundle
import android.os.Handler
import android.support.design.widget.BottomSheetBehavior
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.smart.library.base.STBaseActivity
import com.smart.library.base.STBaseApplication
import com.smart.library.map.layer.STMapView
import com.smart.library.map.model.STMapType
import com.smart.library.widget.recyclerview.STRecyclerViewAdapter
import com.smart.template.R
import com.smart.template.home.behavior.STBehaviorBottomSheetCallback
import kotlinx.android.synthetic.main.st_behavior_bottom_sheet_activity.*

class STBehaviorBottomSheetActivity : STBaseActivity() {

    private val handler: Handler = Handler()
    private val mapView: STMapView by lazy { mapBaiduView }
    private val adapter: STRecyclerViewAdapter<String, STRecyclerViewAdapter.ViewHolder> by lazy {
        object : STRecyclerViewAdapter<String, STRecyclerViewAdapter.ViewHolder>(this, (1..100).map { "I Love You for time $it" }.toMutableList()) {
            override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
                return ViewHolder(LayoutInflater.from(this@STBehaviorBottomSheetActivity).inflate(android.R.layout.test_list_item, null))
            }

            override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
                p0.itemView.findViewById<TextView>(android.R.id.text1).text = dataList[p1]
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.st_behavior_bottom_sheet_activity)

        mapView.initialize(mapType = STMapType.BAIDU)
        mapView.onCreate(this, savedInstanceState)

        recyclerView.adapter = adapter

        val bottomSheetAppbarHeight: Int = STBaseApplication.INSTANCE.resources.getDimensionPixelSize(R.dimen.bottom_sheet_appbar_height)
        val bottomSheetBehavior: BottomSheetBehavior<RelativeLayout> = BottomSheetBehavior.from(bottomSheetLayout)
        val behaviorBottomSheetCallback = STBehaviorBottomSheetCallback(handler, bottomSheetLayout, bottomSheetBehavior, bottomSheetAppbarHeight, 30f)
        bottomSheetBehavior.setBottomSheetCallback(behaviorBottomSheetCallback)

        bottomSheetTv.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }
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