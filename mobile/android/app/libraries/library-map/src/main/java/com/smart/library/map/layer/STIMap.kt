package com.smart.library.map.layer

import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.view.View
import com.baidu.mapapi.model.LatLngBounds
import com.smart.library.map.model.STLatLng
import com.smart.library.map.model.STLatLngBounds
import com.smart.library.map.model.STMapType
import com.smart.library.map.model.STMarker

interface STIMap {

    fun latestLatLon(): STLatLng?

    fun mapType(): STMapType

    fun switchTheme()

    fun mapView(): View

    fun onCreate(context: Context?, savedInstanceState: Bundle?)
    fun onSaveInstanceState(outState: Bundle)
    fun onResume()
    fun onPause()
    fun onDestroy()

    fun setOnMapLoadedCallback(onMapLoaded: () -> Unit)

    fun onLocationButtonClickedListener(): View.OnClickListener

    /**
     * 指南针是否生效
     */
    fun enableCompass(enable: Boolean)

    /**
     * 地图的zoomLevel
     *
     * @param zoomLevel zoomLevel
     */
    fun setZoomLevel(zoomLevel: Float)

    /**
     * 最大/小zoom
     */
    fun setMaxAndMinZoomLevel(maxZoomLevel: Float, minZoomLevel: Float)

    /**
     * 比例尺
     */
    fun enableMapScaleControl(enable: Boolean)

    /**
     * 地图图层旋转
     */
    fun enableRotate(enable: Boolean)

    fun setMapCenter(padding: Map<String, Int> = mapOf(), animate: Boolean = true, vararg latLng: STLatLng?)
    fun setMapCenter(animate: Boolean, zoomLevel: Float, latLng: STLatLng?)
    fun setMapCenter(padding: Map<String, Int>, animate: Boolean, swLatLng: STLatLng?, neLatLng: STLatLng?)

    fun getCurrentMapZoomLevel(): Float

    /**
     * 清除所有的Marker
     */
    fun clear()

    /**
     * 移除marker
     */
    fun removeMarker(marker: STMarker?)

    /**
     * 判断坐标点是否在屏幕地理范围内
     */
    fun isLatLngInScreen(latLng: STLatLng?, callback: (result: Boolean) -> Unit)

    /**
     * 获取当前地图范围
     */
    fun getCurrentMapStatus(callback: (centerLatLng: STLatLng, zoomLevel: Float, radius: Double, bounds: STLatLngBounds) -> Unit)

    fun getCurrentMapCenterLatLng(): STLatLng

    fun getCurrentMapLatLngBounds(): STLatLngBounds

    fun getCurrentMapRadius(): Double

    /**
     * 经纬度转换为屏幕坐标
     */
    fun convertLatLngToScreenCoordinate(latLng: STLatLng?, callback: (Point?) -> Unit)

    /**
     * 屏幕坐标转换为经纬度
     */
    fun convertScreenCoordinateToLatLng(point: Point?, callback: (STLatLng?) -> Unit)

}