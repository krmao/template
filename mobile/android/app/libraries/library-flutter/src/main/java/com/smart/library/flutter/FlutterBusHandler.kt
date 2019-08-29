package com.smart.library.flutter

import android.app.Application
import android.content.Context
import android.content.Intent
import com.smart.library.flutter.test.MineActivity
import com.smart.library.util.bus.STBusManager

@Suppress("unused", "PrivatePropertyName")
class FlutterBusHandler : STBusManager.IBusHandler {

    override fun onInitOnce(application: Application) {

        FlutterInitializer.startInitialization(application) { context: Context, url: String, requestCode: Int ->
            FlutterRouter.find(url)?.goTo?.invoke(context, hashMapOf(), requestCode) ?: false
        }
    }

    override fun onUpgradeOnce(application: Application) {

    }

    override fun onCall(context: Context?, busFunctionName: String, vararg params: Any) {
        when (busFunctionName) {
            "flutter/main" -> {
                context?.startActivity(Intent(context, MineActivity::class.java))
            }
        }
    }

}
