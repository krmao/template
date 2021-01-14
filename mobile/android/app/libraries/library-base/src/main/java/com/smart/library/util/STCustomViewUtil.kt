package com.smart.library.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.TextUtils
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.MotionEvent
import android.view.TouchDelegate
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.StyleableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.doOnLayout
import com.google.android.material.shape.AbsoluteCornerSize
import com.google.android.material.shape.CornerSize
import com.google.android.material.shape.RelativeCornerSize


@Suppress("unused", "ControlFlowWithEmptyBody")
object STCustomViewUtil {

    @JvmStatic
    fun setExpandedTouchArea(view: View, extraSpace: Int) {
        val parent = view.parent as View
        parent.doOnLayout {
            val area = Rect()

            view.getHitRect(area)

            area.top -= extraSpace
            area.bottom += extraSpace
            area.left -= extraSpace
            area.right += extraSpace

            parent.touchDelegate = TouchDelegate(area, view)
        }
    }

    /**
     * 透明区域不可点击
     * https://stackoverflow.com/questions/25014132/android-make-custom-views-transparent-area-not-clickable
     */
    @JvmStatic
    fun isInClickableAreaExcludeTransparent(view: View, bitmap: Bitmap?, event: MotionEvent): Boolean {
        val clickX = (event.x * (bitmap?.width?.toDouble() ?: view.width.toDouble()) / view.width.toDouble()).toInt()
        val clickY = (event.y * (bitmap?.height?.toDouble() ?: view.height.toDouble()) / view.height.toDouble()).toInt()
        if (bitmap != null) {
            if (clickX >= 0 && clickX < bitmap.width && clickY >= 0 && clickY < bitmap.height) {
                if (Color.alpha(bitmap.getPixel(clickX, clickY)) > 0) {
                    return true
                }
            }
        }
        return false
    }

    @JvmStatic
    fun isLayoutRtl(view: View): Boolean {
        return view.layoutDirection == View.LAYOUT_DIRECTION_RTL
    }

    @JvmStatic
    fun getCornerSizeDescription(cornerSize: CornerSize): String {
        return when (cornerSize) {
            is AbsoluteCornerSize -> {
                cornerSize.cornerSize.toString()
            }
            is RelativeCornerSize -> {
                cornerSize.relativePercent.toString()
            }
            else -> {
                "$cornerSize"
            }
        }
    }

    @JvmStatic
    fun getCornerSize(typedArray: TypedArray, index: Int, defaultValue: CornerSize): CornerSize {
        val value = typedArray.peekValue(index) ?: return defaultValue
        return when (value.type) {
            TypedValue.TYPE_DIMENSION -> {
                // Eventually we might want to change this to call getDimension() since corner sizes support
                // floats.
                AbsoluteCornerSize(
                    TypedValue.complexToDimensionPixelSize(value.data, typedArray.resources.displayMetrics).toFloat()
                )
            }
            TypedValue.TYPE_FRACTION -> {
                RelativeCornerSize(value.getFraction(1.0f, 1.0f))
            }
            else -> {
                defaultValue
            }
        }
    }

    /**
     * Called by [.setTheme] and [.getTheme] to apply a theme
     * resource to the current Theme object. May be overridden to change the
     * default (simple) behavior. This method will not be called in multiple
     * threads simultaneously.
     *
     * @param resId the style resource being applied to <var>theme</var>
     * applied to <var>theme</var>
     */
    @JvmStatic
    fun initializeAndApplyThemeResource(context: Context, resId: Int): Resources.Theme {
        val newTheme: Resources.Theme = context.resources.newTheme()
        val contextTheme: Resources.Theme? = context.theme
        if (contextTheme != null) {
            newTheme.setTo(contextTheme)
        }
        newTheme.applyStyle(resId, true)
        return newTheme
    }

    @JvmStatic
    fun getDimensionOrByFractionValue(typedValue: TypedValue?, height: Float, metrics: DisplayMetrics, defaultValue: Float): Float {
        if (typedValue == null) {
            return defaultValue
        }
        if (typedValue.type == TypedValue.TYPE_FRACTION) {
            return typedValue.getFraction(1f, 1f) * height
        } else if (typedValue.type == TypedValue.TYPE_DIMENSION) {
            return typedValue.getDimension(metrics)
        }
        return defaultValue
    }

    /**
     * Returns the [ColorStateList] from the given [TypedArray] attributes. The resource
     * can include themeable attributes, regardless of API level.
     */
    @SuppressLint("ObsoleteSdkInt")
    @JvmStatic
    fun getColorStateList(context: Context, attributes: TypedArray, @StyleableRes index: Int): ColorStateList? {
        if (attributes.hasValue(index)) {
            val resourceId = attributes.getResourceId(index, 0)
            if (resourceId != 0) {
                val value = AppCompatResources.getColorStateList(context, resourceId)
                if (value != null) {
                    return value
                }
            }
        }

        // Reading a single color with getColorStateList() on API 15 and below doesn't always correctly
        // read the value. Instead we'll first try to read the color directly here.
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            val color = attributes.getColor(index, -1)
            if (color != -1) {
                return ColorStateList.valueOf(color)
            }
        }
        return attributes.getColorStateList(index)
    }

    @ColorInt
    fun getColor(typedArray: TypedArray, @StyleableRes index: Int, @ColorInt defaultColor: Int): Int {
        return try {
            val color = typedArray.getColor(index, Integer.MAX_VALUE)
            if (color != Integer.MAX_VALUE) {
                color
            } else {
                val colorStr = typedArray.getString(index)
                if (TextUtils.isEmpty(colorStr)) {
                    defaultColor
                } else {
                    try {
                        Color.parseColor(colorStr)
                    } catch (ignored: Exception) {
                        defaultColor
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            defaultColor
        }
    }

    @JvmStatic
    fun getBoolean(typedArray: TypedArray, index: Int, defaultValue: Boolean): Boolean {
        return try {
            val resId = typedArray.getResourceId(index, -1)
            if (resId != -1)
                typedArray.resources.getBoolean(resId)
            else
                typedArray.getBoolean(index, defaultValue)
        } catch (e: Exception) {
            e.printStackTrace()
            defaultValue
        }
    }

    @JvmStatic
    fun getFloat(typedArray: TypedArray, index: Int, defaultValue: Float): Float {
        val result: Float
        result = try {
            typedArray.getFloat(index, defaultValue)
        } catch (e: Exception) {
            e.printStackTrace()
            defaultValue
        }

        return result
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @JvmStatic
    fun getDrawable(typedArray: TypedArray, index: Int, @ColorInt defaultColorInt: Int, @DrawableRes defaultRes: Int): Drawable? {
        var result: Drawable? = null
        try {
            val drawable = typedArray.getDrawable(index)
            if (drawable != null) {
                result = drawable
            } else {
                val color = typedArray.getColor(index, Integer.MAX_VALUE)
                if (color != Integer.MAX_VALUE) {
                    result = ColorDrawable(color)
                } else {
                    val colorStr = typedArray.getString(index)
                    if (!TextUtils.isEmpty(colorStr)) {
                        try {
                            result = ColorDrawable(Color.parseColor(colorStr))
                        } catch (ignored: Exception) {
                        }

                    } else {
                        val resId = typedArray.getResourceId(index, -1)
                        if (resId != -1) {
                            @Suppress("DEPRECATION")
                            result = typedArray.resources.getDrawable(resId)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (result == null) {
            if (defaultColorInt != -1)
                result = ColorDrawable(defaultColorInt)
            @Suppress("DEPRECATION")
            if (defaultRes != -1)
                result = typedArray.resources.getDrawable(defaultRes)
        }
        return result
    }

    @JvmStatic
    fun getString(typedArray: TypedArray, index: Int, defaultValue: String): String {
        var result: String?
        result = try {
            val resId = typedArray.getResourceId(index, -1)
            if (resId != -1)
                typedArray.resources.getString(resId)
            else
                typedArray.getString(index)
        } catch (e: Exception) {
            e.printStackTrace()
            defaultValue
        }

        if (result == null)
            result = defaultValue
        return result
    }

    /**
     * 为 view 设置背景
     * @param view         view
     * @param index        styleable
     * @param defaultColor -1 or >0
     * @param defaultRes   -1 or >0
     * @param typedArray   typedArray
     */
    @JvmStatic
    fun setBackground(view: View, index: Int, defaultColor: Int, defaultRes: Int, typedArray: TypedArray) = try {
        if (defaultColor != -1)
            view.setBackgroundColor(defaultColor)
        if (defaultRes != -1)
            view.setBackgroundResource(defaultRes)

        val drawable = typedArray.getDrawable(index)
        if (drawable != null) {
            @Suppress("DEPRECATION")
            view.setBackgroundDrawable(drawable)
        } else {
            val color = typedArray.getColor(index, Integer.MAX_VALUE)
            if (color != Integer.MAX_VALUE) {
                view.setBackgroundColor(color)
            } else {
                val colorStr = typedArray.getString(index)
                if (!TextUtils.isEmpty(colorStr)) {
                    try {
                        view.setBackgroundColor(Color.parseColor(colorStr))
                    } catch (ignored: Exception) {
                    }

                } else {
                    val resId = typedArray.getResourceId(index, -1)
                    if (resId == -1) {
                    } else {
                        view.setBackgroundResource(resId)
                    }
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
