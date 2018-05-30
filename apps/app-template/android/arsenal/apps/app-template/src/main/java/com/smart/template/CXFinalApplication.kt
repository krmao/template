package com.smart.template

import android.graphics.Color
import com.smart.library.base.CXConfig
import com.smart.library.widget.titlebar.CXTitleBar
import com.smart.template.library.CXApplication
import com.smart.template.library.R
import com.smart.template.repository.CXRepository
import com.smart.template.tab.HomeTabActivity

@Suppress("unused")
class CXFinalApplication : CXApplication() {
    override fun onCreate() {

        // init before application onCreate
        CXConfig.NOTIFICATION_ICON_SMALL = R.mipmap.ic_notification
        CXConfig.CLASS_ACTIVITY_MAIN = HomeTabActivity::class.java

        CXTitleBar.DEFAULT_BACKGROUND_COLOR = Color.BLACK
        CXTitleBar.DEFAULT_TEXT_COLOR = Color.WHITE
        CXTitleBar.DEFAULT_TEXT_SIZE = 16f

        super.onCreate()

        CXRepository.init()
    }
}
