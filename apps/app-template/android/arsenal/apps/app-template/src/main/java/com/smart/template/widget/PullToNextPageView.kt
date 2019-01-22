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
import com.smart.library.util.CXLogUtil
import com.smart.library.util.CXSystemUtil

class PullToNextPageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ViewGroup(context, attrs, defStyleAttr) {

    private val tag: String = PullToNextPageView::class.java.name
    private var thresholdDistance = CXSystemUtil.screenHeight / 5
    private val dragHelper: ViewDragHelper by lazy {
        ViewDragHelper.create(this, 1.0f, object : ViewDragHelper.Callback() {

            /**
             * 決定parent view中哪些child view可被拖動
             */
            override fun tryCaptureView(child: View, pointerId: Int): Boolean {
                CXLogUtil.d(tag, "tryCaptureView, pointerId=$pointerId")
                return child == childView1
            }

            /**
             * 將要移動到的位置座標
             */
            override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
                CXLogUtil.d(tag, "transformValue clampViewPositionVertical, top=$top, dy=$dy")
                return transformValue(dy.toFloat(), top.toFloat(), childView1.measuredHeight.toFloat()).toInt()
            }

            override fun onViewPositionChanged(changedView: View, left: Int, top: Int, dx: Int, dy: Int) {
                CXLogUtil.d(tag, "onViewPositionChanged, top=$top, dy=$dy")

                if (changedView == childView1) {
                    childView0.offsetTopAndBottom(dy)
                    childView2.offsetTopAndBottom(dy)
                }
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
                    ViewDragHelper.STATE_IDLE -> stateDesc = "IDLE"
                    ViewDragHelper.STATE_SETTLING -> stateDesc = "SETTLING"
                }
                CXLogUtil.d(tag, "onViewDragStateChanged, state=$stateDesc")
            }

            override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
                CXLogUtil.d(tag, "onViewReleased, yvel=$yvel")
                smoothScrollAfterReleased(releasedChild, yvel)
            }
        })
    }
    private val gestureDetector: GestureDetectorCompat by lazy {
        GestureDetectorCompat(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
                val isVerticalScrollEvent = Math.abs(distanceY) > Math.abs(distanceX)
                CXLogUtil.w(tag, "onScroll, isVerticalScrollEvent=$isVerticalScrollEvent")
                return isVerticalScrollEvent
            }
        })
    }
    private lateinit var childView0: View
    private lateinit var childView1: View
    private lateinit var childView2: View

    override fun onFinishInflate() {
        CXLogUtil.d(tag, "onFinishInflate")
        super.onFinishInflate()
        childView0 = getChildAt(0)
        childView1 = getChildAt(1)
        childView2 = getChildAt(2)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        CXLogUtil.d(tag, "onMeasure:widthMeasureSpec=$widthMeasureSpec, heightMeasureSpec=$heightMeasureSpec")

        measureChildren(widthMeasureSpec, heightMeasureSpec)

        val maxWidth = View.MeasureSpec.getSize(widthMeasureSpec)
        val maxHeight = View.MeasureSpec.getSize(heightMeasureSpec)
        val measuredWidth = View.resolveSizeAndState(maxWidth, widthMeasureSpec, 0)
        val measuredHeight = View.resolveSizeAndState(maxHeight, heightMeasureSpec, 0)

        thresholdDistance = measuredHeight / 4
        CXLogUtil.d(tag, "onMeasure:measuredWidth=$measuredWidth, measuredHeight=$measuredHeight, thresholdDistance=$thresholdDistance")
        setMeasuredDimension(measuredWidth, measuredHeight)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        CXLogUtil.d(tag, "onLayout:left=$left, top=$top, right=$right, bottom=$bottom")
        childView0.layout(left, -bottom, right, top)
        childView1.layout(left, top, right, bottom)
        childView2.layout(left, bottom, right, bottom + bottom)

        CXLogUtil.d(tag, "onLayout:childView0, top=${-bottom}, bottom=$top")
        CXLogUtil.d(tag, "onLayout:childView1, top=${top}, bottom=$bottom")
        CXLogUtil.d(tag, "onLayout:childView2, top=${bottom}, bottom=${bottom + bottom}")
    }

    /**
     * 滑动时松手后会以一定的速度继续滑动并逐渐停止
     */
    override fun computeScroll() {
        CXLogUtil.d(tag, "computeScroll")
        if (dragHelper.continueSettling(true)) {
            CXLogUtil.d(tag, "computeScroll fling ...")
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
            CXLogUtil.d(tag, "onInterceptTouchEvent, intercept=$intercept")
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

    private fun smoothScrollAfterReleased(releasedChild: View, yvel: Float) {
        CXLogUtil.d(tag, "smoothScrollAfterReleased: top=${releasedChild.top}")
        val finalTop: Int
        if (releasedChild == childView1) {
            if (Math.abs(releasedChild.top) < thresholdDistance) {
                finalTop = 0
            } else {
                val isPullingDown = releasedChild.top >= 0
                CXLogUtil.d(tag, "isPullingDown:$isPullingDown")
                finalTop = if (isPullingDown) releasedChild.measuredHeight else -releasedChild.measuredHeight
            }
            smoothScroll(releasedChild, finalTop)
        }
    }

    private fun smoothScroll(releasedChild: View, top: Int) {
        if (dragHelper.smoothSlideViewTo(releasedChild, 0, top)) ViewCompat.postInvalidateOnAnimation(this)
    }

    //获取拉升处理后的位移(达到越拉越难拉动的效果)
    private fun transformValue(offset: Float, originValue: Float, totalValue: Float): Float {
        //x 为0的时候 y 一直为0, 所以当x==0的时候,给一个0.1的最小值
        val x = Math.min(Math.max(Math.abs(originValue).toDouble(), 0.1) / Math.abs(totalValue), 1.0).toFloat()
        val y = Math.min(AccelerateInterpolator(0.15f).getInterpolation(x), 1f)

        val transformValue = originValue - offset * y
        CXLogUtil.e(tag, "transformValue originValue=$originValue, transformValue=$transformValue, totalValue=$totalValue, x=$x, y=$y")
        return transformValue
    }


}