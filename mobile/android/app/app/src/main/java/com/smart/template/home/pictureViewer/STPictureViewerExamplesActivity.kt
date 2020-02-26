package com.smart.template.home.pictureViewer

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.smart.library.base.STBaseActivity
import com.smart.library.util.STCppTestUtil
import com.smart.library.util.STToastUtil
import com.smart.template.R
import com.smart.template.home.pictureViewer.animation.STPictureViewerAnimationActivitySTPictureViewer
import com.smart.template.home.pictureViewer.basicfeatures.STPictureViewerBasicFeaturesActivitySTPictureViewer
import com.smart.template.home.pictureViewer.configuration.STPictureViewerConfigurationActivitySTPictureViewer
import com.smart.template.home.pictureViewer.eventhandling.STPictureViewerEventHandlingActivitySTPictureViewer
import com.smart.template.home.pictureViewer.eventhandlingadvanced.STPictureViewerAdvancedEventHandlingActivitySTPictureViewer
import com.smart.template.home.pictureViewer.extension.STPictureViewerExtensionActivity
import com.smart.template.home.pictureViewer.imagedisplay.STPictureViewerImageDisplayActivitySTPictureViewer
import com.smart.template.home.pictureViewer.viewpager.STPictureViewerPagerActivity
import kotlinx.android.synthetic.main.st_picture_viewer_examples_activity.*

class STPictureViewerExamplesActivity : STBaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.st_picture_viewer_examples_activity)
        findViewById<View>(R.id.basicFeatures).setOnClickListener(this)
        findViewById<View>(R.id.imageDisplay).setOnClickListener(this)
        findViewById<View>(R.id.eventHandling).setOnClickListener(this)
        findViewById<View>(R.id.advancedEventHandling).setOnClickListener(this)
        findViewById<View>(R.id.viewPagerGalleries).setOnClickListener(this)
        findViewById<View>(R.id.animation).setOnClickListener(this)
        findViewById<View>(R.id.extension).setOnClickListener(this)
        findViewById<View>(R.id.configuration).setOnClickListener(this)

        cppTest.setOnClickListener {
            STToastUtil.show("cpp:${STCppTestUtil.stringFromJNI1()}")
        }
        cppTest2.setOnClickListener {
            STToastUtil.show("cpp:${STCppTestUtil.stringFromJNI2()}")
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.basicFeatures -> startActivity(STPictureViewerBasicFeaturesActivitySTPictureViewer::class.java)
            R.id.imageDisplay -> startActivity(STPictureViewerImageDisplayActivitySTPictureViewer::class.java)
            R.id.eventHandling -> startActivity(STPictureViewerEventHandlingActivitySTPictureViewer::class.java)
            R.id.advancedEventHandling -> startActivity(
                STPictureViewerAdvancedEventHandlingActivitySTPictureViewer::class.java
            )
            R.id.viewPagerGalleries -> startActivity(STPictureViewerPagerActivity::class.java)
            R.id.animation -> startActivity(STPictureViewerAnimationActivitySTPictureViewer::class.java)
            R.id.extension -> startActivity(STPictureViewerExtensionActivity::class.java)
            R.id.configuration -> startActivity(STPictureViewerConfigurationActivitySTPictureViewer::class.java)
        }
    }

    private fun startActivity(activity: Class<out Activity>) = startActivity(Intent(this, activity))
}