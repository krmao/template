package com.smart.template.home

import android.content.Intent
import android.graphics.Outline
import android.os.Bundle
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.LinearLayout
import com.smart.library.base.STBaseActivity
import com.smart.library.base.STBaseApplication
import com.smart.library.base.ensureOnGlobalLayoutListener
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

    private val backdropBehaviorHeightShow: Int = (com.smart.library.util.STSystemUtil.screenWidth * 9f / 16f).toInt()

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
        initBackdropBehavior(backdropBehaviorHeightShow)
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

    var backdropBehavior: STBottomSheetBackdropHalfBehavior<*>? = null
    private fun initBackdropBehavior(bottomSheetHalfExpandTop: Int) {
        //region viewPager 顶部圆角(不采用设置图片圆角的方式, 左右滑动时图片圆角会跟着滑动, 不好看), https://medium.com/@iamsadesh/android-ui-creating-a-layout-rounded-only-in-the-top-d60514ccab77
        backdropBehaviorViewPager.outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                val radius = 16.toPxFromDp()
                outline.setRoundRect(0, 0, view.width, view.height + radius, radius.toFloat())
            }
        }
        backdropBehaviorViewPager.clipToOutline = true
        //endregion
        backdropBehavior = STBottomSheetBackdropHalfBehavior.from(backdropBehaviorViewPager)
        backdropBehavior?.bottomSheetBehavior = bottomSheetBehavior
        backdropBehavior?.bottomSheetBehaviorClass = LinearLayout::class.java
        backdropBehaviorViewPager.adapter = STBehaviorBottomSheetImagesPagerAdapter(this)
        backdropBehaviorViewPager.layoutParams = backdropBehaviorViewPager.layoutParams.apply { height = bottomSheetHalfExpandTop }
    }

    private fun initFloatingActionButton() {
        floatingActionButton.setOnClickListener {
            resetBottomSheetState((bottomSheetBehavior.getParentHeight() * 1)) {}
        }
        floatingActionButton2.setOnClickListener {
            resetBottomSheetState((bottomSheetBehavior.getParentHeight() * 0.4).toInt()) {}
        }
        floatingActionButton3.setOnClickListener {
            resetBottomSheetState((bottomSheetBehavior.getParentHeight() * 0.3).toInt()) {}
        }

        window?.decorView?.ensureOnGlobalLayoutListener {
            STLogUtil.w(TAG, "onCreate onGlobalLayout callback ${it.height}")
            STSystemUtil.showSystemInfo(this)
        }
    }

    // 当前新版是三段式还是两段式
    private var enableHalfExpandedState = true
    private val peekHeight = 140.toPxFromDp()

    @Suppress("ReplaceJavaStaticMethodWithKotlinAnalog")
    private fun resetBottomSheetState(realHeight: Int, callback: () -> Unit) {
        val parentHeight = bottomSheetBehavior.getParentHeight()
        val minExpandedOffset = (parentHeight * 0.24f).toInt()
        val minHalfExpandedOffset = (parentHeight * 0.5f).toInt()
        val peekOffset = parentHeight - peekHeight
        val realOffset = parentHeight - realHeight
        val maxExpandedOffsetOnDisableHalf = peekOffset - 20.toPxFromDp() // 安全距离 20, 为避免正好多了几个像素这种极限情况
        val maxExpandedOffsetOnEnableHalf = minHalfExpandedOffset - 20.toPxFromDp()

        if (realOffset < minHalfExpandedOffset) {
            // 3.当面板高度大于等于中间态，不满扩展态，则扩展态自适应高度，仍支持三段式；
            enableHalfExpandedState = true

            val finalExpandedOffset = Math.max(Math.min(realOffset, maxExpandedOffsetOnEnableHalf), minExpandedOffset)
            backdropBehavior?.enableHalfExpandedState = enableHalfExpandedState
            bottomSheetBehavior.enableHalfExpandedState = enableHalfExpandedState
            bottomSheetBehavior.setStateOnParentHeightChanged(
                state = STBottomSheetBehavior.STATE_HALF_EXPANDED,
                forceSettlingOnSameState = true,
                parentHeight = parentHeight,
                expandedOffset = finalExpandedOffset,
                halfExpandedOffset = minHalfExpandedOffset,
                peekHeight = peekHeight
            ) {
                callback.invoke()
                STLogUtil.w(TAG, "onAnimationEndCallback")
            }
        } else {
            // 1.不会存在不满收缩态
            // 2.当高于收缩态，低于中间态，则存在两段式（分别为中间态和收缩态），其中中间态自适应高度，箭头仍是横线，不支持上拉，支持下拉变为收缩态；
            enableHalfExpandedState = false

            val finalExpandedOffset = Math.min(maxExpandedOffsetOnDisableHalf, realOffset)
            backdropBehavior?.enableHalfExpandedState = enableHalfExpandedState
            bottomSheetBehavior.enableHalfExpandedState = enableHalfExpandedState
            bottomSheetBehavior.setStateOnParentHeightChanged(
                state = STBottomSheetBehavior.STATE_EXPANDED,
                forceSettlingOnSameState = true,
                parentHeight = parentHeight,
                expandedOffset = finalExpandedOffset,
                halfExpandedOffset = 0,
                peekHeight = peekHeight
            ) {
                callback.invoke()
                STLogUtil.w(TAG, "onAnimationEndCallback")
            }
        }
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