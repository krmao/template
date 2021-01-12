package com.smart.library.widget.shapeable

import android.content.res.ColorStateList
import android.graphics.Canvas
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.Dimension
import com.google.android.material.shape.ShapeAppearanceModel

interface STShableableDelegate {
    fun onDetachedFromWindow()
    fun onAttachedToWindow()
    fun onDraw(canvas: Canvas)
    fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int)
    fun setShapeAppearanceModel(shapeAppearanceModel: ShapeAppearanceModel)

    /**
     * Sets the stroke color resource for this ImageView. Both stroke color and stroke width must be
     * set for a stroke to be drawn.
     *
     * @param strokeColorResourceId Color resource to use for the stroke.
     * @attr ref com.google.android.material.R.styleable#ShapeableImageView_strokeColor
     * @see .setStrokeColor
     * @see .getStrokeColor
     */
    fun setStrokeColorResource(@ColorRes strokeColorResourceId: Int)

    /**
     * Returns the stroke color for this ImageView.
     *
     * @attr ref com.google.android.material.R.styleable#ShapeableImageView_strokeColor
     * @see .setStrokeColor
     * @see .setStrokeColorResource
     */
    fun getStrokeColor(): ColorStateList?

    /**
     * Sets the stroke width for this ImageView. Both stroke color and stroke width must be set for a
     * stroke to be drawn.
     *
     * @param strokeWidth Stroke width for this ImageView.
     * @attr ref com.google.android.material.R.styleable#ShapeableImageView_strokeWidth
     * @see .setStrokeWidthResource
     * @see .getStrokeWidth
     */
    fun setStrokeWidth(@Dimension strokeWidth: Float)

    /**
     * Sets the stroke width dimension resource for this ImageView. Both stroke color and stroke width
     * must be set for a stroke to be drawn.
     *
     * @param strokeWidthResourceId Stroke width dimension resource for this ImageView.
     * @attr ref com.google.android.material.R.styleable#ShapeableImageView_strokeWidth
     * @see .setStrokeWidth
     * @see .getStrokeWidth
     */
    fun setStrokeWidthResource(@DimenRes strokeWidthResourceId: Int)

    /**
     * Gets the stroke width for this ImageView.
     *
     * @return Stroke width for this ImageView.
     * @attr ref com.google.android.material.R.styleable#ShapeableImageView_strokeWidth
     * @see .setStrokeWidth
     * @see .setStrokeWidthResource
     */
    @Dimension
    fun getStrokeWidth(): Float

    fun setStrokeColor(strokeColor: ColorStateList?)

}