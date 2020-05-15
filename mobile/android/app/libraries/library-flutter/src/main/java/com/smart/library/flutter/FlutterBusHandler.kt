package com.smart.library.flutter

import android.app.Application
import android.content.Context
import com.smart.library.util.bus.STBusManager

/**
 * smart://template/flutter?page=home&params=jsonString
 */
@Suppress("unused", "PrivatePropertyName")
class FlutterBusHandler : STBusManager.IBusHandler {

    var cachedPageDemoFlutterEngineId: String? = null
    override fun onInitOnce(application: Application, callback: ((success: Boolean) -> Unit)?) {
        cachedPageDemoFlutterEngineId = STFlutterManager.createCachedFlutterEngine(application, SCHEMA_FLUTTER_PAGE_DEMO)
    }

    override fun onUpgradeOnce(application: Application) {
    }


    override fun onCall(context: Context?, busFunctionName: String, vararg params: Any) {
        when (busFunctionName) {
            "flutter/demo" -> {
                if (context != null) {
                    STFlutterFragmentActivity.goToFlutterFragmentWithCachedEngine(context, cachedPageDemoFlutterEngineId)
                }
            }
            "flutter/order" -> {
                if (context != null) {
                    STFlutterFragmentActivity.goToFlutterFragmentWithNewEngine(context, SCHEMA_FLUTTER_PAGE_ORDER)
                }
            }
            "flutter/not_found" -> {
                if (context != null) {
                    STFlutterActivity.goToFlutterActivityWithNewEngine(context, SCHEMA_FLUTTER_PAGE_NOT_FOUNT)
                }
            }
        }
    }

    override fun onAsyncCall(callback: ((key: Any?, value: Any?) -> Unit)?, context: Context?, busFunctionName: String, vararg params: Any) {

    }

    companion object {
        const val SCHEMA_FLUTTER_PAGE_DEMO = "smart://template/flutter?page=demo&params=jsonString"
        const val SCHEMA_FLUTTER_PAGE_ORDER = "smart://template/flutter?page=order&params=jsonString"
        const val SCHEMA_FLUTTER_PAGE_NOT_FOUNT = "smart://template/flutter?page=not_found&params=jsonString"
    }

}
