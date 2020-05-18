package com.smart.library.flutter

import android.app.Activity
import android.app.Application
import android.os.SystemClock
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.annotation.UiThread
import com.smart.library.util.STJsonUtil
import com.smart.library.util.STLogUtil
import com.smart.template.library.STBridgeCommunication
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

@Suppress("unused")
object STFlutterManager {

    const val TAG = STFlutterFragmentActivity.TAG
    const val CHANNEL_METHOD = "smart.template.flutter/method"

    @JvmStatic
    var currentMethodChannel: MethodChannel? = null
        internal set

    /**
     *  Note: this method is invoked on the main thread.
     */
    @UiThread
    @JvmStatic
    fun onFlutterCallNativeMethod(activity: Activity?, call: MethodCall, result: MethodChannel.Result) {
        val argumentsJsonString: String = STJsonUtil.toJson(call.arguments())
        STLogUtil.w(TAG, "onFlutterCallNativeMethod functionName=${call.method}, arguments=$argumentsJsonString")
        STBridgeCommunication.handleBridge(activity, functionName = call.method, params = argumentsJsonString) { _: String?, resultJsonString: String? ->
            STLogUtil.w(TAG, "onFlutterCallNativeMethod return $resultJsonString")
            result.success(resultJsonString)
        }
    }

    /**
     * 调用最上层的 Flutter method
     */
    @JvmStatic
    fun invokeFlutterMethod(@NonNull method: String, @Nullable arguments: Any, callback: MethodChannel.Result) {
        currentMethodChannel?.invokeMethod(method, arguments, callback)
    }

    @JvmStatic
    fun createCachedFlutterEngine(application: Application, initialRoute: String): String {
        val engineId = SystemClock.elapsedRealtime().toString()
        if (!FlutterEngineCache.getInstance().contains(engineId)) {
            val flutterEngine = FlutterEngine(application)
            flutterEngine.navigationChannel.setInitialRoute(initialRoute)
            flutterEngine.dartExecutor.executeDartEntrypoint(DartExecutor.DartEntrypoint.createDefault())
            FlutterEngineCache.getInstance().put(engineId, flutterEngine)
        }
        return engineId
    }

}