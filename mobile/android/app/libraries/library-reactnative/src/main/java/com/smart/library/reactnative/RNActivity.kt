package com.smart.library.reactnative

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
import com.smart.library.deploy.STDeployManager
import com.smart.library.deploy.model.STIDeployCheckUpdateCallback
import com.smart.library.util.STEventManager
import com.smart.library.util.STLogUtil
import com.smart.library.util.STPreferencesUtil
import com.smart.library.util.STToastUtil
import com.swmansion.gesturehandler.react.RNGestureHandlerEnabledRootView
import org.json.JSONObject

@Suppress("unused", "PrivatePropertyName", "ObjectLiteralToLambda")
class RNActivity : STBaseActivity(), DefaultHardwareBackBtnHandler {

    companion object {
        const val KEY_RESULT: String = "rn_result"
        const val KEY_START_COMPONENT: String = "rn_start_component"
        const val KEY_REQUEST_CODE: String = "rn_request_code"

        @JvmStatic
        @JvmOverloads
        internal fun startForResult(context: Context?, component: String? = null, extras: Bundle, intentFlag: Int? = null) {
            val intent = Intent(context, RNActivity::class.java)
            intentFlag?.let { intent.addFlags(it) }
            intent.putExtra(KEY_START_COMPONENT, component)
            intent.putExtras(extras)
            context?.startActivity(intent)
        }
    }

    private val OVERLAY_PERMISSION_REQ_CODE = 103

    private val TAG: String = RNInstanceManager.TAG

    private val reactRootView: ReactRootView by lazy { RNGestureHandlerEnabledRootView(this) }//ReactRootView(this)

    private var reactInstanceManager: ReactInstanceManager? = null

    private val moduleName: String? by lazy {  intent.getStringExtra(KEY_START_COMPONENT) }

    private val initialProperties: Bundle? by lazy { intent.extras }

    private val pageName: String? by lazy { initialProperties?.getString("page") }
    private val params: JSONObject by lazy {
        try {
            val paramJsonString: String = initialProperties?.getString("param") ?: "{}"
            if (paramJsonString.isNotBlank()) {
                JSONObject(paramJsonString)
            } else {
                JSONObject()
            }
        } catch (e: Exception) {
            JSONObject()
        }
    }
    private val paramDarkFont: Int by lazy { params.optInt("darkFont", 0) }
    private val paramSwipeBack: Int by lazy { params.optInt("swipeBack", 1) }
    private val paramDoubleBack: Int by lazy { params.optInt("doubleBack", 0) }

    private val startTime = System.currentTimeMillis()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableImmersionStatusBar = true
        enableImmersionStatusBarWithDarkFont = paramDarkFont == 1
        enableExitWithDoubleBackPressed = paramDoubleBack == 1
        enableSwipeBack = !enableExitWithDoubleBackPressed && (paramSwipeBack == 1)

        STLogUtil.v(TAG, "initialProperties:$initialProperties")
        STLogUtil.v(TAG, "pageName:$pageName")
        STLogUtil.v(TAG, "params:$params")
        STLogUtil.v(TAG, "paramSwipeBack:$paramSwipeBack")
        STLogUtil.v(TAG, "paramDarkFont:$paramDarkFont")
        STLogUtil.v(TAG, "enableImmersionStatusBar:$enableImmersionStatusBar")
        STLogUtil.v(TAG, "enableImmersionStatusBarWithDarkFont:$enableImmersionStatusBarWithDarkFont")
        STLogUtil.v(TAG, "enableExitWithDoubleBackPressed:$enableExitWithDoubleBackPressed")
        STLogUtil.v(TAG, "enableSwipeBack:$enableSwipeBack")

        super.onCreate(savedInstanceState)

        setContentView(
                FrameLayout(this).apply {
                    this.fitsSystemWindows = false
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

                if (RNInstanceManager.instanceManager != null) {
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
                    RNInstanceManager.initInstanceManager { success: Boolean ->
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
        reactRootView.setEventListener(object : ReactRootView.ReactRootViewEventListener {
            override fun onAttachedToReactInstance(rootView: ReactRootView?) {
                val time = System.currentTimeMillis() - startTime
                STLogUtil.w(TAG, "渲染成功, 耗时:$time ms")
                STToastUtil.show("渲染成功, 耗时:$time ms")

                // 首屏发送通知
                if (!STPreferencesUtil.getBoolean("react-native-inited", false)) {
                    STEventManager.sendEvent("react-native-inited", "renderSuccess")
                }
            }
        })
        reactRootView.post {
            reactInstanceManager = RNInstanceManager.instanceManager
            STLogUtil.w(TAG, "startReactApplication moduleName=$moduleName, initialProperties=$initialProperties")
            reactRootView.startReactApplication(reactInstanceManager, moduleName, initialProperties)
            if (isResumed) reactInstanceManager?.onHostResume(this, this)
            STLogUtil.w(TAG, "startReactApplication end")
        }
    }

    private fun loadBusinessCode() {
        STLogUtil.w(TAG, "loadBusinessCode")
        val businessBundlePath = RNConstant.PATH_ASSETS_BUSINESS_BUNDLE
        RNInstanceManager.loadBundle(RNBundle(businessBundlePath, JSBundleLoader.createAssetLoader(this@RNActivity, businessBundlePath, false)))
    }

    /**
     * 重新设置 react 属性, 并重新渲染 react 界面
     */
    fun updateReactProperties(appProperties: Bundle?) {
        reactRootView.appProperties = appProperties
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

    override fun onBackPressedIntercept(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        STLogUtil.d(TAG, "onBackPress")
        if (reactInstanceManager != null) {
            reactInstanceManager?.onBackPressed()
        } else {
            invokeDefaultOnBackPressed()
        }
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
        if (enableExitWithDoubleBackPressed) {
            exitApp()
        } else {
            super.onBackPressed()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (RNInstanceManager.debug && reactInstanceManager != null && keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            event.startTracking() // 追踪该按键以判断是否触发 onKeyLongPress
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        if (RNInstanceManager.debug && reactInstanceManager != null && keyCode == KeyEvent.KEYCODE_MENU) {
            reactInstanceManager?.showDevOptionsDialog()
            return true
        }
        return super.onKeyUp(keyCode, event)
    }

    override fun onKeyLongPress(keyCode: Int, event: KeyEvent?): Boolean {
        if (RNInstanceManager.debug && reactInstanceManager != null && keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            reactInstanceManager?.showDevOptionsDialog()
            return true
        }
        return super.onKeyLongPress(keyCode, event)
    }

    override fun onDestroy() {
        reactRootView.unmountReactApplication()
        reactInstanceManager?.onHostDestroy(this)
        if (isOnCreateAppliedSuccess) STDeployManager.REACT_NATIVE.onDestroy(this)
        super.onDestroy()
    }

}
