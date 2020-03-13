package com.smart.template.home.animation.magic.captureanim

import android.view.animation.Interpolator
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.smart.library.util.STLogUtil
import com.smart.library.util.animation.STInterpolatorFactory

/**
 * 供给CaptureAnimView的支持类,核心数据计算均在此处
 * https://github.com/xinlyun/CaptureView
 * https://www.jianshu.com/p/51d8dd99d27d
 */
class STMeshHelper {

    private var viewMeasureWidth: Float = 0f
    private var viewMeasureHeight: Float = 0f
    private var bitmapFitWidth: Float = 0f
    private var bitmapFitHeight: Float = 0f

    /**
     * 网格坐标
     * https://www.jianshu.com/p/11e6be1f18e6
     * https://www.jianshu.com/p/51d8dd99d27d
     */
    var meshWidth: Int = 40
        private set
    var meshHeight: Int = 40
        private set

    fun init(viewMeasureWidth: Float, viewMeasureHeight: Float, meshWidth: Int = 80, meshHeight: Int = 80) {
        this.viewMeasureWidth = viewMeasureWidth
        this.viewMeasureHeight = viewMeasureHeight
        this.meshWidth = meshWidth
        this.meshHeight = meshHeight
    }

    fun setBitmapSize(bitmapWidth: Int, bitmapHeight: Int) {
        this.bitmapFitWidth = viewMeasureWidth
        this.bitmapFitHeight = viewMeasureWidth / bitmapWidth * bitmapHeight
        STLogUtil.d(STMagicView.TAG, "setBitmapSize bitmapWidth:$bitmapWidth  bitmapHeight:$bitmapHeight, viewMeasureWidth=$viewMeasureWidth, viewMeasureHeight=$viewMeasureHeight, bitmapFitWidth=$bitmapFitWidth, bitmapFitHeight=$bitmapFitHeight")
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
     * |    |          /
     * | 0.1f        0.3f
     * |
     * @param progress
     * @return
     */
    fun setProgress(progress: Float, leftToX: Float = 0.85f, rightToX: Float = 0.9f): FloatArray {
        //左右边线运动轨迹
        val leftLine: LineProgress
        val rightLine: LineProgress

        //在0~0.3f的部分,左右轨迹要逐渐向中心靠拢
        if (progress <= 0.3f) {
            // leftLine = LinePosi(0f, width * 0.1f * (progress / 0.3f), 0f, height)
            // rightLine = LinePosi(width, width * 0.2f + width * 0.8f * (0.3f - progress) / 0.3f, 0f, height)

            leftLine = LineProgress(0f, viewMeasureWidth * leftToX * (progress / 0.3f), 0f, viewMeasureHeight)
            rightLine = LineProgress(viewMeasureWidth, viewMeasureWidth * (1 - (1 - rightToX) * (progress / 0.3f)), 0f, viewMeasureHeight)
        } else {
            //在0.3f~1f,左右轨迹保持不变,图像按照此轨迹作为边界进行运动
            // leftLine = LinePosi(0f, width * 0.1f, 0f, height)
            // rightLine = LinePosi(width, width * 0.2f, 0f, height)
            leftLine = LineProgress(0f, viewMeasureWidth * leftToX, 0f, viewMeasureHeight)
            rightLine = LineProgress(viewMeasureWidth, viewMeasureWidth * rightToX, 0f, viewMeasureHeight)
        }
        val progressDestY = viewMeasureHeight * progress
        val verts = FloatArray((meshWidth + 1) * (meshHeight + 1) * 2)
        var num = 0
        for (i in 0..meshHeight) {
            for (j in 0..meshWidth) {

                //Y轴点位根据实际bitmap高度进行平均
                val progressY = progressDestY + bitmapFitHeight * i / meshHeight
                val progressLeftX = leftLine.inputY(progressY)
                val progressRightX = rightLine.inputY(progressY)
                val progressDistanceX = progressRightX - progressLeftX

                //X点位根据两个line在Y值时的差值进行平均分布
                val progressX = progressDistanceX * j / meshWidth + progressLeftX

                //先X后Y输出
                verts[num] = progressX
                num++
                verts[num] = progressY
                num++
            }
        }
        return verts
    }


    private val fastOutSlowInInterpolator: Interpolator by lazy { STInterpolatorFactory.createFastOutSlowInInterpolator() } // 从 0f -> 1f 的过程是 先加速再减速
    // private val fastOutLinearInInterpolator: Interpolator by lazy { FastOutLinearInInterpolator() } // 从 0f -> 1f 的过程是 先加速然后匀速
    // private val linearOutSlowInInterpolator: Interpolator by lazy { LinearOutSlowInInterpolator() } // 从 0f -> 1f 的过程是 先匀速再减速
    // private val accelerateDecelerateInterpolator: Interpolator by lazy { AccelerateDecelerateInterpolator() }

    /**
     * 描述运动轨迹(起点XY和终点XY以及变函数Interpolator),根据输入量,可输出相对应的值
     */
    private inner class LineProgress internal constructor(private val fromX: Float, private val toX: Float, private val fromY: Float, private val toY: Float) {
        fun inputY(progressY: Float): Float {
            val disLength = toY - fromY
            val disXLength = toX - fromX
            val disFloat = progressY / disLength
            val disXFloat = fastOutSlowInInterpolator.getInterpolation(disFloat)
            return fromX + disXLength * disXFloat
        }

    }
}