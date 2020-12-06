package com.smart.library.flutter.plugins

import android.app.Activity
import android.os.Build
import com.smart.library.STInitializer
import com.smart.library.util.STSystemUtil
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import org.json.JSONObject

@Suppress("unused", "UNUSED_PARAMETER")
class STFlutterApplicationPlugin : STFlutterBasePlugin() {

    override fun getPluginName(): String {
        return "Application"
    }

    @STFlutterPluginMethod
    fun getApplicationConstants(activity: Activity?, flutterEngineWrapper: FlutterEngine, requestData: JSONObject, result: MethodChannel.Result) {
        val cons = JSONObject()
        cons.put("deviceInfo", getDeviceInfo())
        cons.put("applicationInfo", getApplicationInfo())
        callbackSuccess(result, cons)
    }

    private fun getDeviceInfo(): JSONObject {
        val cons = JSONObject()
        cons.put("osVersion", "Android_" + Build.VERSION.SDK_INT)
        cons.put("deviceType", Build.BRAND + "_" + Build.MODEL)
        cons.put("deviceName", STSystemUtil.MANUFACTURER)
        return cons;
    }


    private fun getApplicationInfo(): JSONObject {
        val cons = JSONObject()
        cons.put("debug", STInitializer.debug())
        cons.put("versionCode", "${STSystemUtil.getAppVersionCode(STInitializer.application())}")
        cons.put("versionName", STSystemUtil.getAppVersionName(STInitializer.application()))
        return cons;
    }
}