package com.smart.template.home

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
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

    private var didFirstRunOnWindowFocusChanged: Boolean = false
    private val onBottomSheetCallback: STBottomSheetBehavior.BottomSheetCallback by lazy {
        object : STBottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, percent: Float) {
                bottomSheetBehavior.dragEnabled = true
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.st_behavior_bottom_sheet_activity)

        bottomSheetBehavior.enableHalfExpandedState = true
        bottomSheetBehavior.dragEnabled = true
        bottomSheetBehavior.addBottomSheetCallback(onBottomSheetCallback)

        initBackdropBehavior(300.toPxFromDp())
        floatingActionButton.setOnClickListener { bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED }
    }

    private fun initBackdropBehavior(bottomSheetHalfExpandTop: Int) {
        val backdropBehavior: STBottomSheetBackdropBehavior<*> = STBottomSheetBackdropBehavior.from(backdropBehaviorViewPager)
        backdropBehavior.bottomSheetBehavior = bottomSheetBehavior
        backdropBehavior.bottomSheetBehaviorClass = LinearLayout::class.java
        backdropBehaviorViewPager.adapter = STBehaviorBottomSheetBackdropImagesPagerAdapter(this)
        backdropBehaviorViewPager.layoutParams = backdropBehaviorViewPager.layoutParams.apply { height = bottomSheetHalfExpandTop }
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
        resetBottomSheetView(STSystemUtil.getScreenContentHeightIncludeStatusBarAndExcludeNavigationBarOnWindowFocusChanged(this) ?: 0, STBottomSheetBehavior.STATE_HALF_EXPANDED)
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
        resetBottomSheetView(STSystemUtil.getScreenContentHeightIncludeStatusBarAndExcludeNavigationBarOnWindowFocusChanged(this))
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
            resetBottomSheetView(STSystemUtil.getScreenContentHeightIncludeStatusBarAndExcludeNavigationBarOnWindowFocusChanged(this))
            insets
        }
    }

    private fun resetBottomSheetView(parentHeight: Int?, state: Int = bottomSheetBehavior.currentFinalState) {
        STLogUtil.e(TAG, "resetBottomSheetViews parentHeight=$parentHeight")
        if (parentHeight == null || parentHeight <= 0) {
            return
        }
        bottomSheetBehavior.setStateOnParentHeightChanged(
            state = state,
            parentHeight = parentHeight,
            expandedOffset = (parentHeight * 0.24f).toInt(),
            halfExpandedOffset = (parentHeight * 0.5f).toInt(),
            peekHeight = bottomSheetPeekHeight
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        bottomSheetBehavior.removeBottomSheetCallback(onBottomSheetCallback)
    }
}