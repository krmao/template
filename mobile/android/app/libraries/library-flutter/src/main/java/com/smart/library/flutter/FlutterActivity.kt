package com.smart.library.flutter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import com.gyf.barlibrary.ImmersionBar
import com.smart.library.base.STBaseActivity
import com.smart.library.base.startActivityForResult
import com.smart.library.util.*
import io.flutter.app.FlutterActivityDelegate
import io.flutter.app.FlutterActivityEvents
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.PluginRegistry
import io.flutter.view.FlutterNativeView
import io.flutter.view.FlutterView

@SuppressLint("InflateParams", "StaticFieldLeak")
@Suppress("MemberVisibilityCanBePrivate", "UNCHECKED_CAST")
class FlutterActivity : STBaseActivity(), FlutterView.Provider, PluginRegistry, FlutterActivityDelegate.ViewFactory {

    companion object {

        var PAGES_COUNT: Int = 0
            internal set

        private const val TAG = "[flutter:native]"
        private const val ROUTE_PATH_PREFIX = "flutter://"
        private const val KEY_ROUTE_FULL_PATH = "FLUTTER_ROUTE_FULL_PATH"

        private var mFlutterView: FlutterView? = null
        private var mFlutterNativeView: FlutterNativeView? = null

        private val ID_PARENT = System.currentTimeMillis().toInt()

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
            if (!STValueUtil.isValid(activity) && STValueUtil.isDoubleClicked(700)) {
                STLogUtil.e(TAG, "detected jump to flutter with doubleClicked, cancel jump !")
                return
            }
            startActivityForResult(activity, Intent(activity, FlutterActivity::class.java).putExtra(KEY_ROUTE_FULL_PATH, routeFullPath), requestCode, options, callback)
            // activity?.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

    }

    private var bitmap: Bitmap? = null
    private var methodChannel: MethodChannel? = null
    private val handler: Handler by lazy { Handler() }
    private val pluginRegistry: PluginRegistry by lazy { delegate }
    private val viewProvider: FlutterView.Provider by lazy { delegate }
    private val eventDelegate: FlutterActivityEvents by lazy { delegate }
    private val delegate by lazy { FlutterActivityDelegate(this, this) }
    private val snapShootImageView: ImageView by lazy { ImageView(this).apply { visibility = View.GONE } }
    // private val loadingView: View by lazy { LayoutInflater.from(this).inflate(R.layout.st_widget_frameloading_loading, null, false).apply { visibility = View.VISIBLE } }
    // private val routeFullPath: String? by lazy { intent?.getStringExtra(KEY_ROUTE_FULL_PATH) }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableSwipeBack = false
        enableImmersionStatusBar = false
        PAGES_COUNT++
        super.onCreate(savedInstanceState)

        // intent = Intent("android.intent.action.RUN").putExtra("route", routeFullPath)
        // STLogUtil.w(TAG, "onCreate routeFullPath->$routeFullPath")
        this.delegate.onCreate(savedInstanceState)

        enableImmersionStatusBar = true
        if (enableImmersionStatusBar) {
            statusBar = ImmersionBar.with(this)
                    .transparentStatusBar()
                    .statusBarColorInt(Color.TRANSPARENT)
                    .navigationBarEnable(false)
                    .statusBarDarkFont(enableImmersionStatusBarWithDarkFont, if (enableImmersionStatusBarWithDarkFont) 0.2f else 0f)
            statusBar?.init()
        }

        // GeneratedPluginRegistrant.registerWith(this);
    }


    override fun createFlutterNativeView(): FlutterNativeView? {
        if (mFlutterNativeView == null) mFlutterNativeView = FlutterNativeView(applicationContext)
        return mFlutterNativeView
    }

    override fun getFlutterView(): FlutterView? = this.viewProvider.flutterView
    override fun createFlutterView(context: Context): FlutterView? {
        STLogUtil.e(TAG, "createFlutterView")
        if (mFlutterView == null) mFlutterView = FlutterView(this, null, createFlutterNativeView())
        setContentView(FrameLayout(this).apply {
            id = ID_PARENT

            addView(snapShootImageView, FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT).apply { topMargin = 0 })
            // addView(loadingView, FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT).apply { topMargin = 0 /*STSystemUtil.statusBarHeight*/ })
        })

        checkIfAddFlutterView()
        mFlutterView?.addFirstFrameListener(object : FlutterView.FirstFrameListener {
            override fun onFirstFrame() {
                STLogUtil.i(TAG, "addFirstFrameListener -> onFirstFrame")
                mFlutterView?.removeFirstFrameListener(this)
                // mFlutterView?.postDelayed({ loadingView.visibility = View.GONE }, resources.getInteger(android.R.integer.config_mediumAnimTime).toLong())
            }
        })
        mFlutterView?.addActivityLifecycleListener {
            STLogUtil.i(TAG, "addActivityLifecycleListener -> onPostResume")
        }
        return mFlutterView
    }

    override fun onStart() {
        super.onStart()
        if (isFlutterViewAttachedOnMe()) this.eventDelegate.onStart()
    }

    override fun onResume() {
        STLogUtil.e(TAG, "onResume ${this}:${Thread.currentThread().name}, PAGES_COUNT=$PAGES_COUNT")
        super.onResume()

        FlutterManager.currentActivity = this
        checkIfAddFlutterView()

        if (isFlutterViewAttachedOnMe()) this.eventDelegate.onResume()

        handler.postDelayed({ snapShootImageView.visibility = View.GONE }, 300)
    }

    override fun onPostResume() {
        super.onPostResume()
        if (isFlutterViewAttachedOnMe()) this.eventDelegate.onPostResume()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (isFlutterViewAttachedOnMe()) this.eventDelegate.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (!(isFlutterViewAttachedOnMe() && this.eventDelegate.onActivityResult(requestCode, resultCode, data))) super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onNewIntent(intent: Intent) {
        if (isFlutterViewAttachedOnMe()) this.eventDelegate.onNewIntent(intent)
    }

    override fun onUserLeaveHint() {
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

    override fun onPause() {
        super.onPause()
        if (isFlutterViewAttachedOnMe()) this.eventDelegate.onPause()
        if (FlutterManager.currentActivity == this) {
            FlutterManager.currentActivity = null
        }
        snapShootImageView.visibility = View.VISIBLE
        snapShootImageView.setImageBitmap(bitmap)
    }

    override fun onStop() {
        STLogUtil.e(TAG, "onStop")
        super.onStop()
        if (isFlutterViewAttachedOnMe()) this.eventDelegate.onStop()
    }

    override fun onBackPressed() {
        // if (!this.eventDelegate.onBackPressed()) super.onBackPressed()
    }

    override fun onDestroy() {
        PAGES_COUNT--
        STLogUtil.e(TAG, "onDestroy ${this}:${Thread.currentThread().name}, PAGES_COUNT=$PAGES_COUNT")
        bitmap?.recycle()
        bitmap = null
        snapShootImageView.setImageBitmap(null)
        super.onDestroy()
    }

    override fun onBackPress(): Boolean {
        STLogUtil.e(TAG, "onBackPress flutter/navigation ${this}:${Thread.currentThread().name}, PAGES_COUNT=$PAGES_COUNT")
        // if (!this.eventDelegate.onBackPressed()) super.onBackPressed()
        // MethodChannel(mFlutterView, "flutter/navigation", JSONMethodCodec.INSTANCE).invokeMethod("popRoute", null)
        methodChannel?.invokeMethod("backPressed", null)
        return true
    }
    /*override fun onBackPress(): Boolean {
        STLogUtil.e(TAG, "onBackPress ${this}:${Thread.currentThread().name}, PAGES_COUNT=$PAGES_COUNT")
        methodChannel?.invokeMethod("pop", null)
        return PAGES_COUNT > 1
    }*/

    override fun retainFlutterNativeView(): Boolean = true
    override fun hasPlugin(key: String): Boolean = this.pluginRegistry.hasPlugin(key)
    override fun <T> valuePublishedByPlugin(pluginKey: String): T = this.pluginRegistry.valuePublishedByPlugin(pluginKey)
    override fun registrarFor(pluginKey: String): PluginRegistry.Registrar = this.pluginRegistry.registrarFor(pluginKey)
    private fun isFlutterViewAttachedOnMe(): Boolean = findViewById<FrameLayout>(ID_PARENT) == (flutterView?.parent as? ViewGroup?)

    private fun checkIfAddFlutterView(rootLayout: FrameLayout? = null): Boolean {
        val rootView: FrameLayout = rootLayout ?: findViewById(ID_PARENT)
        val priorParent: ViewGroup? = mFlutterView?.parent as? ViewGroup?
        if (priorParent != null && priorParent == rootView) {
            return false
        } else {
            attachFlutterView(rootView)
            return true
        }
    }

    private fun detachFlutterView(rootLayout: FrameLayout? = null) {
        STLogUtil.w(TAG, "detachFlutterView now...")
        mFlutterView?.let {
            val rootView: FrameLayout = rootLayout ?: findViewById(ID_PARENT)
            val priorParent: ViewGroup? = it.parent as? ViewGroup?
            if (priorParent != null && priorParent != rootView) priorParent.removeView(it)

            // flutterNativeView.mContext && flutterNativeView.pluginRegistry.mAppContext
            // is applicationContext, no worried about activity leak
            it.flutterNativeView?.pluginRegistry?.let { flutterPluginRegistry ->
                STReflectUtil.set(flutterPluginRegistry, "mActivity", null)
                STReflectUtil.get(flutterPluginRegistry, "mPlatformViewsController")?.let { mPlatformViewsController ->
                    STReflectUtil.set(mPlatformViewsController, "mContext", null)
                }
            }
        }
    }

    private fun attachFlutterView(rootLayout: FrameLayout? = null) {
        STLogUtil.w(TAG, "attachFlutterView mFlutterView==null?${mFlutterView == null}")
        mFlutterView?.let {
            val rootView: FrameLayout = rootLayout ?: findViewById(ID_PARENT)
            detachFlutterView(rootView)
            STLogUtil.w(TAG, "begin attachFlutterView")
            rootView.addView(it, 0, ViewGroup.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT))
            try {
                it.flutterNativeView?.attachViewAndActivity(it, this)
            } catch (e: AssertionError) {
                STLogUtil.e(TAG, "attachFlutterView failure", e)
            }

            // after attach
            //----------------------------------------------------------------------
            methodChannel = MethodChannel(it, FlutterManager.CHANNEL_METHOD)
            methodChannel?.setMethodCallHandler { call, result ->
                STLogUtil.w(TAG, "onMethodCallHandler method=${call?.method}, params=${call?.arguments}, thread=${Thread.currentThread().name}, activity.valid=${STValueUtil.isValid(this)}")
                when (call?.method) {
                    "beforeGoTo" -> {
                        saveSnap()
                        STLogUtil.w(TAG, "onMethodCallHandler beforeGoTo saveSnap")
                        result.success(0)
                    }
                    "goTo" -> {
                        STLogUtil.w(TAG, "onMethodCallHandler goTo with value ${call.arguments} ${call.arguments.javaClass.name}")

                        FlutterActivity.goTo(this, "B", hashMapOf("name" to "jack")) { _: Int, _: Int, intent: Intent? ->
                            val resultValue = intent?.getStringExtra(FlutterManager.KEY_FLUTTER_STRING_RESULT)
                                    ?: "no result"
                            STLogUtil.w(TAG, "onMethodCallHandler goTo callback, result:$resultValue")
                            result.success(resultValue)
                        }
                    }
                    "goToNative" -> {
                        STLogUtil.e(TAG, "onMethodCallHandler goToNative with value ${call.arguments} ${call.arguments.javaClass.name}")
                        val map: java.util.HashMap<String, Any?> = call.arguments as? java.util.HashMap<String, Any?>?
                                ?: hashMapOf()
                        val pageName = map["pageName"]
                        val arguments = map["arguments"]
                        if (pageName == "K") {

                            FlutterTestFragment.goTo(this@FlutterActivity)
                        } else {
                            STToastUtil.show("not found page about pageName:$pageName, arguments:$arguments")
                        }
                    }
                    "willFinish" -> {
                        saveSnap()
                        detachFlutterView() // detach context before finish
                        result.success(1)
                    }
                    "finish" -> {
                        val resultValue = STJsonUtil.toJson(call.arguments)
                        STLogUtil.w(TAG, "onMethodCallHandler finish with value:$resultValue")
                        setResult(Activity.RESULT_OK, Intent().putExtra(FlutterManager.KEY_FLUTTER_STRING_RESULT, resultValue))
                        finish()
                        // overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

                        result.success(1)
                    }
                    else -> {
                        result.error("0", "onMethodCallHandler can't find the method:${call?.method}", call?.arguments)
                    }
                }
            }
            //----------------------------------------------------------------------
        }
    }

    private fun saveSnap() {
        mFlutterView?.bitmap?.let { bitmap = it }
        bitmap?.let {
            snapShootImageView.setImageBitmap(it)
            snapShootImageView.visibility = View.VISIBLE
            STLogUtil.w(TAG, "show snap now")
        }
        STLogUtil.e(TAG, "saveSnap ${this}:${Thread.currentThread().name} bitmap==null?${bitmap == null}")
    }

}
