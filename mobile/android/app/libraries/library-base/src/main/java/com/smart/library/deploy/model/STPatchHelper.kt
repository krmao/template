package com.smart.library.deploy.model

import com.smart.library.base.md5
import com.smart.library.deploy.STDeployConstants
import com.smart.library.deploy.STDeployManager
import com.smart.library.deploy.STDeployPreferenceManager
import com.smart.library.util.STChecksumUtil
import com.smart.library.util.STFileUtil
import com.smart.library.util.STLogUtil
import com.smart.library.util.STZipUtil
import com.smart.library.util.bspatch.BSPatchUtil
import com.smart.library.util.cache.STCacheManager
import java.io.File

/**
 * 装饰模式, 在不改变原类和继承的情况下, 动态的扩展一个对象的功能
 */
@Suppress("MemberVisibilityCanBePrivate", "PrivatePropertyName")
class STPatchHelper(type: STDeployManager, val info: STPatchInfo) {

    val TAG: String by lazy { type.TAG }
    val debug: Boolean by lazy { type.isDebug() }
    val rootDir: File? by lazy { type.getRootDir() }
    val preferenceManager: STDeployPreferenceManager by lazy { type.preferenceManager }

    fun getApplyDir(): File? = STCacheManager.getChildDir(rootDir, STDeployConstants.DIR_NAME_APPLY.md5(debug))
    fun getApplyZipFile(): File = File(getApplyDir(), String.format(STDeployConstants.FILE_NAME_APPLY_ZIP, info.toVersion).md5(debug))
    fun getApplyUnzipDir(): File = File(getApplyDir(), String.format(STDeployConstants.DIR_NAME_APPLY_UNZIP, info.toVersion).md5(debug))

    fun getTempPatchFile(): File = File(getTempDir(), String.format(STDeployConstants.FILE_NAME_PATCH, info.baseVersion, info.toVersion).md5(debug))
    fun getTempZipFile(): File = File(getTempDir(), String.format(STDeployConstants.FILE_NAME_TEMP_ZIP, info.toVersion).md5(debug))
    fun getTempDir(): File? = STCacheManager.getChildDir(rootDir, STDeployConstants.DIR_NAME_TEMP.md5(debug))

    fun checkPatchFileValid(): Boolean = getTempPatchFile().exists()
    fun checkTempBundleFileValid(): Boolean {
        val tempZipFile = getTempZipFile()
        val md5 = STChecksumUtil.genMD5ForFile(tempZipFile)
        val fileExists = tempZipFile.exists()
        val md5Check = md5 == info.bundleChecksum
        val checkTempBundleFileValid = fileExists && md5Check
        STLogUtil.e(TAG, "checkTempBundleFileValid=$checkTempBundleFileValid, fileExists=$fileExists, md5Check=$md5Check, read-md5=$md5 , right-md5=${info.bundleChecksum}, path=${tempZipFile.absolutePath}")
        return checkTempBundleFileValid
    }

    fun merge(baseBundleHelper: STBaseBundleHelper): Boolean {
        STLogUtil.v(TAG, "merge start")
        if (!checkTempBundleFileValid()) {
            if (checkPatchFileValid()) {
                if (baseBundleHelper.info.version == info.baseVersion) {
                    val tempZipFile = getTempZipFile()

                    if (tempZipFile.exists()) {
                        STLogUtil.e(TAG, "rm dest zip file ${tempZipFile.absolutePath}")
                        STFileUtil.deleteFile(tempZipFile)
                    }

                    try {
                        val result = BSPatchUtil.bspatch(baseBundleHelper.getBaseZipFile().absolutePath, tempZipFile.absolutePath, getTempPatchFile().absolutePath)
                        STLogUtil.w(TAG, "bspatch ${if (result == 0) "success" else "failure"}, result=$result")
                        if (result == 0) {
                            return checkTempBundleFileValid() && copyAndUnzipToApplyDir()
                        }
                    } catch (e: Exception) {
                        STLogUtil.e(TAG, "merge exception", e)
                    }
                } else {
                    STLogUtil.e(TAG, "merge failure, local base version is not equal to path's base version")
                }
            } else {
                STLogUtil.e(TAG, "merge failure, checkPatchFileValid == false")
            }
            return false
        } else {
            STLogUtil.w(TAG, "merge checkTempBundleFileValid == true, start copyAndUnzipToApplyDir")
            return copyAndUnzipToApplyDir()
        }
    }

    fun getIndexFile(): File = File(getApplyUnzipDir(), info.indexName)

    fun checkUnzipDirValid(): Boolean {
        val indexFile = getIndexFile()
        val indexFileExists = indexFile.exists()
        STLogUtil.d(TAG, "checkUnzipDirValid=$indexFileExists, path=${indexFile.absolutePath}, indexFileExists=$indexFileExists")
        return indexFileExists
    }

    fun checkZipFileValid(zipFile: File?): Boolean {
        val valid = zipFile?.exists() == true
        STLogUtil.d(TAG, "checkZipFileValid=$valid, path=${zipFile?.absolutePath}")
        return valid
    }

    @Synchronized
    fun copyAndUnzipToApplyDir(): Boolean {
        STLogUtil.v(TAG, "copyAndUnzipToApplyDir start")
        val fromZipFile = getTempZipFile()
        val toZipFile = getApplyZipFile()
        val toUnzipDir = getApplyUnzipDir()

        if (checkZipFileValid(fromZipFile)) {
            if (toZipFile.exists()) toZipFile.delete()
            STFileUtil.fileChannelCopy(fromZipFile, toZipFile)
            if (checkZipFileValid(toZipFile)) {
                STLogUtil.d(TAG, "copy bundle.zip to apply dir success")
                if (STZipUtil.unzipToDirOrFalse(toZipFile, toUnzipDir) && checkUnzipDirValid()) {
                    STLogUtil.d(TAG, "unzip bundle.zip success")
                    preferenceManager.saveTempBundleInfo(getTempBundleInfo())
                    clearTempFiles()
                    return true
                } else {
                    STLogUtil.e(TAG, "unzip bundle.zip failure")
                }
            } else {
                STLogUtil.e(TAG, "copy bundle.zip to apply dir failure")
            }
        }
        STLogUtil.e(TAG, "copyAndUnzipToApplyDir failure")
        return false
    }

    private fun clearTempFiles() {
        STLogUtil.d(TAG, "clearTempFiles")
        STFileUtil.deleteFile(getTempPatchFile())
        STFileUtil.deleteFile(getTempZipFile())
    }

    fun getTempBundleInfo(): STBundleInfo {
        return STBundleInfo(info.toVersion, info.bundleChecksum, info.indexName)
    }

}