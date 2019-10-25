package com.smart.template.home.behavior

import android.support.design.widget.BottomSheetBehavior
import android.view.View
import com.smart.library.base.toDpFromPx
import com.smart.library.util.STLogUtil
import com.smart.library.util.STSystemUtil
import kotlin.math.max
import kotlin.math.min

/*
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.st_bottom_sheet_activity)

    val bottomSheetBehavior: BottomSheetBehavior<RelativeLayout> = BottomSheetBehavior.from(bottomSheetLayout)
    bottomSheetBehavior.isFitToContents = true  // 设置对齐方式, true表示底部对齐, false表示顶部对齐
    bottomSheetBehavior.isHideable = false
    bottomSheetBehavior.skipCollapsed = false   // 设置此底页在展开一次后隐藏时是否应跳过折叠状态.
    bottomSheetBehavior.setBottomSheetCallback(STBottomSheetBehaviorCallback(bottomSheetBehavior, bottomSheetAppbarHeight))
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
        app:layout_anchor="@id/bottomSheetLayout"
        app:layout_anchorGravity="top">

        <android.support.v7.widget.Toolbar
            android:layout_width="match_parent"
            android:layout_height="@dimen/bottom_sheet_appbar_height"
            app:title="巅峰榜 流行指数 第64天" />
    </android.support.design.widget.AppBarLayout>

    <RelativeLayout
        android:id="@+id/bottomSheetLayout"
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
 */
class STBottomSheetBehaviorCallback(private val bottomSheetBehavior: BottomSheetBehavior<out View>, bottomSheetAppbarHeight: Int, dragOffsetPercent: Float = 30f, private val onStateChanged: ((bottomSheet: View, newState: Int) -> Unit)? = null, private val onSlide: ((bottomSheet: View, slideOffset: Float) -> Unit)? = null) : BottomSheetBehavior.BottomSheetCallback() {

    private val tag: String = "sheet"
    private val screenFullHeight: Int = STSystemUtil.screenHeight + STSystemUtil.statusBarHeight
    private val bottomSheetCollapsedTop: Int = screenFullHeight - bottomSheetBehavior.peekHeight
    private val bottomSheetHalfExpandTop: Int = (screenFullHeight / 2f).toInt()
    private val bottomSheetExpandTop: Int = bottomSheetAppbarHeight
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

    override fun onStateChanged(bottomSheet: View, newState: Int) {
        val bottomSheetCurrentTop: Int = bottomSheet.top
        when (newState) {
            BottomSheetBehavior.STATE_HIDDEN -> {
                lastBottomSheetDragTop = -1
                STLogUtil.e(tag, "STATE_HIDDEN")
            }
            BottomSheetBehavior.STATE_COLLAPSED -> {
                lastBottomSheetDragTop = -1
                STLogUtil.e(tag, "STATE_COLLAPSED")
                STLogUtil.d(tag, "screenFullHeight=${screenFullHeight.toDpFromPx()}")
                STLogUtil.d(tag, "bottomSheetCollapsedTop=${bottomSheetCollapsedTop.toDpFromPx()}")
                STLogUtil.d(tag, "bottomSheetHalfExpandTop=${bottomSheetHalfExpandTop.toDpFromPx()}")
                STLogUtil.d(tag, "bottomSheetExpandTop=${bottomSheetExpandTop.toDpFromPx()}")
                STLogUtil.d(tag, "bottomSheetCurrentTop=${bottomSheetCurrentTop.toDpFromPx()}")
            }
            BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                lastBottomSheetDragTop = -1
                STLogUtil.e(tag, "STATE_HALF_EXPANDED")
                STLogUtil.d(tag, "screenFullHeight=${screenFullHeight.toDpFromPx()}")
                STLogUtil.d(tag, "bottomSheetCollapsedTop=${bottomSheetCollapsedTop.toDpFromPx()}")
                STLogUtil.d(tag, "bottomSheetHalfExpandTop=${bottomSheetHalfExpandTop.toDpFromPx()}")
                STLogUtil.d(tag, "bottomSheetExpandTop=${bottomSheetExpandTop.toDpFromPx()}")
                STLogUtil.d(tag, "bottomSheetCurrentTop=${bottomSheetCurrentTop.toDpFromPx()}")
            }
            BottomSheetBehavior.STATE_EXPANDED -> {
                lastBottomSheetDragTop = -1
                STLogUtil.e(tag, "STATE_EXPANDED")
                STLogUtil.d(tag, "screenFullHeight=${screenFullHeight.toDpFromPx()}")
                STLogUtil.d(tag, "bottomSheetCollapsedTop=${bottomSheetCollapsedTop.toDpFromPx()}")
                STLogUtil.d(tag, "bottomSheetHalfExpandTop=${bottomSheetHalfExpandTop.toDpFromPx()}")
                STLogUtil.d(tag, "bottomSheetExpandTop=${bottomSheetExpandTop.toDpFromPx()}")
                STLogUtil.d(tag, "bottomSheetCurrentTop=${bottomSheetCurrentTop.toDpFromPx()}")
            }
            BottomSheetBehavior.STATE_DRAGGING -> {
                lastBottomSheetDragTop = bottomSheetCurrentTop
                STLogUtil.d(tag, "STATE_DRAGGING")
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
            }
        }
        onStateChanged?.invoke(bottomSheet, newState)
    }

    override fun onSlide(bottomSheet: View, slideOffset: Float) {
        if (lastBottomSheetDragTop != -1) {
            lastBottomSheetDragTop = bottomSheet.top
        }
        STLogUtil.v(tag, "onSlide, slideOffset=$slideOffset, pullTopToBottom=$pullTopToBottom, lastBottomSheetDragTop=$lastBottomSheetDragTop, bottomSheet.top=${bottomSheet.top}")
        onSlide?.invoke(bottomSheet, slideOffset)
    }
}