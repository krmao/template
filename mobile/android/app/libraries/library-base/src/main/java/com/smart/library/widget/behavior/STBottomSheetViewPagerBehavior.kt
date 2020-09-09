package com.smart.library.widget.behavior

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IntDef
import androidx.annotation.UiThread
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.math.MathUtils
import androidx.core.view.ViewCompat
import androidx.customview.widget.ViewDragHelper
import androidx.viewpager.widget.ViewPager
import com.smart.library.base.toPxFromDp
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
@Suppress("MemberVisibilityCanBePrivate", "ReplaceJavaStaticMethodWithKotlinAnalog", "LiftReturnOrAssignment", "unused", "UsePropertyAccessSyntax", "RedundantOverride", "ProtectedInFinal")
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
     * @see wrapStateForEnableHalfExpanded
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

        //region rewrite dragCallback
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
                STLogUtil.d(TAG, "onViewPositionChanged lastBottomSheetDragTop=$lastBottomSheetDragTop")
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
                if (STLogUtil.debug) STLogUtil.w(TAG, "onViewReleased start pullTopToBottom=$pullTopToBottom, lastBottomSheetDragTop=$lastBottomSheetDragTop")
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

                // 垂直速度为0, 总位置没有变动, 且当前位置正好与目标位置一致, 则不需要执行动画
                var isYVelZeroAndTotalOffsetZeroAndTopIsJustTargetStateOffset = false

                // 根据自己的业务重置 top and targetState
                if (currentPullTopToBottom != null) {
                    if (enableHalfExpandedState) {
                        when {
                            currentTop < (if (currentPullTopToBottom) (dragThresholdOffset()) else (getHalfExpandedOffset() - dragThresholdOffset())) -> {
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
                } else {
                    // 垂直速度为0, 总位置没有变动, 且当前位置正好与目标位置一致, 则不需要执行动画
                    if (yvel == 0f && lastBottomSheetDragTop == -1) {
                        when (currentTop) {
                            halfExpandedOffset -> {
                                top = halfExpandedOffset
                                targetState = STATE_HALF_EXPANDED
                                STLogUtil.d(TAG, "onViewReleased currentPullTopToBottom==null, lastBottomSheetDragTop=$lastBottomSheetDragTop, currentTop=$currentTop, set targetState=STATE_HALF_EXPANDED")
                                isYVelZeroAndTotalOffsetZeroAndTopIsJustTargetStateOffset = true
                            }
                            collapsedOffset -> {
                                top = collapsedOffset
                                targetState = STATE_COLLAPSED
                                STLogUtil.d(TAG, "onViewReleased currentPullTopToBottom==null, lastBottomSheetDragTop=$lastBottomSheetDragTop, currentTop=$currentTop, set targetState=STATE_COLLAPSED")
                                isYVelZeroAndTotalOffsetZeroAndTopIsJustTargetStateOffset = true
                            }
                            expandedOffset -> {
                                top = expandedOffset
                                targetState = STATE_EXPANDED
                                STLogUtil.d(TAG, "onViewReleased currentPullTopToBottom==null, lastBottomSheetDragTop=$lastBottomSheetDragTop, currentTop=$currentTop, set targetState=STATE_EXPANDED")
                                isYVelZeroAndTotalOffsetZeroAndTopIsJustTargetStateOffset = true
                            }
                            else -> {
                                STLogUtil.d(TAG, "onViewReleased currentPullTopToBottom==null, lastBottomSheetDragTop=$lastBottomSheetDragTop, currentTop=$currentTop)")
                            }
                        }
                    }
                }

                STLogUtil.e(TAG, "onViewReleased isYVelZeroAndTotalOffsetZeroAndTopIsJustTargetStateOffset=$isYVelZeroAndTotalOffsetZeroAndTopIsJustTargetStateOffset, halfExpandedOffset=$halfExpandedOffset, top=$top, collapsedOffset=$collapsedOffset, fitToContentsOffset=$fitToContentsOffset")
                if (!isYVelZeroAndTotalOffsetZeroAndTopIsJustTargetStateOffset && viewDragHelper.settleCapturedViewAt(releasedChild.left, top)) {
                    STLogUtil.w(TAG, "onViewReleased setStateInternal STATE_SETTLING, lastBottomSheetDragTop=$lastBottomSheetDragTop")
                    setStateInternal(STATE_SETTLING)
                    ViewCompat.postOnAnimation(releasedChild, SettleRunnable(releasedChild, targetState))
                } else {
                    STLogUtil.w(TAG, "onViewReleased setStateInternal targetState=$targetState, lastBottomSheetDragTop=$lastBottomSheetDragTop")
                    setStateInternal(targetState)
                }

                // 重置
                lastBottomSheetDragTop = -1

                STLogUtil.w(TAG, "onViewReleased end pullTopToBottom=$pullTopToBottom, lastBottomSheetDragTop=$lastBottomSheetDragTop")
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
        //endregion

        //region only set once BottomSheetCallback
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
        //endregion
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

    //region window insets changed 时会调用
    private var currentParentHeightOnSetFinalState: Int = 0

    /**
     * 切换/显示/隐藏 虚拟导航栏时, 会重新调用 onLayoutChild
     * 切换 app 也会调用
     */
    override fun onLayoutChild(parent: CoordinatorLayout, child: V, layoutDirection: Int): Boolean {
        STLogUtil.w(TAG, "onLayoutChild start currentParentHeight=$currentParentHeightOnSetFinalState, parentHeight=${getParentHeight()}, parent.height=${parent.height}")
        val onLayoutChild = super.onLayoutChild(parent, child, layoutDirection)
        val newParentHeight = parent.height
        if (newParentHeight > 0 && newParentHeight != currentParentHeightOnSetFinalState) {
            onParentHeightChangedListener?.invoke(parent, child, currentFinalState == -1)
        }
        STLogUtil.w(TAG, "onLayoutChild end currentParentHeight=$currentParentHeightOnSetFinalState, parentHeight=${getParentHeight()}, parent.height=${parent.height}")
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
    fun setCustomHalfExpandedOffset(halfExpandedOffset: Int): Int {
        this.customHalfExpandedOffset = halfExpandedOffset
        setHalfExpandedOffset(getParentHeight() / 2)
        return halfExpandedOffset
    }

    /**
     * 在 onLayoutChild 中, super.setHalfExpandedOffset 会被重置为 parentHeight 的一半
     */
    override fun setHalfExpandedOffset(halfExpandedOffset: Int): Int {
        return super.setHalfExpandedOffset(if (customHalfExpandedOffset != -1) customHalfExpandedOffset else halfExpandedOffset)
    }

    override fun calculateCollapsedOffset() {
        super.calculateCollapsedOffset()
    }

    /**
     * @return 状态为 STATE_HALF_EXPANDED 时, getView().top 与屏幕顶部之间距离
     */
    fun getHalfExpandedOffset(): Int {
        return if (customHalfExpandedOffset != -1) customHalfExpandedOffset else this.halfExpandedOffset
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

    fun calculateExpandedOffset(expandedOffset: Int = Math.max(0, getParentHeight() - (getView()?.height ?: getParentHeight()))) {
        this.fitToContentsOffset = expandedOffset
        STLogUtil.w(TAG, "calculateExpandedOffset fitToContentsOffset=$fitToContentsOffset, expandedOffset=$expandedOffset, getView()?.height=${getView()?.height}, getParentHeight=${getParentHeight()}")
    }

    /**
     * @return expandedOffset 状态为 STATE_EXPANDED 时, getView().top 与屏幕顶部之间距离
     */
    public override fun getExpandedOffset(): Int {
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
    fun getBottomSheetViewHeightByState(state: Int): Int {
        val heightOnStateExpanded: Int = getParentHeight() - getExpandedOffset()
        val heightOnStateHalfExpanded: Int = getParentHeight() - getHalfExpandedOffset()
        val heightOnStateCollapsed: Int = getParentHeight() - getCollapsedOffset()

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
    @UiThread
    fun setBottomSheetViewHeight(height: Int) {
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
        if (dragEnabled) super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
    }

    override fun onStopNestedScroll(coordinatorLayout: CoordinatorLayout, child: V, target: View, type: Int) {
        if (dragEnabled) super.onStopNestedScroll(coordinatorLayout, child, target, type)
    }
    //endregion

    //region set state with animation end callback
    @JvmOverloads
    fun getCurrentBottomSheetViewHeight(bottomSheet: View? = getView()): Int {
        val panelHeight = getParentHeight() - (bottomSheet?.top ?: 0)
        STLogUtil.sync { STLogUtil.d(TAG, "getCurrentBottomSheetViewHeight panelHeight=$panelHeight, bottomSheet=$bottomSheet, getView()=${getView()}") }
        return panelHeight
    }

    /**
     * @param parentHeight 务必填写正确的值, 最好在 setOnParentHeightChangedListener 里面执行, 因为此时获取的 parentHeight 最精确
     * @param peekHeight 如果是第一次设置, 务必填写正确的值, 因为第一次 getPeekHeight 是 0/-1
     */
    fun setStateByRealHeight(parentHeight: Int, peekHeight: Int, realPanelContentHeight: Int, callbackBeforeSetState: ((newEnableHalfExpandedState: Boolean) -> Unit)? = null, callbackAfterSetState: ((newEnableHalfExpandedState: Boolean) -> Unit)? = null) {
        STLogUtil.w(TAG, "setStateByRealHeight start parentHeight=$parentHeight, peekHeight=$peekHeight, realPanelContentHeight=$realPanelContentHeight")
        val minExpandedOffset = (parentHeight * 0.24f).toInt()
        val minHalfExpandedOffset = (parentHeight * 0.5f).toInt()
        val peekOffset = parentHeight - peekHeight
        val realOffset = parentHeight - realPanelContentHeight
        val maxExpandedOffsetOnDisableHalf = peekOffset - 20.toPxFromDp() // 安全距离 20, 为避免正好多了几个像素这种极限情况
        val maxExpandedOffsetOnEnableHalf = minHalfExpandedOffset - 20.toPxFromDp()

        if (realOffset < minHalfExpandedOffset) {
            // 3.当面板高度大于等于中间态，不满扩展态，则扩展态自适应高度，仍支持三段式；
            enableHalfExpandedState = true

            val finalExpandedOffset = Math.max(Math.min(realOffset, maxExpandedOffsetOnEnableHalf), minExpandedOffset)
            callbackBeforeSetState?.invoke(enableHalfExpandedState)
            setStateWithResetConfigs(
                state = STBottomSheetBehavior.STATE_HALF_EXPANDED,
                enableAnimation = false,
                notifyOnStateChanged = true,
                forceSettlingOnSameState = true,
                parentHeight = parentHeight,
                expandedOffset = finalExpandedOffset,
                halfExpandedOffset = minHalfExpandedOffset,
                peekHeight = peekHeight
            ) {
                callbackAfterSetState?.invoke(enableHalfExpandedState)
                STLogUtil.w(TAG, "setStateByRealHeight callbackAfterSetState")
            }
        } else {
            // 1.不会存在不满收缩态
            // 2.当高于收缩态，低于中间态，则存在两段式（分别为中间态和收缩态），其中中间态自适应高度，箭头仍是横线，不支持上拉，支持下拉变为收缩态；
            enableHalfExpandedState = false

            val finalExpandedOffset = Math.min(maxExpandedOffsetOnDisableHalf, realOffset)
            callbackBeforeSetState?.invoke(enableHalfExpandedState)
            setStateWithResetConfigs(
                state = STBottomSheetBehavior.STATE_EXPANDED,
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
    */
    @UiThread
    fun setStateWithResetConfigs(@FinalState state: Int = currentFinalState, enableAnimation: Boolean = true, notifyOnStateChanged: Boolean = true, forceSettlingOnSameState: Boolean = false, parentHeight: Int, expandedOffset: Int = getExpandedOffset(), halfExpandedOffset: Int = getHalfExpandedOffset(), peekHeight: Int = this.peekHeight, onAnimationEndCallback: (() -> Unit)? = null) {
        STLogUtil.e(TAG, "setState start parentHeight=$parentHeight, currentFinalState=$currentFinalState, forceSettlingOnSameState=$forceSettlingOnSameState")
        if (parentHeight <= 0) {
            STLogUtil.e(TAG, "setState setParentHeight:$parentHeight failure, return")
            return
        }
        setParentHeight(parentHeight)

        this.calculateExpandedOffset(expandedOffset)
        this.setCustomHalfExpandedOffset(halfExpandedOffset)
        this.calculateCollapsedOffset()
        this.setPeekHeight(peekHeight, requestLayout = false)

        //region 第一次在 setStateWithCallback 之前设置, 否则绝对全屏显示且 2/3 段不起作用
        // 第二次即以后在 setStateWithCallback 之后设置, 避免切换 2/3 段时可能因为高度相差太大出现闪屏问题
        val finalState: Int = currentFinalState
        if (finalState == -1) {
            this.setBottomSheetViewHeight(getBottomSheetViewHeightByState(STATE_EXPANDED))
        }
        //endregion
        this.setStateWithCallback(state, enableAnimation, notifyOnStateChanged, forceSettlingOnSameState) {
            //region 动画结束后设置高度, 避免出现闪现问题
            if (finalState != -1) {
                val child = getView()
                val parent = child?.parent
                if (parent != null && parent.isLayoutRequested && ViewCompat.isAttachedToWindow(child)) {
                    child.post {
                        // must be call after calculateExpandedOffset
                        this.setBottomSheetViewHeight(getBottomSheetViewHeightByState(STATE_EXPANDED))
                    }
                } else {
                    // must be call after calculateExpandedOffset
                    this.setBottomSheetViewHeight(getBottomSheetViewHeightByState(STATE_EXPANDED))
                }
            }
            //endregion
            onAnimationEndCallback?.invoke()
        }
        STLogUtil.e(TAG, "setState end")
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
            if (finalState == STBottomSheetBehavior.STATE_COLLAPSED || finalState == STBottomSheetBehavior.STATE_EXPANDED || finalState == STBottomSheetBehavior.STATE_HALF_EXPANDED || hideable && finalState == STBottomSheetBehavior.STATE_HIDDEN) {
                this.state = finalState
                if (!(hideable && finalState == STBottomSheetBehavior.STATE_HIDDEN)) {
                    currentFinalState = finalState
                }
            }
            onAnimationEndCallback?.invoke()
            return
        }
        startSettlingAnimationWithCallback(finalState, enableAnimation, notifyOnStateChanged, onAnimationEndCallback)
    }

    /**
     * 强制触发 onStateChanged, 即使 state 相同
     */
    @UiThread
    private fun startSettlingAnimationWithCallback(@FinalState state: Int, enableAnimation: Boolean = true, notifyOnStateChanged: Boolean = true, onAnimationEndCallback: (() -> Unit)? = null) {
        val finalState = wrapStateForEnableHalfExpanded(state)

        val child: V? = getView()
        if (child == null) {
            currentFinalState = finalState
            onAnimationEndCallback?.invoke()
            return
        }
        val parent = child.parent
        if (parent != null && parent.isLayoutRequested && ViewCompat.isAttachedToWindow(child)) {
            child.post { startSettlingAnimationWithCallback(child, finalState, enableAnimation, notifyOnStateChanged, onAnimationEndCallback) }
        } else {
            startSettlingAnimationWithCallback(child, finalState, enableAnimation, notifyOnStateChanged, onAnimationEndCallback)
        }
    }

    private fun startSettlingAnimationWithCallback(child: View, @FinalState state: Int, enableAnimation: Boolean = true, notifyOnStateChanged: Boolean = true, onAnimationEndCallback: (() -> Unit)? = null) {
        val finalState = wrapStateForEnableHalfExpanded(state)
        STLogUtil.w(TAG, "startSettlingAnimationWithCallback state=${getStateDescription(state)}, child=$child, finalState=${getStateDescription(finalState)}")
        var tmpState = finalState
        var top: Int
        if (tmpState == STBottomSheetBehavior.STATE_COLLAPSED) {
            top = collapsedOffset
        } else if (tmpState == STBottomSheetBehavior.STATE_HALF_EXPANDED) {
            top = halfExpandedOffset
            if (fitToContents && top <= fitToContentsOffset) {
                tmpState = STBottomSheetBehavior.STATE_EXPANDED
                top = fitToContentsOffset
            }
        } else if (tmpState == STBottomSheetBehavior.STATE_EXPANDED) {
            top = this.expandedOffset
        } else {
            require(!(!hideable || tmpState != 5)) { "Illegal state argument: $tmpState" }
            top = parentHeight
        }

        if (!enableAnimation) {
            val finalTop: Int = top
            val dy: Int = finalTop - child.top
            ViewCompat.offsetTopAndBottom(child, dy)
            setStateInternalWithCallback(tmpState, notifyOnStateChanged = notifyOnStateChanged, onAnimationEndCallback = onAnimationEndCallback)
        } else {
            if (viewDragHelper.smoothSlideViewTo(child, child.left, top)) {
                setStateInternal(STBottomSheetBehavior.STATE_SETTLING)
                ViewCompat.postOnAnimation(child, SettleRunnableWithCallback(viewDragHelper, child, tmpState) { targetState ->
                    setStateInternalWithCallback(targetState, notifyOnStateChanged = notifyOnStateChanged, onAnimationEndCallback = onAnimationEndCallback)
                })
            } else {
                setStateInternalWithCallback(tmpState, notifyOnStateChanged = notifyOnStateChanged, onAnimationEndCallback = onAnimationEndCallback)
            }
        }
    }

    override fun setStateInternal(state: Int) {
        super.setStateInternal(wrapStateForEnableHalfExpanded(state))
    }

    private fun setStateInternalWithCallback(state: Int, notifyOnStateChanged: Boolean = true, onAnimationEndCallback: (() -> Unit)? = null) {
        val finalState = wrapStateForEnableHalfExpanded(state)
        STLogUtil.w(TAG, "setStateInternalWithCallback state=${getStateDescription(state)}, finalState=$finalState")

        if (this.state == finalState) {
            onAnimationEndCallback?.invoke()
            return
        }
        this.state = finalState
        if (finalState == STBottomSheetBehavior.STATE_COLLAPSED || finalState == STBottomSheetBehavior.STATE_HALF_EXPANDED || finalState == STBottomSheetBehavior.STATE_EXPANDED) {
            currentFinalState = finalState
        }

        if (finalState == STBottomSheetBehavior.STATE_HALF_EXPANDED || finalState == STBottomSheetBehavior.STATE_EXPANDED) {
            updateImportantForAccessibility(true)
        } else if (finalState == STBottomSheetBehavior.STATE_HIDDEN || finalState == STBottomSheetBehavior.STATE_COLLAPSED) {
            updateImportantForAccessibility(false)
        }

        onAnimationEndCallback?.invoke()

        if (notifyOnStateChanged) {
            val bottomSheet: View? = viewRef.get()
            if (bottomSheet != null && callback != null) {
                callback.onStateChanged(bottomSheet, finalState)
            }
        }
    }

    /**
     * @see enableHalfExpandedState
     */
    private fun wrapStateForEnableHalfExpanded(state: Int): Int = if (!this.enableHalfExpandedState && state == STATE_HALF_EXPANDED) STATE_COLLAPSED else state
    fun setPeekHeight(peekHeight: Int, requestLayout: Boolean) {
        var layout = false
        if (peekHeight == -1) {
            if (!peekHeightAuto) {
                peekHeightAuto = true
                layout = true
            }
        } else if (peekHeightAuto || this.peekHeight != peekHeight) {
            peekHeightAuto = false
            this.peekHeight = Math.max(0, peekHeight)
            collapsedOffset = parentHeight - peekHeight
            layout = true
        }

        if (requestLayout && layout && state == STBottomSheetBehavior.STATE_COLLAPSED && viewRef != null) {
            val view = viewRef.get()
            view?.requestLayout()
        }
    }

    private class SettleRunnableWithCallback(val viewDragHelper: ViewDragHelper?, val view: View, val targetState: Int, val onAnimationEndCallback: (targetState: Int) -> Unit) : Runnable {
        override fun run() {
            STLogUtil.w(TAG, "---- SettleRunnableWithCallback#run targetState=${getStateDescription(targetState)}")
            if (viewDragHelper != null && viewDragHelper.continueSettling(true)) {
                ViewCompat.postOnAnimation(view, this)
            } else {
                onAnimationEndCallback(targetState)
            }
        }
    }
    //endregion

    @IntDef(STATE_EXPANDED, STATE_HALF_EXPANDED, STATE_COLLAPSED)
    annotation class FinalState

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
         * A utility function to get the [STBottomSheetViewPagerBehavior] associated with the `view`.
         *
         * @param view The [View] with [STBottomSheetViewPagerBehavior].
         * @return The [STBottomSheetViewPagerBehavior] associated with the `view`.
         */
        @Suppress("UNCHECKED_CAST")
        fun <V : View> from(view: V): STBottomSheetViewPagerBehavior<V> {
            val params: ViewGroup.LayoutParams = view.layoutParams
            require(params is CoordinatorLayout.LayoutParams) { "The view is not a child of CoordinatorLayout" }
            val behavior: CoordinatorLayout.Behavior<*>? = params.behavior
            require(behavior is STBottomSheetViewPagerBehavior<*>) { "The view is not associated with STBottomSheetViewPagerBehavior" }
            return behavior as STBottomSheetViewPagerBehavior<V>
        }
    }
}