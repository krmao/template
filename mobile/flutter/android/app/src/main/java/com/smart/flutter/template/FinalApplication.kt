package com.smart.flutter.template

import android.annotation.SuppressLint
import android.content.Context
import com.smart.library.flutter.FlutterInitializer
import com.smart.library.flutter.FlutterRouter
import io.flutter.app.FlutterApplication

class FinalApplication : FlutterApplication() {

    companion object {

        @SuppressLint("StaticFieldLeak")
        @JvmStatic
        lateinit var INSTANCE: FinalApplication
            private set

    }

    override fun onCreate() {
        super.onCreate()

        INSTANCE = this

        FlutterInitializer.startInitialization(this, MainActivity.sRef?.get()) { context: Context, url: String, requestCode: Int ->
            FlutterRouter.find(url)?.goTo?.invoke(context, hashMapOf(), requestCode,null) ?: false
        }
    }
}
