package com.smart.library.bundle

import com.smart.library.util.HKFileUtil
import com.smart.library.util.HKLogUtil
import com.smart.library.util.HKPreferencesUtil
import java.util.*

@Suppress("MemberVisibilityCanPrivate", "PrivatePropertyName")
class HKHybirdConfigManager(val moduleManager: HKHybirdModuleManager) {

    internal val KEY_HYBIRD_CONFIG = "KEY-HYBIRD-CONFIG-${moduleManager.moduleName}"
    internal val KEY_HYBIRD_CONFIG_NEXT = "KEY-HYBIRD_CONFIG_NEXT-${moduleManager.moduleName}"

    internal var currentConfig: HKHybirdConfigModel? = null
        set(value) {
            field = value
            //当设置新的当前生效的配置信息时，更新拦截请求配置
            if (value != null)
                moduleManager.setIntercept(value)
        }

    /**
     * 处理 下次模块启动生效的目标配置文件, 结果保存到 sharedPreference
     * 清除 下次模块启动生效的目标配置信息
     * 返回 当前本地可以使用的最完备的配置信息
     */
    fun fitNextConfigsInfo(): MutableList<HKHybirdConfigModel> {
        HKLogUtil.v(moduleManager.moduleName, "检测是否可以在此时处理下次模块启动生效的配置 开始")
        val configList = getConfigList() //版本号降序排序

        if (!moduleManager.onLineModel) {
            HKLogUtil.v(moduleManager.moduleName, "检测当前模块未被浏览器加载,可以处理")

            val nextConfigStack = getNextConfigStack()
            if (!nextConfigStack.empty()) {
                HKLogUtil.v(moduleManager.moduleName, "检测下次生效的配置信息不为空,开始处理")
                val destConfig = nextConfigStack.pop()
                val destVersion = destConfig.moduleVersion.toFloatOrNull()
                if (destVersion != null) {
                    if (configList.isNotEmpty()) {
                        val iterate = configList.listIterator()
                        while (iterate.hasNext()) {
                            val tmpConfig = iterate.next()
                            val tmpVersion = tmpConfig.moduleVersion.toFloatOrNull()
                            //版本号为空 或者 这是回滚操作,则清空大于等于该版本的所有文件/配置
                            if (tmpVersion == null || tmpVersion >= destVersion) {
                                HKLogUtil.v(moduleManager.moduleName, "清空版本号为$tmpVersion(升级/回滚的目标版本为$destVersion) 的所有本地文件以及配置信息")
                                iterate.remove()                                                                //删除在list中的位置
                                HKFileUtil.deleteFile(HKHybirdModuleManager.getZipFile(tmpConfig))              //删除 zip
                                HKFileUtil.deleteDirectory(HKHybirdModuleManager.getUnzipDir(tmpConfig))        //删除 unzipDir
                            }
                        }
                    }
                    configList.add(0, destConfig)
                    saveConfig(configList)  //彻底删除配置信息，至此已经删除了所有与本版本相关的信息
                }
                clearConfigNext()
            } else {
                HKLogUtil.v(moduleManager.moduleName, "检测下次生效的配置信息为空,无需处理")
            }
        } else {
            HKLogUtil.v(moduleManager.moduleName, "检测当前模块正在被浏览器加载,不能处理")
        }
        HKLogUtil.v(moduleManager.moduleName, "检测是否可以在此时处理下次模块启动生效的配置 结束")
        return configList
    }

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
    internal fun saveConfigNext(configuration: HKHybirdConfigModel) {
        val configStack = getNextConfigStack()
        if (configStack.contains(configuration))
            configStack.remove(configuration)
        configStack.push(configuration)
        HKPreferencesUtil.putList(KEY_HYBIRD_CONFIG_NEXT, configStack)
    }

    /**
     * 清空
     */
    @Synchronized
    internal fun clearConfigNext() {
        HKLogUtil.v(moduleManager.moduleName, "清空下次启动生效的配置信息")
        HKPreferencesUtil.putJsonString(KEY_HYBIRD_CONFIG_NEXT, Stack<HKHybirdConfigModel>())
    }

    @Synchronized
    internal fun getNextConfigStack(): Stack<HKHybirdConfigModel> = HKPreferencesUtil.getStack(KEY_HYBIRD_CONFIG_NEXT, HKHybirdConfigModel::class.java)

    @Synchronized
    internal fun isContainedInNextConfigStack(configuration: HKHybirdConfigModel): Boolean = getNextConfigStack().contains(configuration)
}
