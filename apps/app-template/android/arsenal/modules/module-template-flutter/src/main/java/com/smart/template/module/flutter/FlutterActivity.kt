package com.smart.template.module.flutter

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import com.smart.library.base.CXBaseActivity
import io.flutter.app.FlutterActivityDelegate
import io.flutter.app.FlutterActivityEvents
import io.flutter.plugin.common.PluginRegistry
import io.flutter.view.FlutterNativeView
import io.flutter.view.FlutterView

class FlutterActivity : CXBaseActivity(), FlutterView.Provider, PluginRegistry, FlutterActivityDelegate.ViewFactory {

    private val delegate by lazy { FlutterActivityDelegate(this, this) }
    private val eventDelegate: FlutterActivityEvents by lazy { delegate }
    private val viewProvider: FlutterView.Provider by lazy { delegate }
    private val pluginRegistry: PluginRegistry by lazy { delegate }

    override fun getFlutterView(): FlutterView = this.viewProvider.flutterView

    override fun createFlutterView(context: Context): FlutterView? = null

    override fun createFlutterNativeView(): FlutterNativeView? = null

    override fun retainFlutterNativeView(): Boolean = false

    override fun hasPlugin(key: String): Boolean = this.pluginRegistry.hasPlugin(key)

    override fun <T> valuePublishedByPlugin(pluginKey: String): T = this.pluginRegistry.valuePublishedByPlugin(pluginKey)

    override fun registrarFor(pluginKey: String): PluginRegistry.Registrar = this.pluginRegistry.registrarFor(pluginKey)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent = Intent("android.intent.action.RUN").putExtra("route", "route2")
        this.eventDelegate.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        this.eventDelegate.onStart()
    }

    override fun onResume() {
        super.onResume()
        this.eventDelegate.onResume()
    }

    override fun onDestroy() {
        this.eventDelegate.onDestroy()
        super.onDestroy()
    }

    override fun onBackPressed() {
        if (!this.eventDelegate.onBackPressed()) {
            super.onBackPressed()
        }
    }

    override fun onStop() {
        this.eventDelegate.onStop()
        super.onStop()
    }

    override fun onPause() {
        super.onPause()
        this.eventDelegate.onPause()
    }

    override fun onPostResume() {
        super.onPostResume()
        this.eventDelegate.onPostResume()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        this.eventDelegate.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (!this.eventDelegate.onActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onNewIntent(intent: Intent) {
        this.eventDelegate.onNewIntent(intent)
    }

    public override fun onUserLeaveHint() {
        this.eventDelegate.onUserLeaveHint()
    }

    override fun onTrimMemory(level: Int) {
        this.eventDelegate.onTrimMemory(level)
    }

    override fun onLowMemory() {
        this.eventDelegate.onLowMemory()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        this.eventDelegate.onConfigurationChanged(newConfig)
    }
}

