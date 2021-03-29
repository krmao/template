package com.smart.template

import android.app.Activity
import android.app.Application
import android.content.Context
import com.simple.spiderman.SpiderMan
import com.smart.library.STInitializer
import com.smart.library.util.STReflectUtil
import com.smart.template.home.tab.FinalHomeTabActivity

@Suppress("unused", "SpellCheckingInspection")
class FinalApplication : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        // STInitializer.attachApplicationBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        SpiderMan.setTheme(R.style.SpiderManTheme_Dark)

        STInitializer.initialApplication(
            STInitializer.Config(
                application = this,
                appDebug = BuildConfig.DEBUG,
                configChannel = STInitializer.ConfigChannel({ STReflectUtil.invokeJavaStaticMethod("com.meituan.android.walle.WalleChannelReader", "getChannel", arrayOf(Application::class.java), arrayOf(this)) as? String ?: "" }),
                configClass = STInitializer.ConfigClass(homeClass = FinalHomeTabActivity::class.java),
                configBridge = STInitializer.ConfigBridge(bridgeHandler = object : STInitializer.BridgeHandler {
                    override fun handleBridge(activity: Activity?, functionName: String?, params: String?, callbackId: String?, callback: STInitializer.BridgeHandlerCallback?) {
                        FinalBridgeCommunication.handleBridge(activity, functionName, params, callbackId) { _callbackId: String?, resultJsonString: String? -> callback?.onCallback(_callbackId, resultJsonString) }
                    }
                }),
                configRN = STInitializer.ConfigRN(baseVersion = BuildConfig.VERSION_RN),
                configBundle = STInitializer.ConfigBundle(
                    bundleBusHandlerClassMap = hashMapOf(
                        "reactnative" to "com.smart.library.reactnative.RNBusHandler",
                        "flutter" to "com.smart.library.flutter.FlutterBusHandler",
                        "livestreaming" to "com.smart.library.livestreaming.LiveStreamingBusHandler",
                        "livestreamingpush" to "com.smart.library.livestreaming.push.LiveStreamingPushBusHandler"
                    )
                ),
                configLoading = STInitializer.ConfigLoading(
                    layoutLoadingID = R.layout.final_widget_frameloading_loading,
                    layoutNoDataID = R.layout.final_widget_frameloading_failure,
                    layoutFailureID = R.layout.final_widget_frameloading_empty
                ),
                configAdapterDesign = STInitializer.ConfigAdapterDesign(enableAdapterDesign = true, adapterDesignWidth = 750)
            )
        )
    }
}
