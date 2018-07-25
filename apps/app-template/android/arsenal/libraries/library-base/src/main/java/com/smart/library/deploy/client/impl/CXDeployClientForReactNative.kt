@file:Suppress("unused")

package com.smart.library.deploy.client.impl

import com.smart.library.deploy.client.CXIDeployClient
import com.smart.library.deploy.model.CXDeployType
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
class CXDeployClientForReactNative(
        private val baseInfoOfBundle: CXBundleInfo,
        val rootDir: File = CXCacheManager.getFilesHotPatchReactNativeDir(),
        val pathInAssets: String,
        val checkHandler: ((bundleInfo: CXBundleInfo?, patchInfo: CXPatchInfo?, downloadUrl: String?, isPatch: Boolean) -> Unit?) -> Unit?,
        val downloadHandler: (patchDownloadUrl: String?, toFile: File, callback: (file: File) -> Unit) -> Unit?,
        val isRNOpenedHandler: () -> Boolean
) : CXIDeployClient {

    companion object {
        val TAG: String = "[rn-deploy]"
    }

    private val baseBundleHelper: CXBaseBundleHelper by lazy { CXBaseBundleHelper(baseInfoOfBundle, rootDir, pathInAssets) }
    private val preferenceManager: CXDeployPreferenceManager by lazy { CXDeployPreferenceManager(CXDeployType.REACT_NATIVE, rootDir) }

    private var reloadHandler: ((indexBundleFileInSdcard:File) -> Boolean)? = null
    /**
     * ensure base unzipDir valid
     */
    fun initialize(initCallback: (indexBundleFile: File?) -> Unit?, reloadHandler: (indexBundleFileInSdcard:File) -> Boolean) {
        CXLogUtil.w(TAG, "initialize start")
        isReadyForOpen = false

        // 初始化之前检查是否有可以应用的更新, 且不用回调 reload
        this.reloadHandler = null
        apply()
        this.reloadHandler = reloadHandler

        val indexFile = getIndexBundleFile()
        initCallback.invoke(indexFile)

        detectRNAllPagesClosedEvent()
    }

    private fun detectRNAllPagesClosedEvent() {
        RxBus.toObservable(CXRNAllPagesClosedEvent::class.java).subscribe {
            apply()
        }
    }

    private fun getIndexBundleFile(): File? {
        var finalIndexBundleFile: File? = null

        val appliedBundleInfo: CXBundleInfo? = preferenceManager.getAppliedBundleInfo()

        if (appliedBundleInfo != null) {
            val appliedBundleHelper = CXDeployBundleHelper(appliedBundleInfo, rootDir)
            val appliedIndexBundleFile = appliedBundleHelper.getIndexFile()
            if (appliedBundleHelper.checkUnzipDirValid() && appliedIndexBundleFile.exists()) {
                finalIndexBundleFile = appliedIndexBundleFile
            }
        }
        if (finalIndexBundleFile == null) {
            if (baseBundleHelper.checkUnzipDirValid() || ((baseBundleHelper.checkZipFileValid(baseBundleHelper.getBaseZipFile()) || copyBundleToSDCardFromAssets(baseBundleHelper.getBaseZipFile())) && CXZipUtil.unzipToDirOrFalse(baseBundleHelper.getBaseZipFile(), baseBundleHelper.getBaseUnzipDir()) && baseBundleHelper.checkUnzipDirValid())) {
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
        val copyAndCheckSuccess = CXFileUtil.copyFromAssets(baseBundleHelper.pathInAssets, baseZipFile) && baseBundleHelper.checkZipFileValid(baseZipFile)
        CXLogUtil.w(TAG, "copyBundleToSDCardFromAssets end, copyAndCheckSuccess=$copyAndCheckSuccess")
        return copyAndCheckSuccess
    }

    /**
     * 检查是否有需要处理的更新
     * bundle-rn-1.zip
     */
    fun check() {
        checkHandler.invoke { bundleInfo, patchInfo, downloadUrl, isPatch ->
            if (isPatch && patchInfo != null) {
                if (downloadUrl != null && (patchInfo.baseVersion == baseBundleHelper.info.version ?: 1) && patchInfo.toVersion > patchInfo.baseVersion) {
                    downloadPatch(CXPatchHelper(patchInfo, rootDir, preferenceManager), downloadUrl)
                }
            } else if (bundleInfo != null) {
                if (downloadUrl != null && bundleInfo.version ?: 0 > baseBundleHelper.info.version ?: 1) {
                    download(CXDeployBundleHelper(bundleInfo, rootDir), downloadUrl)
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
            if (patchHelper.checkPatchFileValid()) {
                if (patchHelper.merge(baseBundleHelper)) {
                    apply()
                }
            }
        }
    }

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
                    val deployBundleHelper = CXDeployBundleHelper(tempBundleInfo, rootDir)

                    if (deployBundleHelper.checkUnzipDirValid()) {
                        CXLogUtil.w(TAG, "apply checkUnzipDirValid(${deployBundleHelper.getApplyUnzipDir()}) success")

                        if (reloadHandler?.invoke(deployBundleHelper.getIndexFile()) == true) {
                            CXLogUtil.d(TAG, "apply reload success")

                            // remove old
                            val oldAppliedInfo: CXBundleInfo? = preferenceManager.getAppliedBundleInfo()
                            if (oldAppliedInfo != null) {
                                CXDeployBundleHelper(oldAppliedInfo, rootDir).clearApplyFiles()
                            }

                            // apply new
                            preferenceManager.saveAppliedBundleInfo(tempBundleInfo)
                            preferenceManager.saveTempBundleInfo(null)

                            CXLogUtil.d(TAG, "apply success")
                            return
                        } else {
                            CXLogUtil.e(TAG, "apply reload failure")
                        }
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
}