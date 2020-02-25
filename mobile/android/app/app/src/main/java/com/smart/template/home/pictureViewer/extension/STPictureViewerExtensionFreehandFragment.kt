package com.smart.template.home.pictureViewer.extension

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.davemorrissey.labs.subscaleview.ImageSource
import com.smart.template.R
import com.smart.template.home.pictureViewer.extension.views.STPictureViewerFreehandView

class STPictureViewerExtensionFreehandFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.st_picture_viewer_extension_freehand_fragment, container, false)
        val activity = activity as STPictureViewerExtensionActivity?
        if (activity != null) {
            rootView.findViewById<View>(R.id.previous).setOnClickListener { activity.previous() }
        }
        val imageView: STPictureViewerFreehandView = rootView.findViewById(R.id.imageView)
        imageView.setImage(ImageSource.asset("st_picture_viewer_swissroad.jpg"))
        rootView.findViewById<View>(R.id.reset).setOnClickListener { imageView.reset() }
        return rootView
    }
}