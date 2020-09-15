package com.smart.library.widget.behavior

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.core.widget.NestedScrollView

@Suppress("unused")
class STNestedScrollView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : NestedScrollView(context, attrs) {

    private var onInterceptTouchEventHandler: ((ev: MotionEvent?) -> Unit)? = null
    fun setOnInterceptTouchEventHandler(onInterceptTouchEventHandler: (ev: MotionEvent?) -> Unit) {
        this.onInterceptTouchEventHandler = onInterceptTouchEventHandler
    }

    var enableScroll: Boolean = true
    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        onInterceptTouchEventHandler?.invoke(ev)
        return if (enableScroll) super.onInterceptTouchEvent(ev) else enableScroll
    }
}