package com.smart.library.deploy.model

import java.io.File

data class CXDeployConfigModel(
        val baseBundle: CXBundleInfo,
        val baseBundlePathInAssets: String,
        val checkUpdateHandler: ((bundleInfo: CXBundleInfo?, patchInfo: CXPatchInfo?, downloadUrl: String?, isPatch: Boolean) -> Unit?) -> Unit?,
        val downloadHandler: (patchDownloadUrl: String?, toFile: File, callback: (file: File?) -> Unit) -> Unit?,
        val reloadHandler: (indexBundleFileInSdcard: File?, versionOfIndexBundleFileInSdcard: Int?) -> Boolean,
        val initCallback: (indexBundleFile: File?, versionOfIndexBundleFileInSdcard: Int?) -> Unit?
)