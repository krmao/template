package com.smart.template.home.animation.magic.captureanim

import android.view.animation.AccelerateDecelerateInterpolator

/**
 * Created by linzx on 17-7-27.
 * 供给CaptureAnimView的支持类,核心数据计算均在此处
 */
class STMeshHelper {
    private var width = 0f
    private var height = 0f
    private var bitmapWidth = 0f
    private var bitmapHeight = 0f
    val vetWidth = 40
    val vetHeight = 40

    //核心移动曲线
    private val interpolator = AccelerateDecelerateInterpolator()
    fun init(width: Int, height: Int) {
        this.width = width.toFloat()
        this.height = height.toFloat()
    }

    fun setBitmapSize(bitmapWidth: Int, bitmapHeight: Int) {
        this.bitmapWidth = width
        this.bitmapHeight = width / bitmapWidth * bitmapHeight
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
    fun setProgress(progress: Float, leftToX: Float = 0.7f, rightToX: Float = 0.8f): FloatArray {
        //左右边线运动轨迹
        val leftLine: LineProgress
        val rightLine: LineProgress

        //在0~0.3f的部分,左右轨迹要逐渐向中心靠拢
        if (progress <= 0.3f) {
            // leftLine = LinePosi(0f, width * 0.1f * (progress / 0.3f), 0f, height)
            // rightLine = LinePosi(width, width * 0.2f + width * 0.8f * (0.3f - progress) / 0.3f, 0f, height)

            leftLine = LineProgress(0f, width * leftToX * (progress / 0.3f), 0f, height)
            rightLine = LineProgress(width, width * (1 - (1 - rightToX) * (progress / 0.3f)), 0f, height)
        } else {
            //在0.3f~1f,左右轨迹保持不变,图像按照此轨迹作为边界进行运动
            // leftLine = LinePosi(0f, width * 0.1f, 0f, height)
            // rightLine = LinePosi(width, width * 0.2f, 0f, height)
            leftLine = LineProgress(0f, width * leftToX, 0f, height)
            rightLine = LineProgress(width, width * rightToX, 0f, height)
        }
        val destY = height * progress
        val newFloat = FloatArray((vetWidth + 1) * (vetHeight + 1) * 2)
        var num = 0
        for (i in 0..vetHeight) {
            for (j in 0..vetWidth) {

                //Y轴点位根据实际bitmap高度进行平均
                val posiY = destY + bitmapHeight * i / vetHeight
                val leftPosiX = leftLine.inputY(posiY)
                val rightPosiX = rightLine.inputY(posiY)
                val disPosiX = rightPosiX - leftPosiX

                //X点位根据两个line在Y值时的差值进行平均分布
                val posiX = disPosiX * j / vetWidth + leftPosiX

                //先X后Y输出
                newFloat[num] = posiX
                num++
                newFloat[num] = posiY
                num++
            }
        }
        return newFloat
    }

    /**
     * 描述运动轨迹(起点XY和终点XY以及变函数Interpolator),根据输入量,可输出相对应的值
     */
    private inner class LineProgress internal constructor(private val fromX: Float, private val toX: Float, private val fromY: Float, private val toY: Float) {
        fun inputY(yP: Float): Float {
            val disLength = toY - fromY
            val disXLength = toX - fromX
            val disFloat = yP / disLength
            val disXFloat = interpolator.getInterpolation(disFloat)
            return fromX + disXLength * disXFloat
        }

    }
}