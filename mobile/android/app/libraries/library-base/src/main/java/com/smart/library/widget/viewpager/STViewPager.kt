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
class STViewPager @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : ViewPager(context, attrs) {

    private var disableScroll: Boolean = true
    fun disableScroll(disable: Boolean) {
        this.disableScroll = disable
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return false//!disableScroll
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent): Boolean = false //!disableScroll

    override fun canScrollHorizontally(direction: Int): Boolean {
        return false //super.canScrollHorizontally(direction)
    }

    override fun canScroll(v: View?, checkV: Boolean, dx: Int, x: Int, y: Int): Boolean {
        return false //super.canScroll(v, checkV, dx, x, y)
    }
}
