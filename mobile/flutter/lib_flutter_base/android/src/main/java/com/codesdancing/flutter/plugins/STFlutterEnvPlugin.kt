package com.codesdancing.flutter.plugins

import android.app.Activity
import com.smart.library.util.STURLManager
import com.smart.library.util.STNetworkUtil
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import org.json.JSONObject
import java.util.*

@Suppress("unused", "UNUSED_PARAMETER")
class STFlutterEnvPlugin : STFlutterBasePlugin() {

    @STFlutterPluginMethod
    fun getNetworkType(activity: Activity?, flutterEngineWrapper: FlutterEngine, requestData: JSONObject, result: MethodChannel.Result) {
        val jsonObject = JSONObject()
        jsonObject.putOpt("networkType", STNetworkUtil.getNetworkTypeInfo())
        callbackSuccess(result, jsonObject)
    }

    @STFlutterPluginMethod
    fun getEnv(activity: Activity?, flutterEngineWrapper: FlutterEngine, requestData: JSONObject, result: MethodChannel.Result) {
        val envObj = JSONObject()
        envObj.put("envType", STURLManager.curEnvironment.name.toUpperCase(Locale.getDefault()))
        callbackSuccess(result, envObj)
    }

    override fun getPluginName(): String {
        return "Env"
    }
}