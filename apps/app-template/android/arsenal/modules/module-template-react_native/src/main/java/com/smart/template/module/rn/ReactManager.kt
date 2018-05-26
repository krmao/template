package com.smart.template.module.rn

import android.annotation.SuppressLint
import android.app.Application
import com.facebook.react.ReactInstanceManager
import com.facebook.react.ReactPackage
import com.facebook.react.common.LifecycleState
import com.facebook.react.shell.MainReactPackage
import com.smart.library.util.CXLogUtil
import com.smart.library.util.cache.CXCacheManager
import java.io.File


@Suppress("MemberVisibilityCanBePrivate", "unused")
@SuppressLint("StaticFieldLeak")
object ReactManager {
    const val TAG: String = "[react native]"

    @JvmStatic
    var reactInstanceManager: ReactInstanceManager? = null
        private set

    @JvmStatic
    @Synchronized
    fun init(application: Application, debug: Boolean) {
        if (reactInstanceManager == null) {

            val enableHotPatch = true
            val indexName = "index.android.bundle"

            val localHotPatchIndexFile = File(CXCacheManager.getChildCacheDir("rn"), indexName)

            // assets://index.android.bundle
            // /sdcard/Android/data/com.smart.template/cache/rn/index.android.bundle
            // /sdcard/Android/data/com.smart.template/cache/rn/js-modules
            // /sdcard/Android/data/com.smart.template/cache/rn/drawable-xxxhdpi

            // val jsBundleFile = if (!enableHotPatch || !localHotPatchIndexFile.exists()) "assets://$indexName" else localHotPatchIndexFile.absolutePath
            var jsBundleFile = localHotPatchIndexFile.absolutePath
            jsBundleFile = "assets://$indexName"

            CXLogUtil.e(TAG, "localHotPatchIndexFile:exists=${localHotPatchIndexFile.exists()} , $jsBundleFile")

            reactInstanceManager = ReactInstanceManager.builder()
                .setApplication(application)
                .setJSBundleFile(jsBundleFile)
                .setJSMainModulePath("index")
                .addPackages(
                    mutableListOf<ReactPackage>(
                        MainReactPackage(),
                        ReactCustomPackage()
                    )
                )
                .setUseDeveloperSupport(debug)
                .setInitialLifecycleState(LifecycleState.BEFORE_CREATE)
                .build()

            reactInstanceManager?.createReactContextInBackground()
        }
    }
}
