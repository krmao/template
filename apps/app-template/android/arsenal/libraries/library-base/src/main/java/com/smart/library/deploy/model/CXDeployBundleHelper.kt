package com.smart.library.deploy.model

import com.smart.library.base.md5
import com.smart.library.deploy.CXDeployConstants
import com.smart.library.util.CXFileUtil
import com.smart.library.util.CXLogUtil
import com.smart.library.util.cache.CXCacheManager
import com.smart.library.deploy.model.CXBundleInfo
import java.io.File

/**
 * 装饰模式, 在不改变原类和继承的情况下, 动态的扩展一个对象的功能
 */
@Suppress("MemberVisibilityCanBePrivate", "PrivatePropertyName")
class CXDeployBundleHelper(val debug: Boolean, info: CXBundleInfo, rootDir: File, TAG: String) : CXIBundleHelper(info, rootDir, TAG) {

    fun getApplyZipFile(): File = File(getApplyDir(), String.format(CXDeployConstants.FILE_NAME_APPLY_ZIP, info.version).md5(debug))
    fun getApplyUnzipDir(): File = File(getApplyDir(), String.format(CXDeployConstants.DIR_NAME_APPLY_UNZIP, info.version).md5(debug))

    fun getTempZipFile(): File = File(getTempDir(), String.format(CXDeployConstants.FILE_NAME_TEMP_ZIP, info.version).md5(debug))
    fun getTempDir(): File = CXCacheManager.getChildDir(rootDir, CXDeployConstants.DIR_NAME_TEMP.md5(debug))

    fun getApplyDir(): File = CXCacheManager.getChildDir(rootDir, CXDeployConstants.DIR_NAME_APPLY.md5(debug))
    override fun getIndexFile(): File = File(getApplyUnzipDir(), info.indexName)

    fun clearApplyFiles() {
        CXLogUtil.d(TAG, "apply clearApplyFiles")
        val applyZipFile = getApplyZipFile()
        val applyUnzipDir = getApplyUnzipDir()
        CXLogUtil.w("apply applyZipFile:${applyZipFile.absolutePath}")
        CXLogUtil.w("apply deleteDirectory:${applyUnzipDir.absolutePath}")

        CXFileUtil.deleteFile(applyZipFile)
        CXFileUtil.deleteDirectory(applyUnzipDir)
    }

}