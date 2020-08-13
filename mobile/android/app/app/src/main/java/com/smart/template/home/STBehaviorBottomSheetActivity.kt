package com.smart.template.home

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.smart.library.base.STBaseActivity
import com.smart.library.base.STBaseApplication
import com.smart.library.base.toPxFromDp
import com.smart.library.source.STBottomSheetBehavior
import com.smart.library.util.STLogUtil
import com.smart.library.util.STSystemUtil
import com.smart.library.widget.behavior.STBottomSheetBackdropBehavior
import com.smart.library.widget.behavior.STBottomSheetViewPagerBehavior
import com.smart.template.R
import kotlinx.android.synthetic.main.st_behavior_bottom_sheet_activity.*

@Suppress("UNUSED_ANONYMOUS_PARAMETER")
class STBehaviorBottomSheetActivity : STBaseActivity() {
    @Suppress("PrivatePropertyName", "unused")
    private val TAG = "[BottomSheet]"

    // 面板距离页面底部距离, 面板收缩态的真实高度
    private val bottomSheetPeekHeight: Int by lazy { STBaseApplication.INSTANCE.resources.getDimensionPixelSize(R.dimen.bottomSheetPeekHeight) }
    private val bottomSheetBehavior: STBottomSheetViewPagerBehavior<LinearLayout> by lazy { STBottomSheetViewPagerBehavior.from(bottomSheetContainer) }

    private var currentBottomSheetParentHeight: Int = 0
    private var currentBottomSheetExpandMarginTop: Int = 0
    private var currentBottomSheetHalfExpandTop: Int = 0
    private var currentState: Int = STBottomSheetBehavior.STATE_COLLAPSED
    private var didFirstRunOnWindowFocusChanged: Boolean = false
    private val onBottomSheetCallback: STBottomSheetBehavior.BottomSheetCallback by lazy {
        object : STBottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, percent: Float) {
                bottomSheetBehavior.dragEnabled = true
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (STLogUtil.debug) STLogUtil.d(TAG, "onStateChanged newState=${stateString(newState)}, currentState=${stateString(bottomSheetBehavior.state)}")
                when (newState) {
                    STBottomSheetBehavior.STATE_EXPANDED -> {
                        currentState = newState
                    }

                    STBottomSheetBehavior.STATE_HALF_EXPANDED, STBottomSheetBehavior.STATE_COLLAPSED -> {
                        currentState = newState
                    }

                    STBottomSheetBehavior.STATE_DRAGGING -> {
                    }
                    STBottomSheetBehavior.STATE_SETTLING -> {
                    }
                    else -> {
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.st_behavior_bottom_sheet_activity)

        bottomSheetBehavior.enableHalfExpandedState = true
        bottomSheetBehavior.dragEnabled = true
        bottomSheetBehavior.setBottomSheetCallback(onBottomSheetCallback)

        initBackdropBehavior(300.toPxFromDp())
        floatingActionButton.setOnClickListener { bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        STLogUtil.e(TAG, "onWindowFocusChanged hasFocus=$hasFocus")
        if (hasFocus) {
            if (!didFirstRunOnWindowFocusChanged) {
                didFirstRunOnWindowFocusChanged = true
                onWindowFocusChangedFirstRun(hasFocus)
            }
        }
    }

    private fun onWindowFocusChangedFirstRun(hasFocus: Boolean) {
        STLogUtil.e(TAG, "onWindowFocusChangedFirstRun hasFocus=$hasFocus")
        resetBottomSheetViews(STSystemUtil.getScreenContentHeightIncludeStatusBarAndExcludeNavigationBarOnWindowFocusChanged(this) ?: 0, STBottomSheetBehavior.STATE_HALF_EXPANDED)
        initOnApplyWindowInsetsListener()
    }

    /**
     * 当在设置->系统导航方式 切换导航方式时, 会调用 onConfigurationChanged
     * 此时获取到的 状态栏+内容高度是正确的
     * 其中 onApplyWindowInsetsListener 也会触发一次, 但是获取到的值是不正确的, 配置更改之前的高度
     */
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        STLogUtil.e(TAG, "onConfigurationChanged newConfig=$newConfig")
        STSystemUtil.showSystemInfo(this)
        resetBottomSheetViews(STSystemUtil.getScreenContentHeightIncludeStatusBarAndExcludeNavigationBarOnWindowFocusChanged(this) ?: 0, currentState)
    }

    /**
     * 当显示/隐藏虚拟导航栏时, 会触发 onApplyWindowInsetsListener (注意不是切换导航方式, 切换配置在触发的onConfigurationChanged中才是正确的)
     *
     * 注意:软键盘的展开/收起
     */
    private fun initOnApplyWindowInsetsListener() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { view: View?, insets: WindowInsetsCompat ->
            val navigationBarHeight = insets.systemWindowInsetBottom // 无效, 华为 P20 无论隐藏/显示虚拟导航栏, 都显示为 0, 需要通过 getVisibleNavigationBarHeightOnWindowFocusChanged 重新计算
            STLogUtil.e(TAG, "initOnApplyWindowInsetsListener: onApplyWindowInsetsListener navigationBarHeight=$navigationBarHeight, ${STSystemUtil.getScreenContentHeightIncludeStatusBarAndExcludeNavigationBarOnWindowFocusChanged(this) ?: 0}")
            STSystemUtil.showSystemInfo(this@STBehaviorBottomSheetActivity)
            resetBottomSheetViews(STSystemUtil.getScreenContentHeightIncludeStatusBarAndExcludeNavigationBarOnWindowFocusChanged(this) ?: 0, currentState)
            insets
        }
    }


    private fun initBackdropBehavior(bottomSheetHalfExpandTop: Int) {
        val backdropBehavior: STBottomSheetBackdropBehavior<*> = STBottomSheetBackdropBehavior.from(backdropBehaviorViewPager)
        backdropBehavior.bottomSheetBehavior = bottomSheetBehavior
        backdropBehavior.bottomSheetBehaviorClass = LinearLayout::class.java
        backdropBehaviorViewPager.adapter = STBehaviorBottomSheetBackdropImagesPagerAdapter(this)
        backdropBehaviorViewPager.layoutParams = backdropBehaviorViewPager.layoutParams.apply {
            height = bottomSheetHalfExpandTop
        }
    }

    private fun resetBottomSheetViews(bottomSheetParentHeight: Int, state: Int = currentState) {
        STLogUtil.e(TAG, "resetBottomSheetViews bottomSheetParentHeight=$bottomSheetParentHeight")
        if (bottomSheetParentHeight <= 0 || currentBottomSheetParentHeight == bottomSheetParentHeight) {
            STLogUtil.e(TAG, "resetBottomSheetViews bottomSheetParentHeight=$bottomSheetParentHeight, currentBottomSheetParentHeight=$currentBottomSheetParentHeight, error or repeat return")
            return
        }
        // 屏幕可视范围总高度为 状态栏 + 内容高度, 面板总高度
        currentBottomSheetParentHeight = bottomSheetParentHeight

        // 面板距离页面顶部距离, 面板距离屏幕顶部的距离
        currentBottomSheetExpandMarginTop = (bottomSheetParentHeight * 0.24f).toInt()

        // 面板中间距离, 面板距离页面底部距离, 面板一半的高度
        currentBottomSheetHalfExpandTop = (bottomSheetParentHeight * 0.5f).toInt()

        // 重设最大高度
        setBottomSheetContainerHeight(getBottomSheetContainerVisibleHeightByState(STBottomSheetBehavior.STATE_EXPANDED))
        bottomSheetBehavior.bottomSheetHalfExpandTop = currentBottomSheetHalfExpandTop
        bottomSheetBehavior.setHalfExpandedOffset(currentBottomSheetHalfExpandTop)
        bottomSheetBehavior.calculateCollapsedOffset()
        bottomSheetBehavior.peekHeight = bottomSheetPeekHeight
        setState(state)
    }

    private fun setState(state: Int = currentState) {
        bottomSheetBehavior.state = state
        currentState = state
    }

    private fun stateString(state: Int): String = when (state) {
        STBottomSheetBehavior.STATE_DRAGGING -> "STATE_DRAGGING"
        STBottomSheetBehavior.STATE_SETTLING -> "STATE_SETTLING"
        STBottomSheetBehavior.STATE_EXPANDED -> "STATE_EXPANDED"
        STBottomSheetBehavior.STATE_COLLAPSED -> "STATE_COLLAPSED"
        STBottomSheetBehavior.STATE_HIDDEN -> "STATE_HIDDEN"
        STBottomSheetBehavior.STATE_HALF_EXPANDED -> "STATE_HALF_EXPANDED"
        STBottomSheetBehavior.PEEK_HEIGHT_AUTO -> "PEEK_HEIGHT_AUTO"
        else -> "UNKNOWN_$state"
    }

    /**
     * 浮层面板容器的可视高度
     */
    private fun getBottomSheetContainerVisibleHeightByState(state: Int): Int {
        val heightOnStateExpanded: Int = currentBottomSheetParentHeight - currentBottomSheetExpandMarginTop
        val heightOnStateHalfExpanded: Int = currentBottomSheetHalfExpandTop
        val heightOnStateCollapsed: Int = bottomSheetPeekHeight

        val panelHeight = when (state) {
            STBottomSheetViewPagerBehavior.STATE_EXPANDED -> heightOnStateExpanded
            STBottomSheetViewPagerBehavior.STATE_HALF_EXPANDED -> heightOnStateHalfExpanded
            STBottomSheetViewPagerBehavior.STATE_COLLAPSED -> heightOnStateCollapsed
            else -> heightOnStateHalfExpanded
        }

        STLogUtil.sync { STLogUtil.d(TAG, "getPanelHeightByState ${stateString(state)}, panelHeight=$panelHeight") }
        return panelHeight
    }

    /**
     * 设置浮层面板容器的高度
     */
    private fun setBottomSheetContainerHeight(height: Int) {
        val params: CoordinatorLayout.LayoutParams = bottomSheetContainer.layoutParams as CoordinatorLayout.LayoutParams
        params.height = height
        bottomSheetContainer.layoutParams = params
    }
}