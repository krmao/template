package com.smart.library.flutter.plugins

import android.app.Activity
import android.text.TextUtils
import com.smart.library.util.STLogUtil
import com.smart.library.util.STToastUtil
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import org.json.JSONObject

@Suppress("UNUSED_PARAMETER")
class STFlutterToastPlugin : STFlutterBasePlugin() {

    @STFlutterPluginMethod
    fun show(activity: Activity?, flutterEngineWrapper: FlutterEngine, requestData: JSONObject?, result: MethodChannel.Result) {
        STLogUtil.w("FlutterToastPlugin", "requestData=$requestData")
        val message = requestData?.optString("message")
        if (requestData != null && !TextUtils.isEmpty(message)) {
            STToastUtil.show(message)
        }
        callbackSuccess(result)
    }

    override fun getPluginName(): String {
        return "Toast"
    }
}
