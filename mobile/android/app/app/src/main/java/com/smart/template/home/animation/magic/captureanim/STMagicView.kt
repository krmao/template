package com.smart.template.home.animation.magic.captureanim

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import com.smart.library.util.STLogUtil
import kotlin.math.min

/**
 * Created by linzx on 17-7-27.
 * for the Capture catch and exit anim
 */
class STMagicView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private var onAnimationListener: ((state: String) -> Unit)? = null
    private var width = 0f
    private var height = 0f
    private val acInterpolator = AccelerateDecelerateInterpolator()
    private val valueAnimator: ValueAnimator by lazy {
        ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 800
            interpolator = acInterpolator
            addUpdateListener { animation ->
                val posi = animation.animatedValue as Float
                setProgress(posi)
                if (posi == 1f) {
                    onAnimationListener?.invoke(ANIMATION_END)
                }
            }
        }
    }
    private var bitmap: Bitmap? = null
    private val paint = Paint().apply { isAntiAlias = true }
    private var meshHelper: STMeshHelper = STMeshHelper()
    private var posi = 0f

    fun setOnAnimationListener(onAnimationListener: ((state: String) -> Unit)?) {
        this.onAnimationListener = onAnimationListener
    }

    fun setBitmap(bitmap: Bitmap, width: Int = bitmap.width, height: Int = bitmap.height) {
        this.bitmap = bitmap
        meshHelper.setBitmapSize(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val innerBitmap = bitmap ?: return
        val mesh = meshHelper.setProgress(posi)
        canvas.drawBitmapMesh(innerBitmap, meshHelper.vetWidth, meshHelper.vetHeight, mesh, 0, null, 0, paint)
    }

    /**
     * 应用需求,需要根据在xml描述的宽高自适应故这么写,
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        width = measureHandler(1080, widthMeasureSpec).toFloat()
        height = measureHandler(1920, heightMeasureSpec).toFloat()
        STLogUtil.d(TAG, "onMeasure width:$width  height:$height")
        meshHelper.init(width.toInt(), (height * 0.95f).toInt())
        bitmap?.let { meshHelper.setBitmapSize(it.width, it.height) }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    private fun measureHandler(defaultSize: Int, measureSpec: Int): Int {
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        return when (specMode) {
            MeasureSpec.EXACTLY -> specSize
            MeasureSpec.AT_MOST -> min(defaultSize, specSize)
            else -> defaultSize
        }
    }

    fun setProgress(posi: Float) {
        this.posi = posi
        invalidate()
    }

    fun beginAnim() {
        valueAnimator.start()
    }

    companion object {
        private const val TAG = "[MAGIC_VIEW]]"
        const val ANIMATION_END = "ANIMATION_END"
    }
}