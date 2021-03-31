package com.smart.library.deploy.model

import androidx.annotation.Keep
import com.smart.library.base.md5
import com.smart.library.deploy.STDeployConstants
import com.smart.library.util.STFileUtil
import com.smart.library.util.STLogUtil
import com.smart.library.util.cache.STCacheManager
import java.io.File

/**
 * 装饰模式, 在不改变原类和继承的情况下, 动态的扩展一个对象的功能
 */
@Suppress("MemberVisibilityCanBePrivate", "PrivatePropertyName")
@Keep
class STDeployBundleHelper(val debug: Boolean, info: STBundleInfo, rootDir: File?, TAG: String) : STIBundleHelper(info, rootDir, TAG) {

    fun getApplyZipFile(): File = File(getApplyDir(), String.format(STDeployConstants.FILE_NAME_APPLY_ZIP, info.version).md5(debug))
    fun getApplyUnzipDir(): File = File(getApplyDir(), String.format(STDeployConstants.DIR_NAME_APPLY_UNZIP, info.version).md5(debug))

    fun getTempZipFile(): File = File(getTempDir(), String.format(STDeployConstants.FILE_NAME_TEMP_ZIP, info.version).md5(debug))
    fun getTempDir(): File? = STCacheManager.getChildDir(rootDir, STDeployConstants.DIR_NAME_TEMP.md5(debug))

    fun getApplyDir(): File? = STCacheManager.getChildDir(rootDir, STDeployConstants.DIR_NAME_APPLY.md5(debug))
    override fun getIndexFile(): File = File(getApplyUnzipDir(), info.indexName)

    fun clearApplyFiles() {
        STLogUtil.d(TAG, "apply clearApplyFiles")
        val applyZipFile = getApplyZipFile()
        val applyUnzipDir = getApplyUnzipDir()
        STLogUtil.w("apply applyZipFile:${applyZipFile.absolutePath}")
        STLogUtil.w("apply deleteDirectory:${applyUnzipDir.absolutePath}")

        STFileUtil.deleteFile(applyZipFile)
        STFileUtil.deleteDirectory(applyUnzipDir)
    }

}