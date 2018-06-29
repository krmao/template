package com.smart.template

import android.graphics.Color
import com.smart.library.base.CXBaseApplication
import com.smart.library.base.CXConfig
import com.smart.library.util.CXFileUtil
import com.smart.library.util.image.CXImageManager
import com.smart.library.util.image.impl.CXImageFrescoHandler
import com.smart.library.widget.titlebar.CXTitleBar
import com.smart.template.library.R
import com.smart.template.module.rn.ReactManager
import com.smart.template.repository.CXRepository
import com.smart.template.repository.remote.core.CXOkHttpManager
import com.smart.template.tab.HomeTabActivity

class FinalApplication : CXBaseApplication() {

    override fun onCreate() {
        // init before application onCreate
        CXConfig.NOTIFICATION_ICON_SMALL = R.mipmap.ic_notification
        CXConfig.CLASS_ACTIVITY_MAIN = HomeTabActivity::class.java

        CXTitleBar.DEFAULT_BACKGROUND_COLOR = Color.BLACK
        CXTitleBar.DEFAULT_TEXT_COLOR = Color.WHITE
        CXTitleBar.DEFAULT_TEXT_SIZE = 16f

        super.onCreate()

        //if (CXBaseApplication.DEBUG) {
        Thread.setDefaultUncaughtExceptionHandler { t, e -> CXFileUtil.saveUncaughtException(t, e) }
        //}

        CXRepository.init()

        // image manager with fresco and react native init together
        val frescoConfig = CXImageFrescoHandler.getConfigBuilder(CXBaseApplication.DEBUG, CXOkHttpManager.client).build()
        CXImageManager.initialize(CXImageFrescoHandler(frescoConfig))
        ReactManager.init(this, CXBaseApplication.DEBUG, frescoConfig)
    }
}
