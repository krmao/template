package com.smart.library.deploy.model

import com.smart.library.base.md5
import com.smart.library.deploy.STDeployConstants
import com.smart.library.util.cache.STCacheManager
import java.io.File

/**
 * 装饰模式, 在不改变原类和继承的情况下, 动态的扩展一个对象的功能
 */
class STBaseBundleHelper(val debug: Boolean, info: STBundleInfo, rootDir: File?, val pathInAssets: String, TAG: String) : STIBundleHelper(info, rootDir, TAG) {

    fun getBaseDir(): File? = STCacheManager.getChildDir(rootDir, STDeployConstants.DIR_NAME_BASE.md5(debug))
    fun getBaseZipFile(): File = File(getBaseDir(), String.format(STDeployConstants.FILE_NAME_BASE_ZIP, info.version).md5(debug))
    fun getBaseUnzipDir(): File = File(getBaseDir(), String.format(STDeployConstants.DIR_NAME_BASE_UNZIP, info.version).md5(debug))

    override fun getIndexFile(): File = File(getBaseUnzipDir(), info.indexName)

}