package com.smart.library.flutter

import android.app.Application
import android.content.Context
import com.smart.library.util.STLogUtil
import com.smart.library.util.bus.STBusManager
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.android.FlutterActivityLaunchConfigs
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor

/**
 * smart://template/flutter?page=home&params=jsonString
 */
@Suppress("unused", "PrivatePropertyName")
class FlutterBusHandler : STBusManager.IBusHandler {

    private lateinit var flutterEngine: FlutterEngine

    override fun onInitOnce(application: Application, callback: ((success: Boolean) -> Unit)?) {
        STLogUtil.w(TAG, "onInitOnce start")
        if (!FlutterEngineCache.getInstance().contains("my_engine_id")) {
            STLogUtil.w(TAG, "onInitOnce init cache flutter engine start")
            flutterEngine = FlutterEngine(application)
            flutterEngine.navigationChannel.setInitialRoute(SCHEMA_FLUTTER_PAGE_DEMO)
            flutterEngine.dartExecutor.executeDartEntrypoint(DartExecutor.DartEntrypoint.createDefault())
            FlutterEngineCache.getInstance().put("my_engine_id", flutterEngine)
            STLogUtil.w(TAG, "onInitOnce init cache flutter engine end")
        }
        STLogUtil.w(TAG, "onInitOnce end")
    }

    override fun onUpgradeOnce(application: Application) {
    }


    override fun onCall(context: Context?, busFunctionName: String, vararg params: Any) {
        when (busFunctionName) {
            "flutter/demo" -> {
                STLogUtil.w(TAG, "my_engine_id cacheEngine exists == ${FlutterEngineCache.getInstance().contains("my_engine_id")}")
                context?.startActivity(
                    FlutterActivity
                        .withCachedEngine("my_engine_id")
                        .backgroundMode(FlutterActivityLaunchConfigs.BackgroundMode.opaque)
                        .build(context)
                )
            }
            "flutter/order" -> {
                goToFlutterPage(context, SCHEMA_FLUTTER_PAGE_ORDER)
            }
            "flutter/not_found" -> {
                goToFlutterPage(context, SCHEMA_FLUTTER_PAGE_NOT_FOUNT)
            }
        }
    }

    private fun goToFlutterPage(context: Context?, url: String) {
        context?.startActivity(FlutterActivity.withNewEngine().initialRoute(url).backgroundMode(FlutterActivityLaunchConfigs.BackgroundMode.opaque).build(context))
    }

    override fun onAsyncCall(callback: ((key: Any?, value: Any?) -> Unit)?, context: Context?, busFunctionName: String, vararg params: Any) {

    }

    companion object {
        const val TAG = "[FLUTTER]"
        const val SCHEMA_FLUTTER_PAGE_DEMO = "smart://template/flutter?page=demo&params=jsonString"
        const val SCHEMA_FLUTTER_PAGE_ORDER = "smart://template/flutter?page=order&params=jsonString"
        const val SCHEMA_FLUTTER_PAGE_NOT_FOUNT = "smart://template/flutter?page=not_found&params=jsonString"
    }

}
