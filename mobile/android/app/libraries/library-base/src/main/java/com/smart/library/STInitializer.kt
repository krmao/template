package com.smart.library

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.os.Process
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDex
import com.smart.library.base.STActivityLifecycleCallbacks
import com.smart.library.base.STApplicationVisibleChangedEvent
import com.smart.library.util.*
import com.smart.library.util.bus.STBusManager
import com.smart.library.util.image.STIImageHandler
import com.smart.library.util.image.STImageManager
import com.smart.library.util.image.impl.STImageFrescoHandler
import com.smart.library.util.network.STNetworkChangedReceiver
import com.smart.library.util.okhttp.STOkHttpManager
import com.smart.library.util.rx.RxBus
import com.smart.library.widget.debug.STDebugFragment
import com.smart.library.widget.titlebar.STTitleBar
import org.json.JSONObject

@Suppress("unused", "SpellCheckingInspection", "MemberVisibilityCanBePrivate")
object STInitializer {
    private const val TAG = "[initializer]"

    private var initialized: Boolean = false
    private var options: Options? = null

    fun initOptions(options: Options?): STInitializer {
        this.options = options
        return this
    }

    @JvmStatic
    fun quitApplication() {
        STImageManager.clearMemoryCaches()
        activityLifecycleCallbacks()?.finishAllActivity()

        try {
            STThreadUtils.runOnUiThread(Runnable { Process.killProcess(Process.myPid()) }, 200)
            Process.killProcess(Process.myPid())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 跟随 apk 一起打包的 react native 离线包版本号, 0代表当前无可使用的离线包
     */
    fun rnBaseVersion(): Int = this.options?.rnBaseVersion() ?: 0
    fun rnBundlePathInAssets(): String = this.options?.rnBundlePathInAssets() ?: "bundle-rn.zip"
    fun rnCheckUpdateHTTPGetUrl(): String = this.options?.rnCheckUpdateHTTPGetUrl() ?: ""

    //region rn first screen callback
    private var onRNFirstScreenAttachedCallbacks: MutableList<(attached: Boolean) -> Unit> = mutableListOf()
    private fun notifyOnRNFirstScreenAttachedCallback() {
        val reactNativeInited: Boolean = STPreferencesUtil.getBoolean("react-native-inited", false) ?: false
        if (!reactNativeInited) return
        synchronized(onRNFirstScreenAttachedCallbacks) {
            onRNFirstScreenAttachedCallbacks.forEach { it(reactNativeInited) }
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

        val eventId: Any = this
        STEventManager.register(eventId, "react-native-inited") { eventKey: String, value: Any? ->
            STEventManager.unregisterAll(eventId)
            if ("react-native-inited" == eventKey) {
                STPreferencesUtil.putBoolean("react-native-inited", "renderSuccess" == value)
                notifyOnRNFirstScreenAttachedCallback()
            }
        }
    }
    //endregion

    private var isRNInitialized: Boolean = false
    fun isRNInitialized(): Boolean = this.isRNInitialized
    fun setRNInitialized(isRNInitialized: Boolean) {
        this.isRNInitialized = isRNInitialized
    }

    private var isRNInitializedSuccess: Boolean = false
    fun isRNInitializedSuccess(): Boolean = this.isRNInitializedSuccess
    fun setRNInitializedSuccess(isRNInitializedSuccess: Boolean) {
        this.isRNInitializedSuccess = isRNInitializedSuccess
    }

    //region rn init callbacks
    private var onRNInitializedCallbacks: MutableList<(success: Boolean) -> Unit> = mutableListOf()
    fun ensureRNInitialized(callback: (success: Boolean) -> Unit) {
        if (isRNInitialized()) {
            callback(isRNInitialized())
            return
        }
        if (!onRNInitializedCallbacks.contains(callback)) onRNInitializedCallbacks.add(callback)
        notifyOnRNInitializedCallback()
    }

    fun notifyOnRNInitializedCallback() {
        if (!isRNInitialized()) return
        synchronized(onRNInitializedCallbacks) {
            if (onRNInitializedCallbacks.isNotEmpty()) {
                onRNInitializedCallbacks.forEach { it(isRNInitializedSuccess()) }
                onRNInitializedCallbacks.clear()
            }
        }
    }
    //endregion

    private var isBusInitialized: Boolean = false
    fun isBusInitialized(): Boolean = this.isBusInitialized
    fun setBusInitialized(isBusInitialized: Boolean) {
        this.isBusInitialized = isBusInitialized
    }

    //region bus init callbacks
    private val onBusInitializedCallbacks: MutableList<() -> Unit> = mutableListOf()
    fun ensureBusInitialized(callback: () -> Unit) {
        if (isBusInitialized()) {
            callback()
            return
        }
        if (!onBusInitializedCallbacks.contains(callback)) onBusInitializedCallbacks.add(callback)
        notifyOnBusInitializedCallback()
    }

    fun notifyOnBusInitializedCallback() {
        if (!isBusInitialized()) return
        synchronized(onBusInitializedCallbacks) {
            if (onBusInitializedCallbacks.isNotEmpty()) {
                onBusInitializedCallbacks.forEach { it() }
                onBusInitializedCallbacks.clear()
            }
        }
    }
    //endregion

    @JvmStatic
    fun defaultSharedPreferencesName(): String = this.options?.defaultSharedPreferencesName() ?: "com.smart.shared_preferences"

    @JvmStatic
    fun defaultCacheWebDirName(): String = this.options?.defaultCacheWebDirName() ?: "cache_web"

    @JvmStatic
    fun defaultLogDirName(): String = this.options?.defaultLogDirName() ?: "log"

    @JvmStatic
    fun loginClass(): Class<out Activity>? = this.options?.loginClass()

    @JvmStatic
    fun mainClass(): Class<out Activity>? = this.options?.mainClass()

    @JvmStatic
    fun debug(): Boolean = this.options?.debug() ?: true

    @JvmStatic
    fun application(): Application? = this.application

    @JvmStatic
    fun channel(): String? = this.options?.channel()

    @JvmStatic
    fun bridgeHandler(): BridgeHandler? = this.options?.bridgeHandler()

    fun activityLifecycleCallbacks(): STActivityLifecycleCallbacks? = this.options?.activityLifecycleCallbacks()

    private var application: Application? = null

    @JvmStatic
    fun initOnApplicationCreate(application: Application): STInitializer {
        if (this.initialized) return this

        this.application = application
        this.initialized = true

        Thread.setDefaultUncaughtExceptionHandler { t, e -> STFileUtil.saveUncaughtException(t, e) }
        return this
    }

    /**
     * https://www.cnblogs.com/muouren/p/11741309.html
     * https://juejin.im/post/5d95f4a4f265da5b8f10714b#heading-10
     * http://androidxref.com/4.4.4_r1/xref/libcore/dalvik/src/main/java/dalvik/system/DexPathList.java
     * http://androidxref.com/4.4.4_r1/xref/dalvik/libdex/DexFile.cpp
     * https://github.com/lanshifu/MultiDexTest
     */
    fun attachApplicationBaseContext(base: Context?): STInitializer {
        val startTime = System.currentTimeMillis()
        MultiDex.install(base)
        println("MultiDex.install 耗时:${System.currentTimeMillis() - startTime}ms")
        return this
    }

    @JvmStatic
    fun openSchema(activity: Activity?, url: String?, callback: BridgeHandlerCallback? = null) {
        val bridgeParamsJsonObject = JSONObject()
        bridgeParamsJsonObject.put("url", url)
        bridgeHandler()?.handleBridge(activity, "open", bridgeParamsJsonObject.toString(), null, callback)
    }

    @JvmOverloads
    fun initImageManager(imageHandler: STIImageHandler = STImageFrescoHandler(STImageFrescoHandler.getConfigBuilder(debug(), STOkHttpManager.client).build())): STInitializer {
        STImageManager.initialize(imageHandler)
        return this
    }

    fun initBus(busHandlerClassMap: MutableMap<String, String>, onCallback: ((key: String, success: Boolean) -> Unit)? = null, onCompletely: (() -> Unit)? = null): STInitializer {
        STBusManager.initOnce(
            application(),
            busHandlerClassMap,
            onCallback = { key: String, success: Boolean ->
                STLogUtil.d(TAG, "-- init bus $key, $success")
                if (key == "reactnative") {
                    setRNInitialized(true)
                    setRNInitializedSuccess(success)
                    notifyOnRNInitializedCallback()
                }
                onCallback?.invoke(key, success)
            },
            onCompletely = {
                STLogUtil.w(TAG, "-- init bus onCompletely")
                setBusInitialized(true)
                notifyOnBusInitializedCallback()
                onCompletely?.invoke()
            })
        return this
    }

    class Options {
        private var debug: Boolean = false
        private var channel: String = ""

        private var defaultSharedPreferencesName: String = "com.smart.shared_preferences"
        private var defaultCacheWebDirName: String = "cache_web"
        private var defaultLogDirName: String = "log"
        private var mainClass: Class<out Activity>? = null
        private var loginClass: Class<out Activity>? = null

        private var enableNetworkChangedReceiver: Boolean = true
        private var networkChangedReceiver: STNetworkChangedReceiver? = null
        private var enableActivityLifecycleCallbacks: Boolean = true
        private var activityLifecycleCallbacks: STActivityLifecycleCallbacks? = null
        private var enableCompatVectorFromResources: Boolean = true
        private var bridgeHandler: BridgeHandler? = null

        private var rnBaseVersion: Int = 0
        private var rnBundlePathInAssets: String = "bundle-rn.zip"
        private var rnCheckUpdateHTTPGetUrl: String = ""

        fun rnBaseVersion(): Int = this.rnBaseVersion
        fun setRNBaseVersion(rnBaseVersion: Int = this.rnBaseVersion): Options {
            this.rnBaseVersion = rnBaseVersion
            return this
        }

        fun rnBundlePathInAssets(): String = this.rnBundlePathInAssets
        fun setRNBundlePathInAssets(rnBundlePathInAssets: String = this.rnBundlePathInAssets): Options {
            this.rnBundlePathInAssets = rnBundlePathInAssets
            return this
        }

        fun rnCheckUpdateHTTPGetUrl(): String = this.rnCheckUpdateHTTPGetUrl
        fun setRNCheckUpdateHTTPGetUrl(rnCheckUpdateHTTPGetUrl: String = this.rnCheckUpdateHTTPGetUrl): Options {
            this.rnCheckUpdateHTTPGetUrl = rnCheckUpdateHTTPGetUrl
            return this
        }

        fun setDefaultTitleBarBackgroundColor(@ColorInt color: Int = STTitleBar.DEFAULT_BACKGROUND_COLOR): Options {
            STTitleBar.DEFAULT_BACKGROUND_COLOR = color
            return this
        }

        fun setDefaultTitleBarTextColor(@ColorInt color: Int = STTitleBar.DEFAULT_TEXT_COLOR): Options {
            STTitleBar.DEFAULT_TEXT_COLOR = color
            return this
        }

        fun setDefaultTitleBarTextSize(textSizeDp: Float = STTitleBar.DEFAULT_TEXT_SIZE): Options {
            STTitleBar.DEFAULT_TEXT_SIZE = textSizeDp
            return this
        }

        @SuppressLint("CheckResult")
        @JvmOverloads
        fun setApiURL(devURL: String? = STURLManager.Environments.DEV.getURL(), sitURL: String? = STURLManager.Environments.SIT.getURL(), uatURL: String? = STURLManager.Environments.UAT.getURL(), prdURL: String? = STURLManager.Environments.PRD.getURL()): Options {
            STURLManager.Environments.DEV.setURL(devURL)
            STURLManager.Environments.SIT.setURL(sitURL)
            STURLManager.Environments.UAT.setURL(uatURL)
            STURLManager.Environments.PRD.setURL(prdURL)

            if (STInitializer.debug()) {
                STURLManager.Environments.values().forEach { environment: STURLManager.Environments ->
                    STDebugFragment.addHost(
                        environment.name, environment.map[STURLManager.KEY_HOST]
                            ?: "", STURLManager.curEnvironment == environment
                    )
                }
                RxBus.toObservable(STDebugFragment.HostChangeEvent::class.java)
                    .subscribe { changeEvent ->
                        STURLManager.curEnvironment =
                            STURLManager.Environments.valueOf(changeEvent.hostModel.label)
                        STToastUtil.show("检测到环境切换(${changeEvent.hostModel.label})\n已切换到:${STURLManager.curEnvironment.name}")
                    }

                STDebugFragment.showDebugNotification()
                RxBus.toObservable(STApplicationVisibleChangedEvent::class.java)
                    .subscribe { changeEvent ->
                        if (changeEvent.isApplicationVisible)
                            STDebugFragment.showDebugNotification()
                        else
                            STDebugFragment.cancelDebugNotification()
                    }
            }
            return this
        }

        fun loginClass(): Class<out Activity>? = this.loginClass

        fun setLoginClass(loginClass: Class<out Activity>?): Options {
            this.loginClass = loginClass
            return this
        }

        fun mainClass(): Class<out Activity>? = this.mainClass

        fun setMainClass(mainClass: Class<out Activity>?): Options {
            this.mainClass = mainClass
            return this
        }

        fun defaultLogDirName(): String = this.defaultLogDirName

        fun setDefaultLogDirName(name: String): Options {
            this.defaultLogDirName = name
            return this
        }


        fun defaultCacheWebDirName(): String = this.defaultCacheWebDirName

        fun setDefaultCacheWebDirName(name: String): Options {
            this.defaultCacheWebDirName = name
            return this
        }

        fun defaultSharedPreferencesName(): String = this.defaultSharedPreferencesName

        fun setDefaultSharedPreferencesName(name: String): Options {
            this.defaultSharedPreferencesName = name
            return this
        }


        fun isEnabledNetworkChangedReceiver(): Boolean = this.enableNetworkChangedReceiver

        fun enableNetworkChangedReceiver(enable: Boolean, networkChangedReceiver: BroadcastReceiver? = STNetworkChangedReceiver()): Options {
            if (enable) {
                if (networkChangedReceiver != null && networkChangedReceiver != this.networkChangedReceiver) {
                    this.networkChangedReceiver?.let { receiver ->
                        application()?.applicationContext?.let { context ->
                            STNetworkChangedReceiver.unregister(context, receiver)
                        }
                        this.networkChangedReceiver = null
                    }

                    application()?.applicationContext?.let { context ->
                        this.networkChangedReceiver = STNetworkChangedReceiver.register(context)
                        this.enableNetworkChangedReceiver = enable
                    }
                }
            } else {
                this.networkChangedReceiver?.let { receiver ->
                    application()?.applicationContext?.let { context ->
                        STNetworkChangedReceiver.unregister(context, receiver)
                    }
                    this.networkChangedReceiver = null
                }
                this.enableNetworkChangedReceiver = enable
            }
            return this
        }

        fun isEnabledActivityLifecycleCallbacks(): Boolean = this.enableActivityLifecycleCallbacks
        fun activityLifecycleCallbacks(): STActivityLifecycleCallbacks? = this.activityLifecycleCallbacks

        private fun enableActivityLifecycleCallbacks(enable: Boolean, activityLifecycleCallbacks: STActivityLifecycleCallbacks? = STActivityLifecycleCallbacks()): Options {
            if (enable) {
                if (activityLifecycleCallbacks != null && activityLifecycleCallbacks != this.activityLifecycleCallbacks) {
                    this.activityLifecycleCallbacks?.let {
                        application()?.unregisterActivityLifecycleCallbacks(it)
                        this.activityLifecycleCallbacks = null
                    }

                    application()?.registerActivityLifecycleCallbacks(activityLifecycleCallbacks)
                    this.activityLifecycleCallbacks = activityLifecycleCallbacks
                }
            } else {
                this.activityLifecycleCallbacks?.let {
                    application()?.unregisterActivityLifecycleCallbacks(it)
                    this.activityLifecycleCallbacks = null
                }
            }
            this.enableActivityLifecycleCallbacks = enable
            return this
        }

        fun isEnabledCompatVectorFromResources(): Boolean = this.enableCompatVectorFromResources

        fun enableCompatVectorFromResources(enable: Boolean): Options {
            this.enableCompatVectorFromResources = enable
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(enable) //selector vector support
            return this
        }

        fun debug(): Boolean = this.debug

        fun enableDebug(debug: Boolean): Options {
            this.debug = debug
            return this
        }

        fun channel(): String = this.channel

        fun setChannel(channel: String): Options {
            this.channel = channel
            return this
        }

        fun bridgeHandler(): BridgeHandler? = this.bridgeHandler

        fun setBridgeHandler(bridgeHandler: BridgeHandler?): Options {
            this.bridgeHandler = bridgeHandler
            return this
        }


        override fun toString(): String {
            return "Options(debug=$debug, application=$application, bridgeHandler=$bridgeHandler)"
        }

        init {
            enableActivityLifecycleCallbacks(true)
        }
    }

    interface BridgeHandler {
        fun handleBridge(activity: Activity?, functionName: String?, params: String?, callbackId: String?, callback: BridgeHandlerCallback?)
    }

    interface BridgeHandlerCallback {
        fun onCallback(callbackId: String?, resultJsonString: String?)
    }
}