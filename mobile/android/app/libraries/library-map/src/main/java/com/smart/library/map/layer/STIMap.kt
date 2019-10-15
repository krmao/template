package com.smart.library.map.layer

import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.view.View
import com.smart.library.map.model.STLatLng
import com.smart.library.map.model.STLatLngBounds
import com.smart.library.map.model.STMapType
import com.smart.library.map.model.STMarker

interface STIMap {

    fun onCreate(context: Context?, savedInstanceState: Bundle?)
    fun onSaveInstanceState(outState: Bundle)
    fun onResume()
    fun onPause()
    fun onDestroy()


    fun clear()
    fun switchTheme()
    fun mapView(): View
    fun mapType(): STMapType
    fun latestLatLon(): STLatLng?
    fun removeMarker(marker: STMarker?)
    fun isLatLngInScreen(latLng: STLatLng?, callback: (result: Boolean) -> Unit)

    fun setOnMapLoadedCallback(onMapLoaded: () -> Unit)
    fun onLocationButtonClickedListener(): View.OnClickListener
    fun onLocationButtonLongClickedListener(): View.OnLongClickListener
    fun setOnLocationChangedListener(onLocationChanged: (STLatLng?) -> Unit)

    fun showMapPoi(showMapPoi: Boolean)
    fun isShowMapPoi(): Boolean
    fun showBuildings(isBuildingsEnabled: Boolean)
    fun isBuildingsEnabled(): Boolean
    fun enableCompass(enable: Boolean)
    fun enableTraffic(enable: Boolean)
    fun isTrafficEnabled(): Boolean
    fun setZoomLevel(zoomLevel: Float, animate: Boolean)
    fun setMaxAndMinZoomLevel(maxZoomLevel: Float, minZoomLevel: Float)
    fun enableMapScaleControl(enable: Boolean)
    fun enableRotate(enable: Boolean)

    fun setMapCenter(latLng: STLatLng?, animate: Boolean)
    fun setMapCenter(latLng: STLatLng?, zoomLevel: Float, animate: Boolean)
    fun setMapCenter(padding: Map<String, Int> = mapOf(), animate: Boolean, vararg latLng: STLatLng?)
    fun setMapCenter(animate: Boolean, zoomLevel: Float, latLng: STLatLng?)
    fun setMapCenter(padding: Map<String, Int>, animate: Boolean, swLatLng: STLatLng?, neLatLng: STLatLng?)

    fun getCurrentMapOptions(): STMapOptions
    fun getCurrentMapRadius(): Double
    fun getCurrentMapZoomLevel(): Float
    fun getCurrentMapCenterLatLng(): STLatLng
    fun getCurrentMapLatLngBounds(): STLatLngBounds
    fun getCurrentMapStatus(callback: (centerLatLng: STLatLng, zoomLevel: Float, radius: Double, bounds: STLatLngBounds) -> Unit)

    fun convertLatLngToScreenCoordinate(latLng: STLatLng?, callback: (Point?) -> Unit)
    fun convertScreenCoordinateToLatLng(point: Point?, callback: (STLatLng?) -> Unit)

}