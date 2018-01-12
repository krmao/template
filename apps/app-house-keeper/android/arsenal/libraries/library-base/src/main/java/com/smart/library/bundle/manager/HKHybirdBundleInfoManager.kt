package com.smart.library.bundle.manager

import com.smart.library.bundle.model.HKHybirdModuleBundleModel
import com.smart.library.bundle.model.HKHybirdModuleConfigModel
import com.smart.library.util.HKLogUtil
import com.smart.library.util.HKPreferencesUtil

@Suppress("MemberVisibilityCanPrivate", "PrivatePropertyName", "unused")
object HKHybirdBundleInfoManager {
    private val TAG: String = HKHybirdBundleInfoManager::class.java.name

    private val KEY_HYBIRD_BUNDLE_MODEL_LIST = "KEY_HYBIRD_BUNDLE_MODEL_LIST"

    @Synchronized
    fun getBundles(): MutableMap<String, HKHybirdModuleBundleModel> {
        val map: MutableMap<String, HKHybirdModuleBundleModel> = HKPreferencesUtil.getMap(KEY_HYBIRD_BUNDLE_MODEL_LIST, HKHybirdModuleBundleModel::class.java)
        map.values.forEach {
            it.moduleConfigList = it.moduleConfigList.sortedByDescending { it.moduleVersion.toFloatOrNull() ?: -1f }.toMutableList()
        }
        return map
    }

    @Synchronized
    fun saveBundle(bundle: HKHybirdModuleBundleModel) {
        saveBundles(getBundles().apply { this[bundle.moduleName] = bundle })
    }

    @Synchronized
    fun saveBundles(bundleMap: Map<String, HKHybirdModuleBundleModel>) {
        HKPreferencesUtil.putMap(KEY_HYBIRD_BUNDLE_MODEL_LIST, bundleMap)
        HKLogUtil.w(TAG, "保存配置信息到 sharedPreference: $bundleMap")
    }

    @Synchronized
    fun saveConfigListToBundleByName(moduleName: String?, configList: MutableList<HKHybirdModuleConfigModel>) {
        if (moduleName != null && moduleName.isNotBlank()) {
            val bundles: MutableMap<String, HKHybirdModuleBundleModel> = getBundles()
            var bundle: HKHybirdModuleBundleModel? = bundles[moduleName]
            if (bundle == null) {
                bundle = HKHybirdModuleBundleModel(moduleName, configList)
            } else {
                bundle.moduleConfigList = configList
            }
            bundles[moduleName] = bundle
            saveBundles(bundles)
        }
    }

    @Synchronized
    fun saveConfigListToBundleList(configList: MutableList<HKHybirdModuleConfigModel>) {
        val bundles: MutableMap<String, HKHybirdModuleBundleModel> = getBundles()

        configList.forEach {
            var bundle: HKHybirdModuleBundleModel? = bundles[it.moduleName]
            if (bundle == null) {
                bundle = HKHybirdModuleBundleModel(it.moduleName, arrayListOf(it))
            } else {
                bundle.moduleConfigList.add(it)
            }
            bundles[it.moduleName] = bundle
        }

        saveBundles(bundles)
    }

    @Synchronized
    fun saveNextConfigBundleByName(moduleName: String?, nextConfig: HKHybirdModuleConfigModel?) {
        if (moduleName != null && moduleName.isNotBlank()) {
            val bundles: MutableMap<String, HKHybirdModuleBundleModel> = getBundles()
            var bundle: HKHybirdModuleBundleModel? = bundles[moduleName]
            if (bundle == null) {
                bundle = HKHybirdModuleBundleModel(moduleName, moduleNextConfig = nextConfig)
            } else {
                bundle.moduleNextConfig = nextConfig
            }
            bundles[moduleName] = bundle
            saveBundles(bundles)
        }
    }

    @Synchronized
    fun removeNextConfigBundleByName(moduleName: String?) {
        saveNextConfigBundleByName(moduleName, null)
    }

    @Synchronized
    fun saveConfigToBundleByName(moduleName: String?, config: HKHybirdModuleConfigModel) {
        if (moduleName != null && moduleName.isNotBlank()) {
            val configList = getConfigListFromBundleByName(moduleName)
            if (configList.contains(config))
                configList.remove(config)
            configList.add(0, config)
            saveConfigListToBundleByName(moduleName, configList)
        }
    }

    /**
     * moduleConfigList 降序排序
     */
    @Synchronized
    fun getConfigListFromBundleByName(moduleName: String?): MutableList<HKHybirdModuleConfigModel> {
        return getBundles()[moduleName]?.moduleConfigList ?: mutableListOf()
    }

    @Synchronized
    fun getNextConfigFromBundleByName(moduleName: String?): HKHybirdModuleConfigModel? {
        return getBundles()[moduleName]?.moduleNextConfig
    }
}


