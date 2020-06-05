package com.smart.library.widget.behavior

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.math.MathUtils
import androidx.core.view.ViewCompat
import androidx.customview.widget.ViewDragHelper
import androidx.viewpager.widget.ViewPager
import com.smart.library.source.STBottomSheetBehavior
import com.smart.library.util.STLogUtil
import java.lang.ref.WeakReference
import kotlin.math.max
import kotlin.math.min

/**
 * -keep class STBottomSheetViewPagerBehavior{*;}
 *
 * Override [.findScrollingChild] to support [ViewPager]'s nested scrolling.
 *
 * By the way, In order to override package level method and field.
 * This class put in the same package path where [STBottomSheetBehavior] located.
 */
@Suppress("MemberVisibilityCanBePrivate", "ReplaceJavaStaticMethodWithKotlinAnalog", "LiftReturnOrAssignment")
class STBottomSheetViewPagerBehavior<V : View> @JvmOverloads constructor(context: Context? = null, attrs: AttributeSet? = null) : STBottomSheetBehavior<V>(context, attrs) {

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
     */
    var enableHalfExpandedState = false

    private var pullTopToBottom: Boolean? = null
    private var lastBottomSheetDragTop: Int = -1
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

    init {
        dragCallback = object : ViewDragHelper.Callback() {
            override fun tryCaptureView(child: View, pointerId: Int): Boolean {
                if (state == STBottomSheetBehavior.STATE_DRAGGING) {
                    return false
                }
                if (touchingScrollingChild) {
                    return false
                }
                if (state == STBottomSheetBehavior.STATE_EXPANDED && activePointerId == pointerId) {
                    val scroll = nestedScrollingChildRef.get()
                    if (scroll != null && scroll.canScrollVertically(-1)) {
                        return false
                    }
                }
                return viewRef != null && viewRef.get() === child
            }

            override fun onViewPositionChanged(changedView: View, left: Int, top: Int, dx: Int, dy: Int) {
                lastBottomSheetDragTop = top
                dispatchOnSlide(top)
            }

            override fun onViewDragStateChanged(state: Int) {
                if (state == STBottomSheetBehavior.STATE_DRAGGING) {
                    setStateInternal(STBottomSheetBehavior.STATE_DRAGGING)
                }
            }

            override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
                var top: Int
                var targetState: Int
                val currentTop = releasedChild.top
                val currentPullTopToBottom = pullTopToBottom

                STLogUtil.w(TAG, "onViewReleased start pullTopToBottom=$pullTopToBottom")
                STLogUtil.d(TAG, "onViewReleased yvel=$yvel,enableHalfExpandedState=$enableHalfExpandedState, expandedOffset=$expandedOffset, currentPullTopToBottom=$currentPullTopToBottom, currentTop=$currentTop,")
                STLogUtil.d(TAG, "onViewReleased bottomSheetHalfExpandTop=$bottomSheetHalfExpandTop, dragThresholdOffset()=${dragThresholdOffset()}, collapsedOffset=$collapsedOffset, fitToContentsOffset=$fitToContentsOffset")
                STLogUtil.d(TAG, "onViewReleased getViewVerticalDragRange()=${getViewVerticalDragRange()}, fitToContents=$fitToContents")

                // 维持原代码不变
                if (yvel < 0.0f) {
                    STLogUtil.e(TAG, "onViewReleased yvel < 0.0f")
                    if (fitToContents) {
                        top = fitToContentsOffset
                        targetState = STBottomSheetBehavior.STATE_EXPANDED
                    } else {
                        if (currentTop > halfExpandedOffset) {
                            top = halfExpandedOffset
                            targetState = STBottomSheetBehavior.STATE_HALF_EXPANDED
                        } else {
                            top = 0
                            targetState = STBottomSheetBehavior.STATE_EXPANDED
                        }
                    }
                } else if (hideable && shouldHide(releasedChild, yvel) && (releasedChild.top > collapsedOffset || Math.abs(xvel) < Math.abs(yvel))) {
                    STLogUtil.e(TAG, "onViewReleased hideable=$hideable")
                    top = parentHeight
                    targetState = STBottomSheetBehavior.STATE_HIDDEN
                } else if (yvel == 0.0f || Math.abs(xvel) > Math.abs(yvel)) {
                    STLogUtil.e(TAG, "onViewReleased yvel == 0.0f")
                    if (fitToContents) {
                        if (Math.abs(currentTop - fitToContentsOffset) < Math.abs(currentTop - collapsedOffset)) {
                            top = fitToContentsOffset
                            targetState = STBottomSheetBehavior.STATE_EXPANDED
                        } else {
                            top = collapsedOffset
                            targetState = STBottomSheetBehavior.STATE_COLLAPSED
                        }
                    } else if (currentTop < halfExpandedOffset) {
                        if (currentTop < Math.abs(currentTop - collapsedOffset)) {
                            top = 0
                            targetState = STBottomSheetBehavior.STATE_EXPANDED
                        } else {
                            top = halfExpandedOffset
                            targetState = STBottomSheetBehavior.STATE_HALF_EXPANDED
                        }
                    } else if (Math.abs(currentTop - halfExpandedOffset) < Math.abs(currentTop - collapsedOffset)) {
                        top = halfExpandedOffset
                        targetState = STBottomSheetBehavior.STATE_HALF_EXPANDED
                    } else {
                        top = collapsedOffset
                        targetState = STBottomSheetBehavior.STATE_COLLAPSED
                    }
                } else {
                    STLogUtil.d(TAG, "onViewReleased  yvel > 0.0f set targetState=STATE_COLLAPSED")
                    top = collapsedOffset
                    targetState = STBottomSheetBehavior.STATE_COLLAPSED
                }

                // 根据自己的业务重置 top and targetState
                if (currentPullTopToBottom != null) {
                    if (enableHalfExpandedState) {
                        when {
                            currentTop < (if (currentPullTopToBottom) (dragThresholdOffset()) else (bottomSheetHalfExpandTop + dragThresholdOffset())) -> {
                                STLogUtil.d(TAG, "onViewReleased set targetState=STATE_EXPANDED")
                                top = expandedOffset
                                targetState = STATE_EXPANDED
                            }
                            currentTop < (if (currentPullTopToBottom) (bottomSheetHalfExpandTop + dragThresholdOffset()) else (collapsedOffset - dragThresholdOffset())) -> {
                                STLogUtil.d(TAG, "onViewReleased set targetState=STATE_HALF_EXPANDED")
                                top = halfExpandedOffset
                                targetState = STATE_HALF_EXPANDED
                            }
                            else -> {
                                STLogUtil.d(TAG, "onViewReleased set targetState=STATE_COLLAPSED")
                                top = collapsedOffset
                                targetState = STATE_COLLAPSED
                            }
                        }
                    } else {
                        when {
                            currentTop < (if (currentPullTopToBottom) (dragThresholdOffset()) else (collapsedOffset - dragThresholdOffset())) -> {
                                STLogUtil.d(TAG, "onViewReleased set targetState=STATE_EXPANDED")
                                targetState = STATE_EXPANDED
                            }
                            else -> {
                                STLogUtil.d(TAG, "onViewReleased set targetState=STATE_COLLAPSED")
                                top = collapsedOffset
                                targetState = STATE_COLLAPSED
                            }
                        }
                    }
                }

                if (viewDragHelper.settleCapturedViewAt(releasedChild.left, top)) {
                    STLogUtil.w(TAG, "onViewReleased setStateInternal STATE_SETTLING")
                    setStateInternal(STBottomSheetBehavior.STATE_SETTLING)
                    ViewCompat.postOnAnimation(releasedChild, SettleRunnable(releasedChild, targetState))
                } else {
                    STLogUtil.w(TAG, "onViewReleased setStateInternal targetState=$targetState")
                    setStateInternal(targetState)
                }

                // 重置
                lastBottomSheetDragTop = -1

                STLogUtil.w(TAG, "onViewReleased end pullTopToBottom=$pullTopToBottom")
            }

            override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
                return MathUtils.clamp(top, expandedOffset, if (hideable) parentHeight else collapsedOffset)
            }

            override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
                return child.left
            }

            override fun getViewVerticalDragRange(child: View): Int {
                return if (hideable) {
                    parentHeight
                } else collapsedOffset
            }
        }
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
        (viewRef.get() as? View)?.let {
            val scrollingChild = findScrollingChild(it)
            nestedScrollingChildRef = WeakReference<View>(scrollingChild)
        }
    }

    private val onPageChangeListener by lazy {
        object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {}

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {}

            override fun onPageSelected(p0: Int) {
                updateScrollingChild()
            }
        }
    }

    var bottomSheetHalfExpandTop: Int = 0
    override fun setHalfExpandedOffset(halfExpandedOffset: Int): Int {
        return if (bottomSheetHalfExpandTop > 0) bottomSheetHalfExpandTop else super.setHalfExpandedOffset(halfExpandedOffset)
    }

    /**
     * 当 viewPager 页面切换时, 更新 nestedScrollingChildRef
     */
    fun bindViewPager(viewPager: ViewPager) {
        viewPager.removeOnPageChangeListener(onPageChangeListener)
        viewPager.addOnPageChangeListener(onPageChangeListener)
    }

    /**********************************************************************************************
     * drag control start
     **********************************************************************************************/
    /**
     * 控制 bottom sheet 能否被拖拽滑动
     */
    var dragEnabled = true

    override fun onTouchEvent(parent: CoordinatorLayout, child: V, event: MotionEvent): Boolean = if (dragEnabled) super.onTouchEvent(parent, child, event) else false
    override fun onInterceptTouchEvent(parent: CoordinatorLayout, child: V, event: MotionEvent): Boolean = if (dragEnabled) super.onInterceptTouchEvent(parent, child, event) else false
    override fun onNestedPreFling(coordinatorLayout: CoordinatorLayout, child: V, target: View, velocityX: Float, velocityY: Float): Boolean = if (dragEnabled) super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY) else false
    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout, child: V, directTargetChild: View, target: View, axes: Int, type: Int): Boolean = if (dragEnabled) super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes, type) else false

    override fun onNestedPreScroll(coordinatorLayout: CoordinatorLayout, child: V, target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        if (dragEnabled) super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
    }

    override fun onStopNestedScroll(coordinatorLayout: CoordinatorLayout, child: V, target: View, type: Int) {
        if (dragEnabled) super.onStopNestedScroll(coordinatorLayout, child, target, type)
    }

    /**********************************************************************************************
     * drag control end
     **********************************************************************************************/

    @Suppress("unused")
    companion object {
        const val STATE_DRAGGING = STBottomSheetBehavior.STATE_DRAGGING
        const val STATE_SETTLING = STBottomSheetBehavior.STATE_SETTLING
        const val STATE_EXPANDED = STBottomSheetBehavior.STATE_EXPANDED
        const val STATE_COLLAPSED = STBottomSheetBehavior.STATE_COLLAPSED
        const val STATE_HIDDEN = STBottomSheetBehavior.STATE_HIDDEN
        const val STATE_HALF_EXPANDED = STBottomSheetBehavior.STATE_HALF_EXPANDED
        const val PEEK_HEIGHT_AUTO = STBottomSheetBehavior.PEEK_HEIGHT_AUTO
        const val TAG = "sheet-ViewPagerBehavior"

        /**
         * A utility function to get the [STBottomSheetViewPagerBehavior] associated with the `view`.
         *
         * @param view The [View] with [STBottomSheetViewPagerBehavior].
         * @return The [STBottomSheetViewPagerBehavior] associated with the `view`.
         */
        @Suppress("UNCHECKED_CAST")
        fun <V : View> from(view: V): STBottomSheetViewPagerBehavior<V> {
            val params = view.layoutParams
            require(params is CoordinatorLayout.LayoutParams) { "The view is not a child of CoordinatorLayout" }
            val behavior = params.behavior
            require(behavior is STBottomSheetViewPagerBehavior<*>) { "The view is not associated with STBottomSheetViewPagerBehavior" }
            return behavior as STBottomSheetViewPagerBehavior<V>
        }
    }
}