package com.smart.library.bundle

import com.smart.library.util.HKLogUtil
import java.io.File

/**
 * 负责检查更新和下载
 */
@Suppress("MemberVisibilityCanPrivate", "PrivatePropertyName")
class HKHybirdUpdateManager(val moduleManager: HKHybirdModuleManager) {
    private val TAG = HKHybirdUpdateManager::class.java.simpleName

    var configer: ((configUrl: String?, callback: (HKHybirdModuleConfiguration?) -> Unit) -> Unit)? = null
    var downloader: ((downloadUrl: String?, file: File, callback: (File?) -> Unit) -> Unit)? = null
    var isDownloading = false

    fun checkUpdate() {
        configer?.invoke(moduleManager.localConfiguration?.moduleConfigUrl) { remoteConfiguration ->
            HKLogUtil.d(TAG, "remoteConfiguration:$remoteConfiguration")
            if (remoteConfiguration != null) {
                val remoteVersion = remoteConfiguration.moduleVersion.toFloatOrNull()
                val localVersion = moduleManager.localConfiguration?.moduleVersion?.toFloatOrNull()
                if (remoteVersion != null && localVersion != null && remoteVersion > localVersion) {
                    download(remoteConfiguration)
                }
            }
        }
    }

    private fun download(remoteConfiguration: HKHybirdModuleConfiguration) {
        if (!isDownloading) {
            isDownloading = true
            downloader?.invoke(remoteConfiguration.moduleDownloadUrl, moduleManager.getLocalZipFile(remoteConfiguration.moduleVersion)) { file: File? ->
                if (file != null && file.exists()) {
                    if (moduleManager.verifyZip(file, remoteConfiguration.moduleZipMd5)) {
                        val unzipDir = moduleManager.getLocalUnZipDir(remoteConfiguration.moduleVersion)
                        moduleManager.unzipToLocal(file, unzipDir)
                        if (moduleManager.verifyLocalFiles(unzipDir, remoteConfiguration.moduleFilesMd5)) {
                            //TODO 实时切换

                            HKLogUtil.w(TAG, "download ${file.name} success ! verifyZip success ! verifyLocalFiles success! 实时切换成功 ！！！")
                        } else {
                            HKLogUtil.e(TAG, "download ${file.name} success ! verifyZip success ! but verifyLocalFiles failure! the moduleFilesMd5 of unzipDir is not right!")
                        }
                    } else {
                        HKLogUtil.e(TAG, "download ${file.name} success ! but verifyZip failure! the zipMd5 of localZipFile is not right!")
                    }
                } else {
                    HKLogUtil.e(TAG, "download ${file?.name} failure ! localFile not exist! moduleDownloadUrl=${remoteConfiguration.moduleDownloadUrl}")
                }
                isDownloading = false
            }
        }
    }
}
