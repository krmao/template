package com.smart.template.module.rn

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.support.annotation.UiThread
import com.facebook.imagepipeline.core.ImagePipelineConfig
import com.facebook.react.ReactInstanceManager
import com.facebook.react.ReactInstanceManagerBuilder
import com.facebook.react.ReactPackage
import com.facebook.react.bridge.CatalystInstanceImpl
import com.facebook.react.bridge.JSBundleLoader
import com.facebook.react.bridge.Promise
import com.facebook.react.common.LifecycleState
import com.facebook.react.devsupport.DevLoadingViewController
import com.facebook.react.devsupport.RedBoxHandler
import com.facebook.react.devsupport.interfaces.StackFrame
import com.facebook.react.shell.MainPackageConfig
import com.facebook.react.shell.MainReactPackage
import com.facebook.react.uimanager.UIImplementationProvider
import com.smart.library.base.STBaseApplication
import com.smart.library.deploy.STDeployManager
import com.smart.library.util.STFileUtil
import com.smart.library.util.STLogUtil
import com.smart.library.util.STToastUtil
import com.smart.library.util.cache.STCacheManager
import com.smart.template.module.rn.dev.ReactDevSettingsManager
import com.smart.template.module.rn.packages.ReactCustomPackage
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.io.File
import java.util.*

@Suppress("MemberVisibilityCanBePrivate", "unused")
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
            STLogUtil.w(TAG, "indexBundleFileInSdcard has changed from ${field?.absolutePath} to ${value?.absolutePath}")
            field = value
        }

    @JvmStatic
    var instanceManager: ReactInstanceManager? = null
        private set
        get() {
            if (field == null) {
                val bundlePathInAssets = ReactConstant.PATH_ASSETS_BUNDLE
                val bundlePathInSdcard: String? = indexBundleFileInSdcard?.absolutePath

                // 率先检查 sdCard, 然后再检查 assets, 注意先后顺序
                if (checkValidBundleInSdcard(indexBundleFileInSdcard) && bundlePathInSdcard != null && !bundlePathInSdcard.isNullOrBlank()) {
                    ReactConstant.VERSION_RN_CURRENT = versionOfIndexBundleFileInSdcard ?: 0
                    STLogUtil.e(TAG, "checkValidBundleInSdcard=true")
                    val bundleLoader = JSBundleLoader.createFileLoader(bundlePathInSdcard, bundlePathInSdcard, false)
                    field = initInstanceManager(bundleLoader)
                } else if (checkValidBundleInAssets(bundlePathInAssets)) {
                    ReactConstant.VERSION_RN_CURRENT = ReactConstant.VERSION_RN_BASE
                    STLogUtil.e(TAG, "checkValidBundleInAssets=true")
                    val bundleLoader = JSBundleLoader.createAssetLoader(application, bundlePathInAssets, false)
                    field = initInstanceManager(bundleLoader)
                } else if (checkValidRemoteDebugServer()) {
                    ReactConstant.VERSION_RN_CURRENT = -1
                    STLogUtil.e(TAG, "checkValidRemoteDebugServer=true")
                    val bundleLoader = JSBundleLoader.createCachedBundleFromNetworkLoader(devSettingsManager.getDebugHttpHost(), null)
                    field = initInstanceManager(bundleLoader)
                } else {
                    ReactConstant.VERSION_RN_CURRENT = 0
                    STLogUtil.e(TAG, "no valid bundleLoader for init instanceManager")
                }
                STLogUtil.e(TAG, "init instanceManager ${if (field == null) "failure" else "success"}, baseVersion=${ReactConstant.VERSION_RN_BASE}, currentVersion=${ReactConstant.VERSION_RN_CURRENT}, (注意:currentVersion==-1 代表正在使用在线调试, 无版本号)")
            }
            return field
        }

    @UiThread
    @JvmStatic
    @Synchronized
    fun reloadBundleFromOnlineToOffline() {
        if (!STDeployManager.REACT_NATIVE.isAllPagesClosed()) {
            STToastUtil.show("请先关闭所有的 RN 相关页面")
            return
        }
        Flowable.fromCallable {
            if (devSettingsManager.setDebugHttpHost("")) {
                STToastUtil.show("保存配置成功(IP清空成功)")
                val downloadedJSBundleFilePath = instanceManager?.devSupportManager?.downloadedJSBundleFile
                STLogUtil.e(TAG, "will delete downloadedJSBundleFilePath=$downloadedJSBundleFilePath")
                STFileUtil.deleteFile(downloadedJSBundleFilePath)
                STToastUtil.show("reloadBundleFromSdcard now\ncurrentHost:${devSettingsManager.getDebugHttpHost()}")
                reloadBundleFromSdcard()
            } else {
                STToastUtil.show("保存配置失败(IP清空失败)")
            }
        }.subscribeOn(AndroidSchedulers.mainThread()).subscribe()
    }

    @UiThread
    @JvmStatic
    @JvmOverloads
    @Synchronized
    fun reloadBundleFromSdcard(indexBundleFileInSdcard: File? = this.indexBundleFileInSdcard, versionOfIndexBundleFileInSdcard: Int? = this.versionOfIndexBundleFileInSdcard) {
        Flowable.fromCallable {
            STLogUtil.w(TAG, "reloadBundleFromSdcard start, thread name = ${Thread.currentThread().name}")
            this.indexBundleFileInSdcard = indexBundleFileInSdcard
            this.versionOfIndexBundleFileInSdcard = versionOfIndexBundleFileInSdcard
            STLogUtil.w(TAG, "reloadBundleFromSdcard destroy old instanceManager")
            instanceManager?.destroy()
            STLogUtil.w(TAG, "reloadBundleFromSdcard set old instanceManager null")
            instanceManager = null

            // sure to call instanceManager one time
            if (instanceManager == null) {
                STLogUtil.e(TAG, "instanceManager is null, please check the bundle path or debug server is set")
            } else {
                STLogUtil.i(TAG, "instanceManager reCreate success")
            }
        }.subscribeOn(AndroidSchedulers.mainThread()).subscribe()
    }

    private fun initInstanceManager(bundleLoader: JSBundleLoader): ReactInstanceManager? {
        return getInstanceBuilder(bundleLoader, frescoConfig = frescoConfig).build()?.apply {
            addReactInstanceEventListener {
                isInitialized = true
                STLogUtil.w(TAG, "initialized react instance success")

                getCatalystInstance()?.let { loadBundleTasks.forEach { bundle -> bundle.loadScript(it) } }
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
    fun init(application: Application, debug: Boolean, indexBundleFileInSdcard: File? = null, versionOfIndexBundleFileInSdcard: Int? = null, frescoConfig: ImagePipelineConfig?, onCallNativeListener: ((activity: Activity?, functionName: String?, data: String?, promise: Promise?) -> Unit?)? = null) {
        this.application = application
        this.debug = debug
        this.indexBundleFileInSdcard = indexBundleFileInSdcard
        this.versionOfIndexBundleFileInSdcard = versionOfIndexBundleFileInSdcard
        this.frescoConfig = frescoConfig
        this.onCallNativeListener = onCallNativeListener

        // 禁止显示该 Dev Loading PopupWindow
        // for bug of android.view.WindowManager$BadTokenException
        // at com.facebook.react.devsupport.DevLoadingViewController.void showInternal()
        DevLoadingViewController.setDevLoadingEnabled(false)

        // sure to call instanceManager one time
        if (instanceManager == null) {
            STLogUtil.e(TAG, "instanceManager is null, please check the bundle path or debug server is set")
        } else {
            STLogUtil.i(TAG, "instanceManager init success")
        }
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
