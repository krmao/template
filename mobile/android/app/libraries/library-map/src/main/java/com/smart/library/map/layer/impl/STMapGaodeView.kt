package com.smart.library.map.layer.impl

import android.content.Context
import android.graphics.Point
import android.util.AttributeSet
import android.util.Log
import android.widget.FrameLayout
import com.amap.api.maps2d.*
import com.amap.api.maps2d.model.CameraPosition
import com.amap.api.maps2d.model.LatLng
import com.amap.api.maps2d.model.LatLngBounds
import com.smart.library.base.STBaseApplication
import com.smart.library.map.layer.STIMap
import com.smart.library.map.layer.STMapView
import com.smart.library.map.model.STLatLng
import com.smart.library.map.model.STLatLngBounds
import com.smart.library.map.model.STLatLngType
import com.smart.library.map.model.STMarker
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import kotlin.math.roundToInt

internal class STMapGaodeView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, initLatLon: STLatLng = STMapView.defaultLatLngTianAnMen, initZoomLevel: Float = STMapView.defaultZoomLevel) : FrameLayout(context, attrs, defStyleAttr), STIMap {

    init {
        if (!isInEditMode) {
            initialize(STBaseApplication.INSTANCE)
        }
        addView(createMapView(context, initLatLon, initZoomLevel))
    }

    private val mapView: MapView by lazy { getChildAt(0) as MapView }
    private val map: AMap by lazy { mapView().map }

    private fun map(): AMap = map
    override fun mapView(): MapView = mapView

    override fun onResume() = mapView().onResume()
    override fun onPause() = mapView().onPause()
    override fun onDestroy() = mapView().onDestroy()

    override fun enableCompass(enable: Boolean) {
        map().uiSettings.isCompassEnabled = enable
    }

    override fun setZoomLevel(zoomLevel: Float) {
        map().animateCamera(CameraUpdateFactory.zoomTo(zoomLevel))
    }

    override fun setMaxAndMinZoomLevel(maxZoomLevel: Float, minZoomLevel: Float) {
    }

    override fun enableMapScaleControl(enable: Boolean) {
        map().uiSettings.isScaleControlsEnabled = enable
    }

    override fun enableRotate(enable: Boolean) {
    }

    override fun setMapCenter(padding: Map<String, Int>, animate: Boolean, vararg latLng: STLatLng?) {
        val latLngBoundsBuilder = LatLngBounds.Builder()
        latLng.forEach { item ->
            item?.let {
                latLngBoundsBuilder.include(LatLng(it.latitude, it.longitude))
            }
        }

        val mapStatus = CameraUpdateFactory.newLatLngBounds(latLngBoundsBuilder.build(), width, height, padding["left"] ?: 0)
        if (animate) {
            map().animateCamera(mapStatus)
        } else {
            map().moveCamera(mapStatus)
        }
    }

    private fun convertGaodeLatLngToSTLatLng(latLng: LatLng): STLatLng = STLatLng(latLng.latitude, latLng.longitude, STLatLngType.BD09)
    private fun convertGaodeBoundsToSTLatLngBounds(latLngBounds: LatLngBounds): STLatLngBounds {
        return STLatLngBounds(convertGaodeLatLngToSTLatLng(latLngBounds.southwest), convertGaodeLatLngToSTLatLng(latLngBounds.northeast))
    }

    override fun getCurrentMapRadius(): Double = getDistanceByGaodeLatLng(map().projection.visibleRegion.latLngBounds.northeast, map().cameraPosition.target)

    override fun getCurrentMapZoomLevel(): Float = map().cameraPosition.zoom
    override fun getCurrentMapCenterLatLng(): STLatLng = convertGaodeLatLngToSTLatLng(map().cameraPosition.target)
    override fun getCurrentMapLatLngBounds(): STLatLngBounds = convertGaodeBoundsToSTLatLngBounds(map().projection.visibleRegion.latLngBounds)

    override fun setMapCenter(animate: Boolean, zoomLevel: Float, latLng: STLatLng?) {
        latLng ?: return
        val mapStatus = CameraUpdateFactory.newLatLngZoom(LatLng(latLng.latitude, latLng.longitude), zoomLevel)
        if (animate) {
            map().animateCamera(mapStatus)
        } else {
            map().moveCamera(mapStatus)
        }
    }

    override fun setMapCenter(padding: Map<String, Int>, animate: Boolean, swLatLng: STLatLng?, neLatLng: STLatLng?) {
        swLatLng ?: return
        neLatLng ?: return

        val latLngBoundsBuilder = LatLngBounds.Builder()
        latLngBoundsBuilder.include(LatLng(swLatLng.latitude, swLatLng.longitude))
        latLngBoundsBuilder.include(LatLng(neLatLng.latitude, neLatLng.longitude))

        val mapStatus = CameraUpdateFactory.newLatLngBounds(latLngBoundsBuilder.build(), width, height, padding["left"] ?: 0)
        if (animate) {
            map().animateCamera(mapStatus)
        } else {
            map().moveCamera(mapStatus)
        }
    }

    override fun clear() {
        map().clear()
    }

    override fun removeMarker(marker: STMarker?) {
        marker?.remove()
    }

    override fun isLatLngInScreen(latLng: STLatLng?, callback: (result: Boolean) -> Unit) {
        callback(latLng != null && map().projection?.visibleRegion?.latLngBounds?.contains(LatLng(latLng.latitude, latLng.longitude)) == true)
    }

    override fun getCurrentMapStatus(callback: (centerLatLng: STLatLng, zoomLevel: Float, radius: Double, bounds: STLatLngBounds) -> Unit) {
        callback(getCurrentMapCenterLatLng(), getCurrentMapZoomLevel(), getCurrentMapRadius(), getCurrentMapLatLngBounds())
    }

    override fun convertLatLngToScreenCoordinate(latLng: STLatLng?, callback: (Point?) -> Unit) {
        var point: Point? = null
        if (latLng != null) {
            point = map().projection?.toScreenLocation(LatLng(latLng.latitude, latLng.longitude))
        }
        callback(point)
    }

    override fun convertScreenCoordinateToLatLng(point: Point?, callback: (STLatLng?) -> Unit) {
        var latLng: STLatLng? = null
        if (point != null) {
            val gaodeLatLng = map().projection?.fromScreenLocation(Point(point.x.toDouble().roundToInt(), point.y.toDouble().roundToInt()))
            if (gaodeLatLng != null) {
                latLng = STLatLng(gaodeLatLng.latitude, gaodeLatLng.longitude, STLatLngType.BD09)
            }
        }
        callback(latLng)
    }

    companion object {

        @JvmStatic
        fun getDistanceByGaodeLatLng(startLatLng: LatLng?, endLatLng: LatLng?): Double {
            return AMapUtils.calculateLineDistance(startLatLng, endLatLng).toDouble()
        }

        private val configMap: HashMap<String, String> = hashMapOf(
                "茶田" to "map_config_chatian.json",
                "朱砂痣" to "map_config_zhushazhi.json",
                "绿野仙踪" to "map_config_lvyexianzong.json",
                "青花瓷" to "map_config_qinghuaci.json",
                "一蓑烟雨" to "map_config_yisuoyanyu.json",
                "眼眸" to "map_config_yanmou.json",
                "Candy" to "map_config_candy.json",
                "OKR" to "map_config_okr.json",
                "赛博朋克" to "map_config_saibopengke.json",
                "物流" to "map_config_wuliu.json",
                "出行" to "map_config_chuxing.json",
                "中秋" to "map_config_zhongqiu.json"
        )

        @JvmStatic
        private fun createMapView(context: Context?, initLatLon: STLatLng, initZoomLevel: Float): MapView {
            val options = AMapOptions()
            options.mapType(AMap.MAP_TYPE_NORMAL)

            options.compassEnabled(false)
            options.scaleControlsEnabled(false) // 比例尺
            options.scrollGesturesEnabled(true) // 地图平移手势
            options.zoomGesturesEnabled(true) // 地图缩放控制手势
            options.zoomControlsEnabled(false) // 地图缩放控制按钮
            options.logoPosition(AMapOptions.LOGO_POSITION_BOTTOM_LEFT)
            options.zOrderOnTop(true)

            val gdLatLng: STLatLng? = initLatLon.convertTo(STLatLngType.GCJ02)
            if (gdLatLng?.isValid() == true) {
                options.camera(CameraPosition.fromLatLngZoom(LatLng(gdLatLng.latitude, gdLatLng.longitude), initZoomLevel))
            }

            val mapView = MapView(context, options)

            initMapView(mapView)
            return mapView
        }

        @JvmStatic
        private fun initMapView(mapView: MapView): MapView = mapView.apply {
            // 获取json文件路径
            // val customStyleFilePath = getCustomStyleFilePath(mapView.context, configMap["青花瓷"])
            // 设置个性化地图样式文件的路径和加载方式
            // setMapCustomStylePath(customStyleFilePath)
            // 动态设置个性化地图样式是否生效
            // setMapCustomStyleEnable(true)

            map.apply {

                mapType = AMap.MAP_TYPE_NORMAL // 普通地图（包含3D地图）
                isTrafficEnabled = false            // 开启交通图

                uiSettings.apply {
                    isScaleControlsEnabled = true
                    isZoomControlsEnabled = true
                    isCompassEnabled = true
                    isMyLocationButtonEnabled = true
                    isScrollGesturesEnabled = true
                    isZoomGesturesEnabled = true
                    setAllGesturesEnabled(false)
                    logoPosition = AMapOptions.LOGO_POSITION_BOTTOM_LEFT
                    zoomPosition = AMapOptions.ZOOM_POSITION_RIGHT_BUTTOM
                    setZoomInByScreenCenter(false) // 设置双击地图放大在地图中心位置放大，false则是在点击位置放大
                }
            }
        }

        /**
         * 读取json路径
         */
        @Suppress("SameParameterValue")
        private fun getCustomStyleFilePath(context: Context, customStyleFileName: String?): String? {
            var outputStream: FileOutputStream? = null
            var inputStream: InputStream? = null
            var parentPath: String? = null
            try {
                inputStream = context.assets.open("mapConfig/$customStyleFileName")
                val buffer = ByteArray(inputStream.available())
                inputStream.read(buffer)
                parentPath = context.filesDir.absolutePath
                val customStyleFile = File("$parentPath/$customStyleFileName")
                if (customStyleFile.exists()) {
                    customStyleFile.delete()
                }
                customStyleFile.createNewFile()

                outputStream = FileOutputStream(customStyleFile)
                outputStream.write(buffer)
            } catch (e: IOException) {
                Log.e("CustomMapDemo", "Copy custom style file failed", e)
            } finally {
                try {
                    inputStream?.close()
                    outputStream?.close()
                } catch (e: IOException) {
                    Log.e("CustomMapDemo", "Close stream failed", e)
                    return null
                }

            }
            return "$parentPath/$customStyleFileName"
        }

        @Volatile
        private var isInitialized: Boolean = false

        @JvmStatic
        @Synchronized
        private fun initialize(context: Context?) {
            if (context != null && !isInitialized) {
                isInitialized = true
            }
        }
    }
}
