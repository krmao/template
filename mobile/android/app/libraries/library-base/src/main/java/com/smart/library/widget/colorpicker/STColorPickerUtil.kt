/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.smart.library.widget.colorpicker

import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.annotation.Keep
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.FragmentActivity
import com.smart.library.util.STLogUtil

/*
    var lastSelectedColor: Int = Color.WHITE

    ColorPickerUtil.showColorPickerDialogByColorIntArray(view.activity(), selectedColor = lastSelectedColor, colorIntArrayResId = R.array.colorArray) {
        lastSelectedColor = it
    }
 */
@Keep
@Suppress("unused", "LiftReturnOrAssignment")
object STColorPickerUtil {

    /**
     * Set the alpha component of `color` to be `alpha`.
     *
     * @param alpha 不透明度
     *              使用百分比形式传参 (float/double) 有效范围为 [0.0, 1.0]
     *              or
     *              使用16进制整型进制传参 (int/long) 有效范围为 [0, 255]
     */
    @ColorInt
    fun setAlphaComponent(@ColorInt color: Int, alpha: Number): Int {
        if (alpha is Int || alpha is Long) {
            if (alpha.toInt() < 0 || alpha.toInt() > 255) {
                STLogUtil.e("alpha must be between 0 and 255.")
                return color
            } else {
                return ColorUtils.setAlphaComponent(color, alpha.toInt())
            }
        } else if (alpha is Float || alpha is Double) {
            if (alpha.toDouble() < 0 || alpha.toDouble() > 1) {
                STLogUtil.e("alpha must be between 0.0 and 1.0.")
                return color
            } else {
                return ColorUtils.setAlphaComponent(color, (255 * alpha.toDouble()).toInt())
            }
        } else {
            STLogUtil.e("alpha(int/long) must be between 0 and 255. or alpha(double/float) must be between 0.0 and 1.0.")
            return color
        }
    }

    @JvmStatic
    @JvmOverloads
    fun showColorPickerDialogByColorStringArray(activity: FragmentActivity, size: Int = STColorPickerDialog.SIZE_SMALL, columns: Int = 4, selectedColor: Int = Color.BLACK, colorStringArrayResId: Int, onColorSelected: (color: Int) -> Unit) {
        val colorArray = activity.resources.getStringArray(colorStringArrayResId).map { Color.parseColor(it) }.toIntArray()
        showColorPickerDialog(activity, size, columns, selectedColor, colorArray, onColorSelected)
    }

    @JvmStatic
    @JvmOverloads
    fun showColorPickerDialogByColorIntArray(activity: FragmentActivity, size: Int = STColorPickerDialog.SIZE_SMALL, columns: Int = 4, selectedColor: Int = Color.BLACK, colorIntArrayResId: Int, onColorSelected: (color: Int) -> Unit) {
        val colorArray = activity.resources.getIntArray(colorIntArrayResId)
        showColorPickerDialog(activity, size, columns, selectedColor, colorArray, onColorSelected)
    }

    @JvmStatic
    @JvmOverloads
    fun showColorPickerDialog(activity: FragmentActivity, size: Int = STColorPickerDialog.SIZE_SMALL, columns: Int = 4, selectedColor: Int = Color.BLACK, colors: IntArray, onColorSelected: (color: Int) -> Unit) {
        val colorPickerDialog = STColorPickerDialog.newInstance(colors, selectedColor, columns, size)
        colorPickerDialog.setOnColorSelectedListener(object : STColorPickerSwatch.OnColorSelectedListener {
            override fun onColorSelected(color: Int) {
                onColorSelected(color)
            }
        })
        colorPickerDialog.show(activity.supportFragmentManager, "color-picker")
    }

    @JvmStatic
    @JvmOverloads
    fun showColorAlphaDialog(activity: FragmentActivity, initAlpha: Double = 1.0, @ColorInt initColor: Int = Color.BLUE, onColorAlphaListener: (alpha: Double, colorWithAlpha: Int) -> Unit) {
        val colorPickerDialog = STColorAlphaDialog.newInstance(initAlpha = initAlpha, initColor = initColor)
        colorPickerDialog.setOnColorAlphaListener(onColorAlphaListener)
        colorPickerDialog.show(activity.supportFragmentManager, "color-alpha")
    }

}