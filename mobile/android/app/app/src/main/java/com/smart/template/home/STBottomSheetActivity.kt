package com.smart.template.home

import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.CoordinatorLayout
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.smart.library.base.STBaseActivity
import com.smart.library.base.STBaseApplication
import com.smart.library.util.STSystemUtil
import com.smart.library.widget.recyclerview.STRecyclerViewAdapter
import com.smart.template.R
import com.smart.template.home.behavior.STBottomSheetBehaviorCallback
import kotlinx.android.synthetic.main.st_bottom_sheet_activity.*

class STBottomSheetActivity : STBaseActivity() {

    private val adapter: STRecyclerViewAdapter<String, STRecyclerViewAdapter.ViewHolder> by lazy {
        object : STRecyclerViewAdapter<String, STRecyclerViewAdapter.ViewHolder>(this, (1..100).map { "I Love You for time $it" }.toMutableList()) {
            override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
                return ViewHolder(LayoutInflater.from(this@STBottomSheetActivity).inflate(android.R.layout.test_list_item, null))
            }

            override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
                p0.itemView.findViewById<TextView>(android.R.id.text1).text = dataList[p1]
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.st_bottom_sheet_activity)

        recyclerView.adapter = adapter

        val bottomSheetBehavior: BottomSheetBehavior<RelativeLayout> = BottomSheetBehavior.from(bottomSheetLayout)
        bottomSheetBehavior.setBottomSheetCallback(STBottomSheetBehaviorCallback(bottomSheetBehavior, bottomSheetAppbarHeight, 30f))

        bottomSheetTv.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }

    }

    private val bottomSheetAppbarHeight: Int by lazy { STBaseApplication.INSTANCE.resources.getDimensionPixelSize(R.dimen.bottom_sheet_appbar_height) }
    private var bottomSheetHeightUpdated: Boolean = false
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (!bottomSheetHeightUpdated) {
            val params: CoordinatorLayout.LayoutParams = bottomSheetLayout.layoutParams as CoordinatorLayout.LayoutParams
            params.height = STSystemUtil.screenHeight - bottomSheetAppbarHeight + STSystemUtil.statusBarHeight
            bottomSheetLayout.layoutParams = params
            bottomSheetHeightUpdated = true
        }
    }
}
