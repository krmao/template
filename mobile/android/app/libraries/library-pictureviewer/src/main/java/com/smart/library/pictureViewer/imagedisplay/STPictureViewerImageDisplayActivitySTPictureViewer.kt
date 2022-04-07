package com.smart.library.pictureviewer.imagedisplay

import android.util.Log
import com.smart.library.pictureviewer.R
import com.smart.library.pictureviewer.STPictureViewerAbstractFragmentsActivity
import com.smart.library.pictureviewer.STPictureViewerPageModel
import java.util.*

class STPictureViewerImageDisplayActivitySTPictureViewer : STPictureViewerAbstractFragmentsActivity(R.string.display_title, R.layout.st_picture_viewer_fragments_activity, Arrays.asList(
    STPictureViewerPageModel(R.string.display_p1_subtitle, R.string.display_p1_text),
    STPictureViewerPageModel(R.string.display_p2_subtitle, R.string.display_p2_text),
    STPictureViewerPageModel(R.string.display_p3_subtitle, R.string.display_p3_text)
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
                STPictureViewerImageDisplayLargeFragment::class.java,
                STPictureViewerImageDisplayRotateFragment::class.java,
                STPictureViewerImageDisplayRegionFragment::class.java
        )
    }
}