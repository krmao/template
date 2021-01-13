package com.smart.library.widget.shapeable.edgedrawable

import android.annotation.SuppressLint
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.TextView


/**
 * https://blog.csdn.net/u011625768/article/details/53508474
 * 主题设置 https://www.jianshu.com/p/43b9181bede0
 */
/** An TextView that draws the bitmap with the provided Shape.  */
@Suppress("MemberVisibilityCanBePrivate", "unused")
class STEdgeDrawableHelper(val delegate: STEdgeDrawableDelegate) {

    private var drawableLeftWidth = 0
    private var drawableTopWidth: Int = 0
    private var drawableRightWidth: Int = 0
    private var drawableBottomWidth: Int = 0
    private var drawableLeftHeight = 0
    private var drawableTopHeight: Int = 0
    private var drawableRightHeight: Int = 0
    private var drawableBottomHeight: Int = 0
    private var isDrawableAlignCenter = true

    fun getTextView(): TextView? = delegate.view() as? TextView

    @Suppress("UNUSED_PARAMETER")
    fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        // super.onSizeChanged(width, height, oldWidth, oldHeight)

        val textView = getTextView() ?: return
        val drawables: Array<Drawable?> = textView.compoundDrawables
        val drawableLeft = drawables[0]
        val drawableTop = drawables[1]
        val drawableRight = drawables[2]
        val drawableBottom = drawables[3]
        setDrawable(drawableLeft, 0, drawableLeftWidth, drawableLeftHeight)
        setDrawable(drawableTop, 1, drawableTopWidth, drawableTopHeight)
        setDrawable(drawableRight, 2, drawableRightWidth, drawableRightHeight)
        setDrawable(drawableBottom, 3, drawableBottomWidth, drawableBottomHeight)
        textView.setCompoundDrawables(drawableLeft, drawableTop, drawableRight, drawableBottom)
    }

    private fun setDrawable(drawable: Drawable?, position: Int, drawableWidth: Int, drawableHeight: Int) {
        drawable ?: return
        val finalDrawableWidth = if (drawableWidth == 0) drawable.intrinsicWidth else drawableWidth
        val finalDrawableHeight = if (drawableHeight == 0) drawable.intrinsicHeight else drawableHeight
        drawable.setBounds(0, 0, finalDrawableWidth, finalDrawableHeight)
    }

    private var onLeftDrawableTouchUp: (() -> Unit)? = null
    private var onTopDrawableTouchUp: (() -> Unit)? = null
    private var onRightDrawableTouchUp: (() -> Unit)? = null
    private var onBottomDrawableTouchUp: (() -> Unit)? = null

    fun addOnLeftDrawableTouchUpListener(onDrawableTouchUp: (() -> Unit)?) {
        this.onLeftDrawableTouchUp = onDrawableTouchUp
    }

    fun addOnTopDrawableTouchUpListener(onDrawableTouchUp: (() -> Unit)?) {
        this.onTopDrawableTouchUp = onDrawableTouchUp
    }

    fun addOnRightDrawableTouchUpListener(onDrawableTouchUp: (() -> Unit)?) {
        this.onRightDrawableTouchUp = onDrawableTouchUp
    }

    fun addOnBottomDrawableTouchUpListener(onDrawableTouchUp: (() -> Unit)?) {
        this.onBottomDrawableTouchUp = onDrawableTouchUp
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initEdgeDrawableOnTouchListener() {
        val textView = getTextView() ?: return

        textView.setOnTouchListener(object : STEdgeDrawableClickListener() {
            override fun onDrawableClick(v: View, drawableIndex: Int) {
                when (drawableIndex) {
                    LEFT -> onLeftDrawableTouchUp?.invoke()
                    TOP -> onTopDrawableTouchUp?.invoke()
                    RIGHT -> onRightDrawableTouchUp?.invoke()
                    BOTTOM -> onBottomDrawableTouchUp?.invoke()
                }
            }
        })
    }

    fun init(attrs: AttributeSet?, defStyleAttr: Int) {
        // Ensure we are using the correctly themed context rather than the context that was passed in.
        val wrapContext = delegate.view().context

        val typedArray: TypedArray = wrapContext.obtainStyledAttributes(attrs, delegate.getStyleableRes(), defStyleAttr, 0)

        drawableLeftWidth = typedArray.getDimensionPixelSize(delegate.getStyleableResDrawableLeftWidth(), 0)
        drawableLeftHeight = typedArray.getDimensionPixelSize(delegate.getStyleableResDrawableLeftHeight(), 0)
        drawableTopWidth = typedArray.getDimensionPixelSize(delegate.getStyleableResDrawableTopWidth(), 0)
        drawableTopHeight = typedArray.getDimensionPixelSize(delegate.getStyleableResDrawableTopHeight(), 0)
        drawableRightWidth = typedArray.getDimensionPixelSize(delegate.getStyleableResDrawableRightWidth(), 0)
        drawableRightHeight = typedArray.getDimensionPixelSize(delegate.getStyleableResDrawableRightHeight(), 0)
        drawableBottomWidth = typedArray.getDimensionPixelSize(delegate.getStyleableResDrawableBottomWidth(), 0)
        drawableBottomHeight = typedArray.getDimensionPixelSize(delegate.getStyleableResDrawableBottomHeight(), 0)

        typedArray.recycle()

        initEdgeDrawableOnTouchListener()
    }

}