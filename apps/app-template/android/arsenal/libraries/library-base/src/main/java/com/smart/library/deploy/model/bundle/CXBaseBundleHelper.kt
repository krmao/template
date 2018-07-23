package com.smart.library.deploy.model.bundle

import com.smart.library.deploy.CXDeployConstants
import com.smart.library.util.cache.CXCacheManager
import java.io.File

/**
 * 装饰模式, 在不改变原类和继承的情况下, 动态的扩展一个对象的功能
 */
class CXBaseBundleHelper(info: CXBundleInfo, rootDir: File) : CXIBundleHelper(info, rootDir) {

    fun getBaseDir(): File = CXCacheManager.getChildDir(rootDir, CXDeployConstants.DIR_NAME_BASE)
    fun getBaseZipFile(): File = File(getBaseDir(), info.getZipFileName())
    fun getBaseUnzipDir(): File = File(getBaseDir(), String.format(CXDeployConstants.DIR_NAME_BASE_UNZIP, info.version))

    override fun getIndexFile(): File = File(getBaseUnzipDir(), info.indexName)
}