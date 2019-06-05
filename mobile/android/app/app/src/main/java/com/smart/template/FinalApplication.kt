package com.smart.template

import android.graphics.Color
import com.smart.library.base.STBaseApplication
import com.smart.library.base.STConfig
import com.smart.library.flutter.FlutterManager
import com.smart.library.util.STFileUtil
import com.smart.library.util.image.STImageManager
import com.smart.library.util.image.impl.STImageFrescoHandler
import com.smart.library.widget.debug.STDebugFragment
import com.smart.library.widget.titlebar.STTitleBar
import com.smart.template.home.tab.HomeTabActivity
import com.smart.template.init.STDeployInitManager
import com.smart.template.module.rn.dev.ReactDevSettingsView
import com.smart.template.repository.STRepository
import com.smart.template.repository.remote.core.STOkHttpManager

@Suppress("unused")
class FinalApplication : STBaseApplication() {

    override fun onCreate() {
        // init before application onCreate
        STConfig.NOTIFICATION_ICON_SMALL = R.mipmap.ic_notification
        STConfig.CLASS_ACTIVITY_MAIN = HomeTabActivity::class.java

        STTitleBar.DEFAULT_BACKGROUND_COLOR = Color.BLACK
        STTitleBar.DEFAULT_TEXT_COLOR = Color.WHITE
        STTitleBar.DEFAULT_TEXT_SIZE = 16f

        super.onCreate()

        //if (STBaseApplication.DEBUG) {
        Thread.setDefaultUncaughtExceptionHandler { t, e -> STFileUtil.saveUncaughtException(t, e) }
        //}

        // init global location
        // STLocationManager.initialize(STLocationClientForAMap())

        // init global repository
        STRepository.init()

        STDebugFragment.childViewList.add(ReactDevSettingsView::class.java)

        // image manager with fresco and react native init together
        val frescoConfig = STImageFrescoHandler.getConfigBuilder(DEBUG, STOkHttpManager.client).build()
        STImageManager.initialize(STImageFrescoHandler(frescoConfig))

        STDeployInitManager.init(this, frescoConfig)

        FlutterManager.startInitialization(this)
    }
}
