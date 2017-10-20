package com.smart.library.util.hybird

import com.smart.library.util.HKPreferencesUtil
import com.smart.library.util.HKToastUtil
import com.smart.library.util.cache.HKCacheManager
import com.smart.library.util.network.HKNetworkUtil

/**
 * getAppInfo/getDeviceInfo 放到 userAgent
 */
@Suppress("unused")
class HKHybirdMethods {

    /**
     * 注意 反射的时候 参数最好全部是string
     */
    companion object {
        fun showToast(message: String) {
            HKToastUtil.show(message)
        }

        fun putToLocal(key: String, value: String) {
            HKPreferencesUtil.putString(key, value)
        }

        fun getFromLocal(key: String, default: String? = null) {
            HKPreferencesUtil.getString(key, default)
        }

        fun putToMemory(module: String, key: String, value: String) {
            HKCacheManager.put(module, key, value)
        }

        fun getFromMemory(module: String, key: String, default: String? = null): String? = HKCacheManager.get(module, key, default)

        fun removeFromMemory(module: String) {
            HKCacheManager.remove(module)
        }

        fun removeFromMemory(module: String, key: String) {
            HKCacheManager.remove(module, key)
        }

        fun isNetworkAvailable(): Boolean = HKNetworkUtil.isNetworkAvailable()
    }
}
