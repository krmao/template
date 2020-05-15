package com.smart.library.flutter

import android.app.Activity
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.annotation.UiThread
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

@Suppress("unused")
object STFlutterManager {

    private const val CHANNEL_METHOD = "smart.template.flutter/method"
    private var methodChannel: MethodChannel? = null

    /**
     * 为 Flutter 绑定 channel
     */
    internal fun configureFlutterEngine(activity: Activity?, @NonNull flutterEngine: FlutterEngine) {
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL_METHOD).apply {
            setMethodCallHandler { call: MethodCall, result: MethodChannel.Result ->
                onFlutterCallNativeMethod(activity, this, call, result)
            }
        }
    }

    /**
     *  Note: this method is invoked on the main thread.
     */
    @UiThread
    @JvmStatic
    fun onFlutterCallNativeMethod(activity: Activity?, methodChannel: MethodChannel, call: MethodCall, result: MethodChannel.Result) {
        this.methodChannel = methodChannel
        when (call.method) {
            "getArguments" -> {
                result.success(activity?.intent)
            }
            else -> result.notImplemented()
        }
    }

    /**
     * 调用最上层的 Flutter method
     */
    fun invokeFlutterMethod(@NonNull method: String, @Nullable arguments: Any, callback: MethodChannel.Result) {
        methodChannel?.invokeMethod(method, arguments, callback)
    }

}