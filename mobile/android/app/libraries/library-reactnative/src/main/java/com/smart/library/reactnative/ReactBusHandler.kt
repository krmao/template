package com.smart.library.reactnative

import android.app.Application
import android.content.Context
import com.smart.library.base.STBaseApplication
import com.smart.library.reactnative.dev.ReactDevSettingsView
import com.smart.library.util.bus.STBusManager
import com.smart.library.util.image.impl.STImageFrescoHandler
import com.smart.library.util.okhttp.STOkHttpManager
import com.smart.library.widget.debug.STDebugFragment

@Suppress("unused", "PrivatePropertyName")
class ReactBusHandler : STBusManager.IBusHandler {

    override fun onInitOnce(application: Application) {
        STDebugFragment.childViewList.add(ReactDevSettingsView::class.java)
        val frescoConfig = STImageFrescoHandler.getConfigBuilder(STBaseApplication.DEBUG, STOkHttpManager.client).build()
        STDeployInitManager.init(application, frescoConfig)
    }

    override fun onUpgradeOnce(application: Application) {

    }

    @Suppress("UNCHECKED_CAST")
    override fun onCall(context: Context?, busFunctionName: String, vararg params: Any) {
        when (busFunctionName) {
            "reactnative/open" -> {
                val page: String = (params.getOrNull(0) as? String) ?: ""
                val param: HashMap<String, Any?> = params.getOrNull(1) as? HashMap<String, Any?> ?: hashMapOf()
                val component: String = (params.getOrNull(2) as? String) ?: ""
                ReactJumper.goTo(context, page, hashMapOf("params" to param), component)
            }
        }
    }
}
