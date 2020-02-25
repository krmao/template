package com.smart.template.home.pictureViewer.basicfeatures

import android.os.Bundle
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import com.smart.template.R
import com.smart.template.home.pictureViewer.STPictureViewerAbstractPagesActivity
import com.smart.template.home.pictureViewer.STPictureViewerPageModel
import java.util.*

class STPictureViewerBasicFeaturesActivitySTPictureViewer : STPictureViewerAbstractPagesActivity(R.string.basic_title, R.layout.st_picture_viewer_pages_activity, Arrays.asList(
        STPictureViewerPageModel(R.string.basic_p1_subtitle, R.string.basic_p1_text),
        STPictureViewerPageModel(R.string.basic_p2_subtitle, R.string.basic_p2_text),
        STPictureViewerPageModel(R.string.basic_p3_subtitle, R.string.basic_p3_text),
        STPictureViewerPageModel(R.string.basic_p4_subtitle, R.string.basic_p4_text),
        STPictureViewerPageModel(R.string.basic_p5_subtitle, R.string.basic_p5_text)
)) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = findViewById<SubsamplingScaleImageView>(R.id.imageView)
        view.setImage(ImageSource.asset("st_picture_viewer_physical_political_world_map.jpg"))
    }
}