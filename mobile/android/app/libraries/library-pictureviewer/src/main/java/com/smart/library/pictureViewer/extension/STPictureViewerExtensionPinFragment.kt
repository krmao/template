package com.smart.library.pictureviewer.extension

import android.graphics.PointF
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.davemorrissey.labs.subscaleview.ImageSource
import com.smart.library.pictureviewer.R
import com.smart.library.pictureviewer.extension.views.STPictureViewerPinView

class STPictureViewerExtensionPinFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.st_picture_viewer_extension_pin_fragment, container, false)
        val activity = activity as STPictureViewerExtensionActivity?
        if (activity != null) {
            rootView.findViewById<View>(R.id.next).setOnClickListener { activity.next() }
        }
        val imageViewSTPictureViewer: STPictureViewerPinView = rootView.findViewById(R.id.imageView)
        imageViewSTPictureViewer.setImage(ImageSource.asset("st_picture_viewer_physical_political_world_map.jpg"))
        imageViewSTPictureViewer.setPin(PointF(1602f, 405f))
        return rootView
    }
}