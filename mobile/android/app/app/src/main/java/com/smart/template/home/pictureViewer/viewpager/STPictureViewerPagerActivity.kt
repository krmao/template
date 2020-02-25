package com.smart.template.home.pictureViewer.viewpager

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.smart.template.R
import com.smart.template.home.pictureViewer.STPictureViewerAbstractPagesActivity
import com.smart.template.home.pictureViewer.STPictureViewerPageModel
import java.util.*

class STPictureViewerPagerActivity : STPictureViewerAbstractPagesActivity(R.string.pager_title, R.layout.st_picture_viewer_view_pager, Arrays.asList(
        STPictureViewerPageModel(R.string.pager_p1_subtitle, R.string.pager_p1_text),
        STPictureViewerPageModel(R.string.pager_p2_subtitle, R.string.pager_p2_text)
)) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val horizontalPager = findViewById<ViewPager>(R.id.horizontal_pager)
        horizontalPager.adapter = ScreenSlidePagerAdapter(supportFragmentManager)
        val verticalPager = findViewById<ViewPager>(R.id.vertical_pager)
        verticalPager.adapter = ScreenSlidePagerAdapter(supportFragmentManager)
    }

    override fun onBackPressed() {
        val viewPager = findViewById<ViewPager>(if (page == 0) R.id.horizontal_pager else R.id.vertical_pager)
        if (viewPager.currentItem == 0) {
            super.onBackPressed()
        } else {
            viewPager.currentItem = viewPager.currentItem - 1
        }
    }

    override fun onPageChanged(page: Int) {
        if (page == 0) {
            findViewById<View>(R.id.horizontal_pager).visibility = View.VISIBLE
            findViewById<View>(R.id.vertical_pager).visibility = View.GONE
        } else {
            findViewById<View>(R.id.horizontal_pager).visibility = View.GONE
            findViewById<View>(R.id.vertical_pager).visibility = View.VISIBLE
        }
    }

    private inner class ScreenSlidePagerAdapter internal constructor(fm: FragmentManager?) : FragmentStatePagerAdapter(fm!!) {
        override fun getItem(position: Int): Fragment {
            val fragment = STPictureViewerPagerFragment()
            fragment.setAsset(IMAGES[position])
            return fragment
        }

        override fun getCount(): Int {
            return IMAGES.size
        }
    }

    companion object {
        private val IMAGES = arrayOf("st_picture_viewer_card.png", "st_picture_viewer_physical_political_world_map.jpg", "st_picture_viewer_sanmartino.jpg", "st_picture_viewer_swissroad.jpg")
    }
}