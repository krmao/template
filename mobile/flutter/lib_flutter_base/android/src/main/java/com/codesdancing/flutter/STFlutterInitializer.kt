package com.codesdancing.flutter

import android.app.Application
import com.codesdancing.flutter.boost.STFlutterBoostInitializer
import com.codesdancing.flutter.multiple.STFlutterMultipleInitializer


@Suppress("unused")
object STFlutterInitializer {

    const val TAG = "[flutter]"

    @JvmStatic
    var application: Application? = null
        private set

    @JvmStatic
    var enableMultiple: Boolean = true
        private set

    @JvmStatic
    var debug: Boolean = false
        private set

    private var isInitialized: Boolean = false

    @JvmStatic
    fun initial(application: Application?, debug: Boolean = STFlutterInitializer.debug) {
        if (isInitialized) return
        application ?: return

        this.application = application
        this.debug = debug

        if (enableMultiple) {
            STFlutterMultipleInitializer.initial(application, debug)
        } else {
            STFlutterBoostInitializer.initial(application, debug)
        }
        
        isInitialized = true
    }
}