package com.smart.template.module.rn

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.KeyEvent
import android.widget.FrameLayout
import com.facebook.react.ReactInstanceManager
import com.facebook.react.ReactRootView
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler
import com.smart.library.base.CXBaseActivity
import com.smart.library.base.CXBaseApplication
import com.smart.library.util.CXLogUtil

@Suppress("unused", "PrivatePropertyName")
class ReactActivity : CXBaseActivity(), DefaultHardwareBackBtnHandler {

    private val OVERLAY_PERMISSION_REQ_CODE = 666

    private val TAG: String = ReactManager.TAG

    private var reactRootView: ReactRootView? = null

    private var reactInstanceManager: ReactInstanceManager? = null
    
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

        reactInstanceManager = ReactManager.reactInstanceManager
        val moduleName = "react-module-home"
        val initialProperties = Bundle()
        reactRootView?.startReactApplication(reactInstanceManager, moduleName, initialProperties)

        /**
         * debug 环境下的红色调试界面需要权限 ACTION_MANAGE_OVERLAY_PERMISSION
         * If your app is targeting the Android API level 23 or greater, make sure you have the overlay permission enabled for the development build. You can check it with Settings.canDrawOverlays(this);. This is required in dev builds because react native development errors must be displayed above all the other windows. Due to the new permissions system introduced in the API level 23, the user needs to approve it. This can be achieved by adding the following code to the Activity file in the onCreate() method. OVERLAY_PERMISSION_REQ_CODE is a field of the class which would be responsible for passing the result back to the Activity.
         */
        if (CXBaseApplication.DEBUG && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            startActivityForResult(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName")), OVERLAY_PERMISSION_REQ_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this)) {
                    // SYSTEM_ALERT_WINDOW permission not granted...
                }
            }
        }
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
