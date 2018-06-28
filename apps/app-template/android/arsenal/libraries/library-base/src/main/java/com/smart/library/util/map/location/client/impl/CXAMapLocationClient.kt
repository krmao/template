package com.smart.library.util.map.location.client.impl

import android.Manifest
import android.app.Activity
import android.location.Location
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.smart.library.base.CXBaseApplication
import com.smart.library.util.CXLogUtil
import com.smart.library.util.map.CXLatLng
import com.smart.library.util.map.location.CXLocationManager
import com.smart.library.util.map.location.client.CXILocationClient
import com.smart.library.util.rx.permission.Permission
import com.smart.library.util.rx.permission.RxPermissions
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

/**
 * 高德定位
 *
 * 注意:单次定位是定位前检查权限, 循环定位是定位失败后检查权限
 *
 * @param activity 请求权限时需要
 */
@Suppress("MemberVisibilityCanBePrivate", "unused", "PropertyName")
open class CXAMapLocationClient(activity: Activity?, val ensurePermissions: (client: CXAMapLocationClient, callback: (Permission) -> Unit?) -> Unit? = { client, callback ->
    if (!client.isPermissionHandling && activity != null && !activity.isFinishing) {
        client.isPermissionHandling = true
        RxPermissions(activity).requestEachCombined(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION).subscribe {
            CXLogUtil.e(RxPermissions.TAG, "request permissions callback -> $it")
            client.isPermissionHandling = false
            callback.invoke(it)
        }
    } else {
        callback.invoke(Permission(listOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION).joinToString(), false, true))
    }
    Unit
}) : CXILocationClient {

    protected val TAG: String = CXAMapLocationClient::class.java.name

    var isPermissionHandling: Boolean = false
    protected val rxPermissions: RxPermissions? by lazy { activity?.let { RxPermissions(it) } }
    protected var locationTimerDisposable: Disposable? = null
    protected var locationClient: AMapLocationClient? = null
    protected var locationLoopClient: AMapLocationClient? = null
    protected val refreshCache: ((Location) -> Unit?) = { CXLocationManager.cacheLocation = it;Unit }

    /**
     * 一次定位
     *
     * 定位前会确认是否开启权限, 开启后才会真正的定位, 不开启则返回 onFailure
     */
    override fun startLocation(timeout: Long, onSuccess: ((location: Location) -> Unit?)?, onFailure: ((errorCode: Int, errorMessage: String) -> Unit?)?) {
        stopLocation()
        ensurePermissions.invoke(this@CXAMapLocationClient, {
            if (it.granted) {
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
                                refreshCache.invoke(location) // 更新定位缓存

                                onSuccess?.invoke(location)
                            } else {
                                CXLogUtil.v(TAG, "定位成功, 无效经纬度:$latLng")
                                onFailure?.invoke(AMapLocation.ERROR_CODE_UNKNOWN, "UN_VALID LATLNG :$latLng")
                            }
                        } else {
                            onFailure?.invoke(location.errorCode, location.locationDetail)
                        }
                    }

                    startLocation()
                    locationTimerDisposable = Observable.timer(timeout, TimeUnit.MILLISECONDS).subscribe {
                        this@CXAMapLocationClient.stopLocation()

                        onFailure?.invoke(AMapLocation.ERROR_CODE_UNKNOWN, "LOCATION TIME OUT")
                    }
                }
            } else {
                onFailure?.invoke(AMapLocation.ERROR_CODE_FAILURE_LOCATION_PERMISSION, "尚未开启定位权限")
            }
        })
    }

    /**
     * 循环定位
     *
     * 定位前并不会判断定位权限是否开启
     *
     * 定位失败后会提示是否开启定位权限, 不开启则待 @param interval 时间段后 下一个定位失败再次提示, 如果开启权限则会立即定位一次
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
                        refreshCache.invoke(location) // 更新定位缓存

                        onSuccess?.invoke(location)
                    } else {
                        CXLogUtil.v(TAG, "定位成功, 无效经纬度:$latLng")
                        onFailure?.invoke(AMapLocation.ERROR_CODE_UNKNOWN, "UN_VALID LATLNG :$latLng")
                    }
                } else {
                    CXLogUtil.v(TAG, "定位失败, ${location.errorCode}:${location.locationDetail}")

                    if (location?.errorCode == AMapLocation.ERROR_CODE_FAILURE_LOCATION_PERMISSION) ensurePermissions.invoke(this@CXAMapLocationClient, {
                        if (it.granted) { // 开启定位权限后立即定位一次
                            this.stopLocation()
                            this.startLocation()
                        }
                    })

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