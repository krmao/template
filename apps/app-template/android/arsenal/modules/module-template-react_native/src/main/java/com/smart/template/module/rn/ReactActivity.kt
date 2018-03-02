package com.smart.template.module.rn

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.KeyEvent
import com.facebook.react.LifecycleState
import com.facebook.react.ReactInstanceManager
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler
import com.facebook.react.shell.MainReactPackage
import com.smart.library.base.CXBaseActivity
import com.smart.library.base.CXBaseApplication
import kotlinx.android.synthetic.main.react_activity.*


@Suppress("unused", "PrivatePropertyName")
class ReactActivity : CXBaseActivity(), DefaultHardwareBackBtnHandler {
    override fun invokeDefaultOnBackPressed() {
        super.onBackPressed()
    }

    private var mReactInstanceManager: ReactInstanceManager? = null

    private val OVERLAY_PERMISSION_REQ_CODE = 666

    private var count: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.react_activity)

        mReactInstanceManager = ReactInstanceManager.builder()
            .setApplication(application)
            .setJSBundleFile("assets://index.android.js") //"assets://index.android.js" or "/sdcard/smart/react/index.android.js" 热更新取决于此
            .setBundleAssetName("index.android.bundle")
            .setJSMainModuleName("index")
            .addPackage(MainReactPackage())
            .addPackage(ReactNativePackage())
            .setUseDeveloperSupport(CXBaseApplication.DEBUG)
            .setInitialLifecycleState(LifecycleState.RESUMED)
            .build()

        val bundle = Bundle()

        bundle.putInt("native_params", 1)

        updateReactProperties(bundle)

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
            startActivityForResult(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName")), OVERLAY_PERMISSION_REQ_CODE)
        }
    }

    /**
     * 重新设置 react 属性, 并重新渲染 react 界面
     */
    private fun updateReactProperties(bundle: Bundle?) {
        react_root_view?.startReactApplication(mReactInstanceManager, "react-module-home", bundle)
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

        mReactInstanceManager?.onPause()
    }

    override fun onResume() {
        super.onResume()

        mReactInstanceManager?.onResume(this, this)
    }

    override fun onDestroy() {
        super.onDestroy()

        mReactInstanceManager?.onDestroy()
    }

    override fun onBackPressed() {
        mReactInstanceManager?.onBackPressed() ?: super.onBackPressed()
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            mReactInstanceManager?.showDevOptionsDialog()
            return true
        }
        return super.onKeyUp(keyCode, event)
    }
}
