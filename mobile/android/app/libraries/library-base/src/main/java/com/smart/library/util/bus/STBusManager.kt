package com.smart.library.util.bus

import android.app.Application
import android.content.Context
import com.smart.library.util.STLogUtil

@Suppress("unused", "MemberVisibilityCanPrivate")
object STBusManager {
    private val TAG: String = STBusManager::class.java.simpleName

    interface IBusHandler {
        fun onInitOnce(application: Application)
        fun onUpgradeOnce(application: Application)
        fun onCall(context: Context, busName: String, vararg params: Any)
    }

    private val busHandlerMap: MutableMap<String, IBusHandler> = hashMapOf()
    private var init = false
    fun initOnce(application: Application, busHandlerClassMap: MutableMap<String, String>) {
        if (!init) {
            busHandlerClassMap.forEach {
                try {
                    val busClass = Class.forName(it.value)
                    if (IBusHandler::class.java.isAssignableFrom(busClass)) { // isAssignableFrom 检查 busClass 是否是 IBusHandler 的子类
                        val busHandler = busClass.newInstance() as IBusHandler
                        busHandlerMap[it.key] = busHandler
                        // init once here
                        busHandler.onInitOnce(application)
                        STLogUtil.i(TAG, "init bus ${it.key}:${it.value} success")
                    } else {
                        STLogUtil.w(TAG, "init bus ${it.key}:${it.value} failure, class is not IBusHandler")
                    }
                } catch (e: Exception) {
                    STLogUtil.w(TAG, "init bus ${it.key}:${it.value} error, class not found exception")
                }
            }
            init = true
        }
    }

    fun call(context: Context, busName: String, vararg params: Any) {
        val busHandler = busHandlerMap[busName]
        if (busHandler != null) {
            busHandler.onCall(context, busName, params)
        } else {
            STLogUtil.w(TAG, "can not find bus handler for busName")
        }
    }
}
