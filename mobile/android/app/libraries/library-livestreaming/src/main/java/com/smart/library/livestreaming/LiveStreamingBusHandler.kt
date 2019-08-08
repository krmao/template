package com.smart.library.livestreaming

import android.app.Application
import android.content.Context
import com.smart.library.util.bus.STBusManager

@Suppress("unused", "PrivatePropertyName")
class LiveStreamingBusHandler : STBusManager.IBusHandler {

    override fun onInitOnce(application: Application) {
    }

    override fun onUpgradeOnce(application: Application) {

    }

    override fun onCall(context: Context?, busFunctionName: String, vararg params: Any) {
        when (busFunctionName) {
            "livestreaming/play" -> {
                VideoActivity.intentTo(context, params.firstOrNull() as? String, params.getOrNull(1) as? String)
            }
            "livestreaming/playvideo" -> {
                VideoActivity.intentTo(context, params.firstOrNull() as? String, params.getOrNull(1) as? String)
            }
            "livestreaming/opensettings" -> {
                SettingsActivity.intentTo(context)
            }
        }

    }
}
