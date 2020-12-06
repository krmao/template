package com.smart.library.flutter.plugins

import android.app.Activity
import com.smart.library.STInitializer
import com.smart.library.util.STLogUtil
import com.smart.library.util.STThreadUtils
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.FlutterPlugin.FlutterPluginBinding
import io.flutter.plugin.common.JSONMethodCodec
import io.flutter.plugin.common.MethodChannel
import org.json.JSONObject
import java.lang.reflect.Method
import java.util.concurrent.ConcurrentHashMap

@Suppress("unused", "MemberVisibilityCanBePrivate")
object STFlutterBridgeChannel : FlutterPlugin {

    const val ERROR_NO_PLUGIN = "10001"
    const val ERROR_PLUGIN_ERROR = "10002"
    const val ERROR_BUSINESS_ERROR = "10003"

    private const val TAG = "[flutter_bridge_channel]"

    private const val BRIDGE_CHANNEL_NAME = "codesdancing.flutter.bridge/callNative"
    private const val BRIDGE_EVENT_NAME = "__codesdancing_flutter_event__"

    private val flutterBridgeMethodMap: MutableMap<String, CTFlutterPluginMethodHolder?> = ConcurrentHashMap()
    private var flutterMethodChannel: MethodChannel? = null

    override fun onAttachedToEngine(binding: FlutterPluginBinding) {
        flutterMethodChannel = MethodChannel(binding.binaryMessenger, BRIDGE_CHANNEL_NAME, JSONMethodCodec.INSTANCE)
        flutterMethodChannel?.setMethodCallHandler { call, result ->
            if (flutterBridgeMethodMap.containsKey(call.method)) {
                try {
                    val holder = flutterBridgeMethodMap[call.method]
                    holder?.plugin?.setFlutterBridgeChannel(this)
                    @Suppress("DEPRECATION")
                    flutterBridgeMethodMap[call.method]?.method?.invoke(
                        holder?.plugin, STInitializer.currentActivity(), binding.flutterEngine, call.arguments, result
                    )
                } catch (e: Exception) {
                    result.error(ERROR_PLUGIN_ERROR, ERROR_PLUGIN_ERROR, "invoke plugin method error:" + call.method + "," + e.message + "," + e.cause)
                }
            } else {
                result.notImplemented()
            }
        }
    }

    override fun onDetachedFromEngine(binding: FlutterPluginBinding) {
        flutterMethodChannel?.setMethodCallHandler(null)
        flutterMethodChannel = null
    }

    @JvmStatic
    fun sendEventToDart(eventName: String, data: JSONObject?) {
        val sendEvent = Runnable {
            val eventData = JSONObject()
            eventData.put("eventName", eventName)
            eventData.put("eventInfo", data)

            flutterMethodChannel?.invokeMethod(BRIDGE_EVENT_NAME, eventData, object : MethodChannel.Result {
                override fun success(result: Any?) {
                    STLogUtil.d(TAG, "invokeMethod success result=$result")
                }

                override fun error(errorCode: String, errorMessage: String?, errorDetails: Any?) {
                    STLogUtil.e(TAG, "invokeMethod error errorCode=$errorCode, errorMessage=$errorMessage, errorDetails=$errorDetails")
                }

                override fun notImplemented() {
                    STLogUtil.e(TAG, "invokeMethod notImplemented")
                }
            })
        }
        if (STThreadUtils.isMainThread()) {
            sendEvent.run()
        } else {
            STThreadUtils.runOnUiThread(sendEvent)
        }
    }

    @JvmStatic
    fun registerPlugins(plugins: List<STFlutterBasePlugin?>?) {
        plugins?.forEach { registerPlugin(it) }
    }

    @JvmStatic
    fun registerPlugin(plugin: STFlutterBasePlugin?) {
        plugin ?: return

        val methods: Array<Method> = plugin.javaClass.declaredMethods
        for (method in methods) {

            method.getAnnotation<STFlutterPluginMethod>(STFlutterPluginMethod::class.java) ?: continue

            val paramClasses = method.parameterTypes
            var isIllegal = false
            if (paramClasses.size >= 3) {
                if (paramClasses[0] != Activity::class.java || paramClasses[1] != FlutterEngine::class.java || paramClasses[2] != JSONObject::class.java || paramClasses[3] != MethodChannel.Result::class.java) {
                    isIllegal = true
                }
            } else {
                isIllegal = true
            }

            if (!isIllegal) {
                STLogUtil.e(TAG, "The parameter types of pluginMethod is illegal!")
                continue
            }

            flutterBridgeMethodMap[plugin.getPluginName().toString() + "-" + method.name] = CTFlutterPluginMethodHolder(plugin, method)
        }
    }

    class CTFlutterPluginMethodHolder(var plugin: STFlutterBasePlugin, var method: Method)
}