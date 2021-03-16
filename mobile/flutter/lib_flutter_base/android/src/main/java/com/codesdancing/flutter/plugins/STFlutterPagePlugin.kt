package com.codesdancing.flutter.plugins

import android.app.Activity
import android.content.Intent
import com.codesdancing.flutter.multiple.STFlutterMultipleActivity
import com.smart.library.base.STBaseActivity
import com.smart.library.util.STLogUtil
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import org.json.JSONObject

class STFlutterPagePlugin : STFlutterBasePlugin() {

    @STFlutterPluginMethod
    fun popPage(activity: Activity?, flutterEngineWrapper: FlutterEngine, requestData: JSONObject?, result: MethodChannel.Result) {
        STLogUtil.w("STFlutterPagePlugin", "popPage requestData=$requestData")
        val argumentsJsonString: String? = requestData?.optString("argumentsJsonString")
        activity?.setResult(Activity.RESULT_OK, Intent().apply {
            putExtra(STFlutterMultipleActivity.KEY_ARGUMENTS_JSON_STRING, argumentsJsonString)
        })
        callbackSuccess(result, null)
    }

    @STFlutterPluginMethod
    fun getCurrentPageInitArguments(activity: Activity?, flutterEngineWrapper: FlutterEngine, requestData: JSONObject?, result: MethodChannel.Result) {
        STLogUtil.w("STFlutterPagePlugin", "getCurrentPageInitArguments requestData=$requestData")
        val argumentsJsonString: String? = activity?.intent?.getStringExtra(STFlutterMultipleActivity.KEY_ARGUMENTS_JSON_STRING)
        callbackSuccess(result, argumentsJsonString)
    }

    @STFlutterPluginMethod
    fun enableExitWithDoubleBackPressed(activity: Activity?, flutterEngineWrapper: FlutterEngine, requestData: JSONObject?, result: MethodChannel.Result) {
        STLogUtil.w("STFlutterPagePlugin", "enableExitWithDoubleBackPressed requestData=$requestData")
        val enable = requestData?.optBoolean("enable") ?: false
        (activity as? STBaseActivity)?.enableExitWithDoubleBackPressed(enable)
        callbackSuccess(result, null)
    }

    override fun getPluginName(): String {
        return "Page"
    }

}