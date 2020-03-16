package com.smart.template.home.animation.magic.captureanim

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.Interpolator
import com.smart.library.util.animation.STInterpolatorFactory

/**
 * 神奇效果
 * 窗口最小化时使用
 * @see {@link https://github.com/xinlyun/CaptureView}
 */
@Suppress("MemberVisibilityCanBePrivate", "unused")
class STMagicView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private var progress = 0f
    private var leftLineToXRatio: Float = 0.8f
    private var rightLineToXRatio: Float = 0.85f
    private var leftLineToYRatio: Float = 1.0f
    private var rightLineToYRatio: Float = 1.0f
    private var bitmap: Bitmap? = null
    private var meshHelper: STMeshHelper = STMeshHelper()

    private val defaultDuration: Long = 350
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
            meshHelper.setProgress(progress, leftLineToXRatio, rightLineToXRatio, leftLineToYRatio, rightLineToYRatio),
            0,
            null,
            0,
            defaultPaint
        )
    }

    fun setDuration(duration: Long = defaultDuration): STMagicView {
        animator.duration = duration
        return this
    }

    /**
     * 图片底部是否扭曲处理, 默认 true
     */
    @JvmOverloads
    fun enableBottomLineMesh(enableBottomLineMesh: Boolean = meshHelper.enableBottomLineMesh): Boolean {
        if (enableBottomLineMesh != meshHelper.enableBottomLineMesh) {
            meshHelper.enableBottomLineMesh = enableBottomLineMesh
        }
        return meshHelper.enableBottomLineMesh
    }

    fun isEnableBottomLineMesh(enableBottomLineMesh: Boolean): STMagicView {
        meshHelper.enableBottomLineMesh = enableBottomLineMesh
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

    /**
     * 无论 bitmap 有多大, 都将缩放绘制到 指定大小的空间里
     */
    fun setBitmap(bitmap: Bitmap, meshWidth: Int = meshHelper.meshWidth, meshHeight: Int = meshHelper.meshHeight, bitmapContainerWidth: Float = bitmap.width.toFloat(), bitmapContainerHeight: Float = bitmap.height.toFloat()): STMagicView {
        this.bitmap = bitmap
        meshHelper.init(meshWidth, meshHeight)
        meshHelper.setBitmapParams(bitmap.width.toFloat(), bitmap.height.toFloat(), bitmapContainerWidth, bitmapContainerHeight)
        return this
    }

    fun setLineRatio(leftLintToXRatio: Float = this.leftLineToXRatio, rightLineToXRatio: Float = this.rightLineToXRatio, leftLineToYRatio: Float = this.leftLineToYRatio, rightLineToYRatio: Float = this.rightLineToYRatio): STMagicView {
        this.leftLineToXRatio = leftLintToXRatio
        this.rightLineToXRatio = rightLineToXRatio
        this.leftLineToYRatio = leftLineToYRatio
        this.rightLineToYRatio = rightLineToYRatio
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
        if (isRunning() || isStarted()) {
            animator.cancel()
        }
    }

    /**
     * 动画立即停止在结束位置
     */
    fun end() {
        if (isRunning() || isStarted()) {
            animator.end()
        }
    }

    fun reset() {
        cancel()
        setProgress(0f)
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