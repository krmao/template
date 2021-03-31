package com.smart.library.widget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.Interpolator
import androidx.annotation.Keep
import com.smart.library.util.STLogUtil
import com.smart.library.util.animation.STInterpolatorFactory

/**
 * 神奇效果
 * 窗口最小化时使用
 * @see {@link https://github.com/xinlyun/CaptureView}
 */
@Suppress("MemberVisibilityCanBePrivate", "unused")
@Keep
class STMagicView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private var progress = 0f
    private var leftLineToXRatio: Float = 0.8f
    private var rightLineToXRatio: Float = 0.85f
    private var leftLineToYRatio: Float = 1.0f
    private var rightLineToYRatio: Float = 1.0f
    var bitmap: Bitmap? = null
        private set
    private var meshHelper: MeshHelper = MeshHelper()

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
                onProgressListener?.invoke(progress)
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
        postInvalidate()
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

    /**
     * 扭曲图像, 网格绘制帮助类
     * @see {@link https://github.com/xinlyun/CaptureView}
     * @see {@link https://www.jianshu.com/p/51d8dd99d27d}
     */
    private class MeshHelper {

        private var bitmapContainerWidth: Float = 0f
        private var bitmapContainerHeight: Float = 0f

        private var bitmapFitWidth: Float = 0f
        private var bitmapFitHeight: Float = 0f

        /**
         * 将图像分隔为多少格
         * @see {@link https://www.jianshu.com/p/11e6be1f18e6}
         * @see {@link https://www.jianshu.com/p/51d8dd99d27d}
         */
        var meshWidth: Int = 30
            private set
        var meshHeight: Int = 30
            private set

        fun init(meshWidth: Int = this.meshWidth, meshHeight: Int = this.meshHeight) {
            this.meshWidth = meshWidth
            this.meshHeight = meshHeight
        }

        /**
         * 计算出 bitmap 在容器中经过缩放自适应后的真实宽高
         */
        fun setBitmapParams(bitmapWidth: Float, bitmapHeight: Float, bitmapContainerWidth: Float, bitmapContainerHeight: Float) {
            this.bitmapContainerWidth = bitmapContainerWidth
            this.bitmapContainerHeight = bitmapContainerHeight
            if (bitmapWidth <= 0f || bitmapHeight <= 0f || bitmapContainerWidth <= 0f || bitmapContainerHeight <= 0f) return

            val bitmapAspectRatio: Float = bitmapWidth / bitmapHeight
            val bitmapContainerAspectRatio: Float = bitmapContainerWidth / bitmapContainerHeight

            when {
                bitmapAspectRatio > bitmapContainerAspectRatio -> {
                    this.bitmapFitWidth = bitmapContainerWidth
                    this.bitmapFitHeight = bitmapContainerWidth / bitmapAspectRatio
                }
                bitmapAspectRatio < bitmapContainerAspectRatio -> {
                    this.bitmapFitWidth = bitmapContainerHeight * bitmapAspectRatio
                    this.bitmapFitHeight = bitmapContainerHeight
                }
                else -> {
                    this.bitmapFitWidth = bitmapContainerWidth
                    this.bitmapFitHeight = bitmapContainerHeight
                }
            }
            STLogUtil.sync {
                STLogUtil.d(TAG, "setBitmapParams bitmapAspectRatio=$bitmapAspectRatio, bitmapWidth:$bitmapWidth, bitmapHeight=$bitmapHeight")
                STLogUtil.d(TAG, "setBitmapParams bitmapContainerAspectRatio=$bitmapContainerAspectRatio, bitmapContainerWidth:$bitmapContainerWidth, bitmapContainerHeight=$bitmapContainerHeight")
                STLogUtil.d(TAG, "setBitmapParams bitmapFitAspectRatio=${bitmapFitWidth / bitmapFitHeight}")
            }
        }

        /**
         * | ------------------------------
         * | |                          ||
         * | \                         / |
         * |  |                       /  |
         * |  |                      /   |
         * |  | leftLine            /    |
         * |  \                    /     |
         * |   |                  /      |
         * |   |                 /       |
         * |   |                /        |
         * |   |               /         |
         * |   \              /          |
         * |    |            /rightLine  |
         * |    |           /            |
         * | ->->|          / <-<-<-<
         * | 0.1f        0.3f
         * | leftLine    rightLine
         * @param progress
         * @return verts 图片被分割成网格的坐标数组 x,y,x,y ...
         */
        fun setProgress(progress: Float, leftLintToXRatio: Float = 0.8f, rightLineToXRatio: Float = 0.85f, leftLineToYRatio: Float = 1.5f, rightLineToYRatio: Float = 1.5f): FloatArray {
            // 左右边线运动轨迹
            val leftLine: Line
            val rightLine: Line

            // 在0~0.3f的部分,左右轨迹要逐渐向中心靠拢
            if (progress <= 0.3f) {
                leftLine = Line(0f, bitmapContainerWidth * leftLintToXRatio * (progress / 0.3f), 0f, bitmapContainerHeight * leftLineToYRatio)
                rightLine = Line(bitmapContainerWidth, bitmapContainerWidth * (1 - (1 - rightLineToXRatio) * (progress / 0.3f)), 0f, bitmapContainerHeight * rightLineToYRatio)
            } else {
                // 在0.3f~1f,左右轨迹保持不变,图像按照此轨迹作为边界进行运动
                leftLine = Line(0f, bitmapContainerWidth * leftLintToXRatio, 0f, bitmapContainerHeight * leftLineToYRatio)
                rightLine = Line(bitmapContainerWidth, bitmapContainerWidth * rightLineToXRatio, 0f, bitmapContainerHeight * rightLineToYRatio)
            }

            //==========================================================================================================================================================================================
            // 上面是精确构建出左右两条线在当前进度时所处状态
            //
            // 下面是构建出当前进度图片根据左右两条线扭曲的网格向量数组
            // 由于可以确定图片下移的高度, 所以图片上边线下移后所在 bitmapContainer 中的位置 Y 轴可以确定
            //==========================================================================================================================================================================================

            // verts 图片扭曲后的网格向量数组, [x0,y0,x1,y1 ... xn,yn]
            val verts = FloatArray((meshWidth + 1) * (meshHeight + 1) * 2)
            // 网格数组的下标
            var vertIndex = 0

            // 图片顶部边线在 bitmapContainer 中所处的 Y 轴位置, 图片最上面的位置, 下面每一个网格基于这个位置进行计算位置
            // 此处使用 bitmapContainerHeight 而不是 bitmapFitHeight 是为了图片可以超出自身最底部区域绘制, 当bitmapFitHeight < bitmapContainerHeight 时起作用
            val meshedBitmapTopYInContainer: Float = bitmapContainerHeight * progress

            // 遍历网格数组, 给每一个网格所在顶点赋值根据左右两侧边线在当前进度时扭曲后的数值
            for (meshHeightIndex in 0..meshHeight) {
                // 当前网格顶点高度比例
                val meshHeightRatio: Float = meshHeightIndex / meshHeight.toFloat() // 一定要 float, 否则整除计算出错 [0,1]
                for (meshWidthIndex in 0..meshWidth) {
                    // 当前网格顶点宽度比例
                    val meshWidthRatio: Float = meshWidthIndex / meshWidth.toFloat() // 一定要 float, 否则整除计算出错 [0,1]
                    // 当前网格顶点在当前进度时在 bitmapContainer 中所处的 Y 轴位置, 计算图片扭曲后的位置这里使用图片 自适应 FIT 后的真实绘制宽高计算
                    val meshedBitmapBottomYInContainer: Float = if (this.enableBottomLineMesh) {
                        when {
                            leftLintToXRatio > 0.5f -> meshedBitmapTopYInContainer + bitmapFitHeight * meshHeightRatio * (1 - progress) * (1 + bottomYInterpolator.getInterpolation(meshWidthRatio) * progress)
                            rightLineToXRatio < 0.5f -> meshedBitmapTopYInContainer + bitmapFitHeight * meshHeightRatio * (1 - progress) * (1 + bottomYInterpolator.getInterpolation(1 - meshWidthRatio) * progress)
                            else -> meshedBitmapTopYInContainer + bitmapFitHeight * meshHeightRatio * (1 - progress)
                        }
                    } else {
                        meshedBitmapTopYInContainer + bitmapFitHeight * meshHeightRatio * (1 - progress)
                    }
                    // 图片最顶侧边线在当前进度与最底侧边线的距离
                    val distanceFromTopLineToBottomLineInContainer: Float = meshedBitmapBottomYInContainer - meshedBitmapTopYInContainer
                    // 计算出每个网格平均分摊(均匀分布)扭曲后的高度
                    val meshedItemHeight: Float = distanceFromTopLineToBottomLineInContainer * meshHeightRatio
                    // 当前网格顶点在当前进度时在 bitmapContainer 中所处的 Y 轴位置
                    val meshedBitmapYInContainer: Float = meshedBitmapTopYInContainer + meshedItemHeight

                    // 图片最左侧边线在当前进度所处的 X 轴位置
                    val leftLineXInContainer: Float = leftLine.getOutputValue(meshedBitmapYInContainer)
                    // 图片最右侧边线在当前进度所处的 X 轴位置
                    val rightLineXInContainer: Float = rightLine.getOutputValue(meshedBitmapYInContainer)
                    // 图片最左侧边线在当前进度与最右侧边线的距离
                    val distanceFromLeftLineToRightLineInContainer: Float = rightLineXInContainer - leftLineXInContainer
                    // 计算出每个网格平均分摊(均匀分布)扭曲后的宽度
                    val meshedItemWidth: Float = distanceFromLeftLineToRightLineInContainer * meshWidthRatio
                    // 当前网格顶点在当前进度时在 bitmapContainer 中所处的 X 轴位置
                    val meshedBitmapXInContainer: Float = leftLineXInContainer + meshedItemWidth

                    // 为扭曲后的网格顶点赋值
                    verts[vertIndex++] = meshedBitmapXInContainer
                    verts[vertIndex++] = meshedBitmapYInContainer
                }
            }
            return verts
        }

        var enableBottomLineMesh: Boolean = true
        private val bottomYInterpolator: Interpolator by lazy { STInterpolatorFactory.createAnticipateOvershootInterpolator(1f, 1.5f) } // 从 0f -> 1f 的过程是 先加速再减速
        private val fastOutSlowInInterpolator: Interpolator by lazy { STInterpolatorFactory.createFastOutSlowInInterpolator() } // 从 0f -> 1f 的过程是 先加速再减速

        /**
         * 描述一条线的运动轨迹, 比如左侧边界线, 右侧边界线
         *
         * @param fromX 曲线起点 X 轴坐标
         * @param fromY 曲线起点 Y 轴坐标
         * @param toX   曲线终点 X 轴坐标
         * @param toY   曲线终点 Y 轴坐标
         */
        private inner class Line(val fromX: Float, val toX: Float, val fromY: Float, val toY: Float) {

            // 这条线 Y 轴起点到终点总跨越长度
            private val distanceY = toY - fromY

            // 这条线 X 轴起点到终点总跨越长度
            private val distanceX = toX - fromX

            /**
             * 根据这条线 当前 Y 轴已经移动的距离 计算出与 X 轴应该出现的位置
             */
            fun getOutputValue(inputValue: Float): Float {
                // 根据这条线 当前 Y 轴已经移动的距离 计算出与 Y 轴总长度对应的比例
                val ratioY = inputValue / distanceY
                // 通过插值器获取这条线 X 轴对应应该移动的比例
                val ratioX = fastOutSlowInInterpolator.getInterpolation(ratioY)
                // 计算这条线 X 轴应该出现的位置
                return fromX + distanceX * ratioX
            }
        }
    }

    companion object {
        const val TAG = "[MAGIC_VIEW]]"
    }
}