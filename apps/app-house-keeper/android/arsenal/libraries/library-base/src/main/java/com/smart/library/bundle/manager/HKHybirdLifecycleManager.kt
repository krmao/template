package com.smart.library.bundle.manager

import android.util.Log
import android.webkit.WebViewClient
import com.smart.library.bundle.HKHybird
import com.smart.library.bundle.util.HKHybirdUtil
import com.smart.library.util.HKJsonUtil
import com.smart.library.util.HKLogUtil

object HKHybirdLifecycleManager {
    private val lifecycleMap: MutableMap<String, MutableSet<Int>> = mutableMapOf()

    @Synchronized
    fun isModuleOpened(moduleName: String?): Boolean {
        HKLogUtil.e(HKHybird.TAG, ">>>>>>>>>><<<<<<<<<< 已经加载${moduleName}的浏览器数量=${lifecycleMap[moduleName]?.size ?: 0}")
        return lifecycleMap[moduleName]?.isNotEmpty() == true
    }

    fun onWebViewOpenPage(webViewClient: WebViewClient?, url: String?) {
        HKLogUtil.e(HKHybird.TAG, ">>>>>>>>>><<<<<<<<<< 检测到浏览器正在加载页面, lifecycleMap->")

        HKHybird.modules.forEach {
            if (HKHybird.isMemberOfModule(it.value.currentConfig, url)) {
                HKLogUtil.w(HKHybird.TAG, ">>>>>>>>>><<<<<<<<<< 系统监测到当前 url 属于模块 ${it.key}  url=$url")
                if (webViewClient != null) {

                    val webViewHashCodeSet: MutableSet<Int> = lifecycleMap[it.key] ?: mutableSetOf()
                    webViewHashCodeSet.add(webViewClient.hashCode())
                    lifecycleMap[it.key] = webViewHashCodeSet
                }
            }
        }

        HKLogUtil.j(Log.ERROR, HKHybird.TAG, HKJsonUtil.toJson(lifecycleMap))
    }

    fun onWebViewClose(webViewClient: WebViewClient?) {
        HKLogUtil.e(HKHybird.TAG, ">>>>>>>>>><<<<<<<<<< 检测到浏览器正在关闭, lifecycleMap->")

        HKHybird.modules.forEach {
            if (webViewClient != null) {
                val webViewHashCodeSet: MutableSet<Int> = lifecycleMap[it.key] ?: mutableSetOf()
                webViewHashCodeSet.remove(webViewClient.hashCode())
                lifecycleMap[it.key] = webViewHashCodeSet

                if (webViewHashCodeSet.isEmpty()) {
                    HKLogUtil.e(it.key, ">>>>>>>>>><<<<<<<<<< 监测到模块:${it.key} 已经完全从浏览器中解耦, 强制设置 onlineModel = false, 并检查是否有下一次生效的任务, 此时是设置的最佳时机")
                    it.value.onlineModel = false
                    HKHybirdUtil.fitNextAndFitLocalIfNeedConfigsInfo(it.key)
                }
            }
        }
        HKLogUtil.j(Log.ERROR, HKHybird.TAG, HKJsonUtil.toJson(lifecycleMap))
    }
}
