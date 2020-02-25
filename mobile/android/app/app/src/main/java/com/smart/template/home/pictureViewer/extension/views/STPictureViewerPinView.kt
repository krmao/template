package com.smart.template.home.pictureViewer.extension.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import com.smart.template.R

class STPictureViewerPinView @JvmOverloads constructor(
    context: Context?,
    attr: AttributeSet? = null
) : SubsamplingScaleImageView(context, attr) {
    private val paint = Paint()
    private val vPin = PointF()
    private var sPin: PointF? = null
    private var pin: Bitmap? = null
    fun setPin(sPin: PointF?) {
        this.sPin = sPin
        initialise()
        invalidate()
    }

    private fun initialise() {
        val density = resources.displayMetrics.densityDpi.toFloat()
        pin =
            BitmapFactory.decodeResource(this.resources, R.drawable.st_picture_viewer_pushpin_blue)
        val w = density / 420f * (pin?.width ?: 1)
        val h = density / 420f * (pin?.height ?: 1)
        pin?.let {
            pin = Bitmap.createScaledBitmap(it, w.toInt(), h.toInt(), true)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // Don't draw pin before image is ready so it doesn't move around during setup.
        if (!isReady) {
            return
        }
        paint.isAntiAlias = true
        if (sPin != null && pin != null) {
            sourceToViewCoord(sPin, vPin)
            val vX = vPin.x - pin!!.width / 2
            val vY = vPin.y - pin!!.height
            pin?.let {
                canvas.drawBitmap(it, vX, vY, paint)
            }
        }
    }

    init {
        initialise()
    }
}