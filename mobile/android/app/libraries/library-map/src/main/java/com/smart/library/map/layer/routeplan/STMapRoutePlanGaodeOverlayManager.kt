package com.smart.library.map.layer.routeplan

import android.util.SparseArray
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.*
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.route.DrivePath
import com.smart.library.map.R
import com.smart.library.util.STLogUtil
import com.smart.library.util.STSystemUtil
import org.jetbrains.anko.collections.forEachWithIndex
import java.util.*


@Suppress("MemberVisibilityCanBePrivate", "unused")
class STMapRoutePlanGaodeOverlayManager(val map: AMap, val pathList: MutableList<DrivePath>?, val startPoint: LatLng, val endPoint: LatLng, val throughPointList: List<LatLonPoint> = arrayListOf(), var onPathSelected: ((STMapRoutePlanGaodeOverlay) -> Unit?)? = null) {

    companion object {
        private val throughPointBitmapDescriptor: BitmapDescriptor by lazy { BitmapDescriptorFactory.fromResource(R.drawable.map_marker_icon_to_she_qu) }
        private val fromDestinationBitmapDescriptor: BitmapDescriptor by lazy { BitmapDescriptorFactory.fromResource(R.drawable.map_marker_icon_from) }
        private val toDestinationZongHeBitmapDescriptor: BitmapDescriptor by lazy { BitmapDescriptorFactory.fromResource(R.drawable.map_marker_icon_to_zong_he) }
        private val toDestinationSheQuBitmapDescriptor: BitmapDescriptor by lazy { BitmapDescriptorFactory.fromResource(R.drawable.map_marker_icon_to_she_qu) }
        private val routePathStartBitmapDescriptor: BitmapDescriptor by lazy { BitmapDescriptorFactory.fromResource(R.drawable.map_route_start) }
        private val routePathEndBitmapDescriptor: BitmapDescriptor by lazy { BitmapDescriptorFactory.fromResource(R.drawable.map_route_end) }
    }

    var enableThroughPointVisible = true
        set(value) {
            throughPointMarkerList.forEach { it.isVisible = value }
            field = value
        }

    // 必定经过的点
    private val throughPointMarkerList = ArrayList<Marker>()

    private var fromDestinationMarker: Marker? = null                   // 从哪个目的地
    private var toDestinationMarker: Marker? = null                     // 去往哪个目的地
    private var routePathStartMarker: Marker? = null                    // 路径规划起点
    private var routePathEndMarker: Marker? = null                      // 路径规划终点

    private val routePlanningArray: SparseArray<STMapRoutePlanGaodeOverlay> = SparseArray()

    fun addToMap() {
        pathList?.forEachWithIndex { index, it ->
            // 绘制路线+节点markers
            routePlanningArray.put(index, STMapRoutePlanGaodeOverlay(map, it, startPoint, endPoint).apply {
                this.enablePolylineWithTrafficStatus = false
                this.polylineWidth = STSystemUtil.getPxFromDp(10f)
                this.polylineZIndex = if (index == 0) 1.0f else 0.0f
                this.polylineTransparency = if (index == 0) 1.0f else 0.4f
                this.polylineColor = if (index == 0) STMapRoutePlanGaodeOverlay.colorSelected else STMapRoutePlanGaodeOverlay.colorUnSelected
                this.addToMap()

                if (index == 0) {
                    onPathSelected?.invoke(this)
                }
            })
            // 自适应地图
            zoomToSpan()
            // 绘制必经点
            addThroughPointMarkers()
            // 绘制路线起始点markers
            addRoutePathStartEndMarkers()
            // 绘制目的地起点和终点
            addFromToDestinationMarkers()
        }
    }

    fun selectPolyline(selectedPolyline: Polyline?) {
        selectedPolyline?.let {
            val selectedPolylineStart = selectedPolyline.points.firstOrNull()
            val selectedPolylineEnd = selectedPolyline.points.lastOrNull()
            if (selectedPolylineStart != null && selectedPolylineEnd != null) {
                for (index in 0 until routePlanningArray.size()) {
                    routePlanningArray.get(routePlanningArray.keyAt(index)).let { routePlanningOverlay ->
                        routePlanningOverlay.polylineList.firstOrNull { it.points.firstOrNull() == selectedPolylineStart && it.points.lastOrNull() == selectedPolylineEnd }?.let {
                            STLogUtil.e("[MAP]", "检测到起点和终点相等的路段 id=${it.id}")
                            it.zIndex = if (selectedPolyline == it) 1.0f else 0.0f
                            it.color = if (selectedPolyline == it) STMapRoutePlanGaodeOverlay.colorSelected else STMapRoutePlanGaodeOverlay.colorUnSelected
                            it.setTransparency(if (selectedPolyline == it) 1.0f else 0.4f)

                            if (selectedPolyline == it) {
                                onPathSelected?.invoke(routePlanningOverlay)
                            }
                        }
                    }
                }
            }
        }
    }

    fun removeDrivingPath(key: Int) {
        routePlanningArray.get(key)?.removeFromMap()
        routePlanningArray.remove(key)
    }

    fun removeAllDrivingPath() {
        for (index in 0 until routePlanningArray.size()) routePlanningArray.get(routePlanningArray.keyAt(index))?.removeFromMap()
        routePlanningArray.clear()
    }

    fun zoomToSpan() = map.animateCamera(CameraUpdateFactory.newLatLngBounds(getLatLngBounds(), STSystemUtil.getPxFromDp(100f).toInt()))
    private fun getLatLngBounds(): LatLngBounds = LatLngBounds.builder().apply { include(startPoint);include(endPoint);throughPointList.forEach { include(LatLng(it.latitude, it.longitude)) } }.build()

    fun addThroughPointMarkers() {
        removeThroughPointMarkers()
        throughPointMarkerList.addAll(throughPointList.map { map.addMarker(MarkerOptions().position(LatLng(it.latitude, it.longitude)).visible(enableThroughPointVisible).icon(throughPointBitmapDescriptor)) })
    }

    fun removeThroughPointMarkers() {
        throughPointMarkerList.forEach { it.remove() }
        throughPointMarkerList.clear()
    }

    fun addRoutePathStartEndMarkers() {
        removeRoutePathStartEndMarkers()
        routePathStartMarker = map.addMarker(MarkerOptions().position(startPoint).anchor(0.5f, 0.5f).icon(routePathStartBitmapDescriptor))
        routePathEndMarker = map.addMarker(MarkerOptions().position(endPoint).anchor(0.5f, 0.5f).icon(routePathEndBitmapDescriptor))
    }

    fun removeRoutePathStartEndMarkers() {
        routePathStartMarker?.remove()
        routePathEndMarker?.remove()
        routePathStartMarker = null
        routePathEndMarker = null

    }

    fun addFromToDestinationMarkers() {
        removeFromToDestinationMarkers()
        fromDestinationMarker = map.addMarker(MarkerOptions().position(startPoint).anchor(0.5f, 1f).icon(fromDestinationBitmapDescriptor))
        // toDestinationMarker = map.addMarker(MarkerOptions().position(endPoint).anchor(0.5f, 1f).icon(toDestinationZongHeBitmapDescriptor))
    }

    fun removeFromToDestinationMarkers() {
        fromDestinationMarker?.remove()
        toDestinationMarker?.remove()
        fromDestinationMarker = null
        toDestinationMarker = null
    }

    fun removeFromMap() {
        // 删除必经站点
        removeThroughPointMarkers()
        // 删除起始点 marker
        removeRoutePathStartEndMarkers()
        removeFromToDestinationMarkers()
        // 删除路线
        removeAllDrivingPath()
    }
}