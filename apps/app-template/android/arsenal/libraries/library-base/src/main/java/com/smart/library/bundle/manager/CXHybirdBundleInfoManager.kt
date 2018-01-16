package com.smart.library.bundle.manager

import android.util.Log
import com.smart.library.bundle.CXHybird
import com.smart.library.bundle.model.CXHybirdModuleBundleModel
import com.smart.library.bundle.model.CXHybirdModuleConfigModel
import com.smart.library.util.CXJsonUtil
import com.smart.library.util.CXLogUtil
import com.smart.library.util.CXPreferencesUtil

@Suppress("MemberVisibilityCanPrivate", "PrivatePropertyName", "unused")
object CXHybirdBundleInfoManager {
    private val TAG: String = CXHybirdBundleInfoManager::class.java.name

    private val KEY_HYBIRD_BUNDLE_MODEL_LIST = "KEY_HYBIRD_BUNDLE_MODEL_LIST"

    @Synchronized
    fun getBundles(): MutableMap<String, CXHybirdModuleBundleModel> {
        val map: MutableMap<String, CXHybirdModuleBundleModel> = CXPreferencesUtil.getMap(KEY_HYBIRD_BUNDLE_MODEL_LIST, CXHybirdModuleBundleModel::class.java)
        map.values.forEach {
            it.moduleConfigList = it.moduleConfigList.sortedByDescending { it.moduleVersion.toFloatOrNull() ?: -1f }.toMutableList()
        }
        return map
    }

    @Synchronized
    fun saveBundle(bundle: CXHybirdModuleBundleModel) {
        saveBundles(getBundles().apply { this[bundle.moduleName] = bundle })
    }

    @Synchronized
    fun saveBundles(bundleMap: Map<String, CXHybirdModuleBundleModel>) {
        CXPreferencesUtil.putMap(KEY_HYBIRD_BUNDLE_MODEL_LIST, bundleMap)
        CXLogUtil.w(TAG, "保存配置信息到 sharedPreference: $bundleMap")
    }

    @Synchronized
    fun saveConfigListToBundleByName(moduleName: String?, configList: MutableList<CXHybirdModuleConfigModel>) {
        if (moduleName != null && moduleName.isNotBlank()) {
            val bundles: MutableMap<String, CXHybirdModuleBundleModel> = getBundles()
            var bundle: CXHybirdModuleBundleModel? = bundles[moduleName]
            if (bundle == null) {
                bundle = CXHybirdModuleBundleModel(moduleName, configList)
            } else {
                bundle.moduleConfigList = configList
            }
            bundles[moduleName] = bundle
            saveBundles(bundles)
        }
    }

    @Synchronized
    fun saveConfigListToBundleList(configList: MutableList<CXHybirdModuleConfigModel>) {
        val bundles: MutableMap<String, CXHybirdModuleBundleModel> = getBundles()

        configList.forEach {
            var bundle: CXHybirdModuleBundleModel? = bundles[it.moduleName]
            if (bundle == null) {
                bundle = CXHybirdModuleBundleModel(it.moduleName, arrayListOf(it))
            } else {
                bundle.moduleConfigList.add(it)
            }
            bundles[it.moduleName] = bundle
        }

        saveBundles(bundles)
    }

    @Synchronized
    fun saveNextConfigBundleByName(moduleName: String?, nextConfig: CXHybirdModuleConfigModel?) {
        CXLogUtil.e(CXHybird.TAG, "krmao: saveNextConfigBundleByName: moduleName=$moduleName, nextConfig is ${if (nextConfig == null) "null !" else "not null !"} ---->")
        CXLogUtil.j(Log.ERROR, CXHybird.TAG, CXJsonUtil.toJson(nextConfig))
        if (moduleName != null && moduleName.isNotBlank()) {
            val bundles: MutableMap<String, CXHybirdModuleBundleModel> = getBundles()
            var bundle: CXHybirdModuleBundleModel? = bundles[moduleName]
            if (bundle == null) {
                bundle = CXHybirdModuleBundleModel(moduleName, moduleNextConfig = nextConfig)
            } else {
                bundle.moduleNextConfig = nextConfig
            }
            bundles[moduleName] = bundle
            saveBundles(bundles)
        }
    }

    @Synchronized
    fun removeNextConfigBundleByName(moduleName: String?) {
        CXLogUtil.e(CXHybird.TAG, "removeNextConfigBundleByName: moduleName=$moduleName")
        saveNextConfigBundleByName(moduleName, null)
    }

    @Synchronized
    fun saveConfigToBundleByName(moduleName: String?, config: CXHybirdModuleConfigModel) {
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
    fun getConfigListFromBundleByName(moduleName: String?): MutableList<CXHybirdModuleConfigModel> {
        return getBundles()[moduleName]?.moduleConfigList ?: mutableListOf()
    }

    @Synchronized
    fun getNextConfigFromBundleByName(moduleName: String?): CXHybirdModuleConfigModel? {
        val nextConfig = getBundles()[moduleName]?.moduleNextConfig
        CXLogUtil.e(CXHybird.TAG, "getNextConfigFromBundleByName: moduleName=$moduleName, nextConfig is ${if (nextConfig == null) "null !" else "not null !"} ---->")
        CXLogUtil.j(Log.ERROR, CXHybird.TAG, CXJsonUtil.toJson(nextConfig))
        return nextConfig
    }
}


