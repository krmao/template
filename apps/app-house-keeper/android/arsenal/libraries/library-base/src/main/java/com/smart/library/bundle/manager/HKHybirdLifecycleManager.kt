package com.smart.library.bundle.manager

import android.webkit.WebViewClient
import com.smart.library.bundle.HKHybird
import com.smart.library.util.HKLogUtil

class HKHybirdLifecycleManager(val moduleManager: HKHybirdModuleManager) {

    private val webViewClientSet: MutableSet<WebViewClient?> = mutableSetOf()

    @Synchronized
    fun isModuleOpenNow(): Boolean {
        HKLogUtil.e(moduleManager.moduleName, "isModuleOpenNow -> webViewClientSet.size=${webViewClientSet.size}")
        return !webViewClientSet.isEmpty()
    }

    fun onWebViewOpenPage(webViewClient: WebViewClient?, url: String?) {
        val isMemberOfModule = HKHybird.isMemberOfModule(moduleManager.currentConfig, url)

        HKLogUtil.w(moduleManager.moduleName, "onWebViewOpenPage -> 系统监测到当前 url ${if (isMemberOfModule) "" else "不"} 属于模块 ${moduleManager.moduleName}  url=$url")

        if (isMemberOfModule) {
            HKLogUtil.w(moduleManager.moduleName, "onWebViewOpenPage -> 系统监测到当前 url 属于模块 ${moduleManager.moduleName}  url=$url")
            if (webViewClient != null) {
                HKLogUtil.w(moduleManager.moduleName, "onWebViewOpenPage -> 添加 webView 到 ${moduleManager.moduleName}")
                webViewClientSet.add(webViewClient)
            } else {
                HKLogUtil.e(moduleManager.moduleName, "onWebViewOpenPage -> webView 为空, 不添加到 生命周期, 请检查代码是否存在错误!!!")
            }
        }
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
