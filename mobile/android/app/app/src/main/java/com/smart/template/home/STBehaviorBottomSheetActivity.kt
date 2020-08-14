package com.smart.template.home

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.smart.library.base.STBaseActivity
import com.smart.library.base.STBaseApplication
import com.smart.library.base.toPxFromDp
import com.smart.library.source.STBottomSheetBehavior
import com.smart.library.util.STLogUtil
import com.smart.library.util.STSystemUtil
import com.smart.library.util.STToastUtil
import com.smart.library.widget.behavior.STBottomSheetBackdropHalfBehavior
import com.smart.library.widget.behavior.STBottomSheetViewPagerBehavior
import com.smart.template.R
import kotlinx.android.synthetic.main.st_behavior_bottom_sheet_activity.*

@Suppress("UNUSED_ANONYMOUS_PARAMETER")
class STBehaviorBottomSheetActivity : STBaseActivity() {
    @Suppress("PrivatePropertyName", "unused")
    private val TAG = "[BottomSheet]"

    // 面板距离页面底部距离, 面板收缩态的真实高度

    private val onBottomSheetCallback: STBottomSheetBehavior.BottomSheetCallback by lazy {
        object : STBottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {}
            override fun onSlide(bottomSheet: View, percent: Float) {
                bottomSheetBehavior.dragEnabled = true
            }
        }
    }
    private val bottomSheetPeekHeight: Int by lazy { STBaseApplication.INSTANCE.resources.getDimensionPixelSize(R.dimen.bottomSheetPeekHeight) }
    private val bottomSheetBehavior: STBottomSheetViewPagerBehavior<LinearLayout> by lazy { STBottomSheetViewPagerBehavior.from(bottomSheetContainer) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.st_behavior_bottom_sheet_activity)

        initBottomSheetBehavior()
        initBackdropBehavior(300.toPxFromDp())
        initFloatingActionButton()
    }

    private fun initBottomSheetBehavior() {
        bottomSheetBehavior.enableHalfExpandedState = true
        bottomSheetBehavior.dragEnabled = true
        bottomSheetBehavior.addBottomSheetCallback(onBottomSheetCallback)
        bottomSheetBehavior.setOnParentHeightChangedListener { parent, child, isFirst ->
            val parentHeight = parent.height
            STLogUtil.e(TAG, "onParentHeightChangedListener start isFirst=$isFirst, parentHeight=$parentHeight, getParentHeight=${bottomSheetBehavior.getParentHeight()}, currentFinalState=${bottomSheetBehavior.getStateDescription(bottomSheetBehavior.currentFinalState)}")
            STSystemUtil.showSystemInfo(this@STBehaviorBottomSheetActivity)
            bottomSheetBehavior.setStateOnParentHeightChanged(
                state = if (isFirst) STBottomSheetBehavior.STATE_HALF_EXPANDED else bottomSheetBehavior.currentFinalState,
                parentHeight = parentHeight,
                expandedOffset = (parentHeight * 0.1f).toInt(),
                halfExpandedOffset = (parentHeight * 0.6f).toInt(),
                peekHeight = bottomSheetPeekHeight
            ) {
                STLogUtil.w(TAG, "onAnimationEndCallback")
                STToastUtil.show("onAnimationEndCallback")
            }

            STLogUtil.e(TAG, "onParentHeightChangedListener end newParentHeight=${parent.height}, isFirst=$isFirst")
        }
    }

    private fun initBackdropBehavior(bottomSheetHalfExpandTop: Int) {
        val backdropBehavior: STBottomSheetBackdropHalfBehavior<*> = STBottomSheetBackdropHalfBehavior.from(backdropBehaviorViewPager)
        backdropBehavior.bottomSheetBehavior = bottomSheetBehavior
        backdropBehavior.bottomSheetBehaviorClass = LinearLayout::class.java
        backdropBehaviorViewPager.adapter = STBehaviorBottomSheetImagesPagerAdapter(this)
        backdropBehaviorViewPager.layoutParams = backdropBehaviorViewPager.layoutParams.apply { height = bottomSheetHalfExpandTop }
    }

    private fun initFloatingActionButton() {
        floatingActionButton.setOnClickListener { bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED }
    }

    override fun onDestroy() {
        super.onDestroy()
        bottomSheetBehavior.removeBottomSheetCallback(onBottomSheetCallback)
    }
}