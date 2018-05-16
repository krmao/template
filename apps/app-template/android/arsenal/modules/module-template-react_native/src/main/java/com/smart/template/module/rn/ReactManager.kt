package com.smart.template.module.rn

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.KeyEvent
import android.view.ViewGroup
import com.facebook.react.ReactInstanceManager
import com.facebook.react.ReactPackage
import com.facebook.react.ReactRootView
import com.facebook.react.common.LifecycleState
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler
import com.facebook.react.shell.MainReactPackage
import com.smart.library.base.CXBaseApplication
import com.smart.library.util.cache.CXCacheManager
import java.io.File


@Suppress("MemberVisibilityCanBePrivate", "unused")
@SuppressLint("StaticFieldLeak")
object ReactManager {

    const val TAG: String = "[react native]"

    var activity: Activity? = null
        private set

    var enableHotPatch = false
        private set

    var reactRootView: ReactRootView? = null
        private set

    var reactInstanceManager: ReactInstanceManager? = null
        private set

    /**
     * @see com.smart.template.module.rn.ReactManager.release() 结伴使用
     */
    @JvmOverloads
    @JvmStatic
    fun initialize(activity: Activity, enableHotPatch: Boolean = false, indexName: String = "index.android.bundle", moduleName: String = "react-module-home", initialProperties: Bundle? = null) {
        // 释放之前的
        release()

        this.activity = activity
        this.enableHotPatch = enableHotPatch

        reactRootView = ReactRootView(ReactManager.activity)

        val localHotPatchIndexFile = File(CXCacheManager.getChildCacheDir("rn"), indexName)
        val jsBundleFile = if (!enableHotPatch || !localHotPatchIndexFile.exists()) "assets://$indexName" else localHotPatchIndexFile.absolutePath

        reactInstanceManager = ReactInstanceManager.builder()
            .setApplication(CXBaseApplication.INSTANCE)
            .setJSBundleFile(jsBundleFile) // "assets://index.android.bundle" or "/sdcard/smart/react/index.android.bundle" 热更新取决于此
            .setJSMainModulePath("index")
            .addPackages(
                mutableListOf<ReactPackage>(
                    MainReactPackage(),
                    ReactCustomPackage()
                )
            )
            .setUseDeveloperSupport(CXBaseApplication.DEBUG)
            .setInitialLifecycleState(LifecycleState.BEFORE_RESUME)
            .build()

        reactRootView?.startReactApplication(reactInstanceManager, moduleName, initialProperties)
    }

    @JvmStatic
    fun onHostPause() {
        reactInstanceManager?.onHostPause(activity)
    }

    @JvmStatic
    fun onHostResume(defaultBackButtonImpl: DefaultHardwareBackBtnHandler) {
        reactInstanceManager?.onHostResume(activity, defaultBackButtonImpl)
    }

    @JvmStatic
    fun onHostDestroy() {
        (reactRootView?.parent as? ViewGroup?)?.removeView(reactRootView)
    }

    @JvmStatic
    fun onBackPressed() {
        reactInstanceManager?.onBackPressed()
    }

    @JvmStatic
    fun showDevOptionsDialog() {
        reactInstanceManager?.showDevOptionsDialog()
    }

    /**
     * 重新设置 react 属性, 并重新渲染 react 界面
     */
    @JvmStatic
    fun updateReactProperties(appProperties: Bundle?) {
        reactRootView?.appProperties = appProperties
    }

    @JvmStatic
    fun release() {
        reactInstanceManager?.onHostDestroy(activity)
        onHostDestroy()

        reactInstanceManager = null
        reactRootView = null
    }

}
