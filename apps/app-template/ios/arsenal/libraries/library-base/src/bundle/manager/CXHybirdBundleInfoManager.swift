import Foundation

class CXHybirdBundleInfoManager: NSObject {

    private static let KEY_HYBIRD_BUNDLE_MODEL_LIST = "KEY_HYBIRD_BUNDLE_MODEL_LIST"

    static func getBundles() -> MutableMap<String, CXHybirdModuleBundleModel> {
        let map: MutableMap<String, CXHybirdModuleBundleModel> = CXPreferencesUtil.getMap(KEY_HYBIRD_BUNDLE_MODEL_LIST) ?? MutableMap<String, CXHybirdModuleBundleModel>()
        map.values.forEach { (bundleModel: CXHybirdModuleBundleModel) in
            bundleModel.moduleConfigList.sort {
                $0.moduleVersion.toFloatOrNull() ?? -1 > $1.moduleVersion.toFloatOrNull() ?? -1
            }
        }
        return map
    }

    static func saveBundle(_ bundle: CXHybirdModuleBundleModel) {
        var bundles = getBundles()
        bundles[bundle.moduleName] = bundle
        saveBundles(bundles)
    }


    static func saveBundles(_ bundleMap: Map<String, CXHybirdModuleBundleModel>) {
        CXPreferencesUtil.putMap(KEY_HYBIRD_BUNDLE_MODEL_LIST, bundleMap)
        CXLogUtil.i("--------------->>>> 保存配置信息到 sharedPreference: \(bundleMap.values())")
    }

    static func saveConfigListToBundleByName(_ moduleName: String?, _ configList: MutableList<CXHybirdModuleConfigModel>) {
        if (moduleName != nil && moduleName!.isNotBlank()) {
            var bundles: MutableMap<String, CXHybirdModuleBundleModel> = getBundles()
            var bundle: CXHybirdModuleBundleModel? = bundles[moduleName!]
            if (bundle == nil) {
                bundle = CXHybirdModuleBundleModel(moduleName!, configList)
            } else {
                bundle!.moduleConfigList = configList
            }
            bundles[moduleName!] = bundle
            saveBundles(bundles)
        }
    }


    static func saveConfigListToBundleList(_ configList: MutableList<CXHybirdModuleConfigModel>) {
        var bundles: MutableMap<String, CXHybirdModuleBundleModel> = getBundles()

        configList.forEach { it in
            var bundle: CXHybirdModuleBundleModel? = bundles[it.moduleName]
            if (bundle == nil) {
                var list = MutableList<CXHybirdModuleConfigModel>()
                list.add(it)

                bundle = CXHybirdModuleBundleModel(it.moduleName, list)
            } else {
                bundle!.moduleConfigList.add(it)
            }
            bundles[it.moduleName] = bundle
        }

        saveBundles(bundles)
    }


    static func saveNextConfigBundleByName(_ moduleName: String?, _ nextConfig: CXHybirdModuleConfigModel?) {
        CXLogUtil.e(CXHybird.TAG, "krmao: saveNextConfigBundleByName: moduleName=\(moduleName), nextConfig is \(nextConfig == nil ? "nil !" : "not nil !") ---->")
        CXLogUtil.j(CXLogUtil.ERROR, CXJsonUtil.toJson(nextConfig))
        if (moduleName != nil && moduleName!.isNotBlank()) {
            var bundles: MutableMap<String, CXHybirdModuleBundleModel> = getBundles()
            var bundle: CXHybirdModuleBundleModel? = bundles[moduleName!]
            if (bundle == nil) {
                bundle = CXHybirdModuleBundleModel(moduleName!, nextConfig)
            } else {
                bundle!.moduleNextConfig = nextConfig
            }
            bundles[moduleName!] = bundle
            saveBundles(bundles)
        }
    }


    static func removeNextConfigBundleByName(_ moduleName: String?) {
        CXLogUtil.e(CXHybird.TAG, "removeNextConfigBundleByName: moduleName=\(moduleName)")
        saveNextConfigBundleByName(moduleName, nil)
    }


    static func saveConfigToBundleByName(_ moduleName: String?, _ config: CXHybirdModuleConfigModel) {
        if (moduleName != nil && moduleName!.isNotBlank()) {
            var configList = getConfigListFromBundleByName(moduleName)
            if (configList.contains(config)) {
                configList.remove(config)
            }
            configList.add(0, config)
            saveConfigListToBundleByName(moduleName, configList)
        }
    }

    /**
     * moduleConfigList 降序排序
     */
    static func getConfigListFromBundleByName(_ moduleName: String?) -> MutableList<CXHybirdModuleConfigModel> {
        let configList: MutableList<CXHybirdModuleConfigModel> = getBundles()[moduleName ?? ""]?.moduleConfigList ?? MutableList<CXHybirdModuleConfigModel>()
        return configList.sorted {
            $0.moduleVersion.toFloatOrNull() ?? -1 > $1.moduleVersion.toFloatOrNull() ?? -1
        }
    }


    static func getNextConfigFromBundleByName(_ moduleName: String?) -> CXHybirdModuleConfigModel? {
        let nextConfig = getBundles()[moduleName ?? ""]?.moduleNextConfig
        CXLogUtil.e(CXHybird.TAG, "getNextConfigFromBundleByName: moduleName=\(moduleName), nextConfig is \(nextConfig == nil ? "nil !" : "not nil !") ---->")
        CXLogUtil.j(CXLogUtil.ERROR, CXJsonUtil.toJson(nextConfig))
        return nextConfig
    }
}


