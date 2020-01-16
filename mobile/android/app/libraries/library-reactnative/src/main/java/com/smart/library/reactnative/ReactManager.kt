package com.smart.library.reactnative

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.annotation.UiThread
import com.facebook.imagepipeline.core.ImagePipelineConfig
import com.facebook.react.ReactInstanceManager
import com.facebook.react.ReactInstanceManagerBuilder
import com.facebook.react.bridge.CatalystInstanceImpl
import com.facebook.react.bridge.JSBundleLoader
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactContext
import com.facebook.react.common.LifecycleState
import com.facebook.react.devsupport.DevLoadingViewController
import com.facebook.react.devsupport.RedBoxHandler
import com.facebook.react.devsupport.interfaces.StackFrame
import com.facebook.react.shell.MainPackageConfig
import com.facebook.react.shell.MainReactPackage
import com.facebook.react.uimanager.UIImplementationProvider
import com.oblador.vectoricons.VectorIconsPackage
import com.smart.library.base.STBaseApplication
import com.smart.library.deploy.STDeployManager
import com.smart.library.reactnative.dev.ReactDevSettingsManager
import com.smart.library.reactnative.packages.ReactCustomPackage
import com.smart.library.util.STFileUtil
import com.smart.library.util.STLogUtil
import com.smart.library.util.STReflectUtil
import com.smart.library.util.STToastUtil
import com.smart.library.util.cache.STCacheManager
import com.swmansion.gesturehandler.react.RNGestureHandlerPackage
import com.swmansion.reanimated.ReanimatedPackage
import com.th3rdwave.safeareacontext.SafeAreaContextPackage
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.io.File
import java.util.*

@Suppress("MemberVisibilityCanBePrivate", "unused", "DEPRECATION")
@SuppressLint("StaticFieldLeak")
object ReactManager {
    const val TAG: String = "[REACT_NATIVE]"

    const val KEY_RN_CALL_NATIVE_PARAMS_HASH_MAP: String = "KEY_RN_CALL_NATIVE_PARAMS_HASH_MAP"
    const val KEY_RN_CALL_NATIVE_RESULT_HASH_MAP: String = "KEY_RN_CALL_NATIVE_RESULT_HASH_MAP"
    const val KEY_NATIVE_CALL_RN_PARAMS: String = "params"

    @JvmStatic
    var application: Application = STBaseApplication.INSTANCE
        private set

    @JvmStatic
    var debug: Boolean = STBaseApplication.DEBUG
        private set

    var versionOfIndexBundleFileInSdcard: Int? = null
    var indexBundleFileInSdcard: File? = null
        set(value) {
            if (field != value) {
                STLogUtil.w(TAG, "indexBundleFileInSdcard has changed from ${field?.absolutePath} to ${value?.absolutePath}")
                field = value
            }
        }

    @JvmStatic
    var instanceManager: ReactInstanceManager? = null

    fun destroyInstanceManager() {
        if (instanceManager != null) {
            STLogUtil.w(TAG, "destroyInstanceManager start instanceManager=$instanceManager")
            val innerInstanceManager: ReactInstanceManager? = instanceManager
            if (innerInstanceManager != null) {
                (STReflectUtil.getFieldValue(innerInstanceManager, "mReactInstanceEventListeners") as? ArrayList<*>)?.clear()

                val currentReactContext: ReactContext? = innerInstanceManager.currentReactContext
                if (currentReactContext != null) {
                    STReflectUtil.invokeDeclaredMethod(innerInstanceManager, "tearDownReactContext",
                            arrayOf(ReactContext::class.javaObjectType),
                            arrayOf(currentReactContext)
                    )
                }

                val downloadedJSBundleFilePath: String? = innerInstanceManager.devSupportManager?.downloadedJSBundleFile
                STLogUtil.e(TAG, "will delete downloadedJSBundleFilePath=$downloadedJSBundleFilePath")
                STFileUtil.deleteFile(downloadedJSBundleFilePath)

                getCatalystInstance()?.destroy()
                innerInstanceManager.destroy()
            }

            instanceManager = null
            STLogUtil.w(TAG, "destroyInstanceManager instanceManager=$instanceManager")
        } else {
            STLogUtil.w(TAG, "destroyInstanceManager instanceManager is null, no need to destroy")
        }
    }

    fun initInstanceManager(callback: ((success: Boolean) -> Unit)? = null) {
        STLogUtil.e(TAG, "init instanceManager start instanceManager=$instanceManager")
        if (instanceManager == null) {
            val bundlePathInAssets = ReactConstant.PATH_ASSETS_BUNDLE
            val bundlePathInSdcard: String? = indexBundleFileInSdcard?.absolutePath

            /**
             * 优先使用远程调试
             * 如果 devSettingsManager.getDebugHttpHost().isNotEmpty()
             */
            if (checkValidRemoteDebugServer()) {
                ReactConstant.VERSION_RN_CURRENT = -1
                val bundleLoader: JSBundleLoader = JSBundleLoader.createCachedBundleFromNetworkLoader(devSettingsManager.getDebugHttpHost(), null)
                instanceManager = initInstanceManager(bundleLoader, callback)
            } else if (checkValidBundleInSdcard(indexBundleFileInSdcard) && bundlePathInSdcard != null && !bundlePathInSdcard.isNullOrBlank()) {
                /**
                 * 其次使用 sdcard bundle
                 */
                ReactConstant.VERSION_RN_CURRENT = versionOfIndexBundleFileInSdcard ?: 0
                STLogUtil.e(TAG, "checkValidBundleInSdcard=true")
                val bundleLoader = JSBundleLoader.createFileLoader(bundlePathInSdcard, bundlePathInSdcard, false)
                instanceManager = initInstanceManager(bundleLoader, callback)
            } else if (checkValidBundleInAssets(bundlePathInAssets)) {
                /**
                 * 再其次使用 assets bundle
                 */
                ReactConstant.VERSION_RN_CURRENT = ReactConstant.VERSION_RN_BASE
                STLogUtil.e(TAG, "checkValidBundleInAssets=true")
                val bundleLoader = JSBundleLoader.createAssetLoader(application, bundlePathInAssets, false)
                instanceManager = initInstanceManager(bundleLoader, callback)
            } else {
                ReactConstant.VERSION_RN_CURRENT = 0
                STLogUtil.e(TAG, "no valid bundleLoader for init instanceManager")
                callback?.invoke(false)
            }
            STLogUtil.e(TAG, "init instanceManager ${if (instanceManager == null) "failure" else "success"}, baseVersion=${ReactConstant.VERSION_RN_BASE}, currentVersion=${ReactConstant.VERSION_RN_CURRENT}, (注意:currentVersion==-1 代表正在使用在线调试, 无版本号)")
        } else {
            callback?.invoke(false)
        }
        STLogUtil.e(TAG, "init instanceManager end instanceManager=$instanceManager")
    }

    @UiThread
    @JvmStatic
    @Synchronized
    fun reloadBundleAndDisableRemoteDebug(callback: ((success: Boolean) -> Unit)? = null) {
        Flowable.fromCallable {
            if (devSettingsManager.setDebugHttpHost("")) {
                STToastUtil.show("保存配置成功(IP清空成功)")
                reloadBundle(callback = callback)
            } else {
                callback?.invoke(false)
                STToastUtil.show("保存配置失败(IP清空失败)")
            }
        }.subscribeOn(AndroidSchedulers.mainThread()).subscribe()
    }

    @UiThread
    @JvmStatic
    @JvmOverloads
    @Synchronized
    fun reloadBundle(indexBundleFileInSdcard: File? = ReactManager.indexBundleFileInSdcard, versionOfIndexBundleFileInSdcard: Int? = ReactManager.versionOfIndexBundleFileInSdcard, callback: ((success: Boolean) -> Unit)? = null) {
        if (!STDeployManager.REACT_NATIVE.isAllPagesClosed()) {
            STToastUtil.show("即将关闭所有的 RN 相关页面")
            STDeployManager.REACT_NATIVE.finishAllReactActivities()
        }

        Flowable.fromCallable {
            STLogUtil.w(TAG, "reloadBundleFromSdcard start instanceManager=$instanceManager, thread name = ${Thread.currentThread().name}")
            ReactManager.indexBundleFileInSdcard = indexBundleFileInSdcard
            ReactManager.versionOfIndexBundleFileInSdcard = versionOfIndexBundleFileInSdcard

            destroyInstanceManager()

            initInstanceManager(callback)

            STLogUtil.w(TAG, "reloadBundleFromSdcard end instanceManager=$instanceManager")
        }.subscribeOn(AndroidSchedulers.mainThread()).subscribe()
    }

    private fun initInstanceManager(bundleLoader: JSBundleLoader, callback: ((success: Boolean) -> Unit)? = null): ReactInstanceManager? {
        return getInstanceBuilder(bundleLoader, frescoConfig = frescoConfig).build()?.apply {
            addReactInstanceEventListener {
                isInitialized = true
                STLogUtil.w(TAG, "initialized react instance success")

                getCatalystInstance()?.let { loadBundleTasks.forEach { bundle -> bundle.loadScript(it) } }

                callback?.invoke(true)
            }
            STLogUtil.w(TAG, "createReactContextInBackground start...")
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
    val localHotPatchIndexFile by lazy { File(STCacheManager.getFilesHotPatchReactNativeDir(), indexName) }

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
    fun init(application: Application, debug: Boolean, indexBundleFileInSdcard: File? = null, versionOfIndexBundleFileInSdcard: Int? = null, frescoConfig: ImagePipelineConfig?, onCallNativeListener: ((activity: Activity?, functionName: String?, data: String?, promise: Promise?) -> Unit?)? = null, callback: ((success: Boolean) -> Unit)? = null) {
        STLogUtil.e(TAG, "instanceManager init start")
        ReactManager.application = application
        ReactManager.debug = debug
        ReactManager.indexBundleFileInSdcard = indexBundleFileInSdcard
        ReactManager.versionOfIndexBundleFileInSdcard = versionOfIndexBundleFileInSdcard
        ReactManager.frescoConfig = frescoConfig
        ReactManager.onCallNativeListener = onCallNativeListener

        // 禁止显示该 Dev Loading PopupWindow
        // for bug of android.view.WindowManager$BadTokenException
        // at com.facebook.react.devsupport.DevLoadingViewController.void showInternal()
        DevLoadingViewController.setDevLoadingEnabled(false)

        // sure to call instanceManager one time
        initInstanceManager(callback)
        if (instanceManager == null) {
            STLogUtil.e(TAG, "instanceManager is null, please check the bundle path or debug server is set")
        } else {
            STLogUtil.i(TAG, "instanceManager init success")
        }
        STLogUtil.e(TAG, "instanceManager init end")
    }

    @JvmStatic
    fun checkValidBundleInAssets(bundlePathInAssets: String?): Boolean {
        STLogUtil.d(TAG, "checkValidBundleInAssets bundle(exists=${STFileUtil.existsInAssets(bundlePathInAssets)}):$bundlePathInAssets")
        return (bundlePathInAssets?.isNotEmpty() == true && STFileUtil.existsInAssets(bundlePathInAssets))
    }

    @JvmStatic
    fun checkValidBundleInSdcard(bundleFileInSdcard: File?): Boolean {
        STLogUtil.d(TAG, "checkValidBundleInSdcard bundle(exists=${bundleFileInSdcard?.exists()}):${bundleFileInSdcard?.absolutePath}")
        return bundleFileInSdcard?.exists() == true
    }

    @JvmStatic
    fun checkValidRemoteDebugServer(): Boolean {
        val isCurrentLoadModeServer = isCurrentLoadModeServer()
        STLogUtil.d(TAG, "checkValidRemoteDebugServer $isCurrentLoadModeServer")
        return isCurrentLoadModeServer
    }

    @JvmStatic
    fun isCurrentLoadModeServer(): Boolean = devSettingsManager.getDebugHttpHost().isNotEmpty()

    @Deprecated("只有第一次进入 ReactActivity 的时候是 true, 后面再调用却变成了 false, 未知缘由", ReplaceWith(""))
    @JvmStatic
    private fun devSupportEnabled(): Boolean = instanceManager?.devSupportManager?.devSupportEnabled == true

    @JvmStatic
    private fun getInstanceBuilder(bundleLoader: JSBundleLoader, jsMainModulePath: String = "index", frescoConfig: ImagePipelineConfig?): ReactInstanceManagerBuilder {
        return ReactInstanceManager.builder()
                .setApplication(application)
                .setUIImplementationProvider(UIImplementationProvider())
                .setRedBoxHandler(object : RedBoxHandler {
                    override fun handleRedbox(title: String?, stack: Array<out StackFrame>?, errorType: RedBoxHandler.ErrorType?) {
                        STLogUtil.e(TAG, "handleRedbox errorType:${errorType?.name}, title:$title, stack:$stack")
                    }

                    override fun isReportEnabled(): Boolean {
                        return false
                    }

                    override fun reportRedbox(context: Context?, title: String?, stack: Array<out StackFrame>?, sourceUrl: String?, reportCompletedListener: RedBoxHandler.ReportCompletedListener?) {
                        STLogUtil.e(TAG, "reportRedbox title:$title, stack:$stack, sourceUrl:$sourceUrl")
                    }
                })
                .setJSMainModulePath(jsMainModulePath)
                .setJSBundleLoader(bundleLoader)
                .addPackages(
                        mutableListOf(
                                MainReactPackage(MainPackageConfig.Builder().apply {
                                    frescoConfig?.let { setFrescoConfig(it) }
                                }.build()),
                                ReactCustomPackage(),
                                RNGestureHandlerPackage(),
                                ReanimatedPackage(),
                                VectorIconsPackage(),
                                SafeAreaContextPackage()
                        )
                )
                .setUseDeveloperSupport(debug)
                .setInitialLifecycleState(LifecycleState.BEFORE_CREATE)
    }

}
