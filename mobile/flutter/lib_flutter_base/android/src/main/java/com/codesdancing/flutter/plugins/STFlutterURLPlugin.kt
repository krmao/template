package com.codesdancing.flutter.plugins

import android.app.Activity
import com.codesdancing.flutter.STFlutterUtils
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import org.json.JSONObject

@Suppress("unused", "UNUSED_PARAMETER")
class STFlutterURLPlugin : STFlutterBasePlugin() {

    @STFlutterPluginMethod
    fun openURL(activity: Activity?, flutterEngineWrapper: FlutterEngine, requestData: JSONObject, result: MethodChannel.Result) {
        if (requestData.has("url")) {
            val schemaUrl = requestData.optString("url", "")
            STFlutterUtils.openFlutterPageBySchema(activity, schemaUrl)
            result.success("OK")
        } else {
            result.error("1", "url is empty", requestData)
        }
    }

    override fun getPluginName(): String {
        return "URL"
    }

}