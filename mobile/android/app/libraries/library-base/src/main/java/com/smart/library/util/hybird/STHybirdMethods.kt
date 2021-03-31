package com.smart.library.util.hybird

import androidx.annotation.Keep
import com.smart.library.util.STPreferencesUtil
import com.smart.library.util.STToastUtil
import com.smart.library.util.cache.STCacheManager
import com.smart.library.util.STNetworkUtil

/**
 * getAppInfo/getDeviceInfo 放到 userAgent
 */
//@Keep
@Suppress("unused")
class STHybirdMethods {

    /**
     * 注意 反射的时候 参数最好全部是string
     */
    companion object {
        fun showToast(message: String) = STToastUtil.show(message)

        fun putToLocal(key: String, value: String) {
            STPreferencesUtil.putString(key, value)
        }

        fun getFromLocal(key: String, default: String = "") {
            STPreferencesUtil.getString(key, default)
        }

        fun putToMemory(module: String, key: String, value: String) = STCacheManager.put(module, key, value)

        fun getFromMemory(module: String, key: String, default: String? = null): String? = STCacheManager.get(module, key, default)

        fun removeFromMemory(module: String) = STCacheManager.remove(module)

        fun removeFromMemory(module: String, key: String) = STCacheManager.remove(module, key)

        fun isNetworkAvailable(): Boolean = STNetworkUtil.checkNetworkState()
    }
}
