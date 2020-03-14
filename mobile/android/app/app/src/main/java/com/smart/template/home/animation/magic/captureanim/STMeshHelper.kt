package com.smart.template.home.animation.magic.captureanim

import android.view.animation.Interpolator
import com.smart.library.util.STLogUtil
import com.smart.library.util.animation.STInterpolatorFactory
import kotlin.math.cos

/**
 * 扭曲图像, 网格绘制帮助类
 * @see {@link https://github.com/xinlyun/CaptureView}
 * @see {@link https://www.jianshu.com/p/51d8dd99d27d}
 */
class STMeshHelper {

    private var bitmapContainerWidth: Float = 0f
    private var bitmapContainerHeight: Float = 0f

    private var bitmapFitWidth: Float = 0f
    private var bitmapFitHeight: Float = 0f

    /**
     * 将图像分隔为多少格
     * @see {@link https://www.jianshu.com/p/11e6be1f18e6}
     * @see {@link https://www.jianshu.com/p/51d8dd99d27d}
     */
    var meshWidth: Int = 50
        private set
    var meshHeight: Int = 50
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

        STLogUtil.d(STMagicView.TAG, "setBitmapParams bitmapAspectRatio=$bitmapAspectRatio, bitmapWidth:$bitmapWidth, bitmapHeight=$bitmapHeight")
        STLogUtil.d(STMagicView.TAG, "setBitmapParams bitmapContainerAspectRatio=$bitmapContainerAspectRatio, bitmapContainerWidth:$bitmapContainerWidth, bitmapContainerHeight=$bitmapContainerHeight")
        STLogUtil.d(STMagicView.TAG, "setBitmapParams bitmapFitAspectRatio=${bitmapFitWidth / bitmapFitHeight}")
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
        val cosValue = -1 * cos(Math.toRadians(180.0 * progress)) / 2f // [-1/2->0->1/2]
        val meshedBitmapBottomYInContainer: Float = bitmapFitHeight * (1 + cosValue * progress).toFloat() //底部先回弹再下降

        // 遍历网格数组, 给每一个网格所在顶点赋值根据左右两侧边线在当前进度时扭曲后的数值
        for (meshHeightIndex in 0..meshHeight) {
            for (meshWidthIndex in 0..meshWidth) {
                // 当前网格顶点宽度比例
                val meshWidthRatio: Float = meshWidthIndex / meshWidth.toFloat() // 一定要 float, 否则整除计算出错
                // 当前网格顶点高度比例
                val meshHeightRatio: Float = meshHeightIndex / meshHeight.toFloat() // 一定要 float, 否则整除计算出错

                // 图片最顶侧边线在当前进度与最底侧边线的距离
                val distanceFromTopLineToBottomLineInContainer: Float = meshedBitmapBottomYInContainer - meshedBitmapTopYInContainer
                // 计算出每个网格平均分摊(均匀分布)扭曲后的高度
                val meshedItemHeight: Float = distanceFromTopLineToBottomLineInContainer * meshHeightRatio
                // 当前网格顶点在当前进度时在 bitmapContainer 中所处的 Y 轴位置
                val meshedBitmapYInContainer: Float = meshedBitmapTopYInContainer + meshedItemHeight
                // 当前网格顶点在当前进度时在 bitmapContainer 中所处的 Y 轴位置, 计算图片扭曲后的位置这里使用图片 自适应 FIT 后的真实绘制宽高计算
                // val meshedBitmapYInContainer: Float = meshedBitmapTopYInContainer + bitmapFitHeight * meshHeightRatio * (1 - progress) // * (1 - progress) 是为了让图片下边线下降的更慢一些

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