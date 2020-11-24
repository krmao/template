package com.smart.library.util.bus

import android.app.Activity
import android.app.Application
import android.content.Context
import com.smart.library.util.STLogUtil
import java.lang.ref.WeakReference
import java.util.concurrent.atomic.AtomicInteger

@Suppress("unused", "MemberVisibilityCanPrivate")
object STBusManager {
    private val TAG: String = "[bus]"

    interface IBusHandler {
        fun onInitOnce(application: Application?, callback: ((success: Boolean) -> Unit)?)
        fun onUpgradeOnce(application: Application?)
        fun onCall(context: Context?, busFunctionName: String, vararg params: Any)
        fun onAsyncCall(callback: ((key: Any?, value: Any?) -> Unit)?, context: Context?, busFunctionName: String, vararg params: Any)
    }

    @JvmStatic
    var homeActivity: WeakReference<Activity>? = null

    private val busHandlerMap: MutableMap<String, IBusHandler> = hashMapOf()
    private var isInit = false
    fun isInit(): Boolean = this.isInit

    private var initializedCount: AtomicInteger = AtomicInteger()

    fun initOnce(application: Application?, busHandlerClassMap: MutableMap<String, String>, onCallback: ((key: String, success: Boolean) -> Unit)? = null, onCompletely: (() -> Unit)? = null) {
        if (!isInit) {
            initializedCount.set(0)

            fun handleOnComplete() {
                if (initializedCount.get() == busHandlerClassMap.keys.size) {
                    onCompletely?.invoke()
                }
            }

            busHandlerClassMap.forEach { entry ->
                val key: String = entry.key
                val value: String = entry.value

                try {
                    if (STLogUtil.debug) STLogUtil.v(TAG, "init bus key:$key, value:$value start")
                    val busClass = Class.forName(value)
                    if (IBusHandler::class.javaObjectType.isAssignableFrom(busClass)) { // isAssignableFrom 检查 busClass 是否是 IBusHandler 的子类
                        val busHandler = busClass.newInstance() as IBusHandler
                        busHandlerMap[key] = busHandler
                        // init once here
                        busHandler.onInitOnce(application) { success: Boolean ->
                            onCallback?.invoke(key, success)

                            initializedCount.incrementAndGet()
                            if (STLogUtil.debug) STLogUtil.v(TAG, "---- init bus key:$key, value:$value callback initializedCount=${initializedCount.get()}, busHandlerClassMap.keys.size=${busHandlerClassMap.keys.size}")
                            handleOnComplete()
                        }
                        if (STLogUtil.debug) STLogUtil.v(TAG, "init bus end $key:$value\n")
                    } else {
                        throw java.lang.Exception("init bus end $key:$value failure, class is not IBusHandler")
                    }
                } catch (e: Exception) {
                    initializedCount.incrementAndGet()
                    STLogUtil.e(TAG, "---- init bus exception key:$key, value:$value callback initializedCount=${initializedCount.get()}, busHandlerClassMap.keys.size=${busHandlerClassMap.keys.size}", e)
                    handleOnComplete()
                }
            }
            isInit = true
        }
    }

    fun call(context: Context?, busFunctionName: String, vararg params: Any) {
        val busKeyName = busFunctionName.substringBefore('/')
        val busHandler: IBusHandler? = busHandlerMap[busKeyName]

        STLogUtil.w(TAG, "call busKeyName=$busKeyName, busHandlerMap=$busHandlerMap")

        if (busHandler != null) {
            busHandler.onCall(context, busFunctionName, *params)
        } else {
            STLogUtil.w(TAG, "can not find bus handler for busName")
        }
    }

    fun callAsync(callback: ((key: Any?, value: Any?) -> Unit)?, context: Context?, busFunctionName: String, vararg params: Any) {
        val busHandler: IBusHandler? = busHandlerMap[busFunctionName.substringBefore('/')]
        if (busHandler != null) {
            busHandler.onAsyncCall(callback, context, busFunctionName, *params)
        } else {
            STLogUtil.w(TAG, "can not find bus handler for busName")
        }
    }

    fun isBusHandlerExists(busName: String): Boolean = isInit && busHandlerMap[busName] != null
}
