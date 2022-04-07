package com.smart.library.widget.roundable

import android.graphics.Canvas
import android.view.ViewOutlineProvider
import androidx.annotation.Keep

//@Keep
interface STRoundableDelegate {
    fun getOutlineProvider(): ViewOutlineProvider?
    fun dispatchDraw(canvas: Canvas)
    fun setBackgroundColor(color: Int)
}