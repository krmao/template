package com.smart.template.home.animation.magic.captureanim

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.Interpolator
import com.smart.library.base.toPxFromDp
import com.smart.library.util.STLogUtil
import com.smart.library.util.animation.STInterpolatorFactory
import kotlin.math.min

/**
 * 神奇效果
 * 窗口最小化时使用
 * @see {@link https://github.com/xinlyun/CaptureView}
 */
@Suppress("MemberVisibilityCanBePrivate", "unused")
class STMagicView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private var progress = 0f
    private var bitmap: Bitmap? = null
    private var meshHelper: STMeshHelper = STMeshHelper()

    private val defaultDuration: Long = 800
    private val defaultWidth: Int by lazy { 300.toPxFromDp() }  // 当 xml 中配置宽高为 wrap_content 时, 使用以下宽高
    private val defaultHeight: Int by lazy { 100.toPxFromDp() }
    private val defaultPaint = Paint().apply { isAntiAlias = true }
    private val defaultInterpolator: Interpolator = STInterpolatorFactory.createAccelerateDecelerateInterpolator()

    private var onProgressListener: ((progress: Float) -> Unit)? = null
    private val animator: ValueAnimator by lazy {
        ValueAnimator.ofFloat(0f, 1f).apply {
            duration = defaultDuration
            interpolator = defaultInterpolator
            addUpdateListener { animation ->
                val progress = animation.animatedValue as Float
                setProgress(progress)
                if (progress == 1f) {
                    onProgressListener?.invoke(progress)
                }
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val innerBitmap = bitmap ?: return

        /**
         * 扭曲图像, 使用网格绘制法
         *
         * bitmap：      将要扭曲的图像
         * meshWidth：   控制在横向上把该图像划成多少格
         * meshHeight：  控制在纵向上把该图像划成多少格
         * verts：       网格交叉点坐标数组，长度为(meshWidth + 1) * (meshHeight + 1) * 2
         * vertOffset：  控制verts数组中从第几个数组元素开始才对bitmap进行扭曲
         *
         * @see @{link https://www.jianshu.com/p/51d8dd99d27d}
         * @see @{link https://www.jianshu.com/p/11e6be1f18e6}
         */
        canvas.drawBitmapMesh(
            innerBitmap,
            meshHelper.meshWidth,
            meshHelper.meshHeight,
            meshHelper.setProgress(progress),
            0,
            null,
            0,
            defaultPaint
        )
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (!isInEditMode) {
            val measureWidth: Int = measureHandler(defaultWidth, widthMeasureSpec)
            val measureHeight: Int = measureHandler(defaultHeight, heightMeasureSpec)
            STLogUtil.d(TAG, "onMeasure measureWidth:$measureWidth  measureHeight:$measureHeight")
            meshHelper.init(measureWidth.toFloat(), measureHeight.toFloat())
            bitmap?.let { meshHelper.setBitmapSize(it.width.toFloat(), it.height.toFloat()) }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    /**
     * 不管父View是何模式，若子View有确切数值，则子View大小就是其本身大小，且mode是EXACTLY
     * 若子View是match_parent，则模式与父View相同，且大小同父View（若父View是UNSPECIFIED，则子View大小为0）
     * 若子View是wrap_content，则模式是AT_MOST，大小同父View，表示不可超过父View大小（若父View是UNSPECIFIED，则子View大小为0）
     * @see {@link https://www.jianshu.com/p/cecd0de7ec27}
     */
    private fun measureHandler(defaultSize: Int, measureSpec: Int): Int {
        val specSize = MeasureSpec.getSize(measureSpec)
        return when (MeasureSpec.getMode(measureSpec)) {
            MeasureSpec.EXACTLY -> specSize
            MeasureSpec.AT_MOST -> min(defaultSize, specSize)
            else -> defaultSize
        }
    }

    fun setDuration(duration: Long = defaultDuration): STMagicView {
        animator.duration = duration
        return this
    }

    fun setInterpolator(interpolator: Interpolator = defaultInterpolator): STMagicView {
        animator.interpolator = interpolator
        return this
    }

    fun setOnProgressListener(onProgressListener: ((progress: Float) -> Unit)?) {
        this.onProgressListener = onProgressListener
    }

    /**
     * @param progress [0, 1]
     */
    fun setProgress(progress: Float): STMagicView {
        this.progress = progress
        invalidate()
        return this
    }

    fun setBitmap(bitmap: Bitmap): STMagicView {
        this.bitmap = bitmap
        meshHelper.setBitmapSize(bitmap.width.toFloat(), bitmap.height.toFloat())
        return this
    }

    fun isRunning(): Boolean = animator.isRunning
    fun isStarted(): Boolean = animator.isStarted

    fun start() {
        if (isRunning() || isStarted()) {
            animator.cancel()
        }
        animator.start()
    }

    /**
     * 动画立即停止
     */
    fun cancel() {
        animator.cancel()
    }

    /**
     * 动画立即停止在结束位置
     */
    fun end() {
        animator.end()
    }

    fun resume() {
        animator.resume()
    }

    fun pause() {
        animator.pause()
    }

    companion object {
        const val TAG = "[MAGIC_VIEW]]"
    }
}