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
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.Keep
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.fragment.app.DialogFragment
import com.smart.library.R

/**
 * A dialog which takes in as input an array of colors and creates a palette allowing the user to
 * select a specific color swatch, which invokes a listener.
 */
@Keep
@Suppress("unused", "MemberVisibilityCanBePrivate", "ReplaceArrayEqualityOpWithArraysEquals")
class STColorAlphaDialog : DialogFragment() {
    private var mAlertDialog: AlertDialog? = null
    private var mInitColor: Int = Color.BLUE
    private var mInitAlpha: Double = 1.0 // [0, 1] 不透明度
    private var mTitle: String? = null
    private var mText: TextView? = null
    private var button1: Button? = null
    private var mSeekBar: AppCompatSeekBar? = null
    private var colorSwatch: STColorPickerSwatch? = null
    private var mListener: ((alpha: Double, colorWithAlpha: Int) -> Unit)? = null

    fun initialize(title: String?, initAlpha: Double = 1.0, @ColorInt initColor: Int = Color.BLUE) {
        setArguments(title, initAlpha, initColor)
    }

    private fun setArguments(title: String?, initAlpha: Double = 1.0, @ColorInt initColor: Int = Color.BLUE) {
        val bundle = Bundle()
        bundle.putString(KEY_TITLE, title)
        bundle.putDouble(KEY_INIT_ALPHA, initAlpha)
        bundle.putInt(KEY_INIT_COLOR, initColor)
        arguments = bundle
    }

    fun setOnColorAlphaListener(listener: (alpha: Double, colorWithAlpha: Int) -> Unit) {
        mListener = listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mTitle = arguments?.getString(KEY_TITLE)
            mInitAlpha = arguments?.getDouble(KEY_INIT_ALPHA) ?: mInitAlpha
            mInitColor = arguments?.getInt(KEY_INIT_COLOR) ?: mInitColor
        }
        if (savedInstanceState != null) {
            mInitAlpha = (savedInstanceState.getSerializable(KEY_INIT_ALPHA) as Double?) ?: mInitAlpha
            mInitColor = (savedInstanceState.getSerializable(KEY_INIT_COLOR) as Int?) ?: mInitColor
        }
    }

    @SuppressLint("InflateParams", "SetTextI18n")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val activity: Activity? = activity
        val view = LayoutInflater.from(getActivity()).inflate(R.layout.st_color_alpha_dialog, null)
        colorSwatch = view.findViewById<STColorPickerSwatch>(R.id.colorSwatch) as STColorPickerSwatch
        mText = view.findViewById<View>(android.R.id.text1) as TextView
        button1 = view.findViewById<View>(android.R.id.button1) as Button
        mSeekBar = view.findViewById<View>(android.R.id.progress) as AppCompatSeekBar

        val progress: Int = (mInitAlpha * 100).toInt()

        mInitColor = STColorPickerUtil.setAlphaComponent(mInitColor, (255 * mInitAlpha).toInt())
        colorSwatch?.setColor(mInitColor)

        mText?.text = "不透明度: $progress%"
        mSeekBar?.max = 100
        mSeekBar?.progress = progress
        mSeekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                mText?.text = "不透明度: $progress%"
                mInitAlpha = progress / 100.0
                mInitColor = STColorPickerUtil.setAlphaComponent(mInitColor, (255 * mInitAlpha).toInt())
                colorSwatch?.setColor(mInitColor)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        button1?.setOnClickListener {
            mListener?.invoke(mInitAlpha, mInitColor)
            dismiss()
        }

        val alertDialog = AlertDialog.Builder(activity)
            .setTitle(mTitle)
            .setView(view)
            .create()
        mAlertDialog = alertDialog
        return alertDialog
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(KEY_INIT_ALPHA, mInitAlpha)
    }

    companion object {

        private const val KEY_TITLE = "title"
        private const val KEY_INIT_ALPHA = "init_alpha"
        private const val KEY_INIT_COLOR = "init_color"

        /**
         * @param initAlpha  [0, 1] 不透明度 1 相当于 100% 即 16进制颜色串最前面两位的 FF, FF:完全不透明, 不透明度 0 相当于 00:完全透明
         */
        @JvmStatic
        @JvmOverloads
        fun newInstance(title: String? = "设置透明度", initAlpha: Double = 1.0, @ColorInt initColor: Int = Color.BLUE): STColorAlphaDialog {
            val ret = STColorAlphaDialog()
            ret.initialize(title, initAlpha, initColor)
            return ret
        }
    }
}