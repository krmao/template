package com.smart.library.reactnative

import android.app.Application
import android.content.Context
import com.smart.library.base.STBaseApplication
import com.smart.library.reactnative.dev.ReactDevSettingsView
import com.smart.library.util.bus.STBusManager
import com.smart.library.util.image.impl.STImageFrescoHandler
import com.smart.library.widget.debug.STDebugFragment
import com.smart.library.util.okhttp.STOkHttpManager

@Suppress("unused", "PrivatePropertyName")
class ReactBusHandler : STBusManager.IBusHandler {

    override fun onInitOnce(application: Application) {
        STDebugFragment.childViewList.add(ReactDevSettingsView::class.java)
        val frescoConfig = STImageFrescoHandler.getConfigBuilder(STBaseApplication.DEBUG, STOkHttpManager.client).build()
        STDeployInitManager.init(application, frescoConfig)
    }

    override fun onUpgradeOnce(application: Application) {

    }

    override fun onCall(context: Context, busName: String, vararg params: Any) {

    }
}
