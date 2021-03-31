package com.smart.library.deploy

import androidx.annotation.Keep
import com.smart.library.deploy.model.STBundleInfo
import com.smart.library.util.STLogUtil
import com.smart.library.util.STPreferencesUtil

@Suppress("PrivatePropertyName", "unused", "LiftReturnOrAssignment")
//@Keep
class STDeployPreferenceManager internal constructor(private val type: STDeployManager) {

    private val TAG = "[deploy-${type.name}-preference]"

    private val KEY_BUNDLE_APPLIED by lazy { "KEY_DEPLOY_BUNDLE_APPLIED_${type.name}" }
    private val KEY_BUNDLE_TEMP by lazy { "KEY_DEPLOY_BUNDLE_TEMP_${type.name}" }

    /**
     * tempZipFile 准备好后, 保存
     */
    fun saveTempBundleInfo(bundleInfo: STBundleInfo?) {
        STPreferencesUtil.putEntity(KEY_BUNDLE_TEMP, bundleInfo)
        STLogUtil.d(TAG, "saveTempBundleInfo success, bundleInfo=$bundleInfo")
    }

    fun getTempBundleInfo(): STBundleInfo? = STPreferencesUtil.getEntity(KEY_BUNDLE_TEMP, STBundleInfo::class.java)

    /**
     * 成功将 tempZipFile 应用后, 保存
     */
    fun saveAppliedBundleInfo(bundleInfo: STBundleInfo?) {
        STPreferencesUtil.putEntity(KEY_BUNDLE_APPLIED, bundleInfo)
        STLogUtil.d(TAG, "saveAppliedBundleInfo success, bundleInfo=$bundleInfo")
    }

    fun getAppliedBundleInfo(): STBundleInfo? = STPreferencesUtil.getEntity(KEY_BUNDLE_APPLIED, STBundleInfo::class.java)
}