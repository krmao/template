package com.smart.template.home.animation.magic.captureanim

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import com.smart.library.base.toPxFromDp
import com.smart.library.util.STLogUtil
import kotlin.math.min

/**
 * for the Capture catch and exit anim
 * https://github.com/xinlyun/CaptureView
 */
class STMagicView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private var onAnimationListener: ((state: String) -> Unit)? = null
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
    private var progress = 0f

    fun setOnAnimationListener(onAnimationListener: ((state: String) -> Unit)?) {
        this.onAnimationListener = onAnimationListener
    }

    fun setBitmap(bitmap: Bitmap) {
        this.bitmap = bitmap
        meshHelper.setBitmapSize(bitmap.width, bitmap.height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val innerBitmap = bitmap ?: return
        val verts = meshHelper.setProgress(progress)
        /**
         * bitmap：将要扭曲的图像
         * meshWidth：控制在横向上把该图像划成多少格
         * meshHeight：控制在纵向上把该图像划成多少格
         * verts：网格交叉点坐标数组，长度为(meshWidth + 1) * (meshHeight + 1) * 2
         * vertOffset：控制verts数组中从第几个数组元素开始才对bitmap进行扭曲

         *  https://www.jianshu.com/p/51d8dd99d27d
         * https://www.jianshu.com/p/11e6be1f18e6
         */
        canvas.drawBitmapMesh(
            innerBitmap,
            meshHelper.meshWidth,
            meshHelper.meshHeight,
            verts,
            0,
            null,
            0,
            paint
        )
    }

    // 当 xml 中配置宽高为 wrap_content 时, 使用以下宽高
    private val wrapWidth: Int by lazy { 300.toPxFromDp() }
    private val wrapHeight: Int by lazy { 300.toPxFromDp() }

    /**
     * 应用需求,需要根据在xml描述的宽高自适应故这么写,
     *
     * 不管父View是何模式，若子View有确切数值，则子View大小就是其本身大小，且mode是EXACTLY
     * 若子View是match_parent，则模式与父View相同，且大小同父View（若父View是UNSPECIFIED，则子View大小为0）
     * 若子View是wrap_content，则模式是AT_MOST，大小同父View，表示不可超过父View大小（若父View是UNSPECIFIED，则子View大小为0）
     * 链接：https://www.jianshu.com/p/cecd0de7ec27
     *
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (!isInEditMode) {
            val measureWidth: Int = measureHandler(wrapWidth, widthMeasureSpec)
            val measureHeight: Int = measureHandler(wrapHeight, heightMeasureSpec)
            STLogUtil.d(TAG, "onMeasure measureWidth:$measureWidth  measureHeight:$measureHeight")
            meshHelper.init(measureWidth.toFloat(), measureHeight.toFloat())
            bitmap?.let { meshHelper.setBitmapSize(it.width, it.height) }
        }
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
        this.progress = posi
        invalidate()
    }

    fun beginAnim() {
        valueAnimator.start()
    }

    companion object {
        const val TAG = "[MAGIC_VIEW]]"
        const val ANIMATION_END = "ANIMATION_END"
    }
}