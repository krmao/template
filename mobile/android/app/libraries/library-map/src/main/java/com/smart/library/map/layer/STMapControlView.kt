package com.smart.library.map.layer

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.baidu.mapapi.map.MapView
import com.baidu.mapapi.model.LatLng
import com.smart.library.map.R
import com.smart.library.map.location.STLocationManager
import com.smart.library.map.location.STLocationSensorManager
import com.smart.library.map.model.STLatLng
import com.smart.library.util.STLogUtil
import kotlinx.android.synthetic.main.st_map_view_control_layout.view.*


@SuppressLint("ViewConstructor")
internal class STMapControlView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, map: STIMap) : RelativeLayout(context, attrs, defStyleAttr) {

    private var locationSensorManager: STLocationSensorManager? = null
    private var currentLatLon: LatLng? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.st_map_view_control_layout, this, true)
        locationSensorManager = STLocationSensorManager(context, (map.mapView() as MapView).map) {
            if (STLatLng.isValidLatLng(it.latitude, it.longitude)) {
                currentLatLon = it
            }
        }
        locationBtn.setOnClickListener(locationSensorManager)
    }

    fun onResume() {
        STLogUtil.d("location", "ensurePermissions ...")
        STLocationManager.ensurePermissionsWithoutHandling(context as? Activity) {
            STLogUtil.d("location", "ensurePermissions:$it")
            if (it) {
                locationSensorManager?.startLocation()
            }
        }
    }

    fun onPause() {
        locationSensorManager?.stopLocation()
    }
}