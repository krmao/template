package com.smart.template.module.rn

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import com.facebook.react.ReactInstanceManager
import com.facebook.react.ReactInstanceManagerBuilder
import com.facebook.react.ReactPackage
import com.facebook.react.bridge.CatalystInstanceImpl
import com.facebook.react.bridge.JSBundleLoader
import com.facebook.react.bridge.Promise
import com.facebook.react.common.LifecycleState
import com.facebook.react.common.ReactConstants
import com.facebook.react.devsupport.RedBoxHandler
import com.facebook.react.devsupport.interfaces.StackFrame
import com.facebook.react.shell.MainPackageConfig
import com.facebook.react.shell.MainReactPackage
import com.facebook.react.uimanager.UIImplementationProvider
import com.smart.library.base.CXBaseApplication
import com.smart.library.util.CXFileUtil
import com.smart.library.util.CXLogUtil
import com.smart.library.util.cache.CXCacheManager
import com.smart.template.module.rn.dev.ReactDevSettingsManager
import com.smart.template.module.rn.packages.ReactCustomPackage
import java.io.File
import java.util.*


@Suppress("MemberVisibilityCanBePrivate", "unused")
@SuppressLint("StaticFieldLeak")
object ReactManager {
    const val TAG: String = ReactConstants.TAG

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
                    field = getInstanceBuilder(jsBundleFile).build()?.apply {
                        addReactInstanceEventListener {
                            isInitialized = true
                            CXLogUtil.w(TAG, "initialized base bundle success")

                            getCatalystInstance()?.let {
                                loadBundleTasks.forEach { loader -> loader.loadScript(it) }
                                loadBundleTasks.clear()
                            }
                        }
                        CXLogUtil.w(TAG, "createReactContextInBackground start...")
                        createReactContextInBackground()
                    }
                }
            }
            CXLogUtil.e(TAG, "react native instance==null?${field == null}")
            return field
        }

    @Volatile
    private var isInitialized = false
    private val loadBundleTasks: Vector<JSBundleLoader> = Vector()
    @JvmStatic
    fun loadBundle(bundleLoader: JSBundleLoader?) {
        bundleLoader?.let { loader ->
            if (!isInitialized) {
                synchronized(isInitialized) {
                    if (!isInitialized) {
                        loadBundleTasks.add(loader)
                        return
                    }
                }
            }
            getCatalystInstance()?.let { loader.loadScript(it) }
        }
    }

    @JvmStatic
    fun getCatalystInstance(): CatalystInstanceImpl? = instanceManager?.currentReactContext?.catalystInstance as CatalystInstanceImpl?

    @JvmStatic
    var onCallNativeListener: ((activity: Activity?, functionName: String?, data: String?, promise: Promise?) -> Unit?)? = null

    @JvmStatic
    val devSettingsManager: ReactDevSettingsManager by lazy { ReactDevSettingsManager(application, debug) }

    const val enableHotPatch = true
    const val indexName = "base.bundle"

    @JvmStatic
    val localHotPatchIndexFile by lazy { File(CXCacheManager.getChildCacheDir("rn"), indexName) }

    // assets://index.android.bundle
    // /sdcard/Android/data/com.smart.template/cache/rn/index.android.bundle
    // /sdcard/Android/data/com.smart.template/cache/rn/js-modules
    // /sdcard/Android/data/com.smart.template/cache/rn/drawable-xxxhdpi
    // val jsBundleFile = if (!enableHotPatch || !localHotPatchIndexFile.exists()) "assets://$indexName" else localHotPatchIndexFile.absolutePath
    // val jsBundleFile = localHotPatchIndexFile.absolutePath
    const val jsBundleFile = ReactConstant.PATH_ASSETS_BASE_BUNDLE

    @JvmStatic
    @JvmOverloads
    @Synchronized
    fun init(application: Application, debug: Boolean, onCallNativeListener: ((activity: Activity?, functionName: String?, data: String?, promise: Promise?) -> Unit?)? = null) {
        this.application = application
        this.debug = debug
        this.onCallNativeListener = onCallNativeListener

        // sure to call instanceManager one time
        if (instanceManager == null) {
            CXLogUtil.e(TAG, "instanceManager is null, please check the bundle path or debug server is set")
        }
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
            .setUIImplementationProvider(UIImplementationProvider())
            .setRedBoxHandler(object : RedBoxHandler {
                override fun handleRedbox(title: String?, stack: Array<out StackFrame>?, errorType: RedBoxHandler.ErrorType?) {
                    CXLogUtil.e(TAG, "handleRedbox errorType:${errorType?.name}, title:$title, stack:$stack")
                }

                override fun isReportEnabled(): Boolean {
                    return false
                }

                override fun reportRedbox(context: Context?, title: String?, stack: Array<out StackFrame>?, sourceUrl: String?, reportCompletedListener: RedBoxHandler.ReportCompletedListener?) {
                    CXLogUtil.e(TAG, "reportRedbox title:$title, stack:$stack, sourceUrl:$sourceUrl")
                }
            })
            // .setJSBundleFile(jsBundleFile)
             .setJSMainModulePath(jsMainModulePath)
            .setJSBundleLoader(JSBundleLoader.createAssetLoader(application, jsBundleFile, false))
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
