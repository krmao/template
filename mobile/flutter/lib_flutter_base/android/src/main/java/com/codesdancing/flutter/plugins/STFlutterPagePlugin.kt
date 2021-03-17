package com.codesdancing.flutter.plugins

import android.app.Activity
import android.content.Intent
import com.codesdancing.flutter.multiple.STFlutterMultipleActivity
import com.smart.library.base.STBaseActivity
import com.smart.library.util.STLogUtil
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import org.json.JSONObject

@Suppress("UNUSED_PARAMETER")
class STFlutterPagePlugin : STFlutterBasePlugin() {

    @STFlutterPluginMethod
    fun genUniqueId(activity: Activity?, flutterEngineWrapper: FlutterEngine, requestData: JSONObject?, result: MethodChannel.Result) {
        val uniqueId = (activity as? STFlutterMultipleActivity)?.genUniqueId()
        STLogUtil.w("STFlutterPagePlugin", "genUniqueId uniqueId=$uniqueId")
        callbackSuccess(result, uniqueId)
    }

    @STFlutterPluginMethod
    fun popPage(activity: Activity?, flutterEngineWrapper: FlutterEngine, requestData: JSONObject?, result: MethodChannel.Result) {
        STLogUtil.w("STFlutterPagePlugin", "popPage requestData=$requestData")
        val argumentsJsonString: String? = requestData?.optString("argumentsJsonString")
        activity?.setResult(Activity.RESULT_OK, Intent().apply {
            putExtra(STFlutterMultipleActivity.KEY_ARGUMENTS_JSON_STRING, argumentsJsonString)
        })
        activity?.finish()
        callbackSuccess(result, null)
    }

    @STFlutterPluginMethod
    fun getCurrentPageInitArguments(activity: Activity?, flutterEngineWrapper: FlutterEngine, requestData: JSONObject?, result: MethodChannel.Result) {
        STLogUtil.w("STFlutterPagePlugin", "getCurrentPageInitArguments requestData=$requestData")
        val argumentsJsonString: String? = getCurrentPageInitArguments(activity)
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

    companion object {
        @JvmStatic
        fun getCurrentPageInitArguments(activity: Activity?): String {
            return activity?.intent?.getStringExtra(STFlutterMultipleActivity.KEY_ARGUMENTS_JSON_STRING) ?: "{}"
        }

        @STFlutterPluginMethod
        fun genUniqueId(activity: Activity?): String? {
            return (activity as? STFlutterMultipleActivity)?.genUniqueId()
        }
    }
}