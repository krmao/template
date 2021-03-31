package com.smart.library.deploy.model

import androidx.annotation.Keep
import java.io.File

@Keep
data class STDeployConfigModel(
    val baseBundle: STBundleInfo,
    val baseBundlePathInAssets: String,
    val checkUpdateHandler: (((bundleInfo: STBundleInfo?, patchInfo: STPatchInfo?, downloadUrl: String?, isPatch: Boolean) -> Unit?) -> Unit)? = null,
    val downloadHandler: ((patchDownloadUrl: String?, toFile: File, callback: (file: File?) -> Unit) -> Unit)? = null,
    val reloadHandler: ((indexBundleFileInSdcard: File?, versionOfIndexBundleFileInSdcard: Int?) -> Unit)? = null,
    val initCallback: ((indexBundleFile: File?, versionOfIndexBundleFileInSdcard: Int?) -> Unit)? = null
)