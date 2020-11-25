package com.smart.template.home.test

import android.content.Intent
import android.graphics.Outline
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.ImageView
import android.widget.LinearLayout
import com.smart.library.base.STBaseActivity
import com.smart.library.STInitializer
import com.smart.library.base.ensureOnGlobalLayoutListener
import com.smart.library.base.toPxFromDp
import com.smart.library.source.STBottomSheetBehavior
import com.smart.library.util.STLogUtil
import com.smart.library.util.STSystemUtil
import com.smart.library.util.STToastUtil
import com.smart.library.util.STViewUtil
import com.smart.library.widget.behavior.STBottomSheetBackdropHalfBehavior
import com.smart.library.widget.behavior.STBottomSheetTouchContainerConstrainLayout
import com.smart.library.widget.behavior.STBottomSheetViewPagerBehavior
import com.smart.library.widget.behavior.STBottomSheetViewPagerBehavior.Companion.STATE_COLLAPSED
import com.smart.library.widget.behavior.STBottomSheetViewPagerBehavior.Companion.STATE_EXPANDED
import com.smart.library.widget.behavior.STBottomSheetViewPagerBehavior.Companion.STATE_HALF_EXPANDED
import com.smart.library.widget.behavior.STBottomSheetViewPagerBehavior.Companion.getStateDescription
import com.smart.library.widget.behavior.STNestedScrollView
import com.smart.template.R
import kotlinx.android.synthetic.main.final_behavior_bottom_sheet_activity.*

@Suppress("UNUSED_ANONYMOUS_PARAMETER")
class FinalBehaviorBottomSheetActivity : STBaseActivity() {
    @Suppress("PrivatePropertyName", "unused")
    private val TAG = "[BottomSheet]"

    private val arrowPanelHeight: Int = 32.toPxFromDp()
    private val enableDragOnlyOnSpecialTouchLayout: Boolean = true
    private val nestedScrollView: STNestedScrollView by lazy { findViewById<STNestedScrollView>(R.id.nestedScrollView) }
    private val touchLayout: STBottomSheetTouchContainerConstrainLayout by lazy { findViewById<STBottomSheetTouchContainerConstrainLayout>(R.id.touchLayout) }
    private val arrowIv: ImageView by lazy { findViewById<ImageView>(R.id.arrowIv) }

    private val backdropBehaviorHeightShow: Int = (STSystemUtil.screenWidth * 9f / 16f).toInt()

    // 面板距离页面底部距离, 面板收缩态的真实高度

    private val onBottomSheetCallback: STBottomSheetBehavior.BottomSheetCallback by lazy {
        object : STBottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                STLogUtil.w(TAG, "onStateChanged newState=${getStateDescription(newState)}, bottomSheet.top=${bottomSheet.top}")
                setArrowStatus(newState)

                if (newState == STATE_EXPANDED || newState == STATE_HALF_EXPANDED || newState == STATE_COLLAPSED) {
                    STToastUtil.show("onStateChanged:${getStateDescription(newState)}")
                }
            }

            override fun onSlide(bottomSheet: View, percent: Float) {
                STLogUtil.w(TAG, "onSlide percent=$percent, bottomSheet.top=${bottomSheet.top}")
                bottomSheetBehavior.dragEnabled = true
            }
        }
    }
    private val bottomSheetPeekHeight: Int by lazy { STInitializer.application()?.resources?.getDimensionPixelSize(R.dimen.finalBottomSheetPeekHeight) ?: 0 }
    private val bottomSheetBehavior: STBottomSheetViewPagerBehavior<LinearLayout> by lazy { STBottomSheetViewPagerBehavior.from(bottomSheetContainer) }
    private val imageContentView: ImageView by lazy { findViewById<ImageView>(R.id.imageContentView) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.final_behavior_bottom_sheet_activity)

        initBottomSheetBehavior()
        initBackdropBehavior(backdropBehaviorHeightShow)
        initFloatingActionButton()

        //region 箭头逻辑与拖拽有效范围
        bottomSheetBehavior.setDragOnlyOnSpecialTouchLayout(
            enableDragOnlyOnSpecialTouchLayout = { enableDragOnlyOnSpecialTouchLayout },
            touchLayout = touchLayout,
            nestedScrollView = nestedScrollView
        )
        touchLayout.setOnClickListener {
            if (!STViewUtil.isDoubleClicked()) {
                STLogUtil.d(TAG, "arrowIv clicked")
                when (bottomSheetBehavior.state) {
                    STATE_COLLAPSED -> {
                        touchLayout.post {
                            bottomSheetBehavior.state = if (bottomSheetBehavior.enableHalfExpandedState) STATE_HALF_EXPANDED else STATE_EXPANDED
                        }
                    }
                    STATE_EXPANDED -> {
                        touchLayout.post {
                            bottomSheetBehavior.state = if (bottomSheetBehavior.enableHalfExpandedState) STATE_HALF_EXPANDED else STATE_COLLAPSED
                        }
                    }
                    STATE_HALF_EXPANDED -> {
                        touchLayout.post {
                            bottomSheetBehavior.state = STATE_EXPANDED
                        }
                    }
                }
            }
        }
        //endregion
    }

    //region 箭头相关与拖拽区域相关
    private fun setArrowStatus(panelState: Int) {
        when (panelState) {
            STATE_EXPANDED -> {
                if (bottomSheetBehavior.enableHalfExpandedState) {
                    arrowIv.setImageResource(R.drawable.st_icon_arrow_down)
                    arrowIv.setPadding(0, 0, 0, 0)
                } else {
                    arrowIv.setImageResource(R.drawable.st_icon_arrow_middle)
                    arrowIv.setPadding(0, 0, 0, 3.toPxFromDp())
                }
            }
            STATE_COLLAPSED -> {
                arrowIv.setImageResource(R.drawable.st_icon_arrow_up)
                arrowIv.setPadding(0, 0, 0, 0)
            }
            STATE_HALF_EXPANDED -> {
                arrowIv.setImageResource(R.drawable.st_icon_arrow_middle)
                arrowIv.setPadding(0, 0, 0, 3.toPxFromDp())
            }
        }
        // arrowIv.animateRotation(if (up) 0f else 180f) // 动画方式
    }
    //endregion

    private fun initBottomSheetBehavior() {
        bottomSheetBehavior.addBottomSheetCallback(onBottomSheetCallback)
        bottomSheetBehavior.resetNestedViewsLayoutParamsByBottomSheetContainerHeight = { currentBottomSheetContainerHeightByState ->
            val targetHeight = currentBottomSheetContainerHeightByState - arrowPanelHeight
            val params: ViewGroup.LayoutParams = nestedScrollView.layoutParams
            val oldHeight = params.height
            STLogUtil.sync { STLogUtil.w(TAG, "resetBottomSheetContainerNestedViewsLayoutParamsOnSlide targetHeight=$targetHeight, oldScrollY=${nestedScrollView.scrollY}") }
            if (params.height != targetHeight) {
                params.height = targetHeight
                nestedScrollView.layoutParams = params
                STLogUtil.w(TAG, "resetBottomSheetContainerNestedViewsLayoutParamsOnSlide height=${nestedScrollView.layoutParams.height}, oldHeight$oldHeight, newScrollY=${nestedScrollView.scrollY}")
            } else {
                STLogUtil.e(TAG, "resetBottomSheetContainerNestedViewsLayoutParamsOnSlide repeat ignore set, height=${nestedScrollView.layoutParams.height}")
            }
        }
    }

    private var backdropBehavior: STBottomSheetBackdropHalfBehavior<*>? = null
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
        backdropBehavior?.bottomSheetBehaviorClass = STBottomSheetViewPagerBehavior::class.java
        backdropBehavior?.bottomSheetContainerLayoutClass = LinearLayout::class.java
        backdropBehavior?.dependCollapsedOffset = bottomSheetBehavior.getCollapsedOffset()
        backdropBehavior?.dependHalfExpandedOffset = bottomSheetBehavior.getHalfExpandedOffset()
        backdropBehavior?.dependExpandedOffset = bottomSheetBehavior.expandedOffset
        backdropBehaviorViewPager.adapter = FinalBehaviorBottomSheetImagesPagerAdapter(this)
        backdropBehaviorViewPager.layoutParams = backdropBehaviorViewPager.layoutParams.apply { height = bottomSheetHalfExpandTop }
    }

    private fun initFloatingActionButton() {
        floatingActionButton.setOnClickListener {
            val bottomSheetContentHeight: Int = (bottomSheetBehavior.getParentHeight() * 0.9f).toInt()
            imageContentView.layoutParams = imageContentView.layoutParams.apply { height = bottomSheetContentHeight }

            bottomSheetBehavior.setStateByRealContentHeight(
                parentHeight = bottomSheetBehavior.getParentHeight(),
                peekHeight = bottomSheetPeekHeight,
                bottomSheetContentHeight = bottomSheetContentHeight,
                callbackBeforeSetState = { newEnableHalfExpandedState ->
                    backdropBehavior?.enableHalfExpandedState = newEnableHalfExpandedState
                })
        }
        floatingActionButton2.setOnClickListener {
            val bottomSheetContentHeight: Int = (bottomSheetBehavior.getParentHeight() * 0.4).toInt()
            imageContentView.layoutParams = imageContentView.layoutParams.apply { height = bottomSheetContentHeight }

            bottomSheetBehavior.setStateByRealContentHeight(parentHeight = bottomSheetBehavior.getParentHeight(), peekHeight = bottomSheetPeekHeight, bottomSheetContentHeight = bottomSheetContentHeight, callbackBeforeSetState = { newEnableHalfExpandedState ->
                backdropBehavior?.enableHalfExpandedState = newEnableHalfExpandedState
            })
        }
        floatingActionButton3.setOnClickListener {
            val bottomSheetContentHeight: Int = (bottomSheetBehavior.getParentHeight() * 0.2).toInt()
            val imageViewLayoutParams: LinearLayout.LayoutParams = imageContentView.layoutParams as LinearLayout.LayoutParams
            imageViewLayoutParams.height = bottomSheetContentHeight
            imageContentView.layoutParams = imageViewLayoutParams

            bottomSheetBehavior.setStateByRealContentHeight(parentHeight = bottomSheetBehavior.getParentHeight(), peekHeight = bottomSheetPeekHeight, bottomSheetContentHeight = bottomSheetContentHeight, callbackBeforeSetState = { newEnableHalfExpandedState ->
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