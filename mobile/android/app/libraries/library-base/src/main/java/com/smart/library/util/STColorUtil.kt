package com.smart.library.util

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.*
import android.graphics.drawable.shapes.RoundRectShape
import android.os.Build
import android.view.View
import androidx.annotation.ColorInt
import java.util.*

@Suppress("unused", "MemberVisibilityCanBePrivate", "LiftReturnOrAssignment")
object STColorUtil {

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