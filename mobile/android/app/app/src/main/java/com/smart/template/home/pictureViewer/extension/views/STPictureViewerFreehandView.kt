package com.smart.template.home.pictureViewer.extension.views

import android.content.Context
import android.graphics.*
import android.graphics.Paint.Cap
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import java.util.*

class STPictureViewerFreehandView @JvmOverloads constructor(context: Context?, attr: AttributeSet? = null) : SubsamplingScaleImageView(context, attr), OnTouchListener {
    private val paint = Paint()
    private val vPath = Path()
    private val vPoint = PointF()
    private var vPrev = PointF()
    private var vPrevious: PointF? = null
    private var vStart: PointF? = null
    private var drawing = false
    private var strokeWidth = 0
    private var sPoints: MutableList<PointF?>? = null
    private fun initialise() {
        setOnTouchListener(this)
        val density = resources.displayMetrics.densityDpi.toFloat()
        strokeWidth = (density / 60f).toInt()
    }

    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        return false
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (sPoints != null && !drawing) {
            return super.onTouchEvent(event)
        }
        var consumed = false
        val touchCount = event.pointerCount
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> if (event.actionIndex == 0) {
                vStart = PointF(event.x, event.y)
                vPrevious = PointF(event.x, event.y)
            } else {
                vStart = null
                vPrevious = null
            }
            MotionEvent.ACTION_MOVE -> {
                val sCurrentF = viewToSourceCoord(event.x, event.y)
                val sCurrent = PointF(sCurrentF!!.x, sCurrentF.y)
                val sStart = if (vStart == null) null else PointF(viewToSourceCoord(vStart)!!.x, viewToSourceCoord(vStart)!!.y)
                if (touchCount == 1 && vStart != null) {
                    val vDX = Math.abs(event.x - vPrevious!!.x)
                    val vDY = Math.abs(event.y - vPrevious!!.y)
                    if (vDX >= strokeWidth * 5 || vDY >= strokeWidth * 5) {
                        if (sPoints == null) {
                            sPoints = ArrayList()
                            sPoints?.add(sStart)
                        }
                        sPoints!!.add(sCurrent)
                        vPrevious!!.x = event.x
                        vPrevious!!.y = event.y
                        drawing = true
                    }
                    consumed = true
                    invalidate()
                } else if (touchCount == 1) { // Consume all one touch drags to prevent odd panning effects handled by the superclass.
                    consumed = true
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                invalidate()
                drawing = false
                vPrevious = null
                vStart = null
            }
        }
        // Use parent to handle pinch and two-finger pan.
        return consumed || super.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // Don't draw anything before image is ready.
        if (!isReady) {
            return
        }
        paint.isAntiAlias = true
        if (sPoints != null && sPoints!!.size >= 2) {
            vPath.reset()
            sourceToViewCoord(sPoints!![0]!!.x, sPoints!![0]!!.y, vPrev)
            vPath.moveTo(vPrev.x, vPrev.y)
            for (i in 1 until sPoints!!.size) {
                sourceToViewCoord(sPoints!![i]!!.x, sPoints!![i]!!.y, vPoint)
                vPath.quadTo(vPrev.x, vPrev.y, (vPoint.x + vPrev.x) / 2, (vPoint.y + vPrev.y) / 2)
                vPrev = vPoint
            }
            paint.style = Paint.Style.STROKE
            paint.strokeCap = Cap.ROUND
            paint.strokeWidth = strokeWidth * 2.toFloat()
            paint.color = Color.BLACK
            canvas.drawPath(vPath, paint)
            paint.strokeWidth = strokeWidth.toFloat()
            paint.color = Color.argb(255, 51, 181, 229)
            canvas.drawPath(vPath, paint)
        }
    }

    fun reset() {
        sPoints = null
        invalidate()
    }

    init {
        initialise()
    }
}