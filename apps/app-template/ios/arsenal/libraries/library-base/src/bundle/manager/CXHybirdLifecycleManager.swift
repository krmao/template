import Foundation

class CXHybirdLifecycleManager {
    private static var lifecycleMap: MutableMap<String, MutableSet<Int>> = MutableMap<String, MutableSet<Int>>()


    static func isModuleOpened(_ moduleName: String?) -> Bool {
        if (moduleName == nil) {
            return false
        }
        CXLogUtil.e(CXHybird.TAG, ">>>>>>>>>><<<<<<<<<< 已经加载\(moduleName ?? "nil")的浏览器数量=\(lifecycleMap[moduleName!]?.size ?? 0)")
        return lifecycleMap[moduleName!]?.isNotEmpty() == true
    }

    static func onWebViewOpenPage(_ webViewClient: WebViewClient?, _ url: String?) {
        CXLogUtil.e(CXHybird.TAG, ">>>>>>>>>><<<<<<<<<< 检测到浏览器正在加载页面, lifecycleMap->")

        CXHybird.modules.forEach { it in
            if (CXHybird.isMemberOfModule(it.value.currentConfig, url)) {
                CXLogUtil.w(CXHybird.TAG, ">>>>>>>>>><<<<<<<<<< 系统监测到当前 url 属于模块 \(it.key)  url=\(url)")
                if (webViewClient != nil) {

                    var webViewHashCodeSet: MutableSet<Int> = lifecycleMap[it.key] ?? MutableSet<Int>()
                    webViewHashCodeSet.add(webViewClient!.hashCode())
                    lifecycleMap[it.key] = webViewHashCodeSet
                }
            }
        }

        //CXLogUtil.j(Log.ERROR, CXHybird.TAG, CXJsonUtil.toJson(lifecycleMap))
    }

    static func onWebViewClose(_ webViewClient: WebViewClient?) {
        CXLogUtil.e(CXHybird.TAG, ">>>>>>>>>><<<<<<<<<< 检测到浏览器正在关闭, lifecycleMap->")

        CXHybird.modules.forEach { it in
            if (webViewClient != nil) {
                var webViewHashCodeSet: MutableSet<Int> = lifecycleMap[it.key] ?? MutableSet<Int>()
                webViewHashCodeSet.remove(webViewClient!.hashCode())
                lifecycleMap[it.key] = webViewHashCodeSet

                if (webViewHashCodeSet.isEmpty()) {
                    CXLogUtil.e(CXHybird.TAG, ">>>>>>>>>><<<<<<<<<< 监测到模块:\(it.key) 已经完全从浏览器中解耦, 强制设置 onlineModel = false, 并检查是否有下一次生效的任务, 此时是设置的最佳时机")
                    it.value.onlineModel = false
                    CXHybirdUtil.fitNextAndFitLocalIfNeedConfigsInfo(it.key)
                }
            }
        }
        CXLogUtil.j(CXLogUtil.ERROR, CXJsonUtil.toJson(lifecycleMap))
    }
}
