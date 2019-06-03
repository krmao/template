package com.smart.library.deploy.model

interface STIDeployCheckUpdateCallback {
    fun onCheckUpdateCallback(isHaveNewVersion: Boolean)
    fun onDownloadCallback(downloadSuccess: Boolean)
    fun onMergePatchCallback(mergeSuccess: Boolean)
    fun onApplyCallback(applySuccess: Boolean)
}