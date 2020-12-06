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
            val eventName = requestData.optString("eventName")
            val sequenceId = requestData.optString("sequenceId", "")
            val containerId = requestData.optString("containerId", "")
            STEventManager.register(activity, eventName) { eventKey: String, value: Any? ->
                val jsonObject = (value as? JSONObject) ?: JSONObject()
                flutterBridgeChannel()?.apply {
                    jsonObject.put("sequenceId", sequenceId)
                    jsonObject.put("containerId", containerId)
                    sendEventToDart(eventKey, jsonObject)
                }
            }
        }
        callbackSuccess(result, null)
    }

    @STFlutterPluginMethod
    fun removeEventListener(activity: Activity?, flutterEngineWrapper: FlutterEngine, requestData: JSONObject, result: MethodChannel.Result) {
        if (activity != null) {
            val eventName = requestData.optString("eventName")
            val containerId = requestData.optString("containerId", "")
            if (containerId.isNotBlank()) {
                FlutterBoost.instance().containerManager().findContainerById(containerId)?.contextActivity?.let { host ->
                    STEventManager.unregister(host, eventName)
                } ?: run {
                    STEventManager.unregister(activity, eventName)
                }
            } else {
                STEventManager.unregister(activity, eventName)
            }
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
