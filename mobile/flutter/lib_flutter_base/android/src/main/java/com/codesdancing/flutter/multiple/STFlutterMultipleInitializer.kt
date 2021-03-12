package com.codesdancing.flutter.multiple

import android.app.Application
import io.flutter.embedding.engine.FlutterEngineGroup
import io.flutter.embedding.engine.STFlutterEngineGroup


@Suppress("unused")
object STFlutterMultipleInitializer {

    const val TAG = "[flutter]"

    var application: Application? = null
        private set
    var flutterEngineGroup: STFlutterEngineGroup? = null
        private set

    var debug: Boolean = false
        private set

    private var isInitialized: Boolean = false

    @JvmStatic
    fun initial(application: Application?, debug: Boolean = STFlutterMultipleInitializer.debug) {
        if (isInitialized) return
        application ?: return

        this.application = application
        this.debug = debug
        flutterEngineGroup = STFlutterEngineGroup(application)

        isInitialized = true
    }
}