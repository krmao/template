package com.smart.library.bundle

import com.smart.library.util.HKLogUtil
import com.smart.library.util.HKPreferencesUtil
import java.util.*

/**
 * 处理配置信息 在 sharedpreference 的保存/读取操作,仅限于此
 */
@Suppress("MemberVisibilityCanPrivate", "PrivatePropertyName", "unused")
class HKHybirdConfigManager(val moduleManager: HKHybirdModuleManager) {

    private val KEY_HYBIRD_CONFIG = "KEY-HYBIRD-CONFIG-${moduleManager.moduleName}"
    private val KEY_HYBIRD_CONFIG_NEXT = "KEY-HYBIRD_CONFIG_NEXT-${moduleManager.moduleName}"

    @Synchronized
    internal fun saveConfig(configurationList: MutableList<HKHybirdConfigModel>) {
        HKPreferencesUtil.putList(KEY_HYBIRD_CONFIG, configurationList)
        HKLogUtil.w(moduleManager.moduleName, "保存配置信息到 sharedPreference: ${configurationList.map { it.moduleVersion }}")
    }

    @Synchronized
    internal fun saveConfig(configuration: HKHybirdConfigModel) {
        val configurationList = getConfigList()
        if (configurationList.contains(configuration))
            configurationList.remove(configuration)
        configurationList.add(0, configuration)
        saveConfig(configurationList)
    }

    /**
     * 降序排序
     */
    @Synchronized
    internal fun getConfigList(): MutableList<HKHybirdConfigModel> = HKPreferencesUtil.getList(KEY_HYBIRD_CONFIG, HKHybirdConfigModel::class.java).filter { it.moduleVersion.toFloatOrNull() != null }.sortedByDescending { it.moduleVersion.toFloatOrNull() ?: -1f }.toMutableList()

    /**
     * 入栈
     */
    @Synchronized
    internal fun saveConfigNext(config: HKHybirdConfigModel) {
        val configStack = getNextConfigStack()
        if (configStack.contains(config))
            configStack.remove(config)
        configStack.push(config)
        HKPreferencesUtil.putList(KEY_HYBIRD_CONFIG_NEXT, configStack)
        HKLogUtil.v(moduleManager.moduleName, "保存 '下次浏览器重新加载本模块才会生效的配置信息' ${config.moduleName}:${config.moduleVersion} 到 sharedPreference , 注:只有在该模块完全退出浏览器的时候才可以被应用生效")
    }

    /**
     * 清空
     */
    @Synchronized
    internal fun clearConfigNext() {
        HKPreferencesUtil.putJsonString(KEY_HYBIRD_CONFIG_NEXT, Stack<HKHybirdConfigModel>())
        HKLogUtil.v(moduleManager.moduleName, "清空下次启动生效的配置信息")
    }

    @Synchronized
    internal fun getNextConfigStack(): Stack<HKHybirdConfigModel> = HKPreferencesUtil.getStack(KEY_HYBIRD_CONFIG_NEXT, HKHybirdConfigModel::class.java)

    @Synchronized
    internal fun isContainedInNextConfigStack(configuration: HKHybirdConfigModel): Boolean = getNextConfigStack().contains(configuration)
}
