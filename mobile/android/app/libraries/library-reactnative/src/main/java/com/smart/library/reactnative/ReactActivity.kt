package com.smart.library.reactnative

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.KeyEvent
import android.widget.FrameLayout
import com.facebook.react.ReactInstanceManager
import com.facebook.react.ReactRootView
import com.facebook.react.bridge.JSBundleLoader
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler
import com.smart.library.base.STBaseActivity
import com.smart.library.base.startActivityForResult
import com.smart.library.deploy.STDeployManager
import com.smart.library.deploy.model.STIDeployCheckUpdateCallback
import com.smart.library.util.STLogUtil
import com.smart.library.util.STToastUtil
import com.swmansion.gesturehandler.react.RNGestureHandlerEnabledRootView

@Suppress("unused", "PrivatePropertyName")
class ReactActivity : STBaseActivity(), DefaultHardwareBackBtnHandler {

    companion object {
        const val KEY_RESULT: String = "rn_result"
        const val KEY_START_COMPONENT: String = "rn_start_component"
        const val KEY_REQUEST_CODE: String = "rn_request_code"

        @JvmStatic
        @JvmOverloads
        internal fun startForResult(context: Context?, component: String? = null, extras: Bundle, intentFlag: Int? = null, requestCode: Int, callback: ((requestCode: Int, resultCode: Int, data: Intent?) -> Unit?)? = null) {

            val intent = Intent(context, ReactActivity::class.java)
            intentFlag?.let { intent.addFlags(it) }
            intent.putExtra(KEY_REQUEST_CODE, requestCode)
            intent.putExtra(KEY_START_COMPONENT, component)
            intent.putExtras(extras)

            if (context is Activity) {
                startActivityForResult(context, intent, 0, null, callback)
            } else {
                context?.startActivity(intent)
                STLogUtil.e(ReactManager.TAG, "context is not Activity, can't startActivityForResult!")
                callback?.invoke(requestCode, Activity.RESULT_CANCELED, null)
            }
        }
    }

    private val OVERLAY_PERMISSION_REQ_CODE = 103

    private val TAG: String = ReactManager.TAG

    private var reactRootView: ReactRootView? = null

    private var reactInstanceManager: ReactInstanceManager? = null

    private val requestCode: Int by lazy { intent?.getIntExtra(KEY_REQUEST_CODE, 0) ?: 0 }

    private val moduleName: String?
        get() = intent.getStringExtra(KEY_START_COMPONENT)

    private val initialProperties: Bundle? by lazy { intent.extras }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableSwipeBack = false
        super.onCreate(savedInstanceState)
        reactRootView = RNGestureHandlerEnabledRootView(this) //ReactRootView(this)
        setContentView(
                FrameLayout(this).apply {
                    this.fitsSystemWindows = true
                    this.addView(reactRootView)
                }
        )

        STDeployManager.REACT_NATIVE.onCreate(this, object : STIDeployCheckUpdateCallback {
            override fun onCheckUpdateCallback(isHaveNewVersion: Boolean) {
                STLogUtil.w(TAG, "onCheckUpdateCallback($isHaveNewVersion)")
            }

            override fun onDownloadCallback(downloadSuccess: Boolean) {
                STLogUtil.w(TAG, "onDownloadCallback($downloadSuccess)")
            }

            override fun onMergePatchCallback(mergeSuccess: Boolean) {
                STLogUtil.w(TAG, "onMergePatchCallback($mergeSuccess)")
            }

            override fun onApplyCallback(applySuccess: Boolean) {
                STLogUtil.w(TAG, "onApplyCallback($applySuccess)")
                isOnCreateAppliedSuccess = true

                if (ReactManager.instanceManager != null) {
                    /**
                     * debug 环境下的红色调试界面需要权限 ACTION_MANAGE_OVERLAY_PERMISSION
                     * If your app is targeting the Android API level 23 or greater, make sure you have the overlay permission enabled for the development build. You can check it with Settings.canDrawOverlays(this);. This is required in dev builds because react native development errors must be displayed above all the other windows. Due to the new permissions system introduced in the API level 23, the user needs to approve it. This can be achieved by adding the following code to the Activity file in the onCreate() method. OVERLAY_PERMISSION_REQ_CODE is a field of the class which would be responsible for passing the result back to the Activity.
                     */
                    /*if (ReactManager.debug && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this@ReactActivity)) {
                        startActivityForResult(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName")), OVERLAY_PERMISSION_REQ_CODE)
                    } else {*/
                    startReactApplication()
                    /*}*/
                } else {
                    ReactManager.initInstanceManager { success: Boolean ->
                        STLogUtil.v(TAG, "initInstanceManager $success")
                        if (success) {
                            STLogUtil.v(TAG, "initInstanceManager will startReactApplication start")
                            startReactApplication()
                            STLogUtil.v(TAG, "initInstanceManager will startReactApplication end")
                        }
                    }
                    return
                }
            }
        })
    }

    private var isOnCreateAppliedSuccess = false

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this)) {
                    // SYSTEM_ALERT_WINDOW permission not granted...
                    STToastUtil.show("调试模式请务必打开 SYSTEM_ALERT_WINDOW 权限")
                    finish()
                    return
                } else {
                    startReactApplication()
                }
            }
        } else {
            reactInstanceManager?.onActivityResult(this, requestCode, resultCode, data)
        }
    }

    private fun startReactApplication() {
        STLogUtil.w(TAG, "startReactApplication start")
        // loadBusinessCode()
        reactRootView?.post {
            reactInstanceManager = ReactManager.instanceManager
            reactRootView?.startReactApplication(reactInstanceManager, moduleName, initialProperties)
            if (isResumed) reactInstanceManager?.onHostResume(this, this)
            STLogUtil.w(TAG, "startReactApplication end")
        }
    }

    private fun loadBusinessCode() {
        STLogUtil.w(TAG, "loadBusinessCode")
        val businessBundlePath = ReactConstant.PATH_ASSETS_BUSINESS_BUNDLE
        ReactManager.loadBundle(ReactBundle(businessBundlePath, JSBundleLoader.createAssetLoader(this@ReactActivity, businessBundlePath, false)))
    }

    /**
     * 重新设置 react 属性, 并重新渲染 react 界面
     */
    fun updateReactProperties(appProperties: Bundle?) {
        reactRootView?.appProperties = appProperties
    }

    override fun onNewIntent(intent: Intent?) {
        reactInstanceManager?.onNewIntent(intent) ?: super.onNewIntent(intent)
    }

    private var isResumed: Boolean = false
    override fun onResume() {
        super.onResume()
        reactInstanceManager?.onHostResume(this, this)
        isResumed = true
    }

    override fun onPause() {
        super.onPause()
        reactInstanceManager?.onHostPause(this)
    }

    override fun onBackPress(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        STLogUtil.d(TAG, "onBackPress")
        reactInstanceManager?.onBackPressed() ?: super.onBackPressed()
    }

    /**
     * 1: 这里的实现一定是调用系统默认的 super.onBackPressed()
     * 2: BackAndroid.exitApp() 执行的是 this.invokeDefaultOnBackPressed()
     * 3: 当点击返回键时,强制拦截调用 this.onBackPressed(), 然后会判断 react native 是否会拦截消费该事件(BackAndroid.addEventListener('hardwareBackPress', function() { return false; });),
     *    如果没有拦截消费, 接着会判断 react native 是否含有页面堆栈, 如果有则会自动 goBack() 到上一页,
     *    如果没有页面堆栈则会 调用 this.invokeDefaultOnBackPressed() 即调用 super.onBackPressed() 退出整个 activity
     */
    override fun invokeDefaultOnBackPressed() {
        STLogUtil.d(TAG, "invokeDefaultOnBackPressed")
        super.onBackPressed()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (ReactManager.debug && reactInstanceManager != null && keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            event.startTracking() // 追踪该按键以判断是否触发 onKeyLongPress
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        if (ReactManager.debug && reactInstanceManager != null && keyCode == KeyEvent.KEYCODE_MENU) {
            reactInstanceManager?.showDevOptionsDialog()
            return true
        }
        return super.onKeyUp(keyCode, event)
    }

    override fun onKeyLongPress(keyCode: Int, event: KeyEvent?): Boolean {
        if (ReactManager.debug && reactInstanceManager != null && keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            reactInstanceManager?.showDevOptionsDialog()
            return true
        }
        return super.onKeyLongPress(keyCode, event)
    }

    override fun onDestroy() {
        reactRootView?.unmountReactApplication()
        reactRootView = null
        reactInstanceManager?.onHostDestroy(this)
        if (isOnCreateAppliedSuccess) STDeployManager.REACT_NATIVE.onDestroy(this)
        super.onDestroy()
    }

}
