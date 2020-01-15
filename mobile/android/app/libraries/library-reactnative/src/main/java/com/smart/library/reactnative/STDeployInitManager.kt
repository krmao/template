package com.smart.library.reactnative

import android.app.Application
import com.facebook.imagepipeline.core.ImagePipelineConfig
import com.smart.library.base.STBaseApplication
import com.smart.library.deploy.STDeployManager
import com.smart.library.deploy.model.STBundleInfo
import com.smart.library.deploy.model.STDeployApplyType
import com.smart.library.deploy.model.STDeployConfigModel
import com.smart.library.deploy.model.STPatchInfo
import com.smart.library.util.STJsonUtil
import com.smart.library.util.STLogUtil
import com.smart.library.util.okhttp.STOkHttpManager
import java.io.File

@Suppress("LocalVariableName")
internal object STDeployInitManager {

    @JvmStatic
    fun init(application: Application, frescoConfig: ImagePipelineConfig) {

        val TAG = STDeployManager.REACT_NATIVE.TAG
        STDeployManager.REACT_NATIVE.initialize(
                deployConfig = STDeployConfigModel(
                        baseBundle = STBundleInfo(ReactConstant.VERSION_RN_BASE),
                        baseBundlePathInAssets = "bundle-rn.zip",
                        checkUpdateHandler = {
                            STLogUtil.d(TAG, "checkUpdateHandler invoke")
                            STOkHttpManager.doGet(url = "http://10.47.62.17:7001/android/update", readTimeoutMS = 30 * 1000, callback = { result: String? ->
                                STLogUtil.d(TAG, "checkUpdateHandler downloadString success $result")
                                val jsonObject = STJsonUtil.toJSONObjectOrNull(result)
                                if (jsonObject != null) {
                                    val errorCode = jsonObject.optInt("errorCode", -1)
                                    if (errorCode == 0) {
                                        val resultObject = jsonObject.optJSONObject("result")
                                        val baseVersion = resultObject.optInt("baseVersion", -1)
                                        val toVersion = resultObject.optInt("toVersion", -1)
                                        val downloadUrl = resultObject.optString("downloadUrl")
                                        val bundleChecksum = resultObject.optString("bundleChecksum")
                                        if (baseVersion != -1 && toVersion != -1 && !downloadUrl.isNullOrBlank() && !bundleChecksum.isNullOrBlank()) {
                                            STLogUtil.w(TAG, "check result is valid, adjust is need to download")
                                            it.invoke(null, STPatchInfo(baseVersion, toVersion, bundleChecksum = bundleChecksum), downloadUrl, true)
                                            return@doGet
                                        }
                                    }
                                    STLogUtil.e(TAG, "checkUpdateHandler failure with error result ! $result")
                                } else {
                                    STLogUtil.d(TAG, "checkUpdateHandler parse downloadString to json failure $result")
                                }
                                it.invoke(null, null, null, false)

                            })
                        },
                        downloadHandler = { patchDownloadUrl: String?, file: File, downloadCallback: (file: File?) -> Unit ->
                            STLogUtil.e(TAG, "downloadHandler invoke")
                            if (file.exists()) {
                                STLogUtil.e(TAG, "downloadHandler file exists, no need to download, return null")
                                downloadCallback.invoke(null)
                            } else {
                                STLogUtil.d(TAG, "downloadHandler start")
                                if (patchDownloadUrl != null && !patchDownloadUrl.isNullOrBlank()) {
                                    STOkHttpManager.doGetFile(patchDownloadUrl, file.absolutePath) { toFile ->
                                        if (toFile?.exists() == true) {
                                            STLogUtil.e(TAG, "downloadHandler success and copy dest file exists, return file")
                                            downloadCallback.invoke(file)
                                        } else {
                                            STLogUtil.e(TAG, "downloadHandler file failure, return null")
                                            downloadCallback.invoke(null)
                                        }
                                    }
                                }
                            }
                        },
                        reloadHandler = { indexBundleFile: File?, versionOfIndexBundleFileInSdcard: Int? ->
                            if (ReactManager.instanceManager != null) {
                                STLogUtil.e(TAG, "reloadHandler start")
                                ReactManager.reloadBundle(indexBundleFile, versionOfIndexBundleFileInSdcard)
                                true
                            } else {
                                ReactManager.indexBundleFileInSdcard = indexBundleFile
                                ReactManager.versionOfIndexBundleFileInSdcard = versionOfIndexBundleFileInSdcard
                                STLogUtil.e(TAG, "reloadHandler failure, instanceManager is null")
                                false
                            }
                        },
                        initCallback = { indexBundleFile: File?, versionOfIndexBundleFileInSdcard: Int? ->
                            STLogUtil.e(ReactManager.TAG, "initCallback start")
                            ReactManager.init(application, STBaseApplication.DEBUG, indexBundleFile, versionOfIndexBundleFileInSdcard, frescoConfig, OnRNCallNativeHandler())
                            STLogUtil.e(ReactManager.TAG, "initCallback end")
                        }
                ),
                // checkUpdateTypes = mutableSetOf(STDeployCheckUpdateType.APP_START, STDeployCheckUpdateType.APP_FORGROUND_TO_BACKGROUND, STDeployCheckUpdateType.APP_OPEN_FIRST_PAGE),
                checkUpdateTypes = mutableSetOf(),
                applyTypes = mutableSetOf(STDeployApplyType.APP_START, STDeployApplyType.APP_CLOSE_ALL_PAGES, STDeployApplyType.APP_OPEN_FIRST_PAGE)
        )
    }
}