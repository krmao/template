package com.smart.library.bundle.manager

import android.util.Log
import androidx.annotation.Keep
import com.smart.library.bundle.STHybird
import com.smart.library.bundle.model.STHybirdModuleBundleModel
import com.smart.library.bundle.model.STHybirdModuleConfigModel
import com.smart.library.util.STJsonUtil
import com.smart.library.util.STLogUtil
import com.smart.library.util.STPreferencesUtil

@Suppress("MemberVisibilityCanPrivate", "PrivatePropertyName", "unused")
@Keep
object STHybirdBundleInfoManager {
    private val TAG: String = STHybirdBundleInfoManager::class.java.name

    private val KEY_HYBIRD_BUNDLE_MODEL_LIST = "KEY_HYBIRD_BUNDLE_MODEL_LIST"

    @Synchronized
    fun getBundles(): MutableMap<String, STHybirdModuleBundleModel> {
        val map: MutableMap<String, STHybirdModuleBundleModel> = STPreferencesUtil.getMap(KEY_HYBIRD_BUNDLE_MODEL_LIST, STHybirdModuleBundleModel::class.java)
        map.values.forEach {
            it.moduleConfigList = it.moduleConfigList.sortedByDescending { it.moduleVersion.toFloatOrNull() ?: -1f }.toMutableList()
        }
        return map
    }

    @Synchronized
    fun saveBundle(bundle: STHybirdModuleBundleModel) {
        saveBundles(getBundles().apply { this[bundle.moduleName] = bundle })
    }

    @Synchronized
    fun saveBundles(bundleMap: Map<String, STHybirdModuleBundleModel>) {
        STPreferencesUtil.putMap(KEY_HYBIRD_BUNDLE_MODEL_LIST, bundleMap)
        STLogUtil.w(TAG, "保存配置信息到 sharedPreference: $bundleMap")
    }

    @Synchronized
    fun saveConfigListToBundleByName(moduleName: String?, configList: MutableList<STHybirdModuleConfigModel>) {
        if (moduleName != null && moduleName.isNotBlank()) {
            val bundles: MutableMap<String, STHybirdModuleBundleModel> = getBundles()
            var bundle: STHybirdModuleBundleModel? = bundles[moduleName]
            if (bundle == null) {
                bundle = STHybirdModuleBundleModel(moduleName, configList)
            } else {
                bundle.moduleConfigList = configList
            }
            bundles[moduleName] = bundle
            saveBundles(bundles)
        }
    }

    @Synchronized
    fun saveConfigListToBundleList(configList: MutableList<STHybirdModuleConfigModel>) {
        val bundles: MutableMap<String, STHybirdModuleBundleModel> = getBundles()

        configList.forEach {
            var bundle: STHybirdModuleBundleModel? = bundles[it.moduleName]
            if (bundle == null) {
                bundle = STHybirdModuleBundleModel(it.moduleName, arrayListOf(it))
            } else {
                bundle.moduleConfigList.add(it)
            }
            bundles[it.moduleName] = bundle
        }

        saveBundles(bundles)
    }

    @Synchronized
    fun saveNextConfigBundleByName(moduleName: String?, nextConfig: STHybirdModuleConfigModel?) {
        STLogUtil.e(STHybird.TAG, "krmao: saveNextConfigBundleByName: moduleName=$moduleName, nextConfig is ${if (nextConfig == null) "null !" else "not null !"} ---->")
        STLogUtil.j(Log.ERROR, STHybird.TAG, STJsonUtil.toJson(nextConfig))
        if (moduleName != null && moduleName.isNotBlank()) {
            val bundles: MutableMap<String, STHybirdModuleBundleModel> = getBundles()
            var bundle: STHybirdModuleBundleModel? = bundles[moduleName]
            if (bundle == null) {
                bundle = STHybirdModuleBundleModel(moduleName, moduleNextConfig = nextConfig)
            } else {
                bundle.moduleNextConfig = nextConfig
            }
            bundles[moduleName] = bundle
            saveBundles(bundles)
        }
    }

    @Synchronized
    fun removeNextConfigBundleByName(moduleName: String?) {
        STLogUtil.e(STHybird.TAG, "removeNextConfigBundleByName: moduleName=$moduleName")
        saveNextConfigBundleByName(moduleName, null)
    }

    @Synchronized
    fun saveConfigToBundleByName(moduleName: String?, config: STHybirdModuleConfigModel) {
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
    fun getConfigListFromBundleByName(moduleName: String?): MutableList<STHybirdModuleConfigModel> {
        return getBundles()[moduleName]?.moduleConfigList ?: mutableListOf()
    }

    @Synchronized
    fun getNextConfigFromBundleByName(moduleName: String?): STHybirdModuleConfigModel? {
        val nextConfig = getBundles()[moduleName]?.moduleNextConfig
        STLogUtil.e(STHybird.TAG, "getNextConfigFromBundleByName: moduleName=$moduleName, nextConfig is ${if (nextConfig == null) "null !" else "not null !"} ---->")
        STLogUtil.j(Log.ERROR, STHybird.TAG, STJsonUtil.toJson(nextConfig))
        return nextConfig
    }
}


