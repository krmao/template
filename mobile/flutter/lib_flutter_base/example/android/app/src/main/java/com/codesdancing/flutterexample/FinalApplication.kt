package com.codesdancing.flutterexample

import android.app.Activity
import android.app.Application
import android.content.Context
import com.smart.library.STInitializer

class FinalApplication : Application() {
    public override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        STInitializer.attachApplicationBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()

        STInitializer.initialApplication(
                STInitializer.Config(
                        application = this,
                        appDebug = BuildConfig.DEBUG,
                        configBundle = STInitializer.ConfigBundle(
                                bundleBusHandlerClassMap = hashMapOf(
                                        "flutter" to "com.codesdancing.flutter.STFlutterBusHandler"
                                )
                        ),
                        configBridge = STInitializer.ConfigBridge(bridgeHandler = object : STInitializer.BridgeHandler {
                            override fun handleBridge(activity: Activity?, functionName: String?, params: String?, callbackId: String?, callback: STInitializer.BridgeHandlerCallback?) {
                                FinalBridgeCommunication.handleBridge(activity, functionName, params, callbackId) { _callbackId: String?, resultJsonString: String? -> callback?.onCallback(_callbackId, resultJsonString) }
                            }
                        }),
                )
        )
    }
}