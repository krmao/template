package com.smart.views

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.view.ViewCompat
import androidx.customview.widget.ViewDragHelper
import androidx.customview.widget.ViewDragHelper.STATE_DRAGGING
import androidx.customview.widget.ViewDragHelper.STATE_IDLE
import androidx.customview.widget.ViewDragHelper.STATE_SETTLING
import com.smart.activity.device.base.BaseActivity
import com.smart.webview.SmartServiceWebActivity
import com.smart.R
import com.scwang.smartrefresh.layout.util.DensityUtil

class DraggableView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val dragHelper: ViewDragHelper
    private var isDragging = false
    private var childImageViewContainer: FrameLayout
    private var childImageView: ImageView
    private var childImageRightView: ImageView

    private var containerWidth = 0
    private var containerHeight = 0
    private var childViewWidth = DensityUtil.dp2px(54.65f)
    private var childViewHeight = DensityUtil.dp2px(46.1f)

    private var downX = 0f
    private var downY = 0f
    private var downTime = 0L
    private var isTouchInChildViewOnDown = false
    private val clickTimeout = ViewConfiguration.getTapTimeout().toLong()
    private var touchSlop: Int

    init {
        val viewConfiguration = ViewConfiguration.get(context)
        touchSlop = viewConfiguration.scaledTouchSlop

        dragHelper = ViewDragHelper.create(this, 1.0f, DragCallback())

        childImageViewContainer = FrameLayout(context)
        childImageView = ImageView(context)
        childImageView.setImageResource(R.drawable.icon_smart_service_draggable)
        childImageRightView = ImageView(context)
        childImageRightView.setImageResource(R.drawable.icon_smart_service_draggable_right)
        childImageRightView.visibility = View.INVISIBLE

        childImageViewContainer.addView(
            childImageView,
            LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT).apply {
                gravity = Gravity.END
            }
        )

        childImageViewContainer.addView(
            childImageRightView,
            LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT).apply {
                gravity = Gravity.END
            }
        )

        addView(childImageViewContainer, LayoutParams(childViewWidth, childViewHeight))
    }

    private fun changeChildViewImage(isLeft: Boolean) {
        if (isLeft) {
            childImageView.visibility = View.VISIBLE
            childImageRightView.visibility = View.INVISIBLE
        } else {
            childImageView.visibility = View.INVISIBLE
            childImageRightView.visibility = View.VISIBLE
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        containerWidth = w
        containerHeight = h
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> Log.w("drag", "onInterceptTouchEvent ACTION_DOWN")
            MotionEvent.ACTION_MOVE -> Log.w("drag", "onInterceptTouchEvent ACTION_MOVE")
            MotionEvent.ACTION_UP -> Log.w("drag", "onInterceptTouchEvent ACTION_UP")
            MotionEvent.ACTION_CANCEL -> Log.w("drag", "onInterceptTouchEvent ACTION_CANCEL")
        }
        val result = dragHelper.shouldInterceptTouchEvent(ev)
        Log.w("drag", "onInterceptTouchEvent $result")
        return result
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                Log.w("drag", "onTouchEvent ACTION_DOWN")
                downX = event.x
                downY = event.y
                downTime = System.currentTimeMillis()
                isTouchInChildViewOnDown = isTouchInsideChildView(event)
            }

            MotionEvent.ACTION_MOVE -> Log.w("drag", "onTouchEvent ACTION_MOVE")

            MotionEvent.ACTION_UP -> {
                Log.w("drag", "onTouchEvent ACTION_UP")
                val upTime = System.currentTimeMillis()
                val isClickDurationOk = upTime - downTime <= clickTimeout

                var isMoved = false
                val dx = event.x - downX
                val dy = event.y - downY
                if (dx * dx + dy * dy > touchSlop * touchSlop) {
                    isMoved = true
                }

                val isSingleClick = isTouchInChildViewOnDown && isClickDurationOk && !isMoved

                Log.d("drag", "isMoved=$isMoved isClickDurationOk=$isClickDurationOk")
                if (isSingleClick) {
                    Log.d("drag", "onClicked")
                    SmartServiceWebActivity.open(context)
                    return true
                }
            }

            MotionEvent.ACTION_CANCEL -> Log.w("drag", "onTouchEvent ACTION_CANCEL")
        }

        dragHelper.processTouchEvent(event)
        return isDragging
    }

    private fun isTouchInsideChildView(event: MotionEvent): Boolean {
        val childLocation = IntArray(2)
        childImageViewContainer.getLocationOnScreen(childLocation)

        val childLeft = childLocation[0]
        val childTop = childLocation[1]
        val childRight = childLeft + childImageViewContainer.width
        val childBottom = childTop + childImageViewContainer.height

        val touchX = event.rawX
        val touchY = event.rawY

        return touchX.toInt() in childLeft..childRight && touchY.toInt() in childTop..childBottom
    }

    override fun computeScroll() {
        if (dragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    private inner class DragCallback : ViewDragHelper.Callback() {
        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            val result = childImageViewContainer == child
            Log.d("drag", "tryCaptureView $result")
            return result
        }

        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
            return left.coerceIn(0, containerWidth - child.width)
        }

        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
            return top.coerceIn(0, containerHeight - child.height)
        }

        override fun onViewCaptured(capturedChild: View, activePointerId: Int) {
            super.onViewCaptured(capturedChild, activePointerId)
            Log.d("drag", "onViewCaptured")
        }

        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            val width = releasedChild.width
            val left = releasedChild.left
            val top = releasedChild.top
            val isLeft = left < (containerWidth - width) / 2
            val targetLeft = if (isLeft) 0 else containerWidth - width
            dragHelper.settleCapturedViewAt(targetLeft, top)
            changeChildViewImage(isLeft)
            invalidate()
            Log.d("drag", "onViewReleased")
        }

        override fun onViewDragStateChanged(state: Int) {
            super.onViewDragStateChanged(state)
            when (state) {
                STATE_DRAGGING -> {
                    Log.d("drag", "onViewDragStateChanged STATE_DRAGGING")
                    isDragging = true
                }

                STATE_SETTLING -> {
                    Log.d("drag", "onViewDragStateChanged STATE_SETTLING")
                    isDragging = false
                }

                STATE_IDLE -> {
                    Log.d("drag", "onViewDragStateChanged STATE_IDLE")
                    isDragging = false
                }
            }
        }

        override fun onViewPositionChanged(
            changedView: View, left: Int, top: Int, dx: Int, dy: Int
        ) {
            super.onViewPositionChanged(changedView, left, top, dx, dy)
            Log.d("drag", "onViewPositionChanged")
            changedView.layout(left, top, left + changedView.width, top + changedView.height)
        }
    }

    companion object {
        /**
         * call after BaseActivity setContentView
         *
         * @param activity BaseActivity
         * @return DraggableView
         */
        @JvmStatic
        fun addTo(activity: BaseActivity, parentViewGroup: ViewGroup? = null): View {
            val draggableView = DraggableView(activity).apply {
                layoutParams = LayoutParams(
                    DensityUtil.dp2px(124f), DensityUtil.dp2px(508f), Gravity.END
                ).apply {
                    topMargin = DensityUtil.dp2px(30f - 5f + 24f) + activity.getStatusBarHeight()
                }
                // setBackgroundColor(Color.parseColor("#1AFF00AE"))
            }

            val localViewGroup = parentViewGroup ?: activity.window.decorView as FrameLayout
            localViewGroup.addView(draggableView)
            draggableView.visibility = View.INVISIBLE
            return draggableView
        }
    }
}
