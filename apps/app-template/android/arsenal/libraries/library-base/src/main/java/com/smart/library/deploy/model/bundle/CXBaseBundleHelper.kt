package com.smart.library.deploy.model.bundle

import com.smart.library.base.md5
import com.smart.library.deploy.CXDeployConstants
import com.smart.library.util.cache.CXCacheManager
import java.io.File

/**
 * 装饰模式, 在不改变原类和继承的情况下, 动态的扩展一个对象的功能
 */
class CXBaseBundleHelper(val debug: Boolean, info: CXBundleInfo, rootDir: File, val pathInAssets: String) : CXIBundleHelper(info, rootDir) {

    fun getBaseDir(): File = CXCacheManager.getChildDir(rootDir, CXDeployConstants.DIR_NAME_BASE.md5(debug))
    fun getBaseZipFile(): File = File(getBaseDir(), String.format(CXDeployConstants.FILE_NAME_BASE_ZIP, info.version).md5(debug))
    fun getBaseUnzipDir(): File = File(getBaseDir(), String.format(CXDeployConstants.DIR_NAME_BASE_UNZIP, info.version).md5(debug))

    override fun getIndexFile(): File = File(getBaseUnzipDir(), info.indexName)

}