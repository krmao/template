package com.smart.template.module.flutter

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.support.annotation.NonNull
import com.smart.library.util.CXReflectUtil
import io.flutter.plugin.common.ActivityLifecycleListener
import io.flutter.plugin.common.JSONMethodCodec
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.platform.PlatformPlugin
import io.flutter.view.FlutterMain
import io.flutter.view.FlutterView

@Suppress("UNCHECKED_CAST")
@SuppressLint("StaticFieldLeak")
object FlutterManager {
    
    private var application: Application? = null
    var currentActivity: Activity? = null

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

        val mActivityLifecycleListeners: MutableList<ActivityLifecycleListener>? = CXReflectUtil.get(flutterView, "mActivityLifecycleListeners") as MutableList<ActivityLifecycleListener>
        mActivityLifecycleListeners?.clear()
        flutterView?.addActivityLifecycleListener(PlatformPlugin(activity).apply { MethodChannel(flutterView, "flutter/platform", JSONMethodCodec.INSTANCE).setMethodCallHandler(this) })
    }

    @JvmStatic
    fun startInitialization(@NonNull application: Application) {
        FlutterManager.application = application
        FlutterMain.startInitialization(application)
    }

}