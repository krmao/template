package com.smart.template.init

import android.app.Application
import com.facebook.imagepipeline.core.ImagePipelineConfig
import com.smart.library.base.CXBaseApplication
import com.smart.library.deploy.CXDeployManager
import com.smart.library.deploy.model.*
import com.smart.library.util.CXFileUtil
import com.smart.library.util.CXJsonUtil
import com.smart.library.util.CXLogUtil
import com.smart.template.handlers.OnRNCallNativeHandler
import com.smart.template.module.rn.ReactConstant
import com.smart.template.module.rn.ReactManager
import com.smart.template.repository.CXRepository
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.InputStream

@Suppress("LocalVariableName")
internal object CXDeployInitManager {

    @JvmStatic
    fun init(application: Application, frescoConfig: ImagePipelineConfig) {

        val TAG = CXDeployManager.REACT_NATIVE.TAG
        CXDeployManager.REACT_NATIVE.initialize(
                deployConfig = CXDeployConfigModel(
                        baseBundle = CXBundleInfo(ReactConstant.VERSION_RN_BASE),
                        baseBundlePathInAssets = "bundle-rn.zip",
                        checkUpdateHandler = {
                            CXLogUtil.d(TAG, "checkUpdateHandler invoke")
                            CXRepository.downloadString("http://10.47.62.17:7001/android/update").observeOn(Schedulers.io()).subscribe({ result: String ->
                                CXLogUtil.d(TAG, "checkUpdateHandler downloadString success $result")
                                val jsonObject = CXJsonUtil.toJSONObjectOrNull(result)
                                if (jsonObject != null) {
                                    val errorCode = jsonObject.optInt("errorCode", -1)
                                    if (errorCode == 0) {
                                        val resultObject = jsonObject.optJSONObject("result")
                                        val baseVersion = resultObject.optInt("baseVersion", -1)
                                        val toVersion = resultObject.optInt("toVersion", -1)
                                        val downloadUrl = resultObject.optString("downloadUrl")
                                        val bundleChecksum = resultObject.optString("bundleChecksum")
                                        if (baseVersion != -1 && toVersion != -1 && !downloadUrl.isNullOrBlank() && !bundleChecksum.isNullOrBlank()) {
                                            CXLogUtil.w(TAG, "check result is valid, adjust is need to download")
                                            it.invoke(null, CXPatchInfo(baseVersion, toVersion, bundleChecksum = bundleChecksum), downloadUrl, true)
                                            return@subscribe
                                        }
                                    }
                                    CXLogUtil.e(TAG, "checkUpdateHandler failure with error result ! $result")
                                } else {
                                    CXLogUtil.d(TAG, "checkUpdateHandler parse downloadString to json failure $result")
                                }

                                it.invoke(null, null, null, false)

                            }, { e: Throwable ->
                                CXLogUtil.e(TAG, "checkUpdateHandler downloadString failure  !", e)
                                it.invoke(null, null, null, false)
                            })
                            Unit
                        },
                        downloadHandler = { patchDownloadUrl: String?, file: File, downloadCallback: (file: File?) -> Unit ->
                            CXLogUtil.e(TAG, "downloadHandler invoke")
                            if (file.exists()) {
                                CXLogUtil.e(TAG, "downloadHandler file exists, no need to download, return null")
                                downloadCallback.invoke(null)
                            } else {
                                CXLogUtil.d(TAG, "downloadHandler start")
                                if (patchDownloadUrl != null && !patchDownloadUrl.isNullOrBlank()) {
                                    CXRepository.downloadFile(patchDownloadUrl)
                                            .observeOn(Schedulers.io()) //下载成功后也是异步处理，防止回滚等好性能操作阻塞UI
                                            .subscribe({ content: InputStream ->
                                                if (file.exists()) {
                                                    CXLogUtil.e(TAG, "downloadHandler file exists, return null")
                                                    downloadCallback.invoke(null)
                                                } else {
                                                    CXFileUtil.copy(content, file)
                                                    CXLogUtil.e(TAG, "downloadHandler success and copy dest file exists, return file")
                                                    downloadCallback.invoke(file)
                                                }
                                            }, { e: Throwable ->
                                                CXLogUtil.e(TAG, "downloadHandler error, return null", e)
                                                downloadCallback.invoke(null)
                                            })
                                }
                            }
                        },
                        reloadHandler = { indexBundleFile: File?, versionOfIndexBundleFileInSdcard: Int? ->
                            if (ReactManager.instanceManager != null) {
                                CXLogUtil.e(TAG, "reloadHandler start")
                                ReactManager.reloadBundleFromSdcard(indexBundleFile, versionOfIndexBundleFileInSdcard)
                                true
                            } else {
                                ReactManager.indexBundleFileInSdcard = indexBundleFile
                                ReactManager.versionOfIndexBundleFileInSdcard = versionOfIndexBundleFileInSdcard
                                CXLogUtil.e(TAG, "reloadHandler failure, instanceManager is null")
                                false
                            }
                        },
                        initCallback = { indexBundleFile: File?, versionOfIndexBundleFileInSdcard: Int? -> ReactManager.init(application, CXBaseApplication.DEBUG, indexBundleFile, versionOfIndexBundleFileInSdcard, frescoConfig, OnRNCallNativeHandler()) }
                ),
                checkUpdateTypes = mutableSetOf(CXDeployCheckUpdateType.APP_START, CXDeployCheckUpdateType.APP_FORGROUND_TO_BACKGROUND, CXDeployCheckUpdateType.APP_OPEN_FIRST_PAGE),
                applyTypes = mutableSetOf(CXDeployApplyType.APP_START, CXDeployApplyType.APP_CLOSE_ALL_PAGES, CXDeployApplyType.APP_OPEN_FIRST_PAGE)
        )
    }
}