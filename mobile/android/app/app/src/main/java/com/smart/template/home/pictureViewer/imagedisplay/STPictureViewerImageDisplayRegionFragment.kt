package com.smart.template.home.pictureViewer.imagedisplay

import android.graphics.Bitmap
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import com.davemorrissey.labs.subscaleview.decoder.*
import com.smart.template.R

class STPictureViewerImageDisplayRegionFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.st_picture_viewer_imagedisplay_region_fragment, container, false)
        val imageView: SubsamplingScaleImageView = rootView.findViewById(R.id.imageView)
        imageView.setBitmapDecoderFactory(CompatDecoderFactory<ImageDecoder>(SkiaImageDecoder::class.java, Bitmap.Config.ARGB_8888))
        imageView.setRegionDecoderFactory(CompatDecoderFactory<ImageRegionDecoder>(SkiaImageRegionDecoder::class.java, Bitmap.Config.ARGB_8888))
        imageView.orientation = SubsamplingScaleImageView.ORIENTATION_90
        imageView.setImage(ImageSource.asset("card.png").region(Rect(5200, 651, 8200, 3250)))
        val activity = activity as STPictureViewerImageDisplayActivitySTPictureViewer?
        if (activity != null) {
            rootView.findViewById<View>(R.id.previous).setOnClickListener { activity.previous() }
        }
        rootView.findViewById<View>(R.id.rotate).setOnClickListener { imageView.orientation = (imageView.orientation + 90) % 360 }
        return rootView
    }
}