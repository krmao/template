package com.smart.library.map.layer

import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.smart.library.map.layer.impl.STMapBaiduView
import com.smart.library.map.layer.impl.STMapGaodeView
import com.smart.library.map.model.*
import com.smart.library.util.STViewUtil

@Suppress("MemberVisibilityCanBePrivate", "unused")
class STMapView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr), STIMap {

    private lateinit var controlView: STMapControlView
    private var initMapOptions: STMapOptions = STMapOptions(showMapPoi = false)
    private fun controlView(): STMapControlView = controlView
    private fun map(): STIMap = getChildAt(0) as STIMap

    @JvmOverloads
    fun initialize(mapType: STMapType = STMapType.BAIDU, initMapOptions: STMapOptions = this.initMapOptions) {
        this.initMapOptions = initMapOptions

        when (mapType) {
            STMapType.BAIDU -> addView(STMapBaiduView(context, initMapOptions = initMapOptions))
            STMapType.GAODE -> addView(STMapGaodeView(context, initMapOptions = initMapOptions))
            else -> addView(STMapBaiduView(context, initMapOptions = initMapOptions))
        }

        controlView = STMapControlView(context)
        addView(controlView())

        controlView().showLoading()
        map().setOnMapLoadedCallback {
            controlView().setButtonClickedListener(this)
            controlView().hideLoading()
        }
    }

    @JvmOverloads
    fun switchTo(toMapType: STMapType, callback: ((switchSuccess: Boolean, newMapType: STMapType) -> Unit)? = null) {
        val controlView: STMapControlView = controlView()
        controlView.showLoading()
        synchronized(this) {
            if (map().mapType() != toMapType) {
                val oldMapView: View = getChildAt(0)
                val oldMap: STIMap = oldMapView as STIMap

                // 切换地图后还原状态
                val oldMapOptions: STMapOptions = oldMap.getCurrentMapOptions()

                val newMapView: View
                when (toMapType) {
                    STMapType.BAIDU -> {
                        newMapView = STMapBaiduView(context, initMapOptions = oldMapOptions)
                        addView(newMapView, 0)
                    }
                    STMapType.GAODE -> {
                        newMapView = STMapGaodeView(context, initMapOptions = oldMapOptions)
                        addView(newMapView, 0)
                    }
                    else -> {
                        newMapView = STMapBaiduView(context, initMapOptions = oldMapOptions)
                        addView(newMapView, 0)
                    }
                }

                val newMap: STIMap = newMapView as STIMap
                newMap.onCreate(context, null)
                newMap.onResume()
                newMap.setOnMapLoadedCallback {
                    STViewUtil.animateAlphaToVisibility(View.GONE, 300, {
                        oldMap.onPause()
                        oldMap.onDestroy()
                        removeView(oldMapView)
                        controlView.setButtonClickedListener(this)

                        callback?.invoke(true, toMapType)

                        controlView.hideLoading()
                    }, oldMapView)
                }
            } else {
                callback?.invoke(false, toMapType)
            }
        }
    }

    override fun onCreate(context: Context?, savedInstanceState: Bundle?) = map().onCreate(context, savedInstanceState)
    override fun onSaveInstanceState(outState: Bundle) = map().onSaveInstanceState(outState)
    override fun onResume() = map().onResume()
    override fun onPause() = map().onPause()
    override fun onDestroy() = map().onDestroy()

    override fun showMapPoi(showMapPoi: Boolean) = map().showMapPoi(showMapPoi)
    override fun isShowMapPoi(): Boolean = map().isShowMapPoi()
    override fun showBuildings(isBuildingsEnabled: Boolean) = map().showBuildings(isBuildingsEnabled)
    override fun isBuildingsEnabled(): Boolean = map().isBuildingsEnabled()
    override fun clear() = map().clear()
    override fun switchTheme() = map().switchTheme()
    override fun mapView(): View = map().mapView()
    override fun mapType(): STMapType = map().mapType()
    override fun latestLatLon(): STLatLng? = map().latestLatLon()
    override fun removeMarker(marker: STMarker?) = map().removeMarker(marker)
    override fun isLatLngInScreen(latLng: STLatLng?, callback: (result: Boolean) -> Unit) = map().isLatLngInScreen(latLng, callback)

    override fun setOnMapLoadedCallback(onMapLoaded: () -> Unit) = map().setOnMapLoadedCallback(onMapLoaded)
    override fun onLocationButtonLongClickedListener(): OnLongClickListener = map().onLocationButtonLongClickedListener()
    override fun onLocationButtonClickedListener(): OnClickListener = map().onLocationButtonClickedListener()

    override fun enableCompass(enable: Boolean) = map().enableCompass(enable)
    override fun enableTraffic(enable: Boolean) = map().enableTraffic(enable)
    override fun isTrafficEnabled(): Boolean = map().isTrafficEnabled()
    override fun setMaxAndMinZoomLevel(maxZoomLevel: Float, minZoomLevel: Float) = map().setMaxAndMinZoomLevel(maxZoomLevel, minZoomLevel)
    override fun enableMapScaleControl(enable: Boolean) = map().enableMapScaleControl(enable)
    override fun enableRotate(enable: Boolean) = map().enableRotate(enable)
    override fun setZoomLevel(zoomLevel: Float, animate: Boolean) = map().setZoomLevel(zoomLevel, animate)

    override fun setMapCenter(latLng: STLatLng?, animate: Boolean) = map().setMapCenter(latLng, animate)
    override fun setMapCenter(latLng: STLatLng?, zoomLevel: Float, animate: Boolean) = map().setMapCenter(latLng, zoomLevel, animate)
    override fun setMapCenter(padding: Map<String, Int>, animate: Boolean, vararg latLng: STLatLng?) = map().setMapCenter(padding, animate, *latLng)
    override fun setMapCenter(animate: Boolean, zoomLevel: Float, latLng: STLatLng?) = map().setMapCenter(animate, zoomLevel, latLng)
    override fun setMapCenter(padding: Map<String, Int>, animate: Boolean, swLatLng: STLatLng?, neLatLng: STLatLng?) = map().setMapCenter(padding, animate, swLatLng, neLatLng)

    override fun getCurrentMapRadius(): Double = map().getCurrentMapRadius()
    override fun getCurrentMapZoomLevel(): Float = map().getCurrentMapZoomLevel()
    override fun getCurrentMapOptions(): STMapOptions = map().getCurrentMapOptions()
    override fun getCurrentMapCenterLatLng(): STLatLng = map().getCurrentMapCenterLatLng()
    override fun getCurrentMapLatLngBounds(): STLatLngBounds = map().getCurrentMapLatLngBounds()
    override fun getCurrentMapStatus(callback: (centerLatLng: STLatLng, zoomLevel: Float, radius: Double, bounds: STLatLngBounds) -> Unit) = map().getCurrentMapStatus(callback)

    override fun convertLatLngToScreenCoordinate(latLng: STLatLng?, callback: (Point?) -> Unit) = map().convertLatLngToScreenCoordinate(latLng, callback)
    override fun convertScreenCoordinateToLatLng(point: Point?, callback: (STLatLng?) -> Unit) = map().convertScreenCoordinateToLatLng(point, callback)

    companion object {
        /**
         * 百度地图 4-21, 高德地图 3-20
         * 一切级别以百度为准, 高德对缩放级别 +1, 则高德逻辑范围为 (4-21), 对应百度真实范围 (4-21)
         */
        const val defaultBaiduMaxZoomLevel = 21f    // 高德地图对应 -1
        const val defaultBaiduMinZoomLevel = 4f     // 高德地图对应 -1
        const val defaultBaiduZoomLevel = 14f       // 高德地图对应 -1

        @JvmStatic
        val defaultLatLngTianAnMen = STLatLng(39.915526, 116.403847, STLatLngType.BD09) // 天安门
        val defaultLatLngDongFangMingZhu = STLatLng(31.245105, 121.506377, STLatLngType.BD09) // 东方明珠塔
    }
}