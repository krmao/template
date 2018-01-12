package com.smart.library.bundle.manager

import com.smart.library.bundle.HKHybird
import com.smart.library.bundle.model.HKHybirdModuleConfigModel
import com.smart.library.bundle.util.HKHybirdUtil
import com.smart.library.util.HKLogUtil
import java.io.File
import java.util.concurrent.ConcurrentHashMap

@Suppress("MemberVisibilityCanPrivate", "unused")
object HKHybirdDownloadManager {

    private val downloadMap: ConcurrentHashMap<HKHybirdModuleConfigModel, Boolean> = ConcurrentHashMap()

    @JvmStatic
    fun isDownloading(config: HKHybirdModuleConfigModel?): Boolean {
        return downloadMap[config] == true
    }

    @JvmStatic
    fun setDownloadStatus(config: HKHybirdModuleConfigModel, isDownloading: Boolean) {
        downloadMap[config] = isDownloading
    }

    @JvmStatic
    fun download(remoteConfig: HKHybirdModuleConfigModel, callback: ((isLocalFilesValid: Boolean) -> Unit?)? = null) {
        val start = System.currentTimeMillis()
        val moduleName = remoteConfig.moduleName
        val moduleManager = HKHybird.MODULES[moduleName]

        val zipFile = HKHybirdUtil.getZipFile(remoteConfig)

        HKLogUtil.e(HKHybird.TAG + ":" + moduleName, "下载管理器 开始, 当前版本=${moduleManager?.currentConfig?.moduleVersion}: ${remoteConfig.moduleDownloadUrl}, 当前线程:${Thread.currentThread().name}")
        HKLogUtil.e(HKHybird.TAG + ":" + moduleName, "下载管理器 第一步: 本次任务是否已经存在下次启动生效的队列里, 如有则 return")

        val nextConfig = HKHybirdBundleInfoManager.getNextConfigFromBundleByName(moduleName)

        if (nextConfig == nextConfig) {
            HKLogUtil.w(HKHybird.TAG + ":" + moduleName, "下载管理器 第一步: 系统检测到当前任务已经存在下次启动生效的队列里, 无需重复下载, return isLocalFilesValid:true")
            callback?.invoke(true)
            return
        }

        //检查本地有没有
        HKLogUtil.e(HKHybird.TAG + ":" + moduleName, "下载管理器 第二步: 校验本地是否已经存在有效文件夹, 有无zip包不重要, 如有则 return")
        if (HKHybirdUtil.isLocalFilesValid(remoteConfig)) {
            HKLogUtil.e(HKHybird.TAG + ":" + moduleName, "下载管理器 第二步: 本地文件夹校验成功, 无需重复下载, return isLocalFilesValid:true")
            callback?.invoke(true)
            return
        }

        if (HKHybird.downloader == null) {
            HKLogUtil.e(HKHybird.TAG + ":" + moduleName, "下载管理器 系统检测到 尚未配置zip下载器，请先设置模块的zip下载器, return isLocalFilesValid:false")
            callback?.invoke(false)
            return
        }

        //屏蔽正在下载的
        if (isDownloading(remoteConfig)) {
            HKLogUtil.e(HKHybird.TAG + ":" + moduleName, "下载管理器 系统检测到 相同任务正在下载,不执行重复下载,但是下载完成后依然会调用所有的回调")
            return
        }

        HKLogUtil.v(HKHybird.TAG + ":" + moduleName, "下载管理器 开始进行网络下载 ...")

        setDownloadStatus(remoteConfig, true) //重置下载标记

        HKHybird.downloader?.invoke(remoteConfig.moduleDownloadUrl, zipFile) { file: File? ->
            HKLogUtil.e(HKHybird.TAG + ":" + moduleName, "下载管理器 下载结束 ${file?.path}")

            val isLocalFilesValid = HKHybirdUtil.isLocalFilesValid(remoteConfig)

            if (isLocalFilesValid) {
                HKLogUtil.e(HKHybird.TAG + ":" + moduleName, "下载管理器 更新包校验成功")
                HKHybirdBundleInfoManager.saveNextConfigBundleByName(moduleName, remoteConfig)
                HKHybird.MODULES[moduleName]?.fitNextAndFitLocalIfNeedConfigsInfoSync() //同步处理,由于检查更新是在异步线程,所以这里同步也不会阻塞主线程
            } else {
                HKLogUtil.e(HKHybird.TAG + ":" + moduleName, "下载管理器 更新包校验失败")
            }

            callback?.invoke(isLocalFilesValid)
            setDownloadStatus(remoteConfig, false) //重置下载标记
            HKLogUtil.e(HKHybird.TAG + ":" + moduleName, "下载管理器 结束,  耗时:${System.currentTimeMillis() - start}ms ")
        }
    }
}
