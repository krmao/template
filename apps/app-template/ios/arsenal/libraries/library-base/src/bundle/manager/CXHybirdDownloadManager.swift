import Foundation

class CXHybirdDownloadManager {

    private let downloadMap: ConcurrentHashMap<CXHybirdModuleConfigModel, Bool> = ConcurrentHashMap()

    static func isDownloading(config: CXHybirdModuleConfigModel?) -> Bool {
        return downloadMap[config] == true
    }

    static func setDownloadStatus(config: CXHybirdModuleConfigModel, isDownloading: Bool) {
        downloadMap[config] = isDownloading
    }

    static func download(remoteConfig: CXHybirdModuleConfigModel, callback: ((_ isLocalFilesValid: Bool) -> Void?)? = nil) {
        let start = System.currentTimeMillis()
        let moduleName = remoteConfig.moduleName
        let moduleManager = CXHybird.modules[moduleName]

        let zipFile = CXHybirdUtil.getZipFile(remoteConfig)

        CXLogUtil.e(CXHybird.TAG + ":" + moduleName, "下载管理器 开始, 当前版本=\(moduleManager?.currentConfig?.moduleVersion), 即将下载的版本=\(remoteConfig.moduleVersion), 即将下载的URL=\(remoteConfig.moduleDownloadUrl), 当前线程:\(Thread.currentThread().name)")
        let nextConfig = CXHybirdBundleInfoManager.getNextConfigFromBundleByName(moduleName)
        CXLogUtil.e(CXHybird.TAG + ":" + moduleName, "下载管理器 第一步: nextConfig:\(nextConfig?.moduleName):\(nextConfig?.moduleVersion):\(nextConfig?.moduleDownloadUrl)")
        CXLogUtil.e(CXHybird.TAG + ":" + moduleName, "下载管理器 第一步: remoteConfig:\(remoteConfig.moduleName):\(remoteConfig.moduleVersion):\(remoteConfig.moduleDownloadUrl)")
        if (nextConfig != nil && nextConfig == remoteConfig) {
            CXLogUtil.e(CXHybird.TAG + ":" + moduleName, "下载管理器 第一步: 系统检测到当前任务已经存在下次启动生效的队列里, 无需重复下载")
            CXLogUtil.e(CXHybird.TAG + ":" + moduleName, "下载管理器 第一步: 尝试直接替换到目标版本...")
            CXHybirdUtil.fitNextAndFitLocalIfNeedConfigsInfoSync(moduleName)//同步处理,由于检查更新是在异步线程,所以这里同步也不会阻塞主线程
            callback?.invoke(true)
            return
        }

        //检查本地有没有
        CXLogUtil.e(CXHybird.TAG + ":" + moduleName, "下载管理器 第一步: 校验本地是否已经存在有效文件夹, 有无zip包不重要, 如有则 return")
        if (CXHybirdUtil.isLocalFilesValid(remoteConfig)) {
            CXLogUtil.e(CXHybird.TAG + ":" + moduleName, "下载管理器 第一步: 本地文件夹校验成功, 无需重复下载, return isLocalFilesValid:true")
            callback?.invoke(true)
            return
        }

        if (CXHybird.downloader == nil) {
            CXLogUtil.e(CXHybird.TAG + ":" + moduleName, "下载管理器 第一步: 系统检测到 尚未配置zip下载器，请先设置模块的zip下载器, return isLocalFilesValid:false")
            callback?.invoke(false)
            return
        }

        //屏蔽正在下载的
        if (isDownloading(remoteConfig)) {
            CXLogUtil.e(CXHybird.TAG + ":" + moduleName, "下载管理器 第一步: 系统检测到 相同任务正在下载,不执行重复下载,但是下载完成后依然会调用所有的回调")
            return
        }

        CXLogUtil.e(CXHybird.TAG + ":" + moduleName, "下载管理器 第一步: 开始进行网络下载 ...")

        setDownloadStatus(remoteConfig, true) //重置下载标记

        CXHybird.downloader?.invoke(remoteConfig.moduleDownloadUrl, zipFile) {
            (file: File?) ->
                    CXLogUtil.e(CXHybird.TAG + ":" + moduleName, "下载管理器 第二步: 下载完成 \(file?.path)")

            let isLocalFilesValid = CXHybirdUtil.isLocalFilesValid(remoteConfig)

            if (isLocalFilesValid) {
                CXLogUtil.e(CXHybird.TAG + ":" + moduleName, "下载管理器 第二步: 更新包校验成功")
                CXLogUtil.e(CXHybird.TAG + ":" + moduleName, "下载管理器 第二步: 尝试直接替换到目标版本...")
                if (moduleManager != nil && moduleManager.currentConfig != remoteConfig) {
                    CXHybirdBundleInfoManager.saveNextConfigBundleByName(moduleName, remoteConfig)
                    CXHybirdUtil.fitNextAndFitLocalIfNeedConfigsInfoSync(moduleName)//同步处理,由于检查更新是在异步线程,所以这里同步也不会阻塞主线程
                } else {
                    CXLogUtil.e(CXHybird.TAG + ":" + moduleName, "下载管理器 第二步: moduleManager==nil, 无法直接替换到目标版本, 将由回调做全局初始化操作")
                }
            } else {
                CXLogUtil.e(CXHybird.TAG + ":" + moduleName, "下载管理器 第二步: 更新包校验失败")
            }

            callback?.invoke(isLocalFilesValid)
            setDownloadStatus(remoteConfig, false) //重置下载标记
            CXLogUtil.e(CXHybird.TAG + ":" + moduleName, "下载管理器 结束,  耗时:\(System.currentTimeMillis() - start)ms ")
        }
    }
}
