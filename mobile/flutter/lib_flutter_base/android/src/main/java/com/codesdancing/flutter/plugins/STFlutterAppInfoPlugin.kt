package com.codesdancing.flutter.plugins

import android.app.Activity
import android.os.Build
import com.smart.library.STInitializer
import com.smart.library.util.STSystemUtil
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import org.json.JSONObject

@Suppress("unused", "UNUSED_PARAMETER")
class STFlutterAppInfoPlugin : STFlutterBasePlugin() {

    override fun getPluginName(): String {
        return "AppInfo"
    }

    @STFlutterPluginMethod
    fun getAppInfo(activity: Activity?, flutterEngineWrapper: FlutterEngine, requestData: JSONObject, result: MethodChannel.Result) {
        val appInfoJsonObject = JSONObject()
        appInfoJsonObject.put("debug", STInitializer.debug())
        appInfoJsonObject.put("osVersion", "ANDROID_" + Build.VERSION.SDK_INT)
        appInfoJsonObject.put("deviceType", "ANDROID_" + Build.BRAND + "_" + Build.MODEL)
        appInfoJsonObject.put("deviceName", "ANDROID_" + STSystemUtil.MANUFACTURER)
        appInfoJsonObject.put("versionName", STSystemUtil.getAppVersionName(STInitializer.application()))

        val pageInfoJsonObject = JSONObject()
        pageInfoJsonObject.put("uniqueId", STFlutterPagePlugin.genUniqueId(activity) ?: "")
        pageInfoJsonObject.put("paramsJsonObjectString", STFlutterPagePlugin.getCurrentPageInitArguments(activity))

        appInfoJsonObject.put("pageInfo", pageInfoJsonObject)

        callbackSuccess(result, appInfoJsonObject)
    }
}