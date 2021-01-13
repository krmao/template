package com.smart.library.widget.roundable

import android.graphics.Canvas
import android.view.ViewOutlineProvider

interface STRoundableDelegate {
    fun getOutlineProvider(): ViewOutlineProvider?
    fun dispatchDraw(canvas: Canvas)
    fun setBackgroundColor(color: Int)
}