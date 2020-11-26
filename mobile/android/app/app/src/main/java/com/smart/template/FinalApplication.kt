package com.smart.template

import android.app.Activity
import android.app.Application
import android.content.Context
import android.graphics.Color
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

        STInitializer.initOnApplicationCreate(this)
            .initOptions(
                STInitializer.Options()
                    .enableDebug((STSystemUtil.getAppMetaData("DEBUG") ?: false) as Boolean)
                    .enableNetworkChangedReceiver(true)
                    .enableCompatVectorFromResources(true)
                    .setRNCheckUpdateHTTPGetUrl("")
                    .setRNBundlePathInAssets("bundle-rn.zip")
                    .setRNBaseVersion((STSystemUtil.getAppMetaData("VERSION_RN") ?: 0) as Int)
                    .setDefaultTitleBarBackgroundColor(Color.BLACK)
                    .setDefaultTitleBarTextColor(Color.WHITE)
                    .setDefaultTitleBarTextSize(16f)
                    .setApiURL(prdURL = "http://127.0.0.1:1234")
                    .setChannel(STReflectUtil.invokeJavaStaticMethod("com.meituan.android.walle.WalleChannelReader", "getChannel", arrayOf(Application::class.java), arrayOf(this)) as? String ?: "")
                    .setBridgeHandler(object : STInitializer.BridgeHandler {
                        override fun handleBridge(activity: Activity?, functionName: String?, params: String?, callbackId: String?, callback: STInitializer.BridgeHandlerCallback?) {
                            FinalBridgeCommunication.handleBridge(activity, functionName, params, callbackId) { _callbackId: String?, resultJsonString: String? ->
                                callback?.onCallback(_callbackId, resultJsonString)
                            }
                        }
                    })
                    .setMainClass(FinalHomeTabActivity::class.java)
                    .setLoginClass(null)
            )
            .initImageManager()
            .initBus(
                hashMapOf(
                    "reactnative" to "com.smart.library.reactnative.RNBusHandler",
                    "flutter" to "com.smart.library.flutter.FlutterBusHandler",
                    "livestreaming" to "com.smart.library.livestreaming.LiveStreamingBusHandler",
                    "livestreamingpush" to "com.smart.library.livestreaming.push.LiveStreamingPushBusHandler"
                )
            )
    }
}
