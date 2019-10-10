package com.smart.library.map.location.impl

import android.Manifest
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
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
open class STLocationBaiduClient(private val isNeedAddress: Boolean = true) : STILocationClient {

    protected val TAG: String = STLocationBaiduClient::class.java.name

    protected var locationTimerDisposable: Disposable? = null
    protected val locationClient: LocationClient by lazy {
        LocationClient(STBaseApplication.INSTANCE).apply {
            locOption = LocationClientOption().apply {
                setScanSpan(800)
                locationMode = LocationClientOption.LocationMode.Hight_Accuracy
                setCoorType("bd09ll") //可选，默认gcj02，设置返回的定位结果坐标系
                setScanSpan(1000) //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
                setIsNeedAddress(this@STLocationBaiduClient.isNeedAddress)
                isOpenGps = true
                isLocationNotify = true
                setIsNeedLocationDescribe(true)
                setIsNeedLocationPoiList(true)
                setIgnoreKillProcess(false)
                SetIgnoreCacheException(false)
                setEnableSimulateGps(false)
            }
        }
    }
    protected var locationLoopClient: LocationClient? = null
    protected val refreshCache: ((STLocation) -> Unit?) = { STLocationManager.cacheLocation = it;Unit }

    override fun getLastKnownLocation(): STLocation = toLocation(locationClient.lastKnownLocation)

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
            locationClient.registerLocationListener(object : BDAbstractLocationListener() {
                /**
                 * errorCode 对照表 http://lbsyun.baidu.com/index.php?title=android-locsdk/guide/addition-func/error-code
                 */
                override fun onReceiveLocation(location: BDLocation?) {
                    locationClient.unRegisterLocationListener(this)
                    this@STLocationBaiduClient.stopLocation()

                    if (location?.locType == 61 || location?.locType == 161) {
                        val latLng = STLatLng(location.latitude, location.longitude, STLatLngType.BD09)
                        if (latLng.isValid()) {
                            STLogUtil.d(TAG, "[单次定位]定位成功, 有效经纬度:$latLng")
                            refreshCache.invoke(toLocation(location)) // 更新定位缓存

                            onSuccess?.invoke(toLocation(location))
                            return
                        }
                    }
                    STLogUtil.d(TAG, "定位失败[单次定位], location=$location")
                    onFailure?.invoke(location?.locType ?: 62, "[单次定位]定位失败, location=$location")

                }
            })

            locationClient.start()

            locationTimerDisposable = Observable.timer(timeout, TimeUnit.MILLISECONDS).subscribe {
                stopLocation()

                onFailure?.invoke(62, "[单次定位]LOCATION TIME OUT")
            }
        } else {
            onFailure?.invoke(167, "[单次定位]尚未开启定位权限")
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

    private fun toLocation(location: BDLocation): STLocation {
        return STLocation(location.latitude, location.longitude, location.radius, location)
    }

    private var onLoopLocationListener: BDAbstractLocationListener? = null
    override fun startLocationLoop(interval: Long, ensurePermissions: ((callback: (allPermissionsGranted: Boolean) -> Unit?) -> Unit?)?, onSuccess: ((location: STLocation) -> Unit?)?, onFailure: ((errorCode: Int, errorMessage: String) -> Unit?)?) {
        stopLocationLoop()

        locationLoopClient = LocationClient(STBaseApplication.INSTANCE).apply {

            locOption = LocationClientOption().apply {
                setScanSpan(800)
                locationMode = LocationClientOption.LocationMode.Hight_Accuracy
                setCoorType("bd09ll") //可选，默认gcj02，设置返回的定位结果坐标系
                setScanSpan(1000) //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
                setIsNeedAddress(this@STLocationBaiduClient.isNeedAddress)
                isOpenGps = true
                isLocationNotify = true
                setIsNeedLocationDescribe(true)
                setIsNeedLocationPoiList(true)
                setIgnoreKillProcess(false)
                SetIgnoreCacheException(false)
                setEnableSimulateGps(false)
            }

            onLoopLocationListener = object : BDAbstractLocationListener() {

                /**
                 * errorCode 对照表 http://lbsyun.baidu.com/index.php?title=android-locsdk/guide/addition-func/error-code
                 */
                override fun onLocDiagnosticMessage(locType: Int, diagnosticType: Int, diagnosticMessage: String?) {
                    super.onLocDiagnosticMessage(locType, diagnosticType, diagnosticMessage)
                    STLogUtil.w(TAG, "onLocDiagnosticMessage: locType=$locType, diagnosticType=$diagnosticType, diagnosticMessage=$diagnosticMessage")

                    // 	定位失败，请确认您定位的开关打开状态，是否赋予APP定位权限
                    if (locType == 167 && diagnosticType == 8) ensurePermissions?.invoke {
                        if (it) { // 开启定位权限后立即定位一次
                            this@apply.stop()
                            this@apply.start()
                        }
                    }
                }

                /**
                 * errorCode 对照表 http://lbsyun.baidu.com/index.php?title=android-locsdk/guide/addition-func/error-code
                 */
                override fun onReceiveLocation(location: BDLocation?) {
                    if (location?.locType == 61 || location?.locType == 161) {
                        val latLng = STLatLng(location.latitude, location.longitude, STLatLngType.BD09)
                        if (latLng.isValid()) {

                            STLogUtil.w(TAG, "[循环定位]定位成功, 有效经纬度:$latLng")
                            refreshCache.invoke(toLocation(location)) // 更新定位缓存

                            onSuccess?.invoke(toLocation(location))
                        } else {
                            STLogUtil.w(TAG, "[循环定位]定位成功, 无效经纬度:$latLng")
                            onFailure?.invoke(62, "UN_VALID LATLNG :$latLng")
                        }
                    } else {
                        STLogUtil.w(TAG, "[循环定位]定位失败, location=$location")
                        onFailure?.invoke(62, "[循环定位]定位失败, location=$location")
                    }
                }
            }
            this.registerLocationListener(onLoopLocationListener)
            this.start()
        }

    }

    /**
     * 强制停止单次有效定位
     */
    override fun stopLocation() {
        locationTimerDisposable?.dispose()
        locationClient.stop()
    }

    /**
     * 停止循环定位
     */
    override fun stopLocationLoop() {
        if (onLoopLocationListener != null) {
            locationLoopClient?.unRegisterLocationListener(onLoopLocationListener)
            onLoopLocationListener = null
        }
        locationLoopClient?.stop()
        locationLoopClient = null
    }
}