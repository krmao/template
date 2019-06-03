package com.smart.template.module.flutter

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.support.annotation.NonNull
import io.flutter.view.FlutterMain

@Suppress("UNCHECKED_CAST", "MemberVisibilityCanBePrivate")
@SuppressLint("StaticFieldLeak")
object FlutterManager {

    const val CHANNEL_METHOD = "smart.flutter.io/methods"
    const val KEY_FLUTTER_STRING_RESULT = "KEY_FLUTTER_STRING_RESULT"

    var currentActivity: Activity? = null
        internal set
    var application: Application? = null
        private set

    @JvmStatic
    fun startInitialization(@NonNull application: Application) {
        FlutterManager.application = application
        FlutterMain.startInitialization(application)
    }
    
}