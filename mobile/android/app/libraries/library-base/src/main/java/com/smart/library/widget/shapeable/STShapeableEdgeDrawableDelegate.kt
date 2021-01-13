package com.smart.library.widget.shapeable

import android.view.View
import androidx.annotation.StyleableRes

interface STShapeableEdgeDrawableDelegate {
    fun view(): View
    fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int)

    @StyleableRes
    fun getStyleableRes(): IntArray

    @StyleableRes
    fun getStyleableResDrawableLeftWidth(): Int

    @StyleableRes
    fun getStyleableResDrawableLeftHeight(): Int

    @StyleableRes
    fun getStyleableResDrawableTopWidth(): Int

    @StyleableRes
    fun getStyleableResDrawableTopHeight(): Int

    @StyleableRes
    fun getStyleableResDrawableRightWidth(): Int

    @StyleableRes
    fun getStyleableResDrawableRightHeight(): Int

    @StyleableRes
    fun getStyleableResDrawableBottomWidth(): Int

    @StyleableRes
    fun getStyleableResDrawableBottomHeight(): Int

    fun addOnLeftDrawableTouchUpListener(onDrawableTouchUp: (() -> Unit)?)

    fun addOnTopDrawableTouchUpListener(onDrawableTouchUp: (() -> Unit)?)

    fun addOnRightDrawableTouchUpListener(onDrawableTouchUp: (() -> Unit)?)

    fun addOnBottomDrawableTouchUpListener(onDrawableTouchUp: (() -> Unit)?)

}