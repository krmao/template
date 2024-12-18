package com.smart.library.reactnative

import android.app.Activity
import android.app.Application
import com.facebook.imagepipeline.core.ImagePipelineConfig
import com.facebook.react.bridge.Promise
import com.smart.library.STInitializer
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
internal object RNDeployManager {

    @JvmStatic
    fun init(application: Application?, frescoConfig: ImagePipelineConfig, callback: ((success: Boolean) -> Unit)? = null) {
        val TAG = STDeployManager.REACT_NATIVE.TAG
        STDeployManager.REACT_NATIVE.initialize(
            deployConfig = STDeployConfigModel(
                baseBundle = STBundleInfo(STInitializer.configRN?.baseVersion ?: 0),
                baseBundlePathInAssets = STInitializer.configRN?.bundlePathInAssets ?: "bundle-rn.zip",
                checkUpdateHandler = {
                    STLogUtil.d(TAG, "checkUpdateHandler invoke")
                    STOkHttpManager.doGet(url = STInitializer.configRN?.checkUpdateHTTPGetUrl, readTimeoutMS = 30 * 1000, callback = { result: String? ->
                        STLogUtil.d(TAG, "checkUpdateHandler downloadString success $result")
                        val jsonObject = STJsonUtil.toJSONObjectOrNull(result)
                        if (jsonObject != null) {
                            val errorCode = jsonObject.optInt("errorCode", -1)
                            if (errorCode == 0) {
                                val resultObject = jsonObject.optJSONObject("result")
                                val baseVersion = resultObject?.optInt("baseVersion", -1) ?: -1
                                val toVersion = resultObject?.optInt("toVersion", -1) ?: -1
                                val downloadUrl = resultObject?.optString("downloadUrl") ?: ""
                                val bundleChecksum = resultObject?.optString("bundleChecksum") ?: ""
                                if (baseVersion != -1 && toVersion != -1 && !downloadUrl.isBlank() && !bundleChecksum.isBlank()) {
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
                    RNInstanceManager.reloadBundle(indexBundleFile, versionOfIndexBundleFileInSdcard)
                },
                initCallback = { indexBundleFile: File?, versionOfIndexBundleFileInSdcard: Int? ->
                    STLogUtil.e(RNInstanceManager.TAG, "initCallback start")
                    RNInstanceManager.init(application, STInitializer.debug(), indexBundleFile, versionOfIndexBundleFileInSdcard, frescoConfig, OnRNCallNativeHandler(), callback)
                    STLogUtil.e(RNInstanceManager.TAG, "initCallback end")
                }
            ),
            checkUpdateTypes = mutableSetOf(), // mutableSetOf(STDeployCheckUpdateType.APP_START, STDeployCheckUpdateType.APP_FORGROUND_TO_BACKGROUND, STDeployCheckUpdateType.APP_OPEN_FIRST_PAGE)
            applyTypes = mutableSetOf(STDeployApplyType.APP_START, STDeployApplyType.APP_CLOSE_ALL_PAGES, STDeployApplyType.APP_OPEN_FIRST_PAGE)
        )
    }


    /**
     * react native call native processors
     */
    @Suppress("UNUSED_ANONYMOUS_PARAMETER", "UNUSED_VARIABLE")
    class OnRNCallNativeHandler : Function4<Activity?, String?, String?, Promise?, Unit> {

        /**
         * @param functionName to native functions
         *
         * @param data
         *                  pageName    :String
         *                  requestCode :Int?   must be in [0, 65535]
         *                  params      :HashMap<String, String | Number>?
         *
         * @param promise
         *                  promise?.resolve(RNResult.successJson())
         *                  promise?.reject("0", "functionName not found !")
         */
        override fun invoke(currentActivity: Activity?, functionName: String?, data: String?, promise: Promise?) {
            STInitializer.configBridge?.bridgeHandler?.handleBridge(currentActivity, functionName, data, null, object : STInitializer.BridgeHandlerCallback {
                override fun onCallback(callbackId: String?, resultJsonString: String?) {
                    promise?.resolve(resultJsonString)
                }
            })
        }
    }
}