package com.smart.library.map.layer

import android.graphics.Point
import com.smart.library.map.model.STLatLng
import com.smart.library.map.model.STLatLngBounds
import com.smart.library.map.model.STMarker

interface STIMap {

    fun onResume()
    fun onPause()
    fun onDestroy()

    /**
     * 指南针是否生效
     */
    fun enableCompass(enable: Boolean)

    /**
     * 地图的zoomLevel
     *
     * @param zoomLevel zoomLevel
     */
    fun setZoomLevel(zoomLevel: Double)

    /**
     * 最大/小zoom
     */
    fun setMaxAndMinZoomLevel(maxZoomLevel: Int, minZoomLevel: Int)

    /**
     * 比例尺
     */
    fun enableMapScaleControl(enable: Boolean)

    /**
     * 地图图层旋转
     */
    fun enableRotate(enable: Boolean)

    fun setMapCenter(padding: Map<String, Int> = mapOf(), animate: Boolean = true, vararg latLng: STLatLng?)
    fun setMapCenter(padding: Map<String, Int> = mapOf(), animate: Boolean = true, zoomLevel: Double, vararg latLng: STLatLng?)
    fun setMapCenter(padding: Map<String, Int> = mapOf(), animate: Boolean = true, swLatLng: STLatLng, neLatLng: STLatLng)

    /**
     * 获取当前图层所有marker
     */
    fun getAllMarkers(): List<STMarker>

    /**
     * 清除所有的Marker
     */
    fun clearAllMarkers()

    /**
     * 移除marker
     */
    fun removeMarker(marker: STMarker)

    /**
     * 清除所有的Router
     */
    fun clearAllRouters()

    /**
     * 自定义画线
     */
    fun drawPolyline(latLngList: List<STLatLng>, color: Int, width: Int, isDash: Boolean, clearPreRoute: Boolean)

    /**
     * 自定义画线
     */
    fun drawArcLine(startLatLng: STLatLng, endLatLng: STLatLng, color: Int, width: Int, isDash: Boolean, clearPre: Boolean)

    /**
     * 清除所有画线
     */
    fun clearAllPolyLineView()

    /**
     * 判断坐标点是否在屏幕地理范围内
     */
    fun isLatLonInScreen(latLng: STLatLng, callback: (result: Boolean) -> Unit)

    /**
     * 获取当前地图范围
     */
    fun getCurrentProperties(callback: (centerLatLon: STLatLng, zoomLevel: Float, radius: Float, bounds: STLatLngBounds) -> Unit)

    /**
     * 经纬度转换为屏幕坐标
     */
    fun convertLatLngToScreenCoordinate(latLng: STLatLng, callback: (Point) -> Unit)

    /**
     * 屏幕坐标转换为经纬度
     */
    fun convertScreenCoordinateToLatLng(point: Point, callback: (STLatLng) -> Unit)

}