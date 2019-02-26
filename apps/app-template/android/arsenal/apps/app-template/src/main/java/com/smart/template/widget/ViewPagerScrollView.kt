package com.smart.template.widget

import android.annotation.SuppressLint
import android.content.Context
import android.support.v4.view.ViewCompat
import android.support.v4.view.ViewPager
import android.support.v4.widget.ViewDragHelper
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import com.smart.library.util.CXLogUtil
import com.smart.library.util.CXSystemUtil
import com.smart.template.R

@Suppress("unused", "PrivatePropertyName", "MemberVisibilityCanBePrivate")
class ViewPagerScrollView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

    private val tag: String = ViewPagerScrollView::class.java.name
    private var thresholdDistance = CXSystemUtil.screenHeight / 5
    val viewPager: ViewPager by lazy { findViewById<ViewPager>(R.id.viewPager) }

    private val NORMAL_HEIGHT = CXSystemUtil.getPxFromDp(200f).toInt()
    private val MAX_HEIGHT = CXSystemUtil.screenHeight - CXSystemUtil.getPxFromDp(50f).toInt()
    private val NORMAL_MARGIN_TOP = CXSystemUtil.screenHeight - NORMAL_HEIGHT

    private val MAX_TOP = 1000
    private val MAX_TOP_BOTTOM = 200

    var atMaxStatusNow = false
        set(value) {
            field = value
            requestDisallowInterceptTouchEvent(field)
        }
    var atMidStatusNow = false
    var atNormalStatusNow = true

    init {
        LayoutInflater.from(context).inflate(R.layout.home_scroll_view_pager, this)
        viewPager.pageMargin = CXSystemUtil.getPxFromDp(30f).toInt()
        viewPager.offscreenPageLimit = 10
        viewPager.adapter = ViewPagerAdapter(context, this)
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {
            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
            }

            override fun onPageSelected(p0: Int) {

            }
        })
        viewPager.currentItem = 1

        atMaxStatusNow = false
    }

    fun getCurrentRecyclerView(): CXRecyclerView {
        return viewPager.findViewWithTag(viewPager.currentItem)
    }

    fun canCurrentRecyclerViewPullDown(): Boolean {
        return getCurrentRecyclerView().canScrollVertically(-1)
    }

    fun canCurrentRecyclerViewPullUp(): Boolean {
        return getCurrentRecyclerView().canScrollVertically(1)
    }

    private var lastDownX: Float = 0.toFloat()
    private var lastDownY: Float = 0.toFloat()

    private var globalTouchEvent: MotionEvent? = null
    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
        globalTouchEvent = event

        if (event != null) {
            val deltaX: Float
            val deltaY: Float
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    lastDownX = event.x
                    lastDownY = event.y
                    dragHelper.processTouchEvent(event)
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                }
                MotionEvent.ACTION_MOVE -> {
                    deltaY = event.y - lastDownY
                    deltaX = event.x - lastDownX
                    val yScroll = processIntercept(deltaX, deltaY)
                    val shouldIntercept = dragHelper.shouldInterceptTouchEvent(event)
                    CXLogUtil.e(tag, "yScroll=$yScroll, shouldIntercept=$shouldIntercept")
                    val intercept = yScroll && shouldIntercept
                    CXLogUtil.d(tag, "onInterceptTouchEvent, intercept=$intercept")
                    return intercept
                }
            }
        }
        return super.onInterceptTouchEvent(event)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null) {
            try {
                dragHelper.processTouchEvent(event)
            } catch (e: Exception) {
            }
            return true
        }
        return super.onInterceptTouchEvent(event)
    }

    private fun processIntercept(distanceX: Float, distanceY: Float): Boolean {

        val canCurrentRecyclerViewPullDown = canCurrentRecyclerViewPullDown()
        val canCurrentRecyclerViewPullUp = canCurrentRecyclerViewPullUp()

        val isVerticalScrollEvent = Math.abs(distanceY) > Math.abs(distanceX)

        val shouldInterceptXScroll = !atNormalStatusNow && !isVerticalScrollEvent     // 如果是标准状态， 可以横向滚动

        val canScrollUpToDown = (distanceY > 0 && (atMaxStatusNow && !canCurrentRecyclerViewPullDown || !atMaxStatusNow))
        val canScrollDownToUp = (distanceY < 0 && (atMaxStatusNow && !canCurrentRecyclerViewPullDown || !atMaxStatusNow))

        val shouldInterceptYScroll = isVerticalScrollEvent && (canScrollDownToUp || canScrollUpToDown)

        CXLogUtil.w(tag, "onScroll, atMaxStatusNow=$atMaxStatusNow, atNormalStatusNow=$atNormalStatusNow")
        CXLogUtil.w(tag, "onScroll, canScrollUpToDown=$canScrollUpToDown, canScrollDownToUp=$canScrollDownToUp")
        CXLogUtil.w(tag, "onScroll, isVerticalScrollEvent=$isVerticalScrollEvent, shouldInterceptXScroll=$shouldInterceptXScroll, shouldInterceptYScroll=$shouldInterceptYScroll")
        CXLogUtil.w(tag, "onScroll, distanceY=$distanceY, distanceX=$distanceX,   canCurrentRecyclerViewPullDown=$canCurrentRecyclerViewPullDown, canCurrentRecyclerViewPullUp=$canCurrentRecyclerViewPullUp")
        CXLogUtil.w(tag, "onScroll, canCurrentRecyclerViewPullDown=$canCurrentRecyclerViewPullDown, canCurrentRecyclerViewPullUp=$canCurrentRecyclerViewPullUp")
        return shouldInterceptXScroll || shouldInterceptYScroll
    }

    private var lastTop = 0
    val dragHelper: ViewDragHelper by lazy {
        ViewDragHelper.create(this, 1.0f, object : ViewDragHelper.Callback() {

            override fun tryCaptureView(child: View, pointerId: Int): Boolean {
                CXLogUtil.d(tag, "tryCaptureView, pointerId=$pointerId child=$child child==viewpager?${child == viewPager}")
                return true
            }

            override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
                return 0
            }

            override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
                CXLogUtil.d(tag, "transformValue clampViewPositionVertical, top=$top, dy=$dy")
                var finalTop = lastTop
                if (top < 0) {
                    if (Math.abs(top) < MAX_TOP) {
                        if (canCurrentRecyclerViewPullDown()) {
                            try {
                                getCurrentRecyclerView().dispatchTouchEvent(MotionEvent.obtain(globalTouchEvent))
                            } catch (e: Exception) {

                            }
                        } else {
                            finalTop = top
                            atMaxStatusNow = false
                            atNormalStatusNow = false
                            atMidStatusNow = true
                            lastTop = finalTop
                        }
                    } else {
                        atMaxStatusNow = true
                        atNormalStatusNow = false
                        atMidStatusNow = false

                        try {
                            getCurrentRecyclerView().dispatchTouchEvent(MotionEvent.obtain(globalTouchEvent))
                        } catch (e: Exception) {

                        }
                    }
                } else if (top > 0) {
                    if (top > MAX_TOP_BOTTOM) {
                        if (canCurrentRecyclerViewPullUp()) {
                            try {
                                getCurrentRecyclerView().dispatchTouchEvent(MotionEvent.obtain(globalTouchEvent))
                            } catch (e: Exception) {

                            }
                        } else {

                            finalTop = top
                            atMaxStatusNow = false
                            atNormalStatusNow = false
                            atMidStatusNow = true
                            lastTop = finalTop
                        }

                    } else {
                        atMaxStatusNow = false
                        atNormalStatusNow = true
                        atMidStatusNow = false

                        try {
                            getCurrentRecyclerView().dispatchTouchEvent(MotionEvent.obtain(globalTouchEvent))
                        } catch (e: Exception) {

                        }
                    }
                }
                return finalTop // transformValue(dy.toFloat(), top.toFloat(), measuredHeight.toFloat()).toInt()
            }

            override fun onViewPositionChanged(changedView: View, left: Int, top: Int, dx: Int, dy: Int) {
                CXLogUtil.d(tag, "onViewPositionChanged, top=$top, dy=$dy")
            }

            override fun onEdgeDragStarted(edgeFlags: Int, pointerId: Int) {
                CXLogUtil.d(tag, "onEdgeDragStarted, edgeFlags=$edgeFlags, pointerId=$pointerId")
            }

            override fun onEdgeTouched(edgeFlags: Int, pointerId: Int) {
                CXLogUtil.d(tag, "onEdgeTouched, edgeFlags=$edgeFlags, pointerId=$pointerId")
            }

            override fun onEdgeLock(edgeFlags: Int): Boolean {
                CXLogUtil.d(tag, "onEdgeLock, edgeFlags=$edgeFlags")
                return super.onEdgeLock(edgeFlags)
            }

            override fun onViewDragStateChanged(state: Int) {
                var stateDesc = "NONE"
                when (state) {
                    ViewDragHelper.STATE_DRAGGING -> stateDesc = "DRAGGING"
                    ViewDragHelper.STATE_IDLE -> {
                        stateDesc = "IDLE"
                    }
                    ViewDragHelper.STATE_SETTLING -> stateDesc = "SETTLING"
                }
                CXLogUtil.d(tag, "onViewDragStateChanged, state=$stateDesc")
            }

            override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
                CXLogUtil.d(tag, "onViewReleased, yvel=$yvel")
            }
        })
    }

    fun smoothScroll(releasedChild: View, top: Int) {
        if (dragHelper.smoothSlideViewTo(releasedChild, 0, top)) ViewCompat.postInvalidateOnAnimation(this)
    }

    override fun computeScroll() {
        CXLogUtil.d(tag, "computeScroll")
        if (dragHelper.continueSettling(true)) {
            CXLogUtil.d(tag, "computeScroll fling ...")
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

}