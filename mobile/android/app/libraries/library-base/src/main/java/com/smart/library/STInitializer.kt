package com.smart.library

import android.app.Activity
import android.app.Application
import android.content.Context
import android.graphics.Color
import android.os.Process
import android.util.Log
import androidx.annotation.ColorInt
import androidx.annotation.Keep
import androidx.annotation.LayoutRes
import androidx.multidex.MultiDex
import com.smart.library.base.STActivityLifecycleCallbacks
import com.smart.library.base.STApplicationVisibleChangedEvent
import com.smart.library.util.*
import com.smart.library.util.bus.STBusManager
import com.smart.library.util.image.STIImageHandler
import com.smart.library.util.image.STImageManager
import com.smart.library.util.image.impl.STImageFrescoHandler
import com.smart.library.util.okhttp.STOkHttpManager
import com.smart.library.util.rx.RxBus
import com.smart.library.widget.debug.STDebugFragment
import com.smart.library.widget.titlebar.STTitleBar
import org.json.JSONObject

@Keep
@Suppress("unused", "SpellCheckingInspection", "MemberVisibilityCanBePrivate")
object STInitializer {

    data class State(var isRNInitialized: Boolean = false, var isRNInitializedSuccess: Boolean = false, var isBusInitialized: Boolean = false) {
        //region rn first screen callback
        fun isRNFirstScreenAttached(): Boolean = STPreferencesUtil.getBoolean("react-native-inited", false) ?: false

        private var onRNFirstScreenAttachedCallbacks: MutableList<(attached: Boolean) -> Unit> = mutableListOf()
        private fun notifyOnRNFirstScreenAttachedCallback() {
            val isRNFirstScreenAttached: Boolean = isRNFirstScreenAttached()
            if (!isRNFirstScreenAttached) return

            synchronized(onRNFirstScreenAttachedCallbacks) {
                onRNFirstScreenAttachedCallbacks.forEach { it(isRNFirstScreenAttached) }
                onRNFirstScreenAttachedCallbacks.clear()
            }
        }

        /**
         * 程序运行黑屏或白屏的问题 https://www.jianshu.com/p/23f4bbb372c8
         * 监听 react native 首屏渲染事件, 此处可以关闭引导页
         */
        fun ensureRNFirstScreenAttached(callback: (attached: Boolean) -> Unit) {
            if (STPreferencesUtil.getBoolean("react-native-inited", false) == true) {
                callback(true)
                return
            }

            if (!onRNFirstScreenAttachedCallbacks.contains(callback)) onRNFirstScreenAttachedCallbacks.add(callback)
            notifyOnRNFirstScreenAttachedCallback()

            val eventId = "$this-${this.hashCode()}"
            STEventManager.register(eventId, "react-native-inited") { eventKey: String, value: Any? ->
                STEventManager.unregisterAll(eventId)
                if ("react-native-inited" == eventKey) {
                    STPreferencesUtil.putBoolean("react-native-inited", "renderSuccess" == value)
                    notifyOnRNFirstScreenAttachedCallback()
                }
            }
        }
        //endregion

        //region rn init callbacks
        private var onRNInitializedCallbacks: MutableList<(success: Boolean) -> Unit> = mutableListOf()
        fun ensureRNInitialized(callback: (success: Boolean) -> Unit) {
            if (isRNInitialized) {
                callback(isRNInitialized)
                return
            }
            if (!onRNInitializedCallbacks.contains(callback)) onRNInitializedCallbacks.add(callback)
            notifyOnRNInitializedCallback()
        }

        fun notifyOnRNInitializedCallback() {
            if (!isRNInitialized) return
            synchronized(onRNInitializedCallbacks) {
                if (onRNInitializedCallbacks.isNotEmpty()) {
                    onRNInitializedCallbacks.forEach { it(isRNInitializedSuccess) }
                    onRNInitializedCallbacks.clear()
                }
            }
        }
        //endregion

        //region bus init callbacks
        private val onBusInitializedCallbacks: MutableList<() -> Unit> = mutableListOf()
        fun ensureBusInitialized(callback: () -> Unit) {
            if (isBusInitialized) {
                callback()
                return
            }
            if (!onBusInitializedCallbacks.contains(callback)) onBusInitializedCallbacks.add(callback)
            notifyOnBusInitializedCallback()
        }

        fun notifyOnBusInitializedCallback() {
            if (!isBusInitialized) return
            synchronized(onBusInitializedCallbacks) {
                if (onBusInitializedCallbacks.isNotEmpty()) {
                    onBusInitializedCallbacks.forEach { it() }
                    onBusInitializedCallbacks.clear()
                }
            }
        }
        //endregion
    }

    data class Config(
        var application: Application,
        var appDebug: Boolean = false,
        var configAppInfo: ConfigAppInfo = ConfigAppInfo(),
        var configChannel: ConfigChannel = ConfigChannel(),
        var configName: ConfigName = ConfigName(),
        var configClass: ConfigClass = ConfigClass(),
        var configBridge: ConfigBridge = ConfigBridge(),
        var configRN: ConfigRN = ConfigRN(),
        var configTitleBar: ConfigTitleBar = ConfigTitleBar(),
        var configLoading: ConfigLoading = ConfigLoading(),
        var configNetwork: ConfigNetwork = ConfigNetwork(),
        var configLifecycle: ConfigLifecycle = ConfigLifecycle(),
        var configBundle: ConfigBundle = ConfigBundle(),
        var configImage: ConfigImage = ConfigImage(),
        var configAdapterDesign: ConfigAdapterDesign = ConfigAdapterDesign(),
    )

    data class ConfigAdapterDesign(
        val enableAdapterDesign: Boolean = false,
        val adapterDesignWidth: Int = -1,
        val adapterDesignHeight: Int = -1
    )

    data class ConfigImage(
        var imageHandler: STIImageHandler? = null
    )

    data class ConfigBundle(
        var bundleBusHandlerClassMap: MutableMap<String, String> = hashMapOf()
    )

    data class ConfigAppInfo(
        var appIcon: Int = -1
    )

    data class ConfigChannel(private var appChannelInvoker: () -> String = { "" }) {
        val appChannel: String by lazy { appChannelInvoker.invoke() }
    }

    data class ConfigName(
        var appSPName: String = "com.codesdancing.shared_preferences",
        var appWebCacheDirName: String = "cache_web",
        var appLogDirName: String = "log"
    )

    data class ConfigClass(
        var homeClass: Class<out Activity>? = null,
        var loginClass: Class<out Activity>? = null
    )

    data class ConfigBridge(
        var bridgeHandler: BridgeHandler? = null
    )

    data class ConfigRN(
        var baseVersion: Int = 0,
        var bundlePathInAssets: String = "bundle-rn.zip",
        var checkUpdateHTTPGetUrl: String = ""
    )

    data class ConfigTitleBar(
        @ColorInt var backgroundColor: Int = STTitleBar.DEFAULT_BACKGROUND_COLOR,
        @ColorInt var textColor: Int = STTitleBar.DEFAULT_TEXT_COLOR,
        var textSize: Float = STTitleBar.DEFAULT_TEXT_SIZE
    )

    data class ConfigLoading(
        @LayoutRes var layoutLoadingID: Int = R.layout.st_widget_frameloading_loading,
        @LayoutRes var layoutNoDataID: Int = R.layout.st_widget_frameloading_nodata,
        @LayoutRes var layoutFailureID: Int = R.layout.st_widget_frameloading_failure,
        var descriptionLoading: String? = null,
        var descriptionNoData: String? = null,
        var descriptionFailure: String? = null,
        @ColorInt var allBackgroundColor: Int = Color.parseColor("#FFFFFFFE"),
    )

    data class ConfigNetwork(
        val environments: ConfigNetworkEnvironments = ConfigNetworkEnvironments()
    )

    data class ConfigNetworkEnvironments(
        var devURL: String? = null,
        var prdURL: String? = null,
        var sitURL: String? = null,
        var uatURL: String? = null
    )

    data class ConfigLifecycle(
        var activityLifecycleCallbacks: STActivityLifecycleCallbacks = STActivityLifecycleCallbacks()
    )

    interface BridgeHandler {
        fun handleBridge(activity: Activity?, functionName: String?, params: String?, callbackId: String?, callback: BridgeHandlerCallback?)
    }

    interface BridgeHandlerCallback {
        fun onCallback(callbackId: String?, resultJsonString: String?)
    }

    private const val TAG = "[initializer]"

    @JvmStatic
    var config: Config? = null
        private set

    @JvmStatic
    val configAppInfo: ConfigAppInfo?
        get() = config?.configAppInfo

    @JvmStatic
    val configAdapterDesign: ConfigAdapterDesign?
        get() = config?.configAdapterDesign

    @JvmStatic
    val configChannel: ConfigChannel?
        get() = config?.configChannel

    @JvmStatic
    val configName: ConfigName?
        get() = config?.configName

    @JvmStatic
    val configClass: ConfigClass?
        get() = config?.configClass

    @JvmStatic
    val configBridge: ConfigBridge?
        get() = config?.configBridge

    @JvmStatic
    val configRN: ConfigRN?
        get() = config?.configRN

    @JvmStatic
    val configTitleBar: ConfigTitleBar?
        get() = config?.configTitleBar

    @JvmStatic
    val configLoading: ConfigLoading?
        get() = config?.configLoading

    @JvmStatic
    val configNetwork: ConfigNetwork?
        get() = config?.configNetwork

    @JvmStatic
    val configLifecycle: ConfigLifecycle?
        get() = config?.configLifecycle

    @JvmStatic
    val configBundle: ConfigBundle?
        get() = config?.configBundle

    @JvmStatic
    val configImage: ConfigImage?
        get() = config?.configImage

    @JvmStatic
    var initialized: Boolean = false
        private set

    @JvmStatic
    val state: State = State()

    @JvmStatic
    fun debug(): Boolean = this.config?.appDebug ?: false

    @JvmStatic
    fun currentActivity(): Activity? = this.configLifecycle?.activityLifecycleCallbacks?.currentActivity()

    @JvmStatic
    fun application(): Application? = this.config?.application

    @JvmStatic
    fun setApplicationProperties(config: Config): STInitializer {
        if (this.config != null) return this

        this.config = config
        return this
    }

    /**
     * https://www.cnblogs.com/muouren/p/11741309.html
     * https://juejin.im/post/5d95f4a4f265da5b8f10714b#heading-10
     * http://androidxref.com/4.4.4_r1/xref/libcore/dalvik/src/main/java/dalvik/system/DexPathList.java
     * http://androidxref.com/4.4.4_r1/xref/dalvik/libdex/DexFile.cpp
     * https://github.com/lanshifu/MultiDexTest
     */
    @JvmStatic
    fun attachApplicationBaseContext(base: Context?, enableCrashManager: Boolean = true): STInitializer {
        val startTime = System.currentTimeMillis()
        MultiDex.install(base)
        if (enableCrashManager) {
            STCrashManager.attachBaseContext(base)
        }
        println("MultiDex.install 耗时:${System.currentTimeMillis() - startTime}ms")
        return this
    }

    @JvmStatic
    fun openSchema(activity: Activity?, url: String?, callback: BridgeHandlerCallback? = null) {
        val bridgeParamsJsonObject = JSONObject()
        bridgeParamsJsonObject.put("url", url)
        config?.configBridge?.bridgeHandler?.handleBridge(activity, "open", bridgeParamsJsonObject.toString(), null, callback)
    }

    @JvmStatic
    fun initialApplication(config: Config? = this.config): STInitializer {
        if (this.initialized) return this

        if (this.config == null) this.config = config

        Log.w(TAG, "appDebug=${debug()}")

        STLogUtil.debug = debug()

        //region register activityLifecycleCallbacks
        val application: Application? = config?.application
        val activityLifecycleCallbacks = config?.configLifecycle?.activityLifecycleCallbacks
        if (activityLifecycleCallbacks != null) {
            application?.registerActivityLifecycleCallbacks(activityLifecycleCallbacks)
        }
        //endregion

        //region environments and debug host
        val environments = config?.configNetwork?.environments
        if (environments != null) {
            STURLManager.Environments.DEV.setURL(environments.devURL)
            STURLManager.Environments.SIT.setURL(environments.sitURL)
            STURLManager.Environments.UAT.setURL(environments.uatURL)
            STURLManager.Environments.PRD.setURL(environments.prdURL)
        }
        //endregion

        //region defaultUncaughtExceptionHandler
        // Thread.setDefaultUncaughtExceptionHandler { t, e -> STFileUtil.saveUncaughtException(t, e) }
        //endregion

        //region init rn and flutter immediately
        val bundleBusHandlerClassMap = config?.configBundle?.bundleBusHandlerClassMap
        if (bundleBusHandlerClassMap != null) {
            STBusManager.initOnce(
                config.application,
                bundleBusHandlerClassMap,
                onCallback = { key: String, success: Boolean ->
                    STLogUtil.d(TAG, "-- init bus $key, $success")
                    if (key == "reactnative") {
                        state.isRNInitialized = true
                        state.isRNInitializedSuccess = success
                        state.notifyOnRNInitializedCallback()
                    }
                },
                onCompletely = {
                    STLogUtil.w(TAG, "-- init bus onCompletely")
                    state.isBusInitialized = true
                    state.notifyOnBusInitializedCallback()
                }
            )
        }
        //endregion

        // 重置屏幕适配
        STAdaptScreenUtils.getPreLoadRunnable().run()

        //region bundle and bus
        STThreadUtils.runOnUiThread(object : Runnable {
            override fun run() {
                //region image
                val imageHandler = config?.configImage?.imageHandler ?: STImageFrescoHandler(STImageFrescoHandler.getConfigBuilder(application, debug(), STOkHttpManager.defaultClient).build())
                STImageManager.initialize(application, imageHandler)
                //endregion

                if (debug()) {
                    STURLManager.Environments.values().forEach { environment: STURLManager.Environments ->
                        STDebugFragment.addHost(environment.name, environment.map[STURLManager.KEY_HOST] ?: "", STURLManager.curEnvironment == environment)
                    }
                    @Suppress("UNUSED_VARIABLE")
                    val ignoreResult = RxBus.toObservable(STDebugFragment.HostChangeEvent::class.java).subscribe { changeEvent ->
                        STURLManager.curEnvironment = STURLManager.Environments.valueOf(changeEvent.hostModel.label)
                        STToastUtil.show("检测到环境切换(${changeEvent.hostModel.label})\n已切换到:${STURLManager.curEnvironment.name}")
                    }

                    STDebugFragment.showDebugNotification()
                    @Suppress("UNUSED_VARIABLE")
                    val ignoreResultV2 = RxBus.toObservable(STApplicationVisibleChangedEvent::class.java).subscribe { _ ->
                        // if (changeEvent.isApplicationVisible) STDebugFragment.showDebugNotification() else STDebugFragment.cancelDebugNotification()
                    }
                }
            }
        }, 1200)
        //endregion

        return this
    }

    @JvmStatic
    fun quitApplication() {
        STImageManager.clearMemoryCaches()

        //region unregister activityLifecycleCallbacks
        val application: Application? = config?.application
        val activityLifecycleCallbacks = config?.configLifecycle?.activityLifecycleCallbacks
        if (activityLifecycleCallbacks != null) {
            application?.unregisterActivityLifecycleCallbacks(activityLifecycleCallbacks)
            activityLifecycleCallbacks.finishAllActivity()
        }
        //endregion

        try {
            @Suppress("RedundantSamConstructor")
            STThreadUtils.runOnUiThread(Runnable { Process.killProcess(Process.myPid()) }, 200)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
