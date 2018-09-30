package com.smart.template.module.flutter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import com.smart.library.base.CXBaseActivity
import com.smart.library.base.startActivityForResult
import com.smart.library.util.CXJsonUtil
import com.smart.library.util.CXLogUtil
import com.smart.library.util.CXSystemUtil
import com.smart.library.util.CXValueUtil
import io.flutter.app.FlutterActivityDelegate
import io.flutter.app.FlutterActivityEvents
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.PluginRegistry
import io.flutter.view.FlutterNativeView
import io.flutter.view.FlutterView
import org.jetbrains.anko.async


@SuppressLint("InflateParams")
@Suppress("MemberVisibilityCanBePrivate")
class FlutterActivity : CXBaseActivity(), FlutterView.Provider, PluginRegistry, FlutterActivityDelegate.ViewFactory {

    companion object {

        private const val KEY_ROUTE_FULL_PATH = "FLUTTER_ROUTE_FULL_PATH"
        private const val ROUTE_PATH_PREFIX = "flutter://"
        private const val TAG = "flutter"
        const val CHANNEL_METHOD = "flutter.channel.method";

        @SuppressLint("StaticFieldLeak")
        private var mFlutterNativeView: FlutterNativeView? = null
        @SuppressLint("StaticFieldLeak")
        private var mFlutterView: FlutterView? = null
        val ID_PARENT = System.currentTimeMillis().toInt()
        private var methodChannel: MethodChannel? = null

        @JvmStatic
        @JvmOverloads
        fun goTo(activity: Activity?, routeName: String, routeParams: HashMap<String, Any>? = null, requestCode: Int = 0, callback: ((requestCode: Int, resultCode: Int, data: Intent?) -> Unit?)? = null) {
            val paramsBuffer = StringBuffer()
            routeParams?.entries?.let {
                it.forEachIndexed { index, mutableEntry ->
                    paramsBuffer.append("${mutableEntry.key}=${mutableEntry.value}")
                    if (index < it.size - 1) paramsBuffer.append("&")
                }
            }
            val routeFullPath = "$ROUTE_PATH_PREFIX$routeName?${if (routeParams?.isEmpty() == true) "" else paramsBuffer.toString()}"
            goToByFullPath(activity, routeFullPath, requestCode, callback, null)
        }

        /**
         * @param routeFullPath flutter://routeName?key=value&key1=value1
         */
        @JvmStatic
        @JvmOverloads
        fun goToByFullPath(activity: Activity?, routeFullPath: String, requestCode: Int = 0, callback: ((requestCode: Int, resultCode: Int, data: Intent?) -> Unit?)? = null, options: Bundle? = null) {
            if (!CXValueUtil.isValid(activity) && CXValueUtil.isDoubleClicked(700)) {
                CXLogUtil.e(TAG, "detected jump to flutter with doubleClicked, cancel jump !")
                return
            }
            startActivityForResult(activity, Intent(activity, FlutterActivity::class.java).putExtra(KEY_ROUTE_FULL_PATH, routeFullPath), requestCode, options, callback)
        }
    }

    private val routeFullPath: String? by lazy { intent?.getStringExtra(KEY_ROUTE_FULL_PATH) }

    private val delegate by lazy { FlutterActivityDelegate(this, this) }
    private val eventDelegate: FlutterActivityEvents by lazy { delegate }
    private val viewProvider: FlutterView.Provider by lazy { delegate }
    private val pluginRegistry: PluginRegistry by lazy { delegate }

    override fun getFlutterView(): FlutterView? = this.viewProvider.flutterView

    private val loadingView: View by lazy {
        LayoutInflater.from(this).inflate(R.layout.cx_widget_frameloading_loading, null, false)
    }

    private val snapShootImageView: ImageView by lazy {
        ImageView(this).apply { visibility = View.GONE }
    }

    override fun createFlutterView(context: Context): FlutterView? {
        CXLogUtil.e(TAG, "createFlutterView")
        if (mFlutterView == null) {
            mFlutterView = FlutterView(this, null, createFlutterNativeView())
            methodChannel = MethodChannel(mFlutterView, CHANNEL_METHOD)
            methodChannel?.setMethodCallHandler { call, result ->
                val activity = FlutterManager.currentActivity
                CXLogUtil.w(TAG, "onChannelCall: method=${call?.method}, params=${call?.arguments}, thread=${Thread.currentThread().name}, activity.valid=${CXValueUtil.isValid(activity)}")
                when (call?.method) {
                    "goTo" -> {
                        FlutterActivity.goTo(activity, "route2", hashMapOf("name" to "jack")) { requestCode: Int, resultCode: Int, data: Intent? ->
                            result.success(call.arguments)
                        }
                    }
                    "finish" -> {
                        activity?.setResult(Activity.RESULT_OK, Intent().putExtra("result", CXJsonUtil.toJson(call.arguments)))
                        result.success(1)
                        activity?.finish()
                    }
                    else -> {
                        result.error("0", "can't find the method:${call?.method}", call?.arguments)
                    }
                }
            }
        }
        setContentView(FrameLayout(this).apply {
            id = ID_PARENT
            checkIfAddFlutterView(this)
            addView(loadingView, FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT).apply {
                topMargin = CXSystemUtil.statusBarHeight
            })
            addView(snapShootImageView, FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT).apply {
                topMargin = 0//CXSystemUtil.statusBarHeight
            })
        })

        loadingView.visibility = View.VISIBLE
        mFlutterView?.addFirstFrameListener(object : FlutterView.FirstFrameListener {
            override fun onFirstFrame() {
                CXLogUtil.e(TAG, "addFirstFrameListener -> onFirstFrame")
                mFlutterView?.removeFirstFrameListener(this)
                async { Thread.sleep(3000);runOnUiThread { loadingView.visibility = View.GONE } }
            }
        })

        mFlutterView?.addActivityLifecycleListener {
            CXLogUtil.e(TAG, "addActivityLifecycleListener -> onPostResume")
        }
        CXLogUtil.e(TAG, "createFlutterView mFlutterView==null?${mFlutterView == null}, showSplashScreenUntilFirstFrame=${showSplashScreenUntilFirstFrame()}")
        return mFlutterView
    }

    private fun showSplashScreenUntilFirstFrame(): Boolean {
        try {
            val activityInfo = packageManager.getActivityInfo(componentName, PackageManager.GET_META_DATA)
            val metadata = activityInfo.metaData
            return metadata != null && metadata.getBoolean("io.flutter.app.android.SplashScreenUntilFirstFrame")
        } catch (var3: PackageManager.NameNotFoundException) {
            return false
        }

    }


    override fun createFlutterNativeView(): FlutterNativeView? {
        if (mFlutterNativeView == null) {
            mFlutterNativeView = FlutterNativeView(applicationContext)
        }
        return mFlutterNativeView
    }

    fun isFlutterViewAttachedOnMe(): Boolean {
        return findViewById<FrameLayout>(ID_PARENT) == (flutterView?.parent as? ViewGroup?)
    }

    fun checkIfAddFlutterView(rootLayout: FrameLayout? = null): Boolean {
        val rootView: FrameLayout = rootLayout ?: findViewById(ID_PARENT)
        val priorParent: ViewGroup? = mFlutterView?.parent as? ViewGroup?
        if (priorParent != null && priorParent == rootView) {
            return false
        } else {
            detachFlutterView(rootView)
            rootView.addView(mFlutterView, 0, ViewGroup.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT))
            FlutterManager.resetActivity(mFlutterView, this)
            return true
        }
    }

    fun push() {
        CXLogUtil.w(TAG, "push routeFullPath->$routeFullPath")
        methodChannel?.invokeMethod("push", routeFullPath, object : MethodChannel.Result {
            override fun notImplemented() {
            }

            override fun error(p0: String?, p1: String?, p2: Any?) {
            }

            override fun success(p0: Any?) {
            }
        })
        isPushed = true
    }

    fun pop() {
        methodChannel?.invokeMethod("pop", routeFullPath, object : MethodChannel.Result {
            override fun notImplemented() {
            }

            override fun error(p0: String?, p1: String?, p2: Any?) {
            }

            override fun success(p0: Any?) {
            }
        })
        isPushed = false
    }

    override fun retainFlutterNativeView(): Boolean = true
    override fun hasPlugin(key: String): Boolean = this.pluginRegistry.hasPlugin(key)
    override fun <T> valuePublishedByPlugin(pluginKey: String): T = this.pluginRegistry.valuePublishedByPlugin(pluginKey)
    override fun registrarFor(pluginKey: String): PluginRegistry.Registrar = this.pluginRegistry.registrarFor(pluginKey)


    override fun onCreate(savedInstanceState: Bundle?) {
        enableSwipeBack = true
        super.onCreate(savedInstanceState)
        intent = Intent("android.intent.action.RUN").putExtra("route", routeFullPath)
        CXLogUtil.w(TAG, "onCreate routeFullPath->$routeFullPath")
        this.eventDelegate.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        if (isFlutterViewAttachedOnMe()) this.eventDelegate.onStart()
    }

    private var isPushed = false
    override fun onResume() {
        CXLogUtil.e(TAG, "onResume")
        super.onResume()

        FlutterManager.currentActivity = this
        if (checkIfAddFlutterView()) {

        }
        push()
        if (isFlutterViewAttachedOnMe()) this.eventDelegate.onResume()

        async {
            Thread.sleep(300)
            runOnUiThread {
                snapShootImageView.visibility = View.GONE
            }
        }
    }

    override fun onPause() {
        CXLogUtil.e(TAG, "onPause")
        super.onPause()

        mFlutterView?.bitmap?.let {
            snapShootImageView.visibility = View.VISIBLE
            snapShootImageView.setImageBitmap(it)
        }

        if (isFlutterViewAttachedOnMe()) this.eventDelegate.onPause()
        if (FlutterManager.currentActivity == this) {
            FlutterManager.currentActivity = null
        }
    }

    override fun onRestart() {
        CXLogUtil.e(TAG, "onRestart")
        super.onRestart()
    }

    override fun onStop() {
        CXLogUtil.e(TAG, "onStop")
        super.onStop()
        if (isFlutterViewAttachedOnMe()) this.eventDelegate.onStop()
    }

    override fun onDestroy() {
        CXLogUtil.e(TAG, "onDestroy")
        super.onDestroy()
        // if (isFlutterViewAttachedOnMe()) this.eventDelegate.onDestroy()
//        detachFlutterView()
    }

    fun detachFlutterView(rootLayout: FrameLayout? = null) {
        val rootView: FrameLayout = rootLayout ?: findViewById(ID_PARENT)
        val priorParent: ViewGroup? = mFlutterView?.parent as? ViewGroup?
        if (priorParent != null && priorParent != rootView) {
            priorParent.removeView(mFlutterView)
        }
    }

    override fun onBackPressed() {
        if (!this.eventDelegate.onBackPressed()) {
            super.onBackPressed()
        }
    }

    override fun onPostResume() {
        super.onPostResume()
        if (isFlutterViewAttachedOnMe()) this.eventDelegate.onPostResume()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (isFlutterViewAttachedOnMe()) this.eventDelegate.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (!(isFlutterViewAttachedOnMe() && this.eventDelegate.onActivityResult(requestCode, resultCode, data))) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onNewIntent(intent: Intent) {
        if (isFlutterViewAttachedOnMe()) this.eventDelegate.onNewIntent(intent)
    }

    public override fun onUserLeaveHint() {
        if (isFlutterViewAttachedOnMe()) this.eventDelegate.onUserLeaveHint()
    }

    override fun onTrimMemory(level: Int) {
        if (isFlutterViewAttachedOnMe()) this.eventDelegate.onTrimMemory(level)
    }

    override fun onLowMemory() {
        if (isFlutterViewAttachedOnMe()) this.eventDelegate.onLowMemory()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (isFlutterViewAttachedOnMe()) this.eventDelegate.onConfigurationChanged(newConfig)
    }
}

