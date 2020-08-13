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
 * -keep class smart.library.widget.behavior.STBottomSheetViewPagerBehavior{*;}
 *
 * Override [.findScrollingChild] to support [ViewPager]'s nested scrolling.
 *
 * By the way, In order to override package level method and field.
 * This class put in the same package path where [STBottomSheetBehavior] located.
 */
@Suppress("MemberVisibilityCanBePrivate", "ReplaceJavaStaticMethodWithKotlinAnalog", "LiftReturnOrAssignment", "unused")
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

    var currentFinalState: Int = -1 // 首次 setState 之前为 -1, 标记第一次
        private set

    init {
        dragCallback = object : ViewDragHelper.Callback() {
            override fun tryCaptureView(child: View, pointerId: Int): Boolean {
                if (state == STATE_DRAGGING) {
                    return false
                }
                if (touchingScrollingChild) {
                    return false
                }
                if (state == STATE_EXPANDED && activePointerId == pointerId) {
                    val scroll = getNestedScrollingChildRef()?.get()
                    if (scroll != null && scroll.canScrollVertically(-1)) {
                        return false
                    }
                }
                return getViewRef() != null && getViewRef()?.get() === child
            }

            override fun onViewPositionChanged(changedView: View, left: Int, top: Int, dx: Int, dy: Int) {
                lastBottomSheetDragTop = top
                dispatchOnSlide(top)
            }

            override fun onViewDragStateChanged(state: Int) {
                if (state == STATE_DRAGGING) {
                    setStateInternal(STATE_DRAGGING)
                }
            }

            override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
                var top: Int
                var targetState: Int
                val currentTop = releasedChild.top
                val currentPullTopToBottom = pullTopToBottom

                if (STLogUtil.debug) STLogUtil.w(TAG, "onViewReleased start pullTopToBottom=$pullTopToBottom")
                if (STLogUtil.debug) STLogUtil.d(TAG, "onViewReleased yvel=$yvel,enableHalfExpandedState=$enableHalfExpandedState, expandedOffset=$expandedOffset, currentPullTopToBottom=$currentPullTopToBottom, currentTop=$currentTop,")
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
                            top = 0
                            targetState = STATE_EXPANDED
                        }
                    }
                } else if (hideable && shouldHide(releasedChild, yvel) && (releasedChild.top > collapsedOffset || Math.abs(xvel) < Math.abs(yvel))) {
                    STLogUtil.e(TAG, "onViewReleased hideable=$hideable")
                    top = parentHeight
                    targetState = STATE_HIDDEN
                } else if (yvel == 0.0f || Math.abs(xvel) > Math.abs(yvel)) {
                    STLogUtil.e(TAG, "onViewReleased yvel == 0.0f")
                    if (fitToContents) {
                        if (Math.abs(currentTop - fitToContentsOffset) < Math.abs(currentTop - collapsedOffset)) {
                            top = fitToContentsOffset
                            targetState = STATE_EXPANDED
                        } else {
                            top = collapsedOffset
                            targetState = STATE_COLLAPSED
                        }
                    } else if (currentTop < halfExpandedOffset) {
                        if (currentTop < Math.abs(currentTop - collapsedOffset)) {
                            top = 0
                            targetState = STATE_EXPANDED
                        } else {
                            top = halfExpandedOffset
                            targetState = STATE_HALF_EXPANDED
                        }
                    } else if (Math.abs(currentTop - halfExpandedOffset) < Math.abs(currentTop - collapsedOffset)) {
                        top = halfExpandedOffset
                        targetState = STATE_HALF_EXPANDED
                    } else {
                        top = collapsedOffset
                        targetState = STATE_COLLAPSED
                    }
                } else {
                    STLogUtil.d(TAG, "onViewReleased  yvel > 0.0f set targetState=STATE_COLLAPSED")
                    top = collapsedOffset
                    targetState = STATE_COLLAPSED
                }

                // 根据自己的业务重置 top and targetState
                if (currentPullTopToBottom != null) {
                    if (enableHalfExpandedState) {
                        when {
                            currentTop < (if (currentPullTopToBottom) (dragThresholdOffset()) else (getHalfExpandedOffset() + dragThresholdOffset())) -> {
                                STLogUtil.d(TAG, "onViewReleased set targetState=STATE_EXPANDED")
                                top = expandedOffset
                                targetState = STATE_EXPANDED
                            }
                            currentTop < (if (currentPullTopToBottom) (getHalfExpandedOffset() + dragThresholdOffset()) else (collapsedOffset - dragThresholdOffset())) -> {
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

                STLogUtil.e(TAG, "onViewReleased halfExpandedOffset=$halfExpandedOffset, top=$top, collapsedOffset=$collapsedOffset, fitToContentsOffset=$fitToContentsOffset")
                if (viewDragHelper.settleCapturedViewAt(releasedChild.left, top)) {
                    STLogUtil.w(TAG, "onViewReleased setStateInternal STATE_SETTLING")
                    setStateInternal(STATE_SETTLING)
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
        super.setBottomSheetCallback(object : BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, percent: Float) {
                // 遍历过程中 removeBottomSheetCallback 时是不安全的
                callbackSet.forEach { it.onSlide(bottomSheet, percent) }
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    STBottomSheetBehavior.STATE_EXPANDED -> {
                        currentFinalState = newState
                    }
                    STBottomSheetBehavior.STATE_HALF_EXPANDED -> {
                        currentFinalState = newState
                    }
                    STBottomSheetBehavior.STATE_COLLAPSED -> {
                        currentFinalState = newState
                    }
                    else -> {
                    }
                }
                // 遍历过程中 removeBottomSheetCallback 时是不安全的
                callbackSet.forEach { it.onStateChanged(bottomSheet, newState) }
            }
        })
    }

    //region new callback api
    private val callbackSet: MutableSet<BottomSheetCallback> = mutableSetOf()

    @Deprecated("addBottomSheetCallback", ReplaceWith("addBottomSheetCallback(callback)"))
    override fun setBottomSheetCallback(callback: BottomSheetCallback?) {
        addBottomSheetCallback(callback)
    }

    fun addBottomSheetCallback(callback: BottomSheetCallback?) {
        if (callback != null) {
            callbackSet.add(callback)
        }
    }

    /**
     * 遍历过程中 removeBottomSheetCallback 是不安全的
     */
    fun removeBottomSheetCallback(callback: BottomSheetCallback?) {
        if (callback != null) {
            callbackSet.remove(callback)
        }
    }

    fun removeAllBottomSheetCallbacks() {
        callbackSet.clear()
    }
    //endregion

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

    private val onPageChangeListener by lazy {
        object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {}

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {}

            override fun onPageSelected(p0: Int) {
                updateScrollingChild()
            }
        }
    }

    public override fun setHalfExpandedOffset(halfExpandedOffset: Int): Int {
        return super.setHalfExpandedOffset(halfExpandedOffset)
    }

    public override fun calculateCollapsedOffset() {
        super.calculateCollapsedOffset()
    }

    fun getHalfExpandedOffset(): Int {
        return this.halfExpandedOffset
    }

    fun getCollapsedOffset(): Int {
        return this.collapsedOffset
    }

    private fun setParentHeight(parentHeight: Int): Boolean {
        if (parentHeight <= 0 || parentHeight == this.parentHeight) {
            STLogUtil.e(TAG, "setParentHeight:$parentHeight failure, return false")
            return false
        }
        this.parentHeight = parentHeight
        STLogUtil.e(TAG, "setParentHeight:$parentHeight success, return true")
        return true
    }

    fun calculateExpandedOffset(expandedOffset: Int = Math.max(0, getParentHeight() - (getView()?.height ?: getParentHeight()))) {
        this.fitToContentsOffset = expandedOffset
        STLogUtil.w(TAG, "calculateExpandedOffset fitToContentsOffset=$fitToContentsOffset, expandedOffset=$expandedOffset, getView()?.height=${getView()?.height}, getParentHeight=${getParentHeight()}")
    }

    /**
     * 面板距离页面顶部距离, 面板距离屏幕顶部的距离
     */
    public override fun getExpandedOffset(): Int {
        return super.getExpandedOffset()
    }

    /**
     * 重设面板可滑动的高度/状态, 当虚拟键盘切换/隐藏/显示导致屏幕内容高度变化时
     *
     * @param parentHeight 屏幕可视范围总高度为 状态栏 + 内容高度, 面板总高度
     * @param expandedOffset 面板距离页面顶部距离, 面板距离屏幕顶部的距离
     * @param halfExpandedOffset 面板中间距离, 面板距离页面底部距离, 面板一半的高度
     */
    fun setStateOnParentHeightChanged(state: Int = currentFinalState, parentHeight: Int, expandedOffset: Int = getExpandedOffset(), halfExpandedOffset: Int = getHalfExpandedOffset(), peekHeight: Int = this.peekHeight) {
        STLogUtil.e(TAG, "resetBottomSheetViews start parentHeight=$parentHeight, currentFinalState=$currentFinalState")
        if (!setParentHeight(parentHeight) && (parentHeight > 0 && currentFinalState != -1)) {
            STLogUtil.e(TAG, "resetBottomSheetViews setParentHeight:$parentHeight failure, return")
            return
        }
        this.calculateExpandedOffset(expandedOffset)
        // must be call after calculateExpandedOffset
        this.setBottomSheetContainerHeight(getBottomSheetContainerVisibleHeightByState(STBottomSheetBehavior.STATE_EXPANDED))

        this.setHalfExpandedOffset(halfExpandedOffset)
        this.calculateCollapsedOffset()

        this.setPeekHeight(peekHeight)
        this.setState(state)
        STLogUtil.e(TAG, "resetBottomSheetViews end")
    }

    override fun setState(state: Int) {
        super.setState(state)
        currentFinalState = state
    }

    fun getParentHeight(): Int {
        return this.parentHeight
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
    private fun getBottomSheetContainerVisibleHeightByState(state: Int): Int {
        val heightOnStateExpanded: Int = getParentHeight() - expandedOffset
        val heightOnStateHalfExpanded: Int = getHalfExpandedOffset()
        val heightOnStateCollapsed: Int = getPeekHeight()

        val panelHeight = when (state) {
            STATE_EXPANDED -> heightOnStateExpanded
            STATE_HALF_EXPANDED -> heightOnStateHalfExpanded
            STATE_COLLAPSED -> heightOnStateCollapsed
            else -> heightOnStateHalfExpanded
        }

        STLogUtil.sync { STLogUtil.d(TAG, "getPanelHeightByState ${getStateDescription(state)}, panelHeight=$panelHeight") }
        return panelHeight
    }

    /**
     * 设置浮层面板容器的高度
     */
    private fun setBottomSheetContainerHeight(height: Int) {
        val bottomSheetContainer: View? = getView()
        val params: CoordinatorLayout.LayoutParams? = bottomSheetContainer?.layoutParams as? CoordinatorLayout.LayoutParams
        if (params != null) {
            params.height = height
            bottomSheetContainer.layoutParams = params
        }
    }

    fun getStateDescription(state: Int): String = when (state) {
        STATE_DRAGGING -> "STATE_DRAGGING"
        STATE_SETTLING -> "STATE_SETTLING"
        STATE_EXPANDED -> "STATE_EXPANDED"
        STATE_COLLAPSED -> "STATE_COLLAPSED"
        STATE_HIDDEN -> "STATE_HIDDEN"
        STATE_HALF_EXPANDED -> "STATE_HALF_EXPANDED"
        STBottomSheetBehavior.PEEK_HEIGHT_AUTO -> "PEEK_HEIGHT_AUTO"
        else -> "UNKNOWN_$state"
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
     * 设置 当拖拽从下拉 recyclerView 列表到顶部的时候, 继续下拉是否联动面板
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
        const val TAG = "[sheet-ViewPagerBehavior]"

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