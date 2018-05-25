package com.smart.template.module.rn

import android.annotation.SuppressLint
import android.app.Application
import com.facebook.react.ReactInstanceManager
import com.facebook.react.ReactPackage
import com.facebook.react.common.LifecycleState
import com.facebook.react.shell.MainReactPackage
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

            val enableHotPatch = false
            val indexName = "index.android.bundle"

            val localHotPatchIndexFile = File(CXCacheManager.getChildCacheDir("rn"), indexName)

            // "assets://index.android.bundle" or "/sdcard/smart/react/index.android.bundle" 热更新取决于此
            val jsBundleFile = if (!enableHotPatch || !localHotPatchIndexFile.exists()) "assets://$indexName" else localHotPatchIndexFile.absolutePath

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
