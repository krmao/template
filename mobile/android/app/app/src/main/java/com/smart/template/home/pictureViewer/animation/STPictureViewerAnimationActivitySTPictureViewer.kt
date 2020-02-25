package com.smart.template.home.pictureViewer.animation

import android.graphics.PointF
import android.os.Bundle
import android.view.View
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import com.smart.template.R
import com.smart.template.home.pictureViewer.STPictureViewerAbstractPagesActivity
import com.smart.template.home.pictureViewer.STPictureViewerPageModel
import com.smart.template.home.pictureViewer.extension.views.STPictureViewerPinView
import java.util.*

class STPictureViewerAnimationActivitySTPictureViewer : STPictureViewerAbstractPagesActivity(R.string.animation_title, R.layout.st_picture_viewer_animation_activity, Arrays.asList(
        STPictureViewerPageModel(R.string.animation_p1_subtitle, R.string.animation_p1_text),
        STPictureViewerPageModel(R.string.animation_p2_subtitle, R.string.animation_p2_text),
        STPictureViewerPageModel(R.string.animation_p3_subtitle, R.string.animation_p3_text),
        STPictureViewerPageModel(R.string.animation_p4_subtitle, R.string.animation_p4_text)
)) {
    private var viewSTPictureViewer: STPictureViewerPinView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findViewById<View>(R.id.play).setOnClickListener { play() }
        viewSTPictureViewer = findViewById(R.id.imageView)
        viewSTPictureViewer!!.setImage(ImageSource.asset("sanmartino.jpg"))
    }

    override fun onPageChanged(page: Int) {
        if (page == 2) {
            viewSTPictureViewer!!.setPanLimit(SubsamplingScaleImageView.PAN_LIMIT_CENTER)
        } else {
            viewSTPictureViewer!!.setPanLimit(SubsamplingScaleImageView.PAN_LIMIT_INSIDE)
        }
    }

    private fun play() {
        val random = Random()
        if (viewSTPictureViewer!!.isReady) {
            val maxScale = viewSTPictureViewer!!.maxScale
            val minScale = viewSTPictureViewer!!.minScale
            val scale = random.nextFloat() * (maxScale - minScale) + minScale
            val center = PointF(random.nextInt(viewSTPictureViewer!!.sWidth).toFloat(), random.nextInt(viewSTPictureViewer!!.sHeight).toFloat())
            viewSTPictureViewer!!.setPin(center)
            val animationBuilder = viewSTPictureViewer!!.animateScaleAndCenter(scale, center)
            if (page == 3) {
                animationBuilder!!.withDuration(2000).withEasing(SubsamplingScaleImageView.EASE_OUT_QUAD).withInterruptible(false).start()
            } else {
                animationBuilder!!.withDuration(750).start()
            }
        }
    }
}