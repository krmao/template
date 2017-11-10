package com.smart.library.bundle

import java.util.*

/**
 * 负责检查更新和下载
 */
class HKHybirdModuleUpdateManager {
    private val updateStack = Stack<HKHybirdModuleConfiguration>()
    private var downloadManager: ((downloadUrl: String, filePath: String, callback: (Boolean) -> Unit) -> Unit)? = null

    /**
     * 初始化下载器
     * 第一次启动时检测本地是否有未下载完的
     * 检测到下载成功未解压替换的，执行解压替换
     */
    fun init() {

    }

    /**
     * 检查是否有更新
     */
    fun checkUpdate() {

    }

    fun checkUpdateSync(module: HKHybirdModuleManager? = null): Boolean? {
        updateStack.push(HKHybirdModuleConfiguration())
        return true
    }

    fun addUpdateTask(configuration: HKHybirdModuleConfiguration) {
        if (!updateStack.contains(configuration)) {
            updateStack.push(configuration)
            downloadManager?.invoke(configuration.moduleDownloadUrl, "") { downloadSuccess: Boolean ->
                if (downloadSuccess) {

                } else {
                    updateStack.remove(configuration)
                }
            }
        }
    }

    fun setDownloadManager(manager: (downloadUrl: String, filePath: String, callback: (downloadSuccess: Boolean) -> Unit) -> Unit) {
        downloadManager = manager
    }
}
