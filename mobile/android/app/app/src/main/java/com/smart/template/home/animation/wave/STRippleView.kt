package com.smart.template.home.animation.wave

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PointF
import android.os.CountDownTimer
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import kotlin.math.*

/**
 * 涟漪效果控件
 * https://github.com/NanBox/RippleLayout
 */
@Suppress("MemberVisibilityCanBePrivate", "unused")
class STRippleView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {
    //图片横向、纵向的格数
    private val meshWidth = 20
    private val meshHeight = 20

    //图片的顶点数
    private val vertsCount = (meshWidth + 1) * (meshHeight + 1)

    //原坐标数组
    private val staticVerts = FloatArray(vertsCount * 2)

    //转换后的坐标数组
    private val targetVerts = FloatArray(vertsCount * 2)

    //当前控件的图片
    private var bitmap: Bitmap? = null

    //水波宽度的一半
    private val rippleWidth = 100f

    //水波扩散速度
    private val rippleSpeed = 15f

    //水波半径
    private var rippleRadius = 0f

    //水波动画是否执行中
    private var isRippling = false

    override fun dispatchDraw(canvas: Canvas) {
        if (isRippling && bitmap != null) {
            canvas.drawBitmapMesh(bitmap!!, meshWidth, meshHeight, targetVerts, 0, null, 0, null)
        } else {
            super.dispatchDraw(canvas)
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> showRipple(ev.x, ev.y)
        }
        return super.dispatchTouchEvent(ev)
    }

    /**
     * 显示水波动画
     *
     * @param originX 原点 x 坐标
     * @param originY 原点 y 坐标
     */
    fun showRipple(originX: Float, originY: Float) {
        if (isRippling) {
            return
        }
        initData()
        if (bitmap == null) {
            return
        }
        isRippling = true
        //循环次数，通过控件对角线距离计算，确保水波纹完全消失
        val viewLength = getLength(bitmap!!.width.toFloat(), bitmap!!.height.toFloat()).toInt()
        val count = ((viewLength + rippleWidth) / rippleSpeed).toInt()
        val cdt: CountDownTimer = object : CountDownTimer((count * 10).toLong(), 10) {
            override fun onTick(millisUntilFinished: Long) {
                rippleRadius = (count - (millisUntilFinished / 10).toInt()) * rippleSpeed
                warp(originX, originY)
            }

            override fun onFinish() {
                isRippling = false
            }
        }
        cdt.start()
    }

    /**
     * 初始化 Bitmap 及对应数组
     */
    private fun initData() {
        bitmap = getCacheBitmapFromView(this)
        if (bitmap == null) {
            return
        }
        val bitmapWidth = bitmap!!.width.toFloat()
        val bitmapHeight = bitmap!!.height.toFloat()
        var index = 0
        for (height in 0..meshHeight) {
            val y = bitmapHeight * height / meshHeight
            for (width in 0..meshWidth) {
                val x = bitmapWidth * width / meshWidth
                targetVerts[index * 2] = x
                staticVerts[index * 2] = targetVerts[index * 2]
                targetVerts[index * 2 + 1] = y
                staticVerts[index * 2 + 1] = targetVerts[index * 2 + 1]
                index += 1
            }
        }
    }

    /**
     * 图片转换
     *
     * @param originX 原点 x 坐标
     * @param originY 原点 y 坐标
     */
    private fun warp(originX: Float, originY: Float) {
        var i = 0
        while (i < vertsCount * 2) {
            val staticX = staticVerts[i]
            val staticY = staticVerts[i + 1]
            val length = getLength(staticX - originX, staticY - originY)
            if (length > rippleRadius - rippleWidth && length < rippleRadius + rippleWidth) {
                val point = getRipplePoint(originX, originY, staticX, staticY)
                targetVerts[i] = point.x
                targetVerts[i + 1] = point.y
            } else {
                //复原
                targetVerts[i] = staticVerts[i]
                targetVerts[i + 1] = staticVerts[i + 1]
            }
            i += 2
        }
        invalidate()
    }

    /**
     * 获取水波的偏移坐标
     *
     * @param originX 原点 x 坐标
     * @param originY 原点 y 坐标
     * @param staticX 待偏移顶点的原 x 坐标
     * @param staticY 待偏移顶点的原 y 坐标
     * @return 偏移后坐标
     */
    private fun getRipplePoint(originX: Float, originY: Float, staticX: Float, staticY: Float): PointF {
        val length = getLength(staticX - originX, staticY - originY)
        //偏移点与原点间的角度
        val angle = atan(abs((staticY - originY) / (staticX - originX)).toDouble()).toFloat()
        //计算偏移距离
        val rate = (length - rippleRadius) / rippleWidth
        val offset = cos(rate.toDouble()).toFloat() * 10f
        val offsetX = offset * cos(angle.toDouble()).toFloat()
        val offsetY = offset * sin(angle.toDouble()).toFloat()
        //计算偏移后的坐标
        val targetX: Float
        val targetY: Float
        if (length < rippleRadius + rippleWidth && length > rippleRadius) {
            //波峰外的偏移坐标
            targetX = if (staticX > originX) {
                staticX + offsetX
            } else {
                staticX - offsetX
            }
            targetY = if (staticY > originY) {
                staticY + offsetY
            } else {
                staticY - offsetY
            }
        } else {
            //波峰内的偏移坐标
            targetX = if (staticX > originY) {
                staticX - offsetX
            } else {
                staticX + offsetX
            }
            targetY = if (staticY > originY) {
                staticY - offsetY
            } else {
                staticY + offsetY
            }
        }
        return PointF(targetX, targetY)
    }

    /**
     * 根据宽高，获取对角线距离
     *
     * @param width  宽
     * @param height 高
     * @return 距离
     */
    private fun getLength(width: Float, height: Float): Float {
        return sqrt(width * width + height * height.toDouble()).toFloat()
    }

    /**
     * 获取 View 的缓存视图
     *
     * @param view 对应的View
     * @return 对应View的缓存视图
     */
    private fun getCacheBitmapFromView(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val bgDrawable = view.background
        bgDrawable?.draw(canvas)
        view.draw(canvas)
        return bitmap
    }
}