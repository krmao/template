package com.codesdancing.flutter.plugins

import android.app.Activity
import com.codesdancing.flutter.STFlutterUtils
import com.smart.library.STInitializer
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import org.json.JSONObject

@Suppress("unused", "UNUSED_PARAMETER")
class STFlutterURLPlugin : STFlutterBasePlugin() {

    @STFlutterPluginMethod
    fun openURL(activity: Activity?, flutterEngineWrapper: FlutterEngine, requestData: JSONObject, result: MethodChannel.Result) {
        if (requestData.has("url")) {
            val schemaUrl = requestData.optString("url", "")
            STInitializer.openSchema(activity,schemaUrl, object :STInitializer.BridgeHandlerCallback{
                override fun onCallback(callbackId: String?, resultJsonString: String?) {
                    result.success(resultJsonString)
                }
            })
            // STFlutterUtils.openFlutterPageBySchema(activity, schemaUrl)
            // result.success("OK")
        } else {
            result.error("1", "url is empty", requestData)
        }
    }

    override fun getPluginName(): String {
        return "URL"
    }

}