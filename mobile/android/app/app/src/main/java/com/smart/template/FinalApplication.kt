package com.smart.template

import android.app.Activity
import android.app.Application
import android.content.Context
import android.graphics.Color
import com.smart.library.STInitializer
import com.smart.library.util.STReflectUtil
import com.smart.library.util.STSystemUtil
import com.smart.template.home.tab.HomeTabActivity
import com.smart.template.library.STBridgeCommunication

@Suppress("unused", "SpellCheckingInspection")
class FinalApplication : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        STInitializer.attachApplicationBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()

        STInitializer.initOnApplicationCreate(this) {
            STInitializer.Options()
                .enableDebug((STSystemUtil.getAppMetaData("DEBUG") ?: false) as Boolean)
                .enableNetworkChangedReceiver(true)
                .enableCompatVectorFromResources(true)
                .enableActivityLifecycleCallbacks(true)
                .setDefaultTitleBarBackgroundColor(Color.BLACK)
                .setDefaultTitleBarTextColor(Color.WHITE)
                .setDefaultTitleBarTextSize(16f)
                .setApiURL(prdURL = "http://127.0.0.1:1234")
                .setChannel(STReflectUtil.invokeJavaStaticMethod("com.meituan.android.walle.WalleChannelReader", "getChannel", arrayOf(Application::class.java), arrayOf(this)) as? String ?: "")
                .setBridgeHandler(object : STInitializer.BridgeHandler {
                    override fun handleBridge(activity: Activity?, functionName: String?, params: String?, callbackId: String?, callback: STInitializer.BridgeHandlerCallback?) {
                        STBridgeCommunication.handleBridge(activity, functionName, params, callbackId) { _callbackId: String?, resultJsonString: String? ->
                            callback?.onCallback(_callbackId, resultJsonString)
                        }
                    }
                })
                .setMainClass(HomeTabActivity::class.java)
                .setLoginClass(null)
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

        if (isGodEyeEnabled()) {
            if (cn.hikyson.godeye.core.utils.ProcessUtils.isMainProcess(this)) {
                cn.hikyson.godeye.core.GodEye.instance().init(this)
                cn.hikyson.godeye.core.GodEye.instance().install(cn.hikyson.godeye.core.GodEyeConfig.fromAssets("config/config_godeye.xml"))
                cn.hikyson.godeye.monitor.GodEyeMonitor.work(this, 5388) // adb forward tcp:5388 tcp:5388 http://localhost:5388/index.html
            }
        }
    }

    private fun isGodEyeEnabled(): Boolean {
        try {
            Class.forName("cn.hikyson.godeye.core.GodEye")
            return true
        } catch (e: Exception) {
        }
        return false
    }

    fun exitApplication() {
        if (isGodEyeEnabled()) {
            cn.hikyson.godeye.monitor.GodEyeMonitor.shutDown()
            cn.hikyson.godeye.core.GodEye.instance().uninstall()
        }
    }

}
