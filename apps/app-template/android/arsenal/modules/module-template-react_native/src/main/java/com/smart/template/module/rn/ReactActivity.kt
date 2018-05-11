package com.smart.template.module.rn

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.KeyEvent
import com.facebook.react.ReactInstanceManager
import com.facebook.react.ReactPackage
import com.facebook.react.common.LifecycleState
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler
import com.facebook.react.shell.MainReactPackage
import com.smart.library.base.CXBaseActivity
import com.smart.library.base.CXBaseApplication
import com.smart.library.util.cache.CXCacheManager
import kotlinx.android.synthetic.main.react_activity.*
import java.io.File


@Suppress("unused", "PrivatePropertyName")
class ReactActivity : CXBaseActivity(), DefaultHardwareBackBtnHandler {
    override fun invokeDefaultOnBackPressed() {
        super.onBackPressed()
    }

    private var enableHotPatch = false

    private var mReactInstanceManager: ReactInstanceManager? = null

    private val OVERLAY_PERMISSION_REQ_CODE = 666

    private var count: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        enableSwipeBack = false
        super.onCreate(savedInstanceState)
        setContentView(R.layout.react_activity)

        val indexName = "index.android.bundle"
        val localHotPatchIndexFile = File(CXCacheManager.getChildCacheDir("rn"), indexName)
        val jsBundleFile = if (!enableHotPatch || !localHotPatchIndexFile.exists()) "assets://index.android.bundle" else localHotPatchIndexFile.absolutePath

        mReactInstanceManager = ReactInstanceManager.builder()
            .setApplication(application)
            .setJSBundleFile(jsBundleFile) // "assets://index.android.bundle" or "/sdcard/smart/react/index.android.bundle" 热更新取决于此
            .setJSMainModulePath("index")
            .addPackages(
                mutableListOf<ReactPackage>(
                    MainReactPackage(),
                    ReactNativePackage()
                )
            )
            .setUseDeveloperSupport(CXBaseApplication.DEBUG)
            .setInitialLifecycleState(LifecycleState.RESUMED)
            .build()

        val bundle = Bundle()

        bundle.putInt("native_params", 1)

        react_root_view?.startReactApplication(mReactInstanceManager, "react-module-home", bundle)

        btn_menu?.setOnClickListener {
            mReactInstanceManager?.showDevOptionsDialog()
        }

        btn_send?.setOnClickListener {
            //向 react native 发送数据
            ReactBridge.callReact(mReactInstanceManager?.currentReactContext, "native_event", "msg://${count++}")

            bundle.putInt("native_params", 9000 + count)
            updateReactProperties(bundle)
        }

        /**
         * If your app is targeting the Android API level 23 or greater, make sure you have the overlay permission enabled for the development build. You can check it with Settings.canDrawOverlays(this);. This is required in dev builds because react native development errors must be displayed above all the other windows. Due to the new permissions system introduced in the API level 23, the user needs to approve it. This can be achieved by adding the following code to the Activity file in the onCreate() method. OVERLAY_PERMISSION_REQ_CODE is a field of the class which would be responsible for passing the result back to the Activity.
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            startActivityForResult(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + packageName)), OVERLAY_PERMISSION_REQ_CODE)
        }
    }

    /**
     * 重新设置 react 属性, 并重新渲染 react 界面
     */
    private fun updateReactProperties(bundle: Bundle?) {
        react_root_view?.appProperties = bundle
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

    override fun onPause() {
        super.onPause()

        mReactInstanceManager?.onHostPause(this)
    }

    override fun onResume() {
        super.onResume()

        mReactInstanceManager?.onHostResume(this, this)
    }

    override fun onDestroy() {
        super.onDestroy()

        mReactInstanceManager?.onHostDestroy(this)
    }

    override fun onBackPress(): Boolean {
        if (mReactInstanceManager != null) {
            mReactInstanceManager!!.onBackPressed()
            return true
        } else {
            return super.onBackPress()
        }
    }

    override fun onBackPressed() {
        if (mReactInstanceManager != null) {
            mReactInstanceManager!!.onBackPressed()
        } else {
            super.onBackPressed()
        }
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            mReactInstanceManager?.showDevOptionsDialog()
            return true
        }
        return super.onKeyUp(keyCode, event)
    }
}
