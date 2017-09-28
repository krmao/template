package com.xixi.library.android.util.hybird

import com.xixi.library.android.base.FSActivityLifecycleCallbacks
import com.xixi.library.android.util.FSPreferencesUtil
import com.xixi.library.android.util.FSToastUtil
import com.xixi.library.android.util.cache.FSCacheManager
import com.xixi.library.android.util.network.FSNetworkUtil

/**
 * getAppInfo/getDeviceInfo 放到 userAgent
 */
class FSHybirdMethods {

    fun showToast(message: String) {
        FSToastUtil.show(message)
    }

    fun putToLocal(key: String, value: String) {
        FSPreferencesUtil.putString(key, value)
    }

    fun getFromLocal(key: String, default: String? = null) {
        FSPreferencesUtil.getString(key, default)
    }

    fun putToMemory(module: String, key: String, value: String) {
        FSCacheManager.put(module, key, value)
    }

    fun getFromMemory(module: String, key: String, default: String? = null): String? {
        return FSCacheManager.get(module, key, default)
    }

    fun removeFromMemory(module: String) {
        FSCacheManager.remove(module)
    }

    fun removeFromMemory(module: String, key: String) {
        FSCacheManager.remove(module, key)
    }

    fun isNetworkAvailable(): Boolean {
        return FSNetworkUtil.isNetworkAvailable()
    }

    fun isApplicationVisible(): Boolean {
        return FSActivityLifecycleCallbacks.isApplicationVisible
    }
}
