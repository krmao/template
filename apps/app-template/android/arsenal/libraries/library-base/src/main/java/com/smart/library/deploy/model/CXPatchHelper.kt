package com.smart.library.deploy.model

import com.smart.library.base.md5
import com.smart.library.deploy.CXDeployConstants
import com.smart.library.deploy.CXDeployManager
import com.smart.library.deploy.CXDeployPreferenceManager
import com.smart.library.util.CXChecksumUtil
import com.smart.library.util.CXFileUtil
import com.smart.library.util.CXLogUtil
import com.smart.library.util.CXZipUtil
import com.smart.library.util.bspatch.BSPatchUtil
import com.smart.library.util.cache.CXCacheManager
import java.io.File

/**
 * 装饰模式, 在不改变原类和继承的情况下, 动态的扩展一个对象的功能
 */
@Suppress("MemberVisibilityCanBePrivate", "PrivatePropertyName")
class CXPatchHelper(type: CXDeployManager, val info: CXPatchInfo) {

    val TAG: String by lazy { type.TAG }
    val debug: Boolean by lazy { type.isDebug() }
    val rootDir: File by lazy { type.getRootDir() }
    val preferenceManager: CXDeployPreferenceManager by lazy { type.preferenceManager }

    fun getApplyDir(): File = CXCacheManager.getChildDir(rootDir, CXDeployConstants.DIR_NAME_APPLY.md5(debug))
    fun getApplyZipFile(): File = File(getApplyDir(), String.format(CXDeployConstants.FILE_NAME_APPLY_ZIP, info.toVersion).md5(debug))
    fun getApplyUnzipDir(): File = File(getApplyDir(), String.format(CXDeployConstants.DIR_NAME_APPLY_UNZIP, info.toVersion).md5(debug))

    fun getTempPatchFile(): File = File(getTempDir(), String.format(CXDeployConstants.FILE_NAME_PATCH, info.baseVersion, info.toVersion).md5(debug))
    fun getTempZipFile(): File = File(getTempDir(), String.format(CXDeployConstants.FILE_NAME_TEMP_ZIP, info.toVersion).md5(debug))
    fun getTempDir(): File = CXCacheManager.getChildDir(rootDir, CXDeployConstants.DIR_NAME_TEMP.md5(debug))

    fun checkPatchFileValid(): Boolean = getTempPatchFile().exists()
    fun checkTempBundleFileValid(): Boolean {
        val tempZipFile = getTempZipFile()
        val md5 = CXChecksumUtil.genMD5Checksum(tempZipFile)
        val fileExists = tempZipFile.exists()
        val md5Check = md5 == info.bundleChecksum
        val checkTempBundleFileValid = fileExists && md5Check
        CXLogUtil.e(TAG, "checkTempBundleFileValid=$checkTempBundleFileValid, fileExists=$fileExists, md5Check=$md5Check, read-md5=$md5 , right-md5=${info.bundleChecksum}, path=${tempZipFile.absolutePath}")
        return checkTempBundleFileValid
    }

    fun merge(baseBundleHelper: CXBaseBundleHelper): Boolean {
        CXLogUtil.v(TAG, "merge start")
        if (!checkTempBundleFileValid()) {
            if (checkPatchFileValid()) {
                if (baseBundleHelper.info.version == info.baseVersion) {
                    val tempZipFile = getTempZipFile()

                    if (tempZipFile.exists()) {
                        CXLogUtil.e(TAG, "rm dest zip file ${tempZipFile.absolutePath}")
                        CXFileUtil.deleteFile(tempZipFile)
                    }

                    try {
                        val result = BSPatchUtil.bspatch(baseBundleHelper.getBaseZipFile().absolutePath, tempZipFile.absolutePath, getTempPatchFile().absolutePath)
                        CXLogUtil.w(TAG, "bspatch ${if (result == 0) "success" else "failure"}, result=$result")
                        if (result == 0) {
                            return checkTempBundleFileValid() && copyAndUnzipToApplyDir()
                        }
                    } catch (e: Exception) {
                        CXLogUtil.e(TAG, "merge exception", e)
                    }
                } else {
                    CXLogUtil.e(TAG, "merge failure, local base version is not equal to path's base version")
                }
            } else {
                CXLogUtil.e(TAG, "merge failure, checkPatchFileValid == false")
            }
            return false
        } else {
            CXLogUtil.w(TAG, "merge checkTempBundleFileValid == true, start copyAndUnzipToApplyDir")
            return copyAndUnzipToApplyDir()
        }
    }

    fun getIndexFile(): File = File(getApplyUnzipDir(), info.indexName)

    fun checkUnzipDirValid(): Boolean {
        val indexFile = getIndexFile()
        val indexFileExists = indexFile.exists()
        CXLogUtil.d(TAG, "checkUnzipDirValid=$indexFileExists, path=${indexFile.absolutePath}, indexFileExists=$indexFileExists")
        return indexFileExists
    }

    fun checkZipFileValid(zipFile: File?): Boolean {
        val valid = zipFile?.exists() == true
        CXLogUtil.d(TAG, "checkZipFileValid=$valid, path=${zipFile?.absolutePath}")
        return valid
    }

    @Synchronized
    fun copyAndUnzipToApplyDir(): Boolean {
        CXLogUtil.v(TAG, "copyAndUnzipToApplyDir start")
        val fromZipFile = getTempZipFile()
        val toZipFile = getApplyZipFile()
        val toUnzipDir = getApplyUnzipDir()

        if (checkZipFileValid(fromZipFile)) {
            if (toZipFile.exists()) toZipFile.delete()
            CXFileUtil.fileChannelCopy(fromZipFile, toZipFile)
            if (checkZipFileValid(toZipFile)) {
                CXLogUtil.d(TAG, "copy bundle.zip to apply dir success")
                if (CXZipUtil.unzipToDirOrFalse(toZipFile, toUnzipDir) && checkUnzipDirValid()) {
                    CXLogUtil.d(TAG, "unzip bundle.zip success")
                    preferenceManager.saveTempBundleInfo(getTempBundleInfo())
                    clearTempFiles()
                    return true
                } else {
                    CXLogUtil.e(TAG, "unzip bundle.zip failure")
                }
            } else {
                CXLogUtil.e(TAG, "copy bundle.zip to apply dir failure")
            }
        }
        CXLogUtil.e(TAG, "copyAndUnzipToApplyDir failure")
        return false
    }

    private fun clearTempFiles() {
        CXLogUtil.d(TAG, "clearTempFiles")
        CXFileUtil.deleteFile(getTempPatchFile())
        CXFileUtil.deleteFile(getTempZipFile())
    }

    fun getTempBundleInfo(): CXBundleInfo {
        return CXBundleInfo(info.toVersion, info.bundleChecksum, info.indexName)
    }

}