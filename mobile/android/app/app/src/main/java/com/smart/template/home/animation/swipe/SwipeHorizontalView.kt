package com.smart.template.home.animation.swipe

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import com.smart.library.base.toPxFromDp
import com.smart.library.util.STSystemUtil

class SwipeHorizontalView(context: Context?, attrs: AttributeSet?) : HorizontalScrollView(context, attrs) {
    private var menuWidth = 80.toPxFromDp()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val wrapperLayout = getChildAt(0) as LinearLayout
        val contentView = wrapperLayout.getChildAt(0)
        contentView.layoutParams = contentView.layoutParams.apply { width = STSystemUtil.screenWidth }
        val menuView = wrapperLayout.getChildAt(1)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    private var downX: Float = 0f
    private var downY: Float = 0f

    /*@SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = ev.rawX
                downY = ev.rawY
            }
            MotionEvent.ACTION_UP -> {
                if ()
                    if (scrollX > (menuWidth / 4)) smoothScrollTo(menuWidth, 0) else smoothScrollTo(0, 0)
                return true
            }
        }
        return super.onTouchEvent(ev)
    }*/

    private var isLeft = true

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_CANCEL || ev.action == MotionEvent.ACTION_UP) {
            val range = 70
            if (isLeft) {
                if (scrollX > range) {
                    isLeft = false
                    smoothScrollTo(menuWidth, 0)
                } else {
                    smoothScrollTo(0, 0)
                }
            } else {
                if (scrollX < menuWidth - range) {
                    isLeft = true
                    smoothScrollTo(0, 0)
                } else {
                    smoothScrollTo(menuWidth, 0)
                }
            }
            return true
        }
        return super.onTouchEvent(ev)
    }

    //恢复状态
    fun reset() {
        isLeft = true
        scrollTo(0, 0)
    }

}