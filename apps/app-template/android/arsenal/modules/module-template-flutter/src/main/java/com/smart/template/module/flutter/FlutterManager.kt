package com.smart.template.module.flutter

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.support.annotation.NonNull
import io.flutter.view.FlutterMain
import io.flutter.view.FlutterView

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

    @JvmStatic
    fun resetActivity(flutterView: FlutterView?, activity: Activity?) {
        /*
        CXReflectUtil.set(flutterView?.flutterNativeView, "mFlutterView", flutterView)
        CXReflectUtil.set(flutterView?.flutterNativeView?.pluginRegistry, "mFlutterView", flutterView)
        CXReflectUtil.set(flutterView?.flutterNativeView?.pluginRegistry, "mActivity", activity)
        CXReflectUtil.set(CXReflectUtil.get(flutterView?.flutterNativeView?.pluginRegistry, "mPlatformViewsController"), "mContext", activity)
        CXReflectUtil.set(flutterView, "mActivityLifecycleListeners", mutableListOf(PlatformPlugin(activity).apply { MethodChannel(flutterView, "flutter/platform", JSONMethodCodec.INSTANCE).setMethodCallHandler(this) }))
        */
        try {
            flutterView?.flutterNativeView?.attachViewAndActivity(flutterView, activity)
        } catch (ae: AssertionError) {
            println("In new implementation for FlutterPluginRegistry,AssertionError is thrown when try to attach twice, it doesn't matter even we ignore it.")
        }

        // val mActivityLifecycleListeners: MutableList<ActivityLifecycleListener>? = CXReflectUtil.get(flutterView, "mActivityLifecycleListeners") as MutableList<ActivityLifecycleListener>
        // mActivityLifecycleListeners?.clear()
        // flutterView?.addActivityLifecycleListener(PlatformPlugin(activity).apply { MethodChannel(flutterView, "flutter/platform", JSONMethodCodec.INSTANCE).setMethodCallHandler(this) })
    }

}