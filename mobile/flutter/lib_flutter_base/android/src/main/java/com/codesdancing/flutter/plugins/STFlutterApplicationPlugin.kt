package com.codesdancing.flutter.plugins

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
        val jsonObject = JSONObject()
        jsonObject.put("deviceInfo", getDeviceInfo())
        jsonObject.put("applicationInfo", getApplicationInfo())
        jsonObject.put("argumentsJsonString", STFlutterPagePlugin.getCurrentPageInitArguments(activity))
        jsonObject.put("uniqueId", STFlutterPagePlugin.genUniqueId(activity))
        callbackSuccess(result, jsonObject)
    }

    private fun getDeviceInfo(): JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put("osVersion", "Android_" + Build.VERSION.SDK_INT)
        jsonObject.put("deviceType", Build.BRAND + "_" + Build.MODEL)
        jsonObject.put("deviceName", STSystemUtil.MANUFACTURER)
        return jsonObject;
    }


    private fun getApplicationInfo(): JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put("debug", STInitializer.debug())
        jsonObject.put("versionCode", "${STSystemUtil.getAppVersionCode(STInitializer.application())}")
        jsonObject.put("versionName", STSystemUtil.getAppVersionName(STInitializer.application()))
        return jsonObject;
    }
}