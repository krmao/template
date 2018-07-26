@file:Suppress("unused")

package com.smart.library.deploy.client.impl

import com.smart.library.base.CXApplicationVisibleChangedEvent
import com.smart.library.deploy.CXDeployManager
import com.smart.library.deploy.client.CXIDeployClient
import com.smart.library.deploy.model.bundle.*
import com.smart.library.deploy.preference.CXDeployPreferenceManager
import com.smart.library.util.CXFileUtil
import com.smart.library.util.CXLogUtil
import com.smart.library.util.CXZipUtil
import com.smart.library.util.cache.CXCacheManager
import com.smart.library.util.rx.RxBus
import java.io.File
import java.util.*

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
open class CXDeployClientForReactNative(
        val debug: Boolean,
        val baseInfoOfBundle: CXBundleInfo,
        val rootDir: File = CXCacheManager.getFilesHotPatchReactNativeDir(),
        val pathInAssets: String,
        val checkHandler: ((bundleInfo: CXBundleInfo?, patchInfo: CXPatchInfo?, downloadUrl: String?, isPatch: Boolean) -> Unit?) -> Unit?,
        val downloadHandler: (patchDownloadUrl: String?, toFile: File, callback: (file: File) -> Unit) -> Unit?,
        val reloadHandler: (indexBundleFileInSdcard: File) -> Boolean,
        val isRNOpenedHandler: () -> Boolean,
        val initCallback: (indexBundleFile: File?) -> Unit?
) : CXIDeployClient {

    companion object {
        const val TAG: String = "[rn-deploy]"
    }

    private val baseBundleHelper: CXBaseBundleHelper by lazy { CXBaseBundleHelper(debug, baseInfoOfBundle, rootDir, pathInAssets) }
    private val preferenceManager: CXDeployPreferenceManager by lazy { CXDeployPreferenceManager(CXDeployManager.REACT_NATIVE, rootDir) }

    private val onReadyCallbackList: Vector<() -> Unit?> = Vector()
    @Volatile
    private var isReadyForOpen: Boolean = false
        private set(value) {
            field = value
            if (value) {
                synchronized(isReadyForOpen) {
                    onReadyCallbackList.forEach { it.invoke() }
                    onReadyCallbackList.clear()
                }
            }
        }

    fun checkIsReadyForLoad(onReadyCallback: () -> Unit?) {
        if (isReadyForOpen) {
            onReadyCallback.invoke()
        } else {
            synchronized(isReadyForOpen) {
                onReadyCallbackList.add(onReadyCallback)
            }
        }
    }

    private fun detectRNAllPagesClosedEvent() {
        RxBus.toObservable(CXRNAllPagesClosedEvent::class.java).subscribe {
            apply()
        }
    }

    private fun getLatestValidAppliedBundleHelper(): CXIBundleHelper? {
        val appliedBundleInfo: CXBundleInfo? = preferenceManager.getAppliedBundleInfo()

        if (appliedBundleInfo != null) {
            val appliedBundleHelper = CXDeployBundleHelper(debug, appliedBundleInfo, rootDir)
            if (appliedBundleHelper.checkUnzipDirValid()) {
                return appliedBundleHelper
            }
        }
        if (baseBundleHelper.checkUnzipDirValid() || ((baseBundleHelper.checkZipFileValid(baseBundleHelper.getBaseZipFile()) || copyBundleToSDCardFromAssets(baseBundleHelper.getBaseZipFile())) && CXZipUtil.unzipToDirOrFalse(baseBundleHelper.getBaseZipFile(), baseBundleHelper.getBaseUnzipDir()) && baseBundleHelper.checkUnzipDirValid())) {
            return baseBundleHelper
        }
        return null
    }

    private fun getIndexBundleFile(): File? {
        return getLatestValidAppliedBundleHelper()?.getIndexFile()
    }

    /**
     * 将 assets 中的基础数据copy并解压到 sdcard
     */
    fun copyBundleToSDCardFromAssets(baseZipFile: File = baseBundleHelper.getBaseZipFile()): Boolean {
        CXLogUtil.w(TAG, "copyBundleToSDCardFromAssets start")
        CXFileUtil.deleteFile(baseZipFile)
        val copyAndCheckSuccess = CXFileUtil.copyFromAssets(baseBundleHelper.pathInAssets, baseZipFile) && baseBundleHelper.checkZipFileValid(baseZipFile)
        CXLogUtil.w(TAG, "copyBundleToSDCardFromAssets end, copyAndCheckSuccess=$copyAndCheckSuccess")
        return copyAndCheckSuccess
    }

    /**
     * 检查是否有需要处理的更新
     * bundle-rn-1.zip
     */
    override fun check() {
        checkHandler.invoke { bundleInfo, patchInfo, downloadUrl, isPatch ->
            val latestValidAppliedBundleHelper = getLatestValidAppliedBundleHelper()
            CXLogUtil.w(TAG, "isPatch=$isPatch, baseBundleInfo   = ${baseBundleHelper.info}")
            CXLogUtil.w(TAG, "isPatch=$isPatch, latestValidAppliedBundleInfo   = ${latestValidAppliedBundleHelper?.info}")
            if (isPatch && patchInfo != null) {
                CXLogUtil.w(TAG, "isPatch=$isPatch, remotePatchInfo = $patchInfo")
                val latestValidAppliedBundleVersion = latestValidAppliedBundleHelper?.info?.version ?: patchInfo.baseVersion
                CXLogUtil.e(TAG, "isPatch=$isPatch, latestValidAppliedBundleVersion=$latestValidAppliedBundleVersion, remoteVersion=${patchInfo.toVersion} ")
                if (downloadUrl != null && (patchInfo.baseVersion == baseBundleHelper.info.version) && patchInfo.toVersion > latestValidAppliedBundleVersion) {
                    val patchHelper = CXPatchHelper(debug, patchInfo, rootDir, preferenceManager)
                    if (!patchHelper.checkZipFileValid(patchHelper.getApplyZipFile()) && !patchHelper.checkUnzipDirValid()) {

                        if (!patchHelper.checkTempBundleFileValid()) {
                            if (!patchHelper.checkPatchFileValid()) {
                                CXLogUtil.w(TAG, "checkPatchFileValid invalid, start download patch")
                                downloadPatch(CXPatchHelper(debug, patchInfo, rootDir, preferenceManager), downloadUrl)
                            } else {
                                CXLogUtil.e(TAG, "patch file is valid now, no need to download, need merge and copy to apply")
                                merge(patchHelper)
                            }
                        } else {
                            CXLogUtil.e(TAG, "checkTempBundleFileValid is valid now, no need to download, need copy to apply")
                            merge(patchHelper)
                        }
                    } else {
                        CXLogUtil.e(TAG, "apply zip and unzip files is valid now, no need to download")
                    }
                } else {
                    CXLogUtil.e(TAG, "check version is no need to download")
                }
            } else if (bundleInfo != null) {
                CXLogUtil.w(TAG, "isPatch=$isPatch, remoteBundleInfo = $bundleInfo")
                // todo not complete
                if (downloadUrl != null && bundleInfo.version > baseBundleHelper.info.version) {
                    val deployBundleHelper = CXDeployBundleHelper(debug, bundleInfo, rootDir)
                    if (!deployBundleHelper.checkZipFileValid(deployBundleHelper.getTempZipFile()) && !deployBundleHelper.checkUnzipDirValid()) {
                        download(CXDeployBundleHelper(debug, bundleInfo, rootDir), downloadUrl)
                    } else {
                        CXLogUtil.e(TAG, "apply zip and unzip files is valid now, no need to download")
                    }
                } else {
                    CXLogUtil.e(TAG, "check version is no need to download")
                }
            }
        }
    }

    /**
     * 下载新的更新数据
     */
    fun download(deployBundleHelper: CXDeployBundleHelper, downloadUrl: String) {
        downloadHandler.invoke(downloadUrl, deployBundleHelper.getTempZipFile()) { bundleFile: File? ->
            if (deployBundleHelper.checkZipFileValid(bundleFile)) {
                preferenceManager.saveTempBundleInfo(deployBundleHelper.info)
                apply()
            }
        }
    }

    /**
     * 下载新的更新数据
     */
    fun downloadPatch(patchHelper: CXPatchHelper, downloadUrl: String) {
        downloadHandler.invoke(downloadUrl, patchHelper.getTempPatchFile()) { patchFile: File? ->
            merge(patchHelper)
        }
    }

    fun merge(patchHelper: CXPatchHelper) {
        CXLogUtil.e(TAG, "merge")
        if (patchHelper.merge(baseBundleHelper)) {
            apply()
        }
    }

    /**
     * 在合适的实际删除老的更新数据, 并拷贝新的更新数据到目标位置
     */
    @Synchronized
    fun apply() {
        CXLogUtil.d(TAG, "apply check start")
        if (!isRNOpenedHandler.invoke()) {
            CXLogUtil.e(TAG, "apply rn didn't opened, apply start")

            synchronized(this) {

                val tempBundleInfo: CXBundleInfo? = preferenceManager.getTempBundleInfo()
                if (tempBundleInfo != null) {
                    val deployBundleHelper = CXDeployBundleHelper(debug, tempBundleInfo, rootDir)

                    if (deployBundleHelper.checkUnzipDirValid()) {
                        CXLogUtil.w(TAG, "apply checkUnzipDirValid(${deployBundleHelper.getApplyUnzipDir()}) success")

                        reloadHandler.invoke(deployBundleHelper.getIndexFile())

                        CXLogUtil.d(TAG, "apply reload success")

                        // remove old
                        val oldAppliedInfo: CXBundleInfo? = preferenceManager.getAppliedBundleInfo()

                        CXLogUtil.d(TAG, "apply tempBundleInfo:$tempBundleInfo")
                        CXLogUtil.d(TAG, "apply oldAppliedInfo:$oldAppliedInfo")

                        if (oldAppliedInfo != null && oldAppliedInfo.version != tempBundleInfo.version) {
                            CXLogUtil.d(TAG, "apply clearOldApplyFiles")
                            CXDeployBundleHelper(debug, oldAppliedInfo, rootDir).clearApplyFiles()
                        } else {
                            CXLogUtil.d(TAG, "apply oldAppliedInfo == null, or oldAppliedInfo.version == tempBundleInfo.version, just save")
                        }

                        // apply new
                        preferenceManager.saveAppliedBundleInfo(tempBundleInfo)
                        preferenceManager.saveTempBundleInfo(null)

                        CXLogUtil.d(TAG, "apply success")
                    } else {
                        preferenceManager.saveTempBundleInfo(null)
                        CXLogUtil.e(TAG, "apply checkUnzipDirValid(${deployBundleHelper.getApplyUnzipDir()}) failure, clear temp info")
                    }
                } else {
                    CXLogUtil.e(TAG, "apply getTempBundleInfo failure")
                }

            }
        } else {
            CXLogUtil.e(TAG, "apply rn did opened, apply cancel")
        }
    }

    init {
        CXLogUtil.w(TAG, "initialize start")
        isReadyForOpen = false
        apply()
        val indexFile = getIndexBundleFile()
        initCallback.invoke(indexFile)
        detectRNAllPagesClosedEvent()
        RxBus.toObservable(CXApplicationVisibleChangedEvent::class.java).subscribe {
            if (!it.isApplicationVisible) {
                check()
            }
        }
    }
}