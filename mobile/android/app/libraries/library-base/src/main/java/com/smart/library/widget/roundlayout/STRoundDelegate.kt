package com.smart.library.widget.roundlayout

import android.graphics.Canvas
import android.util.AttributeSet
import android.view.ViewOutlineProvider

interface STRoundDelegate {
    fun getOutlineProvider(): ViewOutlineProvider
    fun dispatchDraw(canvas: Canvas)
    fun setBackgroundColor(color: Int)
}