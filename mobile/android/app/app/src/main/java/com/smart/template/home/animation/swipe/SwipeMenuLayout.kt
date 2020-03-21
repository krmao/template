package com.smart.template.home.animation.swipe

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PointF
import android.util.AttributeSet
import android.view.*
import com.smart.library.util.STLogUtil
import com.smart.library.util.animation.STInterpolatorFactory
import com.smart.template.R
import java.lang.ref.WeakReference
import kotlin.math.abs

/**
 * 侧滑菜单
 * https://github.com/mcxtzhang/SwipeDelMenuLayout/blob/master/README-cn.md
 */
@Suppress("unused", "MemberVisibilityCanBePrivate")
class SwipeMenuLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ViewGroup(context, attrs, defStyleAttr) {

    var enableSwipe = true                      // 是否开启滑动删除功能
    var enableBlockingMode = true               // 是否开启阻塞模式
    var isRightToLeft = true                    // true 从右向左滑出菜单, false 从左向右滑出菜单
    var isExpanded = false                      // 当前是否是展开
        private set

    private val scaleTouchSlop by lazy { ViewConfiguration.get(context).scaledTouchSlop }
    private val maxVelocity by lazy { ViewConfiguration.get(context).scaledMaximumFlingVelocity }

    private var expandAnimation: ValueAnimator? = null
    private var collapsedAnimation: ValueAnimator? = null

    private var pointerId = 0                   // 第一个触摸的手指的 pointerId 值
    private var menuWidth = 0                   // 菜单宽度(最大滑动距离)
    private var maxHeight = 0                   // 根据 childView 的最大高度计算出自己的高度
    private var isClickEvent = true             // 如果是点击事件，关闭菜单
    private var blockingFlag = false            // 是否拦截事件
    private val lastPoint = PointF()            // 上一个 MotionEvent
    private val firstPoint = PointF()           // 第一个 MotionEvent
    private var slideThresholdValue = 0         // 滑动阀值(右侧菜单宽度的40%) 手指抬起时, 超过了展开，否则收起菜单
    private var isFingerTouching = false        // 是否有手指正在触摸
    private var contentView: View? = null       // 存储contentView(第一个View)
    private var forceInterceptTouchEvent = false            // 强制拦截事件, 自己消费
    private var velocityTracker: VelocityTracker? = null    // 计算滑动速率

    init {
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.SwipeMenuLayout, defStyleAttr, 0)
        for (index in 0 until typedArray.indexCount) {
            when (val attr = typedArray.getIndex(index)) {
                R.styleable.SwipeMenuLayout_enableSwipe -> {
                    enableSwipe = typedArray.getBoolean(attr, true)
                }
                R.styleable.SwipeMenuLayout_enableBlockingMode -> {
                    enableBlockingMode = typedArray.getBoolean(attr, true)
                }
                R.styleable.SwipeMenuLayout_isRightToLeft -> {
                    isRightToLeft = typedArray.getBoolean(attr, true)
                }
            }
        }
        typedArray.recycle()

        isClickable = true // 设置可点击, 获取触摸事件
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        menuWidth = 0 // 由于ViewHolder的复用机制，每次这里要手动恢复初始值
        maxHeight = 0
        var contentWidth = 0 // 适配GridLayoutManager，将以第一个子Item(即ContentItem)的宽度为控件宽度
        // 为了子View的高，可以matchParent(参考的FrameLayout 和LinearLayout的Horizontal)
        val measureMatchParentChildren = MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.EXACTLY
        var needMeasureChildHeight = false
        for (index in 0 until childCount) {
            val childView = getChildAt(index)
            // 令每一个子View可点击，从而获取触摸事件
            childView.isClickable = true
            if (childView.visibility != View.GONE) {
                // 后续计划加入上滑、下滑，则将不再支持Item的margin
                measureChild(childView, widthMeasureSpec, heightMeasureSpec)
                val childLayoutParams = childView.layoutParams as MarginLayoutParams
                maxHeight = maxHeight.coerceAtLeast(childView.measuredHeight)
                if (measureMatchParentChildren && childLayoutParams.height == LayoutParams.MATCH_PARENT) {
                    needMeasureChildHeight = true
                }
                if (index > 0) { // 第一个布局是Left item，从第二个开始才是RightMenu
                    menuWidth += childView.measuredWidth
                } else {
                    contentView = childView
                    contentWidth = childView.measuredWidth
                }
            }
        }
        setMeasuredDimension(paddingLeft + paddingRight + contentWidth, maxHeight + paddingTop + paddingBottom) //宽度取第一个Item(Content)的宽度
        slideThresholdValue = menuWidth * 4 / 10 //滑动判断的临界值
        // 如果子View的height有MatchParent属性的，设置子View高度
        if (needMeasureChildHeight) {
            forceUniformHeight(childCount, widthMeasureSpec)
        }
    }

    override fun generateLayoutParams(attrs: AttributeSet): LayoutParams = MarginLayoutParams(context, attrs)

    /**
     * 给 match_parent 的子 View 设置高度
     */
    private fun forceUniformHeight(count: Int, widthMeasureSpec: Int) {
        // Pretend that the linear layout has an exact size. This is the measured height of
        // ourselves. The measured height should be the max height of the children, changed
        // to accommodate the heightMeasureSpec from the parent
        val uniformMeasureSpec = MeasureSpec.makeMeasureSpec(
            measuredHeight,
            MeasureSpec.EXACTLY
        ) //以父布局高度构建一个Exactly的测量参数
        for (i in 0 until count) {
            val child = getChildAt(i)
            if (child.visibility != View.GONE) {
                val lp = child.layoutParams as MarginLayoutParams
                if (lp.height == LayoutParams.MATCH_PARENT) {
                    // Temporarily force children to reuse their old measured width
                    // FIXME: this may not be right for something like wrapping text?
                    val oldWidth = lp.width //measureChildWithMargins 这个函数会用到宽，所以要保存一下
                    lp.width = child.measuredWidth
                    // Remeasure with new dimensions
                    measureChildWithMargins(child, widthMeasureSpec, 0, uniformMeasureSpec, 0)
                    lp.width = oldWidth
                }
            }
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val childCount = childCount
        var left = 0 + paddingLeft
        var right = 0 + paddingLeft
        for (i in 0 until childCount) {
            val childView = getChildAt(i)
            if (childView.visibility != View.GONE) {
                if (i == 0) { //第一个子View是内容 宽度设置为全屏
                    childView.layout(left, paddingTop, left + childView.measuredWidth, paddingTop + childView.measuredHeight)
                    left += childView.measuredWidth
                } else {
                    if (isRightToLeft) {
                        childView.layout(left, paddingTop, left + childView.measuredWidth, paddingTop + childView.measuredHeight)
                        left += childView.measuredWidth
                    } else {
                        childView.layout(right - childView.measuredWidth, paddingTop, right, paddingTop + childView.measuredHeight)
                        right -= childView.measuredWidth
                    }
                }
            }
        }
    }

    private fun getActionName(ev: MotionEvent?): String = when (ev?.action) {
        MotionEvent.ACTION_DOWN -> "ACTION_DOWN"
        MotionEvent.ACTION_MOVE -> "ACTION_MOVE"
        MotionEvent.ACTION_UP -> "ACTION_UP"
        MotionEvent.ACTION_CANCEL -> "ACTION_CANCEL"
        else -> "ACTION_KNOWN(${ev?.action})"
    }

    /**
     * 分发操作
     *
     * viewGroup 级别的 dispatchTouchEvent 才会执行 分发操作
     * view 级别的 dispatchTouchEvent 并不执行 分发操作, 只会调用自己的 onTouchEvent
     *
     * @return false 后续的 ACTION_MOVE/ACTION_UP 不会再触发
     * @return true 后续的 ACTION_MOVE/ACTION_UP 都会逐层(可被拦截)传递到这里
     *
     * https://www.jianshu.com/p/35a8309b9597
     */
    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (enableSwipe) {
            acquireVelocityTracker(event)
            val verTracker = velocityTracker
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    forceInterceptTouchEvent = false // 判断手指起始落点，如果距离属于滑动了，就屏蔽一切点击事件
                    isClickEvent = true // 仿QQ，侧滑菜单展开时，点击内容区域，关闭侧滑菜单
                    blockingFlag = false // 每次DOWN时，默认是不拦截的
                    if (isFingerTouching) { // 如果有别的指头摸过了，那么就return false。这样后续的move..等事件也不会再来找这个View了。
                        STLogUtil.e(TAG, "dispatchTouchEvent ${getActionName(event)} return false 忽略多指滑动")
                        return false
                    } else {
                        isFingerTouching = true // 第一个摸的指头，赶紧改变标志，宣誓主权。
                    }

                    lastPoint[event.rawX] = event.rawY
                    firstPoint[event.rawX] = event.rawY // 判断手指起始落点，如果距离属于滑动了，就屏蔽一切点击事件。

                    // 如果down，view 和 cacheView 不一样，则立马让它还原。且把它置为null
                    if (lastExpandSwipeMenuLayout != null) {
                        if (lastExpandSwipeMenuLayout?.get() !== this) {
                            lastExpandSwipeMenuLayout?.get()?.smoothClose()
                            blockingFlag = enableBlockingMode // IOS模式开启的话，且当前有侧滑菜单的View，且不是自己的，就该拦截事件咯。
                        }
                        // 只要有一个侧滑菜单处于打开状态， 就不给外层布局上下滑动了
                        parent.requestDisallowInterceptTouchEvent(true)
                    }
                    // 求第一个触点的id， 此时可能有多个触点，但至少一个，计算滑动速率用
                    pointerId = event.getPointerId(0)
                }
                MotionEvent.ACTION_MOVE -> {
                    // IOS模式开启的话，且当前有侧滑菜单的View，且不是自己的，就该拦截事件咯。滑动也不该出现
                    if (!blockingFlag) {
                        val offsetX = lastPoint.x - event.rawX
                        // 为了在水平滑动中禁止父类ListView等再竖直滑动
                        if (abs(offsetX) > scaleTouchSlop || abs(scrollX) > scaleTouchSlop) { // 修改此处，使屏蔽父布局滑动更加灵敏，
                            parent.requestDisallowInterceptTouchEvent(true)
                        }
                        // 仿QQ，侧滑菜单展开时，点击内容区域，关闭侧滑菜单。begin
                        if (abs(offsetX) > scaleTouchSlop) {
                            isClickEvent = false
                        }
                        // 仿QQ，侧滑菜单展开时，点击内容区域，关闭侧滑菜单。end
                        scrollBy(offsetX.toInt(), 0) //滑动使用scrollBy
                        // 越界修正
                        if (isRightToLeft) { // scrollX 范围为 [0, rightMenuWidth]
                            // 修正位置
                            if (scrollX < 0) scrollTo(0, 0)
                            // 修正位置
                            if (scrollX > menuWidth) scrollTo(menuWidth, 0)
                        } else { // scrollX 范围为 [-rightMenuWidth, 0]
                            // 修正位置
                            if (scrollX < -menuWidth) scrollTo(-menuWidth, 0)
                            // 修正位置
                            if (scrollX > 0) scrollTo(0, 0)
                        }
                        lastPoint[event.rawX] = event.rawY
                    }
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    // 判断手指起始落点，如果距离属于滑动了，就屏蔽一切点击事件。
                    if (abs(event.rawX - firstPoint.x) > scaleTouchSlop) {
                        forceInterceptTouchEvent = true
                    }

                    // IOS模式开启的话，且当前有侧滑菜单的View，且不是自己的，就该拦截事件咯。滑动也不该出现
                    if (!blockingFlag) { // 且滑动了 才判断是否要收起、展开menu
                        if (verTracker != null) {
                            // 求伪瞬时速度
                            verTracker.computeCurrentVelocity(1000, maxVelocity.toFloat())
                            val velocityX = verTracker.getXVelocity(pointerId)
                            if (abs(velocityX) > 1000) { // 滑动速度超过阈值
                                if (velocityX < -1000) {
                                    if (isRightToLeft) { // 左滑
                                        //平滑展开Menu
                                        smoothExpand()
                                    } else {
                                        //平滑关闭Menu
                                        smoothClose()
                                    }
                                } else {
                                    if (isRightToLeft) { //左滑
                                        // 平滑关闭Menu
                                        smoothClose()
                                    } else {
                                        //平滑展开Menu
                                        smoothExpand()
                                    }
                                }
                            } else {
                                if (abs(scrollX) > slideThresholdValue) { //否则就判断滑动距离
                                    //平滑展开Menu
                                    smoothExpand()
                                } else {
                                    // 平滑关闭Menu
                                    smoothClose()
                                }
                            }
                        }
                    }
                    releaseVelocityTracker()
                    isFingerTouching = false // 没有手指在摸我了
                }
            }
        }
        val result: Boolean = super.dispatchTouchEvent(event)
        STLogUtil.w(TAG, "dispatchTouchEvent ${getActionName(event)} return $result canScrollRightToLeft()=${canScrollRightToLeft()}, canScrollLeftToRight()=${canScrollLeftToRight()}")
        return result
    }

    /**
     * 拦截操作
     *
     * @return true 拦截, 不再向 子 view/viewGroup 的 dispatchTouchEvent 传递, 执行当前 onTouchEvent
     * @return false 不拦截, 向 子 view/viewGroup 的 dispatchTouchEvent 传递
     */
    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        // 禁止侧滑时，点击事件不受干扰。
        if (enableSwipe) {
            when (event.action) {
                MotionEvent.ACTION_MOVE ->                     // 屏蔽滑动时的事件
                    if (abs(event.rawX - firstPoint.x) > scaleTouchSlop) {
                        STLogUtil.e(TAG, "onInterceptTouchEvent ${getActionName(event)} return true 检测到移动 canScrollRightToLeft()=${canScrollRightToLeft()}, canScrollLeftToRight()=${canScrollLeftToRight()}")
                        return true
                    }
                MotionEvent.ACTION_UP -> {
                    // 为了在侧滑时，屏蔽子View的点击事件
                    if (isRightToLeft) {
                        if (scrollX > scaleTouchSlop) {
                            // 解决一个智障问题~ 居然不给点击侧滑菜单 我跪着谢罪
                            // 这里判断落点在内容区域屏蔽点击，内容区域外，允许传递事件继续向下的的。。。
                            if (event.x < width - scrollX) {
                                // 仿QQ，侧滑菜单展开时，点击内容区域，关闭侧滑菜单。
                                if (isClickEvent) {
                                    smoothClose()
                                }
                                STLogUtil.e(TAG, "onInterceptTouchEvent ${getActionName(event)} return true 点击范围在菜单外 屏蔽 canScrollRightToLeft()=${canScrollRightToLeft()}, canScrollLeftToRight()=${canScrollLeftToRight()}")
                                return true // true表示拦截
                            }
                        }
                    } else {
                        if (-scrollX > scaleTouchSlop) {
                            if (event.x > -scrollX) { // 点击范围在菜单外 屏蔽
                                // 仿QQ，侧滑菜单展开时，点击内容区域，关闭侧滑菜单。
                                if (isClickEvent) {
                                    smoothClose()
                                }
                                STLogUtil.e(TAG, "onInterceptTouchEvent ${getActionName(event)} return true 点击范围在菜单外 屏蔽 canScrollRightToLeft()=${canScrollRightToLeft()}, canScrollLeftToRight()=${canScrollLeftToRight()}")
                                return true
                            }
                        }
                    }
                    // 判断手指起始落点，如果距离属于滑动了，就屏蔽一切点击事件。
                    if (forceInterceptTouchEvent) {
                        STLogUtil.e(TAG, "onInterceptTouchEvent ${getActionName(event)} return true isUserSwiped=true 距离属于滑动, 屏蔽一切点击事件 canScrollRightToLeft()=${canScrollRightToLeft()}, canScrollLeftToRight()=${canScrollLeftToRight()}")
                        return true
                    }
                }
            }
            //模仿IOS 点击其他区域关闭：
            if (blockingFlag) {
                // IOS模式开启，且当前有菜单的View，且不是自己的 拦截点击事件给子View
                STLogUtil.e(TAG, "onInterceptTouchEvent ${getActionName(event)} return true iosInterceptFlag=true, IOS模式开启 canScrollRightToLeft()=${canScrollRightToLeft()}, canScrollLeftToRight()=${canScrollLeftToRight()}")
                return true
            }
        }
        val result: Boolean = super.onInterceptTouchEvent(event)
        STLogUtil.w(TAG, "onInterceptTouchEvent ${getActionName(event)} return $result canScrollRightToLeft()=${canScrollRightToLeft()}, canScrollLeftToRight()=${canScrollLeftToRight()}")
        return result
    }

    /**
     * 消费触摸事件
     *
     * 如果 ACTION_MOVE 传递到了这里, 而返回值是 false, 则后续的 ACTION_UP 不会传递到这里了
     *
     * @return true 消费, 后续的 ACTION_MOVE/ACTION_UP 都会逐层(可被拦截)传递到这里
     * @return false 未消费, 则会将 ACTION_DOWN 传递给父 ViewGroup 的 onTouchEvent 进行处理, 直到由哪一层 ViewGroup 消费了 ACTION_DOWN 事件为止,
     */
    @SuppressLint("ClickableViewAccessibility")
    @Suppress("RedundantOverride")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val result: Boolean = super.onTouchEvent(event)
        STLogUtil.d(TAG, "onTouchEvent ${getActionName(event)} return $result canScrollRightToLeft()=${canScrollRightToLeft()}, canScrollLeftToRight()=${canScrollLeftToRight()}")
        return result
    }

    fun smoothExpand() {
        lastExpandSwipeMenuLayout = WeakReference(this) // 展开就加入ViewCache
        val toX = if (isRightToLeft) menuWidth else -menuWidth
        if (scrollX != toX) {

            contentView?.isLongClickable = false // 侧滑菜单展开，屏蔽content长按
            cancelAnimation()
            expandAnimation = ValueAnimator.ofInt(scrollX, toX).apply {
                duration = 300
                interpolator = STInterpolatorFactory.createDecelerateInterpolator()
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        isExpanded = true
                    }
                })
                addUpdateListener { animation -> scrollTo((animation.animatedValue as Int), 0) }
            }
            expandAnimation?.start()
        }
    }

    private fun cancelAnimation() {
        if (collapsedAnimation?.isRunning == true) collapsedAnimation?.cancel()
        if (expandAnimation?.isRunning == true) expandAnimation?.cancel()
    }

    fun canScrollRightToLeft(): Boolean {
        if (enableSwipe) {
            if (isRightToLeft) {
                if (scrollX < menuWidth) {
                    return true
                }
            } else {
                if (scrollX < 0) {
                    return true
                }
            }
        }
        return false
    }

    fun canScrollLeftToRight(): Boolean {
        if (enableSwipe) {
            if (isRightToLeft) {
                if (scrollX > 0) {
                    return true
                }
            } else {
                if (scrollX > -menuWidth) {
                    return true
                }
            }
        }
        return false
    }

    fun smoothClose() {
        if (lastExpandSwipeMenuLayout?.get() === this) lastExpandSwipeMenuLayout = null

        if (scrollX != 0) {
            contentView?.isLongClickable = true // 侧滑菜单展开，屏蔽content长按
            cancelAnimation()
            collapsedAnimation = ValueAnimator.ofInt(scrollX, 0).apply {
                interpolator = STInterpolatorFactory.createDecelerateInterpolator()
                duration = 300
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        isExpanded = false
                    }
                })
                addUpdateListener { animation -> scrollTo((animation.animatedValue as Int), 0) }
            }
            collapsedAnimation?.start()
        }
    }

    /**
     * @param event 向VelocityTracker添加MotionEvent
     */
    private fun acquireVelocityTracker(event: MotionEvent) {
        if (null == velocityTracker) {
            velocityTracker = VelocityTracker.obtain()
        }
        velocityTracker?.addMovement(event)
    }

    private fun releaseVelocityTracker() {
        velocityTracker?.clear()
        velocityTracker?.recycle()
        velocityTracker = null
    }

    override fun onDetachedFromWindow() {
        smoothClose()
        if (this === lastExpandSwipeMenuLayout?.get()) lastExpandSwipeMenuLayout = null
        super.onDetachedFromWindow()
    }

    // 展开时，禁止长按
    override fun performLongClick(): Boolean = if (abs(scrollX) > scaleTouchSlop) false else super.performLongClick()

    /**
     * 快速关闭。
     * 用于 点击侧滑菜单上的选项,同时想让它快速关闭(删除 置顶)。
     * 这个方法在ListView里是必须调用的，
     * 在RecyclerView里，视情况而定，如果是mAdapter.notifyItemRemoved(pos)方法不用调用。
     */
    fun quickClose() {
        if (this === lastExpandSwipeMenuLayout?.get()) {
            cancelAnimation()
            lastExpandSwipeMenuLayout?.get()?.scrollTo(0, 0)
            lastExpandSwipeMenuLayout = null
        }
    }

    companion object {
        private const val TAG = "[SwipeMenuLayout]"
        var lastExpandSwipeMenuLayout: WeakReference<SwipeMenuLayout>? = null
            private set
    }
}