package com.smart.library.widget.roundable

import android.annotation.TargetApi
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import androidx.annotation.ColorInt
import androidx.annotation.Keep
import com.smart.library.R

/*
@Suppress("unused")
class STRoundConstraintLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : ConstraintLayout(context, attrs, defStyle), STRoundDelegate {

    //region round layout support
    private val roundLayoutHelper: STRoundLayoutHelper by lazy { STRoundLayoutHelper(this) }
    override fun setBackgroundColor(color: Int) {
        roundLayoutHelper.setBackgroundColor(color)
    }

    override fun dispatchDraw(canvas: Canvas) {
        roundLayoutHelper.dispatchDraw(canvas)
        super.dispatchDraw(canvas)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    override fun getOutlineProvider(): ViewOutlineProvider {
        return roundLayoutHelper.getOutlineProvider()
    }

    //endregion
    init {
        roundLayoutHelper.render(attrs)
    }

}
*/
@Suppress("MemberVisibilityCanBePrivate", "unused")
//@Keep
class STRoundableLayoutHelper(val view: View) : STRoundableDelegate {

    private val path: Path = Path()

    /** corner radius */
    var cornerLeftTop: Float = 0F
        set(value) {
            field = value
            view.postInvalidate()
        }

    var cornerRightTop: Float = 0F
        set(value) {
            field = value
            view.postInvalidate()
        }

    var cornerLeftBottom: Float = 0F
        set(value) {
            field = value
            view.postInvalidate()
        }

    var cornerRightBottom: Float = 0F
        set(value) {
            field = value
            view.postInvalidate()
        }

    /** side option means top and bottom corner */

    /**
     * if left side value existed,
     * leftTop and leftBottom value is equal to leftSide value.
     * this is made in consideration of the custom attribute of motion layout.
     * because Constraint only has maximum two custom attribute. (2.0.0-beta2)
     */
    var cornerLeftSide: Float = 0F
        set(value) {
            field = value

            if (field != 0F) {
                cornerLeftTop = field
                cornerLeftBottom = field
            }

            view.postInvalidate()
        }

    var cornerRightSide: Float = 0F
        set(value) {
            field = value

            if (field != 0F) {
                cornerRightTop = field
                cornerRightBottom = field
            }

            view.postInvalidate()
        }


    var cornerAll: Float = 0F
        set(value) {
            field = value

            if (field != 0F) {
                cornerLeftSide = field
                cornerRightSide = field
            }

            view.postInvalidate()
        }

    /** background color */
    var backgroundColor: Int? = null
        set(@ColorInt value) {
            field = value
            view.postInvalidate()
        }

    override fun setBackgroundColor(color: Int) {
        backgroundColor = color
    }

    /** stroke & dash options */
    var strokeLineWidth: Float = 0F
        set(value) {
            field = value
            view.postInvalidate()
        }

    var strokeLineColor = 0XFFFFFFFF.toInt()
        set(@ColorInt value) {
            field = value
            view.postInvalidate()
        }

    var dashLineGap: Float = 0F
        set(value) {
            field = value
            view.postInvalidate()
        }

    var dashLineWidth: Float = 0F
        set(value) {
            field = value
            view.postInvalidate()
        }

    fun render(attrs: AttributeSet?) {
        attrs?.let {
            /** set corner radii */
            with(view.context.obtainStyledAttributes(it, R.styleable.STRoundableLayout)) {
                cornerLeftTop = getDimensionPixelSize(R.styleable.STRoundableLayout_cornerLeftTop, 0).toFloat()
                cornerRightTop = getDimensionPixelSize(R.styleable.STRoundableLayout_cornerRightTop, 0).toFloat()
                cornerLeftBottom = getDimensionPixelSize(R.styleable.STRoundableLayout_cornerLeftBottom, 0).toFloat()
                cornerRightBottom = getDimensionPixelSize(R.styleable.STRoundableLayout_cornerRightBottom, 0).toFloat()
                backgroundColor = getColor(R.styleable.STRoundableLayout_backgroundColor, Color.WHITE)
                strokeLineWidth = getDimensionPixelSize(R.styleable.STRoundableLayout_strokeWidth, 0).toFloat()
                strokeLineColor = getColor(R.styleable.STRoundableLayout_strokeColor, Color.BLACK)
                dashLineWidth = getDimensionPixelSize(R.styleable.STRoundableLayout_dashLineWidth, 0).toFloat()
                dashLineGap = getDimensionPixelSize(R.styleable.STRoundableLayout_dashLineGap, 0).toFloat()
                cornerLeftSide = getDimensionPixelSize(R.styleable.STRoundableLayout_cornerLeftSide, 0).toFloat()
                cornerRightSide = getDimensionPixelSize(R.styleable.STRoundableLayout_cornerRightSide, 0).toFloat()
                cornerAll = getDimensionPixelSize(R.styleable.STRoundableLayout_cornerAll, 0).toFloat()
                recycle()
            }
        }
    }

    /**
     * call before super.dispatchDraw(canvas)
     */
    override fun dispatchDraw(canvas: Canvas) {
        /** for outline remake whenever draw */
        clipPathCanvas(
            canvas,
            floatArrayOf(
                cornerLeftTop, cornerLeftTop, cornerRightTop, cornerRightTop, cornerRightBottom,
                cornerRightBottom, cornerLeftBottom, cornerLeftBottom
            )
        )

        /** set drawable resource corner & background & stroke */
        with(GradientDrawable()) {
            cornerRadii = floatArrayOf(
                cornerLeftTop, cornerLeftTop, cornerRightTop, cornerRightTop,
                cornerRightBottom, cornerRightBottom, cornerLeftBottom, cornerLeftBottom
            )

            if (strokeLineWidth != 0F) {
                setStroke(strokeLineWidth.toInt(), strokeLineColor, dashLineWidth, dashLineGap)
            }

            setColor(backgroundColor ?: Color.WHITE)
            view.background = this
        }
        view.outlineProvider = getOutlineProvider()
        if (view is ViewGroup) {
            view.clipChildren = false
        } else {
            view.clipToOutline = true
        }
    }

    private fun clipPathCanvas(canvas: Canvas, floatArray: FloatArray) {
        path.addRoundRect(
            RectF(0F, 0F, canvas.width.toFloat(), canvas.height.toFloat()),
            floatArray,
            Path.Direction.CW
        )
        canvas.clipPath(path)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    override fun getOutlineProvider(): ViewOutlineProvider? {
        var outlineProvider: ViewOutlineProvider? = null
        try {
            outlineProvider = object : ViewOutlineProvider() {
                override fun getOutline(view: View, outline: Outline) {
                    outline.setConvexPath(path)
                }
            }
        } catch (pathMustBeConvexException: IllegalArgumentException) {
            pathMustBeConvexException.printStackTrace()
        }
        return outlineProvider
    }

}