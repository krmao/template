import Foundation

class CXHybirdLifecycleManager {
    private let lifecycleMap: MutableMap<String, MutableSet<Int>> = mutableMapOf()


    func  isModuleOpened(moduleName: String?)-> Boolean {
        CXLogUtil.e(CXHybird.TAG, ">>>>>>>>>><<<<<<<<<< 已经加载\(moduleName)的浏览器数量=\(lifecycleMap[moduleName]?.size ?? 0)")
        return lifecycleMap[moduleName]?.isNotEmpty() == true
    }

    func  onWebViewOpenPage(webViewClient: WebViewClient?, url: String?) {
        CXLogUtil.e(CXHybird.TAG, ">>>>>>>>>><<<<<<<<<< 检测到浏览器正在加载页面, lifecycleMap->")

        CXHybird.modules.forEach {
            if (CXHybird.isMemberOfModule(it.value.currentConfig, url)) {
                CXLogUtil.w(CXHybird.TAG, ">>>>>>>>>><<<<<<<<<< 系统监测到当前 url 属于模块 \(it.key)  url=\(url)")
                if (webViewClient != nil) {

                    let webViewHashCodeSet: MutableSet<Int> = lifecycleMap[it.key] ?? mutableSetOf()
                    webViewHashCodeSet.add(webViewClient.hashCode())
                    lifecycleMap[it.key] = webViewHashCodeSet
                }
            }
        }

        //CXLogUtil.j(Log.ERROR, CXHybird.TAG, CXJsonUtil.toJson(lifecycleMap))
    }

    func  onWebViewClose(webViewClient: WebViewClient?) {
        CXLogUtil.e(CXHybird.TAG, ">>>>>>>>>><<<<<<<<<< 检测到浏览器正在关闭, lifecycleMap->")

        CXHybird.modules.forEach {
            if (webViewClient != nil) {
                let webViewHashCodeSet: MutableSet<Int> = lifecycleMap[it.key] ?? mutableSetOf()
                webViewHashCodeSet.remove(webViewClient.hashCode())
                lifecycleMap[it.key] = webViewHashCodeSet

                if (webViewHashCodeSet.isEmpty()) {
                    CXLogUtil.e(CXHybird.TAG, ">>>>>>>>>><<<<<<<<<< 监测到模块:\(it.key) 已经完全从浏览器中解耦, 强制设置 onlineModel = false, 并检查是否有下一次生效的任务, 此时是设置的最佳时机")
                    it.value.onlineModel = false
                    CXHybirdUtil.fitNextAndFitLocalIfNeedConfigsInfo(it.key)
                }
            }
        }
        CXLogUtil.j(Log.ERROR, CXHybird.TAG, CXJsonUtil.toJson(lifecycleMap))
    }
}
