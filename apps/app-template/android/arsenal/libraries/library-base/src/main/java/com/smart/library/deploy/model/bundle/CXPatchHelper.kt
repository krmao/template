package com.smart.library.deploy.model.bundle

import com.mlibrary.util.bspatch.MBSPatchUtil
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
class CXPatchHelper(val info: CXPatchInfo, val rootDir: File) {

    private val TAG: String = "[rn-deploy]"

    fun getTempPatchFile(): File = File(getTempDir(), String.format(CXDeployConstants.FILE_NAME_PATCH, info.baseVersion, info.toVersion).md5(CXDeployManager.debug))
    fun getTempZipFile(): File = File(getTempDir(), String.format(CXDeployConstants.FILE_NAME_TEMP_ZIP, info.toVersion).md5(CXDeployManager.debug))
    fun getTempDir(): File = CXCacheManager.getChildDir(rootDir, CXDeployConstants.DIR_NAME_TEMP.md5(CXDeployManager.debug))

    fun checkPatchFileValid(): Boolean = getTempPatchFile().exists()
    fun checkTempBundleFileValid(): Boolean = getTempZipFile().exists()

    fun merge(baseBundleHelper: CXBaseBundleHelper): Boolean {
        if (!checkTempBundleFileValid()) {
            if (checkPatchFileValid()) {
                if (baseBundleHelper.info.version == info.baseVersion) {
                    val temZipFile = getTempZipFile()

                    if (temZipFile.exists()) {
                        CXFileUtil.deleteFile(temZipFile)
                        temZipFile.createNewFile()
                    }

                    try {
                        MBSPatchUtil().bspatch(baseBundleHelper.getBaseZipFile().absolutePath, getTempPatchFile().absolutePath, temZipFile.absolutePath)
                    } catch (e: Exception) {
                        CXLogUtil.e(TAG, e)
                    }
                    return checkTempBundleFileValid()
                } else {
                    return false
                }
            } else {
                return false
            }
        } else return true
    }

    fun getTempBundleInfo(): CXBundleInfo {
        return CXBundleInfo(info.toVersion, info.bundleChecksum, info.indexName)
    }

}