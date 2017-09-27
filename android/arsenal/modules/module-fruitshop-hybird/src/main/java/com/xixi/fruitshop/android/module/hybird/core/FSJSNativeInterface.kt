package com.xixi.fruitshop.android.module.hybird.core

import com.xixi.library.android.base.FSActivityLifecycleCallbacks
import com.xixi.library.android.util.FSPreferencesUtil
import com.xixi.library.android.util.network.FSNetworkUtil

/**
 * getAppInfo/getDeviceInfo 放到 userAgent
 */
class FSJSNativeInterface {

    fun showToast(message: String?) {

    }

    fun putStringToLocal(key: String, value: String) {
        FSPreferencesUtil.putString(key, value)
    }

    fun getStringFromLocal(key: String, default: String? = null) {
        FSPreferencesUtil.getString(key, default)
    }

    fun isNetworkAvailable(): Boolean {
        return FSNetworkUtil.isNetworkAvailable()
    }

    fun isApplicationVisible(): Boolean {
        return FSActivityLifecycleCallbacks.isApplicationVisible
    }

    /**
     * finish current page which webView in
     */
    fun finish() {
    }
}
