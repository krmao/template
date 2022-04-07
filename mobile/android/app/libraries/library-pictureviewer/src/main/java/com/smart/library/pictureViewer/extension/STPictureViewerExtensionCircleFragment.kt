package com.smart.library.pictureviewer.extension

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import com.smart.library.pictureviewer.R

class STPictureViewerExtensionCircleFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.st_picture_viewer_extension_circle_fragment, container, false)
        val activity = activity as STPictureViewerExtensionActivity?
        if (activity != null) {
            rootView.findViewById<View>(R.id.next).setOnClickListener { activity.next() }
            rootView.findViewById<View>(R.id.previous).setOnClickListener { activity.previous() }
        }
        val imageView: SubsamplingScaleImageView = rootView.findViewById(R.id.imageView)
        imageView.setImage(ImageSource.asset("st_picture_viewer_sanmartino.jpg"))
        return rootView
    }
}