package com.smart.template.module.flutter

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
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
        initFlutterConfig(application)
        FlutterMain.startInitialization(application)
    }

    private fun initFlutterConfig(applicationContext: Context) {
        try {
            val metadata = applicationContext.packageManager.getApplicationInfo(applicationContext.packageName, PackageManager.GET_META_DATA).metaData
            if (metadata != null) {
                metadata.putString(FlutterMain.PUBLIC_AOT_VM_SNAPSHOT_DATA_KEY, "vm_snapshot_data")
                metadata.putString(FlutterMain.PUBLIC_AOT_VM_SNAPSHOT_INSTR_KEY, "vm_snapshot_instr")
                metadata.putString(FlutterMain.PUBLIC_AOT_ISOLATE_SNAPSHOT_DATA_KEY, "isolate_snapshot_data")
                metadata.putString(FlutterMain.PUBLIC_AOT_ISOLATE_SNAPSHOT_INSTR_KEY, "isolate_snapshot_instr")
                metadata.putString(FlutterMain.PUBLIC_FLUTTER_ASSETS_DIR_KEY, "flutter_assets")
                // metadata.putString(FlutterMain.PUBLIC_AOT_AOT_SHARED_LIBRARY_PATH, "app.so")
                // metadata.putString(FlutterMain.PUBLIC_FLX_KEY, "app.flx")
            }
        } catch (var2: PackageManager.NameNotFoundException) {
            throw RuntimeException(var2)
        }
    }

}