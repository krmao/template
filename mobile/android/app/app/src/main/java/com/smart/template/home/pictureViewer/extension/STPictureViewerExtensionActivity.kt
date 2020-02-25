package com.smart.template.home.pictureViewer.extension

import android.util.Log
import com.smart.template.R
import com.smart.template.home.pictureViewer.STPictureViewerAbstractFragmentsActivity
import com.smart.template.home.pictureViewer.STPictureViewerPageModel
import com.smart.template.home.pictureViewer.imagedisplay.STPictureViewerImageDisplayActivitySTPictureViewer
import java.util.*

class STPictureViewerExtensionActivity : STPictureViewerAbstractFragmentsActivity(R.string.extension_title, R.layout.st_picture_viewer_fragments_activity, Arrays.asList(
        STPictureViewerPageModel(R.string.extension_p1_subtitle, R.string.extension_p1_text),
        STPictureViewerPageModel(R.string.extension_p2_subtitle, R.string.extension_p2_text),
        STPictureViewerPageModel(R.string.extension_p3_subtitle, R.string.extension_p3_text)
)) {
    override fun onPageChanged(page: Int) {
        try {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frame, FRAGMENTS[page].newInstance())
                    .commit()
        } catch (e: Exception) {
            Log.e(STPictureViewerImageDisplayActivitySTPictureViewer::class.java.name, "Failed to load fragment", e)
        }
    }

    companion object {
        private val FRAGMENTS = Arrays.asList(
                STPictureViewerExtensionPinFragment::class.java,
                STPictureViewerExtensionCircleFragment::class.java,
                STPictureViewerExtensionFreehandFragment::class.java
        )
    }
}