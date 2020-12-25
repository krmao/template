package com.smart.library.flutter.plugins

import android.app.Activity
import com.idlefish.flutterboost.FlutterBoost
import com.smart.library.util.STEventManager
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import org.json.JSONObject

@Suppress("unused", "UNUSED_PARAMETER")
class STFlutterEventPlugin : STFlutterBasePlugin() {

    @STFlutterPluginMethod
    fun addEventListener(activity: Activity?, flutterEngineWrapper: FlutterEngine, requestData: JSONObject, result: MethodChannel.Result) {
        if (activity != null) {
            val eventId = requestData.optString("eventId")
            val eventKey = requestData.optString("eventKey", "")
            STEventManager.register(eventId, eventKey) { key: String, value: Any? ->
                val jsonObject = (value as? JSONObject) ?: JSONObject()
                flutterBridgeChannel()?.apply {
                    jsonObject.put("eventId", eventId)
                    jsonObject.put("eventKey", eventKey)
                    sendEventToDart(key, jsonObject)
                }
            }
        }
        callbackSuccess(result, null)
    }

    @STFlutterPluginMethod
    fun removeEventListener(activity: Activity?, flutterEngineWrapper: FlutterEngine, requestData: JSONObject, result: MethodChannel.Result) {
        if (activity != null) {
            val eventId = requestData.optString("eventId")
            val eventKey = requestData.optString("eventKey", "")
            STEventManager.unregister(eventId, eventKey)
        }
        callbackSuccess(result, null)
    }

    @STFlutterPluginMethod
    fun sendEvent(activity: Activity?, flutterEngineWrapper: FlutterEngine, requestData: JSONObject, result: MethodChannel.Result) {
        val eventName = requestData.optString("eventName")
        val eventData = requestData.optJSONObject("eventInfo")

        STEventManager.sendEvent(eventName, eventData)
        callbackSuccess(result, null)
    }

    override fun getPluginName(): String {
        return "Event"
    }
}
