package com.smart.library.pictureviewer.imagedisplay

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import com.smart.library.pictureviewer.R

class STPictureViewerImageDisplayLargeFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.st_picture_viewer_imagedisplay_large_fragment, container, false)
        val activity = activity as STPictureViewerImageDisplayActivitySTPictureViewer?
        if (activity != null) {
            rootView.findViewById<View>(R.id.next).setOnClickListener { activity.next() }
        }
        val imageView: SubsamplingScaleImageView = rootView.findViewById(R.id.imageView)
        imageView.setImage(ImageSource.asset("st_picture_viewer_physical_political_world_map.jpg"))
        return rootView
    }
}