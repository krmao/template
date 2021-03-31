package com.smart.library.flutter.plugins

import android.app.Activity
import com.smart.library.flutter.STFlutterBoostActivity
import com.smart.library.util.STLogUtil
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import org.json.JSONObject

@Suppress("UNUSED_PARAMETER")
class STFlutterPagePlugin : STFlutterBasePlugin() {

    @STFlutterPluginMethod
    fun enableExitWithDoubleBackPressed(activity: Activity?, flutterEngineWrapper: FlutterEngine, requestData: JSONObject?, result: MethodChannel.Result) {
        STLogUtil.w("FlutterToastPlugin", "requestData=$requestData")
        val enable = requestData?.optBoolean("enable") ?: false
        (activity as? STFlutterBoostActivity)?.enableExitWithDoubleBackPressed(enable)
        callbackSuccess(result)
    }

    override fun getPluginName(): String {
        return "Page"
    }
}