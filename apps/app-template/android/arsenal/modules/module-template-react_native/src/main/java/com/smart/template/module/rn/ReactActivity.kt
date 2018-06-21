package com.smart.template.module.rn

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.KeyEvent
import android.widget.FrameLayout
import com.facebook.react.ReactInstanceManager
import com.facebook.react.ReactRootView
import com.facebook.react.bridge.CatalystInstanceImpl
import com.facebook.react.bridge.JSBundleLoader
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler
import com.smart.library.base.CXBaseActivity
import com.smart.library.base.CXBaseApplication
import com.smart.library.util.CXLogUtil
import com.smart.library.util.CXToastUtil


@Suppress("unused", "PrivatePropertyName")
class ReactActivity : CXBaseActivity(), DefaultHardwareBackBtnHandler {

    companion object {
        const val KEY_RESULT: String = "rn_result"
        const val KEY_START_COMPONENT: String = "rn_start_component"
        const val KEY_START_COMPONENT_PAGE: String = "rn_start_component_page"

        @JvmStatic
        @JvmOverloads
        fun start(context: Context?, component: String? = null, page: String? = null) {
            val intent = Intent(context, ReactActivity::class.java)
            intent.putExtra(KEY_START_COMPONENT, component)
            intent.putExtra(KEY_START_COMPONENT_PAGE, page)
            context?.startActivity(intent)
        }

        @JvmStatic
        @JvmOverloads
        fun startWithNewTask(context: Context?, component: String? = null, page: String? = null) {
            val intent = Intent(context, ReactActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra(KEY_START_COMPONENT, component)
            intent.putExtra(KEY_START_COMPONENT_PAGE, page)
            context?.startActivity(intent)
        }
    }

    private val OVERLAY_PERMISSION_REQ_CODE = 666

    private val TAG: String = ReactManager.TAG

    private var reactRootView: ReactRootView? = null

    private var reactInstanceManager: ReactInstanceManager? = null

    private val moduleName: String
        get() = intent.getStringExtra(KEY_START_COMPONENT)
            ?: ReactManager.devSettingsManager.getDefaultStartComponent()

    private val pageName: String
        get() = intent.getStringExtra(KEY_START_COMPONENT_PAGE)
            ?: ReactManager.devSettingsManager.getDefaultStartComponentPage()

    private val initialProperties by lazy { Bundle().apply { putString("page", pageName) } }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableSwipeBack = false
        super.onCreate(savedInstanceState)
        reactRootView = ReactRootView(this)
        setContentView(
            FrameLayout(this).apply {
                this.fitsSystemWindows = true
                this.addView(reactRootView)
            }
        )

        reactInstanceManager = ReactManager.instanceManager

        CXLogUtil.w(TAG, "reactInstanceManager==null?${reactInstanceManager == null}")

        if (reactInstanceManager != null) {

            /**
             * debug 环境下的红色调试界面需要权限 ACTION_MANAGE_OVERLAY_PERMISSION
             * If your app is targeting the Android API level 23 or greater, make sure you have the overlay permission enabled for the development build. You can check it with Settings.canDrawOverlays(this);. This is required in dev builds because react native development errors must be displayed above all the other windows. Due to the new permissions system introduced in the API level 23, the user needs to approve it. This can be achieved by adding the following code to the Activity file in the onCreate() method. OVERLAY_PERMISSION_REQ_CODE is a field of the class which would be responsible for passing the result back to the Activity.
             */
            if (CXBaseApplication.DEBUG && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
                startActivityForResult(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName")), OVERLAY_PERMISSION_REQ_CODE)
            } else {
                startReact()
                // debug 模式下每次进来强制重新加载, 避免摇晃手机等操作
                if (CXBaseApplication.DEBUG && ReactManager.isCurrentLoadModeServer()) {
                    Looper.myQueue().addIdleHandler {
                        CXLogUtil.e(TAG, "debug 模式下自动 reload -->")
                        CXToastUtil.show("debug 模式下自动 reload -->")
//                        ReactManager.devSettingsManager.reload()

                        false
                    }
                }
            }

        } else {
            CXLogUtil.e(TAG, "请提供可以被 RN 初始化的离线包或者远程调试主机")
            CXToastUtil.show("请提供可以被 RN 初始化的离线包或者远程调试主机")
            finish()
            return
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this)) {
                    // SYSTEM_ALERT_WINDOW permission not granted...
                    CXToastUtil.show("调试模式请务必打开 SYSTEM_ALERT_WINDOW 权限")
                    finish()
                    return
                } else {
                    startReact()
                    // debug 模式下每次进来强制重新加载, 避免摇晃手机等操作
                    if (CXBaseApplication.DEBUG && ReactManager.isCurrentLoadModeServer()) {
                        Looper.myQueue().addIdleHandler {
                            CXLogUtil.e(TAG, "debug 模式下自动 reload -->")
                            CXToastUtil.show("debug 模式下自动 reload -->")
//                            ReactManager.devSettingsManager.reload()

                            false
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("SdCardPath")
    private fun startReact() {
        CXLogUtil.w(TAG, "startReact")
        reactRootView?.startReactApplication(reactInstanceManager, moduleName, initialProperties)

        return

        var businessBundlePath = "assets://business.android.bundle"
        //businessBundlePath = "/sdcard/Android/data/com.smart.template/cache/rn/business.android.bundle"

        /**
         * Called when the react context is initialized (all modules registered). Always called on the
         * UI thread.
         */

        reactInstanceManager?.addReactInstanceEventListener {
            CXLogUtil.e(TAG, "initialized success")

            try {
                (reactInstanceManager?.currentReactContext?.catalystInstance as? CatalystInstanceImpl)?.let { catalystInstance ->
                    JSBundleLoader.createAssetLoader(this@ReactActivity, businessBundlePath, true).loadScript(catalystInstance)
                }
                CXLogUtil.e(TAG, "loadBusinessBundle $businessBundlePath done.")
            } catch (e: Throwable) {
                CXLogUtil.e(TAG, "loadBusinessBundle $businessBundlePath error.", e)
            }

//            (reactInstanceManager?.currentReactContext?.catalystInstance as? CatalystInstanceImpl)?.let { catalystInstance ->
//                JSBundleLoader.createFileLoader(businessBundlePath, businessBundlePath, true).loadScript(catalystInstance)
//            }

            /*var loadScriptFile: Method? = null
            if (loadScriptFile == null) {
                try {
                    loadScriptFile = CatalystInstanceImpl::class.java.getDeclaredMethod("loadScriptFromFile", String::class.java, String::class.java, java.lang.Boolean.TYPE)
                    loadScriptFile.isAccessible = true
                } catch (e: NoSuchMethodException) {
                    CXLogUtil.e(TAG, "cannot found method: CatalystInstanceImpl.loadScriptFromFile(String, String)", e)
                }
            }

            val catalystInstance = reactInstanceManager?.currentReactContext?.catalystInstance
            CXLogUtil.e(TAG, "loadBusinessBundle $businessBundlePath start...")
            try {
                loadScriptFile?.invoke(catalystInstance, businessBundlePath, businessBundlePath, true)
                CXLogUtil.e(TAG, "loadBusinessBundle $businessBundlePath done.")
            } catch (e: Throwable) {
                CXLogUtil.e(TAG, "loadBusinessBundle $businessBundlePath error.")
                CXLogUtil.e(TAG, "error invoke method: CatalystInstanceImpl.loadScriptFromFile(String, String)", e)
            }*/

//            reactRootView?.startReactApplication(reactInstanceManager, moduleName, initialProperties)
        }

        reactInstanceManager?.createReactContextInBackground()
    }

    /**
     * 重新设置 react 属性, 并重新渲染 react 界面
     */
    fun updateReactProperties(appProperties: Bundle?) {
        reactRootView?.appProperties = appProperties
    }


    override fun onResume() {
        super.onResume()
        reactInstanceManager?.onHostResume(this, this)
    }

    override fun onPause() {
        super.onPause()
        reactInstanceManager?.onHostPause(this)
    }

    override fun onBackPress(): Boolean {
        CXLogUtil.d(TAG, "onBackPress")
        reactInstanceManager?.onBackPressed()
        return true
    }

    override fun invokeDefaultOnBackPressed() {
        CXLogUtil.d(TAG, "invokeDefaultOnBackPressed")
        super.onBackPressed()
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            reactInstanceManager?.showDevOptionsDialog()
            return true
        }
        return super.onKeyUp(keyCode, event)
    }

    override fun onDestroy() {
        reactInstanceManager?.onHostDestroy(this)
        super.onDestroy()
    }

}
