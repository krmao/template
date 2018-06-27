package com.smart.library.util.map.location

import android.location.Location
import com.smart.library.util.CXPreferencesUtil
import com.smart.library.util.map.CXMapUtil
import com.smart.library.util.map.location.client.CXILocationClient
import com.smart.library.util.map.location.client.impl.CXAMapLocationClient

/**
 * 使用代理模式构造
 *
 * @see CXAMapLocationClient
 */
@Suppress("PrivatePropertyName", "unused", "MemberVisibilityCanBePrivate")
open class CXLocationManager(protected val locationClient: CXILocationClient) {

    companion object {
        private val KEY_CACHE_LOCATION = "KEY_CACHE_LOCATION_${CXLocationManager::class.java.name}"

        /**
         * 缓存定位
         */
        @JvmStatic
        var cacheLocation: Location? = CXPreferencesUtil.getEntity(KEY_CACHE_LOCATION, Location::class.java)
            internal set(value) {
                if (CXMapUtil.isValidLatLng(value?.latitude, value?.longitude)) {
                    field = value
                    cacheLocationTime = System.currentTimeMillis()
                    CXPreferencesUtil.putEntity(KEY_CACHE_LOCATION, value)
                }
            }

        /**
         * 缓存定位时的时间
         */
        @JvmStatic
        var cacheLocationTime: Long = 0
            private set(value) {
                field = value
            }

        /**
         * 缓存定位时的时间距离现在的时间差
         */
        @JvmStatic
        var cacheLocationTimeDelta: Long = System.currentTimeMillis()
            get() = System.currentTimeMillis() - cacheLocationTime
            private set
    }

    /**
     * 一次定位
     */
    fun startLocation(timeout: Long = 5000, onSuccess: ((location: Location) -> Unit?)? = null, onFailure: ((errorCode: Int, errorMessage: String) -> Unit?)? = null) = locationClient.startLocation(timeout, onSuccess, onFailure)

    /**
     * 循环定位
     */
    fun startLocationLoop(interval: Long = 30000, onSuccess: ((location: Location) -> Unit?)? = null, onFailure: ((errorCode: Int, errorMessage: String) -> Unit?)? = null) = locationClient.startLocationLoop(interval, onSuccess, onFailure)

    /**
     * 停止循环定位
     */
    fun stopLocationLoop() = locationClient.stopLocationLoop()

    /**
     * 停止定位
     */
    fun stopLocation() = locationClient.stopLocation()
}