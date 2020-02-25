package com.smart.template.home.pictureViewer.configuration

import android.graphics.PointF
import android.os.Bundle
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import com.smart.template.R
import com.smart.template.home.pictureViewer.STPictureViewerAbstractPagesActivity
import com.smart.template.home.pictureViewer.STPictureViewerPageModel
import java.util.*

class STPictureViewerConfigurationActivitySTPictureViewer : STPictureViewerAbstractPagesActivity(R.string.configuration_title, R.layout.st_picture_viewer_pages_activity, Arrays.asList(
        STPictureViewerPageModel(R.string.configuration_p1_subtitle, R.string.configuration_p1_text),
        STPictureViewerPageModel(R.string.configuration_p2_subtitle, R.string.configuration_p2_text),
        STPictureViewerPageModel(R.string.configuration_p3_subtitle, R.string.configuration_p3_text),
        STPictureViewerPageModel(R.string.configuration_p4_subtitle, R.string.configuration_p4_text),
        STPictureViewerPageModel(R.string.configuration_p5_subtitle, R.string.configuration_p5_text),
        STPictureViewerPageModel(R.string.configuration_p6_subtitle, R.string.configuration_p6_text),
        STPictureViewerPageModel(R.string.configuration_p7_subtitle, R.string.configuration_p7_text),
        STPictureViewerPageModel(R.string.configuration_p8_subtitle, R.string.configuration_p8_text),
        STPictureViewerPageModel(R.string.configuration_p9_subtitle, R.string.configuration_p9_text),
        STPictureViewerPageModel(R.string.configuration_p10_subtitle, R.string.configuration_p10_text)
)) {
    private var view: SubsamplingScaleImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = findViewById(R.id.imageView)
        view?.setImage(ImageSource.asset("st_picture_viewer_card.png"))
    }

    override fun onPageChanged(page: Int) {
        if (page == 0) {
            view!!.setMinimumDpi(50)
        } else {
            view!!.maxScale = 2f
        }
        if (page == 1) {
            view!!.setMinimumTileDpi(50)
        } else {
            view!!.setMinimumTileDpi(320)
        }
        if (page == 4) {
            view!!.setDoubleTapZoomStyle(SubsamplingScaleImageView.ZOOM_FOCUS_CENTER)
        } else if (page == 5) {
            view!!.setDoubleTapZoomStyle(SubsamplingScaleImageView.ZOOM_FOCUS_CENTER_IMMEDIATE)
        } else {
            view!!.setDoubleTapZoomStyle(SubsamplingScaleImageView.ZOOM_FOCUS_FIXED)
        }
        if (page == 6) {
            view!!.setDoubleTapZoomDpi(240)
        } else {
            view!!.setDoubleTapZoomScale(1f)
        }
        if (page == 7) {
            view!!.setPanLimit(SubsamplingScaleImageView.PAN_LIMIT_CENTER)
        } else if (page == 8) {
            view!!.setPanLimit(SubsamplingScaleImageView.PAN_LIMIT_OUTSIDE)
        } else {
            view!!.setPanLimit(SubsamplingScaleImageView.PAN_LIMIT_INSIDE)
        }
        if (page == 9) {
            view!!.setDebug(true)
        } else {
            view!!.setDebug(false)
        }
        if (page == 2) {
            view!!.setScaleAndCenter(0f, PointF(3900f, 3120f))
            view!!.isPanEnabled = false
        } else {
            view!!.isPanEnabled = true
        }
        if (page == 3) {
            view!!.setScaleAndCenter(1f, PointF(3900f, 3120f))
            view!!.isZoomEnabled = false
        } else {
            view!!.isZoomEnabled = true
        }
    }
}