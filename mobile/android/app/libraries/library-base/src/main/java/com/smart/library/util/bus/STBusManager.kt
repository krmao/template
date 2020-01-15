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
        fun onInitOnce(application: Application)
        fun onUpgradeOnce(application: Application)
        fun onCall(context: Context?, busFunctionName: String, vararg params: Any)
    }

    @JvmStatic
    var homeActivity: WeakReference<Activity>? = null

    private val busHandlerMap: MutableMap<String, IBusHandler> = hashMapOf()
    private var isInit = false

    fun initOnce(application: Application, busHandlerClassMap: MutableMap<String, String>) {
        if (!isInit) {
            busHandlerClassMap.forEach {
                try {
                    STLogUtil.v(TAG, "\nkey:${it.key}, value:${it.value}")
                    val busClass = Class.forName(it.value)
                    if (IBusHandler::class.javaObjectType.isAssignableFrom(busClass)) { // isAssignableFrom 检查 busClass 是否是 IBusHandler 的子类
                        val busHandler = busClass.newInstance() as IBusHandler
                        busHandlerMap[it.key] = busHandler
                        // init once here
                        busHandler.onInitOnce(application)
                        STLogUtil.i(TAG, "init bus ${it.key}:${it.value} success\n")
                    } else {
                        STLogUtil.e(TAG, "init bus ${it.key}:${it.value} failure, class is not IBusHandler\n")
                    }
                } catch (e: Exception) {
                    STLogUtil.e(TAG, "init bus ${it.key}:${it.value} error, class not found exception\n", e)
                }
            }
            isInit = true
        }
    }

    fun call(context: Context?, busFunctionName: String, vararg params: Any) {
        val busHandler = busHandlerMap[busFunctionName.substringBefore('/')]
        if (busHandler != null) {
            busHandler.onCall(context, busFunctionName, *params)
        } else {
            STLogUtil.w(TAG, "can not find bus handler for busName")
        }
    }

    fun isBusHandlerExists(busName: String): Boolean = isInit && busHandlerMap[busName] != null
}
