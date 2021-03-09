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
            val url = requestData.optString("url", "")
            val params = requestData.optJSONObject("urlParams")
            val exts = requestData.optJSONObject("exts")

            STFlutterUtils.openFlutterPageBySchema(activity,url)
            /*FlutterBoost.instance().containerManager().currentTopRecord.container.contextActivity.apply {
                FlutterBoost.instance().platform().openContainer(this, url, STJsonUtil.toMapOrNull(params?.toString()), 0, STJsonUtil.toMapOrNull(exts?.toString()))
            }*/
            result.success("OK")
        } else {
            result.error("1", "url is empty", requestData)
        }
    }

    override fun getPluginName(): String {
        return "URL"
    }

}