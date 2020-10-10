/*
 * fit to https://github.com/material-components/material-components-android/releases/tag/1.2.1
 */

package com.smart.library.widget.behavior

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IntDef
import androidx.annotation.NonNull
import androidx.annotation.UiThread
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.math.MathUtils
import androidx.core.view.ViewCompat
import androidx.customview.widget.ViewDragHelper
import androidx.viewpager.widget.ViewPager
import com.google.android.material.R
import com.smart.library.base.toPxFromDp
import com.smart.library.source.STBottomSheetBehaviorV2
import com.smart.library.util.STLogUtil
import java.lang.ref.WeakReference
import kotlin.math.max
import kotlin.math.min

/**
 * -keep class smart.library.widget.behavior.STBottomSheetViewPagerBehavior{*;}
 *
 * Override [.findScrollingChild] to support [ViewPager]'s nested scrolling.
 *
 * By the way, In order to override package level method and field.
 * This class put in the same package path where [STBottomSheetBehaviorV2] located.
 */
@Suppress("MemberVisibilityCanBePrivate", "ReplaceJavaStaticMethodWithKotlinAnalog", "LiftReturnOrAssignment", "unused", "UsePropertyAccessSyntax", "RedundantOverride", "ProtectedInFinal")
class STBottomSheetViewPagerBehaviorV2<V : View> @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : STBottomSheetBehaviorV2<V>(context, attrs) {

    /**
     * 滑动或者超过多少阈值, 滚动到下个状态
     * 参考:
     * private fun dragThresholdOffset(): Float = Math.abs(getViewVerticalDragRange() / min(99f, max(dragOffsetPercent, 1f)))
     */
    var dragOffsetPercent: Float = 50f

    /**
     * 默认 false 两段式滑动
     *
     * false 两段式滑动, STATE_EXPANDED/STATE_COLLAPSED 没有 STATE_HALF_EXPANDED
     * true 三段式滑动, STATE_EXPANDED/STATE_HALF_EXPANDED/STATE_COLLAPSED
     *
     * 注意: 从 STATE_EXPANDED 状态拖拽下滑, 如果手指触摸的是内嵌的滚动列表, 则不会触发 onViewReleased, 松手强制设置 STATE_COLLAPSED,
     *      如果手指触摸的是内嵌的非滚动布局, 则会乖乖的到 STATE_HALF_EXPANDED 状态
     * @see wrapStateForEnableHalfExpanded
     */
    var enableHalfExpandedState = false

    private var pullTopToBottom: Boolean? = null

    // 向下拖拽 可滚动的 childView.nestedScrollView 到顶部继续拖拽联动 childView 面板继续向下滑动时
    // 默认 false 会从 expanded 直接滑动到 collapsed, true 做自定义处理
    var enableCustomTargetTopAndStateOnStopNestedScroll: Boolean = true
    var enableCustomTargetTopAndStateOnViewReleased: Boolean = true

    // 向下拖拽 可滚动的 childView.nestedScrollView 到顶部继续拖拽联动 childView 面板继续向下滑动时, 根据 lastBottomSheetDragTop 获取的 pullTopToBottom 并没有起作用, 这里从 onSlide 里获取 pullTopToBottom
    private var lastSlideTop: Int = -1
        private set(value) {
            if (field != -1) {
                pullTopToBottom = value - field > 0
            } else {
                pullTopToBottom = null
            }
            field = value
        }

    private fun getViewVerticalDragRange(): Int = if (hideable) parentHeight else collapsedOffset
    private fun dragThresholdOffset(): Float = Math.abs(getViewVerticalDragRange() / min(99f, max(dragOffsetPercent, 1f)))

    var currentFinalState: Int = -1 // 首次 setState 之前为 -1, 标记第一次
        private set

    override fun dispatchOnSlide(top: Int) {
        super.dispatchOnSlide(top)
        lastSlideTop = top
    }

    init {
        //region rewrite dragCallback
        dragCallback = object : ViewDragHelper.Callback() {
            override fun tryCaptureView(@NonNull child: View, pointerId: Int): Boolean {
                STLogUtil.d(TAG, "tryCaptureView pointerId=$pointerId, state=${getStateDescription(state)}, touchingScrollingChild=$touchingScrollingChild, activePointerId=$activePointerId, pointerId=$pointerId")

                if (state == STBottomSheetBehaviorV2.STATE_DRAGGING) {
                    return false
                }
                if (touchingScrollingChild) {
                    return false
                }
                if (state == STBottomSheetBehaviorV2.STATE_EXPANDED && activePointerId == pointerId) {
                    val nestedScrollingView: View? = if (nestedScrollingChildRef != null) nestedScrollingChildRef?.get() else null

                    STLogUtil.d(TAG, "tryCaptureView canScrollVertically=${nestedScrollingView?.canScrollVertically(-1)}, nestedScrollingView=$nestedScrollingView")

                    if (nestedScrollingView != null && nestedScrollingView.canScrollVertically(-1)) {
                        // Let the content scroll up
                        return false
                    }
                }
                STLogUtil.d(TAG, "tryCaptureView viewRef?.get()===child?${viewRef?.get() === child}")

                return viewRef?.get() === child
            }

            override fun onViewPositionChanged(@NonNull changedView: View, left: Int, top: Int, dx: Int, dy: Int) {
                dispatchOnSlide(top)
                STLogUtil.d(TAG, "onViewPositionChanged left=$left, top=$top, dx=$dx, dy=$dy, lastSlideTop=$lastSlideTop")
            }

            override fun onViewDragStateChanged(state: Int) {
                STLogUtil.d(TAG, "onViewDragStateChanged state=${getStateDescription(state)}")

                if (state == ViewDragHelper.STATE_DRAGGING && draggable) {
                    setStateInternal(STBottomSheetBehaviorV2.STATE_DRAGGING)
                }
            }

            private fun releasedLow(child: View): Boolean {
                // Needs to be at least half way to the bottom.
                return child.top > (parentHeight + getExpandedOffset()) / 2
            }

            override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
                STLogUtil.d(TAG, "onViewReleased xvel=$xvel, yvel=$yvel")

                var top: Int
                var targetState: Int
                val currentTop = releasedChild.top
                val currentPullTopToBottom = pullTopToBottom

                if (STLogUtil.debug) STLogUtil.w(TAG, "onViewReleased start pullTopToBottom=$pullTopToBottom")
                if (STLogUtil.debug) STLogUtil.d(TAG, "onViewReleased yvel=$yvel,enableHalfExpandedState=$enableHalfExpandedState, expandedOffset=$expandedOffset, currentPullTopToBottom=$currentPullTopToBottom, currentTop=$currentTop,")
                if (STLogUtil.debug) STLogUtil.w(TAG, "onViewReleased start pullTopToBottom=$pullTopToBottom, lastSlideTop=$lastSlideTop")
                if (STLogUtil.debug) STLogUtil.d(TAG, "onViewReleased yvel=$yvel, xvel=$xvel, enableHalfExpandedState=$enableHalfExpandedState, expandedOffset=$expandedOffset, currentPullTopToBottom=$currentPullTopToBottom, currentTop=$currentTop,")
                if (STLogUtil.debug) STLogUtil.d(TAG, "onViewReleased halfExpandedOffset=$halfExpandedOffset, dragThresholdOffset()=${dragThresholdOffset()}, collapsedOffset=$collapsedOffset, fitToContentsOffset=$fitToContentsOffset")
                if (STLogUtil.debug) STLogUtil.d(TAG, "onViewReleased getViewVerticalDragRange()=${getViewVerticalDragRange()}, fitToContents=$fitToContents")

                // 维持原代码不变
                if (yvel < 0.0f) {
                    STLogUtil.e(TAG, "onViewReleased yvel < 0.0f")
                    if (fitToContents) {
                        top = fitToContentsOffset
                        targetState = STATE_EXPANDED
                    } else {
                        if (currentTop > halfExpandedOffset) {
                            top = halfExpandedOffset
                            targetState = STATE_HALF_EXPANDED
                        } else {
                            top = expandedOffset
                            targetState = STATE_EXPANDED
                        }
                    }
                } else if (hideable && shouldHide(releasedChild, yvel)) {
                    STLogUtil.e(TAG, "onViewReleased hideable=$hideable")
                    top = parentHeight
                    targetState = STATE_HIDDEN
                } else if (yvel == 0.0f || Math.abs(xvel) > Math.abs(yvel)) {
                    STLogUtil.e(TAG, "onViewReleased yvel == 0.0f")

                    // Hide if the view was either released low or it was a significant vertical swipe
                    // otherwise settle to closest expanded state.
                    if (Math.abs(xvel) < Math.abs(yvel) && yvel > SIGNIFICANT_VEL_THRESHOLD
                        || releasedLow(releasedChild)
                    ) {
                        top = parentHeight
                        targetState = STBottomSheetBehaviorV2.STATE_HIDDEN
                    } else if (fitToContents) {
                        top = fitToContentsOffset
                        targetState = STBottomSheetBehaviorV2.STATE_EXPANDED
                    } else if (Math.abs(releasedChild.top - expandedOffset)
                        < Math.abs(releasedChild.top - halfExpandedOffset)
                    ) {
                        top = expandedOffset
                        targetState = STBottomSheetBehaviorV2.STATE_EXPANDED
                    } else {
                        top = halfExpandedOffset
                        targetState = STBottomSheetBehaviorV2.STATE_HALF_EXPANDED
                    }
                } else if (yvel == 0f || Math.abs(xvel) > Math.abs(yvel)) {
                    // If the Y velocity is 0 or the swipe was mostly horizontal indicated by the X velocity
                    // being greater than the Y velocity, settle to the nearest correct height.
                    if (fitToContents) {
                        if (Math.abs(currentTop - fitToContentsOffset)
                            < Math.abs(currentTop - collapsedOffset)
                        ) {
                            top = fitToContentsOffset
                            targetState = STBottomSheetBehaviorV2.STATE_EXPANDED
                        } else {
                            top = collapsedOffset
                            targetState = STBottomSheetBehaviorV2.STATE_COLLAPSED
                        }
                    } else {
                        if (currentTop < halfExpandedOffset) {
                            if (currentTop < Math.abs(currentTop - collapsedOffset)) {
                                top = expandedOffset
                                targetState = STBottomSheetBehaviorV2.STATE_EXPANDED
                            } else {
                                top = halfExpandedOffset
                                targetState = STBottomSheetBehaviorV2.STATE_HALF_EXPANDED
                            }
                        } else {
                            if (Math.abs(currentTop - halfExpandedOffset)
                                < Math.abs(currentTop - collapsedOffset)
                            ) {
                                top = halfExpandedOffset
                                targetState = STBottomSheetBehaviorV2.STATE_HALF_EXPANDED
                            } else {
                                top = collapsedOffset
                                targetState = STBottomSheetBehaviorV2.STATE_COLLAPSED
                            }
                        }
                    }
                } else { // Moving Down
                    if (fitToContents) {
                        top = collapsedOffset
                        targetState = STBottomSheetBehaviorV2.STATE_COLLAPSED
                    } else {
                        // Settle to the nearest correct height.
                        if (Math.abs(currentTop - halfExpandedOffset) < Math.abs(currentTop - collapsedOffset)
                        ) {
                            top = halfExpandedOffset
                            targetState = STBottomSheetBehaviorV2.STATE_HALF_EXPANDED
                        } else {
                            top = collapsedOffset
                            targetState = STBottomSheetBehaviorV2.STATE_COLLAPSED
                        }
                    }
                }

                if (enableCustomTargetTopAndStateOnViewReleased) {
                    val results: Array<Int> = customTargetTopAndState(child = releasedChild, targetOriginTop = top, targetOriginState = targetState, settleFromViewDragHelper = true)
                    top = results[0]
                    targetState = results[1]
                } else {
                    startSettlingAnimation(releasedChild, targetState, top, true)
                }

                STLogUtil.w(TAG, "onViewReleased end top=$top, targetState=$targetState, pullTopToBottom=$pullTopToBottom, lastSlideTop=$lastSlideTop")
            }

            override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
                val clampViewPositionVertical: Int = MathUtils.clamp(top, expandedOffset, if (hideable) parentHeight else collapsedOffset)
                STLogUtil.d(TAG, "clampViewPositionVertical clampViewPositionVertical=$clampViewPositionVertical")
                return clampViewPositionVertical
            }

            override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
                val clampViewPositionHorizontal: Int = child.left
                STLogUtil.d(TAG, "clampViewPositionHorizontal clampViewPositionHorizontal=$clampViewPositionHorizontal")
                return clampViewPositionHorizontal
            }

            override fun getViewVerticalDragRange(child: View): Int {
                val viewVerticalDragRange: Int = if (hideable) getParentHeight() else collapsedOffset
                STLogUtil.d(TAG, "getViewVerticalDragRange viewVerticalDragRange=$viewVerticalDragRange")
                return viewVerticalDragRange
            }
        }
        //endregion

        //region only set once BottomSheetCallback
        super.addBottomSheetCallback(object : BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, percent: Float) {
                STLogUtil.d(TAG, "onSlide percent=$percent")
                resetNestedViewsLayoutParamsByBottomSheetContainerHeight?.invoke(getCurrentBottomSheetContainerHeight(bottomSheet))
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                STLogUtil.d(TAG, "onStateChanged newState=${getStateDescription(newState)}, this.state=${getStateDescription(getState())}, currentFinalState=${getStateDescription(currentFinalState)}")
                when (newState) {
                    STBottomSheetBehaviorV2.STATE_EXPANDED, STBottomSheetBehaviorV2.STATE_HALF_EXPANDED, STBottomSheetBehaviorV2.STATE_COLLAPSED -> {
                        currentFinalState = newState
                        resetNestedViewsLayoutParamsByBottomSheetContainerHeight?.invoke(getBottomSheetContainerHeightByState(newState))
                    }
                    else -> {
                    }
                }
            }
        })
        //endregion
    }

    /**
     * onStateChanged or onSlide 设置 bottomSheet 里面嵌套的子 views(NestedScrollView) 的高度等属性
     */
    var resetNestedViewsLayoutParamsByBottomSheetContainerHeight: ((currentBottomSheetContainerHeightByState: Int) -> Unit)? = null

    /**
     * 根据自己的业务重置 top and targetState
     */
    private fun customTargetTopAndState(child: View, targetOriginTop: Int, targetOriginState: Int, settleFromViewDragHelper: Boolean): Array<Int> {
        // 垂直速度为0, 总位置没有变动, 且当前位置正好与目标位置一致, 则不需要执行动画
        var isTotalOffsetZeroAndTopIsJustTargetStateOffset = false

        var targetFinalTop: Int = targetOriginTop
        var targetFinalState: Int = targetOriginState

        val currentTop: Int = child.top
        val currentPullTopToBottom: Boolean? = pullTopToBottom // 当内部滚动松开手指时, child 位置改变才会触发 onViewReleased, 所以通过 onSlide 获得 pullTopToBottom
        val dragThresholdOffset: Float = dragThresholdOffset()
        val halfExpandedOffset: Int = getHalfExpandedOffset()
        val collapsedOffset: Int = collapsedOffset
        val expandedOffset: Int = expandedOffset
        STLogUtil.e(TAG, "customTargetTopAndState currentPullTopToBottom=$currentPullTopToBottom, currentTop=$currentTop, dragThresholdOffset=$dragThresholdOffset, halfExpandedOffset=$halfExpandedOffset, collapsedOffset=$collapsedOffset, expandedOffset=$expandedOffset, enableHalfExpandedState=$enableHalfExpandedState")

        if (currentPullTopToBottom != null) {
            if (enableHalfExpandedState) {
                when {
                    currentTop < (if (currentPullTopToBottom) (dragThresholdOffset) else (halfExpandedOffset - dragThresholdOffset)) -> {
                        STLogUtil.d(TAG, "customTargetTopAndState set targetState=STATE_EXPANDED")
                        targetFinalTop = expandedOffset
                        targetFinalState = STATE_EXPANDED
                    }
                    currentTop < (if (currentPullTopToBottom) (halfExpandedOffset + dragThresholdOffset) else (collapsedOffset - dragThresholdOffset)) -> {
                        STLogUtil.d(TAG, "customTargetTopAndState set targetState=STATE_HALF_EXPANDED")
                        targetFinalTop = halfExpandedOffset
                        targetFinalState = STATE_HALF_EXPANDED
                    }
                    else -> {
                        STLogUtil.d(TAG, "customTargetTopAndState set targetState=STATE_COLLAPSED")
                        targetFinalTop = collapsedOffset
                        targetFinalState = STATE_COLLAPSED
                    }
                }
            } else {
                when {
                    currentTop < (if (currentPullTopToBottom) (dragThresholdOffset) else (collapsedOffset - dragThresholdOffset)) -> {
                        STLogUtil.d(TAG, "customTargetTopAndState set targetState=STATE_EXPANDED")
                        targetFinalState = STATE_EXPANDED
                    }
                    else -> {
                        STLogUtil.d(TAG, "customTargetTopAndState set targetState=STATE_COLLAPSED")
                        targetFinalTop = collapsedOffset
                        targetFinalState = STATE_COLLAPSED
                    }
                }
            }
        } else {
            // 总位置没有变动, 且当前位置正好与目标位置一致, 则不需要执行动画
            if (lastSlideTop == -1) {
                when (currentTop) {
                    halfExpandedOffset -> {
                        targetFinalTop = halfExpandedOffset
                        targetFinalState = STATE_HALF_EXPANDED
                        STLogUtil.d(TAG, "customTargetTopAndState currentPullTopToBottom==null, lastSlideTop=$lastSlideTop, currentTop=$currentTop, set targetState=STATE_HALF_EXPANDED")
                        isTotalOffsetZeroAndTopIsJustTargetStateOffset = true
                    }
                    collapsedOffset -> {
                        targetFinalTop = collapsedOffset
                        targetFinalState = STATE_COLLAPSED
                        STLogUtil.d(TAG, "customTargetTopAndState currentPullTopToBottom==null, lastSlideTop=$lastSlideTop, currentTop=$currentTop, set targetState=STATE_COLLAPSED")
                        isTotalOffsetZeroAndTopIsJustTargetStateOffset = true
                    }
                    expandedOffset -> {
                        targetFinalTop = expandedOffset
                        targetFinalState = STATE_EXPANDED
                        STLogUtil.d(TAG, "customTargetTopAndState currentPullTopToBottom==null, lastSlideTop=$lastSlideTop, currentTop=$currentTop, set targetState=STATE_EXPANDED")
                        isTotalOffsetZeroAndTopIsJustTargetStateOffset = true
                    }
                    else -> {
                        STLogUtil.d(TAG, "customTargetTopAndState currentPullTopToBottom==null, lastSlideTop=$lastSlideTop, currentTop=$currentTop)")
                    }
                }
            }
        }

        STLogUtil.e(TAG, "customTargetTopAndState isYVelZeroAndTotalOffsetZeroAndTopIsJustTargetStateOffset=$isTotalOffsetZeroAndTopIsJustTargetStateOffset, halfExpandedOffset=$halfExpandedOffset, top=$targetFinalTop, collapsedOffset=$collapsedOffset, fitToContentsOffset=$fitToContentsOffset")
        if (!isTotalOffsetZeroAndTopIsJustTargetStateOffset) {
            STLogUtil.w(TAG, "customTargetTopAndState startSettlingAnimation, lastSlideTop=$lastSlideTop, settleFromViewDragHelper$settleFromViewDragHelper")
            startSettlingAnimation(child, targetFinalState, targetFinalTop, settleFromViewDragHelper)
        } else {
            STLogUtil.w(TAG, "customTargetTopAndState setStateInternal targetState=$targetFinalState, lastSlideTop=$lastSlideTop")
            setStateInternal(targetFinalState)
        }

        // 重置
        lastSlideTop = -1
        return arrayOf(targetFinalTop, targetFinalState)
    }

    override fun findScrollingChild(view: View): View? {
        if (ViewCompat.isNestedScrollingEnabled(view)) {
            return view
        }

        if (view is ViewPager) {
            val currentViewPagerChild: View? = try {
                view.getChildAt(view.currentItem)
            } catch (e: Exception) {
                null
            }
            if (currentViewPagerChild == null) {
                return null
            } else {
                val scrollingChild = findScrollingChild(currentViewPagerChild)
                if (scrollingChild != null) {
                    return scrollingChild
                }
            }
        } else if (view is ViewGroup) {
            var i = 0
            val count = view.childCount
            while (i < count) {
                val scrollingChild = findScrollingChild(view.getChildAt(i))
                if (scrollingChild != null) {
                    return scrollingChild
                }
                i++
            }
        }
        return null
    }

    fun updateScrollingChild() {
        (getViewRef()?.get() as? View)?.let {
            val scrollingChild = findScrollingChild(it)
            setNestedScrollingChildRef(WeakReference<View>(scrollingChild))
        }
    }

    //region window insets changed 时会调用
    private var currentParentHeightOnSetFinalState: Int = 0

    /**
     * 切换/显示/隐藏 虚拟导航栏时, 会重新调用 onLayoutChild
     * 切换 app 也会调用
     */
    @SuppressLint("PrivateResource")
    override fun onLayoutChild(@NonNull parent: CoordinatorLayout, @NonNull child: V, layoutDirection: Int): Boolean {
        val finalState = wrapStateForEnableHalfExpanded(state)
        Log.w(TAG, "onLayoutChild start state=${getStateDescription(state)}, finalState=${getStateDescription(finalState)}, currentFinalState=${getStateDescription(currentFinalState)}, getCollapsedOffset=${getCollapsedOffset()}, expandedOffset=${getExpandedOffset()}, currentParentHeight=$currentParentHeightOnSetFinalState, parentHeight=${getParentHeight()}, parent.height=${parent.height}")

        if (ViewCompat.getFitsSystemWindows(parent) && !ViewCompat.getFitsSystemWindows(child)) {
            child.fitsSystemWindows = true
        }

        if (viewRef == null) {
            // First layout with this behavior.
            peekHeightMin = parent.resources.getDimensionPixelSize(R.dimen.design_bottom_sheet_peek_height_min)
            setSystemGestureInsets(child)
            viewRef = WeakReference(child)
            // Only set MaterialShapeDrawable as background if shapeTheming is enabled, otherwise will
            // default to android:background declared in styles or layout.
            if (shapeThemingEnabled && materialShapeDrawable != null) {
                ViewCompat.setBackground(child, materialShapeDrawable)
            }
            // Set elevation on MaterialShapeDrawable
            if (materialShapeDrawable != null) {
                // Use elevation attr if set on bottomsheet; otherwise, use elevation of child view.
                materialShapeDrawable.elevation = if (elevation == -1f) ViewCompat.getElevation(child) else elevation
                // Update the material shape based on initial state.
                isShapeExpanded = state == STBottomSheetBehaviorV2.STATE_EXPANDED
                materialShapeDrawable.interpolation = if (isShapeExpanded) 0f else 1f
            }
            updateAccessibilityActions()
            if (ViewCompat.getImportantForAccessibility(child)
                == ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_AUTO
            ) {
                ViewCompat.setImportantForAccessibility(child, ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_YES)
            }
        }
        if (viewDragHelper == null) {
            viewDragHelper = ViewDragHelper.create(parent, dragCallback)
        }

        val savedTop: Int = child.top
        // First let the parent lay it out
        parent.onLayoutChild(child, layoutDirection) // 注意设置 android:layout_gravity="bottom", 否则有闪现现象
        // Offset the bottom sheet
        parentWidth = parent.width
        parentHeight = parent.height
        childHeight = child.height
        fitToContentsOffset = Math.max(0, parentHeight - childHeight)
        calculateHalfExpandedOffset()
        calculateCollapsedOffset()

        Log.w(TAG, "onLayoutChild offsetTopAndBottom enableHalfExpandedState=$enableHalfExpandedState, state=${getStateDescription(state)}, finalState=${getStateDescription(finalState)}, currentFinalState=${getStateDescription(currentFinalState)}, fitToContentsOffset=$fitToContentsOffset, customHalfExpandedOffset=$customHalfExpandedOffset, halfExpandedOffset=$halfExpandedOffset, collapsedOffset=$collapsedOffset")

        // 通过 val targetOffset: Int = expandedOffset - child.top 解决 当设置不同的面板内图片或者 设置不同的 view.padding 时导致的面板位置错误改变
        // 参见 onStateChanged 里面的 setArrowStatus, 如果修改为 targetOffset, 则当箭头改变时会导致面板移动到错误的位置
        if (finalState == STBottomSheetBehaviorV2.STATE_EXPANDED) {
            val targetOffset: Int = expandedOffset - child.top
            Log.w(TAG, "onLayoutChild offsetTopAndBottom expandedOffset=$expandedOffset, child.top=${child.top}, targetOffset=$targetOffset")
            if (targetOffset != 0) {
                ViewCompat.offsetTopAndBottom(child, targetOffset)
            }
        } else if (finalState == STBottomSheetBehaviorV2.STATE_HALF_EXPANDED) {
            val targetOffset: Int = halfExpandedOffset - child.top
            Log.w(TAG, "onLayoutChild offsetTopAndBottom halfExpandedOffset=$halfExpandedOffset, child.top=${child.top}, targetOffset=$targetOffset")
            if (targetOffset != 0) {
                ViewCompat.offsetTopAndBottom(child, targetOffset)
            }
        } else if (hideable && finalState == STBottomSheetBehaviorV2.STATE_HIDDEN) {
            val targetOffset: Int = parentHeight - child.top
            Log.w(TAG, "onLayoutChild offsetTopAndBottom parentHeight=$parentHeight, child.top=${child.top}, targetOffset=$targetOffset")
            if (targetOffset != 0) {
                ViewCompat.offsetTopAndBottom(child, targetOffset)
            }
        } else if (finalState == STBottomSheetBehaviorV2.STATE_COLLAPSED) {
            val targetOffset: Int = collapsedOffset - child.top
            Log.w(TAG, "onLayoutChild offsetTopAndBottom collapsedOffset=$collapsedOffset, child.top=${child.top}, targetOffset=$targetOffset")
            if (targetOffset != 0) {
                ViewCompat.offsetTopAndBottom(child, targetOffset)
            }
        } else if (finalState == STBottomSheetBehaviorV2.STATE_DRAGGING || finalState == STBottomSheetBehaviorV2.STATE_SETTLING) {
            Log.w(TAG, "onLayoutChild offsetTopAndBottom (savedTop-child.top)=${savedTop - child.top}")
            ViewCompat.offsetTopAndBottom(child, savedTop - child.top)
        }

        nestedScrollingChildRef = WeakReference(findScrollingChild(child))
        // return true
        val onLayoutChild = true

        //region 当高度改变时, 通知重设配置
        val newParentHeight: Int = parent.height
        if (newParentHeight > 0 && newParentHeight != currentParentHeightOnSetFinalState) {
            onParentHeightChangedListener?.invoke(parent, child, currentFinalState == -1)
        }
        //endregion

        Log.w(TAG, "onLayoutChild end currentParentHeight=$currentParentHeightOnSetFinalState, parentHeight=${getParentHeight()}, parent.height=${parent.height}")
        return onLayoutChild
    }

    private var onParentHeightChangedListener: ((parent: CoordinatorLayout, child: V, isFirst: Boolean) -> Unit)? = null

    /**
     * 当切换/显示/隐藏 虚拟导航栏导致的视觉问题时, 重新设置所有高度
     *
     * 等同->
     * ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { view: View?, insets: WindowInsetsCompat ->
     *     STSystemUtil.showSystemInfo(this)
     *     insets
     * }
     */
    @UiThread
    fun setOnParentHeightChangedListener(onParentHeightChangedListener: (parent: CoordinatorLayout, child: V, isFirst: Boolean) -> Unit) {
        this.onParentHeightChangedListener = onParentHeightChangedListener
    }
    //endregion

    private var customHalfExpandedOffset: Int = -1

    @UiThread
    fun setCustomHalfExpandedOffset(halfExpandedOffset: Int) {
        this.customHalfExpandedOffset = halfExpandedOffset
        calculateHalfExpandedOffset()
    }

    override fun calculateHalfExpandedOffset() {
        halfExpandedOffset = if (customHalfExpandedOffset != -1) customHalfExpandedOffset else (parentHeight * (1 - halfExpandedRatio)).toInt()
    }

    override fun calculateCollapsedOffset() {
        super.calculateCollapsedOffset()
    }

    /**
     * @return 状态为 STATE_HALF_EXPANDED 时, getView().top 与屏幕顶部之间距离
     */
    fun getHalfExpandedOffset(): Int {
        return halfExpandedOffset
    }

    /**
     * @return 状态为 STATE_COLLAPSED 时, getView().top 与屏幕顶部之间距离
     */
    fun getCollapsedOffset(): Int {
        return this.collapsedOffset
    }

    @UiThread
    private fun setParentHeight(parentHeight: Int): Boolean {
        if (parentHeight <= 0 || parentHeight == currentParentHeightOnSetFinalState) {
            STLogUtil.e(TAG, "setParentHeight:$parentHeight failure, return false")
            return false
        }
        this.parentHeight = parentHeight
        this.currentParentHeightOnSetFinalState = parentHeight
        STLogUtil.e(TAG, "setParentHeight:$parentHeight success, return true")
        return true
    }

    override fun setFitToContents(fitToContents: Boolean) {
        super.setFitToContents(fitToContents)
        STLogUtil.w(TAG, "setFitToContents fitToContents=$fitToContents")
    }

    fun setFitToContentsOffset(fitToContentsOffset: Int) {
        this.fitToContentsOffset = fitToContentsOffset
        STLogUtil.w(TAG, "setFitToContentsOffset fitToContentsOffset=$fitToContentsOffset")
    }

    override fun setExpandedOffset(expandedOffset: Int) {
        super.setExpandedOffset(expandedOffset)
        STLogUtil.w(TAG, "setExpandedOffset expandedOffset=$expandedOffset")
    }

    /**
     * @return expandedOffset 状态为 STATE_EXPANDED 时, getView().top 与屏幕顶部之间距离
     */
    override fun getExpandedOffset(): Int {
        return super.getExpandedOffset()
    }

    fun getParentHeight(): Int {
        return if (currentFinalState != -1 && this.currentParentHeightOnSetFinalState > 0) this.currentParentHeightOnSetFinalState else this.parentHeight
    }

    fun getView(): V? {
        return getViewRef()?.get()
    }

    fun getViewRef(): WeakReference<V>? {
        return this.viewRef
    }

    fun getNestedScrollingChildRef(): WeakReference<View>? {
        return this.nestedScrollingChildRef
    }

    fun setNestedScrollingChildRef(nestedScrollingChildRef: WeakReference<View>?) {
        this.nestedScrollingChildRef = nestedScrollingChildRef
    }

    /**
     * 浮层面板容器的可视高度
     */
    @Suppress("SameParameterValue")
    fun getBottomSheetContainerHeightByState(@FinalState state: Int): Int {
        val heightOnStateExpanded: Int = getParentHeight() - getExpandedOffset()
        val heightOnStateHalfExpanded: Int = getParentHeight() - getHalfExpandedOffset()
        val heightOnStateCollapsed: Int = getParentHeight() - getCollapsedOffset()

        val bottomSheetContainerHeightByState = when (state) {
            STATE_EXPANDED -> heightOnStateExpanded
            STATE_HALF_EXPANDED -> heightOnStateHalfExpanded
            STATE_COLLAPSED -> heightOnStateCollapsed
            else -> heightOnStateHalfExpanded
        }

        STLogUtil.sync { STLogUtil.d(TAG, "getBottomSheetContainerHeightByState ${getStateDescription(state)}, bottomSheetContainerHeightByState=$bottomSheetContainerHeightByState") }
        return bottomSheetContainerHeightByState
    }

    /**
     * 设置浮层面板容器的高度
     */
    @UiThread
    fun setBottomSheetContainerHeight(height: Int) {
        val bottomSheetContainer: View? = getView()
        val params: CoordinatorLayout.LayoutParams? = bottomSheetContainer?.layoutParams as? CoordinatorLayout.LayoutParams
        if (params != null) {
            params.height = height
            bottomSheetContainer.layoutParams = params
        }
    }
    //endregion

    //region 当 viewPager 页面切换时, 更新 nestedScrollingChildRef
    private val onPageChangeListener by lazy {
        object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {}

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {}

            override fun onPageSelected(p0: Int) {
                updateScrollingChild()
            }
        }
    }

    /**
     * 当 viewPager 页面切换时, 更新 nestedScrollingChildRef
     */
    @UiThread
    fun bindViewPager(viewPager: ViewPager) {
        viewPager.removeOnPageChangeListener(onPageChangeListener)
        viewPager.addOnPageChangeListener(onPageChangeListener)
    }
    //endregion

    //region drag control
    /**
     * 设置 当拖拽从下拉 recyclerView 列表到顶部的时候, 继续下拉是否联动面板
     */
    var dragEnabled = true

    override fun onTouchEvent(parent: CoordinatorLayout, child: V, event: MotionEvent): Boolean = if (dragEnabled) super.onTouchEvent(parent, child, event) else false
    override fun onInterceptTouchEvent(parent: CoordinatorLayout, child: V, event: MotionEvent): Boolean = if (dragEnabled) super.onInterceptTouchEvent(parent, child, event) else false
    override fun onNestedPreFling(coordinatorLayout: CoordinatorLayout, child: V, target: View, velocityX: Float, velocityY: Float): Boolean = if (dragEnabled) super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY) else false
    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout, child: V, directTargetChild: View, target: View, axes: Int, type: Int): Boolean = if (dragEnabled) super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes, type) else false

    override fun onNestedPreScroll(coordinatorLayout: CoordinatorLayout, child: V, target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        STLogUtil.e(TAG, "onNestedPreScroll start type=$type, lastNestedScrollDy=$lastNestedScrollDy")
        if (dragEnabled) super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
        STLogUtil.e(TAG, "onNestedPreScroll end type=$type, lastNestedScrollDy=$lastNestedScrollDy")
    }

    override fun onStopNestedScroll(@NonNull coordinatorLayout: CoordinatorLayout, @NonNull child: V, @NonNull target: View, type: Int) {
        STLogUtil.e(TAG, "onStopNestedScroll start type=$type, lastNestedScrollDy=$lastNestedScrollDy")
        if (!dragEnabled) {
            return
        }
        if (child.top == getExpandedOffset()) {
            setStateInternal(STBottomSheetBehaviorV2.STATE_EXPANDED)
            return
        }
        if (nestedScrollingChildRef == null || target !== nestedScrollingChildRef!!.get() || !nestedScrolled) {
            return
        }
        var top: Int
        var targetState: Int
        if (lastNestedScrollDy > 0) {
            if (fitToContents) {
                top = fitToContentsOffset
                targetState = STBottomSheetBehaviorV2.STATE_EXPANDED
            } else {
                val currentTop = child.top
                if (currentTop > halfExpandedOffset) {
                    top = halfExpandedOffset
                    targetState = STBottomSheetBehaviorV2.STATE_HALF_EXPANDED
                } else {
                    top = expandedOffset
                    targetState = STBottomSheetBehaviorV2.STATE_EXPANDED
                }
            }
            STLogUtil.e(TAG, "onStopNestedScroll lastNestedScrollDy($lastNestedScrollDy)>0 targetState=${getStateDescription(targetState)}")
        } else if (hideable && shouldHide(child, getYVelocity())) {
            top = parentHeight
            targetState = STBottomSheetBehaviorV2.STATE_HIDDEN
            STLogUtil.e(TAG, "onStopNestedScroll STATE_HIDDEN")
        } else if (lastNestedScrollDy == 0) {
            val currentTop = child.top
            if (fitToContents) {
                if (Math.abs(currentTop - fitToContentsOffset) < Math.abs(currentTop - collapsedOffset)) {
                    top = fitToContentsOffset
                    targetState = STBottomSheetBehaviorV2.STATE_EXPANDED
                    STLogUtil.e(TAG, "onStopNestedScroll fitToContents STATE_EXPANDED")
                } else {
                    top = collapsedOffset
                    targetState = STBottomSheetBehaviorV2.STATE_COLLAPSED
                    STLogUtil.e(TAG, "onStopNestedScroll fitToContents STATE_COLLAPSED")
                }
            } else {
                if (currentTop < halfExpandedOffset) {
                    if (currentTop < Math.abs(currentTop - collapsedOffset)) {
                        top = expandedOffset
                        targetState = STBottomSheetBehaviorV2.STATE_EXPANDED
                        STLogUtil.e(TAG, "onStopNestedScroll 1.1 STATE_EXPANDED")
                    } else {
                        top = halfExpandedOffset
                        targetState = STBottomSheetBehaviorV2.STATE_HALF_EXPANDED
                        STLogUtil.e(TAG, "onStopNestedScroll 1.2 STATE_HALF_EXPANDED")
                    }
                } else {
                    if (Math.abs(currentTop - halfExpandedOffset) < Math.abs(currentTop - collapsedOffset)) {
                        top = halfExpandedOffset
                        targetState = STBottomSheetBehaviorV2.STATE_HALF_EXPANDED
                        STLogUtil.e(TAG, "onStopNestedScroll 1.3 STATE_HALF_EXPANDED")
                    } else {
                        top = collapsedOffset
                        targetState = STBottomSheetBehaviorV2.STATE_COLLAPSED
                        STLogUtil.e(TAG, "onStopNestedScroll 1.4 STATE_HALF_EXPANDED")
                    }
                }
            }
        } else {
            if (fitToContents) {
                top = collapsedOffset
                targetState = STBottomSheetBehaviorV2.STATE_COLLAPSED
                STLogUtil.e(TAG, "onStopNestedScroll lastNestedScrollDy($lastNestedScrollDy)<=0, fitToContents==true, STATE_COLLAPSED")
            } else {
                // Settle to nearest height.
                val currentTop = child.top
                if (Math.abs(currentTop - halfExpandedOffset) < Math.abs(currentTop - collapsedOffset)) {
                    top = halfExpandedOffset
                    targetState = STBottomSheetBehaviorV2.STATE_HALF_EXPANDED
                    STLogUtil.e(TAG, "onStopNestedScroll lastNestedScrollDy($lastNestedScrollDy)<=0, fitToContents==false, STATE_HALF_EXPANDED")
                } else {
                    top = collapsedOffset
                    targetState = STBottomSheetBehaviorV2.STATE_COLLAPSED
                    STLogUtil.e(TAG, "onStopNestedScroll lastNestedScrollDy($lastNestedScrollDy)<=0, fitToContents==false, STATE_COLLAPSED")
                }
            }
        }

        if (enableCustomTargetTopAndStateOnStopNestedScroll) {
            val results: Array<Int> = customTargetTopAndState(child = child, targetOriginTop = top, targetOriginState = targetState, settleFromViewDragHelper = false)
            top = results[0]
            targetState = results[1]
        } else {
            startSettlingAnimation(child, targetState, top, false)
        }
        nestedScrolled = false

        STLogUtil.e(TAG, "onStopNestedScroll end top=$top, targetState=${getStateDescription(targetState)}")
    }
    //endregion

    //region set state with animation end callback
    @JvmOverloads
    fun getCurrentBottomSheetContainerHeight(bottomSheet: View? = getView()): Int {
        val currentBottomSheetContainerHeight = getParentHeight() - (bottomSheet?.top ?: 0)
        STLogUtil.sync { STLogUtil.d(TAG, "getCurrentBottomSheetContainerHeight currentBottomSheetContainerHeight=$currentBottomSheetContainerHeight, bottomSheet=$bottomSheet, getView()=${getView()}") }
        return currentBottomSheetContainerHeight
    }

    /**
     * @param parentHeight 务必填写正确的值, 最好在 setOnParentHeightChangedListener 里面执行, 因为此时获取的 parentHeight 最精确
     * @param peekHeight 如果是第一次设置, 务必填写正确的值, 因为第一次 getPeekHeight 是 0/-1
     */
    fun setStateByRealContentHeight(parentHeight: Int, peekHeight: Int, bottomSheetContentHeight: Int, callbackBeforeSetState: ((newEnableHalfExpandedState: Boolean) -> Unit)? = null, callbackAfterSetState: ((newEnableHalfExpandedState: Boolean) -> Unit)? = null) {
        val minExpandedOffset: Int = (parentHeight * 0.24f).toInt()
        val minHalfExpandedOffset: Int = (parentHeight * (1f - 0.4f)).toInt()
        val peekOffset: Int = parentHeight - peekHeight
        val realOffset: Int = parentHeight - bottomSheetContentHeight
        val maxExpandedOffsetOnDisableHalf: Int = peekOffset - 20.toPxFromDp() // 安全距离 20, 为避免正好多了几个像素这种极限情况
        val maxExpandedOffsetOnEnableHalf: Int = minHalfExpandedOffset - 20.toPxFromDp()

        STLogUtil.w(TAG, "setStateByRealHeight start parentHeight=$parentHeight, peekHeight=$peekHeight, realPanelContentHeight=$bottomSheetContentHeight, realOffset=$realOffset, minHalfExpandedOffset=$minHalfExpandedOffset")

        if (realOffset < minHalfExpandedOffset) {
            // 3.当面板高度大于等于中间态，不满扩展态，则扩展态自适应高度，仍支持三段式；
            enableHalfExpandedState = true

            val finalExpandedOffset = Math.max(Math.min(realOffset, maxExpandedOffsetOnEnableHalf), minExpandedOffset)
            STLogUtil.w(TAG, "setStateByRealHeight enableHalfExpandedState=$enableHalfExpandedState, finalExpandedOffset=$finalExpandedOffset, minHalfExpandedOffset=$minHalfExpandedOffset, parentHeight=$parentHeight")
            callbackBeforeSetState?.invoke(enableHalfExpandedState)
            setStateWithResetConfigs(
                state = STBottomSheetBehaviorV2.STATE_HALF_EXPANDED,
                enableAnimation = false,
                notifyOnStateChanged = true,
                forceSettlingOnSameState = true,
                parentHeight = parentHeight,
                expandedOffset = finalExpandedOffset,
                halfExpandedOffset = minHalfExpandedOffset,
                peekHeight = peekHeight,
                bottomSheetContentHeight = bottomSheetContentHeight
            ) {
                callbackAfterSetState?.invoke(enableHalfExpandedState)
                STLogUtil.w(TAG, "setStateByRealHeight callbackAfterSetState")
            }
        } else {
            // 1.不会存在不满收缩态
            // 2.当高于收缩态，低于中间态，则存在两段式（分别为中间态和收缩态），其中中间态自适应高度，箭头仍是横线，不支持上拉，支持下拉变为收缩态；
            enableHalfExpandedState = false

            val finalExpandedOffset = Math.min(maxExpandedOffsetOnDisableHalf, realOffset)
            STLogUtil.w(TAG, "setStateByRealHeight enableHalfExpandedState=$enableHalfExpandedState, finalExpandedOffset=$finalExpandedOffset, minHalfExpandedOffset=$minHalfExpandedOffset, parentHeight=$parentHeight")
            callbackBeforeSetState?.invoke(enableHalfExpandedState)
            setStateWithResetConfigs(
                state = STBottomSheetBehaviorV2.STATE_EXPANDED,
                enableAnimation = false,
                notifyOnStateChanged = true,
                forceSettlingOnSameState = true,
                parentHeight = parentHeight,
                expandedOffset = finalExpandedOffset,
                halfExpandedOffset = 0,
                peekHeight = peekHeight
            ) {
                callbackAfterSetState?.invoke(enableHalfExpandedState)
                STLogUtil.w(TAG, "setStateByRealHeight callbackAfterSetState")
            }
        }
        STLogUtil.w(TAG, "setStateByRealHeight end")
    }

    @UiThread
    override fun setState(@FinalState state: Int) {
        val finalState = wrapStateForEnableHalfExpanded(state)
        STLogUtil.w(TAG, "setState state=${getStateDescription(state)}, finalState=${getStateDescription(finalState)}, enableHalfExpandedState=$enableHalfExpandedState")

        super.setState(finalState)
        currentFinalState = finalState
    }

    /**
     * 当切换/显示/隐藏 虚拟导航栏导致的视觉问题时, 重新设置所有高度
     *
     * 做了以下事情 ->
     * 在第一次 onWindowFocusChanged 中获取正确的 parentHeight, 代替 isFirst
     * 使用 onConfigurationChanged/setOnApplyWindowInsetsListener 监听 parentHeight changed
     *
     * @param parentHeight 屏幕可视范围总高度为 状态栏 + 内容高度, 面板可滑动总高度, 不包含虚拟状态栏
     * @param expandedOffset 状态为 STATE_EXPANDED 时, getView().top 与屏幕顶部之间距离
     * @param halfExpandedOffset 状态为 STATE_HALF_EXPANDED 时, getView().top 与屏幕顶部之间距离
     * @param peekHeight 状态为 STATE_COLLAPSED 时, getView().top 与屏幕底部之间距离
     */
    /*
        private fun initBottomSheetBehavior() {
            bottomSheetBehavior.enableHalfExpandedState = true
            bottomSheetBehavior.dragEnabled = true
            bottomSheetBehavior.addBottomSheetCallback(onBottomSheetCallback)
            bottomSheetBehavior.setOnParentHeightChangedListener { parent, child, isFirst ->
                val parentHeight = parent.height
                STLogUtil.e(TAG, "onParentHeightChangedListener start isFirst=$isFirst, parentHeight=$parentHeight, getParentHeight=${bottomSheetBehavior.getParentHeight()}, currentFinalState=${bottomSheetBehavior.getStateDescription(bottomSheetBehavior.currentFinalState)}")
                STSystemUtil.showSystemInfo(this@STBehaviorBottomSheetActivity)
                bottomSheetBehavior.setStateOnParentHeightChanged(
                    state = if (isFirst) STBottomSheetBehaviorV2.STATE_HALF_EXPANDED else bottomSheetBehavior.currentFinalState,
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
    */
    @UiThread
    fun setStateWithResetConfigs(@FinalState state: Int = currentFinalState, enableAnimation: Boolean = true, notifyOnStateChanged: Boolean = true, forceSettlingOnSameState: Boolean = false, parentHeight: Int, expandedOffset: Int = getExpandedOffset(), halfExpandedOffset: Int = getHalfExpandedOffset(), peekHeight: Int = this.peekHeight, bottomSheetContentHeight: Int = parentHeight, onAnimationEndCallback: (() -> Unit)? = null) {
        STLogUtil.e(TAG, "setState start parentHeight=$parentHeight, currentFinalState=$currentFinalState, forceSettlingOnSameState=$forceSettlingOnSameState")
        if (parentHeight <= 0) {
            STLogUtil.e(TAG, "setState setParentHeight:$parentHeight failure, return")
            return
        }

        setParentHeight(parentHeight)

        this.setExpandedOffset(expandedOffset)
        fitToContents = true
        setFitToContentsOffset(expandedOffset)

        this.setCustomHalfExpandedOffset(halfExpandedOffset)
        this.setPeekHeight(peekHeight, false)
        this.calculateCollapsedOffset()


        val minHeightOffset: Int = 20.toPxFromDp()
        // 当真实内容高度超过 STATE_EXPANDED 时, 容器高度最大为 STATE_EXPANDED 高度, 否则自适应容器高度为真实内容高度, 即最大高度不能超过 STATE_EXPANDED
        // 当真实内容高度小于 STATE_EXPANDED 时, 最小不能低于 STATE_HALF_EXPANDED + 20dp 或者 STATE_COLLAPSED + 20dp, 20dp 作为缓冲高度
        val bottomSheetContainerHeight = Math.max(
            Math.min(bottomSheetContentHeight, getBottomSheetContainerHeightByState(STATE_EXPANDED)),
            ((if (enableHalfExpandedState) getBottomSheetContainerHeightByState(STATE_HALF_EXPANDED) else getBottomSheetContainerHeightByState(STATE_COLLAPSED)) + minHeightOffset)
        )
        this.setBottomSheetContainerHeight(bottomSheetContainerHeight)
        this.setStateWithCallback(state, enableAnimation, notifyOnStateChanged, forceSettlingOnSameState) {
            onAnimationEndCallback?.invoke()
        }
        STLogUtil.e(TAG, "setState end bottomSheetContainerHeight=$bottomSheetContainerHeight, bottomSheetContentHeight=$bottomSheetContentHeight")
    }

    @UiThread
    fun setStateWithCallback(@FinalState state: Int, enableAnimation: Boolean = true, notifyOnStateChanged: Boolean = true, forceSettlingOnSameState: Boolean, onAnimationEndCallback: (() -> Unit)? = null) {
        val finalState = wrapStateForEnableHalfExpanded(state)
        STLogUtil.w(TAG, "setStateWithCallback state=${getStateDescription(state)}, finalState=${getStateDescription(finalState)}, enableHalfExpandedState=$enableHalfExpandedState, forceSettlingOnSameState=$forceSettlingOnSameState")

        if (!forceSettlingOnSameState && finalState == getState()) {
            onAnimationEndCallback?.invoke()
            return
        }
        if (getViewRef() == null) {
            if (finalState == STBottomSheetBehaviorV2.STATE_COLLAPSED || finalState == STBottomSheetBehaviorV2.STATE_EXPANDED || finalState == STBottomSheetBehaviorV2.STATE_HALF_EXPANDED || (hideable && finalState == STBottomSheetBehaviorV2.STATE_HIDDEN)) {
                this.state = finalState
                if (!(hideable && finalState == STBottomSheetBehaviorV2.STATE_HIDDEN)) {
                    currentFinalState = finalState
                }
            }
            STLogUtil.e(TAG, "setStateWithCallback getViewRef() == null")
            onAnimationEndCallback?.invoke()
            return
        }
        STLogUtil.e(TAG, "setStateWithCallback settleToStatePendingLayoutWithCallback")
        settleToStatePendingLayoutWithCallback(finalState, enableAnimation, notifyOnStateChanged, onAnimationEndCallback)
    }

    private fun settleToStatePendingLayoutWithCallback(@State state: Int, enableAnimation: Boolean = true, notifyOnStateChanged: Boolean = true, onAnimationEndCallback: (() -> Unit)? = null) {
        val finalState = wrapStateForEnableHalfExpanded(state)
        STLogUtil.w(TAG, "settleToStatePendingLayoutWithCallback state=${getStateDescription(state)}, finalState=${getStateDescription(finalState)}, enableAnimation=$enableAnimation, notifyOnStateChanged=$notifyOnStateChanged")

        val child: V? = getView()
        if (child == null) {
            currentFinalState = finalState
            STLogUtil.e(TAG, "settleToStatePendingLayoutWithCallback child == null")
            onAnimationEndCallback?.invoke()
            return
        }

        super.state = finalState
        if (finalState == STBottomSheetBehaviorV2.STATE_COLLAPSED || finalState == STBottomSheetBehaviorV2.STATE_HALF_EXPANDED || finalState == STBottomSheetBehaviorV2.STATE_EXPANDED) {
            currentFinalState = finalState
        }

        // Start the animation; wait until a pending layout if there is one.
        val parent = child.parent
        if (parent != null && parent.isLayoutRequested && ViewCompat.isAttachedToWindow(child)) {
            child.post { settleToStateWithCallback(child, finalState, enableAnimation = enableAnimation, notifyOnStateChanged = notifyOnStateChanged, onAnimationEndCallback = onAnimationEndCallback) }
        } else {
            settleToStateWithCallback(child, finalState, enableAnimation = enableAnimation, notifyOnStateChanged = notifyOnStateChanged, onAnimationEndCallback = onAnimationEndCallback)
        }
    }

    private fun settleToStateWithCallback(child: View, state: Int, enableAnimation: Boolean = true, notifyOnStateChanged: Boolean = true, onAnimationEndCallback: (() -> Unit)? = null) {
        var finalState = wrapStateForEnableHalfExpanded(state)
        STLogUtil.w(TAG, "settleToStateWithCallback state=${getStateDescription(state)}, finalState=${getStateDescription(finalState)}, enableAnimation=$enableAnimation, notifyOnStateChanged=$notifyOnStateChanged")

        var top: Int
        if (finalState == STBottomSheetBehaviorV2.STATE_COLLAPSED) {
            top = collapsedOffset
        } else if (finalState == STBottomSheetBehaviorV2.STATE_HALF_EXPANDED) {
            top = halfExpandedOffset
            if (fitToContents && top <= fitToContentsOffset) {
                // Skip to the expanded state if we would scroll past the height of the contents.
                finalState = STBottomSheetBehaviorV2.STATE_EXPANDED
                top = fitToContentsOffset
            }
        } else if (finalState == STBottomSheetBehaviorV2.STATE_EXPANDED) {
            top = expandedOffset
        } else if (hideable && finalState == STBottomSheetBehaviorV2.STATE_HIDDEN) {
            top = parentHeight
        } else {
            throw IllegalArgumentException("settleToStateWithCallback Illegal state argument: $state")
        }

        super.state = finalState
        if (finalState == STBottomSheetBehaviorV2.STATE_COLLAPSED || finalState == STBottomSheetBehaviorV2.STATE_HALF_EXPANDED || finalState == STBottomSheetBehaviorV2.STATE_EXPANDED) {
            currentFinalState = finalState
        }

        if (!enableAnimation) {
            val finalTop: Int = top
            val dy: Int = finalTop - child.top
            STLogUtil.d(TAG, "settleToStateWithCallback offset begin dy=$dy, finalTop=$finalTop, child.top=${child.top}")
            ViewCompat.offsetTopAndBottom(child, dy)
            STLogUtil.d(TAG, "settleToStateWithCallback offset end dy=$dy, finalTop=$finalTop, child.top=${child.top}")
            setStateInternalWithCallback(finalState, notifyOnStateChanged = notifyOnStateChanged, onAnimationEndCallback = onAnimationEndCallback)
        } else {
            startSettlingAnimationWithCallback(child, finalState, top, false)
        }
    }

    fun startSettlingAnimationWithCallback(child: View, state: Int, top: Int, settleFromViewDragHelper: Boolean, notifyOnStateChanged: Boolean = true, onAnimationEndCallback: (() -> Unit)? = null) {
        val finalState = wrapStateForEnableHalfExpanded(state)
        STLogUtil.w(TAG, "startSettlingAnimationWithCallback state=${getStateDescription(state)}, finalState=${getStateDescription(finalState)}, notifyOnStateChanged=$notifyOnStateChanged")

        val startedSettling: Boolean = (viewDragHelper != null && if (settleFromViewDragHelper) viewDragHelper!!.settleCapturedViewAt(child.left, top) else viewDragHelper!!.smoothSlideViewTo(child, child.left, top))
        if (startedSettling) {
            setStateInternalWithCallback(STBottomSheetBehaviorV2.STATE_SETTLING, notifyOnStateChanged = notifyOnStateChanged, onAnimationEndCallback = onAnimationEndCallback)
            // STATE_SETTLING won't animate the material shape, so do that here with the target state.
            updateDrawableForTargetState(finalState)
            if (settleRunnable == null) {
                // If the singleton SettleRunnable instance has not been instantiated, create it.
                settleRunnable = SettleRunnableWithCallback(viewDragHelper, child, finalState, notifyOnStateChanged = notifyOnStateChanged, onAnimationEndCallback = onAnimationEndCallback)
            }
            // If the SettleRunnable has not been posted, post it with the correct state.
            if (!settleRunnable.isPosted) {
                settleRunnable.targetState = finalState
                ViewCompat.postOnAnimation(child, settleRunnable)
                settleRunnable.isPosted = true
            } else {
                // Otherwise, if it has been posted, just update the target state.
                settleRunnable.targetState = finalState
            }
        } else {
            setStateInternalWithCallback(finalState, notifyOnStateChanged = notifyOnStateChanged, onAnimationEndCallback = onAnimationEndCallback)
        }
    }

    override fun setStateInternal(state: Int) {
        super.setStateInternal(wrapStateForEnableHalfExpanded(state))
    }

    private fun setStateInternalWithCallback(state: Int, notifyOnStateChanged: Boolean = true, onAnimationEndCallback: (() -> Unit)? = null) {
        val finalState = wrapStateForEnableHalfExpanded(state)
        STLogUtil.w(TAG, "setStateInternalWithCallback state=${getStateDescription(state)}, finalState=$finalState, notifyOnStateChanged=$notifyOnStateChanged")

        if (this.state == finalState && !notifyOnStateChanged) {
            STLogUtil.e(TAG, "setStateInternalWithCallback this.state == finalState")
            onAnimationEndCallback?.invoke()
            return
        }
        this.state = finalState
        if (finalState == STBottomSheetBehaviorV2.STATE_COLLAPSED || finalState == STBottomSheetBehaviorV2.STATE_HALF_EXPANDED || finalState == STBottomSheetBehaviorV2.STATE_EXPANDED) {
            currentFinalState = finalState
        }

        val bottomSheet: View? = getView()
        if (bottomSheet == null) {
            STLogUtil.e(TAG, "setStateInternalWithCallback bottomSheet==null")
            onAnimationEndCallback?.invoke()
            return
        }

        if (state == STBottomSheetBehaviorV2.STATE_EXPANDED) {
            updateImportantForAccessibility(true)
        } else if (state == STBottomSheetBehaviorV2.STATE_HALF_EXPANDED || state == STBottomSheetBehaviorV2.STATE_HIDDEN || state == STBottomSheetBehaviorV2.STATE_COLLAPSED) {
            updateImportantForAccessibility(false)
        }

        updateDrawableForTargetState(state)
        if (notifyOnStateChanged) {
            for (i in callbacks.indices) {
                callbacks[i].onStateChanged(bottomSheet, state)
            }
        }
        updateAccessibilityActions()

        onAnimationEndCallback?.invoke()
    }

    /**
     * @see enableHalfExpandedState
     */
    private fun wrapStateForEnableHalfExpanded(state: Int): Int = if (!this.enableHalfExpandedState && state == STATE_HALF_EXPANDED) STATE_EXPANDED else state

    private inner class SettleRunnableWithCallback(val viewDragHelper: ViewDragHelper?, val view: View, targetState: Int, val notifyOnStateChanged: Boolean = true, val onAnimationEndCallback: (() -> Unit)? = null) : SettleRunnable(view, targetState) {
        override fun run() {
            STLogUtil.w(TAG, "---- SettleRunnableWithCallback#run targetState=${getStateDescription(targetState)}")
            if (viewDragHelper != null && viewDragHelper.continueSettling(true)) {
                ViewCompat.postOnAnimation(view, this)
            } else {
                setStateInternalWithCallback(targetState, notifyOnStateChanged = notifyOnStateChanged, onAnimationEndCallback = onAnimationEndCallback)
            }
        }
    }
    //endregion

    /**
     * 只有指定的 view 可以拖拽滑动, 内部的可滚动的子 view 只处理自己的滚动事件
     */
    private var enableDragOnlyOnSpecialTouchLayout: Boolean = false
    private var touchLayout: View? = null
    fun setDragOnlyOnSpecialTouchLayout(enableDragOnlyOnSpecialTouchLayout: () -> Boolean = { false }, touchLayout: STBottomSheetViewPagerBehavior.OnInterceptTouchEventHandler?, nestedScrollView: STBottomSheetViewPagerBehavior.OnInterceptTouchEventHandler?) {
        this.touchLayout = touchLayout?.getView()
        this.enableDragOnlyOnSpecialTouchLayout = enableDragOnlyOnSpecialTouchLayout()
        nestedScrollView?.setOnInterceptTouchEventHandler { motionEvent: MotionEvent? ->
            when (motionEvent?.action) {
                MotionEvent.ACTION_DOWN -> {
                    this.enableDragOnlyOnSpecialTouchLayout = enableDragOnlyOnSpecialTouchLayout()
                    dragEnabled = false //enableDragBottomSheetOnNestedScrollViewActionDown()
                    STLogUtil.d(TAG, "ACTION_DOWN nestedScrollView")
                }
                MotionEvent.ACTION_UP -> {
                    STLogUtil.d(TAG, "ACTION_UP nestedScrollView")
                    dragEnabled = true
                }
                MotionEvent.ACTION_CANCEL -> {
                    STLogUtil.d(TAG, "ACTION_CANCEL nestedScrollView")
                    dragEnabled = true
                }
                MotionEvent.ACTION_MOVE -> {
                    STLogUtil.d(TAG, "ACTION_MOVE nestedScrollView")
                }
                else -> {
                    STLogUtil.d(TAG, "ACTION_OTHER nestedScrollView")
                }
            }
        }
        touchLayout?.setOnInterceptTouchEventHandler { motionEvent: MotionEvent? ->
            when (motionEvent?.action) {
                MotionEvent.ACTION_DOWN -> {
                    STLogUtil.d(TAG, "ACTION_DOWN touchLayout")
                    dragEnabled = true
                }
                MotionEvent.ACTION_UP -> {
                    STLogUtil.d(TAG, "ACTION_UP touchLayout")
                }
                MotionEvent.ACTION_CANCEL -> {
                    STLogUtil.d(TAG, "ACTION_CANCEL touchLayout")
                }
                MotionEvent.ACTION_MOVE -> {
                    STLogUtil.d(TAG, "ACTION_MOVE touchLayout")
                }
                else -> {
                    STLogUtil.d(TAG, "ACTION_OTHER touchLayout")
                }
            }
        }
    }

    /*interface OnInterceptTouchEventHandler {
        fun setOnInterceptTouchEventHandler(onInterceptTouchEventHandler: (ev: MotionEvent?) -> Unit)
        fun onInterceptTouchEvent(ev: MotionEvent?): Boolean
        fun getView(): View
    }*/

    @IntDef(STATE_EXPANDED, STATE_HALF_EXPANDED, STATE_COLLAPSED)
    annotation class FinalState

    @Suppress("unused")
    companion object {
        const val STATE_DRAGGING = STBottomSheetBehaviorV2.STATE_DRAGGING
        const val STATE_SETTLING = STBottomSheetBehaviorV2.STATE_SETTLING
        const val STATE_EXPANDED = STBottomSheetBehaviorV2.STATE_EXPANDED
        const val STATE_COLLAPSED = STBottomSheetBehaviorV2.STATE_COLLAPSED
        const val STATE_HIDDEN = STBottomSheetBehaviorV2.STATE_HIDDEN
        const val STATE_HALF_EXPANDED = STBottomSheetBehaviorV2.STATE_HALF_EXPANDED
        const val PEEK_HEIGHT_AUTO = STBottomSheetBehaviorV2.PEEK_HEIGHT_AUTO
        const val TAG = "[BottomSheet]"

        @JvmStatic
        fun getStateDescription(state: Int): String = when (state) {
            STATE_DRAGGING -> "STATE_DRAGGING"
            STATE_SETTLING -> "STATE_SETTLING"
            STATE_EXPANDED -> "STATE_EXPANDED"
            STATE_COLLAPSED -> "STATE_COLLAPSED"
            STATE_HIDDEN -> "STATE_HIDDEN"
            STATE_HALF_EXPANDED -> "STATE_HALF_EXPANDED"
            else -> "$state"
        }

        /**
         * A utility function to get the [STBottomSheetViewPagerBehaviorV2] associated with the `view`.
         *
         * @param view The [View] with [STBottomSheetViewPagerBehaviorV2].
         * @return The [STBottomSheetViewPagerBehaviorV2] associated with the `view`.
         */
        @Suppress("UNCHECKED_CAST")
        fun <V : View> from(view: V): STBottomSheetViewPagerBehaviorV2<V> {
            val params: ViewGroup.LayoutParams = view.layoutParams
            require(params is CoordinatorLayout.LayoutParams) { "The view is not a child of CoordinatorLayout" }
            val behavior: CoordinatorLayout.Behavior<*>? = params.behavior
            require(behavior is STBottomSheetViewPagerBehaviorV2<*>) { "The view is not associated with STBottomSheetViewPagerBehavior" }
            return behavior as STBottomSheetViewPagerBehaviorV2<V>
        }
    }
}