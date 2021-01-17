package com.smart.library.util

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.*
import android.graphics.drawable.shapes.RoundRectShape
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.StyleableRes
import androidx.core.content.ContextCompat
import com.smart.library.R
import java.util.*


@Suppress("unused", "MemberVisibilityCanBePrivate", "LiftReturnOrAssignment")
object STColorUtil {

    @JvmStatic
    @Suppress("ReplaceJavaStaticMethodWithKotlinAnalog")
    fun manipulateColor(color: Int, factor: Float): Int {
        val a = Color.alpha(color)
        val r = Math.round(Color.red(color) * factor)
        val g = Math.round(Color.green(color) * factor)
        val b = Math.round(Color.blue(color) * factor)
        return Color.argb(a, Math.min(r, 255), Math.min(g, 255), Math.min(b, 255))
    }

    @JvmStatic
    fun isColorDark(@ColorInt color: Int): Boolean {
        val darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255
        return darkness >= 0.5
    }

    @ColorInt
    fun getRipplePressedColor(context: Context, @ColorInt normalColor: Int): Int {
        if (isColorDark(normalColor)) {
            return ContextCompat.getColor(context, R.color.st_pressed)
        } else {
            return ContextCompat.getColor(context, R.color.st_pressed_deeper)
        }
    }

    @JvmStatic
    @ColorInt
    fun getBackgroundColor(view: View?, @ColorInt defaultColor: Int = Color.TRANSPARENT): Int = (view?.background as? ColorDrawable)?.color ?: defaultColor

    /**
     * https://stackoverflow.com/questions/27787870/how-to-use-rippledrawable-programmatically-in-code-not-xml-with-android-5-0-lo
     */
    @JvmStatic
    fun getAdaptiveRippleDrawable(normalColor: Int, pressedColor: Int): Drawable {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            RippleDrawable(ColorStateList.valueOf(pressedColor), getColorDrawableFromColor(normalColor), null)
        } else {
            getStateListDrawable(normalColor, pressedColor)
        }
    }

    @JvmStatic
    fun getAdaptiveRippleDrawable(drawableNormal: Drawable, pressedColor: Int): Drawable {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            RippleDrawable(ColorStateList.valueOf(pressedColor), drawableNormal, null)
        } else {
            getStateListDrawable(drawableNormal, ColorDrawable(pressedColor))
        }
    }


    /**
     * https://stackoverflow.com/questions/27787870/how-to-use-rippledrawable-programmatically-in-code-not-xml-with-android-5-0-lo
     */
    @JvmStatic
    fun setViewBackgroundAdaptiveRippleDrawable(view: View, attrs: AttributeSet?, defStyleAttr: Int, @StyleableRes styleableRes: IntArray, @StyleableRes rippleColorIndex: Int) {
        val normalColor: Int = getBackgroundColor(view)

        val typedArray: TypedArray = view.context.obtainStyledAttributes(attrs, styleableRes, defStyleAttr, 0)

        @Suppress("DEPRECATION")
        val rippleColor: Int = STCustomViewUtil.getColor(typedArray, rippleColorIndex, getRipplePressedColor(view.context, normalColor))
        typedArray.recycle()

        view.background = getAdaptiveRippleDrawable(normalColor, rippleColor)
    }

    @JvmStatic
    fun getPressedColorColorStateList(normalColor: Int, pressedColor: Int): ColorStateList {
        return ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_pressed),
                // intArrayOf(android.R.attr.state_focused),
                // intArrayOf(android.R.attr.state_activated),
                intArrayOf()
            ), intArrayOf(
                pressedColor,
                // pressedColor,
                pressedColor,
                normalColor
            )
        )
    }

    @JvmStatic
    fun getStateListDrawable(normalColor: Int, pressedColor: Int): StateListDrawable {
        val states = StateListDrawable()
        states.addState(intArrayOf(android.R.attr.state_pressed), ColorDrawable(pressedColor))
        // states.addState(intArrayOf(android.R.attr.state_focused), ColorDrawable(pressedColor))
        // states.addState(intArrayOf(android.R.attr.state_activated), ColorDrawable(pressedColor))
        states.addState(intArrayOf(), ColorDrawable(normalColor))
        return states
    }

    @JvmStatic
    fun getStateListDrawable(drawableNormal: Drawable, drawablePressed: Drawable): StateListDrawable {
        val states = StateListDrawable()
        states.addState(intArrayOf(android.R.attr.state_pressed), drawablePressed)
        // states.addState(intArrayOf(android.R.attr.state_focused), ColorDrawable(pressedColor))
        // states.addState(intArrayOf(android.R.attr.state_activated), ColorDrawable(pressedColor))
        states.addState(intArrayOf(), drawableNormal)
        return states
    }

    @JvmStatic
    fun getRippleMask(color: Int, radius: Float = 3f): Drawable {
        val outerRadii = FloatArray(8)
        Arrays.fill(outerRadii, radius)
        val roundRectShape = RoundRectShape(outerRadii, null, null)
        val shapeDrawable = ShapeDrawable(roundRectShape)
        shapeDrawable.paint.color = color
        return shapeDrawable
    }

    @JvmStatic
    fun getColorDrawableFromColor(color: Int): ColorDrawable {
        return ColorDrawable(color)
    }
}