import Foundation

class CXHybirdBundleInfoManager {

    private let TAG: String = "\(type(of: self))"

    private let KEY_HYBIRD_BUNDLE_MODEL_LIST = "KEY_HYBIRD_BUNDLE_MODEL_LIST"

    func getBundles() -> MutableMap<String, CXHybirdModuleBundleModel> {
        let map: MutableMap<String, CXHybirdModuleBundleModel> = CXPreferencesUtil.getMap(KEY_HYBIRD_BUNDLE_MODEL_LIST, CXHybirdModuleBundleModel.TAG)
        map.values.forEach {
            it.moduleConfigList = it.moduleConfigList.sortedByDescending {
                it.moduleVersion.toFloatOrNull() ?? -1
            }.toMutableList()
        }
        return map
    }

    func saveBundle(bundle: CXHybirdModuleBundleModel) {
        saveBundles(getBundles().apply {
            this[bundle.moduleName] = bundle
        })
    }


    func saveBundles(bundleMap: Map<String, CXHybirdModuleBundleModel>) {
        CXPreferencesUtil.putMap(KEY_HYBIRD_BUNDLE_MODEL_LIST, bundleMap)
        CXLogUtil.w(TAG, "保存配置信息到 sharedPreference: \(bundleMap)")
    }

    func saveConfigListToBundleByName(moduleName: String?, configList: MutableList<CXHybirdModuleConfigModel>) {
        if (moduleName != nil && moduleName.isNotBlank()) {
            let bundles: MutableMap<String, CXHybirdModuleBundleModel> = getBundles()
            var bundle: CXHybirdModuleBundleModel? = bundles[moduleName]
            if (bundle == nil) {
                bundle = CXHybirdModuleBundleModel(moduleName, configList)
            } else {
                bundle.moduleConfigList = configList
            }
            bundles[moduleName] = bundle
            saveBundles(bundles)
        }
    }


    func saveConfigListToBundleList(configList: MutableList<CXHybirdModuleConfigModel>) {
        let bundles: MutableMap<String, CXHybirdModuleBundleModel> = getBundles()

        configList.forEach {
            var bundle: CXHybirdModuleBundleModel? = bundles[it.moduleName]
            if (bundle == nil) {
                bundle = CXHybirdModuleBundleModel(it.moduleName, arrayListOf(it))
            } else {
                bundle.moduleConfigList.add(it)
            }
            bundles[it.moduleName] = bundle
        }

        saveBundles(bundles)
    }


    func saveNextConfigBundleByName(moduleName: String?, nextConfig: CXHybirdModuleConfigModel?) {
        CXLogUtil.e(CXHybird.TAG, "krmao: saveNextConfigBundleByName: moduleName=\(moduleName), nextConfig is \(nextConfig == nil ? "nil !" : "not nil !") ---->")
        CXLogUtil.j(Log.ERROR, CXHybird.TAG, CXJsonUtil.toJson(nextConfig))
        if (moduleName != nil && moduleName.isNotBlank()) {
            let bundles: MutableMap<String, CXHybirdModuleBundleModel> = getBundles()
            var bundle: CXHybirdModuleBundleModel? = bundles[moduleName]
            if (bundle == nil) {
                bundle = CXHybirdModuleBundleModel(moduleName, moduleNextConfig = nextConfig)
            } else {
                bundle.moduleNextConfig = nextConfig
            }
            bundles[moduleName] = bundle
            saveBundles(bundles)
        }
    }


    func removeNextConfigBundleByName(moduleName: String?) {
        CXLogUtil.e(CXHybird.TAG, "removeNextConfigBundleByName: moduleName=\(moduleName)")
        saveNextConfigBundleByName(moduleName, nil)
    }


    func saveConfigToBundleByName(moduleName: String?, config: CXHybirdModuleConfigModel) {
        if (moduleName != nil && moduleName.isNotBlank()) {
            let configList = getConfigListFromBundleByName(moduleName)
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

    func getConfigListFromBundleByName(moduleName: String?) -> MutableList<CXHybirdModuleConfigModel> {
        return getBundles()[moduleName]?.moduleConfigList ?? mutableListOf()
    }


    func getNextConfigFromBundleByName(moduleName: String?) -> CXHybirdModuleConfigModel? {
        let nextConfig = getBundles()[moduleName]?.moduleNextConfig
        CXLogUtil.e(CXHybird.TAG, "getNextConfigFromBundleByName: moduleName=\(moduleName), nextConfig is \(nextConfig == nil ? "nil !" : "not nil !") ---->")
        CXLogUtil.j(Log.ERROR, CXHybird.TAG, CXJsonUtil.toJson(nextConfig))
        return nextConfig
    }
}


