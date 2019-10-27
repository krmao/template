package com.smart.template.home.behavior

import android.os.Handler
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.CoordinatorLayout
import android.view.View
import android.view.ViewTreeObserver
import com.smart.library.base.toDpFromPx
import com.smart.library.util.STLogUtil
import com.smart.library.util.STSystemUtil
import kotlin.math.max
import kotlin.math.min

/*
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.st_behavior_bottom_sheet_activity)

    // init bottomSheet behavior
    val bottomSheetAppbarHeight: Int = STBaseApplication.INSTANCE.resources.getDimensionPixelSize(R.dimen.bottom_sheet_appbar_height)
    val bottomSheetBehavior: BottomSheetBehaviorForViewPager<LinearLayout> = BottomSheetBehaviorForViewPager.from(bottomSheetLayout)
    val behaviorBottomSheetCallback = STBottomSheetCallback(handler, bottomSheetLayout, bottomSheetBehavior, bottomSheetAppbarHeight, 30f)
    bottomSheetBehavior.setBottomSheetCallback(behaviorBottomSheetCallback)
}

<?xml version="1.0" encoding="utf-8"?>
<android.support.design.widget.CoordinatorLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@drawable/st_beauty"
    tools:ignore="HardcodedText">

    <android.support.design.widget.AppBarLayout
        android:id="@+id/titleBar"
        android:layout_width="3dp"
        android:layout_height="wrap_content"
        android:layout_gravity="top"
        android:visibility="visible"
        app:layout_anchor="@id/behaviorView"
        app:layout_anchorGravity="top">

        <android.support.v7.widget.Toolbar
            android:layout_width="match_parent"
            android:layout_height="@dimen/bottom_sheet_appbar_height"
            app:title="巅峰榜 流行指数 第64天" />
    </android.support.design.widget.AppBarLayout>

    <RelativeLayout
        android:id="@+id/behaviorView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="#ffffff"
        app:behavior_hideable="false"
        app:behavior_peekHeight="80dp"
        app:layout_behavior="@string/bottom_sheet_behavior"
        tools:ignore="MissingPrefix">

        <TextView
            android:id="@+id/bottomSheetTv"
            android:layout_width="match_parent"
            android:layout_height="40dp"
            android:gravity="center"
            android:text="这是一个BottomSheet" />

        <View
            android:id="@+id/redLine"
            android:layout_width="match_parent"
            android:layout_height="1dp"
            android:layout_below="@id/bottomSheetTv"
            android:background="@color/red" />

        <android.support.v7.widget.RecyclerView
            android:id="@+id/recyclerView"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_below="@id/redLine"
            android:layout_alignParentTop="@id/redLine"
            android:layout_alignParentBottom="true"
            app:layoutManager="android.support.v7.widget.LinearLayoutManager" />
    </RelativeLayout>

</android.support.design.widget.CoordinatorLayout>
*/

/**
 * 从底部拖动滑出的面板, 具有三段状态高度
 * http://s0developer0android0com.icopy.site/reference/com/google/android/material/bottomsheet/BottomSheetBehavior
 *
 * @param dragOffsetPercent 拖拽滑动超过 整体可滑动范围的百分之多少, 就可以滚动到 顺着滑动方向的 下一个状态, 有效范围 [1,99]
 * @param bottomSheetExpandTop expand 完全展开状态 bottom sheet layout top 值, 影响拖动触发状态改变的 位移 距离
 * @param behaviorView 设置了 app:layout_behavior="@string/bottom_sheet_behavior" 的那个 view
 */
@Suppress("MemberVisibilityCanBePrivate")
class STBottomSheetCallback @JvmOverloads constructor(private val handler: Handler = Handler(), val behaviorView: View, private val bottomSheetBehavior: BottomSheetBehavior<out View>, private val bottomSheetExpandTop: Int = 0, dragOffsetPercent: Float = 30f, private val onStateChanged: ((bottomSheet: View, newState: Int) -> Unit)? = null, private val onSlide: ((bottomSheet: View, slideOffset: Float) -> Unit)? = null) : BottomSheetBehavior.BottomSheetCallback() {

    init {
        behaviorView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val viewTreeObserver: ViewTreeObserver = behaviorView.viewTreeObserver
                if (viewTreeObserver.isAlive) {
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
                val params: CoordinatorLayout.LayoutParams = behaviorView.layoutParams as CoordinatorLayout.LayoutParams
                params.height = STSystemUtil.screenHeight - bottomSheetExpandTop + STSystemUtil.statusBarHeight
                behaviorView.layoutParams = params
            }
        })
    }

    private val tag: String = "sheet"
    private val screenFullHeight: Int = STSystemUtil.screenHeight + STSystemUtil.statusBarHeight
    val bottomSheetCollapsedTop: Int = screenFullHeight - bottomSheetBehavior.peekHeight
    val bottomSheetHalfExpandTop: Int = (screenFullHeight / 2f).toInt()
    private var lastBottomSheetDragTop: Int = bottomSheetCollapsedTop
        private set(value) {
            if (lastBottomSheetDragTop != -1) {
                pullTopToBottom = value - field > 0
            } else {
                pullTopToBottom = null
            }
            field = value
        }
    private var pullTopToBottom: Boolean? = null
    private val offsetTenPercent = (bottomSheetCollapsedTop - bottomSheetExpandTop) / min(99f, max(dragOffsetPercent, 1f))

    private var filterOnStateChangedRunnable: Runnable? = null
    private fun filterOnStateChanged(bottomSheet: View, newState: Int, description: String) {
        if (filterOnStateChangedRunnable != null) {
            handler.removeCallbacks(filterOnStateChangedRunnable)
        }
        filterOnStateChangedRunnable = null
        filterOnStateChangedRunnable = object : Runnable {
            override fun run() {
                STLogUtil.e(tag, "STATE_FILTER : $description")
                onStateChanged?.invoke(bottomSheet, newState)
            }
        }
        handler.postDelayed(filterOnStateChangedRunnable, 100)
    }

    override fun onStateChanged(bottomSheet: View, newState: Int) {
        val bottomSheetCurrentTop: Int = bottomSheet.top
        when (newState) {
            BottomSheetBehavior.STATE_HIDDEN -> {
                lastBottomSheetDragTop = -1
                STLogUtil.e(tag, "STATE_HIDDEN")

                filterOnStateChanged(bottomSheet, newState, "STATE_HIDDEN")
            }
            BottomSheetBehavior.STATE_COLLAPSED -> {
                lastBottomSheetDragTop = -1
                STLogUtil.e(tag, "STATE_COLLAPSED")
                STLogUtil.d(tag, "screenFullHeight=${screenFullHeight.toDpFromPx()}")
                STLogUtil.d(tag, "bottomSheetCollapsedTop=${bottomSheetCollapsedTop.toDpFromPx()}")
                STLogUtil.d(tag, "bottomSheetHalfExpandTop=${bottomSheetHalfExpandTop.toDpFromPx()}")
                STLogUtil.d(tag, "bottomSheetExpandTop=${bottomSheetExpandTop.toDpFromPx()}")
                STLogUtil.d(tag, "bottomSheetCurrentTop=${bottomSheetCurrentTop.toDpFromPx()}")

                filterOnStateChanged(bottomSheet, newState, "STATE_COLLAPSED")
            }
            BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                lastBottomSheetDragTop = -1
                STLogUtil.e(tag, "STATE_HALF_EXPANDED")
                STLogUtil.d(tag, "screenFullHeight=${screenFullHeight.toDpFromPx()}")
                STLogUtil.d(tag, "bottomSheetCollapsedTop=${bottomSheetCollapsedTop.toDpFromPx()}")
                STLogUtil.d(tag, "bottomSheetHalfExpandTop=${bottomSheetHalfExpandTop.toDpFromPx()}")
                STLogUtil.d(tag, "bottomSheetExpandTop=${bottomSheetExpandTop.toDpFromPx()}")
                STLogUtil.d(tag, "bottomSheetCurrentTop=${bottomSheetCurrentTop.toDpFromPx()}")

                filterOnStateChanged(bottomSheet, newState, "STATE_HALF_EXPANDED")
            }
            BottomSheetBehavior.STATE_EXPANDED -> {
                lastBottomSheetDragTop = -1
                STLogUtil.e(tag, "STATE_EXPANDED")
                STLogUtil.d(tag, "screenFullHeight=${screenFullHeight.toDpFromPx()}")
                STLogUtil.d(tag, "bottomSheetCollapsedTop=${bottomSheetCollapsedTop.toDpFromPx()}")
                STLogUtil.d(tag, "bottomSheetHalfExpandTop=${bottomSheetHalfExpandTop.toDpFromPx()}")
                STLogUtil.d(tag, "bottomSheetExpandTop=${bottomSheetExpandTop.toDpFromPx()}")
                STLogUtil.d(tag, "bottomSheetCurrentTop=${bottomSheetCurrentTop.toDpFromPx()}")
                filterOnStateChanged(bottomSheet, newState, "STATE_EXPANDED")
            }
            BottomSheetBehavior.STATE_DRAGGING -> {
                lastBottomSheetDragTop = bottomSheetCurrentTop
                STLogUtil.d(tag, "STATE_DRAGGING")

                onStateChanged?.invoke(bottomSheet, newState)
            }
            BottomSheetBehavior.STATE_SETTLING -> {
                STLogUtil.e(tag, "STATE_SETTLING")
                STLogUtil.d(tag, "screenFullHeight=${screenFullHeight.toDpFromPx()}")
                STLogUtil.d(tag, "bottomSheetCollapsedTop=${bottomSheetCollapsedTop.toDpFromPx()}")
                STLogUtil.d(tag, "bottomSheetHalfExpandTop=${bottomSheetHalfExpandTop.toDpFromPx()}")
                STLogUtil.d(tag, "bottomSheetExpandTop=${bottomSheetExpandTop.toDpFromPx()}")
                STLogUtil.d(tag, "bottomSheetCurrentTop=${bottomSheetCurrentTop.toDpFromPx()}")

                val currentPullTopToBottom = pullTopToBottom
                if (currentPullTopToBottom != null) {
                    bottomSheet.clearAnimation()
                    bottomSheet.post {
                        when {
                            bottomSheetCurrentTop < (if (currentPullTopToBottom) (bottomSheetExpandTop + offsetTenPercent) else (bottomSheetHalfExpandTop - offsetTenPercent)) -> {
                                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                            }
                            bottomSheetCurrentTop < (if (currentPullTopToBottom) (bottomSheetHalfExpandTop + offsetTenPercent) else (bottomSheetCollapsedTop - offsetTenPercent)) -> {
                                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
                            }
                            else -> {
                                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                            }
                        }
                    }
                }
                lastBottomSheetDragTop = -1

                onStateChanged?.invoke(bottomSheet, newState)
            }
            else -> {
                onStateChanged?.invoke(bottomSheet, newState)
            }
        }
    }

    override fun onSlide(bottomSheet: View, slideOffset: Float) {
        if (lastBottomSheetDragTop != -1) {
            lastBottomSheetDragTop = bottomSheet.top
        }
        STLogUtil.v(tag, "onSlide, slideOffset=$slideOffset, pullTopToBottom=$pullTopToBottom, lastBottomSheetDragTop=$lastBottomSheetDragTop, bottomSheet.top=${bottomSheet.top}")
        onSlide?.invoke(bottomSheet, slideOffset)
    }
}