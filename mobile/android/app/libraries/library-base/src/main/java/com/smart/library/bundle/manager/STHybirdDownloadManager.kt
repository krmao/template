package com.smart.library.bundle.manager

import androidx.annotation.Keep
import com.smart.library.bundle.STHybird
import com.smart.library.bundle.model.STHybirdModuleConfigModel
import com.smart.library.bundle.util.STHybirdUtil
import com.smart.library.util.STLogUtil
import java.io.File
import java.util.concurrent.ConcurrentHashMap

@Suppress("MemberVisibilityCanPrivate", "unused")
//@Keep
object STHybirdDownloadManager {

    private val downloadMap: ConcurrentHashMap<STHybirdModuleConfigModel, Boolean> = ConcurrentHashMap()

    @JvmStatic
    fun isDownloading(config: STHybirdModuleConfigModel?): Boolean {
        return downloadMap[config] == true
    }

    @JvmStatic
    fun setDownloadStatus(config: STHybirdModuleConfigModel, isDownloading: Boolean) {
        downloadMap[config] = isDownloading
    }

    @JvmStatic
    fun download(remoteConfig: STHybirdModuleConfigModel, callback: ((isLocalFilesValid: Boolean) -> Unit?)? = null) {
        val start = System.currentTimeMillis()
        val moduleName = remoteConfig.moduleName
        val moduleManager = STHybird.modules[moduleName]

        val zipFile = STHybirdUtil.getZipFile(remoteConfig)

        STLogUtil.e(STHybird.TAG + ":" + moduleName, "下载管理器 开始, 当前版本=${moduleManager?.currentConfig?.moduleVersion}, 即将下载的版本=${remoteConfig.moduleVersion}, 即将下载的URL=${remoteConfig.moduleDownloadUrl}, 当前线程:${Thread.currentThread().name}")
        val nextConfig = STHybirdBundleInfoManager.getNextConfigFromBundleByName(moduleName)
        STLogUtil.e(STHybird.TAG + ":" + moduleName, "下载管理器 第一步: nextConfig:${nextConfig?.moduleName}:${nextConfig?.moduleVersion}:${nextConfig?.moduleDownloadUrl}")
        STLogUtil.e(STHybird.TAG + ":" + moduleName, "下载管理器 第一步: remoteConfig:${remoteConfig.moduleName}:${remoteConfig.moduleVersion}:${remoteConfig.moduleDownloadUrl}")
        if (nextConfig != null && nextConfig == remoteConfig) {
            STLogUtil.e(STHybird.TAG + ":" + moduleName, "下载管理器 第一步: 系统检测到当前任务已经存在下次启动生效的队列里, 无需重复下载")
            STLogUtil.e(STHybird.TAG + ":" + moduleName, "下载管理器 第一步: 尝试直接替换到目标版本...")
            STHybirdUtil.fitNextAndFitLocalIfNeedConfigsInfoSync(moduleName)//同步处理,由于检查更新是在异步线程,所以这里同步也不会阻塞主线程
            callback?.invoke(true)
            return
        }

        //检查本地有没有
        STLogUtil.e(STHybird.TAG + ":" + moduleName, "下载管理器 第一步: 校验本地是否已经存在有效文件夹, 有无zip包不重要, 如有则 return")
        if (STHybirdUtil.isLocalFilesValid(remoteConfig)) {
            STLogUtil.e(STHybird.TAG + ":" + moduleName, "下载管理器 第一步: 本地文件夹校验成功, 无需重复下载, return isLocalFilesValid:true")
            callback?.invoke(true)
            return
        }

        if (STHybird.downloader == null) {
            STLogUtil.e(STHybird.TAG + ":" + moduleName, "下载管理器 第一步: 系统检测到 尚未配置zip下载器，请先设置模块的zip下载器, return isLocalFilesValid:false")
            callback?.invoke(false)
            return
        }

        //屏蔽正在下载的
        if (isDownloading(remoteConfig)) {
            STLogUtil.e(STHybird.TAG + ":" + moduleName, "下载管理器 第一步: 系统检测到 相同任务正在下载,不执行重复下载,但是下载完成后依然会调用所有的回调")
            return
        }

        STLogUtil.e(STHybird.TAG + ":" + moduleName, "下载管理器 第一步: 开始进行网络下载 ...")

        setDownloadStatus(remoteConfig, true) //重置下载标记

        STHybird.downloader?.invoke(remoteConfig.moduleDownloadUrl, zipFile) { file: File? ->
            STLogUtil.e(STHybird.TAG + ":" + moduleName, "下载管理器 第二步: 下载完成 ${file?.path}")

            val isLocalFilesValid = STHybirdUtil.isLocalFilesValid(remoteConfig)

            if (isLocalFilesValid) {
                STLogUtil.e(STHybird.TAG + ":" + moduleName, "下载管理器 第二步: 更新包校验成功")
                STLogUtil.e(STHybird.TAG + ":" + moduleName, "下载管理器 第二步: 尝试直接替换到目标版本...")
                if (moduleManager != null && moduleManager.currentConfig != remoteConfig) {
                    STHybirdBundleInfoManager.saveNextConfigBundleByName(moduleName, remoteConfig)
                    STHybirdUtil.fitNextAndFitLocalIfNeedConfigsInfoSync(moduleName)//同步处理,由于检查更新是在异步线程,所以这里同步也不会阻塞主线程
                } else {
                    STLogUtil.e(STHybird.TAG + ":" + moduleName, "下载管理器 第二步: moduleManager==null, 无法直接替换到目标版本, 将由回调做全局初始化操作")
                }
            } else {
                STLogUtil.e(STHybird.TAG + ":" + moduleName, "下载管理器 第二步: 更新包校验失败")
            }

            callback?.invoke(isLocalFilesValid)
            setDownloadStatus(remoteConfig, false) //重置下载标记
            STLogUtil.e(STHybird.TAG + ":" + moduleName, "下载管理器 结束,  耗时:${System.currentTimeMillis() - start}ms ")
        }
    }
}
