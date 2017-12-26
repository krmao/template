package com.smart.library.bundle

import com.smart.library.util.HKFileUtil
import com.smart.library.util.HKLogUtil
import com.smart.library.util.HKPreferencesUtil
import java.util.*

@Suppress("MemberVisibilityCanPrivate", "PrivatePropertyName")
class HKHybirdConfigManager(val moduleManager: HKHybirdModuleManager) {

    internal val KEY_HYBIRD_CONFIG = "KEY-HYBIRD-CONFIG-${moduleManager.moduleFullName}"
    internal val KEY_HYBIRD_CONFIG_NEXT = "KEY-HYBIRD_CONFIG_NEXT-${moduleManager.moduleFullName}"

    internal var currentConfig: HKHybirdConfigModel? = null
        set(value) {
            field = value
            //当设置新的当前生效的配置信息时，更新拦截请求配置
            moduleManager.setRequestIntercept(value)
        }

    /**
     * 回滚还是升级,在 HKModuleManager 初始化之前执行
     */
    init {
        val nextConfigStack = getNextConfigStack()
        if (!nextConfigStack.empty()) {
            val destConfig = nextConfigStack.pop()
            val destVersion = destConfig.moduleVersion.toFloatOrNull()
            if (destVersion != null) {
                //回滚还是升级，取决于此
                val configList = getConfigList() //版本号降序排序
                if (configList.isNotEmpty()) {
                    val iterate = configList.listIterator()
                    while (iterate.hasNext()) {
                        val tmpConfig = iterate.next()
                        val tmpVersion = tmpConfig.moduleVersion.toFloatOrNull()
                        if (tmpVersion == null || tmpVersion >= destVersion) {
                            iterate.remove()                                                        //删除在list中的位置
                            HKFileUtil.deleteFile(moduleManager.getZipFile(tmpConfig))              //删除 zip
                            HKFileUtil.deleteDirectory(moduleManager.getUnzipDir(tmpConfig))        //删除 unzipDir
                        }
                    }
                    configList.add(0, destConfig)
                    saveConfig(configList)  //彻底删除配置信息，至此已经删除了所有与本版本相关的信息
                } else {
                    saveConfig(destConfig)
                }
            }
        }
        clearConfigNext()
    }

    @Synchronized
    internal fun saveConfig(configurationList: MutableList<HKHybirdConfigModel>) {
        HKPreferencesUtil.putList(KEY_HYBIRD_CONFIG, configurationList)
        HKLogUtil.w("save sorted list : ${configurationList.map { it.moduleVersion }}")
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
        HKPreferencesUtil.putJsonString(KEY_HYBIRD_CONFIG_NEXT, Stack<HKHybirdConfigModel>())
    }

    @Synchronized
    internal fun getNextConfigStack(): Stack<HKHybirdConfigModel> = HKPreferencesUtil.getStack(KEY_HYBIRD_CONFIG_NEXT, HKHybirdConfigModel::class.java)

    @Synchronized
    internal fun isContainedInNextConfigStack(configuration: HKHybirdConfigModel): Boolean = getNextConfigStack().contains(configuration)
}
