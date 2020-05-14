package com.smart.library.flutter

import android.app.Application
import android.content.Context
import com.smart.library.util.bus.STBusManager
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor

@Suppress("unused", "PrivatePropertyName")
class FlutterBusHandler : STBusManager.IBusHandler {

    lateinit var flutterEngine: FlutterEngine

    override fun onInitOnce(application: Application, callback: ((success: Boolean) -> Unit)?) {
        // Instantiate a FlutterEngine.
        flutterEngine = FlutterEngine(application)
        // Configure an initial route.
        flutterEngine.navigationChannel.setInitialRoute("/")
        // Start executing Dart code to pre-warm the FlutterEngine.
        flutterEngine.dartExecutor.executeDartEntrypoint(DartExecutor.DartEntrypoint.createDefault())
        // Cache the FlutterEngine to be used by FlutterActivity or FlutterFragment.
        FlutterEngineCache.getInstance().put("my_engine_id", flutterEngine)
    }

    override fun onUpgradeOnce(application: Application) {

    }

    override fun onCall(context: Context?, busFunctionName: String, vararg params: Any) {
        when (busFunctionName) {
            "flutter/module" -> {
                context?.startActivity(
                    FlutterActivity
                        // .withNewEngine()
                        // .initialRoute("/index")
                        .withCachedEngine("my_engine_id")
                        // .backgroundMode(io.flutter.embedding.android.FlutterActivityLaunchConfigs.BackgroundMode.transparent)
                        .build(context)
                )
            }
        }
    }

    override fun onAsyncCall(callback: ((key: Any?, value: Any?) -> Unit)?, context: Context?, busFunctionName: String, vararg params: Any) {

    }

}
