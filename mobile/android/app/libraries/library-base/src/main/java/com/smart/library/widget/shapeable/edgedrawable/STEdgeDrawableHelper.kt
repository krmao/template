package com.smart.library.widget.shapeable.edgedrawable

import android.annotation.SuppressLint
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.annotation.DrawableRes
import com.smart.library.util.STLogUtil


/**
 * https://blog.csdn.net/u011625768/article/details/53508474
 * 主题设置 https://www.jianshu.com/p/43b9181bede0
 */
/** An TextView that draws the bitmap with the provided Shape.  */
@Suppress("MemberVisibilityCanBePrivate", "unused")
class STEdgeDrawableHelper(val delegate: STEdgeDrawableDelegate) {

    enum class Position(val value: Int) {
        LEFT(0),
        TOP(1),
        RIGHT(2),
        BOTTOM(3);
    }

    private var drawableLeftWidth = 0
    private var drawableTopWidth: Int = 0
    private var drawableRightWidth: Int = 0
    private var drawableBottomWidth: Int = 0
    private var drawableLeftHeight = 0
    private var drawableTopHeight: Int = 0
    private var drawableRightHeight: Int = 0
    private var drawableBottomHeight: Int = 0
    private var isDrawableAlignCenter = true

    private var onDrawableClickListener: ((Position) -> Unit)? = null

    fun getTextView(): TextView? = delegate.view() as? TextView

    @Suppress("UNUSED_PARAMETER")
    fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        // super.onSizeChanged(width, height, oldWidth, oldHeight)

        setCompoundDrawables()
    }

    @Suppress("DEPRECATION")
    @SuppressLint("UseCompatLoadingForDrawables")
    @JvmOverloads
    fun setEdgeDrawable(position: Position, @DrawableRes drawableResId: Int, width: Int = 0, height: Int = 0) {
        setEdgeDrawable(position, delegate.view().resources.getDrawable(drawableResId), width, height)
    }

    @JvmOverloads
    fun setEdgeDrawable(position: Position, drawable: Drawable?, width: Int = 0, height: Int = 0) {
        setCompoundDrawables(position, drawable, width, height)
    }

    fun setOnEdgeDrawableClickListener(onDrawableClickListener: ((Position) -> Unit)?) {
        this.onDrawableClickListener = onDrawableClickListener
    }

    /**
     * @param position null reset all
     */
    private fun setCompoundDrawables(position: Position? = null, drawable: Drawable? = null, drawableWidth: Int = 0, drawableHeight: Int = 0) {
        val textView = getTextView() ?: return
        val drawables: Array<Drawable?> = textView.compoundDrawables

        val drawableLeft = if (position == Position.LEFT) drawable else drawables[0]
        val drawableTop = if (position == Position.TOP) drawable else drawables[1]
        val drawableRight = if (position == Position.RIGHT) drawable else drawables[2]
        val drawableBottom = if (position == Position.BOTTOM) drawable else drawables[3]

        setCompoundDrawableBounds(drawableLeft, if (position == Position.LEFT && drawableWidth > 0) drawableWidth else drawableLeftWidth, if (position == Position.LEFT && drawableHeight > 0) drawableHeight else drawableLeftHeight)
        setCompoundDrawableBounds(drawableTop, if (position == Position.TOP && drawableWidth > 0) drawableWidth else drawableTopWidth, if (position == Position.TOP && drawableHeight > 0) drawableHeight else drawableTopHeight)
        setCompoundDrawableBounds(drawableRight, if (position == Position.RIGHT && drawableWidth > 0) drawableWidth else drawableRightWidth, if (position == Position.RIGHT && drawableHeight > 0) drawableHeight else drawableRightHeight)
        setCompoundDrawableBounds(drawableBottom, if (position == Position.BOTTOM && drawableWidth > 0) drawableWidth else drawableBottomWidth, if (position == Position.BOTTOM && drawableHeight > 0) drawableHeight else drawableBottomHeight)

        textView.setCompoundDrawables(drawableLeft, drawableTop, drawableRight, drawableBottom)
    }

    private fun setCompoundDrawableBounds(drawable: Drawable?, drawableWidth: Int, drawableHeight: Int): Drawable? {
        drawable ?: return null
        STLogUtil.d("[drawable]", "drawableWidth=$drawableWidth, drawableHeight=$drawableHeight")
        val finalDrawableWidth = if (drawableWidth <= 0) drawable.intrinsicWidth else drawableWidth
        val finalDrawableHeight = if (drawableHeight <= 0) drawable.intrinsicHeight else drawableHeight
        drawable.setBounds(0, 0, finalDrawableWidth, finalDrawableHeight)
        return drawable
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initEdgeDrawableOnTouchListener() {
        val textView = getTextView() ?: return

        textView.setOnTouchListener(object : STEdgeDrawableClickListener() {
            override fun onDrawableClick(v: View, drawableIndex: Int) {
                when (drawableIndex) {
                    LEFT -> onDrawableClickListener?.invoke(Position.LEFT)
                    TOP -> onDrawableClickListener?.invoke(Position.TOP)
                    RIGHT -> onDrawableClickListener?.invoke(Position.RIGHT)
                    BOTTOM -> onDrawableClickListener?.invoke(Position.BOTTOM)
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

        setCompoundDrawables()
        initEdgeDrawableOnTouchListener()
    }

}