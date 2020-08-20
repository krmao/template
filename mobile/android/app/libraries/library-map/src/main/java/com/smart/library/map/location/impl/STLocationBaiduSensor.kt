package com.smart.library.map.location.impl

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.SENSOR_SERVICE
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.view.LayoutInflater
import android.view.View
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import com.smart.library.base.STBaseApplication
import com.smart.library.map.R
import com.smart.library.map.location.STLocation
import com.smart.library.util.STLogUtil
import kotlin.math.abs

@SuppressLint("InflateParams")
class STLocationBaiduSensor(val context: Context?, val map: BaiduMap, val callback: ((STLocation) -> Unit)? = null) : SensorEventListener, View.OnClickListener {

    private var lastSensorX: Float = 0f
    private var currentLat: Double = 0.0
    private var currentLng: Double = 0.0
    private var currentAccuracy: Float = 0f
    private var currentDirection: Float = 0f
    private val sensorManager by lazy { STBaseApplication.INSTANCE.getSystemService(SENSOR_SERVICE) as? SensorManager? }
    private val locationBaiduClient by lazy { STLocationBaiduClient(false) }

    init {
        this.map.isMyLocationEnabled = true
        this.map.setMyLocationConfiguration(MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, BitmapDescriptorFactory.fromView(LayoutInflater.from(context).inflate(R.layout.st_location_sensor_layout, null, false)), Color.parseColor("#1A0099FF"), Color.TRANSPARENT))
        // this.map.setMyLocationConfiguration(MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, BitmapDescriptorFactory.fromResource(R.drawable.st_map_location_with_sensor), Color.parseColor("#1A0099FF"), Color.TRANSPARENT))
    }

    fun startLocation() {
        stopLocation()
        this.map.isMyLocationEnabled = true
        this.locationBaiduClient.startLocationLoop(800,
            onSuccess = { location: STLocation ->
                STLogUtil.d("location", "sensor location loop callback success, callback=$callback, location=$location")
                this.currentLat = location.latitude
                this.currentLng = location.longitude
                this.currentAccuracy = location.accuracy
                this.map.setMyLocationData(
                    MyLocationData.Builder()
                        .accuracy(this.currentAccuracy)
                        .direction(this.currentDirection)
                        .latitude(this.currentLat)
                        .longitude(this.currentLng).build()
                )
                this.callback?.invoke(location)
            },
            onFailure = { errorCode: Int, errorMessage: String ->
                STLogUtil.e("location", "sensor location loop callback failure, errorCode=$errorCode, errorMessage=$errorMessage")
            })
        startSensorService()
    }

    fun stopLocation() {
        this.map.isMyLocationEnabled = false
        this.locationBaiduClient.stopLocationLoop()
        stopSensorService()
    }


    override fun onClick(view: View) {
        this.animateMapToMyLocation(LatLng(this.currentLat, this.currentLng), this.map.mapStatus.zoom)
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
    private fun startSensorService() = sensorManager?.let { it.registerListener(this@STLocationBaiduSensor, it.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_UI) }

    private fun stopSensorService() = sensorManager?.unregisterListener(this)

}