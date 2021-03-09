package com.codesdancing.flutter

import android.app.Application
import com.codesdancing.flutter.plugins.*
import com.idlefish.flutterboost.FlutterBoost
import com.idlefish.flutterboost.FlutterBoostDelegate
import com.smart.library.STInitializer
import com.smart.library.util.STJsonUtil
import com.smart.library.util.STLogUtil
import io.flutter.FlutterInjector
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.FlutterJNI
import io.flutter.embedding.engine.dart.DartExecutor.DartEntrypoint
import io.flutter.view.FlutterMain
import java.util.*


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

        FlutterBoost.instance().setup(application, object : FlutterBoostDelegate {
            override fun pushNativeRoute(pageName: String, arguments: HashMap<String, String>) {
                STLogUtil.w(TAG, "pushNativeRoute pageName=$pageName, arguments=$arguments")
                val schemaUrl = "smart://template/flutter?page=$pageName&params=${STJsonUtil.toJson(arguments)}"  // native page 没有 uniqueId 字段
                STLogUtil.w(TAG, "pushNativeRoute schemaUrl=$schemaUrl, arguments=$arguments")
                STInitializer.openSchema(FlutterBoost.instance().currentActivity(), schemaUrl) // 确保所有跳转都走 handleBridge
            }

            override fun pushFlutterRoute(pageName: String?, uniqueId: String?, arguments: HashMap<String, String>?) {
                STFlutterUtils.openFlutterPageByName(FlutterBoost.instance().currentActivity(), pageName, arguments)
                STLogUtil.w(TAG, "pushFlutterRoute pageName=$pageName, uniqueId=$uniqueId, arguments=$arguments")
                val schemaUrl = "smart://template/flutter?page=$pageName&params=${STJsonUtil.toJson(arguments)}&uniqueId=$uniqueId" // flutter page 有 uniqueId 字段
                STLogUtil.w(TAG, "pushFlutterRoute schemaUrl=$schemaUrl, uniqueId=$uniqueId, arguments=$arguments")
                STInitializer.openSchema(FlutterBoost.instance().currentActivity(), schemaUrl) // 确保所有跳转都走 handleBridge
            }
        }) { engine: FlutterEngine ->
            STLogUtil.w(TAG, "onEngineCreated") // 引擎创建后的操作，比如自定义MethodChannel，PlatformView等
            STFlutterPluginRegistrant.registerWith(engine)
        }

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