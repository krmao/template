package com.smart.template.module.flutter

import android.annotation.SuppressLint
import android.app.Application
import android.support.annotation.NonNull
import io.flutter.view.FlutterMain
import io.flutter.view.FlutterNativeView

object FlutterManager {

    private var application: Application? = null

    @SuppressLint("StaticFieldLeak")
    var flutterNativeView: FlutterNativeView? = null
        private set
        get() {
            if (field == null) {
                if (application == null) {
                    throw AssertionError("must run startInitialization first")
                }
            }

            application?.let {
                field = FlutterNativeView(it)
            }
            return field
        }

    @JvmStatic
    fun startInitialization(@NonNull application: Application) {
        FlutterManager.application = application
        FlutterMain.startInitialization(application)
    }

}