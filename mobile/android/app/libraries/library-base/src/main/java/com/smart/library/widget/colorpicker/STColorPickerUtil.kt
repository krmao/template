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
import androidx.annotation.IntRange
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.FragmentActivity
import com.smart.library.util.STLogUtil

/*
    var lastSelectedColor: Int = Color.WHITE

    ColorPickerUtil.showColorPickerDialogByColorIntArray(view.activity(), selectedColor = lastSelectedColor, colorIntArrayResId = R.array.colorArray) {
        lastSelectedColor = it
    }
 */
@Suppress("unused")
object STColorPickerUtil {

    /**
     * Set the alpha component of `color` to be `alpha`.
     */
    @ColorInt
    fun setAlphaComponent(@ColorInt color: Int, @IntRange(from = 0x0, to = 0xFF) alpha: Int): Int {
        if (alpha < 0 || alpha > 255) {
            STLogUtil.e("alpha must be between 0 and 255.")
            return color
        }
        return ColorUtils.setAlphaComponent(color, alpha)
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
        colorPickerDialog.show(activity.supportFragmentManager, "picker")
    }

}