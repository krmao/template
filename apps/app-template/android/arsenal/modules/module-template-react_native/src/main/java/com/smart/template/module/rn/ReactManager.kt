package com.smart.template.module.rn

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import com.facebook.react.ReactInstanceManager
import com.facebook.react.ReactInstanceManagerBuilder
import com.facebook.react.ReactPackage
import com.facebook.react.bridge.Promise
import com.facebook.react.common.LifecycleState
import com.facebook.react.shell.MainPackageConfig
import com.facebook.react.shell.MainReactPackage
import com.smart.template.module.rn.dev.ReactDevSettingsManager
import com.smart.library.base.CXBaseApplication
import com.smart.library.util.CXFileUtil
import com.smart.library.util.CXLogUtil
import com.smart.library.util.cache.CXCacheManager
import com.smart.template.module.rn.packages.ReactCustomPackage
import java.io.File


@Suppress("MemberVisibilityCanBePrivate", "unused")
@SuppressLint("StaticFieldLeak")
object ReactManager {
    const val TAG: String = "react native"

    @JvmStatic
    var application: Application = CXBaseApplication.INSTANCE
        private set

    @JvmStatic
    var debug: Boolean = CXBaseApplication.DEBUG
        private set

    @JvmStatic
    var instanceManager: ReactInstanceManager? = null
        private set
        get() {
            if (field == null) {
                if (checkValidPackageOrServer(jsBundleFile)) {
                    field = getInstanceBuilder(jsBundleFile).build()
                }
            }
            CXLogUtil.e(TAG, "react native instance==null?${field == null}")
            return field
        }

    @JvmStatic
    var onCallNativeListener: ((activity: Activity?, functionName: String?, data: String?, promise: Promise?) -> Unit?)? = null

    @JvmStatic
    val devSettingsManager: ReactDevSettingsManager by lazy { ReactDevSettingsManager(application, debug) }

    const val enableHotPatch = true
    const val indexName = "index.android.bundle"

    @JvmStatic
    val localHotPatchIndexFile by lazy { File(CXCacheManager.getChildCacheDir("rn"), indexName) }

    // assets://index.android.bundle
    // /sdcard/Android/data/com.smart.template/cache/rn/index.android.bundle
    // /sdcard/Android/data/com.smart.template/cache/rn/js-modules
    // /sdcard/Android/data/com.smart.template/cache/rn/drawable-xxxhdpi
    // val jsBundleFile = if (!enableHotPatch || !localHotPatchIndexFile.exists()) "assets://$indexName" else localHotPatchIndexFile.absolutePath
    // val jsBundleFile = localHotPatchIndexFile.absolutePath
    const val jsBundleFile = "assets://$indexName"

    @JvmStatic
    @JvmOverloads
    @Synchronized
    fun init(application: Application, debug: Boolean, onCallNativeListener: ((activity: Activity?, functionName: String?, data: String?, promise: Promise?) -> Unit?)? = null) {
        this.application = application
        this.debug = debug
        this.onCallNativeListener = onCallNativeListener

        instanceManager?.createReactContextInBackground()
        CXLogUtil.e(TAG, "react native did${if (instanceManager == null) " not " else " "}run createReactContextInBackground")
    }

    @JvmStatic
    fun checkValidPackageOrServer(jsBundleFile: String?): Boolean {
        CXLogUtil.d(TAG, "checkValidPackageOrServer jsBundleFile(exists=${CXFileUtil.existsInAssets(jsBundleFile)}):$jsBundleFile")
        return (jsBundleFile?.isNotEmpty() == true && CXFileUtil.existsInAssets(jsBundleFile)) || isCurrentLoadModeServer()
    }

    @JvmStatic
    fun isCurrentLoadModeServer(): Boolean = devSettingsManager.getDebugHttpHost().isNotEmpty()

    @JvmStatic
    @JvmOverloads
    fun getInstanceBuilder(jsBundleFile: String, jsMainModulePath: String = "index"): ReactInstanceManagerBuilder {
        return ReactInstanceManager.builder()
            .setApplication(application)
            .setJSBundleFile(jsBundleFile)
            .setJSMainModulePath(jsMainModulePath)
            .addPackages(
                mutableListOf<ReactPackage>(
                    MainReactPackage(MainPackageConfig.Builder().apply {
                        //setFrescoConfig() fixme
                    }.build()),
                    ReactCustomPackage()
                )
            )
            .setUseDeveloperSupport(debug)
            .setInitialLifecycleState(LifecycleState.BEFORE_CREATE)
    }

}
