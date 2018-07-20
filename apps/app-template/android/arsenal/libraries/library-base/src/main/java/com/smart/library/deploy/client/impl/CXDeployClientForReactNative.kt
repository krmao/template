@file:Suppress("unused")

package com.smart.library.deploy.client.impl

import com.mlibrary.util.bspatch.MBSPatchUtil
import com.smart.library.deploy.client.CXIDeployClient
import com.smart.library.deploy.model.CXDeployType
import com.smart.library.deploy.model.bundle.CXBaseBundleHelper
import com.smart.library.deploy.model.bundle.CXBundleInfo
import com.smart.library.deploy.model.bundle.CXDeployBundleHelper
import com.smart.library.deploy.model.bundle.CXIBundleHelper
import com.smart.library.deploy.preference.CXDeployPreferenceManager
import com.smart.library.util.CXFileUtil
import com.smart.library.util.CXLogUtil
import com.smart.library.util.CXZipUtil
import com.smart.library.util.cache.CXCacheManager
import java.io.File

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
@Suppress("PrivatePropertyName", "MemberVisibilityCanBePrivate")
class CXDeployClientForReactNative(
        private val baseInfoOfBundle: CXBundleInfo,
        val rootDir: File = CXCacheManager.getFilesHotPatchReactNativeDir(),
        val checkHandler: ((bundleInfo: CXBundleInfo?, patchDownloadUrl: String?) -> Unit?) -> Unit?,
        val downloadHandler: (patchDownloadUrl: String?, callback: (file: File?) -> Unit) -> Unit?
) : CXIDeployClient {

    companion object {
        private val TAG: String = "[rn-deploy]"
    }

    private val bsPatchUtil: MBSPatchUtil by lazy { MBSPatchUtil() }
    private val baseBundleHelper: CXBaseBundleHelper by lazy { CXBaseBundleHelper(baseInfoOfBundle, rootDir) }
    private val preferenceManager: CXDeployPreferenceManager by lazy { CXDeployPreferenceManager(CXDeployType.REACT_NATIVE, rootDir) }

    /**
     * ensure base unzipDir valid
     */
    fun initialize(callback: (indexBundleFile: File?) -> Unit) {
        CXLogUtil.w(TAG, "initialize start")
        callback.invoke(getIndexBundleFile())
    }

    private fun getIndexBundleFile(): File? {
        var finalIndexBundleFile: File? = null

        val appliedBundleInfo: CXBundleInfo? = preferenceManager.getAppliedBundleInfo()
        if (appliedBundleInfo != null) {
            val appliedIndexBundleFile = CXDeployBundleHelper(appliedBundleInfo, rootDir).getIndexFile()
            if (appliedIndexBundleFile.exists()) {
                finalIndexBundleFile = appliedIndexBundleFile
            }
        }
        if (finalIndexBundleFile == null) {
            if (baseBundleHelper.checkUnzipDirValid() || ((baseBundleHelper.checkZipFileValid(baseBundleHelper.getBaseZipFile()) || copyBundleToSDCardFromAssets(baseBundleHelper.getBaseZipFile())) && unzipToDir(baseBundleHelper.getBaseZipFile(), baseBundleHelper.getBaseUnzipDir(), baseBundleHelper))) {
                finalIndexBundleFile = baseBundleHelper.getIndexFile()
            }
        }
        return finalIndexBundleFile
    }

    /**
     * 将 assets 中的基础数据copy并解压到 sdcard
     */
    fun copyBundleToSDCardFromAssets(baseZipFile: File = baseBundleHelper.getBaseZipFile()): Boolean {
        CXLogUtil.w(TAG, "copyBundleToSDCardFromAssets start")
        CXFileUtil.deleteFile(baseZipFile)
        val copyAndCheckSuccess = CXFileUtil.copyFromAssets(baseBundleHelper.info.fullName, baseZipFile) && baseBundleHelper.checkZipFileValid(baseZipFile)
        CXLogUtil.w(TAG, "copyBundleToSDCardFromAssets end, copyAndCheckSuccess=$copyAndCheckSuccess")
        return copyAndCheckSuccess
    }

    /**
     * 解压 bundle 到指定文件夹
     */
    fun unzipToDir(zipFile: File, unZipDir: File?, helper: CXIBundleHelper): Boolean {
        CXLogUtil.w(TAG, "unzipToDir start")
        val unzipAndCheckSuccess = CXZipUtil.unzipOrFalse(zipFile, unZipDir) && helper.checkUnzipDirValid()
        CXLogUtil.w(TAG, "unzipToDir end, unzipAndCheckSuccess=$unzipAndCheckSuccess")
        return unzipAndCheckSuccess
    }

    /**
     * 检查是否有需要处理的更新
     * bundle-rn-1.zip
     */
    fun check() {
        checkHandler.invoke { bundleInfo, patchDownloadUrl ->
            if (bundleInfo != null && patchDownloadUrl != null && bundleInfo.version > baseBundleHelper.info.version) {
                download(CXDeployBundleHelper(bundleInfo, rootDir), patchDownloadUrl)
            }
        }
    }

    /**
     * 下载新的更新数据
     */
    fun download(deployBundleHelper: CXDeployBundleHelper, patchDownloadUrl: String) {
        downloadHandler.invoke(patchDownloadUrl) { patchFile: File? ->
            if (patchFile != null && patchFile.exists()) {
                val isNeedMerge = true //TODO

                @Suppress("ConstantConditionIf")
                if (isNeedMerge) {
                    val tempZipFile = deployBundleHelper.getTempZipFile()
                    merge(patchFile, tempZipFile)
                    if (tempZipFile.exists()) {
                        preferenceManager.saveTempBundleInfo(deployBundleHelper.info)
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
            bsPatchUtil.bspatch(baseBundleHelper.getBaseZipFile().absolutePath, toFile.absolutePath, patchFile.absolutePath)
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
            val tempBundleInfo: CXBundleInfo? = preferenceManager.getTempBundleInfo()
            if (tempBundleInfo != null) {
                val deployBundleHelper = CXDeployBundleHelper(tempBundleInfo, rootDir)

                deployBundleHelper.clearApplyDir()
                preferenceManager.saveAppliedBundleInfo(null)

                val fromZipFile = deployBundleHelper.getTempZipFile()
                val toZipFile = deployBundleHelper.getApplyZipFile()

                CXFileUtil.fileChannelCopy(fromZipFile, toZipFile)

                if (toZipFile.exists()) {
                    CXLogUtil.d(TAG, "copy bundle.zip to apply dir success")

                    val toUnzipDir = deployBundleHelper.getApplyUnzipDir()
                    CXZipUtil.unzipOrFalse(toZipFile, toUnzipDir)

                    if (unzipToDir(toZipFile, toUnzipDir, deployBundleHelper)) {
                        CXLogUtil.d(TAG, "unzip bundle.zip success")

                        preferenceManager.saveAppliedBundleInfo(tempBundleInfo)
                        deployBundleHelper.clearTempDir()
                        preferenceManager.saveTempBundleInfo(null)

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


}