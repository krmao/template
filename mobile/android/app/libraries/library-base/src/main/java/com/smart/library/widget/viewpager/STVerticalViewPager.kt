package com.smart.library.widget.viewpager

import android.annotation.SuppressLint
import android.content.Context
import androidx.viewpager.widget.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.Keep

/**
 * Uses a combination of a PageTransformer and swapping X & Y coordinates
 * of touch events to create the illusion of a vertically scrolling ViewPager.
 *
 * Requires API 11+
 */
@Suppress("unused")
@Keep
class STVerticalViewPager : ViewPager {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    private fun init() {
        // The majority of the magic happens here
        setPageTransformer(true, VerticalPageTransformer())
        // The easiest way to get rid of the overscroll drawing that happens on the left and right
        overScrollMode = View.OVER_SCROLL_NEVER
    }

    private inner class VerticalPageTransformer : ViewPager.PageTransformer {

        override fun transformPage(view: View, position: Float) =
            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.alpha = 0f
            } else if (position <= 1) { // [-1,1]
                view.alpha = 1f
                // Counteract the default slide transition
                view.translationX = view.width * -position
                //set Y position to swipe in from top
                val yPosition = position * view.height
                view.translationY = yPosition
            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.alpha = 0f
            }
    }

    /**
     * Swaps the X and Y coordinates of your touch event.
     */
    private fun swapXY(ev: MotionEvent): MotionEvent {
        val width = width.toFloat()
        val height = height.toFloat()
        val newX = ev.y / height * width
        val newY = ev.x / width * height
        ev.setLocation(newX, newY)
        return ev
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val intercepted = super.onInterceptTouchEvent(swapXY(ev))
        swapXY(ev) // return touch coordinates to original reference frame for any child views
        return intercepted
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent): Boolean = super.onTouchEvent(swapXY(ev))

}
