package com.smart.template.home

import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.CoordinatorLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.smart.library.base.STBaseActivity
import com.smart.library.widget.recyclerview.STRecyclerViewAdapter
import com.smart.template.R
import kotlinx.android.synthetic.main.st_bottom_sheet_activity.*

/**
 *  bottom_sheet不添加android:background="#ffffff"发现是透明的
 */
class STBottomSheetActivity : STBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.st_bottom_sheet_activity)

        recyclerView.adapter = object : STRecyclerViewAdapter<String, STRecyclerViewAdapter.ViewHolder>(this, (1..100).map { "I Love You for time $it" }.toMutableList()) {
            override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
                return ViewHolder(LayoutInflater.from(this@STBottomSheetActivity).inflate(android.R.layout.test_list_item, null))
            }

            override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
                p0.itemView.findViewById<TextView>(android.R.id.text1).text = dataList[p1]
            }
        }

        // http://s0developer0android0com.icopy.site/reference/com/google/android/material/bottomsheet/BottomSheetBehavior
        val bottomSheetBehavior: BottomSheetBehavior<RelativeLayout> = BottomSheetBehavior.from(bottomSheetLayout)
        bottomSheetBehavior.isFitToContents = false
        bottomSheetBehavior.isHideable = false
        bottomSheetBehavior.skipCollapsed = false // 设置此底页在展开一次后隐藏时是否应跳过折叠状态.

        bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetTv.visibility = View.INVISIBLE
                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    bottomSheetTv.visibility = View.VISIBLE
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                println("onSlide: " + bottomSheet.top + " ; " + titleBar.height)
                bottomSheetTv.visibility = View.INVISIBLE
                if (bottomSheet.top == titleBar.height) {
                    titleBar.visibility = View.VISIBLE
                    titleBar.alpha = slideOffset
                    titleBar.translationY = (titleBar.height - bottomSheet.top).toFloat()
                } else {
                    titleBar.visibility = View.INVISIBLE
                }
            }
        })

        titleBar.visibility = View.INVISIBLE
        bottomSheetTv.setOnClickListener { bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED }
    }

    private var setBottomSheetHeight = false
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        //指定design_bottom_sheet的高度 ，根据需要可以自己修改
        if (!setBottomSheetHeight) {
            val params = bottomSheetLayout.layoutParams as CoordinatorLayout.LayoutParams
            params.height = recyclerView.height
            bottomSheetLayout.layoutParams = params
            setBottomSheetHeight = true
        }
    }
}
