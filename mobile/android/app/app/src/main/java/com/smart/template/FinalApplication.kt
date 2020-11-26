package com.smart.template

import android.app.Activity
import android.app.Application
import android.content.Context
import com.smart.library.STInitializer
import com.smart.library.util.STReflectUtil
import com.smart.library.util.STSystemUtil
import com.smart.template.home.tab.FinalHomeTabActivity

@Suppress("unused", "SpellCheckingInspection")
class FinalApplication : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        STInitializer.attachApplicationBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()

        STInitializer.initialApplication(
            STInitializer.Config(
                application = this,
                appDebug = (STSystemUtil.getAppMetaData(applicationInfo, "DEBUG") ?: false) as Boolean,
                configChannel = STInitializer.ConfigChannel(appChannel = STReflectUtil.invokeJavaStaticMethod("com.meituan.android.walle.WalleChannelReader", "getChannel", arrayOf(Application::class.java), arrayOf(this)) as? String ?: ""),
                configClass = STInitializer.ConfigClass(homeClass = FinalHomeTabActivity::class.java),
                configBridge = STInitializer.ConfigBridge(bridgeHandler = object : STInitializer.BridgeHandler {
                    override fun handleBridge(activity: Activity?, functionName: String?, params: String?, callbackId: String?, callback: STInitializer.BridgeHandlerCallback?) {
                        FinalBridgeCommunication.handleBridge(activity, functionName, params, callbackId) { _callbackId: String?, resultJsonString: String? -> callback?.onCallback(_callbackId, resultJsonString) }
                    }
                }),
                configRN = STInitializer.ConfigRN(baseVersion = (STSystemUtil.getAppMetaData(applicationInfo, "VERSION_RN") ?: 0) as Int),
                configBundle = STInitializer.ConfigBundle(
                    bundleBusHandlerClassMap = hashMapOf(
                        "reactnative" to "com.smart.library.reactnative.RNBusHandler",
                        "flutter" to "com.smart.library.flutter.FlutterBusHandler",
                        "livestreaming" to "com.smart.library.livestreaming.LiveStreamingBusHandler",
                        "livestreamingpush" to "com.smart.library.livestreaming.push.LiveStreamingPushBusHandler"
                    )
                )
            )
        )
    }
}
