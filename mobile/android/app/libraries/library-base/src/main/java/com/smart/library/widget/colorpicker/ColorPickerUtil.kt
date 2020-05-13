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
import androidx.fragment.app.FragmentActivity

/*
    var lastSelectedColor: Int = Color.WHITE

    ColorPickerUtil.showColorPickerDialog(view.activity(), selectedColor = lastSelectedColor, colorArrayResId = R.array.colorArray) {
        lastSelectedColor = it
    }
 */
object ColorPickerUtil {

    @JvmStatic
    @JvmOverloads
    fun showColorPickerDialog(activity: FragmentActivity, size: Int = ColorPickerDialog.SIZE_SMALL, columns: Int = 4, selectedColor: Int = Color.BLACK, colorArrayResId: Int, onColorSelected: (color: Int) -> Unit) {
        val colorStringArray = activity.resources.getStringArray(colorArrayResId)
        val colorArray = colorStringArray.map { Color.parseColor(it) }.toIntArray()
        showColorPickerDialog(activity, size, columns, selectedColor, colorArray, onColorSelected)
    }

    @JvmStatic
    @JvmOverloads
    fun showColorPickerDialog(activity: FragmentActivity, size: Int = ColorPickerDialog.SIZE_SMALL, columns: Int = 4, selectedColor: Int = Color.BLACK, colors: IntArray, onColorSelected: (color: Int) -> Unit) {
        val colorPickerDialog = ColorPickerDialog.newInstance(colors, selectedColor, columns, size)
        colorPickerDialog.setOnColorSelectedListener { onColorSelected(it) }
        colorPickerDialog.show(activity.supportFragmentManager, "picker")
    }

}