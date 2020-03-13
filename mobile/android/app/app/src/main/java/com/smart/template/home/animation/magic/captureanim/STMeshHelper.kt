package com.smart.template.home.animation.magic.captureanim

import android.view.animation.Interpolator
import com.smart.library.util.STLogUtil
import com.smart.library.util.animation.STInterpolatorFactory

/**
 * 扭曲图像, 网格绘制帮助类
 * @see {@link https://github.com/xinlyun/CaptureView}
 * @see {@link https://www.jianshu.com/p/51d8dd99d27d}
 */
class STMeshHelper {

    private var viewMeasureWidth: Float = 0f
    private var viewMeasureHeight: Float = 0f
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

    fun init(viewMeasureWidth: Float, viewMeasureHeight: Float, meshWidth: Int = this.meshWidth, meshHeight: Int = this.meshHeight) {
        this.viewMeasureWidth = viewMeasureWidth
        this.viewMeasureHeight = viewMeasureHeight
        this.meshWidth = meshWidth
        this.meshHeight = meshHeight
    }

    /**
     * 输入图像宽高, 使得图像自适应 view 的宽高, 相当于 fitTop
     */
    fun setBitmapSize(bitmapWidth: Float, bitmapHeight: Float) {
        val innerViewMeasureWidth: Float = viewMeasureWidth
        val innerViewMeasureHeight: Float = viewMeasureHeight
        if (innerViewMeasureWidth == 0f || innerViewMeasureHeight == 0f || bitmapWidth == 0f || bitmapHeight == 0f) return

        val viewAspectRatio: Float = innerViewMeasureWidth / innerViewMeasureHeight
        val bitmapAspectRatio: Float = bitmapWidth / bitmapHeight

        when {
            bitmapAspectRatio > viewAspectRatio -> {
                this.bitmapFitWidth = innerViewMeasureWidth
                this.bitmapFitHeight = innerViewMeasureWidth / bitmapAspectRatio
            }
            bitmapAspectRatio < viewAspectRatio -> {
                this.bitmapFitWidth = innerViewMeasureHeight * bitmapAspectRatio
                this.bitmapFitHeight = innerViewMeasureHeight
            }
            else -> {
                this.bitmapFitWidth = innerViewMeasureWidth
                this.bitmapFitHeight = innerViewMeasureHeight
            }
        }
        STLogUtil.d(STMagicView.TAG, "setBitmapSize bitmapWidth:$bitmapWidth  bitmapHeight:$bitmapHeight, viewMeasureWidth=$viewMeasureWidth, viewMeasureHeight=$viewMeasureHeight, bitmapFitWidth=$bitmapFitWidth, bitmapFitHeight=$bitmapFitHeight, viewAspectRatio=$viewAspectRatio, bitmapAspectRatio=$bitmapAspectRatio")
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
    fun setProgress(progress: Float, leftLintToXRatio: Float = 0.85f, rightLineToXRatio: Float = 0.9f): FloatArray {
        //左右边线运动轨迹
        val leftLine: LineProgress
        val rightLine: LineProgress

        //在0~0.3f的部分,左右轨迹要逐渐向中心靠拢
        if (progress <= 0.3f) {
            // leftLine = LinePosi(0f, width * 0.1f * (progress / 0.3f), 0f, height)
            // rightLine = LinePosi(width, width * 0.2f + width * 0.8f * (0.3f - progress) / 0.3f, 0f, height)

            leftLine = LineProgress(0f, viewMeasureWidth * leftLintToXRatio * (progress / 0.3f), 0f, viewMeasureHeight)
            rightLine = LineProgress(viewMeasureWidth, viewMeasureWidth * (1 - (1 - rightLineToXRatio) * (progress / 0.3f)), 0f, viewMeasureHeight)
        } else {
            //在0.3f~1f,左右轨迹保持不变,图像按照此轨迹作为边界进行运动
            // leftLine = LinePosi(0f, width * 0.1f, 0f, height)
            // rightLine = LinePosi(width, width * 0.2f, 0f, height)
            leftLine = LineProgress(0f, viewMeasureWidth * leftLintToXRatio, 0f, viewMeasureHeight)
            rightLine = LineProgress(viewMeasureWidth, viewMeasureWidth * rightLineToXRatio, 0f, viewMeasureHeight)
        }

        // 首先计算出 这条线 在 view 中 Y 轴应该出现的位置
        val progressYInView: Float = viewMeasureHeight * progress

        // 初始化图片被分割为网格后的 verts 数组
        val verts = FloatArray((meshWidth + 1) * (meshHeight + 1) * 2)
        var num = 0
        for (meshHeightIndex in 0..meshHeight) {
            for (meshWidthIndex in 0..meshWidth) {
                // 计算出扭曲后的 bitmap 网格 Y 坐标
                val progressYInBitmap: Float = progressYInView + bitmapFitHeight * meshHeightIndex / meshHeight

                val progressLeftLineXInBitmap: Float = leftLine.calculateProgressXByProgressY(progressYInBitmap)
                val progressRightLineXInBitmap: Float = rightLine.calculateProgressXByProgressY(progressYInBitmap)
                val progressDistanceXInBitmap: Float = progressRightLineXInBitmap - progressLeftLineXInBitmap

                // 当前 bitmap 网格宽度比例
                val meshWidthRatio: Float = meshWidthIndex / meshWidth.toFloat() // 一定要 float, 否则整除计算出错
                // 计算出扭曲后的 bitmap 网格 X 坐标
                val progressXInBitmap: Float = progressLeftLineXInBitmap + progressDistanceXInBitmap * meshWidthRatio

                verts[num++] = progressXInBitmap
                verts[num++] = progressYInBitmap
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
    private inner class LineProgress(private val fromX: Float, private val toX: Float, private val fromY: Float, private val toY: Float) {

        // 这条线 Y 轴起点到终点总跨越长度
        private val distanceY = toY - fromY

        // 这条线 X 轴起点到终点总跨越长度
        private val distanceX = toX - fromX

        /**
         * 根据这条线 当前 Y 轴已经移动的距离 计算出与 X 轴应该出现的位置
         */
        fun calculateProgressXByProgressY(progressY: Float): Float {
            // 根据这条线 当前 Y 轴已经移动的距离 计算出与 Y 轴总长度对应的比例
            val ratioY = progressY / distanceY
            // 通过插值器获取这条线 X 轴对应应该移动的比例
            val ratioX = fastOutSlowInInterpolator.getInterpolation(ratioY)
            // 计算这条线 X 轴应该出现的位置
            return fromX + distanceX * ratioX
        }
    }
}