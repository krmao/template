package com.smart.library.flutter

import android.app.Activity
import android.app.Application
import android.os.SystemClock
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.annotation.UiThread
import com.idlefish.flutterboost.FlutterBoost
import com.idlefish.flutterboost.Utils
import com.idlefish.flutterboost.interfaces.INativeRouter
import com.smart.library.STInitializer
import com.smart.library.util.STLogUtil
import io.flutter.embedding.android.FlutterView
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

@Suppress("unused")
object STFlutterInitializer {

    const val TAG = "[flutter]"
    const val CHANNEL_METHOD = "smart.template.flutter/method"

    @JvmStatic
    var currentMethodChannel: MethodChannel? = null
        internal set

    var application: Application? = null
        private set

    var debug: Boolean = false
        private set

    lateinit var flutterEngine: FlutterEngine
    var cachedPageDemoFlutterEngineId: String? = null
    private var isInitialized: Boolean = false

    @JvmStatic
    fun initial(application: Application?, debug: Boolean = STFlutterInitializer.debug) {
        if (isInitialized) return

        this.application = application
        this.debug = debug

        val router = INativeRouter { context, url, urlParams, requestCode, exts ->
            val assembleUrl: String = Utils.assembleUrl(url, urlParams)
            STFlutterRouter.openContainer(context, assembleUrl, urlParams, requestCode, exts)
        }

        // 生命周期监听
        val boostLifecycleListener: FlutterBoost.BoostLifecycleListener = object : FlutterBoost.BoostLifecycleListener {
            override fun beforeCreateEngine() {}
            override fun onEngineCreated() {
                // 引擎创建后的操作，比如自定义MethodChannel，PlatformView等
            }

            override fun onPluginsRegistered() {}
            override fun onEngineDestroy() {}
        }

        // 生成Platform配置
        @Suppress("DEPRECATION")
        val platform = FlutterBoost.ConfigBuilder(application, router)
            .isDebug(true)
            .dartEntrypoint("main") //dart入口，默认为main函数，这里可以根据native的环境自动选择Flutter的入口函数来统一Native和Flutter的执行环境，（比如debugMode == true ? "mainDev" : "mainProd"，Flutter的main.dart里也要有这两个对应的入口函数）
            .whenEngineStart(FlutterBoost.ConfigBuilder.ANY_ACTIVITY_CREATED)
            .renderMode(FlutterView.RenderMode.texture)
            .lifecycleListener(boostLifecycleListener)
            .build()
        // 初始化
        FlutterBoost.instance().init(platform)
        isInitialized = true
    }

    /**
     *  Note: this method is invoked on the main thread.
     */
    @UiThread
    @JvmStatic
    fun onFlutterCallNativeMethod(activity: Activity?, call: MethodCall, result: MethodChannel.Result) {
        val argumentsJsonString: String = call.arguments()// STJsonUtil.toJson(call.arguments()) // flutter call jsonEncode
        STLogUtil.w(TAG, "onFlutterCallNativeMethod functionName=${call.method}, arguments=$argumentsJsonString")
        STInitializer.configBridge?.bridgeHandler?.handleBridge(activity, call.method, argumentsJsonString, null, object : STInitializer.BridgeHandlerCallback {
            override fun onCallback(callbackId: String?, resultJsonString: String?) {
                STLogUtil.w(TAG, "onFlutterCallNativeMethod return $resultJsonString")
                result.success(resultJsonString)
            }
        })
    }

    /**
     * 调用最上层的 Flutter method
     */
    @JvmStatic
    fun invokeFlutterMethod(@NonNull method: String, @Nullable arguments: Any, callback: MethodChannel.Result) {
        currentMethodChannel?.invokeMethod(method, arguments, callback)
    }

    @JvmStatic
    fun createCachedFlutterEngine(application: Application?, initialRoute: String): String {
        application ?: return ""

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