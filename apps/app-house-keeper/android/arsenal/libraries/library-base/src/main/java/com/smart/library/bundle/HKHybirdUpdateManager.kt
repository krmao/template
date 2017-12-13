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
        HKLogUtil.e(TAG, "checkUpdate start")

        if (configer == null) {
            HKLogUtil.e(TAG, "尚未配置config下载器，请先设置config下载器")
            return
        }

        if (isDownloading) {
            HKLogUtil.e(TAG, "正在下载更新中，在更新安装成功前不能下载其他更新 return")
            return
        }


        val moduleConfigUrl = moduleManager.latestValidConfiguration?.moduleConfigUrl ?: ""
        HKLogUtil.e(TAG, "do download moduleConfig start --> $moduleConfigUrl")
        configer?.invoke(moduleConfigUrl) { remoteConfiguration: HKHybirdModuleConfiguration? ->
            HKLogUtil.e(TAG, "do download moduleConfig end <--\nremoteConfigurationJsonString:$remoteConfiguration")
            HKLogUtil.e(TAG, "do download moduleConfig end <--\nremoteConfiguration != null?${remoteConfiguration != null}")
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
        if (remoteVersion != null && localVersion != null) {

            //版本号相等时不做任何处理，避免不必要的麻烦
            if (remoteVersion > localVersion) {
                HKLogUtil.e("${moduleManager.moduleFullName} 开始升级 -->")

                //升级
                moduleManager.onLineMode = true     //切换在线模式
                isDownloading = true
                HKLogUtil.e(TAG, "切换在线模式并且立即下载")
                download(remoteConfiguration)       //然后立即下载
            } else if (remoteVersion < localVersion) {
                HKLogUtil.e("${moduleManager.moduleFullName} 开始回滚 -->")

                //回滚
                moduleManager.completeRemoveAllGTLatestConfigSafely(remoteConfiguration)//delete 即时生效
                moduleManager.onLineMode = true
                isDownloading = true
                HKLogUtil.e(TAG, "切换在线模式并且立即下载")
                download(remoteConfiguration)       //然后立即下载
            }
        } else {
            HKLogUtil.e(TAG, "remoteVersion:$remoteVersion or localVersion:$localVersion is null !")
        }
    }

    /**
     * todo 安全性有待验证
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
        HKLogUtil.e(TAG, "do download zip start --> ${remoteConfiguration.moduleDownloadUrl}")

        val zipFile = moduleManager.getZipFile(remoteConfiguration)

        //1: 如果即将下载的版本 本地解压包存在切校验正确,zip即使不存在也无需下载
        if (moduleManager.isLocalFilesValid(remoteConfiguration)) {
            HKLogUtil.e(TAG, "do download zip end <-- local file valid")
            completeUpdating(remoteConfiguration)
            return
        }

        if (downloader == null) {
            HKLogUtil.e(TAG, "尚未配置zip下载器，请先设置模块的zip下载器")
            return
        }

        //如果即将下载的版本 本地解压包不存在或者校验失败,执行下载zip
        downloader?.invoke(remoteConfiguration.moduleDownloadUrl, zipFile) { file: File? ->
            HKLogUtil.e(TAG, "do download zip end <-- file:${file?.path}")

            if (moduleManager.isLocalFilesValid(remoteConfiguration)) {
                HKLogUtil.e(TAG, "do download zip end <-- local file valid")
                completeUpdating(remoteConfiguration)
            } else {
                isDownloading = false
                //moduleManager.onLineMode = false //todo 线上版本是否完备？
                HKLogUtil.e(TAG, "do download zip end <-- local file not valid !")
            }
        }
    }
}
