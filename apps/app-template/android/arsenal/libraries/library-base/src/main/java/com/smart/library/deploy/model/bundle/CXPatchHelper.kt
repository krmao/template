package com.smart.library.deploy.model.bundle

import com.mlibrary.util.bspatch.MBSPatchUtil
import com.smart.library.deploy.CXDeployConstants
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

    fun getTempPatchFile(): File = File(getTempDir(), info.patchName)
    fun getTempZipFile(): File = File(getTempDir(), info.bundleFullName)
    fun getTempDir(): File = CXCacheManager.getChildDir(rootDir, CXDeployConstants.DIR_NAME_TEMP)

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
        return CXBundleInfo(info.bundleFullName, info.toVersion, info.bundleChecksum, info.indexName)
    }

}