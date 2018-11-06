package com.smart.template.module.flutter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import com.smart.library.base.CXBaseActivity
import com.smart.library.base.startActivityForResult
import com.smart.library.util.CXJsonUtil
import com.smart.library.util.CXLogUtil
import com.smart.library.util.CXValueUtil
import io.flutter.app.FlutterActivityDelegate
import io.flutter.app.FlutterActivityEvents
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.PluginRegistry
import io.flutter.view.FlutterNativeView
import io.flutter.view.FlutterView

@SuppressLint("InflateParams", "StaticFieldLeak")
@Suppress("MemberVisibilityCanBePrivate")
class FlutterActivity : CXBaseActivity(), FlutterView.Provider, PluginRegistry, FlutterActivityDelegate.ViewFactory {

    companion object {

        var PUSH_COUNT: Int = 0
            internal set

        var PAGES_COUNT: Int = 0
            internal set

        private const val TAG = "flutter"
        private const val ROUTE_PATH_PREFIX = "flutter://"
        private const val CHANNEL_METHOD = "smart.flutter.io/methods"
        private const val KEY_ROUTE_FULL_PATH = "FLUTTER_ROUTE_FULL_PATH"

        private var mFlutterView: FlutterView? = null
        private var methodChannel: MethodChannel? = null
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
            if (!CXValueUtil.isValid(activity) && CXValueUtil.isDoubleClicked(700)) {
                CXLogUtil.e(TAG, "detected jump to flutter with doubleClicked, cancel jump !")
                return
            }
            startActivityForResult(activity, Intent(activity, FlutterActivity::class.java).putExtra(KEY_ROUTE_FULL_PATH, routeFullPath), requestCode, options, callback)
            activity?.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

    }

    private var bitmap: Bitmap? = null
    private var currentPageNum: Int = -1
    private val handler: Handler by lazy { Handler() }
    private val pluginRegistry: PluginRegistry by lazy { delegate }
    private val viewProvider: FlutterView.Provider by lazy { delegate }
    private val eventDelegate: FlutterActivityEvents by lazy { delegate }
    private val delegate by lazy { FlutterActivityDelegate(this, this) }
    private val routeFullPath: String? by lazy { intent?.getStringExtra(KEY_ROUTE_FULL_PATH) }
    private val snapShootImageView: ImageView by lazy { ImageView(this).apply { visibility = View.GONE } }
    private val loadingView: View by lazy { LayoutInflater.from(this).inflate(R.layout.cx_widget_frameloading_loading, null, false) }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableSwipeBack = false
        enableImmersionStatusBar = false
        PAGES_COUNT++
        super.onCreate(savedInstanceState)
        // intent = Intent("android.intent.action.RUN").putExtra("route", routeFullPath)
        // CXLogUtil.w(TAG, "onCreate routeFullPath->$routeFullPath")
        this.eventDelegate.onCreate(savedInstanceState)
        // GeneratedPluginRegistrant.registerWith(this);
    }

    override fun onStart() {
        super.onStart()
        if (isFlutterViewAttachedOnMe()) this.eventDelegate.onStart()
    }

    var methodChannel: MethodChannel? = null
    override fun onResume() {
        /*var debugLog = ""
        when {
            currentPageNum == -1 -> {
                // 第一次进入该页面
                debugLog = "第一次进入该页面"
                currentPageNum = PAGES_COUNT
                push()
            }
            currentPageNum < PAGES_COUNT -> {
                // 从B页面返回到A页面, 执行完A的onResume 才会执行B的onDestroy
                debugLog = "从B页面返回到A页面, 执行完A的onResume 才会执行B的onDestroy"
                pop()
            }
            currentPageNum == PAGES_COUNT -> {
                // 从后台切回前台
                debugLog = "从后台切回前台"
            }
        }

        CXLogUtil.e(TAG, "onResume ${this}:${Thread.currentThread().name}, PAGES_COUNT=$PAGES_COUNT, currentPageNum=$currentPageNum, PUSH_COUNT=$PUSH_COUNT, debugLog=$debugLog")
        */
        super.onResume()

        FlutterManager.currentActivity = this
        // 是否需要重新添加 flutterView
        if (checkIfAddFlutterView()) {

        }

        methodChannel = MethodChannel(flutterView, FlutterManager.CHANNEL_METHOD)
        methodChannel?.setMethodCallHandler { call, result ->
            CXLogUtil.w(TAG, "onChannelCall: method=${call?.method}, params=${call?.arguments}, thread=${Thread.currentThread().name}, activity.valid=${CXValueUtil.isValid(this)}")
            when (call?.method) {
                "goTo" -> {
                    CXLogUtil.w(TAG, "onChannelCall goTo with value ${call.arguments}")
                    FlutterActivity.goTo(this, "B", hashMapOf("name" to "jack")) { _: Int, _: Int, intent: Intent? ->
                        val resultValue = intent?.getStringExtra(FlutterManager.KEY_FLUTTER_STRING_RESULT) ?: "no result";
                        CXLogUtil.w(TAG, "onChannelCall goTo callback, result:$resultValue")
                        result.success(resultValue)
                    }
                }
                "finish" -> {
                    val resultValue = CXJsonUtil.toJson(call.arguments)
                    CXLogUtil.w(TAG, "onChannelCall finish with value:$resultValue")
                    setResult(Activity.RESULT_OK, Intent().putExtra(FlutterManager.KEY_FLUTTER_STRING_RESULT, resultValue))
                    result.success(1)
                    finish()
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                }
                else -> {
                    result.error("0", "onChannelCall can't find the method:${call?.method}", call?.arguments)
                }
            }
        }

        /*methodChannel?.setMethodCallHandler { call, result ->
            CXLogUtil.w(TAG, "onChannelCall: method=${call?.method}, params=${call?.arguments}, thread=${Thread.currentThread().name}, activity.valid=${CXValueUtil.isValid(this)}")
            when (call?.method) {
                "goTo" -> {
                    handler.post {
                        saveSnap()
                        FlutterActivity.goTo(this, "route2", hashMapOf("name" to "jack")) { _: Int, _: Int, _: Intent? ->
                            result.success(call.arguments)
                        }
                    }
                }
                "finish" -> {
                    setResult(Activity.RESULT_OK, Intent().putExtra("result", CXJsonUtil.toJson(call.arguments)))
                    result.success(1)
                    finish()
                }
                else -> {
                    result.error("0", "can't find the method:${call?.method}", call?.arguments)
                }
            }
        }*/

        if (isFlutterViewAttachedOnMe()) this.eventDelegate.onResume()

        /*handler.postDelayed({
            snapShootImageView.visibility = View.GONE
        }, 300)*/
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
        CXLogUtil.e(TAG, "onPause ${this}:${Thread.currentThread().name}, PAGES_COUNT=$PAGES_COUNT, currentPageNum=$currentPageNum")
        super.onPause()

        CXLogUtil.e(TAG, "onPause0 ${this}:${Thread.currentThread().name} bitmap==null?${bitmap == null}")
        if (isFlutterViewAttachedOnMe()) this.eventDelegate.onPause()
        if (FlutterManager.currentActivity == this) {
            FlutterManager.currentActivity = null
        }
        CXLogUtil.e(TAG, "onPause1 ${this}:${Thread.currentThread().name} bitmap==null?${bitmap == null}")
        snapShootImageView.visibility = View.VISIBLE
        snapShootImageView.setImageBitmap(bitmap)
        CXLogUtil.e(TAG, "onPause2 ${this}:${Thread.currentThread().name} bitmap==null?${bitmap == null}")
    }

    override fun onStop() {
        CXLogUtil.e(TAG, "onStop")
        super.onStop()
        if (isFlutterViewAttachedOnMe()) this.eventDelegate.onStop()
    }

    override fun onBackPressed() {
        // if (!this.eventDelegate.onBackPressed()) super.onBackPressed()
    }

    override fun onDestroy() {
        PAGES_COUNT--
        if (currentPageNum == 1) pop()
        CXLogUtil.e(TAG, "onDestroy ${this}:${Thread.currentThread().name}, PAGES_COUNT=$PAGES_COUNT, currentPageNum=$currentPageNum, PUSH_COUNT=$PUSH_COUNT")
        bitmap?.recycle()
        bitmap = null
        snapShootImageView.setImageBitmap(null)
        super.onDestroy()
    }

    override fun getFlutterView(): FlutterView? = this.viewProvider.flutterView
    override fun createFlutterView(context: Context): FlutterView? {
        CXLogUtil.e(TAG, "createFlutterView")
        if (mFlutterView == null) mFlutterView = FlutterView(this, null, createFlutterNativeView())

        setContentView(FrameLayout(this).apply {
            id = ID_PARENT
            // addView(loadingView, FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT).apply { topMargin = CXSystemUtil.statusBarHeight })
            // addView(snapShootImageView, FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT).apply { topMargin = 0 })
        })

        // loadingView.visibility = View.VISIBLE
        /*mFlutterView?.addFirstFrameListener(object : FlutterView.FirstFrameListener {
            override fun onFirstFrame() {
                CXLogUtil.i(TAG, "addFirstFrameListener -> onFirstFrame")
                mFlutterView?.removeFirstFrameListener(this)
                loadingView.visibility = View.GONE
            }
        })
        mFlutterView?.addActivityLifecycleListener {
            CXLogUtil.i(TAG, "addActivityLifecycleListener -> onPostResume")
        }*/
        return mFlutterView
    }

    override fun createFlutterNativeView(): FlutterNativeView? {
        if (mFlutterNativeView == null) mFlutterNativeView = FlutterNativeView(applicationContext)
        return mFlutterNativeView
    }

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
            detachFlutterView(rootView)
            rootView.addView(mFlutterView, 0, ViewGroup.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT))
            FlutterManager.resetActivity(mFlutterView, this)
            return true
        }
    }

    private fun detachFlutterView(rootLayout: FrameLayout? = null) {
        val rootView: FrameLayout = rootLayout ?: findViewById(ID_PARENT)
        val priorParent: ViewGroup? = mFlutterView?.parent as? ViewGroup?
        if (priorParent != null && priorParent != rootView) priorParent.removeView(mFlutterView)
    }

    private fun push() {
        PUSH_COUNT++
        /* CXLogUtil.w(TAG, "push routeFullPath->$routeFullPath, PUSH_COUNT=$PUSH_COUNT")
         methodChannel?.invokeMethod("push", routeFullPath, object : MethodChannel.Result {
             override fun notImplemented() {
             }

             override fun error(p0: String?, p1: String?, p2: Any?) {
             }

             override fun success(p0: Any?) {
             }
         })*/
    }

    private fun pop() {
        PUSH_COUNT--
        /*CXLogUtil.w(TAG, "pop routeFullPath->$routeFullPath, PUSH_COUNT=$PUSH_COUNT")
        methodChannel?.invokeMethod("pop", routeFullPath, object : MethodChannel.Result {
            override fun notImplemented() {
            }

            override fun error(p0: String?, p1: String?, p2: Any?) {
            }

            override fun success(p0: Any?) {
            }
        })*/
    }

    private fun saveSnap() {
        mFlutterView?.bitmap?.let { bitmap = it }
        bitmap?.let {
            snapShootImageView.setImageBitmap(it)
            snapShootImageView.visibility = View.VISIBLE
            CXLogUtil.w(TAG, "show snap now")
        }
        CXLogUtil.e(TAG, "saveSnap ${this}:${Thread.currentThread().name} bitmap==null?${bitmap == null}")
    }

}
