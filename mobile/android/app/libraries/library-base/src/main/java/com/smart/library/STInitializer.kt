package com.smart.library

import android.app.Activity
import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDex
import com.smart.library.base.STActivityLifecycleCallbacks
import com.smart.library.base.STApplicationVisibleChangedEvent
import com.smart.library.util.STLogUtil
import com.smart.library.util.network.STNetworkChangedReceiver
import com.smart.library.util.rx.RxBus
import org.json.JSONObject

@Suppress("unused")
object STInitializer {
    private var initialized: Boolean = false
    private var options: Options? = null

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
    fun application(): Application? = this.options?.application()

    @JvmStatic
    fun channel(): String? = this.options?.channel()

    @JvmStatic
    fun bridgeHandler(): BridgeHandler? = this.options?.bridgeHandler()

    @JvmStatic
    var activityStartedCount: Int = 0
        internal set

    @JvmStatic
    var activityStoppedCount: Int = 0
        internal set

    @JvmStatic
    var isApplicationVisible: Boolean = false
        internal set(value) {
            if (field != value) {
                STLogUtil.e("applicationLifeCycle", "系统监测到应用程序 正在 从 ${if (field) "前台" else "后台"} 切换到 ${if (value) "前台" else "后台"} ")
                field = value
                RxBus.post(STApplicationVisibleChangedEvent(value))
            }
        }

    @JvmStatic
    fun initOnApplicationCreate(options: Options) {
        if (!this.initialized) {
            this.options = options
            this.initialized = true
        }
    }

    /**
     * https://www.cnblogs.com/muouren/p/11741309.html
     * https://juejin.im/post/5d95f4a4f265da5b8f10714b#heading-10
     * http://androidxref.com/4.4.4_r1/xref/libcore/dalvik/src/main/java/dalvik/system/DexPathList.java
     * http://androidxref.com/4.4.4_r1/xref/dalvik/libdex/DexFile.cpp
     * https://github.com/lanshifu/MultiDexTest
     */
    fun attachApplicationBaseContext(base: Context?) {
        val startTime = System.currentTimeMillis()
        MultiDex.install(base)
        println("MultiDex.install 耗时:${System.currentTimeMillis() - startTime}ms")
    }

    @JvmStatic
    fun openSchema(activity: Activity?, url: String?, callback: BridgeHandlerCallback? = null) {
        val bridgeParamsJsonObject = JSONObject()
        bridgeParamsJsonObject.put("url", url)
        bridgeHandler()?.handleBridge(activity, "open", bridgeParamsJsonObject.toString(), null, callback)
    }

    class Options(private val application: Application) {
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
        private var activityLifecycleCallbacks: Application.ActivityLifecycleCallbacks? = null
        private var enableCompatVectorFromResources: Boolean = true
        private var bridgeHandler: BridgeHandler? = null

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
                        application.let { context ->
                            STNetworkChangedReceiver.unregister(context, receiver)
                        }
                        this.networkChangedReceiver = null
                    }

                    application.let { context ->
                        this.networkChangedReceiver = STNetworkChangedReceiver.register(context)
                        this.enableNetworkChangedReceiver = enable
                    }
                }
            } else {
                this.networkChangedReceiver?.let { receiver ->
                    application.let { context ->
                        STNetworkChangedReceiver.unregister(context, receiver)
                    }
                    this.networkChangedReceiver = null
                }
                this.enableNetworkChangedReceiver = enable
            }
            return this
        }

        fun isEnabledActivityLifecycleCallbacks(): Boolean = this.enableActivityLifecycleCallbacks

        fun enableActivityLifecycleCallbacks(enable: Boolean, activityLifecycleCallbacks: Application.ActivityLifecycleCallbacks? = STActivityLifecycleCallbacks()): Options {
            if (enable) {
                if (activityLifecycleCallbacks != null && activityLifecycleCallbacks != this.activityLifecycleCallbacks) {
                    this.activityLifecycleCallbacks?.let {
                        application().unregisterActivityLifecycleCallbacks(it)
                        this.activityLifecycleCallbacks = null
                    }

                    application().registerActivityLifecycleCallbacks(activityLifecycleCallbacks)
                    this.activityLifecycleCallbacks = activityLifecycleCallbacks
                }
            } else {
                this.activityLifecycleCallbacks?.let {
                    application().unregisterActivityLifecycleCallbacks(it)
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

        fun application(): Application = this.application

        fun bridgeHandler(): BridgeHandler? = this.bridgeHandler

        fun setBridgeHandler(bridgeHandler: BridgeHandler?): Options {
            this.bridgeHandler = bridgeHandler
            return this
        }


        override fun toString(): String {
            return "Options(debug=$debug, application=$application, bridgeHandler=$bridgeHandler)"
        }
    }

    interface BridgeHandler {
        fun handleBridge(activity: Activity?, functionName: String?, params: String?, callbackId: String?, callback: BridgeHandlerCallback?)
    }

    interface BridgeHandlerCallback {
        fun onCallback(callbackId: String?, resultJsonString: String?)
    }
}