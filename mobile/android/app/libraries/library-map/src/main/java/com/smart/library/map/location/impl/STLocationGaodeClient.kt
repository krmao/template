package com.smart.library.map.location.impl

import android.Manifest
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.amap.api.maps.LocationSource
import com.smart.library.base.STBaseApplication
import com.smart.library.map.location.STILocationClient
import com.smart.library.map.location.STLocation
import com.smart.library.map.location.STLocationManager
import com.smart.library.map.model.STLatLng
import com.smart.library.map.model.STLatLngType
import com.smart.library.util.STLogUtil
import com.smart.library.util.STSystemUtil
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

/**
 * 高德定位
 *
 * 注意:单次定位是定位前检查权限, 循环定位是定位失败后检查权限
 */
@Suppress("MemberVisibilityCanBePrivate", "unused", "PropertyName")
open class STLocationGaodeClient(private val isNeedAddress: Boolean = true) : STILocationClient, LocationSource {

    protected val TAG: String = STLocationGaodeClient::class.java.name

    protected var locationTimerDisposable: Disposable? = null
    protected val locationClient: AMapLocationClient by lazy {
        AMapLocationClient(STBaseApplication.INSTANCE).apply {
            setLocationOption(AMapLocationClientOption().apply {
                interval = 800L
                httpTimeOut = 10000             // 默认 10s
                isLocationCacheEnable = true    // 默认 true
                isOnceLocation = true           // 强制单次定位
                isMockEnable = false            // 禁止模拟数据
                isNeedAddress = this@STLocationGaodeClient.isNeedAddress           // 不需要返回地址
                isWifiScan = true               // 设置是否允许调用WIFI刷新 默认值为true，当设置为false时会停止主动调用WIFI刷新，将会极大程度影响定位精度，但可以有效的降低定位耗电
                locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
            })
        }
    }
    protected var locationLoopClient: AMapLocationClient? = null
    protected val refreshCache: ((STLocation) -> Unit?) = { STLocationManager.cacheLocation = it;Unit }

    override fun getLastKnownLocation(): STLocation = toLocation(locationClient.lastKnownLocation)

    private fun toLocation(location: AMapLocation): STLocation {
        return STLocation(location.latitude, location.longitude, location.accuracy, location)
    }

    /**
     *
     *  @see Manifest.permission.ACCESS_COARSE_LOCATION  用于进行网络定位
     *  @see Manifest.permission.ACCESS_FINE_LOCATION    用于访问GPS定位
     *
     *  一次定位
     *
     *  定位前会确认是否开启权限, 开启后才会真正的定位, 不开启则返回 onFailure
     */
    override fun startLocation(timeout: Long, onSuccess: ((location: STLocation) -> Unit?)?, onFailure: ((errorCode: Int, errorMessage: String) -> Unit?)?) {
        stopLocation()
        if (STSystemUtil.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)) {
            locationClient.setLocationListener(object : AMapLocationListener {
                override fun onLocationChanged(location: AMapLocation?) {
                    locationClient.unRegisterLocationListener(this)
                    this@STLocationGaodeClient.stopLocation()

                    if (location?.errorCode == AMapLocation.LOCATION_SUCCESS) {
                        val latLng = STLatLng(location.latitude, location.longitude, STLatLngType.GCJ02)
                        if (latLng.isValid()) {
                            STLogUtil.d(TAG, "[单次定位]定位成功, 有效经纬度:$latLng")
                            refreshCache.invoke(toLocation(location)) // 更新定位缓存

                            onSuccess?.invoke(toLocation(location))
                            return
                        }
                    }
                    STLogUtil.d(TAG, "定位失败[单次定位], location=$location")
                    onFailure?.invoke(AMapLocation.ERROR_CODE_UNKNOWN, "[单次定位]定位失败, location=$location")
                }
            })

            locationClient.startLocation()
            locationTimerDisposable = Observable.timer(timeout, TimeUnit.MILLISECONDS).subscribe {
                stopLocation()

                onFailure?.invoke(AMapLocation.ERROR_CODE_UNKNOWN, "[单次定位]LOCATION TIME OUT")
            }
        } else {
            onFailure?.invoke(AMapLocation.ERROR_CODE_FAILURE_LOCATION_PERMISSION, "[单次定位]尚未开启定位权限")
        }
    }

    /**
     * 循环定位
     *
     * 定位前并不会判断定位权限是否开启
     *
     * 定位失败后会提示是否开启定位权限, 不开启则待 @param interval 时间段后 下一个定位失败再次提示, 如果开启权限则会立即定位一次
     */
    override fun startLocationLoop(interval: Long, onSuccess: ((location: STLocation) -> Unit?)?, onFailure: ((errorCode: Int, errorMessage: String) -> Unit?)?) {
        startLocationLoop(interval, null, onSuccess, onFailure)
    }

    private var onLoopLocationListener: AMapLocationListener? = null
    override fun startLocationLoop(interval: Long, ensurePermissions: ((callback: (allPermissionsGranted: Boolean) -> Unit?) -> Unit?)?, onSuccess: ((location: STLocation) -> Unit?)?, onFailure: ((errorCode: Int, errorMessage: String) -> Unit?)?) {
        stopLocationLoop()

        locationLoopClient = AMapLocationClient(STBaseApplication.INSTANCE).apply {

            setLocationOption(AMapLocationClientOption().apply {
                this.interval = interval
                httpTimeOut = 10000             // 默认 10s
                isLocationCacheEnable = true    // 默认 true
                isOnceLocation = false          // 强制非单次定位
                isMockEnable = false            // 禁止模拟数据
                isNeedAddress = this@STLocationGaodeClient.isNeedAddress           // 不需要返回地址
                isWifiScan = true               // 设置是否允许调用WIFI刷新 默认值为true，当设置为false时会停止主动调用WIFI刷新，将会极大程度影响定位精度，但可以有效的降低定位耗电
                locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
            })

            onLoopLocationListener = AMapLocationListener { location ->
                if (location?.errorCode == AMapLocation.LOCATION_SUCCESS) {
                    val latLng = STLatLng(location.latitude, location.longitude, STLatLngType.GCJ02)
                    if (latLng.isValid()) {

                        val wrapLocation = toLocation(location)
                        STLogUtil.w(TAG, "[循环定位]定位成功, 有效经纬度:$wrapLocation")
                        refreshCache.invoke(wrapLocation) // 更新定位缓存

                        onSuccess?.invoke(wrapLocation)
                    } else {
                        STLogUtil.w(TAG, "[循环定位]定位成功, 无效经纬度:$location")
                        onFailure?.invoke(AMapLocation.ERROR_CODE_UNKNOWN, "UN_VALID LOCATION :$location")
                    }
                } else {
                    STLogUtil.w(TAG, "[循环定位]定位失败, ${location?.errorCode}:${location?.locationDetail}")

                    if (location?.errorCode == AMapLocation.ERROR_CODE_FAILURE_LOCATION_PERMISSION) ensurePermissions?.invoke {
                        if (it) { // 开启定位权限后立即定位一次
                            this@apply.stopLocation()
                            this@apply.startLocation()
                        }
                    }
                    STLogUtil.w(TAG, "[循环定位]定位失败, location=$location")
                    onFailure?.invoke(AMapLocation.ERROR_CODE_UNKNOWN, "[循环定位]定位失败, location=$location")
                }
            }
            this.setLocationListener(onLoopLocationListener)
            this.startLocation()
        }

    }

    /**
     * 强制停止单次有效定位
     */
    override fun stopLocation() {
        locationTimerDisposable?.dispose()
        locationClient.stopLocation()
    }

    /**
     * 停止循环定位
     */
    override fun stopLocationLoop() {
        if (onLoopLocationListener != null) {
            locationLoopClient?.unRegisterLocationListener(onLoopLocationListener)
            onLoopLocationListener = null
        }
        locationLoopClient?.stopLocation()
        locationLoopClient?.onDestroy() // 销毁定位,释放定位资源, 当不再需要进行定位时调用此方法 该方法会释放所有定位资源，调用后再进行定位需要重新实例化AMapLocationClient
        locationLoopClient = null
    }

    override fun deactivate() {
        stopLocationLoop()
    }

    override fun activate(onLocationChangedListener: LocationSource.OnLocationChangedListener?) {
        startLocationLoop(1000,
                onSuccess = { location: STLocation ->
                    onLocationChangedListener?.onLocationChanged(location.location as? AMapLocation)
                },
                onFailure = { errorCode: Int, errorMessage: String ->
                    STLogUtil.e(TAG, "amap location failure, errorCode:$errorCode, errorMessage=$errorMessage")
                }
        )
    }
}