package com.smart.library.deploy

import com.smart.library.deploy.model.CXBundleInfo
import com.smart.library.util.CXLogUtil
import com.smart.library.util.CXPreferencesUtil


@Suppress("PrivatePropertyName", "unused", "LiftReturnOrAssignment")
class CXDeployPreferenceManager internal constructor(private val type: CXDeployManager) {

    private val TAG = "[deploy-${type.name}-preference]"

    private val KEY_BUNDLE_APPLIED by lazy { "KEY_DEPLOY_BUNDLE_APPLIED_${type.name}" }
    private val KEY_BUNDLE_TEMP by lazy { "KEY_DEPLOY_BUNDLE_TEMP_${type.name}" }

    /**
     * tempZipFile 准备好后, 保存
     */
    fun saveTempBundleInfo(bundleInfo: CXBundleInfo?) {
        CXPreferencesUtil.putEntity(KEY_BUNDLE_TEMP, bundleInfo)
        CXLogUtil.d(TAG, "saveTempBundleInfo success, bundleInfo=$bundleInfo")
    }

    fun getTempBundleInfo(): CXBundleInfo? = CXPreferencesUtil.getEntity(KEY_BUNDLE_TEMP, CXBundleInfo::class.java)

    /**
     * 成功将 tempZipFile 应用后, 保存
     */
    fun saveAppliedBundleInfo(bundleInfo: CXBundleInfo?) {
        CXPreferencesUtil.putEntity(KEY_BUNDLE_APPLIED, bundleInfo)
        CXLogUtil.d(TAG, "saveAppliedBundleInfo success, bundleInfo=$bundleInfo")
    }

    fun getAppliedBundleInfo(): CXBundleInfo? = CXPreferencesUtil.getEntity(KEY_BUNDLE_APPLIED, CXBundleInfo::class.java)
}