package com.smart.template.module.flutter

import android.app.Application
import io.flutter.view.FlutterMain

object FlutterManager {

    @JvmStatic
    fun startInitialization(application: Application) {
        FlutterMain.startInitialization(application)
    }

}