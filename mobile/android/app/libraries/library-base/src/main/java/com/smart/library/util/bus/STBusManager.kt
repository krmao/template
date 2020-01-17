package com.smart.library.util.bus

import android.app.Activity
import android.app.Application
import android.content.Context
import com.smart.library.util.STLogUtil
import java.lang.ref.WeakReference

@Suppress("unused", "MemberVisibilityCanPrivate")
object STBusManager {
    private val TAG: String = STBusManager::class.java.simpleName

    interface IBusHandler {
        fun onInitOnce(application: Application, callback: ((success: Boolean) -> Unit)?)
        fun onUpgradeOnce(application: Application)
        fun onCall(context: Context?, busFunctionName: String, vararg params: Any)
        fun onAsyncCall(callback: ((key: Any?, value: Any?) -> Unit)?, context: Context?, busFunctionName: String, vararg params: Any)
    }

    @JvmStatic
    var homeActivity: WeakReference<Activity>? = null

    private val busHandlerMap: MutableMap<String, IBusHandler> = hashMapOf()
    private var isInit = false

    fun initOnce(application: Application, busHandlerClassMap: MutableMap<String, String>, callback: ((key: String, success: Boolean) -> Unit)? = null) {
        if (!isInit) {
            busHandlerClassMap.forEach { entry ->
                val key: String = entry.key
                val value: String = entry.value

                try {
                    STLogUtil.v(TAG, "\nkey:$key, value:$value")
                    val busClass = Class.forName(value)
                    if (IBusHandler::class.javaObjectType.isAssignableFrom(busClass)) { // isAssignableFrom 检查 busClass 是否是 IBusHandler 的子类
                        val busHandler = busClass.newInstance() as IBusHandler
                        busHandlerMap[key] = busHandler
                        // init once here
                        busHandler.onInitOnce(application) { success: Boolean ->
                            callback?.invoke(key, success)
                        }
                        STLogUtil.i(TAG, "init bus $key:$value success\n")
                    } else {
                        STLogUtil.e(TAG, "init bus $key:$value failure, class is not IBusHandler\n")
                    }
                } catch (e: Exception) {
                    STLogUtil.e(TAG, "init bus $key:$value exception, ${e.message}\n")
                }
            }
            isInit = true
        }
    }

    fun call(context: Context?, busFunctionName: String, vararg params: Any) {
        val busHandler: IBusHandler? = busHandlerMap[busFunctionName.substringBefore('/')]
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
