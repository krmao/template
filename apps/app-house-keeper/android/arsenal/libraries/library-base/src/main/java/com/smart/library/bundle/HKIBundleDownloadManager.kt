package com.smart.library.bundle

interface HKIBundleDownloadManager {
    /**
     * 1:第一次启动时检测本地是否有未下载完的
     */
    fun init()

    /**
     * 检查是否有更新
     */
    fun checkUpdate(module: Any? = null)

    /**
     * 下载更新包
     */
    fun download(module: Any? = null)
}
