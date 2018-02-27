package com.smart.template.module.react

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.KeyEvent
import com.facebook.react.ReactInstanceManager
import com.facebook.react.bridge.ReactContext
import com.facebook.react.common.LifecycleState
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler
import com.facebook.react.modules.core.DeviceEventManagerModule
import com.facebook.react.shell.MainReactPackage
import com.smart.library.base.CXBaseActivity
import kotlinx.android.synthetic.main.react_activity.*


@Suppress("unused", "PrivatePropertyName")
class ReactActivity : CXBaseActivity(), DefaultHardwareBackBtnHandler {
    override fun invokeDefaultOnBackPressed() {
        super.onBackPressed();
    }

    private var mReactInstanceManager: ReactInstanceManager? = null

    private val OVERLAY_PERMISSION_REQ_CODE = 666

    private var count: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.react_activity)

        mReactInstanceManager = ReactInstanceManager.builder()
            .setApplication(application)
            .setBundleAssetName("index.android.bundle")
            .setJSMainModulePath("index")
            .addPackage(MainReactPackage())
            .setUseDeveloperSupport(BuildConfig.DEBUG)
            .setInitialLifecycleState(LifecycleState.RESUMED)
            .build()

        val bundle = Bundle()

        bundle.putInt("a", 1)
        bundle.putString("b", "2")
        bundle.putDouble("c", 3.0)
        bundle.putIntegerArrayList("d", arrayListOf(4, 5, 6, 7, 8, 9))

        react_root_view?.startReactApplication(mReactInstanceManager, "react-module-home", bundle)

        btn_menu?.setOnClickListener {
            mReactInstanceManager?.showDevOptionsDialog()
        }
        btn_send?.setOnClickListener {
            //向 react native 发送数据
            mReactInstanceManager?.currentReactContext?.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)?.emit("native_event", "msg://${count++}")
        }

        /**
         * If your app is targeting the Android API level 23 or greater, make sure you have the overlay permission enabled for the development build. You can check it with Settings.canDrawOverlays(this);. This is required in dev builds because react native development errors must be displayed above all the other windows. Due to the new permissions system introduced in the API level 23, the user needs to approve it. This can be achieved by adding the following code to the Activity file in the onCreate() method. OVERLAY_PERMISSION_REQ_CODE is a field of the class which would be responsible for passing the result back to the Activity.
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            startActivityForResult(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + packageName)), OVERLAY_PERMISSION_REQ_CODE)
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

    override fun onPause() {
        super.onPause()

        if (mReactInstanceManager != null) {
            mReactInstanceManager?.onHostPause(this)
        }
    }

    override fun onResume() {
        super.onResume()

        if (mReactInstanceManager != null) {
            mReactInstanceManager?.onHostResume(this, this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        if (mReactInstanceManager != null) {
            mReactInstanceManager?.onHostDestroy(this)
        }
    }

    override fun onBackPressed() {
        if (mReactInstanceManager != null) {
            mReactInstanceManager?.onBackPressed()
        } else {
            super.onBackPressed()
        }
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_MENU && mReactInstanceManager != null) {
            mReactInstanceManager?.showDevOptionsDialog()
            return true
        }
        return super.onKeyUp(keyCode, event)
    }
}
