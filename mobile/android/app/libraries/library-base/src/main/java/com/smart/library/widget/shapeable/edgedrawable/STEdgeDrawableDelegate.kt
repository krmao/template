package com.smart.library.widget.shapeable.edgedrawable

import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.Keep
import androidx.annotation.StyleableRes

//@Keep
interface STEdgeDrawableDelegate {
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

    fun setEdgeDrawable(position: STEdgeDrawableHelper.Position, @DrawableRes drawableResId: Int, width: Int = 0, height: Int = 0)

    fun setEdgeDrawable(position: STEdgeDrawableHelper.Position, drawable: Drawable?, width: Int = 0, height: Int = 0)

    fun setOnEdgeDrawableClickListener(onEdgeDrawableClickListener: ((STEdgeDrawableHelper.Position) -> Unit)?)

}