package com.smart.library.map.location

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.view.View
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import com.smart.library.base.STBaseApplication
import kotlin.math.abs

@SuppressLint("InflateParams")
class STLocationSensorManager(val context: Context, val map: BaiduMap, val callback: ((LatLng) -> Unit)? = null) : SensorEventListener, View.OnClickListener, BDAbstractLocationListener() {

    private var lastSensorX: Float = 0f
    private var currentLat: Double = 0.0
    private var currentLng: Double = 0.0
    private var currentAccuracy: Float = 0f
    private var currentDirection: Float = 0f
    private val sensorManager by lazy { STBaseApplication.INSTANCE.getSystemService(SENSOR_SERVICE) as? SensorManager? }
    private val locationClient by lazy { LocationClient(STBaseApplication.INSTANCE).apply { locOption = locationOptions } }

    private val locationOptions by lazy {
        val option = LocationClientOption()
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.locationMode = LocationClientOption.LocationMode.Hight_Accuracy
        //可选，默认gcj02，设置返回的定位结果坐标系
        option.setCoorType("bd09ll")
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setScanSpan(1000)
        //可选，设置是否需要地址信息，默认不需要
        option.setIsNeedAddress(true)
        //可选，默认false,设置是否使用gps
        option.isOpenGps = true
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.isLocationNotify = true
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationDescribe(true)
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIsNeedLocationPoiList(true)
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setIgnoreKillProcess(false)
        //可选，默认false，设置是否收集CRASH信息，默认收集
        option.SetIgnoreCacheException(false)
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        option.setEnableSimulateGps(false)
        option
    }

    init {
        this.map.isMyLocationEnabled = true
        this.map.setMyLocationConfiguration(MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, null))
        this.locationClient.registerLocationListener(this)
    }

    fun startLocation() {
        stopLocation()
        this.map.isMyLocationEnabled = true
        this.locationClient.registerLocationListener(this)
        this.locationClient.start()
        startSensorService()
    }

    fun stopLocation() {
        this.map.isMyLocationEnabled = false
        this.locationClient.unRegisterLocationListener(this)
        this.locationClient.stop()
        stopSensorService()
    }


    override fun onClick(view: View) {
        this.animateMapToMyLocation(LatLng(this.currentLat, this.currentLng), this.map.mapStatus.zoom)
    }

    override fun onReceiveLocation(bdLocation: BDLocation?) {
        if (bdLocation != null) {
            this.currentLat = bdLocation.latitude
            this.currentLng = bdLocation.longitude
            this.currentAccuracy = bdLocation.radius
            this.map.setMyLocationData(
                    MyLocationData.Builder()
                            .accuracy(this.currentAccuracy)
                            .direction(this.currentDirection)
                            .latitude(this.currentLat)
                            .longitude(this.currentLng).build()
            )
            this.callback?.invoke(LatLng(this.currentLat, this.currentLng))
        }
    }

    private fun animateMapToMyLocation(latLng: LatLng?, _zoomLevel: Float) {
        var zoomLevel = _zoomLevel
        if (latLng != null) {
            val maxZoomLevel = this.map.maxZoomLevel
            val minZoomLevel = this.map.minZoomLevel
            if (zoomLevel > maxZoomLevel || zoomLevel < minZoomLevel) {
                zoomLevel = 18.0f
            }
            val builder = MapStatus.Builder()
            builder.target(latLng).zoom(zoomLevel)
            this.map.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()))
        }
    }
    // -- 方向感应

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    @Suppress("DEPRECATION")
    override fun onSensorChanged(sensorEvent: SensorEvent?) {
        if (sensorEvent != null) {
            val x = sensorEvent.values[SensorManager.DATA_X]
            if (abs(x - lastSensorX) > 1.0) {
                currentDirection = x
                map.setMyLocationData(
                        MyLocationData.Builder()
                                .accuracy(currentAccuracy)
                                .direction(currentDirection)
                                .latitude(currentLat)
                                .longitude(currentLng)
                                .build()
                )
            }
            lastSensorX = x
        }
    }

    @Suppress("DEPRECATION")
    private fun startSensorService() = sensorManager?.let { it.registerListener(this@STLocationSensorManager, it.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_UI) }

    private fun stopSensorService() = sensorManager?.unregisterListener(this)

}