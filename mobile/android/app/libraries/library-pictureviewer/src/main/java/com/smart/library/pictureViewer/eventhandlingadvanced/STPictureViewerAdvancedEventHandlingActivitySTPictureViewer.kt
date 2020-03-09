package com.smart.library.pictureviewer.eventhandlingadvanced

import android.os.Bundle
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.widget.Toast
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import com.smart.library.pictureviewer.R
import com.smart.library.pictureviewer.STPictureViewerAbstractPagesActivity
import com.smart.library.pictureviewer.STPictureViewerPageModel
import java.util.*

class STPictureViewerAdvancedEventHandlingActivitySTPictureViewer : STPictureViewerAbstractPagesActivity(R.string.advancedevent_title, R.layout.st_picture_viewer_pages_activity, Arrays.asList(
    STPictureViewerPageModel(R.string.advancedevent_p1_subtitle, R.string.advancedevent_p1_text),
    STPictureViewerPageModel(R.string.advancedevent_p2_subtitle, R.string.advancedevent_p2_text),
    STPictureViewerPageModel(R.string.advancedevent_p3_subtitle, R.string.advancedevent_p3_text),
    STPictureViewerPageModel(R.string.advancedevent_p4_subtitle, R.string.advancedevent_p4_text),
    STPictureViewerPageModel(R.string.advancedevent_p5_subtitle, R.string.advancedevent_p5_text)
)) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val imageView = findViewById<SubsamplingScaleImageView>(R.id.imageView)
        val gestureDetector = GestureDetector(this, object : SimpleOnGestureListener() {
            override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                if (imageView.isReady) {
                    val sCoord = imageView.viewToSourceCoord(e.x, e.y)
                    Toast.makeText(applicationContext, "Single tap: " + sCoord!!.x.toInt() + ", " + sCoord.y.toInt(), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(applicationContext, "Single tap: Image not ready", Toast.LENGTH_SHORT).show()
                }
                return true
            }

            override fun onLongPress(e: MotionEvent) {
                if (imageView.isReady) {
                    val sCoord = imageView.viewToSourceCoord(e.x, e.y)
                    Toast.makeText(applicationContext, "Long press: " + sCoord!!.x.toInt() + ", " + sCoord.y.toInt(), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(applicationContext, "Long press: Image not ready", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onDoubleTap(e: MotionEvent): Boolean {
                if (imageView.isReady) {
                    val sCoord = imageView.viewToSourceCoord(e.x, e.y)
                    Toast.makeText(applicationContext, "Double tap: " + sCoord!!.x.toInt() + ", " + sCoord.y.toInt(), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(applicationContext, "Double tap: Image not ready", Toast.LENGTH_SHORT).show()
                }
                return true
            }
        })
        imageView.setImage(ImageSource.asset("st_picture_viewer_sanmartino.jpg"))
        imageView.setOnTouchListener { _, motionEvent -> gestureDetector.onTouchEvent(motionEvent) }
    }
}