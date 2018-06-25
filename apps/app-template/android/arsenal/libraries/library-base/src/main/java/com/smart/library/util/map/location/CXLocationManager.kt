package com.smart.library.util.map.location

import android.Manifest
import android.app.Activity
import android.location.Location
import com.smart.library.util.map.location.client.CXILocationClient
import com.smart.library.util.map.location.client.impl.CXAMapLocationClient
import com.smart.library.util.map.CXMapUtil
import com.smart.library.util.CXPreferencesUtil
import com.smart.library.util.rx.permission.RxPermissions

/**
 * @param activity 请求权限时需要
 * @param useAmap true:高德定位 false 百度定位(暂未实现)
 */
@Suppress("PrivatePropertyName", "unused", "MemberVisibilityCanBePrivate")
class CXLocationManager(val activity: Activity? = null, val useAmap: Boolean = true) {

    companion object {
        private val KEY_CACHE_LOCATION = "KEY_CACHE_LOCATION_${CXLocationManager::class.java.name}"

        @JvmStatic
        var cacheLocation: Location? = CXPreferencesUtil.getEntity(KEY_CACHE_LOCATION, Location::class.java)
            internal set(value) {
                if (CXMapUtil.isValidLatLng(value?.latitude, value?.longitude)) {
                    field = value
                    CXPreferencesUtil.putEntity(KEY_CACHE_LOCATION, value)
                }
            }
    }

    private val ensurePermissions: () -> Unit? = {
        activity?.let { RxPermissions(it).request(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION).subscribe { } }
        Unit
    }

    private val locationClient: CXILocationClient by lazy {
        if (useAmap) CXAMapLocationClient(ensurePermissions) {
            cacheLocation = it
            Unit
        } else CXAMapLocationClient(ensurePermissions) {
            cacheLocation = it
            Unit
        }
    }

    /**
     * 一次定位
     */
    fun startLocation(timeout: Long = 5000, onSuccess: ((location: Location) -> Unit?)? = null, onFailure: ((errorCode: Int, errorMessage: String) -> Unit?)? = null) {
        locationClient.startLocation(timeout, onSuccess, onFailure)
    }

    /**
     * 循环定位
     */
    fun startLocationLoop(interval: Long = 30000, onSuccess: ((location: Location) -> Unit?)? = null, onFailure: ((errorCode: Int, errorMessage: String) -> Unit?)? = null) {
        locationClient.startLocationLoop(interval, onSuccess, onFailure)
    }

    /**
     * 停止循环定位
     */
    fun stopLocationLoop() {
        locationClient.stopLocationLoop()
    }

    /**
     * 停止定位
     */
    fun stopLocation() {
        locationClient.stopLocation()
    }
}
