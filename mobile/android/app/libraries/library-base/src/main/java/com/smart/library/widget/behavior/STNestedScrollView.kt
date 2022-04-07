package com.smart.library.widget.behavior

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.Keep
import androidx.core.widget.NestedScrollView
import com.smart.library.util.STLogUtil

@Suppress("unused")
//@Keep
class STNestedScrollView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : NestedScrollView(context, attrs), STBottomSheetViewPagerBehavior.OnInterceptTouchEventHandler {

    private var onInterceptTouchEventHandler: ((ev: MotionEvent?) -> Unit)? = null
    override fun setOnInterceptTouchEventHandler(onInterceptTouchEventHandler: (ev: MotionEvent?) -> Unit) {
        this.onInterceptTouchEventHandler = onInterceptTouchEventHandler
    }

    var enableScroll: Boolean = true
    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        onInterceptTouchEventHandler?.invoke(ev)
        return if (enableScroll) super.onInterceptTouchEvent(ev) else enableScroll
    }

    override fun getView(): View {
        return this
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        STLogUtil.e("[BottomSheet]","STNestedScrollView onLayout")
        super.onLayout(changed, l, t, r, b)
    }

    override fun requestChildFocus(child: View?, focused: View?) {
        STLogUtil.e("[BottomSheet]","STNestedScrollView requestChildFocus")
        super.requestChildFocus(child, focused)
    }
}