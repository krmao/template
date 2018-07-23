package com.smart.library.deploy.model.bundle

import com.smart.library.base.md5
import com.smart.library.deploy.CXDeployConstants
import com.smart.library.deploy.CXDeployManager
import com.smart.library.util.CXFileUtil
import com.smart.library.util.CXLogUtil
import com.smart.library.util.cache.CXCacheManager
import java.io.File

/**
 * 装饰模式, 在不改变原类和继承的情况下, 动态的扩展一个对象的功能
 */
@Suppress("MemberVisibilityCanBePrivate", "PrivatePropertyName")
class CXDeployBundleHelper(info: CXBundleInfo, rootDir: File) : CXIBundleHelper(info, rootDir) {

    fun getApplyZipFile(): File = File(getApplyDir(), info.getZipFileName()?.md5(CXDeployManager.debug))
    fun getApplyUnzipDir(): File = File(getApplyDir(), String.format(CXDeployConstants.DIR_NAME_APPLY_UNZIP, info.version).md5(CXDeployManager.debug))

    fun getTempZipFile(): File = File(getTempDir(), info.getZipFileName()?.md5(CXDeployManager.debug))
    fun getTempDir(): File = CXCacheManager.getChildDir(rootDir, CXDeployConstants.DIR_NAME_TEMP.md5(CXDeployManager.debug))

    fun getApplyDir(): File = CXCacheManager.getChildDir(rootDir, CXDeployConstants.DIR_NAME_APPLY.md5(CXDeployManager.debug))
    override fun getIndexFile(): File = File(getApplyUnzipDir(), info.indexName)

    fun clearApplyDir() {
        CXLogUtil.d(TAG, "clearApplyDir")
        val applyDir: File = getApplyDir()
        CXFileUtil.deleteDirectory(applyDir)
        applyDir.mkdirs()
    }

    fun clearTempDir() {
        CXLogUtil.d(TAG, "clearTempDir")
        val tempDir: File = getTempDir()
        CXFileUtil.deleteDirectory(tempDir)
        tempDir.mkdirs()
    }

}