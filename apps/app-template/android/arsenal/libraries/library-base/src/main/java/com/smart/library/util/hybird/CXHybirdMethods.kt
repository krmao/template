package com.smart.library.util.hybird

import com.smart.library.util.CXPreferencesUtil
import com.smart.library.util.CXToastUtil
import com.smart.library.util.cache.CXCacheManager
import com.smart.library.util.network.CXNetworkUtil

/**
 * getAppInfo/getDeviceInfo 放到 userAgent
 */
@Suppress("unused")
class CXHybirdMethods {

    /**
     * 注意 反射的时候 参数最好全部是string
     */
    companion object {
        fun showToast(message: String) = CXToastUtil.show(message)

        fun putToLocal(key: String, value: String) {
            CXPreferencesUtil.putString(key, value)
        }

        fun getFromLocal(key: String, default: String = "") {
            CXPreferencesUtil.getString(key, default)
        }

        fun putToMemory(module: String, key: String, value: String) = CXCacheManager.put(module, key, value)

        fun getFromMemory(module: String, key: String, default: String? = null): String? = CXCacheManager.get(module, key, default)

        fun removeFromMemory(module: String) = CXCacheManager.remove(module)

        fun removeFromMemory(module: String, key: String) = CXCacheManager.remove(module, key)

        fun isNetworkAvailable(): Boolean = CXNetworkUtil.isNetworkAvailable()
    }
}
