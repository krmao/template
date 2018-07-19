@file:Suppress("unused")

package com.smart.library.deploy.client.impl

import com.mlibrary.util.bspatch.MBSPatchUtil
import com.smart.library.deploy.client.CXIDeployClient
import com.smart.library.util.CXFileUtil
import com.smart.library.util.CXLogUtil
import com.smart.library.util.CXPreferencesUtil
import com.smart.library.util.CXZipUtil
import com.smart.library.util.cache.CXCacheManager
import java.io.File

@Suppress("MemberVisibilityCanBePrivate", "unused")
data class BundleInfo(
        val fullName: String? = "bundle.zip",
        val version: Int,
        val checksum: String = ""
) {
    fun getZipFileName(): String? = if (fullName == null || fullName.isNullOrBlank()) null else CXFileUtil.getFileName(fullName, false)

    fun getUnzipDirName(): String? = if (fullName == null || fullName.isNullOrBlank()) null else CXFileUtil.getFileName(fullName, true)

    fun getApplyZipFile(applyDir: File): File = File(applyDir, getZipFileName())
    fun getApplyUnzipDir(applyDir: File): File = File(applyDir, getUnzipDirName())

    fun getTempZipFile(tempDir: File): File = File(tempDir, getZipFileName())

    fun getBaseZipFile(baseDir: File): File = File(baseDir, getZipFileName())
    fun getBaseUnzipDir(baseDir: File): File = File(baseDir, getUnzipDirName())
}

@Suppress("PrivatePropertyName", "MemberVisibilityCanBePrivate")
/**
 *
 * 负责 react-native 动态部署相关处理
 *
 * bundle:  包含全部数据的 zip 压缩包
 *
 * base:    基准代码
 *
 * temp:    具体应用前的临时存放的合并好的区域
 *
 * patch:   具体应用后的新版本的代码
 *
 */
class CXDeployClientForReactNative(
        val baseBundleInfo: BundleInfo,
        val rootDir: File = CXCacheManager.getFilesHotPatchReactNativeDir(),
        val checkHandler: ((bundleInfo: BundleInfo?, patchDownloadUrl: String?) -> Unit?) -> Unit?,
        val downloadHandler: (patchDownloadUrl: String?, callback: (file: File?) -> Unit) -> Unit?
) : CXIDeployClient {
    private val TAG: String = "[rn-deploy]"

    private val applyDirName = "apply"
    private val tempDirName = "temp"
    private val baseDirName = "base"
    fun getBaseDir(rootDir: File): File = CXCacheManager.getChildDir(rootDir, baseDirName)
    fun getTempDir(rootDir: File): File = CXCacheManager.getChildDir(rootDir, tempDirName)
    fun getApplyDir(rootDir: File): File = CXCacheManager.getChildDir(rootDir, applyDirName)

    private val bsPatchUtil: MBSPatchUtil by lazy { MBSPatchUtil() }

    /**
     * ensure base unzipDir valid
     */
    fun initialize(callback: (indexBundleFile: File?) -> Unit) {
        CXLogUtil.w(TAG, "initialize start")
        callback.invoke(getIndexBundleFile())
    }

    private fun getIndexBundleFile(): File? {
        var finalIndexBundleFile: File? = null

        val appliedBundleInfo: BundleInfo? = getAppliedBundleInfo()
        if (appliedBundleInfo != null) {
            val appliedIndexBundleFile = File(appliedBundleInfo.getApplyUnzipDir(getApplyDir(rootDir)), "index.android.bundle")
            if (appliedIndexBundleFile.exists()) {
                finalIndexBundleFile = appliedIndexBundleFile
            }
        }
        if (finalIndexBundleFile == null) {
            val baseDir = getBaseDir(rootDir)
            val baseZipFile = baseBundleInfo.getBaseZipFile(baseDir)
            val baseUnzipDir = baseBundleInfo.getBaseUnzipDir(baseDir)
            val baseIndexBundleFile = File(baseBundleInfo.getBaseUnzipDir(getBaseDir(rootDir)), "index.android.bundle")

            if (checkUnzipDirValid(baseUnzipDir) || ((checkZipFileValid(baseZipFile) || copyBundleToSDCardFromAssets(baseZipFile)) && unzipToDir(baseZipFile, baseUnzipDir))) {
                finalIndexBundleFile = baseIndexBundleFile
            }
        }
        return finalIndexBundleFile
    }

    /**
     * 将 assets 中的基础数据copy并解压到 sdcard
     */
    fun copyBundleToSDCardFromAssets(baseZipFile: File = baseBundleInfo.getBaseZipFile(baseBundleInfo.getBaseZipFile(rootDir))): Boolean {
        CXLogUtil.w(TAG, "copyBundleToSDCardFromAssets start")
        CXFileUtil.deleteFile(baseZipFile)
        val copyAndCheckSuccess = CXFileUtil.copyFromAssets(baseBundleInfo.fullName, baseZipFile) && checkZipFileValid(baseZipFile)
        CXLogUtil.w(TAG, "copyBundleToSDCardFromAssets end, copyAndCheckSuccess=$copyAndCheckSuccess")
        return copyAndCheckSuccess
    }

    /**
     * 解压 bundle 到指定文件夹
     */
    fun unzipToDir(zipFile: File, unZipDir: File?): Boolean {
        CXLogUtil.w(TAG, "unzipToDir start")
        val unzipAndCheckSuccess = CXZipUtil.unzipOrFalse(zipFile, unZipDir) && checkUnzipDirValid(unZipDir)
        CXLogUtil.w(TAG, "unzipToDir end, unzipAndCheckSuccess=$unzipAndCheckSuccess")
        return unzipAndCheckSuccess
    }

    /**
     * 检查是否有需要处理的更新
     * bundle-rn-1.zip
     */
    fun check() {
        checkHandler.invoke { bundleInfo, patchDownloadUrl ->
            if (bundleInfo != null && patchDownloadUrl != null && bundleInfo.version > baseBundleInfo.version) {
                download(bundleInfo, patchDownloadUrl)
            }
        }
    }

    /**
     * 下载新的更新数据
     */
    fun download(bundleInfo: BundleInfo, patchDownloadUrl: String) {
        downloadHandler.invoke(patchDownloadUrl) { patchFile: File? ->
            if (patchFile != null && patchFile.exists()) {
                val isNeedMerge = true //TODO

                @Suppress("ConstantConditionIf")
                if (isNeedMerge) {
                    val tempZipFile = bundleInfo.getTempZipFile(rootDir)
                    merge(patchFile, tempZipFile)
                    if (tempZipFile.exists()) {
                        saveTempBundleInfo(bundleInfo)
                        apply()
                    }
                }
            }
        }
    }

    /**
     * 通过基础数据合并生成新的数据
     */
    fun merge(patchFile: File?, toFile: File?) {
        if (patchFile == null || !patchFile.exists() || toFile == null) {
            return
        }
        if (toFile.exists()) CXFileUtil.deleteFile(toFile)

        try {
            bsPatchUtil.bspatch(baseBundleInfo.getBaseZipFile(rootDir).absolutePath, toFile.absolutePath, patchFile.absolutePath)
        } catch (e: Exception) {
            CXLogUtil.e(TAG, e)
        }
    }

    /**
     * 在合适的实际删除老的更新数据, 并拷贝新的更新数据到目标位置
     */
    @Synchronized
    fun apply() {
        if (!isRNRunningNow()) {
            val tempBundleInfo: BundleInfo? = getTempBundleInfo()
            if (tempBundleInfo != null) {
                clearApplyDir()

                val fromZipFile = tempBundleInfo.getTempZipFile(getTempDir(rootDir))
                val toZipFile = tempBundleInfo.getApplyZipFile(getApplyDir(rootDir))

                CXFileUtil.fileChannelCopy(fromZipFile, toZipFile)

                if (toZipFile.exists()) {
                    CXLogUtil.d(TAG, "copy bundle.zip to apply dir success")

                    val toUnzipDir = tempBundleInfo.getApplyUnzipDir(getApplyDir(rootDir))
                    CXZipUtil.unzipOrFalse(toZipFile, toUnzipDir)
                    if (checkUnzipDirValid(toUnzipDir)) {
                        CXLogUtil.d(TAG, "unzip bundle.zip success")

                        saveAppliedBundleInfo(tempBundleInfo)
                        clearTempDir()

                        reload()
                    } else {
                        CXLogUtil.e(TAG, "unzip bundle.zip failure")
                    }
                } else {
                    CXLogUtil.e(TAG, "copy bundle.zip to apply dir failure")
                }
            }

        }
    }

    fun checkUnzipDirValid(dir: File?): Boolean {
        val indexFileExists = File(dir, "index.android.bundle").exists()
        val valid = dir?.listFiles()?.isNotEmpty() == true && indexFileExists

        CXLogUtil.d(TAG, "checkUnzipDirValid=$valid, path=${dir?.absolutePath}, indexFileExists=$indexFileExists")
        return valid
    }

    fun checkZipFileValid(zipFile: File?): Boolean {
        val valid = zipFile?.exists() == true
        CXLogUtil.d(TAG, "checkZipFileValid=$valid, path=${zipFile?.absolutePath}")
        return valid
    }

    fun clearApplyDir() {
        CXLogUtil.d(TAG, "clearApplyDir")
        saveAppliedBundleInfo(null)
        val applyDir: File = getApplyDir(rootDir)
        CXFileUtil.deleteDirectory(applyDir)
        applyDir.mkdirs()
    }

    fun clearTempDir() {
        CXLogUtil.d(TAG, "clearTempDir")
        saveTempBundleInfo(null)
        val tempDir: File = getTempDir(rootDir)
        CXFileUtil.deleteDirectory(tempDir)
        tempDir.mkdirs()
    }

    /**
     * 合适的时候刷新 instance 加载的代码, 应用新的更新
     */
    fun reload() {
        if (!isRNRunningNow()) {
            CXLogUtil.d(TAG, "reload now ...")
        } else {
            CXLogUtil.e(TAG, "can't reload !")
        }
    }

    fun isReloadingNow(): Boolean {
        return false //todo
    }

    var reactNativeStartCount: Int = 0

    /**
     * 是否有rn页面被打开, 如果没有打开过, 可以立即应用热部署, 重新加载代码
     */
    fun isRNRunningNow() = reactNativeStartCount > 0

    private val KEY_BUNDLE_RN_APPLIED = "KEY_BUNDLE_RN_APPLIED"
    private val KEY_BUNDLE_RN_TEMP = "KEY_BUNDLE_RN_APPLIED"

    /**
     * tempZipFile 准备好后, 保存
     */
    private fun saveTempBundleInfo(bundleInfo: BundleInfo?) {
        CXPreferencesUtil.putEntity(KEY_BUNDLE_RN_TEMP, bundleInfo)
        CXLogUtil.d(TAG, "saveTempBundleInfo success, bundleInfo=$bundleInfo")
    }

    private fun getTempBundleInfo(): BundleInfo? {
        val bundleInfo: BundleInfo? = CXPreferencesUtil.getEntity(KEY_BUNDLE_RN_TEMP, BundleInfo::class.java)
        if (bundleInfo != null && bundleInfo.getTempZipFile(getTempDir(rootDir)).exists()) {
            CXLogUtil.d(TAG, "getTempBundleInfo success, bundleInfo=$bundleInfo")
            return bundleInfo
        } else {
            saveTempBundleInfo(null)
            CXLogUtil.e(TAG, "getTempBundleInfo failure")
            return null
        }
    }

    /**
     * 成功将 tempZipFile 应用后, 保存
     */
    private fun saveAppliedBundleInfo(bundleInfo: BundleInfo?) {
        CXPreferencesUtil.putEntity(KEY_BUNDLE_RN_APPLIED, bundleInfo)
        CXLogUtil.d(TAG, "saveAppliedBundleInfo success, bundleInfo=$bundleInfo")
    }

    private fun getAppliedBundleInfo(): BundleInfo? {
        val bundleInfo: BundleInfo? = CXPreferencesUtil.getEntity(KEY_BUNDLE_RN_APPLIED, BundleInfo::class.java)
        if (bundleInfo != null && checkUnzipDirValid(bundleInfo.getApplyUnzipDir(getApplyDir(rootDir)))) {
            CXLogUtil.d(TAG, "getAppliedBundleInfo success, bundleInfo=$bundleInfo")
            return bundleInfo
        } else {
            saveAppliedBundleInfo(null)
            CXLogUtil.e(TAG, "getAppliedBundleInfo failure")
            return null
        }
    }

}