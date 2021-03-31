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
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import androidx.annotation.Keep

/**
 * A drawable which sets its color filter to a color specified by the user, and changes to a
 * slightly darker color when pressed or focused.
 */
@Keep
class STColorStateDrawable(layers: Array<Drawable?>?, private val mColor: Int) : LayerDrawable(layers!!) {
    @Suppress("DEPRECATION")
    override fun onStateChange(states: IntArray): Boolean {
        var pressedOrFocused = false
        for (state in states) {
            if (state == android.R.attr.state_pressed || state == android.R.attr.state_focused) {
                pressedOrFocused = true
                break
            }
        }
        if (pressedOrFocused) {
            super.setColorFilter(getPressedColor(mColor), PorterDuff.Mode.SRC_ATOP)
        } else {
            super.setColorFilter(mColor, PorterDuff.Mode.SRC_ATOP)
        }
        return super.onStateChange(states)
    }

    /**
     * Given a particular color, adjusts its value by a multiplier.
     */
    private fun getPressedColor(color: Int): Int {
        val hsv = FloatArray(3)
        Color.colorToHSV(color, hsv)
        hsv[2] = hsv[2] * PRESSED_STATE_MULTIPLIER
        return Color.HSVToColor(hsv)
    }

    override fun isStateful(): Boolean {
        return true
    }

    companion object {
        private const val PRESSED_STATE_MULTIPLIER = 0.70f
    }

}