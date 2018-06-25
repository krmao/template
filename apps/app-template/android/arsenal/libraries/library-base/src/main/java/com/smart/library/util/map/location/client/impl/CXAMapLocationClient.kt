package com.smart.library.util.map.location.client.impl

import android.location.Location
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.smart.library.base.CXBaseApplication
import com.smart.library.util.CXLogUtil
import com.smart.library.util.map.CXLatLng
import com.smart.library.util.map.location.client.CXILocationClient
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

/**
 * 高德定位
 */
@Suppress("MemberVisibilityCanBePrivate", "unused", "PrivatePropertyName")
internal class CXAMapLocationClient(val ensurePermissions: (() -> Unit?)? = null, val refreshCache: ((Location) -> Unit?)? = null) : CXILocationClient {

    private val TAG: String = CXAMapLocationClient::class.java.name

    private var locationTimerDisposable: Disposable? = null
    private var locationClient: AMapLocationClient? = null
    private var locationLoopClient: AMapLocationClient? = null

    /**
     * 一次定位
     */
    override fun startLocation(timeout: Long, onSuccess: ((location: Location) -> Unit?)?, onFailure: ((errorCode: Int, errorMessage: String) -> Unit?)?) {
        stopLocation()

        locationClient = AMapLocationClient(CXBaseApplication.INSTANCE).apply {

            setLocationOption(AMapLocationClientOption().apply {
                interval = 800L
                httpTimeOut = 10000             // 默认 10s
                isLocationCacheEnable = true    // 默认 true
                isOnceLocation = true           // 强制非单次定位
                isMockEnable = false            // 禁止模拟数据
                isNeedAddress = false           // 不需要返回地址
                isWifiScan = true               // 设置是否允许调用WIFI刷新 默认值为true，当设置为false时会停止主动调用WIFI刷新，将会极大程度影响定位精度，但可以有效的降低定位耗电
                locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
            })

            setLocationListener { location ->
                this@CXAMapLocationClient.stopLocation()


                if (location?.errorCode == AMapLocation.LOCATION_SUCCESS) {
                    val latLng = CXLatLng(location.latitude, location.longitude)
                    if (latLng.isValid()) {

                        CXLogUtil.d(TAG, "定位成功, 有效经纬度:$latLng")
                        refreshCache?.invoke(location) // 更新定位缓存

                        onSuccess?.invoke(location)
                    } else {
                        CXLogUtil.v(TAG, "定位成功, 无效经纬度:$latLng")
                        onFailure?.invoke(AMapLocation.ERROR_CODE_UNKNOWN, "UN_VALID LATLNG :$latLng")
                    }
                } else {
                    if (location?.errorCode == AMapLocation.ERROR_CODE_FAILURE_LOCATION_PERMISSION) ensurePermissions?.invoke()

                    onFailure?.invoke(location.errorCode, location.locationDetail)
                }
            }

            startLocation()
            locationTimerDisposable = Observable.timer(timeout, TimeUnit.MILLISECONDS).subscribe {
                this@CXAMapLocationClient.stopLocation()

                onFailure?.invoke(AMapLocation.ERROR_CODE_UNKNOWN, "LOCATION TIME OUT")
            }
        }
    }

    /**
     * 循环定位
     */
    override fun startLocationLoop(interval: Long, onSuccess: ((location: Location) -> Unit?)?, onFailure: ((errorCode: Int, errorMessage: String) -> Unit?)?) {
        stopLocationLoop()

        locationLoopClient = AMapLocationClient(CXBaseApplication.INSTANCE).apply {

            setLocationOption(AMapLocationClientOption().apply {
                this.interval = interval
                httpTimeOut = 10000             // 默认 10s
                isLocationCacheEnable = true    // 默认 true
                isOnceLocation = false          // 强制非单次定位
                isMockEnable = false            // 禁止模拟数据
                isNeedAddress = false           // 不需要返回地址
                isWifiScan = true               // 设置是否允许调用WIFI刷新 默认值为true，当设置为false时会停止主动调用WIFI刷新，将会极大程度影响定位精度，但可以有效的降低定位耗电
                locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
            })

            setLocationListener { location ->

                if (location?.errorCode == AMapLocation.LOCATION_SUCCESS) {
                    val latLng = CXLatLng(location.latitude, location.longitude)
                    if (latLng.isValid()) {

                        CXLogUtil.d(TAG, "定位成功, 有效经纬度:$latLng")
                        refreshCache?.invoke(location) // 更新定位缓存

                        onSuccess?.invoke(location)
                    } else {
                        CXLogUtil.v(TAG, "定位成功, 无效经纬度:$latLng")
                        onFailure?.invoke(AMapLocation.ERROR_CODE_UNKNOWN, "UN_VALID LATLNG :$latLng")
                    }
                } else {
                    CXLogUtil.v(TAG, "定位失败, ${location.errorCode}:${location.locationDetail}")

                    if (location?.errorCode == AMapLocation.ERROR_CODE_FAILURE_LOCATION_PERMISSION) ensurePermissions?.invoke()

                    onFailure?.invoke(location.errorCode, location.locationDetail)
                }
            }

            this.startLocation()
        }

    }

    /**
     * 强制停止单次有效定位
     */
    override fun stopLocation() {
        locationTimerDisposable?.dispose()
        locationClient?.setLocationListener({})
        locationClient?.stopLocation()
        locationClient?.onDestroy() // 销毁定位,释放定位资源, 当不再需要进行定位时调用此方法 该方法会释放所有定位资源，调用后再进行定位需要重新实例化AMapLocationClient
        locationClient = null
    }

    /**
     * 停止循环定位
     */
    override fun stopLocationLoop() {
        locationLoopClient?.setLocationListener({})
        locationLoopClient?.stopLocation()
        locationLoopClient?.onDestroy() // 销毁定位,释放定位资源, 当不再需要进行定位时调用此方法 该方法会释放所有定位资源，调用后再进行定位需要重新实例化AMapLocationClient
        locationLoopClient = null
    }

}