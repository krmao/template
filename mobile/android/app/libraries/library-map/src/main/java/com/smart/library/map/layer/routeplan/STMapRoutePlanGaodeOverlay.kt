package com.smart.library.map.layer.routeplan

import android.graphics.Color
import com.amap.api.maps.AMap
import com.amap.api.maps.model.*
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.route.DrivePath
import com.amap.api.services.route.TMC
import com.smart.library.map.R
import com.smart.library.map.model.STLatLng
import org.jetbrains.anko.collections.forEachWithIndex
import java.util.*

fun LatLonPoint.latLng() = LatLng(this.latitude, this.longitude)
fun LatLng.latLngPoint() = LatLonPoint(this.latitude, this.longitude)
fun LatLng.isValid() = STLatLng.isValidLatLng(this.latitude, this.longitude)

@Suppress("MemberVisibilityCanBePrivate", "unused")
class STMapRoutePlanGaodeOverlay @JvmOverloads constructor(val map: AMap, val path: DrivePath?, val startPoint: LatLng, val endPoint: LatLng, val throughPointList: List<LatLonPoint> = arrayListOf()) {

    companion object {

        val colorSelected = Color.parseColor("#36C77B")
        val colorUnSelected = Color.parseColor("#5036C77B") // 不透明度 80%

        internal val stationNodeBitmapDescriptor: BitmapDescriptor by lazy { BitmapDescriptorFactory.fromResource(R.drawable.map_route_green) }
        internal val greenBitmapDescriptor: BitmapDescriptor by lazy { BitmapDescriptorFactory.fromResource(R.drawable.map_route_green) }
        internal val greenSlowBitmapDescriptor: BitmapDescriptor by lazy { BitmapDescriptorFactory.fromResource(R.drawable.map_route_green_light) }
        internal val orangeBitmapDescriptor: BitmapDescriptor by lazy { BitmapDescriptorFactory.fromResource(R.drawable.map_route_orange) }
        internal val redBitmapDescriptor: BitmapDescriptor by lazy { BitmapDescriptorFactory.fromResource(R.drawable.map_route_red) }

        internal fun getTexture(status: String): BitmapDescriptor {
            return when (status) {
                "畅通" -> greenBitmapDescriptor
                "缓行" -> greenSlowBitmapDescriptor
                "拥堵" -> orangeBitmapDescriptor
                "严重拥堵" -> redBitmapDescriptor
                else -> greenBitmapDescriptor
            }
        }
    }

    val polylineList: MutableList<Polyline> = ArrayList()
    val polylineOptionsTexture: PolylineOptions by lazy {
        PolylineOptions().apply {
            width(polylineWidth)
            isUseTexture = true
            geodesic(true)
            transparency(polylineTransparency)
            zIndex(polylineZIndex)
        }
    }
    val polylineOptionsColor: PolylineOptions by lazy {
        PolylineOptions().apply {
            width(32f)
            color(polylineColor)
            //isUseTexture = false
            geodesic(true)
            transparency(polylineTransparency)
            zIndex(polylineZIndex)
        }
    }

    var polylineColor = colorSelected
        set(value) {
            polylineOptionsColor.color(value)
            field = value
        }
    var polylineWidth = 180f
        set(value) {
            polylineOptionsColor.width(value)
            polylineOptionsTexture.width(value)
            field = value
        }
    var polylineTransparency = 1.0f
        set(value) {
            polylineOptionsColor.transparency(value)
            polylineOptionsTexture.transparency(value)
            field = value
        }
    var polylineZIndex = 0.0f
        set(value) {
            polylineOptionsColor.zIndex(value)
            polylineOptionsTexture.zIndex(value)
            field = value
        }
    var enablePolylineWithTrafficStatus = false

    val stationNodeMarkers: MutableList<Marker> = ArrayList()

    var distance: Float = 0f // m

    var enableStationNode = false        // 路段节点图标是否启用
    var enableStationNodeVisible = false // 路段节点图标控制显示接口
        set(value) {
            this.stationNodeMarkers.forEach { it.isVisible = value }
            field = value
        }

    private fun showPolyline() = addPolyLine(polylineOptionsColor)
    private fun showPolylineTexture() = addPolyLine(polylineOptionsTexture)

    private fun addStationMarker(options: MarkerOptions?) = options.let { map.addMarker(it)?.let { stationNodeMarkers.add(it) } }
    private fun addPolyLine(options: PolylineOptions?) = options.let { map.addPolyline(it)?.let { polylineList.add(it) } }
    private fun addDrivingStationMarkers(latLng: LatLng) = addStationMarker(MarkerOptions().position(latLng).visible(enableStationNodeVisible).anchor(0.5f, 0.5f).icon(stationNodeBitmapDescriptor))

    private fun textureWayUpdate(tmcSection: List<TMC>) {
        if (tmcSection.isNotEmpty()) {
            polylineOptionsTexture.add(startPoint)

            tmcSection[0].polyline.firstOrNull()?.let {
                polylineOptionsTexture.add(it.latLng())
            }
            val customTextureList = arrayListOf<BitmapDescriptor>()
            tmcSection.forEach { section ->
                section.polyline.forEachWithIndex { _, polyline ->
                    polylineOptionsTexture.add(polyline.latLng())
                    customTextureList.add(getTexture(section.status))
                }
            }
            polylineOptionsTexture.add(endPoint)
            polylineOptionsTexture.customTextureList = customTextureList
            polylineOptionsTexture.customTextureIndex = customTextureList.mapIndexed { index, _ -> index } //设置纹理对应的Index
        }
    }

    fun addToMap(): STMapRoutePlanGaodeOverlay {
        distance = 0f

        if (polylineWidth > 0f && path != null) {
            val tmcList: MutableList<TMC> = ArrayList()

            polylineOptionsColor.add(startPoint)
            path.steps.forEach {

                distance += it.distance

                tmcList.addAll(it.tmCs)
                if (it.polyline.isNotEmpty()) {

                    if (enableStationNode) addDrivingStationMarkers(it.polyline[0].latLng())

                    it.polyline.forEach {
                        it.latLng().let {
                            polylineOptionsColor.add(it)
                        }
                    }
                }
            }
            polylineOptionsColor.add(endPoint)

            if (enablePolylineWithTrafficStatus && tmcList.isNotEmpty()) {
                textureWayUpdate(tmcList)
                showPolylineTexture()
            } else {
                showPolyline()
            }
        }
        return this
    }

    fun removeFromMap() {
        // 删除路线节点
        stationNodeMarkers.forEach { it.remove() }
        stationNodeMarkers.clear()

        polylineList.forEach { it.remove() }
        polylineList.clear()
    }
}