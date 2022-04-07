package com.smart.library.pictureviewer.viewpager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import com.smart.library.base.STBaseFragment
import com.smart.library.pictureviewer.R

class STPictureViewerPagerFragment : STBaseFragment() {
    private var asset: String? = null
    fun setAsset(asset: String?) {
        this.asset = asset
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.st_picture_viewer_view_pager_page, container, false)
        if (savedInstanceState != null) {
            if (asset == null && savedInstanceState.containsKey(BUNDLE_ASSET)) {
                asset = savedInstanceState.getString(BUNDLE_ASSET)
            }
        }
        if (asset != null) {
            val imageView: SubsamplingScaleImageView = rootView.findViewById(R.id.imageView)
            imageView.setImage(ImageSource.asset(asset!!))
        }
        return rootView
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val rootView = view
        if (rootView != null) {
            outState.putString(BUNDLE_ASSET, asset)
        }
    }

    companion object {
        private const val BUNDLE_ASSET = "asset"
    }
}