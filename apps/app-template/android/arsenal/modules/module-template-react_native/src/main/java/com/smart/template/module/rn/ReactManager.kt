package com.smart.template.module.rn

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import com.facebook.imagepipeline.core.ImagePipelineConfig
import com.facebook.react.ReactInstanceManager
import com.facebook.react.ReactInstanceManagerBuilder
import com.facebook.react.ReactPackage
import com.facebook.react.bridge.CatalystInstanceImpl
import com.facebook.react.bridge.JSBundleLoader
import com.facebook.react.bridge.Promise
import com.facebook.react.common.LifecycleState
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
    const val TAG: String = "[rn]"

    const val KEY_RN_CALL_NATIVE_PARAMS_HASH_MAP: String = "KEY_RN_CALL_NATIVE_PARAMS_HASH_MAP"
    const val KEY_RN_CALL_NATIVE_RESULT_HASH_MAP: String = "KEY_RN_CALL_NATIVE_RESULT_HASH_MAP"
    const val KEY_NATIVE_CALL_RN_PARAMS: String = "params"

    @JvmStatic
    var application: Application = CXBaseApplication.INSTANCE
        private set

    @JvmStatic
    var debug: Boolean = CXBaseApplication.DEBUG
        private set

    private var indexBundleFileInSdcard: File? = null
    @JvmStatic
    var instanceManager: ReactInstanceManager? = null
        private set
        get() {
            if (field == null) {
                val bundlePathInAssets = ReactConstant.PATH_ASSETS_BASE_BUNDLE
                if (checkValidBundleInAssets(bundlePathInAssets)) {
                    val bundleLoader = JSBundleLoader.createAssetLoader(application, bundlePathInAssets, false)
                    field = initInstanceManager(bundleLoader)
                } else if (checkValidBundleInSdcard(indexBundleFileInSdcard)) {
                    val bundleLoader = JSBundleLoader.createFileLoader(CXFileUtil.getFileName(bundlePathInAssets), bundlePathInAssets, false)
                    field = initInstanceManager(bundleLoader)
                } else if (checkValidRemoteDebugServer()) {
                    if (isRemoteJSDebugEnabled()) {
                        val remoteDebugServer = devSettingsManager.getDebugHttpHost()
                        val bundleLoader = JSBundleLoader.createRemoteDebuggerBundleLoader(remoteDebugServer, instanceManager?.devSupportManager?.jsBundleURLForRemoteDebugging)
                        field = initInstanceManager(bundleLoader)
                    } else {
                        val remoteDebugServer = devSettingsManager.getDebugHttpHost()
                        val cacheFile = File(CXCacheManager.getChildDir(CXCacheManager.getFilesHotPatchReactNativeDir(), "cacheBundleFromNetwork"), "index.android.location")
                        val bundleLoader = JSBundleLoader.createCachedBundleFromNetworkLoader(remoteDebugServer, cacheFile.absolutePath)
                        field = initInstanceManager(bundleLoader)
                    }
                }
            }
            CXLogUtil.e(TAG, "react native instance==null?${field == null}")
            return field
        }

    private fun initInstanceManager(bundleLoader: JSBundleLoader): ReactInstanceManager? {
        return getInstanceBuilder(bundleLoader, frescoConfig = frescoConfig).build()?.apply {
            addReactInstanceEventListener {
                isInitialized = true
                CXLogUtil.w(TAG, "initialized base bundle success")

                getCatalystInstance()?.let { loadBundleTasks.forEach { bundle -> bundle.loadScript(it) } }
            }
            CXLogUtil.w(TAG, "createReactContextInBackground start...")
            createReactContextInBackground()
        }
    }

    @Volatile
    private var isInitialized = false
    private val loadBundleTasks: Vector<ReactBundle> = Vector()
    @JvmStatic
    fun loadBundle(bundle: ReactBundle?) {
        bundle?.let {
            if (!isInitialized) {
                synchronized(isInitialized) {
                    if (!isInitialized) {
                        if (!loadBundleTasks.contains(bundle)) loadBundleTasks.add(it)
                        return
                    }
                }
            }

            if (loadBundleTasks.contains(bundle)) loadBundleTasks.elementAt(loadBundleTasks.indexOf(bundle)).loadScript(getCatalystInstance())
            else loadBundleTasks.add(it.loadScript(getCatalystInstance()))

            Unit
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
    val localHotPatchIndexFile by lazy { File(CXCacheManager.getFilesHotPatchReactNativeDir(), indexName) }

    // assets://index.android.bundle
    // /sdcard/Android/data/com.smart.template/cache/rn/index.android.bundle
    // /sdcard/Android/data/com.smart.template/cache/rn/js-modules
    // /sdcard/Android/data/com.smart.template/cache/rn/drawable-xxxhdpi
    // val jsBundleFile = if (!enableHotPatch || !localHotPatchIndexFile.exists()) "assets://$indexName" else localHotPatchIndexFile.absolutePath
    // val jsBundleFile = localHotPatchIndexFile.absolutePath

    var frescoConfig: ImagePipelineConfig? = null

    @JvmStatic
    @JvmOverloads
    @Synchronized
    fun init(application: Application, debug: Boolean, indexBundleFileInSdcard: File? = null, frescoConfig: ImagePipelineConfig?, onCallNativeListener: ((activity: Activity?, functionName: String?, data: String?, promise: Promise?) -> Unit?)? = null) {
        this.application = application
        this.debug = debug
        this.indexBundleFileInSdcard = indexBundleFileInSdcard
        this.frescoConfig = frescoConfig
        this.onCallNativeListener = onCallNativeListener

        // sure to call instanceManager one time
        if (instanceManager == null) {
            CXLogUtil.e(TAG, "instanceManager is null, please check the bundle path or debug server is set")
        } else {
            CXLogUtil.i(TAG, "instanceManager init success")
        }
    }

    @JvmStatic
    fun checkValidBundleInAssets(bundlePathInAssets: String?): Boolean {
        CXLogUtil.d(TAG, "checkValidBundleInAssets bundle(exists=${CXFileUtil.existsInAssets(bundlePathInAssets)}):$bundlePathInAssets")
        return (bundlePathInAssets?.isNotEmpty() == true && CXFileUtil.existsInAssets(bundlePathInAssets))
    }

    @JvmStatic
    fun checkValidBundleInSdcard(bundleFileInSdcard: File?): Boolean {
        CXLogUtil.d(TAG, "checkValidBundleInSdcard bundle(exists=${bundleFileInSdcard?.exists()}):${bundleFileInSdcard?.absolutePath}")
        return bundleFileInSdcard?.exists() == true
    }

    @JvmStatic
    fun checkValidRemoteDebugServer(): Boolean {
        val isCurrentLoadModeServer = isCurrentLoadModeServer()
        CXLogUtil.d(TAG, "checkValidRemoteDebugServer $isCurrentLoadModeServer")
        return isCurrentLoadModeServer
    }

    @JvmStatic
    fun isCurrentLoadModeServer(): Boolean = devSettingsManager.getDebugHttpHost().isNotEmpty()

    @JvmStatic
    fun devSupportEnabled(): Boolean = instanceManager?.devSupportManager?.devSupportEnabled == true

    @JvmStatic
    fun isRemoteJSDebugEnabled(): Boolean = instanceManager?.devSupportManager?.devSettings?.isRemoteJSDebugEnabled == true

    @JvmStatic
    private fun getInstanceBuilder(bundleLoader: JSBundleLoader, jsMainModulePath: String = "index", frescoConfig: ImagePipelineConfig?): ReactInstanceManagerBuilder {
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
                .setJSMainModulePath(jsMainModulePath)
                .setJSBundleLoader(bundleLoader)
                .addPackages(
                        mutableListOf<ReactPackage>(
                                MainReactPackage(MainPackageConfig.Builder().apply {
                                    frescoConfig?.let { setFrescoConfig(it) }
                                }.build()),
                                ReactCustomPackage()
                        )
                )
                .setUseDeveloperSupport(debug)
                .setInitialLifecycleState(LifecycleState.BEFORE_CREATE)
    }

}
