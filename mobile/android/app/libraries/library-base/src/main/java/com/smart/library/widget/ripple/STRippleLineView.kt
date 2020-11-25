package com.smart.library.widget.ripple

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import com.smart.library.util.STLogUtil
import java.util.*
import kotlin.math.min

/**
 * 水波纹特效
 * https://github.com/hackware1993/WaveView
 */
@Suppress("unused", "MemberVisibilityCanBePrivate")
class STRippleLineView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    var durationMs: Long = 2000                  // 一个波纹从创建到消失的持续时间
    var fromRadiusPx: Float = 0f                // 初始波纹半径 = 0f
    var createCircleSpeedMs: Long = 400         // 波纹的创建速度，每500ms创建一个
    var maxRadiusRateOnMinEdge: Float = 0.85f   // 与最小边的比率

    private var runningTime: Long = 0
    private var maxRadiusChanged = false
    private var maxRadius: Float = 0f           // 最大波纹半径 = 0f
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#FFFFFF")
        style = Paint.Style.FILL
    }

    private val circleList: MutableList<Circle> = ArrayList()

    fun setStyle(style: Paint.Style) {
        paint.style = style
    }

    fun setColor(color: Int) {
        paint.color = color
    }

    fun setMaxRadius(maxRadius: Float) {
        this.maxRadius = maxRadius
        maxRadiusChanged = true
    }

    private var circleRunnable: Runnable? = null

    /**
     * 开始
     */
    fun start() {
        stopImmediately(false)
        runningTime = 0
        circleRunnable = createCircleRunnable()
        post(circleRunnable)
    }

    /**
     * 缓慢停止
     */
    fun stop() {
        runningTime = -1
    }

    /**
     * 立即停止
     */
    fun stopImmediately(needInvalidate: Boolean = true) {
        removeCallbacks(circleRunnable)
        stop()
        circleList.clear()
        circleRunnable = null
        if (needInvalidate) postInvalidate()
    }

    private fun createCircleRunnable(): Runnable {
        return object : Runnable {
            override fun run() {
                if (runningTime != -1L && runningTime <= durationMs) {
                    val circle = Circle()
                    circleList.add(circle)
                    postInvalidate()

                    runningTime += createCircleSpeedMs
                    postDelayed(this, createCircleSpeedMs)
                } else {
                    stop()
                }
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        val iterator = circleList.iterator()
        var index = 0
        while (iterator.hasNext()) {
            STLogUtil.sync {
                STLogUtil.w(TAG, "onDraw index=$index,circleList=${circleList.size}")
                index++
            }

            val circle = iterator.next()
            val radius = circle.getCurrentRadius()
            if (System.currentTimeMillis() - circle.createTime < durationMs) {
                paint.alpha = circle.getCurrentAlpha(radius)
                canvas.drawCircle(width / 2.toFloat(), height / 2.toFloat(), radius, paint)
                postInvalidateDelayed(10)
            } else {
                iterator.remove()
                postInvalidateDelayed(10)
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        if (!maxRadiusChanged) maxRadius = min(w, h) * maxRadiusRateOnMinEdge / 2.0f
    }

    var interpolator: Interpolator = DecelerateInterpolator()
    var fromAlpha: Float = 1f // [0f, 1f]
    var toAlpha: Float = 0f // [0f, 1f]

    private inner class Circle(val createTime: Long = System.currentTimeMillis()) {
        fun getCurrentAlpha(currentRadius: Float): Int {
            val radiusRatio: Float = (currentRadius - fromRadiusPx) / (maxRadius - fromRadiusPx)
            val alphaInterpolation: Float = interpolator.getInterpolation(radiusRatio)
            val currentAlpha = (255 * (fromAlpha + (toAlpha - fromAlpha) * alphaInterpolation)).toInt()
            STLogUtil.sync { STLogUtil.d(TAG, "currentAlpha=$currentAlpha, fromAlpha=$fromAlpha, toAlpha=$toAlpha, alphaInterpolation=$alphaInterpolation, radiusRatio=$radiusRatio") }
            return currentAlpha
        }

        fun getCurrentRadius(): Float {
            val timeRatio = (System.currentTimeMillis() - createTime) / durationMs.toFloat()
            return fromRadiusPx + interpolator.getInterpolation(timeRatio) * (maxRadius - fromRadiusPx)
        }
    }

    companion object {
        const val TAG = "[RippleLineView]"
    }
}