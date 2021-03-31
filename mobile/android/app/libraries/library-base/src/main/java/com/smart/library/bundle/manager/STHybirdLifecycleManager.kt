package com.smart.library.bundle.manager

import android.util.Log
import android.webkit.WebViewClient
import androidx.annotation.Keep
import com.smart.library.bundle.STHybird
import com.smart.library.bundle.util.STHybirdUtil
import com.smart.library.util.STJsonUtil
import com.smart.library.util.STLogUtil

@Keep
object STHybirdLifecycleManager {
    private val lifecycleMap: MutableMap<String, MutableSet<Int>> = mutableMapOf()

    @Synchronized
    fun isModuleOpened(moduleName: String?): Boolean {
        STLogUtil.e(STHybird.TAG, ">>>>>>>>>><<<<<<<<<< 已经加载${moduleName}的浏览器数量=${lifecycleMap[moduleName]?.size ?: 0}")
        return lifecycleMap[moduleName]?.isNotEmpty() == true
    }

    fun onWebViewOpenPage(webViewClient: WebViewClient?, url: String?) {
        STLogUtil.e(STHybird.TAG, ">>>>>>>>>><<<<<<<<<< 检测到浏览器正在加载页面, lifecycleMap->")

        STHybird.modules.forEach {
            if (STHybird.isMemberOfModule(it.value.currentConfig, url)) {
                STLogUtil.w(STHybird.TAG, ">>>>>>>>>><<<<<<<<<< 系统监测到当前 url 属于模块 ${it.key}  url=$url")
                if (webViewClient != null) {

                    val webViewHashCodeSet: MutableSet<Int> = lifecycleMap[it.key] ?: mutableSetOf()
                    webViewHashCodeSet.add(webViewClient.hashCode())
                    lifecycleMap[it.key] = webViewHashCodeSet
                }
            }
        }

        STLogUtil.j(Log.ERROR, STHybird.TAG, STJsonUtil.toJson(lifecycleMap))
    }

    fun onWebViewClose(webViewClient: WebViewClient?) {
        STLogUtil.e(STHybird.TAG, ">>>>>>>>>><<<<<<<<<< 检测到浏览器正在关闭, lifecycleMap->")

        STHybird.modules.forEach {
            if (webViewClient != null) {
                val webViewHashCodeSet: MutableSet<Int> = lifecycleMap[it.key] ?: mutableSetOf()
                webViewHashCodeSet.remove(webViewClient.hashCode())
                lifecycleMap[it.key] = webViewHashCodeSet

                if (webViewHashCodeSet.isEmpty()) {
                    STLogUtil.e(STHybird.TAG, ">>>>>>>>>><<<<<<<<<< 监测到模块:${it.key} 已经完全从浏览器中解耦, 强制设置 onlineModel = false, 并检查是否有下一次生效的任务, 此时是设置的最佳时机")
                    it.value.onlineModel = false
                    STHybirdUtil.fitNextAndFitLocalIfNeedConfigsInfo(it.key)
                }
            }
        }
        STLogUtil.j(Log.ERROR, STHybird.TAG, STJsonUtil.toJson(lifecycleMap))
    }
}
