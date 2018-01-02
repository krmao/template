package com.smart.library.bundle.manager

import android.webkit.WebViewClient
import com.smart.library.util.HKLogUtil

class HKHybirdLifecycleManager(val moduleManager: HKHybirdModuleManager) {

    private val webViewClientSet: MutableSet<WebViewClient?> = mutableSetOf()

    @Synchronized
    fun isModuleOpenNow(): Boolean {
        HKLogUtil.e(moduleManager.moduleName, "isModuleOpenNow -> webViewClientSet.size=${webViewClientSet.size}")
        return !webViewClientSet.isEmpty()
    }

    fun onWebViewOpenPage(webViewClient: WebViewClient?) {
        if (webViewClient != null)
            webViewClientSet.add(webViewClient)
        HKLogUtil.e(moduleManager.moduleName, "onWebViewOpenPage -> webViewClientSet.size=${webViewClientSet.size}")
    }

    fun onWebViewClose(webViewClient: WebViewClient?) {
        if (webViewClient != null)
            webViewClientSet.remove(webViewClient)
        HKLogUtil.e(moduleManager.moduleName, "onWebViewClose -> webViewClientSet.size=${webViewClientSet.size}")

        if (webViewClientSet.isEmpty()) {
            HKLogUtil.e(moduleManager.moduleName, "系统监测到当前模块已经完全从浏览器中解耦,强制 onlineModel = false , 并检查是否有 下一次加载本模块 生效的任务,此时是设置的最佳时机")
            moduleManager.onlineModel = false
            moduleManager.fitNextAndFitLocalIfNeedConfigsInfo()
        }
    }
}
