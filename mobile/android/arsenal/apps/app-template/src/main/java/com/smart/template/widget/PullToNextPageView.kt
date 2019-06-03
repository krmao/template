package com.smart.template.widget

import android.annotation.SuppressLint
import android.content.Context
import android.support.v4.view.GestureDetectorCompat
import android.support.v4.view.ViewCompat
import android.support.v4.widget.ViewDragHelper
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import com.smart.library.util.STLogUtil
import com.smart.library.util.STSystemUtil

class PullToNextPageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ViewGroup(context, attrs, defStyleAttr) {

    private val tag: String = PullToNextPageView::class.java.name
    private var thresholdDistance = STSystemUtil.screenHeight / 5
    private var pageViewList = mutableListOf<View>()
    var pageIndex = 1
        private set
    private var nextPageIndex = pageIndex

    private val dragHelper: ViewDragHelper by lazy {
        ViewDragHelper.create(this, 1.0f, object : ViewDragHelper.Callback() {

            override fun tryCaptureView(child: View, pointerId: Int): Boolean {
                STLogUtil.d(tag, "tryCaptureView, pointerId=$pointerId")
                return child == pageViewList[pageIndex]
            }

            override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
                STLogUtil.d(tag, "transformValue clampViewPositionVertical, top=$top, dy=$dy")
                return transformValue(dy.toFloat(), top.toFloat(), measuredHeight.toFloat()).toInt()
            }

            override fun onViewPositionChanged(changedView: View, left: Int, top: Int, dx: Int, dy: Int) {
                STLogUtil.d(tag, "onViewPositionChanged, top=$top, dy=$dy")
                pageViewList.forEach { if (it != changedView) it.offsetTopAndBottom(dy) }
            }

            override fun onEdgeDragStarted(edgeFlags: Int, pointerId: Int) {
                STLogUtil.d(tag, "onEdgeDragStarted, edgeFlags=$edgeFlags, pointerId=$pointerId")
            }

            override fun onEdgeTouched(edgeFlags: Int, pointerId: Int) {
                STLogUtil.d(tag, "onEdgeTouched, edgeFlags=$edgeFlags, pointerId=$pointerId")
            }

            override fun onEdgeLock(edgeFlags: Int): Boolean {
                STLogUtil.d(tag, "onEdgeLock, edgeFlags=$edgeFlags")
                return super.onEdgeLock(edgeFlags)
            }

            override fun onViewDragStateChanged(state: Int) {
                var stateDesc = "NONE"
                when (state) {
                    ViewDragHelper.STATE_DRAGGING -> stateDesc = "DRAGGING"
                    ViewDragHelper.STATE_IDLE -> {
                        stateDesc = "IDLE"
                        onSmoothScrollAnimationEnd()
                    }
                    ViewDragHelper.STATE_SETTLING -> stateDesc = "SETTLING"
                }
                STLogUtil.d(tag, "onViewDragStateChanged, state=$stateDesc")
            }

            override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
                STLogUtil.d(tag, "onViewReleased, yvel=$yvel")
                smoothScrollAfterReleased(releasedChild, yvel)
            }
        })
    }
    private val gestureDetector: GestureDetectorCompat by lazy {
        GestureDetectorCompat(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
                val isVerticalScrollEvent = Math.abs(distanceY) > Math.abs(distanceX)
                STLogUtil.w(tag, "onScroll, isVerticalScrollEvent=$isVerticalScrollEvent")
                return isVerticalScrollEvent
            }
        })
    }

    override fun onFinishInflate() {
        STLogUtil.d(tag, "onFinishInflate")
        super.onFinishInflate()
        (0..6).forEach { pageViewList.add(getChildAt(it)) }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        STLogUtil.d(tag, "onMeasure:widthMeasureSpec=$widthMeasureSpec, heightMeasureSpec=$heightMeasureSpec")

        measureChildren(widthMeasureSpec, heightMeasureSpec)

        val maxWidth = View.MeasureSpec.getSize(widthMeasureSpec)
        val maxHeight = View.MeasureSpec.getSize(heightMeasureSpec)
        val measuredWidth = View.resolveSizeAndState(maxWidth, widthMeasureSpec, 0)
        val measuredHeight = View.resolveSizeAndState(maxHeight, heightMeasureSpec, 0)

        thresholdDistance = measuredHeight / 4
        STLogUtil.d(tag, "onMeasure:measuredWidth=$measuredWidth, measuredHeight=$measuredHeight, thresholdDistance=$thresholdDistance")
        setMeasuredDimension(measuredWidth, measuredHeight)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        STLogUtil.d(tag, "onLayout:left=$left, top=$top, right=$right, bottom=$bottom")
        val containerViewHeight = bottom - top

        for (i in 0 until pageViewList.size) {
            val itemTop = top + (i - pageIndex) * containerViewHeight
            val itemBottom = bottom + (i - pageIndex) * containerViewHeight
            pageViewList[i].layout(left, itemTop, right, itemBottom)
            STLogUtil.d(tag, "onLayout:childView$i, top=$itemTop, bottom=$itemBottom")
        }

        invalidate()
    }

    override fun computeScroll() {
        STLogUtil.d(tag, "computeScroll")
        if (dragHelper.continueSettling(true)) {
            STLogUtil.d(tag, "computeScroll fling ...")
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
        if (event != null) {
            val yScroll = gestureDetector.onTouchEvent(event)
            val shouldIntercept = dragHelper.shouldInterceptTouchEvent(event)

            if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                dragHelper.processTouchEvent(event)
            }
            val intercept = yScroll && shouldIntercept
            STLogUtil.d(tag, "onInterceptTouchEvent, intercept=$intercept")
            return intercept
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

    private fun smoothScrollAfterReleased(releasedChild: View, @Suppress("UNUSED_PARAMETER") yvel: Float) {
        val isPullingDown = releasedChild.top >= 0
        STLogUtil.d(tag, "smoothScrollAfterReleased: top=${releasedChild.top}, isPullingDown=$isPullingDown")
        val finalTop: Int
        if (releasedChild == pageViewList[pageIndex]) {
            if (Math.abs(releasedChild.top) < thresholdDistance) {
                finalTop = 0
            } else {
                if (isPullingDown && pageIndex >= 1) {
                    nextPageIndex = pageIndex - 1
                } else if (!isPullingDown && pageIndex < pageViewList.size - 1) {
                    nextPageIndex = pageIndex + 1
                }
                if (nextPageIndex != pageIndex) {
                    finalTop = if (isPullingDown) releasedChild.measuredHeight else -releasedChild.measuredHeight
                } else {
                    finalTop = 0  // 第一个和最后一个view 禁止滑动到上一个/下一个页面
                }
            }
            smoothScroll(releasedChild, finalTop)
        }
    }

    private fun onSmoothScrollAnimationEnd() {
        if (nextPageIndex != pageIndex) {
            pageIndex = nextPageIndex
            onPageChanged(pageIndex)
        }
    }

    private fun onPageChanged(pageIndex: Int) {
        STLogUtil.e(tag, "onPageChanged, pageIndex=$pageIndex")
    }

    fun smoothScrollTo(toPageIndex: Int) {
        if (toPageIndex >= 0 && toPageIndex < pageViewList.size && toPageIndex != pageIndex) {
            val finalTop: Int = (pageIndex - toPageIndex) * measuredHeight
            nextPageIndex = toPageIndex
            smoothScroll(pageViewList[pageIndex], finalTop)
        }
    }

    private fun smoothScroll(releasedChild: View, top: Int) {
        if (dragHelper.smoothSlideViewTo(releasedChild, 0, top)) ViewCompat.postInvalidateOnAnimation(this)
    }

    /**
     * 获取拉升处理后的位移(达到越拉越难拉动的效果)
     */
    private fun transformValue(offset: Float, originValue: Float, totalValue: Float): Float {
        //x 为0的时候 y 一直为0, 所以当x==0的时候,给一个0.1的最小值
        val x = Math.min(Math.max(Math.abs(originValue).toDouble(), 0.1) / Math.abs(totalValue), 1.0).toFloat()
        val y = Math.min(AccelerateInterpolator(0.15f).getInterpolation(x), 1f)

        val transformValue = originValue - offset * y
        STLogUtil.e(tag, "transformValue originValue=$originValue, transformValue=$transformValue, totalValue=$totalValue, x=$x, y=$y")
        return transformValue
    }

    interface IPageView {
        fun canScrollTopToBottom(): Boolean
        fun canScrollBottomToTop(): Boolean
        fun onLoad(pageIndex: Int)
    }

}