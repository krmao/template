package com.smart.library.map.location

import android.Manifest
import android.app.Activity
import android.location.Location
import com.smart.library.util.STLogUtil
import com.smart.library.util.STSystemUtil
import com.smart.library.map.model.STLatLng
import com.smart.library.map.location.impl.STLocationClientForAMap
import com.smart.library.util.rx.permission.RxPermissions

/**
 * 外观模式(门面模式) 结合 静态代理模式
 *
 * @see STLocationClientForAMap
 */
@Suppress("PrivatePropertyName", "unused", "MemberVisibilityCanBePrivate")
object STLocationManager {

    private var locationClient: STILocationClient? = null

    @Volatile
    var isPermissionHandling: Boolean = false

    /**
     * 定位前可能需要请求定位权限
     */
    @JvmStatic
    fun ensurePermissions(activity: Activity?, callback: (allPermissionsGranted: Boolean) -> Unit?) {
        if (STSystemUtil.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)) {
            callback.invoke(true)
        } else {
            if (!isPermissionHandling && activity != null && !activity.isFinishing) {
                isPermissionHandling = true
                RxPermissions(activity).requestEachCombined(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION).subscribe {
                    STLogUtil.e(RxPermissions.TAG, "request permissions callback -> $it")
                    isPermissionHandling = false
                    callback.invoke(it.granted)
                }
            } else {
                callback.invoke(false)
            }
        }
        Unit
    }

    @JvmStatic
    fun initialize(locationClient: STILocationClient) {
        this.locationClient = locationClient
    }

    /**
     * 缓存定位
     */
    @JvmStatic
    var cacheLocation: Location? = locationClient?.getLastKnownLocation()
        internal set(value) {
            if (STLatLng.isValidLatLng(value?.latitude, value?.longitude)) {
                field = value
                cacheLocationTime = System.currentTimeMillis()
            }
        }

    /**
     * @param validInterval 缓存有效间隔 单位/秒
     */
    @JvmStatic
    @JvmOverloads
    fun isCacheLocationValid(validInterval: Long = 60 * 1000) = cacheLocationTimeDelta < validInterval

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

    /**
     * 一次定位
     */
    @JvmStatic
    @JvmOverloads
    fun startLocation(timeout: Long = 5000, onSuccess: ((location: Location) -> Unit?)? = null, onFailure: ((errorCode: Int, errorMessage: String) -> Unit?)? = null) = locationClient?.startLocation(timeout, onSuccess, onFailure)

    /**
     * 循环定位
     */
    @JvmStatic
    @JvmOverloads
    fun startLocationLoop(interval: Long = 30000, ensurePermissions: ((callback: (allPermissionsGranted: Boolean) -> Unit?) -> Unit?)? = null, onSuccess: ((location: Location) -> Unit?)? = null, onFailure: ((errorCode: Int, errorMessage: String) -> Unit?)? = null) = locationClient?.startLocationLoop(interval, ensurePermissions, onSuccess, onFailure)

    /**
     * 停止循环定位
     */
    @JvmStatic
    fun stopLocationLoop() = locationClient?.stopLocationLoop()

    /**
     * 停止定位
     */
    @JvmStatic
    fun stopLocation() = locationClient?.stopLocation()
}