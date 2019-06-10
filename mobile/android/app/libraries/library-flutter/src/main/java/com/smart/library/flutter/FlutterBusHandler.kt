package com.smart.library.flutter

import android.app.Application
import android.content.Context
import com.smart.library.util.bus.STBusManager

@Suppress("unused", "PrivatePropertyName")
class FlutterBusHandler : STBusManager.IBusHandler {

    override fun onInitOnce(application: Application) {
        FlutterManager.startInitialization(application)
    }

    override fun onUpgradeOnce(application: Application) {

    }

    override fun onCall(context: Context, busName: String, vararg params: Any) {

    }
}
