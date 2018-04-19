package com.smart.template

import com.smart.library.base.CXConfig
import com.smart.template.library.CXApplication
import com.smart.template.library.R
import com.smart.template.tab.HomeTabActivity

@Suppress("unused")
class CXFinalApplication : CXApplication() {
    override fun onCreate() {

        // init before application onCreate
        CXConfig.ICON_NOTIFICATION_SMALL = R.mipmap.ic_notification
        CXConfig.CLASS_ACTIVITY_MAIN = HomeTabActivity::class.java

        super.onCreate()
    }
}
