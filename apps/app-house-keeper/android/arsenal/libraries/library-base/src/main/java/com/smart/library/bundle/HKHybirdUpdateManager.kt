package com.smart.library.bundle

import com.smart.library.util.HKLogUtil
import java.io.File

/**
 * 负责检查更新和下载
 *
 * 注意: 当下载到一半或者执行解压校验一半忽然杀死App,则下次启动重新执行更行步骤，app启动时加载最新版本的文件夹目录，
 * 校验失败则删除并加载上一版本目录，如果没有了，则执行从安装包中解压初始包
 *
 */
@Suppress("MemberVisibilityCanPrivate", "PrivatePropertyName")
class HKHybirdUpdateManager(val moduleManager: HKHybirdModuleManager) {
    private val TAG = HKHybirdUpdateManager::class.java.simpleName

    var configer: ((configUrl: String, callback: (HKHybirdModuleConfiguration?) -> Unit?) -> Unit?)? = null
    var downloader: ((downloadUrl: String, file: File?, callback: (File?) -> Unit?) -> Unit?)? = null
    private var isDownloading = false

    fun checkUpdate() {
        HKLogUtil.w(TAG, "checkUpdate start")

        if (isDownloading) {
            HKLogUtil.e(TAG, "正在下载更新中，在更新安装成功前不能下载其他更新 return")
            return
        }

        configer?.invoke(moduleManager.latestValidConfiguration?.moduleConfigUrl ?: "") { remoteConfiguration: HKHybirdModuleConfiguration? ->
            HKLogUtil.d(TAG, "remoteConfigurationJsonString:$remoteConfiguration")
            HKLogUtil.w(TAG, "remoteConfiguration != null?${remoteConfiguration != null}")
            if (remoteConfiguration != null) {

                //1:正式包，所有机器可以拉取
                //2:测试包，只要测试机器可以拉取
                HKLogUtil.w("remoteConfiguration.moduleDebug == ${remoteConfiguration.moduleDebug}")
                HKLogUtil.w("HKHybirdManager.DEBUG == ${HKHybirdManager.DEBUG}")
                if (!remoteConfiguration.moduleDebug || (remoteConfiguration.moduleDebug && HKHybirdManager.DEBUG)) {
                    startUpdating(remoteConfiguration)
                }
            }
        }
    }

    private fun startUpdating(remoteConfiguration: HKHybirdModuleConfiguration) {
        val remoteVersion = remoteConfiguration.moduleVersion.toFloatOrNull()
        val localVersion = moduleManager.latestValidConfiguration?.moduleVersion?.toFloatOrNull()
        HKLogUtil.e("${moduleManager.moduleFullName} 当前版本:$localVersion   远程版本:$remoteVersion")
        if (remoteVersion != null && localVersion != null && remoteVersion > localVersion) {
            moduleManager.onLineMode = true     //切换在线模式
            isDownloading = true
            HKLogUtil.e(TAG, "切换在线模式并且立即下载")
            download(remoteConfiguration)       //然后立即下载
        } else {
            HKLogUtil.e(TAG, "无需下载")
        }
    }

    /**
     * 锁住 moduleManager 确保升级期间不会有乱入操作，导致数据混乱
     */
    private fun completeUpdating(remoteConfiguration: HKHybirdModuleConfiguration) {
        synchronized(moduleManager) {
            HKLogUtil.e(TAG, "实时切换 start")
            val start = System.currentTimeMillis()
            moduleManager.latestValidConfiguration = remoteConfiguration
            moduleManager.onLineMode = false
            moduleManager.saveConfiguration(remoteConfiguration)
            isDownloading = false
            HKLogUtil.e(TAG, "实时切换 end  耗时:${System.currentTimeMillis() - start}ms ")
        }
    }


    private fun download(remoteConfiguration: HKHybirdModuleConfiguration) {
        HKLogUtil.e(TAG, "do download start --> ${remoteConfiguration.moduleDownloadUrl}")

        downloader?.invoke(remoteConfiguration.moduleDownloadUrl, moduleManager.getZipFile(remoteConfiguration)) { _: File? ->
            if (moduleManager.isLocalFilesValid(remoteConfiguration))
                completeUpdating(remoteConfiguration)
        }
    }
}
