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

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import com.smart.library.R

/**
 * Creates a circular swatch of a specified color.  Adds a checkmark if marked as checked.
 */
@SuppressLint("ViewConstructor")
class STColorPickerSwatch(context: Context, private val mColor: Int, checked: Boolean, private val mOnColorSelectedListener: OnColorSelectedListener?) : FrameLayout(context), View.OnClickListener {
    private val mSwatchImage: ImageView by lazy { findViewById<View>(R.id.color_picker_swatch) as ImageView }
    private val mCheckmarkImage: ImageView by lazy { findViewById<View>(R.id.color_picker_checkmark) as ImageView }

    /**
     * Interface for a callback when a color square is selected.
     */
    interface OnColorSelectedListener {
        /**
         * Called when a specific color square has been selected.
         */
        fun onColorSelected(color: Int)
    }

    @Suppress("DEPRECATION")
    fun setColor(color: Int) {
        mSwatchImage.setImageDrawable(
            STColorStateDrawable(
                arrayOf(
                    context.resources.getDrawable(R.drawable.st_color_picker_swatch)
                ),
                color
            )
        )
    }

    private fun setChecked(checked: Boolean) {
        mCheckmarkImage.visibility = if (checked) View.VISIBLE else View.GONE
    }

    override fun onClick(v: View) {
        mOnColorSelectedListener?.onColorSelected(mColor)
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.st_color_picker_swatch, this)
        setColor(mColor)
        setChecked(checked)
        setOnClickListener(this)
    }
}