package com.smart.library.pictureviewer.eventhandling

import android.os.Bundle
import android.widget.Toast
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import com.smart.library.pictureviewer.R
import com.smart.library.pictureviewer.STPictureViewerAbstractPagesActivity
import com.smart.library.pictureviewer.STPictureViewerPageModel
import java.util.*

class STPictureViewerEventHandlingActivitySTPictureViewer : STPictureViewerAbstractPagesActivity(R.string.event_title, R.layout.st_picture_viewer_pages_activity, Arrays.asList(
    STPictureViewerPageModel(R.string.event_p1_subtitle, R.string.event_p1_text),
    STPictureViewerPageModel(R.string.event_p2_subtitle, R.string.event_p2_text),
    STPictureViewerPageModel(R.string.event_p3_subtitle, R.string.event_p3_text)
)) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val imageView = findViewById<SubsamplingScaleImageView>(R.id.imageView)
        imageView.setImage(ImageSource.asset("st_picture_viewer_sanmartino.jpg"))
        imageView.setOnClickListener { v -> Toast.makeText(v.context, "Clicked", Toast.LENGTH_SHORT).show() }
        imageView.setOnLongClickListener { v ->
            Toast.makeText(v.context, "Long clicked", Toast.LENGTH_SHORT).show()
            true
        }
    }
}