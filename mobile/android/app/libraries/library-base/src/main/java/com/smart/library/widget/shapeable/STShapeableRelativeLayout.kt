package com.smart.library.widget.shapeable

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.Dimension
import androidx.annotation.Keep
import com.google.android.material.shape.ShapeAppearanceModel
import com.google.android.material.shape.Shapeable
import com.google.android.material.theme.overlay.MaterialThemeOverlay
import com.smart.library.R

/** An TextView that draws the bitmap with the provided Shape.  */
//@Keep
class STShapeableRelativeLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : RelativeLayout(MaterialThemeOverlay.wrap(context, attrs, defStyleAttr, STShapeableHelper.DEF_STYLE_RES), attrs, defStyleAttr), Shapeable, STShapeableDelegate {
    private val shapeableHelper: STShapeableHelper by lazy { STShapeableHelper(this) }

    override fun onDetachedFromWindow() {
        shapeableHelper.onDetachedFromWindow()
        super.onDetachedFromWindow()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        shapeableHelper.onAttachedToWindow()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        shapeableHelper.onDraw(canvas)
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)
        shapeableHelper.onSizeChanged(width, height, oldWidth, oldHeight)
    }

    override fun setShapeAppearanceModel(shapeAppearanceModel: ShapeAppearanceModel) {
        shapeableHelper.shapeAppearanceModel = shapeAppearanceModel
    }

    override fun getShapeAppearanceModel(): ShapeAppearanceModel {
        return shapeableHelper.shapeAppearanceModel
    }

    override fun setStrokeColorResource(@ColorRes strokeColorResourceId: Int) {
        shapeableHelper.setStrokeColorResource(strokeColorResourceId)
    }

    override fun getStrokeColor(): ColorStateList? {
        return shapeableHelper.getStrokeColor()
    }

    override fun setStrokeWidth(@Dimension strokeWidth: Float) {
        shapeableHelper.setStrokeWidth(strokeWidth)
    }

    override fun setStrokeWidthResource(@DimenRes strokeWidthResourceId: Int) {
        shapeableHelper.setStrokeWidthResource(strokeWidthResourceId)
    }

    @Dimension
    override fun getStrokeWidth(): Float {
        return shapeableHelper.getStrokeWidth()
    }

    override fun setStrokeColor(strokeColor: ColorStateList?) {
        shapeableHelper.setStrokeColor(strokeColor)
    }

    override fun view(): View = this
    override fun getStyleableRes(): IntArray = R.styleable.STShapeableRelativeLayout
    override fun getStyleableResStrokeColor(): Int = R.styleable.STShapeableRelativeLayout_strokeColor
    override fun getStyleableResStrokeWidth(): Int = R.styleable.STShapeableRelativeLayout_strokeWidth
    override fun getStyleableResStrokeInPadding(): Int = R.styleable.STShapeableRelativeLayout_strokeInPadding
    override fun getStyleableResCornerFamily(): Int = R.styleable.STShapeableRelativeLayout_cornerFamily
    override fun getStyleableResCornerFamilyBottomLeft(): Int = R.styleable.STShapeableRelativeLayout_cornerFamilyBottomLeft
    override fun getStyleableResCornerFamilyBottomRight(): Int = R.styleable.STShapeableRelativeLayout_cornerFamilyBottomRight
    override fun getStyleableResCornerFamilyTopLeft(): Int = R.styleable.STShapeableRelativeLayout_cornerFamilyTopLeft
    override fun getStyleableResCornerFamilyTopRight(): Int = R.styleable.STShapeableRelativeLayout_cornerFamilyTopRight
    override fun getStyleableResCornerSize(): Int = R.styleable.STShapeableRelativeLayout_cornerSize
    override fun getStyleableResCornerSizeBottomLeft(): Int = R.styleable.STShapeableRelativeLayout_cornerSizeBottomLeft
    override fun getStyleableResCornerSizeBottomRight(): Int = R.styleable.STShapeableRelativeLayout_cornerSizeBottomRight
    override fun getStyleableResCornerSizeTopLeft(): Int = R.styleable.STShapeableRelativeLayout_cornerSizeTopLeft
    override fun getStyleableResCornerSizeTopRight(): Int = R.styleable.STShapeableRelativeLayout_cornerSizeTopRight
    
    init {
        shapeableHelper.init(attrs, defStyleAttr)
    }
}