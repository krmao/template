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
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ProgressBar
import androidx.annotation.Keep
import androidx.fragment.app.DialogFragment
import com.smart.library.R
import com.smart.library.widget.colorpicker.STColorPickerSwatch.OnColorSelectedListener

/**
 * A dialog which takes in as input an array of colors and creates a palette allowing the user to
 * select a specific color swatch, which invokes a listener.
 */
@Keep
@Suppress("unused", "MemberVisibilityCanBePrivate", "ReplaceArrayEqualityOpWithArraysEquals", "DEPRECATION")
class STColorPickerDialog : DialogFragment(), OnColorSelectedListener {
    private var mAlertDialog: AlertDialog? = null
    private var colors: IntArray? = null
    private var mSelectedColor = 0
    private var mColumns = 0
    private var mSize = 0
    private var mTitle: String? = null
    private var mPalette: STColorPickerPalette? = null
    private var mProgress: ProgressBar? = null
    private var mListener: OnColorSelectedListener? = null

    fun initialize(title: String?, colors: IntArray, selectedColor: Int, columns: Int, size: Int) {
        setArguments(title, columns, size)
        setColors(colors, selectedColor)
    }

    private fun setArguments(title: String?, columns: Int, size: Int) {
        val bundle = Bundle()
        bundle.putString(KEY_TITLE, title)
        bundle.putInt(KEY_COLUMNS, columns)
        bundle.putInt(KEY_SIZE, size)
        arguments = bundle
    }

    fun setOnColorSelectedListener(listener: OnColorSelectedListener?) {
        mListener = listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mTitle = arguments?.getString(KEY_TITLE)
            mColumns = arguments?.getInt(KEY_COLUMNS) ?: mColumns
            mSize = arguments?.getInt(KEY_SIZE) ?: mSize
        }
        if (savedInstanceState != null) {
            colors = savedInstanceState.getIntArray(KEY_COLORS)
            mSelectedColor = (savedInstanceState.getSerializable(KEY_SELECTED_COLOR) as Int?)!!
        }
    }

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val activity: Activity? = activity
        val view = LayoutInflater.from(getActivity()).inflate(R.layout.st_color_picker_dialog, null)
        mProgress = view.findViewById<View>(android.R.id.progress) as ProgressBar
        mPalette = view.findViewById<View>(R.id.color_picker) as STColorPickerPalette
        mPalette?.init(mSize, mColumns, this)
        if (colors != null) {
            showPaletteView()
        }
        val alertDialog = AlertDialog.Builder(activity)
            .setTitle(mTitle)
            .setView(view)
            .create()
        mAlertDialog = alertDialog
        return alertDialog
    }

    override fun onColorSelected(color: Int) {
        mListener?.onColorSelected(color)
        if (targetFragment is OnColorSelectedListener) {
            val listener = targetFragment as OnColorSelectedListener?
            listener?.onColorSelected(color)
        }
        if (color != mSelectedColor) {
            mSelectedColor = color
            // Redraw palette to show checkmark on newly selected color before dismissing.
            mPalette?.drawPalette(colors, mSelectedColor)
        }
        dismiss()
    }

    fun showPaletteView() {
        if (mProgress != null && mPalette != null) {
            mProgress?.visibility = View.GONE
            refreshPalette()
            mPalette?.visibility = View.VISIBLE
        }
    }

    fun showProgressBarView() {
        mProgress?.visibility = View.VISIBLE
        mPalette?.visibility = View.GONE
    }

    fun setColors(colors: IntArray, selectedColor: Int) {
        if (this.colors != colors || mSelectedColor != selectedColor) {
            this.colors = colors
            mSelectedColor = selectedColor
            refreshPalette()
        }
    }

    fun setColors(colors: IntArray) {
        if (this.colors != colors) {
            this.colors = colors
            refreshPalette()
        }
    }

    private fun refreshPalette() {
        if (mPalette != null && colors != null) {
            mPalette?.drawPalette(colors, mSelectedColor)
        }
    }

    var selectedColor: Int
        get() = mSelectedColor
        set(color) {
            if (mSelectedColor != color) {
                mSelectedColor = color
                refreshPalette()
            }
        }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putIntArray(KEY_COLORS, colors)
        outState.putSerializable(KEY_SELECTED_COLOR, mSelectedColor)
    }

    companion object {

        const val SIZE_LARGE = 1
        const val SIZE_SMALL = 2

        private const val KEY_TITLE = "title"
        private const val KEY_COLORS = "colors"
        private const val KEY_SELECTED_COLOR = "selected_color"
        private const val KEY_COLUMNS = "columns"
        private const val KEY_SIZE = "size"

        @JvmStatic
        fun newInstance(title: String?, colors: IntArray, selectedColor: Int, columns: Int, size: Int): STColorPickerDialog {
            val ret = STColorPickerDialog()
            ret.initialize(title, colors, selectedColor, columns, size)
            return ret
        }

        @JvmStatic
        fun newInstance(colors: IntArray, selectedColor: Int, columns: Int, size: Int): STColorPickerDialog {
            return newInstance("选取颜色", colors, selectedColor, columns, size)
        }
    }
}