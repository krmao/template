package com.codesdancing.flutter.plugins

import android.app.Activity
import android.text.TextUtils
import com.smart.library.STInitializer
import com.smart.library.util.STLogUtil
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import org.json.JSONObject

@Suppress("UNUSED_PARAMETER", "unused")
class STFlutterBridgeCompactPlugin : STFlutterBasePlugin() {

    @STFlutterPluginMethod
    fun callNative(activity: Activity?, flutterEngineWrapper: FlutterEngine, requestData: JSONObject?, result: MethodChannel.Result) {
        STLogUtil.w("STFlutterBridgeCompactPlugin", "requestData=$requestData")
        val functionName = requestData?.optString("functionName")
        val params = requestData?.optString("params")
        STLogUtil.w("STFlutterBridgeCompactPlugin", "functionName=$functionName, params=$params")

        if (!TextUtils.isEmpty(functionName)) {
            STInitializer.configBridge?.bridgeHandler?.handleBridge(activity, functionName, params, null, object : STInitializer.BridgeHandlerCallback {
                override fun onCallback(callbackId: String?, resultJsonString: String?) {
                    callbackSuccess(result, successData = resultJsonString)
                }
            })
        } else {
            callbackFail(result, errorMsg = "functionName is null or empty !!!", failData = null)
        }
    }

    override fun getPluginName(): String {
        return "BridgeCompact"
    }
}
