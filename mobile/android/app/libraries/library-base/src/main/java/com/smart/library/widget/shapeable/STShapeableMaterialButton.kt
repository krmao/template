package com.smart.library.widget.shapeable

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.Dimension
import androidx.annotation.Keep
import com.google.android.material.shape.ShapeAppearanceModel
import com.google.android.material.shape.Shapeable
import com.smart.library.R
import com.smart.library.util.STReflectUtil
import com.smart.library.widget.shapeable.edgedrawable.STEdgeDrawableDelegate
import com.smart.library.widget.shapeable.edgedrawable.STEdgeDrawableHelper

/*
<!-- android:background="?attr/selectableItemBackground"-->
<com.smart.library.widget.shapeable.STShapeableButton
    android:id="@+id/button"
    style="@style/STButton.TextButton.Icon"
    android:layout_width="100dp"
    android:layout_height="40dp"
    android:layout_margin="5dp"
    android:backgroundTint="@color/orange"
    android:drawablePadding="5dp"
    android:gravity="center"
    android:paddingLeft="5dp"
    android:paddingTop="1dp"
    android:paddingRight="1dp"
    android:paddingBottom="1dp"
    android:text="button"
    android:textColor="@color/red"
    android:textSize="10sp"
    app:cornerFamilyBottomLeft="rounded"
    app:cornerFamilyBottomRight="rounded"
    app:cornerFamilyTopLeft="rounded"
    app:cornerFamilyTopRight="rounded"
    app:cornerSizeBottomLeft="5dp"
    app:cornerSizeBottomRight="5dp"
    app:cornerSizeTopLeft="70%"
    app:cornerSizeTopRight="70%"
    app:drawableLeftCompat="@drawable/st_emo_im_happy"
    app:drawableLeftHeight="10dp"
    app:drawableLeftWidth="10dp"
    app:strokeColor="#0000ff"
    app:strokeWidth="2dp"
    tools:ignore="SmallSp" />
*/
/** An TextView that draws the bitmap with the provided Shape.  */
// https://material.io/components/buttons/android#using-buttons
// https://www.cnblogs.com/sydmobile/p/13674518.html
/**
 * MaterialButton 继承 AppCompactButton 继承 Button
 * xml 中使用 Button 会被自动替换为 AppCompactButton
 * 所以自定义 Button 的时候需要继承 AppCompactButton(AppCompactButton 实现了 backgroundTint/backgroundTintMode)
 */
//@Keep
class STShapeableMaterialButton @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : com.google.android.material.button.MaterialButton(context, attrs, defStyleAttr), Shapeable, STShapeableDelegate, STEdgeDrawableDelegate {
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

    override fun getStrokeWidth(): Int {
        return shapeableHelper.getStrokeWidth().toInt()
    }

    override fun setStrokeColor(strokeColor: ColorStateList?) {
        shapeableHelper.setStrokeColor(strokeColor)
    }

    override fun view(): View = this
    override fun getStyleableRes(): IntArray = R.styleable.STShapeableButton
    override fun getStyleableResStrokeColor(): Int = R.styleable.STShapeableButton_strokeColor
    override fun getStyleableResStrokeWidth(): Int = R.styleable.STShapeableButton_strokeWidth
    override fun getStyleableResStrokeInPadding(): Int = R.styleable.STShapeableButton_strokeInPadding
    override fun getStyleableResCornerFamily(): Int = R.styleable.STShapeableButton_cornerFamily
    override fun getStyleableResCornerFamilyBottomLeft(): Int = R.styleable.STShapeableButton_cornerFamilyBottomLeft
    override fun getStyleableResCornerFamilyBottomRight(): Int = R.styleable.STShapeableButton_cornerFamilyBottomRight
    override fun getStyleableResCornerFamilyTopLeft(): Int = R.styleable.STShapeableButton_cornerFamilyTopLeft
    override fun getStyleableResCornerFamilyTopRight(): Int = R.styleable.STShapeableButton_cornerFamilyTopRight
    override fun getStyleableResCornerSize(): Int = R.styleable.STShapeableButton_cornerSize
    override fun getStyleableResCornerSizeBottomLeft(): Int = R.styleable.STShapeableButton_cornerSizeBottomLeft
    override fun getStyleableResCornerSizeBottomRight(): Int = R.styleable.STShapeableButton_cornerSizeBottomRight
    override fun getStyleableResCornerSizeTopLeft(): Int = R.styleable.STShapeableButton_cornerSizeTopLeft
    override fun getStyleableResCornerSizeTopRight(): Int = R.styleable.STShapeableButton_cornerSizeTopRight

    //region edge drawable
    override fun getStyleableResDrawableLeftWidth(): Int = R.styleable.STShapeableButton_drawableLeftWidth
    override fun getStyleableResDrawableLeftHeight(): Int = R.styleable.STShapeableButton_drawableLeftHeight
    override fun getStyleableResDrawableTopWidth(): Int = R.styleable.STShapeableButton_drawableTopWidth
    override fun getStyleableResDrawableTopHeight(): Int = R.styleable.STShapeableButton_drawableTopHeight
    override fun getStyleableResDrawableRightWidth(): Int = R.styleable.STShapeableButton_drawableRightWidth
    override fun getStyleableResDrawableRightHeight(): Int = R.styleable.STShapeableButton_drawableRightHeight
    override fun getStyleableResDrawableBottomWidth(): Int = R.styleable.STShapeableButton_drawableBottomWidth
    override fun getStyleableResDrawableBottomHeight(): Int = R.styleable.STShapeableButton_drawableBottomHeight
    override fun setEdgeDrawable(position: STEdgeDrawableHelper.Position, drawableResId: Int, width: Int, height: Int) = edgeDrawableHelper.setEdgeDrawable(position, drawableResId, width, height)
    override fun setEdgeDrawable(position: STEdgeDrawableHelper.Position, drawable: Drawable?, width: Int, height: Int) = edgeDrawableHelper.setEdgeDrawable(position, drawable, width, height)
    override fun setOnEdgeDrawableClickListener(onEdgeDrawableClickListener: ((STEdgeDrawableHelper.Position) -> Unit)?) = edgeDrawableHelper.setOnEdgeDrawableClickListener(onEdgeDrawableClickListener)
    //endregion

    init {
        shapeableHelper.init(attrs, defStyleAttr)
        edgeDrawableHelper.init(attrs, defStyleAttr)
    }

    companion object {
        init {
            // 如果在 xml 中未设置 style, 则默认使用这个 style
            STReflectUtil.setJavaStaticFieldValue(com.google.android.material.button.MaterialButton::class.java, "DEF_STYLE_RES", R.style.STButton_MaterialComponents_Text_Icon)
        }
    }
}