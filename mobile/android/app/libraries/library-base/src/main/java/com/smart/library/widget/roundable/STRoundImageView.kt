package com.smart.library.widget.roundable

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import androidx.annotation.ColorRes
import androidx.appcompat.widget.AppCompatImageView
import com.smart.library.R
import kotlin.math.max
import kotlin.math.min

/**
 * 自定义的圆角矩形ImageView
 */
@Suppress("unused")
class STRoundImageView : AppCompatImageView {

    companion object {
        const val TYPE_CIRCLE = 0
        const val TYPE_ROUND = 1
        const val TYPE_OVAL = 2
    }

    /**
     * 图片的类型，圆形or圆角
     */
    private var type: Int = TYPE_ROUND
    /**
     * 描边的颜色、宽度
     */
    private var mBorderColor: Int = Color.WHITE
    private var mProgressColor: Int = Color.WHITE

    private var mBorderWidth: Float = 0f
        set(value) {
            field = value
            mCircleRect = RectF(mBorderWidth / 2, mBorderWidth / 2, mRadius * 2 + mBorderWidth / 2, mRadius * 2 + mBorderWidth / 2)
        }
    /**
     * 圆角的半径
     */
    private var mRadius: Float = 0f
        set(value) {
            field = value
            mCircleRect = RectF(mBorderWidth / 2, mBorderWidth / 2, mRadius * 2 + mBorderWidth / 2, mRadius * 2 + mBorderWidth / 2)
        }

    private var mCircleRect: RectF = RectF(mBorderWidth / 2, mBorderWidth / 2, mRadius * 2 + mBorderWidth / 2, mRadius * 2 + mBorderWidth / 2)
    /**
     * 圆角的大小
     */
    private var mCornerRadius: Float = dp2px(10).toFloat()
    //左上角圆角大小
    private var mLeftTopCornerRadius: Float = 0f
    //右上角圆角大小
    private var mRightTopCornerRadius: Float = 0f
    //左下角圆角大小
    private var mLeftBottomCornerRadius: Float = 0f
    //右下角圆角大小
    private var mRightBottomCornerRadius: Float = 0f

    /**
     * 绘图的Paint
     */
    private var mBitmapPaint = Paint().apply { isAntiAlias = true }
    private var mBorderPaint = Paint().apply { isAntiAlias = true;style = Paint.Style.STROKE;strokeCap = Paint.Cap.ROUND }
    /**
     * 3x3 矩阵，主要用于缩小放大
     */
    private var mMatrix = Matrix()
    /**
     * 渲染图像，使用图像为绘制图形着色
     */
    private var mBitmapShader: BitmapShader? = null
    /**
     * view的宽度
     */
    private var mWidth: Int = 0
    /**
     * 圆角图片区域
     */
    private var mRoundRect: RectF? = null
    private var mRoundPath = Path()
    /**
     * 从-90开始，范围0~360
     */
    private var mBorderProgress: Int = 0


    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.STRoundImageView, defStyle, 0)
        type = typedArray.getInt(R.styleable.STRoundImageView_stType, type)
        mBorderColor = typedArray.getColor(R.styleable.STRoundImageView_stBorderColor, mBorderColor)
        mBorderWidth = typedArray.getDimension(R.styleable.STRoundImageView_stBorderWidth, mBorderWidth)
        mCornerRadius = typedArray.getDimension(R.styleable.STRoundImageView_stCornerRadius, mCornerRadius)
        mLeftTopCornerRadius = typedArray.getDimension(R.styleable.STRoundImageView_stLeftTopCornerRadius, mLeftTopCornerRadius)
        mLeftBottomCornerRadius = typedArray.getDimension(R.styleable.STRoundImageView_stLeftBottomCornerRadius, mLeftBottomCornerRadius)
        mRightTopCornerRadius = typedArray.getDimension(R.styleable.STRoundImageView_stRightTopCornerRadius, mRightTopCornerRadius)
        mRightBottomCornerRadius = typedArray.getDimension(R.styleable.STRoundImageView_stRightBottomCornerRadius, mRightBottomCornerRadius)
        mProgressColor = typedArray.getColor(R.styleable.STRoundImageView_stProgressColor, mProgressColor)
        mBorderProgress = typedArray.getInteger(R.styleable.STRoundImageView_stProgressBorder, mBorderProgress)
        typedArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        /**
         * 如果类型是圆形，则强制改变view的宽高一致，以小值为准
         */
        if (type == TYPE_CIRCLE) {
            mWidth = min(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec))
            mRadius = mWidth / 2 - mBorderWidth / 2
            setMeasuredDimension(mWidth, mWidth)
        }

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        // 圆角图片的范围
        if (type == TYPE_ROUND || type == TYPE_OVAL) {
            mRoundRect = RectF(mBorderWidth / 2, mBorderWidth / 2, w - mBorderWidth / 2, h - mBorderWidth / 2)
        }
    }

    override fun onDraw(canvas: Canvas) {

        mBorderPaint.color = mBorderColor
        mBorderPaint.strokeWidth = mBorderWidth

        if (drawable == null) {
            return
        }
        setUpShader()

        when (type) {
            TYPE_ROUND -> {
                setRoundPath()
                canvas.drawPath(mRoundPath, mBitmapPaint)
                //绘制描边
                canvas.drawPath(mRoundPath, mBorderPaint)
            }
            TYPE_CIRCLE -> {
                canvas.drawCircle(mRadius + mBorderWidth / 2, mRadius + mBorderWidth / 2, mRadius, mBitmapPaint)
                //绘制描边
                canvas.drawCircle(mRadius + mBorderWidth / 2, mRadius + mBorderWidth / 2, mRadius, mBorderPaint)
                //进度
                mBorderPaint.color = mProgressColor
                canvas.drawArc(mCircleRect, -90f, mBorderProgress.toFloat(), false, mBorderPaint)
            }
            else -> {
                if (mRoundRect != null) {
                    canvas.drawOval(mRoundRect!!, mBitmapPaint)
                    canvas.drawOval(mRoundRect!!, mBorderPaint)
                }
            }
        }
    }

    private fun setRoundPath() {
        mRoundPath.reset()

        /**
         * 如果四个圆角大小都是默认值0，
         * 则将四个圆角大小设置为mCornerRadius的值
         */
        if (mLeftTopCornerRadius == 0f &&
            mLeftBottomCornerRadius == 0f &&
            mRightTopCornerRadius == 0f &&
            mRightBottomCornerRadius == 0f
        ) {

            mRoundRect?.let {
                mRoundPath.addRoundRect(
                    it,
                    floatArrayOf(mCornerRadius, mCornerRadius, mCornerRadius, mCornerRadius, mCornerRadius, mCornerRadius, mCornerRadius, mCornerRadius),
                    Path.Direction.CW
                )
            }


        } else {
            mRoundRect?.let {
                mRoundPath.addRoundRect(
                    it,
                    floatArrayOf(mLeftTopCornerRadius, mLeftTopCornerRadius, mRightTopCornerRadius, mRightTopCornerRadius, mRightBottomCornerRadius, mRightBottomCornerRadius, mLeftBottomCornerRadius, mLeftBottomCornerRadius),
                    Path.Direction.CW
                )
            }
        }

    }

    /**
     * 初始化BitmapShader
     */
    private fun setUpShader() {
        val tmpDrawable = drawable ?: return
        if (mBitmapShader == null) {
            val bmp = drawableToBitmap(tmpDrawable)
            if (bmp != null) {
                // 将bmp作为着色器，就是在指定区域内绘制bmp
                mBitmapShader = BitmapShader(bmp, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
                var scale = 1.0f
                if (type == TYPE_CIRCLE) {
                    // 拿到bitmap宽或高的小值
                    val bSize = min(bmp.width, bmp.height)
                    scale = mWidth * 1.0f / bSize
                    //使缩放后的图片居中
                    val dx = (bmp.width * scale - mWidth) / 2
                    val dy = (bmp.height * scale - mWidth) / 2
                    mMatrix.setTranslate(-dx, -dy)

                } else if (type == TYPE_ROUND || type == TYPE_OVAL) {

                    if (!(bmp.width == width && bmp.height == height)) {
                        // 如果图片的宽或者高与view的宽高不匹配，计算出需要缩放的比例；缩放后的图片的宽高，一定要大于我们view的宽高；所以我们这里取大值；
                        scale = max(
                            width * 1.0f / bmp.width,
                            height * 1.0f / bmp.height
                        )
                        //使缩放后的图片居中
                        val dx = (scale * bmp.width - width) / 2
                        val dy = (scale * bmp.height - height) / 2
                        mMatrix.setTranslate(-dx, -dy)
                    }
                }
                // shader的变换矩阵，我们这里主要用于放大或者缩小
                mMatrix.preScale(scale, scale)
                mBitmapShader?.setLocalMatrix(mMatrix)
                // 设置变换矩阵
                mBitmapShader?.setLocalMatrix(mMatrix)
                // 设置shader
                mBitmapPaint.shader = mBitmapShader
            }
        }
    }


    /**
     * drawable转bitmap
     */
    @Suppress("DEPRECATION")
    private fun drawableToBitmap(drawable: Drawable): Bitmap? {
        try {
            val bitmap: Bitmap
            if (drawable is BitmapDrawable) {
                return drawable.bitmap
            }
            val w = drawable.intrinsicWidth
            val h = drawable.intrinsicHeight
            bitmap = Bitmap.createBitmap(
                w, h, if (drawable.opacity != PixelFormat.OPAQUE)
                    Bitmap.Config.ARGB_4444
                else
                    Bitmap.Config.RGB_565
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, w, h)
            drawable.draw(canvas)
            return bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

    }

    /**
     * 设置图片类型:
     * imageType=0 圆形图片
     * imageType=1 圆角图片
     * 默认为圆形图片
     */
    fun setType(imageType: Int): STRoundImageView {
        if (this.type != imageType) {
            this.type = imageType
            if (this.type != TYPE_ROUND && this.type != TYPE_CIRCLE && this.type != TYPE_OVAL) {
                this.type = TYPE_OVAL
            }
            requestLayout()
        }
        return this
    }

    /**
     * 设置圆角图片的圆角大小
     */
    fun setCornerRadius(cornerRadius: Int): STRoundImageView {
        val radius = dp2px(cornerRadius)
        if (mCornerRadius != radius.toFloat()) {
            mCornerRadius = radius.toFloat()
            invalidate()
        }
        return this
    }

    /**
     * 设置圆角图片的左上圆角大小
     */
    fun setLeftTopCornerRadius(cornerRadius: Int): STRoundImageView {
        val radius = dp2px(cornerRadius)
        if (mLeftTopCornerRadius != radius.toFloat()) {
            mLeftTopCornerRadius = radius.toFloat()
            invalidate()
        }
        return this
    }

    /**
     * 设置圆角图片的右上圆角大小
     */
    fun setRightTopCornerRadius(cornerRadius: Int): STRoundImageView {
        val radius = dp2px(cornerRadius)
        if (mRightTopCornerRadius != radius.toFloat()) {
            mRightTopCornerRadius = radius.toFloat()
            invalidate()
        }
        return this
    }

    /**
     * 设置圆角图片的左下圆角大小
     */
    fun setLeftBottomCornerRadius(cornerRadius: Int): STRoundImageView {
        val radius = dp2px(cornerRadius)
        if (mLeftBottomCornerRadius != radius.toFloat()) {
            mLeftBottomCornerRadius = radius.toFloat()
            invalidate()
        }
        return this
    }

    /**
     * 设置圆角图片的右下圆角大小
     */
    fun setRightBottomCornerRadius(cornerRadius: Int): STRoundImageView {
        val radius = dp2px(cornerRadius)
        if (mRightBottomCornerRadius != radius.toFloat()) {
            mRightBottomCornerRadius = radius.toFloat()
            invalidate()
        }

        return this
    }

    /**
     * 设置描边宽度
     */
    fun setBorderWidth(borderWidth: Int): STRoundImageView {
        val width = dp2px(borderWidth)
        if (mBorderWidth != width.toFloat()) {
            mBorderWidth = width.toFloat()
            invalidate()
        }

        return this
    }

    /**
     * 设置描边颜色
     */
    fun setBorderColor(borderColor: Int): STRoundImageView {
        if (mBorderColor != borderColor) {
            mBorderColor = borderColor
            invalidate()
        }

        return this
    }

    private fun dp2px(dpVal: Int): Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal.toFloat(), resources.displayMetrics).toInt()

    fun setProgress(progress: Int, @ColorRes color: Int) {
        mBorderProgress = progress
        mProgressColor = color
        invalidate()
    }

}
