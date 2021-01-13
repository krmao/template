package com.smart.library.widget.shapeable

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.Dimension
import com.google.android.material.shape.ShapeAppearanceModel
import com.google.android.material.shape.Shapeable
import com.google.android.material.theme.overlay.MaterialThemeOverlay
import com.smart.library.R
import com.smart.library.widget.shapeable.edgedrawable.STEdgeDrawableDelegate
import com.smart.library.widget.shapeable.edgedrawable.STEdgeDrawableHelper

/** An TextView that draws the bitmap with the provided Shape.  */
class STShapeableEditText @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : androidx.appcompat.widget.AppCompatEditText(MaterialThemeOverlay.wrap(context, attrs, defStyleAttr, STShapeableHelper.DEF_STYLE_RES), attrs, defStyleAttr), Shapeable, STShapeableDelegate, STEdgeDrawableDelegate {
    private val shapeableHelper: STShapeableHelper by lazy { STShapeableHelper(this) }
    private val edgeDrawableHelper: STEdgeDrawableHelper by lazy { STEdgeDrawableHelper(this) }

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
        edgeDrawableHelper.onSizeChanged(width, height, oldWidth, oldHeight)
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
    override fun getStyleableRes(): IntArray = R.styleable.STShapeableEditText
    override fun getStyleableResStrokeColor(): Int = R.styleable.STShapeableEditText_strokeColor
    override fun getStyleableResStrokeWidth(): Int = R.styleable.STShapeableEditText_strokeWidth
    override fun getStyleableResStrokeInPadding(): Int = R.styleable.STShapeableEditText_strokeInPadding
    override fun getStyleableResCornerFamily(): Int = R.styleable.STShapeableEditText_cornerFamily
    override fun getStyleableResCornerFamilyBottomLeft(): Int = R.styleable.STShapeableEditText_cornerFamilyBottomLeft
    override fun getStyleableResCornerFamilyBottomRight(): Int = R.styleable.STShapeableEditText_cornerFamilyBottomRight
    override fun getStyleableResCornerFamilyTopLeft(): Int = R.styleable.STShapeableEditText_cornerFamilyTopLeft
    override fun getStyleableResCornerFamilyTopRight(): Int = R.styleable.STShapeableEditText_cornerFamilyTopRight
    override fun getStyleableResCornerSize(): Int = R.styleable.STShapeableEditText_cornerSize
    override fun getStyleableResCornerSizeBottomLeft(): Int = R.styleable.STShapeableEditText_cornerSizeBottomLeft
    override fun getStyleableResCornerSizeBottomRight(): Int = R.styleable.STShapeableEditText_cornerSizeBottomRight
    override fun getStyleableResCornerSizeTopLeft(): Int = R.styleable.STShapeableEditText_cornerSizeTopLeft
    override fun getStyleableResCornerSizeTopRight(): Int = R.styleable.STShapeableEditText_cornerSizeTopRight

    //region edge drawable
    override fun getStyleableResDrawableLeftWidth(): Int = R.styleable.STShapeableEditText_drawableLeftWidth
    override fun getStyleableResDrawableLeftHeight(): Int = R.styleable.STShapeableEditText_drawableLeftHeight
    override fun getStyleableResDrawableTopWidth(): Int = R.styleable.STShapeableEditText_drawableTopWidth
    override fun getStyleableResDrawableTopHeight(): Int = R.styleable.STShapeableEditText_drawableTopHeight
    override fun getStyleableResDrawableRightWidth(): Int = R.styleable.STShapeableEditText_drawableRightWidth
    override fun getStyleableResDrawableRightHeight(): Int = R.styleable.STShapeableEditText_drawableRightHeight
    override fun getStyleableResDrawableBottomWidth(): Int = R.styleable.STShapeableEditText_drawableBottomWidth
    override fun getStyleableResDrawableBottomHeight(): Int = R.styleable.STShapeableEditText_drawableBottomHeight
    override fun addOnLeftDrawableTouchUpListener(onDrawableTouchUp: (() -> Unit)?) = edgeDrawableHelper.addOnLeftDrawableTouchUpListener(onDrawableTouchUp)
    override fun addOnTopDrawableTouchUpListener(onDrawableTouchUp: (() -> Unit)?) = edgeDrawableHelper.addOnTopDrawableTouchUpListener(onDrawableTouchUp)
    override fun addOnRightDrawableTouchUpListener(onDrawableTouchUp: (() -> Unit)?) = edgeDrawableHelper.addOnRightDrawableTouchUpListener(onDrawableTouchUp)
    override fun addOnBottomDrawableTouchUpListener(onDrawableTouchUp: (() -> Unit)?) = edgeDrawableHelper.addOnBottomDrawableTouchUpListener(onDrawableTouchUp)
    //endregion

    init {
        shapeableHelper.init(attrs, defStyleAttr)
        edgeDrawableHelper.init(attrs, defStyleAttr)
        //region 否则不弹出键盘
        isFocusable = true
        isFocusableInTouchMode = true
        isClickable = true
        isLongClickable = true
        //endregion
    }
}