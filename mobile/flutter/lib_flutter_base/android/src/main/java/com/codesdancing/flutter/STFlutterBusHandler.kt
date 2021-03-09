package com.codesdancing.flutter

import android.app.Application
import android.content.Context
import com.smart.library.STInitializer
import com.smart.library.util.bus.STBusManager

class STFlutterBusHandler : STBusManager.IBusHandler {

    override fun onInitOnce(application: Application?, callback: ((success: Boolean) -> Unit)?) {
        application ?: return
        STFlutterInitializer.initial(application, STInitializer.debug())
        callback?.invoke(true)
    }

    override fun onUpgradeOnce(application: Application?) {
    }

    /**
     * smart://template/flutter?page=demo&params=jsonString
     */
    override fun onCall(context: Context?, busFunctionName: String, vararg params: Any) {
        when (busFunctionName) {
            "flutter/open" -> {
                if (context != null) {
                    val schemaUrl:String = (params.getOrNull(0) as? String) ?: "smart://template/flutter?page=flutter_settings&params="
                    STFlutterUtils.openFlutterPageBySchema(schemaUrl)
                }
            }
        }
    }

    override fun onAsyncCall(callback: ((key: Any?, value: Any?) -> Unit)?, context: Context?, busFunctionName: String, vararg params: Any) {

    }
}
