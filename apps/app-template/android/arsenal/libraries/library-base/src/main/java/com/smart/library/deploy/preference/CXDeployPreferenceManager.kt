package com.smart.library.deploy.preference

import com.smart.library.deploy.model.CXDeployType
import com.smart.library.deploy.model.bundle.CXBundleInfo
import com.smart.library.deploy.model.bundle.CXDeployBundleHelper
import com.smart.library.util.CXLogUtil
import com.smart.library.util.CXPreferencesUtil
import java.io.File


@Suppress("PrivatePropertyName", "unused", "LiftReturnOrAssignment")
class CXDeployPreferenceManager(private val type: CXDeployType, private val rootDir: File) {

    private val TAG = "[rn-deploy-preference]"

    private val KEY_BUNDLE_APPLIED by lazy {
        "KEY_BUNDLE_APPLIED_${type.name}"
    }
    private val KEY_BUNDLE_TEMP by lazy {
        "KEY_BUNDLE_TEMP_${type.name}"
    }

    /**
     * tempZipFile 准备好后, 保存
     */
    fun saveTempBundleInfo(bundleInfo: CXBundleInfo?) {
        CXPreferencesUtil.putEntity(KEY_BUNDLE_TEMP, bundleInfo)
        CXLogUtil.d(TAG, "saveTempBundleInfo success, bundleInfo=$bundleInfo")
    }

    fun getTempBundleInfo(): CXBundleInfo? {
        val bundleInfo: CXBundleInfo? = CXPreferencesUtil.getEntity(KEY_BUNDLE_TEMP, CXBundleInfo::class.java)
        if (bundleInfo != null && CXDeployBundleHelper(bundleInfo, rootDir).getTempZipFile().exists()) {
            CXLogUtil.d(TAG, "getTempBundleInfo success, bundleInfo=$bundleInfo")
            return bundleInfo
        } else {
            saveTempBundleInfo(null)
            CXLogUtil.e(TAG, "getTempBundleInfo failure")
            return null
        }
    }

    /**
     * 成功将 tempZipFile 应用后, 保存
     */
    fun saveAppliedBundleInfo(bundleInfo: CXBundleInfo?) {
        CXPreferencesUtil.putEntity(KEY_BUNDLE_APPLIED, bundleInfo)
        CXLogUtil.d(TAG, "saveAppliedBundleInfo success, bundleInfo=$bundleInfo")
    }

    fun getAppliedBundleInfo(): CXBundleInfo? {
        val bundleInfo: CXBundleInfo? = CXPreferencesUtil.getEntity(KEY_BUNDLE_APPLIED, CXBundleInfo::class.java)
        if (bundleInfo != null && CXDeployBundleHelper(bundleInfo, rootDir).checkUnzipDirValid()) {
            CXLogUtil.d(TAG, "getAppliedBundleInfo success, bundleInfo=$bundleInfo")
            return bundleInfo
        } else {
            saveAppliedBundleInfo(null)
            CXLogUtil.e(TAG, "getAppliedBundleInfo failure")
            return null
        }
    }
}