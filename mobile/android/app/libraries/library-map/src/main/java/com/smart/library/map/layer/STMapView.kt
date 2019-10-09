package com.smart.library.map.layer

import android.content.Context
import android.graphics.Point
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.smart.library.map.layer.impl.STMapBaiduView
import com.smart.library.map.model.*

class STMapView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr), STIMap {
    companion object {
        const val defaultZoomLevel = 14f
        @JvmStatic
        val defaultLatLngTianAnMen = STLatLng(39.920116, 116.403703, STLatLngType.BD09) // 天安门
    }

    @JvmOverloads
    fun initialize(mapType: STMapType = STMapType.BAIDU, initLatLon: STLatLng = defaultLatLngTianAnMen, initZoomLevel: Float = defaultZoomLevel) {
        when (mapType) {
            STMapType.BAIDU -> addView(STMapBaiduView(context, initLatLon = initLatLon, initZoomLevel = initZoomLevel))
            else -> addView(STMapBaiduView(context, initLatLon = initLatLon, initZoomLevel = initZoomLevel))
        }

        addView(STMapControlView(context, map = map()))
    }

    private val map: STIMap by lazy { getChildAt(0) as STIMap }
    private val controlView: STMapControlView by lazy { getChildAt(1) as STMapControlView }

    private fun controlView(): STMapControlView = controlView
    fun map(): STIMap = map

    override fun mapView(): View = map().mapView()

    override fun onResume() {
        map().onResume()
        controlView().onResume()
    }

    override fun onPause() {
        map().onPause()
        controlView().onPause()
    }

    override fun onDestroy() = map().onDestroy()

    override fun enableCompass(enable: Boolean) = map().enableCompass(enable)

    override fun setZoomLevel(zoomLevel: Float) = map().setZoomLevel(zoomLevel)

    override fun setMaxAndMinZoomLevel(maxZoomLevel: Float, minZoomLevel: Float) = map().setMaxAndMinZoomLevel(maxZoomLevel, minZoomLevel)

    override fun enableMapScaleControl(enable: Boolean) = map().enableMapScaleControl(enable)

    override fun enableRotate(enable: Boolean) = map().enableRotate(enable)

    override fun setMapCenter(padding: Map<String, Int>, animate: Boolean, vararg latLng: STLatLng?) = map().setMapCenter(padding, animate, *latLng)

    override fun setMapCenter(animate: Boolean, zoomLevel: Float, latLng: STLatLng?) = map().setMapCenter(animate, zoomLevel, latLng)

    override fun setMapCenter(padding: Map<String, Int>, animate: Boolean, swLatLng: STLatLng?, neLatLng: STLatLng?) = map().setMapCenter(padding, animate, swLatLng, neLatLng)

    override fun getCurrentMapZoomLevel(): Float = map().getCurrentMapZoomLevel()

    override fun clear() = map().clear()

    override fun removeMarker(marker: STMarker?) = map().removeMarker(marker)

    override fun isLatLngInScreen(latLng: STLatLng?, callback: (result: Boolean) -> Unit) = map().isLatLngInScreen(latLng, callback)

    override fun getCurrentMapStatus(callback: (centerLatLng: STLatLng, zoomLevel: Float, radius: Double, bounds: STLatLngBounds) -> Unit) = map().getCurrentMapStatus(callback)

    override fun getCurrentMapCenterLatLng(): STLatLng = map().getCurrentMapCenterLatLng()

    override fun getCurrentMapLatLngBounds(): STLatLngBounds = map().getCurrentMapLatLngBounds()

    override fun getCurrentMapRadius(): Double = map().getCurrentMapRadius()

    override fun convertLatLngToScreenCoordinate(latLng: STLatLng?, callback: (Point?) -> Unit) = map().convertLatLngToScreenCoordinate(latLng, callback)

    override fun convertScreenCoordinateToLatLng(point: Point?, callback: (STLatLng?) -> Unit) = map().convertScreenCoordinateToLatLng(point, callback)
}