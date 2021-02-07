package com.codesdancing.flutter

import android.app.Activity
import android.app.Application
import com.codesdancing.flutter.plugins.*
import com.idlefish.flutterboost.FlutterBoost
import com.idlefish.flutterboost.Utils
import com.idlefish.flutterboost.interfaces.INativeRouter
import com.smart.library.STInitializer
import com.smart.library.util.STJsonUtil
import com.smart.library.util.STLogUtil
import io.flutter.FlutterInjector
import io.flutter.embedding.android.FlutterEngineProvider
import io.flutter.embedding.android.FlutterView
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.FlutterJNI
import io.flutter.embedding.engine.dart.DartExecutor.DartEntrypoint
import io.flutter.view.FlutterMain

@Suppress("unused")
object STFlutterInitializer {

    const val TAG = "[flutter]"
    const val CHANNEL_METHOD = "smart.template.flutter/method"

    private var application: Application? = null

    var debug: Boolean = false
        private set

    private var isInitialized: Boolean = false

    @JvmStatic
    fun initial(application: Application?, debug: Boolean = STFlutterInitializer.debug) {
        if (isInitialized) return
        application ?: return

        this.application = application
        this.debug = debug

        val router = INativeRouter { context, url, urlParams, requestCode, exts ->
            STLogUtil.w(TAG, "INativeRouter#openContainer, url=$url, urlParams=$urlParams, requestCode=$requestCode, exts=$exts")
            // STFlutterRouter.openByName(context, url, urlParams, requestCode, exts)
            val schemaUrl = "smart://template/flutter?page=$url&params=${STJsonUtil.toJson(urlParams)}"
            STLogUtil.w(TAG, "INativeRouter#openContainer, schemaUrl=$schemaUrl, requestCode=$requestCode, exts=$exts")
            STInitializer.openSchema(context as? Activity, schemaUrl) // 确保所有跳转都走 handleBridge
        }

        val initialRoute = "flutter_settings"
        val flutterEngine: FlutterEngine? = getOrCreateCachedFlutterEngine(application, cacheEngineId = "flutter_boost_engine", initialRoute = initialRoute, automaticallyRegisterPlugins = false)
        val flutterEngineProvider: FlutterEngineProvider? = if (flutterEngine == null) null else {
            FlutterEngineProvider { flutterEngine }
        }

        // 生命周期监听
        val boostLifecycleListener: FlutterBoost.BoostLifecycleListener = object : FlutterBoost.BoostLifecycleListener {
            override fun beforeCreateEngine() {
                STLogUtil.w(TAG, "beforeCreateEngine")
            }

            override fun onEngineCreated() {
                // 引擎创建后的操作，比如自定义MethodChannel，PlatformView等
                STLogUtil.w(TAG, "onEngineCreated")
                STFlutterPluginRegistrant.registerWith(FlutterBoost.instance().engineProvider())
            }

            override fun onPluginsRegistered() {
                STLogUtil.w(TAG, "onPluginsRegistered ${FlutterBoost.instance().engineProvider().plugins}")
            }

            override fun onEngineDestroy() {
                STLogUtil.w(TAG, "onEngineDestroy")
            }
        }

        // 生成Platform配置
        @Suppress("DEPRECATION")
        val platform = FlutterBoost.ConfigBuilder(application, router)
                .isDebug(debug)
                .dartEntrypoint("main") //dart入口，默认为main函数，这里可以根据native的环境自动选择Flutter的入口函数来统一Native和Flutter的执行环境，（比如debugMode == true ? "mainDev" : "mainProd"，Flutter的main.dart里也要有这两个对应的入口函数）
                .whenEngineStart(FlutterBoost.ConfigBuilder.ANY_ACTIVITY_CREATED)
                .renderMode(FlutterView.RenderMode.texture)
                .lifecycleListener(boostLifecycleListener)
                .initialRoute(initialRoute)
                .flutterEngineProvider(flutterEngineProvider)
                .shellArgs(listOf())
                .build()
        // 初始化

        STFlutterBridgeChannel.INSTANCE().registerPlugins(
                mutableListOf(
                        STFlutterBridgeCompactPlugin(),
                        STFlutterPagePlugin(),
                        STFlutterToastPlugin(),
                        STFlutterURLPlugin(),
                        STFlutterEnvPlugin(),
                        STFlutterEventPlugin(),
                        STFlutterApplicationPlugin()
                )
        )

        FlutterBoost.instance().init(platform)
        isInitialized = true
    }

    /**
     * @param cacheEngineId null or empty will not cache
     * @param automaticallyRegisterPlugins flutter_boost will registerPlugins self, so automaticallyRegisterPlugins = false
     */
    @JvmStatic
    @JvmOverloads
    fun getOrCreateCachedFlutterEngine(application: Application?, cacheEngineId: String? = null, initialRoute: String? = null, dartVmArgs: Array<String>? = null, automaticallyRegisterPlugins: Boolean = true): FlutterEngine? {
        application ?: return null

        if (cacheEngineId.isNullOrBlank() || !FlutterEngineCache.getInstance().contains(cacheEngineId)) {
            // Starts initialization of the native system.
            // This loads the Flutter engine's native library to enable subsequent JNI calls. This also
            // starts locating and unpacking Dart resources packaged in the app's APK.
            // Calling this method multiple times has no effect.
            FlutterMain.startInitialization(application)
            // Blocks until initialization of the native system has completed.
            // Calling this method multiple times has no effect.
            FlutterMain.ensureInitializationComplete(application, dartVmArgs)

            // create flutterEngine
            val flutterEngine = FlutterEngine(application, FlutterInjector.instance().flutterLoader(), FlutterJNI(), dartVmArgs, automaticallyRegisterPlugins)

            // init flutterEngine

            if (!flutterEngine.dartExecutor.isExecutingDart) {
                if (initialRoute != null) flutterEngine.navigationChannel.setInitialRoute(initialRoute)
                flutterEngine.dartExecutor.executeDartEntrypoint(DartEntrypoint.createDefault())
            }

            // cache flutterEngine
            if (!cacheEngineId.isNullOrBlank()) FlutterEngineCache.getInstance().put(cacheEngineId, flutterEngine)
            return flutterEngine
        }
        return FlutterEngineCache.getInstance()[cacheEngineId]
    }

}