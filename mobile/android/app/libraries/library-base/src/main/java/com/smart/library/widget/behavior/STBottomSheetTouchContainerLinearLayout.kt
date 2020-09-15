package com.smart.library.widget.behavior

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout

@Suppress("unused")
class STBottomSheetTouchContainerLinearLayout @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr), STBottomSheetViewPagerBehavior.OnInterceptTouchEventHandler {

    private var onInterceptTouchEventHandler: ((ev: MotionEvent?) -> Unit)? = null
    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        onInterceptTouchEventHandler?.invoke(ev)
        return super.onInterceptTouchEvent(ev)
    }

    override fun setOnInterceptTouchEventHandler(onInterceptTouchEventHandler: (ev: MotionEvent?) -> Unit) {
        this.onInterceptTouchEventHandler = onInterceptTouchEventHandler
    }
    override fun getView(): View {
        return this
    }

}