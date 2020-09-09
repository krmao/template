package com.smart.template.home

import android.content.Intent
import android.graphics.Outline
import android.os.Bundle
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.ImageView
import android.widget.LinearLayout
import com.smart.library.base.STBaseActivity
import com.smart.library.base.STBaseApplication
import com.smart.library.base.ensureOnGlobalLayoutListener
import com.smart.library.base.toPxFromDp
import com.smart.library.source.STBottomSheetBehavior
import com.smart.library.util.STLogUtil
import com.smart.library.util.STSystemUtil
import com.smart.library.widget.behavior.STBottomSheetBackdropHalfBehavior
import com.smart.library.widget.behavior.STBottomSheetViewPagerBehavior
import com.smart.library.widget.behavior.STBottomSheetViewPagerBehavior.Companion.getStateDescription
import com.smart.template.R
import kotlinx.android.synthetic.main.st_behavior_bottom_sheet_activity.*

@Suppress("UNUSED_ANONYMOUS_PARAMETER")
class STBehaviorBottomSheetActivity : STBaseActivity() {
    @Suppress("PrivatePropertyName", "unused")
    private val TAG = "[BottomSheet]"

    private val backdropBehaviorHeightShow: Int = (STSystemUtil.screenWidth * 9f / 16f).toInt()

    // 面板距离页面底部距离, 面板收缩态的真实高度

    private val onBottomSheetCallback: STBottomSheetBehavior.BottomSheetCallback by lazy {
        object : STBottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                STLogUtil.w(TAG, "onStateChanged newState=${getStateDescription(newState)}, bottomSheet.top=${bottomSheet.top}")
            }

            override fun onSlide(bottomSheet: View, percent: Float) {
                STLogUtil.w(TAG, "onSlide percent=$percent, bottomSheet.top=${bottomSheet.top}")
                bottomSheetBehavior.dragEnabled = true
            }
        }
    }
    private val bottomSheetPeekHeight: Int by lazy { STBaseApplication.INSTANCE.resources.getDimensionPixelSize(R.dimen.bottomSheetPeekHeight) }
    private val bottomSheetBehavior: STBottomSheetViewPagerBehavior<LinearLayout> by lazy { STBottomSheetViewPagerBehavior.from(bottomSheetContainer) }
    private val imageContentView: ImageView by lazy { findViewById<ImageView>(R.id.imageContentView) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.st_behavior_bottom_sheet_activity)

        initBottomSheetBehavior()
        initBackdropBehavior(backdropBehaviorHeightShow)
        initFloatingActionButton()
    }

    private fun initBottomSheetBehavior() {
        bottomSheetBehavior.dragEnabled = true
        bottomSheetBehavior.addBottomSheetCallback(onBottomSheetCallback)
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
            floatingActionButton.postDelayed({
                val bottomSheetContentHeight: Int = (bottomSheetBehavior.getParentHeight() * 0.76f).toInt()
                imageContentView.layoutParams = imageContentView.layoutParams.apply { height = bottomSheetContentHeight }

                bottomSheetBehavior.setStateByRealHeight(parentHeight = bottomSheetBehavior.getParentHeight(), peekHeight = bottomSheetPeekHeight, bottomSheetContentHeight = bottomSheetContentHeight, callbackBeforeSetState = { newEnableHalfExpandedState ->
                    backdropBehavior?.enableHalfExpandedState = newEnableHalfExpandedState
                })
            },700)
        }
        floatingActionButton2.setOnClickListener {
            floatingActionButton2.postDelayed({
                val bottomSheetContentHeight: Int = (bottomSheetBehavior.getParentHeight() * 0.4).toInt()
                imageContentView.layoutParams = imageContentView.layoutParams.apply { height = bottomSheetContentHeight }

                bottomSheetBehavior.setStateByRealHeight(parentHeight = bottomSheetBehavior.getParentHeight(), peekHeight = bottomSheetPeekHeight, bottomSheetContentHeight = bottomSheetContentHeight, callbackBeforeSetState = { newEnableHalfExpandedState ->
                    backdropBehavior?.enableHalfExpandedState = newEnableHalfExpandedState
                })
            }, 700)
        }
        floatingActionButton3.setOnClickListener {
            val bottomSheetContentHeight: Int = (bottomSheetBehavior.getParentHeight() * 0.2).toInt()
            val imageViewLayoutParams: LinearLayout.LayoutParams = imageContentView.layoutParams as LinearLayout.LayoutParams
            imageViewLayoutParams.height = bottomSheetContentHeight
            imageContentView.layoutParams = imageViewLayoutParams

            bottomSheetBehavior.setStateByRealHeight(parentHeight = bottomSheetBehavior.getParentHeight(), peekHeight = bottomSheetPeekHeight, bottomSheetContentHeight = bottomSheetContentHeight, callbackBeforeSetState = { newEnableHalfExpandedState ->
                backdropBehavior?.enableHalfExpandedState = newEnableHalfExpandedState
            })
        }

        window?.decorView?.ensureOnGlobalLayoutListener {
            STLogUtil.w(TAG, "onCreate onGlobalLayout callback ${it.height}")
            STSystemUtil.showSystemInfo(this)
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