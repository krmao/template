package com.smart.library.widget.shapeable

import android.annotation.TargetApi
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.*
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewOutlineProvider
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.Dimension
import androidx.appcompat.content.res.AppCompatResources
import com.google.android.material.shape.*
import com.smart.library.R
import com.smart.library.util.STCustomViewUtil
import org.jetbrains.anko.displayMetrics


/**
 * 代码设置 https://medium.com/android-beginners/shapeableimageview-material-components-for-android-cac6edac2c0d
 * 主题设置 https://www.jianshu.com/p/43b9181bede0
 */
/** An TextView that draws the bitmap with the provided Shape.  */
@Suppress("MemberVisibilityCanBePrivate", "unused")
class STShapeableHelper(val view: View) : Shapeable, STShableableDelegate {
    private val pathProvider = ShapeAppearancePathProvider()
    private val destination: RectF = RectF()
    private val maskRect: RectF = RectF()
    private var borderPaint: Paint = Paint()
    private var clearPaint: Paint = Paint()
    private val path = Path()
    private var strokeColor: ColorStateList? = null
    private var shapeAppearanceModel: ShapeAppearanceModel? = null
    private var strokeInPadding = false

    @Dimension
    private var strokeWidth = 0f
    private var maskPath: Path = Path()
    private var shadowDrawable: MaterialShapeDrawable? = null

    override fun onDetachedFromWindow() {
        view.setLayerType(View.LAYER_TYPE_NONE, null)
        // super.onDetachedFromWindow()
    }

    override fun onAttachedToWindow() {
        // super.onAttachedToWindow()
        view.setLayerType(View.LAYER_TYPE_HARDWARE, null)
    }

    override fun onDraw(canvas: Canvas) {
        // super.onDraw(canvas)
        canvas.drawPath(maskPath, clearPaint)
        drawStroke(canvas)
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        // super.onSizeChanged(width, height, oldWidth, oldHeight)
        updateShapeMask(width, height)
    }

    override fun setShapeAppearanceModel(shapeAppearanceModel: ShapeAppearanceModel) {
        this.shapeAppearanceModel = shapeAppearanceModel
        shadowDrawable?.shapeAppearanceModel = shapeAppearanceModel
        updateShapeMask(view.width, view.height)
        view.invalidate()
    }

    override fun getShapeAppearanceModel(): ShapeAppearanceModel {
        return shapeAppearanceModel!!
        // return getShapeAppearanceModelWithViewHeightChanged()
    }

    private var fractionHeight = 0
    private val displayMetrics by lazy { view.context.displayMetrics }

    private fun updateShapeMask(width: Int, height: Int) {
        if (strokeInPadding) {
            destination[view.paddingLeft.toFloat(), view.paddingTop.toFloat(), (width - view.paddingRight).toFloat()] = (height - view.paddingBottom).toFloat()
        } else {
            val strokeSafePadding = strokeWidth / 2f
            destination[strokeSafePadding, strokeSafePadding, (width - strokeSafePadding)] = (height - strokeSafePadding)
        }
        pathProvider.calculatePath(getShapeAppearanceModel(), 1f /*interpolation*/, destination, path)
        // Remove path from rect to draw with clear paint.
        maskPath.rewind()
        maskPath.addPath(path)
        // Do not include padding to clip the background too.
        maskRect[0f, 0f, width.toFloat()] = height.toFloat()
        maskPath.addRect(maskRect, Path.Direction.CCW)

    }

    private fun drawStroke(canvas: Canvas) {
        if (strokeColor == null) {
            return
        }
        borderPaint.strokeWidth = strokeWidth
        val colorForState = strokeColor?.getColorForState(view.drawableState, strokeColor!!.defaultColor)
        if (strokeWidth > 0 && colorForState != Color.TRANSPARENT) {
            borderPaint.color = colorForState!!
            canvas.drawPath(path, borderPaint)
        }
    }

    /**
     * Sets the stroke color resource for this ImageView. Both stroke color and stroke width must be
     * set for a stroke to be drawn.
     *
     * @param strokeColorResourceId Color resource to use for the stroke.
     * @attr ref com.google.android.material.R.styleable#ShapeableImageView_strokeColor
     * @see .setStrokeColor
     * @see .getStrokeColor
     */
    override fun setStrokeColorResource(@ColorRes strokeColorResourceId: Int) {
        setStrokeColor(AppCompatResources.getColorStateList(view.context, strokeColorResourceId))
    }

    /**
     * Returns the stroke color for this ImageView.
     *
     * @attr ref com.google.android.material.R.styleable#ShapeableImageView_strokeColor
     * @see .setStrokeColor
     * @see .setStrokeColorResource
     */
    override fun getStrokeColor(): ColorStateList? {
        return strokeColor
    }

    /**
     * Sets the stroke width for this ImageView. Both stroke color and stroke width must be set for a
     * stroke to be drawn.
     *
     * @param strokeWidth Stroke width for this ImageView.
     * @attr ref com.google.android.material.R.styleable#ShapeableImageView_strokeWidth
     * @see .setStrokeWidthResource
     * @see .getStrokeWidth
     */
    override fun setStrokeWidth(@Dimension strokeWidth: Float) {
        if (this.strokeWidth != strokeWidth) {
            this.strokeWidth = strokeWidth
            view.invalidate()
        }
    }

    /**
     * Sets the stroke width dimension resource for this ImageView. Both stroke color and stroke width
     * must be set for a stroke to be drawn.
     *
     * @param strokeWidthResourceId Stroke width dimension resource for this ImageView.
     * @attr ref com.google.android.material.R.styleable#ShapeableImageView_strokeWidth
     * @see .setStrokeWidth
     * @see .getStrokeWidth
     */
    override fun setStrokeWidthResource(@DimenRes strokeWidthResourceId: Int) {
        setStrokeWidth(view.resources.getDimensionPixelSize(strokeWidthResourceId).toFloat())
    }

    /**
     * Gets the stroke width for this ImageView.
     *
     * @return Stroke width for this ImageView.
     * @attr ref com.google.android.material.R.styleable#ShapeableImageView_strokeWidth
     * @see .setStrokeWidth
     * @see .setStrokeWidthResource
     */
    @Dimension
    override fun getStrokeWidth(): Float {
        return strokeWidth
    }

    override fun setStrokeColor(strokeColor: ColorStateList?) {
        this.strokeColor = strokeColor
        view.invalidate()
    }

    @TargetApi(VERSION_CODES.LOLLIPOP)
    internal inner class OutlineProvider : ViewOutlineProvider() {
        private val rect = Rect()
        override fun getOutline(view: View, outline: Outline) {
            if (shapeAppearanceModel == null) {
                return
            }
            destination.round(rect)
            shadowDrawable?.bounds = rect
            shadowDrawable?.getOutline(outline)
        }
    }

    companion object {
        @JvmField
        val DEF_STYLE_RES = R.style.STShapeableAppearanceDefaultStyle
    }

    private fun peekValue(typedArray: TypedArray, index: Int): TypedValue {
        return typedArray.peekValue(index) ?: TypedValue()
    }

    fun init(attrs: AttributeSet?, defStyleAttr: Int) {
        // Ensure we are using the correctly themed context rather than the context that was passed in.
        val wrapContext = view.context
        clearPaint.isAntiAlias = true
        clearPaint.color = Color.WHITE
        clearPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
        val typedArray: TypedArray = wrapContext.obtainStyledAttributes(attrs, R.styleable.STShapeableAppearance, defStyleAttr, DEF_STYLE_RES)
        strokeColor = STCustomViewUtil.getColorStateList(wrapContext, typedArray, R.styleable.STShapeableAppearance_strokeColor)
        strokeWidth = typedArray.getDimensionPixelSize(R.styleable.STShapeableAppearance_strokeWidth, 0).toFloat()

        strokeInPadding = typedArray.getBoolean(R.styleable.STShapeableAppearance_strokeInPadding, strokeInPadding)
        borderPaint.style = Paint.Style.STROKE
        borderPaint.isAntiAlias = true

        val cornerFamily = typedArray.getInt(R.styleable.STShapeableAppearance_cornerFamily, CornerFamily.ROUNDED)
        val cornerFamilyBottomLeft = typedArray.getInt(R.styleable.STShapeableAppearance_cornerFamilyBottomLeft, cornerFamily)
        val cornerFamilyBottomRight = typedArray.getInt(R.styleable.STShapeableAppearance_cornerFamilyBottomRight, cornerFamily)
        val cornerFamilyTopLeft = typedArray.getInt(R.styleable.STShapeableAppearance_cornerFamilyTopLeft, cornerFamily)
        val cornerFamilyTopRight = typedArray.getInt(R.styleable.STShapeableAppearance_cornerFamilyTopRight, cornerFamily)

        val noDefinedCornerSize = AbsoluteCornerSize(-1000f)
        val cornerSize = STCustomViewUtil.getCornerSize(typedArray, R.styleable.STShapeableAppearance_cornerSize, noDefinedCornerSize)
        val cornerSizeBottomLeft = STCustomViewUtil.getCornerSize(typedArray, R.styleable.STShapeableAppearance_cornerSizeBottomLeft, cornerSize)
        val cornerSizeBottomRight = STCustomViewUtil.getCornerSize(typedArray, R.styleable.STShapeableAppearance_cornerSizeBottomRight, cornerSize)
        val cornerSizeTopLeft = STCustomViewUtil.getCornerSize(typedArray, R.styleable.STShapeableAppearance_cornerSizeTopLeft, cornerSize)
        val cornerSizeTopRight = STCustomViewUtil.getCornerSize(typedArray, R.styleable.STShapeableAppearance_cornerSizeTopRight, cornerSize)

        shapeAppearanceModel = ShapeAppearanceModel.builder(wrapContext, attrs, defStyleAttr, DEF_STYLE_RES).apply {
            // 如果自定义了 **app:cornerSize**, 则会覆盖 **shapeAppearanceOverlay**
            if (cornerSizeTopLeft != noDefinedCornerSize) setTopLeftCorner(cornerFamilyTopLeft, cornerSizeTopLeft)
            if (cornerSizeTopRight != noDefinedCornerSize) setTopRightCorner(cornerFamilyTopRight, cornerSizeTopRight)
            if (cornerSizeBottomRight != noDefinedCornerSize) setBottomRightCorner(cornerFamilyBottomRight, cornerSizeBottomRight)
            if (cornerSizeBottomLeft != noDefinedCornerSize) setBottomLeftCorner(cornerFamilyBottomLeft, cornerSizeBottomLeft)
        }
            .build().also { shadowDrawable = MaterialShapeDrawable(it) }

        if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
            view.outlineProvider = OutlineProvider()
        }
    }


}