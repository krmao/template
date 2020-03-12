package com.smart.template.home.animation.wave

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import java.util.*
import kotlin.math.min

/**
 * 水波纹特效
 * https://github.com/hackware1993/WaveView
 */
@Suppress("unused")
class STRippleLineView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    var speed: Int = 500 // 波纹的创建速度，每500ms创建一个
    var duration: Long = 2000 // 一个波纹从创建到消失的持续时间
    var initialRadius: Float = 0f // 初始波纹半径 = 0f
    var maxRadiusRate: Float = 0.85f
    var interpolator: Interpolator = LinearInterpolator()

    private var isRunning = false
    private var maxRadiusSet = false
    private var maxRadius: Float = 0f // 最大波纹半径 = 0f
    private var lastCreateTime: Long = 0
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val circleList: MutableList<Circle> = ArrayList()
    private val createCircle: Runnable = object : Runnable {
        override fun run() {
            if (isRunning) {
                newCircle()
                postDelayed(this, speed.toLong())
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        if (!maxRadiusSet) {
            maxRadius = min(w, h) * maxRadiusRate / 2.0f
        }
    }

    fun setStyle(style: Paint.Style?) {
        paint.style = style
    }

    fun setColor(color: Int) {
        paint.color = color
    }

    /**
     * 开始
     */
    fun start() {
        if (!isRunning) {
            isRunning = true
            createCircle.run()
        }
    }

    /**
     * 缓慢停止
     */
    fun stop() {
        isRunning = false
    }

    /**
     * 立即停止
     */
    fun stopImmediately() {
        isRunning = false
        circleList.clear()
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        val iterator = circleList.iterator()
        while (iterator.hasNext()) {
            val circle = iterator.next()
            val radius = circle.currentRadius
            if (System.currentTimeMillis() - circle.createTime < duration) {
                paint.alpha = circle.alpha
                canvas.drawCircle(width / 2.toFloat(), height / 2.toFloat(), radius, paint)
            } else {
                iterator.remove()
            }
        }
        if (circleList.size > 0) {
            postInvalidateDelayed(10)
        }
    }

    fun setMaxRadius(maxRadius: Float) {
        this.maxRadius = maxRadius
        maxRadiusSet = true
    }

    private fun newCircle() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastCreateTime < speed) {
            return
        }
        val circle = Circle()
        circleList.add(circle)
        invalidate()
        lastCreateTime = currentTime
    }

    private inner class Circle(val createTime: Long = System.currentTimeMillis()) {

        val alpha: Int
            get() {
                val percent = (currentRadius - initialRadius) / (maxRadius - initialRadius)
                return (255 - interpolator.getInterpolation(percent) * 255).toInt()
            }

        val currentRadius: Float
            get() {
                val percent = (System.currentTimeMillis() - createTime) * 1.0f / duration
                return initialRadius + interpolator.getInterpolation(percent) * (maxRadius - initialRadius)
            }

    }
}