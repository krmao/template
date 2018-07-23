package com.smart.library.deploy.model.bundle

import com.smart.library.base.md5
import com.smart.library.deploy.CXDeployConstants
import com.smart.library.deploy.CXDeployManager
import com.smart.library.util.cache.CXCacheManager
import java.io.File

/**
 * 装饰模式, 在不改变原类和继承的情况下, 动态的扩展一个对象的功能
 */
class CXBaseBundleHelper(info: CXBundleInfo, rootDir: File) : CXIBundleHelper(info, rootDir) {

    fun getBaseDir(): File = CXCacheManager.getChildDir(rootDir, CXDeployConstants.DIR_NAME_BASE.md5(CXDeployManager.debug))
    fun getBaseZipFile(): File = File(getBaseDir(), info.getZipFileName()?.md5(CXDeployManager.debug))
    fun getBaseUnzipDir(): File = File(getBaseDir(), String.format(CXDeployConstants.DIR_NAME_BASE_UNZIP, info.version).md5(CXDeployManager.debug))

    override fun getIndexFile(): File = File(getBaseUnzipDir(), info.indexName)
}