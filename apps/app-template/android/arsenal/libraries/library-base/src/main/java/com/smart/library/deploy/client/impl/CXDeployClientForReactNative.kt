@file:Suppress("unused")

package com.smart.library.deploy.client.impl

import com.smart.library.base.CXApplicationVisibleChangedEvent
import com.smart.library.deploy.CXDeployManager
import com.smart.library.deploy.CXDeployPreferenceManager
import com.smart.library.deploy.client.CXIDeployClient
import com.smart.library.deploy.model.*
import com.smart.library.util.CXFileUtil
import com.smart.library.util.CXLogUtil
import com.smart.library.util.CXZipUtil
import com.smart.library.util.rx.RxBus
import com.smart.library.deploy.model.CXBaseBundleHelper
import com.smart.library.deploy.model.CXBundleInfo
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
internal class CXDeployClientForReactNative(
        private val debug: Boolean,
        private val rootDir: File,
        private val deployConfig: CXDeployConfigModel,
        private val TAG: String = CXDeployManager.REACT_NATIVE.TAG
) : CXIDeployClient {

    override fun getRootDir(): File = this.rootDir

    private val baseBundleHelper: CXBaseBundleHelper by lazy { CXBaseBundleHelper(debug, deployConfig.baseBundle, rootDir, deployConfig.baseBundlePathInAssets, TAG) }
    private val preferenceManager: CXDeployPreferenceManager by lazy { CXDeployManager.REACT_NATIVE.preferenceManager }

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

    private fun getLatestValidAppliedBundleHelper(): CXIBundleHelper? {
        val appliedBundleInfo: CXBundleInfo? = preferenceManager.getAppliedBundleInfo()

        if (appliedBundleInfo != null) {
            val appliedBundleHelper = CXDeployBundleHelper(debug, appliedBundleInfo, rootDir, TAG)
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


    private var isChecking = false
    /**
     * 检查是否有需要处理的更新
     * bundle-rn-1.zip
     */
    override fun checkUpdate(checkUpdateCallback: CXIDeployCheckUpdateCallback?) {
        CXLogUtil.e(TAG, "checkUpdate beginning -->")
        //todo 临时处理, 上一次更新没处理结束, 禁止开始下一次更新, 多线程问题
        if (isChecking) {
            CXLogUtil.e(TAG, "checkUpdate, last checking is not completely")
            checkUpdateCallback?.onCheckUpdateCallback(false)
            checkUpdateCallback?.onDownloadCallback(false)
            checkUpdateCallback?.onMergePatchCallback(false)
            checkUpdateCallback?.onApplyCallback(false)
            return
        }
        isChecking = true

        deployConfig.checkUpdateHandler.invoke { bundleInfo, patchInfo, downloadUrl, isPatch ->
            val latestValidAppliedBundleHelper = getLatestValidAppliedBundleHelper()
            CXLogUtil.w(TAG, "isPatch=$isPatch, baseBundleInfo   = ${baseBundleHelper.info}")
            CXLogUtil.w(TAG, "isPatch=$isPatch, latestValidAppliedBundleInfo   = ${latestValidAppliedBundleHelper?.info}")
            if (isPatch && patchInfo != null) {
                CXLogUtil.w(TAG, "isPatch=$isPatch, remotePatchInfo = $patchInfo")
                val latestValidAppliedBundleVersion = latestValidAppliedBundleHelper?.info?.version ?: patchInfo.baseVersion
                CXLogUtil.e(TAG, "isPatch=$isPatch, latestValidAppliedBundleVersion=$latestValidAppliedBundleVersion, remoteVersion=${patchInfo.toVersion} ")
                if (downloadUrl != null && (patchInfo.baseVersion == baseBundleHelper.info.version) && patchInfo.toVersion > latestValidAppliedBundleVersion) {
                    checkUpdateCallback?.onCheckUpdateCallback(true)
                    val patchHelper = CXPatchHelper(CXDeployManager.REACT_NATIVE, patchInfo)
                    if (!patchHelper.checkZipFileValid(patchHelper.getApplyZipFile()) && !patchHelper.checkUnzipDirValid()) {

                        if (!patchHelper.checkTempBundleFileValid()) {
                            if (!patchHelper.checkPatchFileValid()) {
                                CXLogUtil.w(TAG, "checkPatchFileValid invalid, start download patch")
                                downloadPatch(CXPatchHelper(CXDeployManager.REACT_NATIVE, patchInfo), downloadUrl,
                                        downloadCallback = {
                                            checkUpdateCallback?.onDownloadCallback(it)
                                        },
                                        mergeCallback = {
                                            checkUpdateCallback?.onMergePatchCallback(it)
                                        },
                                        applyCallback = {
                                            checkUpdateCallback?.onApplyCallback(it)
                                        }
                                )
                            } else {
                                CXLogUtil.e(TAG, "patch file is valid now, no need to download, need merge and copy to apply")
                                checkUpdateCallback?.onDownloadCallback(true)
                                merge(patchHelper,
                                        mergeCallback = {
                                            checkUpdateCallback?.onMergePatchCallback(it)
                                        },
                                        applyCallback = {
                                            checkUpdateCallback?.onApplyCallback(it)
                                        })
                                isChecking = false
                            }
                        } else {
                            CXLogUtil.e(TAG, "checkTempBundleFileValid is valid now, no need to download, need copy to apply")
                            checkUpdateCallback?.onDownloadCallback(true)
                            merge(patchHelper,
                                    mergeCallback = {
                                        checkUpdateCallback?.onMergePatchCallback(it)
                                    },
                                    applyCallback = {
                                        checkUpdateCallback?.onApplyCallback(it)
                                    })
                            isChecking = false
                        }
                    } else {
                        isChecking = false
                        CXLogUtil.e(TAG, "apply zip and unzip files is valid now, no need to download")
                        checkUpdateCallback?.onDownloadCallback(true)
                        checkUpdateCallback?.onMergePatchCallback(true)
                        tryApply { checkUpdateCallback?.onApplyCallback(it) }
                    }
                } else {
                    isChecking = false
                    CXLogUtil.e(TAG, "checkUpdate version is no need to download")
                    checkUpdateCallback?.onCheckUpdateCallback(false)
                    checkUpdateCallback?.onDownloadCallback(false)
                    checkUpdateCallback?.onMergePatchCallback(false)
                    checkUpdateCallback?.onApplyCallback(false)
                }
            } else if (bundleInfo != null) {
                CXLogUtil.w(TAG, "isPatch=$isPatch, remoteBundleInfo = $bundleInfo")
                // todo not complete
                if (downloadUrl != null && bundleInfo.version > baseBundleHelper.info.version) {
                    val deployBundleHelper = CXDeployBundleHelper(debug, bundleInfo, rootDir, TAG)
                    if (!deployBundleHelper.checkZipFileValid(deployBundleHelper.getTempZipFile()) && !deployBundleHelper.checkUnzipDirValid()) {
                        download(CXDeployBundleHelper(debug, bundleInfo, rootDir, TAG), downloadUrl)
                    } else {
                        CXLogUtil.e(TAG, "apply zip and unzip files is valid now, no need to download")
                    }
                } else {
                    CXLogUtil.e(TAG, "checkUpdate version is no need to download")
                }
            } else {
                checkUpdateCallback?.onCheckUpdateCallback(false)
                checkUpdateCallback?.onDownloadCallback(false)
                checkUpdateCallback?.onMergePatchCallback(false)
                checkUpdateCallback?.onApplyCallback(false)
            }
        }
    }

    override fun checkUpdate() {
        checkUpdate(null)
    }

    /**
     * 下载新的更新数据
     */
    fun download(deployBundleHelper: CXDeployBundleHelper, downloadUrl: String, applyCallback: ((applySuccess: Boolean) -> Unit?)? = null) {
        deployConfig.downloadHandler.invoke(downloadUrl, deployBundleHelper.getTempZipFile()) { bundleFile: File? ->
            if (deployBundleHelper.checkZipFileValid(bundleFile)) {
                preferenceManager.saveTempBundleInfo(deployBundleHelper.info)
                tryApply(applyCallback)
            } else {
                applyCallback?.invoke(false)
            }
        }
    }

    /**
     * 下载新的更新数据
     */
    fun downloadPatch(patchHelper: CXPatchHelper, downloadUrl: String, downloadCallback: ((applySuccess: Boolean) -> Unit?)? = null, mergeCallback: ((mergeSuccess: Boolean) -> Unit?)? = null, applyCallback: ((applySuccess: Boolean) -> Unit?)? = null) {
        CXLogUtil.e(TAG, "downloadHandler invoke start")
        deployConfig.downloadHandler.invoke(downloadUrl, patchHelper.getTempPatchFile()) { patchFile: File? ->
            CXLogUtil.e(TAG, "downloadHandler invoke end, patchFile=${patchFile?.absolutePath}")
            if (patchFile?.exists() == true) {
                CXLogUtil.e(TAG, "downloadPatch success, start merge ${patchFile.absolutePath}")
                downloadCallback?.invoke(true)
                merge(patchHelper, mergeCallback, applyCallback)
                isChecking = false
            } else {
                CXLogUtil.e(TAG, "downloadPatch failure, end checkUpdate")
                isChecking = false
                downloadCallback?.invoke(false)
                mergeCallback?.invoke(false)
                applyCallback?.invoke(false)
            }
        }
    }

    fun merge(patchHelper: CXPatchHelper, mergeCallback: ((mergeSuccess: Boolean) -> Unit?)? = null, applyCallback: ((applySuccess: Boolean) -> Unit?)? = null) {
        CXLogUtil.e(TAG, "merge, tempPatchFilePath=${patchHelper.getTempPatchFile().absolutePath}")
        if (patchHelper.merge(baseBundleHelper)) {
            mergeCallback?.invoke(true)
            tryApply(applyCallback)
        } else {
            mergeCallback?.invoke(false)
            applyCallback?.invoke(false)
        }
    }

    override fun tryApply() {
        tryApply(null)
    }

    /**
     * 在合适的实际删除老的更新数据, 并拷贝新的更新数据到目标位置
     * @param only apply valid tempInfo, return success:true
     */
    @Synchronized
    override fun tryApply(applyCallback: ((applySuccess: Boolean) -> Unit?)?) {
        CXLogUtil.d(TAG, "tryApply ...")
        if (CXDeployManager.REACT_NATIVE.isAllPagesClosed()) {
            CXLogUtil.e(TAG, "apply rn didn't opened, apply start")

            synchronized(this) {

                val tempBundleInfo: CXBundleInfo? = preferenceManager.getTempBundleInfo()
                if (tempBundleInfo != null) {
                    val deployBundleHelper = CXDeployBundleHelper(debug, tempBundleInfo, rootDir, TAG)

                    if (deployBundleHelper.checkUnzipDirValid()) {
                        CXLogUtil.w(TAG, "apply checkUnzipDirValid(${deployBundleHelper.getApplyUnzipDir()}) success")

                        deployConfig.reloadHandler.invoke(deployBundleHelper.getIndexFile(), deployBundleHelper.info.version)

                        CXLogUtil.d(TAG, "apply reload success")

                        // remove old
                        val oldAppliedInfo: CXBundleInfo? = preferenceManager.getAppliedBundleInfo()

                        CXLogUtil.d(TAG, "apply tempBundleInfo:$tempBundleInfo")
                        CXLogUtil.d(TAG, "apply oldAppliedInfo:$oldAppliedInfo")

                        if (oldAppliedInfo != null && oldAppliedInfo.version != tempBundleInfo.version) {
                            CXLogUtil.d(TAG, "apply clearOldApplyFiles")
                            CXDeployBundleHelper(debug, oldAppliedInfo, rootDir, TAG).clearApplyFiles()
                        } else {
                            CXLogUtil.d(TAG, "apply oldAppliedInfo == null, or oldAppliedInfo.version == tempBundleInfo.version, just save")
                        }

                        // apply new
                        preferenceManager.saveAppliedBundleInfo(tempBundleInfo)
                        preferenceManager.saveTempBundleInfo(null)

                        CXLogUtil.d(TAG, "apply success")
                        applyCallback?.invoke(true)
                    } else {
                        preferenceManager.saveTempBundleInfo(null)
                        CXLogUtil.e(TAG, "apply checkUnzipDirValid(${deployBundleHelper.getApplyUnzipDir()}) failure, clear temp info")
                        applyCallback?.invoke(false)
                    }
                } else {
                    CXLogUtil.e(TAG, "apply getTempBundleInfo failure")
                    applyCallback?.invoke(false)
                }

            }
        } else {
            CXLogUtil.e(TAG, "apply rn did opened, apply cancel")
            applyCallback?.invoke(false)
        }
    }

    init {
        CXLogUtil.w(TAG, "initialize start")
        isReadyForOpen = false
        tryApply()

        val validBundleHelper = getLatestValidAppliedBundleHelper()

        deployConfig.initCallback.invoke(validBundleHelper?.getIndexFile(), validBundleHelper?.info?.version)
        RxBus.toObservable(CXApplicationVisibleChangedEvent::class.java).subscribe {
            if (!it.isApplicationVisible) {
                CXLogUtil.e(TAG, "detect isApplicationVisible=false, start checkUpdate")
                checkUpdate()
            }
        }
    }
}