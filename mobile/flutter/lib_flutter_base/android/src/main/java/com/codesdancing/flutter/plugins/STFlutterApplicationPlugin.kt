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
    fun getAppInfo(activity: Activity?, flutterEngineWrapper: FlutterEngine, requestData: JSONObject, result: MethodChannel.Result) {
        val appInfojsonObject = JSONObject()
        appInfojsonObject.put("debug", STInitializer.debug())
        appInfojsonObject.put("osVersion", "ANDROID_" + Build.VERSION.SDK_INT)
        appInfojsonObject.put("deviceType", "ANDROID_" + Build.BRAND + "_" + Build.MODEL)
        appInfojsonObject.put("deviceName", "ANDROID_" + STSystemUtil.MANUFACTURER)
        appInfojsonObject.put("versionName", STSystemUtil.getAppVersionName(STInitializer.application()))

        val pageInfoJsonObject = JSONObject()
        pageInfoJsonObject.put("uniqueId", STFlutterPagePlugin.genUniqueId(activity) ?: "")
        pageInfoJsonObject.put("paramsJsonObjectString", STFlutterPagePlugin.getCurrentPageInitArguments(activity))

        appInfojsonObject.put("pageInfo", pageInfoJsonObject)

        callbackSuccess(result, appInfojsonObject)
    }
}