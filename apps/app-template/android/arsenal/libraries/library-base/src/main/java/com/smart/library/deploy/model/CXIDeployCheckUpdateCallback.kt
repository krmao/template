package com.smart.library.deploy.model

interface CXIDeployCheckUpdateCallback {
    fun onCheckUpdateCallback(isHaveNewVersion: Boolean)
    fun onDownloadCallback(downloadSuccess: Boolean)
    fun onMergePatchCallback(mergeSuccess: Boolean)
    fun onApplyCallback(applySuccess: Boolean)
}