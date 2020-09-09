package com.smart.template.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.smart.library.base.STBaseActivity
import com.smart.library.base.STBaseApplication
import com.smart.library.source.STBottomSheetBehavior
import com.smart.library.util.STLogUtil
import com.smart.library.util.STSystemUtil
import com.smart.library.util.STToastUtil
import com.smart.library.widget.behavior.STBottomSheetBackdropSecondFloorBehavior
import com.smart.library.widget.behavior.STBottomSheetViewPagerBehavior
import com.smart.library.widget.behavior.STBottomSheetViewPagerBehavior.Companion.getStateDescription
import com.smart.template.R
import kotlinx.android.synthetic.main.st_behavior_bottom_sheet_activity.backdropBehaviorViewPager
import kotlinx.android.synthetic.main.st_behavior_bottom_sheet_activity.bottomSheetContainer
import kotlinx.android.synthetic.main.st_behavior_bottom_sheet_activity.floatingActionButton
import kotlinx.android.synthetic.main.st_behavior_bottom_sheet_second_floor_activity.*

@Suppress("UNUSED_ANONYMOUS_PARAMETER")
class STBehaviorBottomSheetSecondFloorActivity : STBaseActivity() {
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
        setContentView(R.layout.st_behavior_bottom_sheet_second_floor_activity)

        initBottomSheetBehavior()
        initFloatingActionButton()
        initBackdropBehavior()
    }

    private fun initBottomSheetBehavior() {
        bottomSheetBehavior.enableHalfExpandedState = false
        bottomSheetBehavior.dragEnabled = true
        bottomSheetBehavior.isFitToContents = true
        bottomSheetBehavior.addBottomSheetCallback(onBottomSheetCallback)
        bottomSheetBehavior.setOnParentHeightChangedListener { parent, child, isFirst ->
            val parentHeight = parent.height
            STLogUtil.e(TAG, "onParentHeightChangedListener start isFirst=$isFirst, parentHeight=$parentHeight, getParentHeight=${bottomSheetBehavior.getParentHeight()}, currentFinalState=${getStateDescription(bottomSheetBehavior.currentFinalState)}")
            STSystemUtil.showSystemInfo(this@STBehaviorBottomSheetSecondFloorActivity)
            initSecondFloorBehavior(parentHeight - bottomSheetPeekHeight)
            bottomSheetBehavior.setStateWithResetConfigs(
                state = if (isFirst) STBottomSheetBehavior.STATE_EXPANDED else bottomSheetBehavior.currentFinalState,
                parentHeight = parentHeight,
                expandedOffset = 0,
                halfExpandedOffset = 0,
                peekHeight = bottomSheetPeekHeight
            ) {
                STLogUtil.w(TAG, "onAnimationEndCallback")
                STToastUtil.show("onAnimationEndCallback")
            }

            STLogUtil.e(TAG, "onParentHeightChangedListener end newParentHeight=${parent.height}, isFirst=$isFirst")
        }
    }

    private fun initBackdropBehavior() {
        backdropBehaviorViewPager.adapter = STBehaviorBottomSheetImagesPagerAdapter(this)
    }

    private fun initSecondFloorBehavior(secondFloorHeight: Int) {
        val backdropBehavior: STBottomSheetBackdropSecondFloorBehavior<*> = STBottomSheetBackdropSecondFloorBehavior.from(secondFloorContainer)
        backdropBehavior.bottomSheetBehavior = bottomSheetBehavior
        backdropBehavior.bottomSheetBehaviorClass = LinearLayout::class.java
        secondFloorContainer.layoutParams = secondFloorContainer.layoutParams.apply { height = secondFloorHeight }
    }

    private fun initFloatingActionButton() {
        floatingActionButton.setOnClickListener { bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED }
    }

    /**
     * B 页面 如果 android:windowIsTranslucent==true, 则不会调用 A 页面的 onRestart
     */
    override fun onRestart() {
        super.onRestart()
        STLogUtil.d(TAG, "activity: onRestart")
    }

    override fun onStart() {
        super.onStart()
        STLogUtil.d(TAG, "activity: onStart")
    }

    override fun onResume() {
        super.onResume()
        STLogUtil.d(TAG, "activity: onResume")
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        STLogUtil.d(TAG, "activity: onWindowFocusChanged hasFocus=$hasFocus")
    }

    override fun onPause() {
        super.onPause()
        STLogUtil.d(TAG, "activity: onPause")
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        STLogUtil.d(TAG, "activity: onNewIntent")
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        STLogUtil.d(TAG, "activity: onAttachedToWindow")
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        STLogUtil.d(TAG, "activity: onDetachedFromWindow")
    }

    override fun onDestroy() {
        super.onDestroy()
        STLogUtil.d(TAG, "activity: onDestroy")
        bottomSheetBehavior.removeBottomSheetCallback(onBottomSheetCallback)
    }
}