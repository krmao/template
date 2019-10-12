package com.smart.library.map.layer.impl

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import com.baidu.mapapi.SDKInitializer
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import com.baidu.mapapi.model.LatLngBounds
import com.baidu.mapapi.utils.DistanceUtil
import com.smart.library.base.STBaseApplication
import com.smart.library.map.layer.STIMap
import com.smart.library.map.layer.STMapView
import com.smart.library.map.location.STLocationManager
import com.smart.library.map.location.impl.STLocationBaiduSensor
import com.smart.library.map.model.*
import com.smart.library.util.STLogUtil
import com.smart.library.util.STPreferencesUtil
import com.smart.library.util.cache.STCacheManager
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

internal class STMapBaiduView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, initLatLon: STLatLng = STMapView.defaultLatLngTianAnMen, initZoomLevel: Float = STMapView.defaultZoomLevel) : FrameLayout(context, attrs, defStyleAttr), STIMap, View.OnClickListener {

    init {
        if (!isInEditMode) {
            initialize(STBaseApplication.INSTANCE)
        }
        addView(createMapView(context, initLatLon, initZoomLevel))
    }

    private val mapView: TextureMapView by lazy { getChildAt(0) as TextureMapView }
    private val map: BaiduMap by lazy { mapView().map }

    private fun map(): BaiduMap = map
    override fun mapView(): TextureMapView = mapView
    override fun latestLatLon(): STLatLng? = latestLatLon

    private var locationBaiduSensor: STLocationBaiduSensor? = null

    private var onLocationChanged: ((STLatLng?) -> Unit)? = null
    fun setOnLocationChangedListener(onLocationChanged: (STLatLng?) -> Unit) {
        this.onLocationChanged = onLocationChanged
    }

    private var latestLatLon: STLatLng? = null
    override fun onCreate(context: Context?, savedInstanceState: Bundle?) {
        mapView().onCreate(context, savedInstanceState)
        locationBaiduSensor = STLocationBaiduSensor(context, map()) {
            val stLatLng = STLatLng(it.latitude, it.longitude, STLatLngType.BD09)
            if (stLatLng.isValid()) {
                latestLatLon = stLatLng
                onLocationChanged?.invoke(latestLatLon)
            }
        }
    }

    override fun setOnMapLoadedCallback(onMapLoaded: () -> Unit) {
        map().setOnMapLoadedCallback {
            onMapLoaded()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) = mapView().onSaveInstanceState(outState)
    override fun onResume() {
        mapView().onResume()
        STLogUtil.d("location", "ensurePermissions ...")
        STLocationManager.ensurePermissionsWithoutHandling(context as? Activity) {
            STLogUtil.d("location", "ensurePermissions:$it")
            if (it) {
                locationBaiduSensor?.startLocation()
            }
        }
    }

    override fun onPause() {
        mapView().onPause()
        locationBaiduSensor?.stopLocation()
    }

    override fun onDestroy() = mapView().onDestroy()

    override fun mapType(): STMapType = STMapType.BAIDU

    override fun onLocationButtonClickedListener(): OnClickListener = this

    override fun onClick(locationButtonView: View?) {
        if (latestLatLon?.isValid() == true) {
            setMapCenter(animate = true, latLng = *arrayOf(latestLatLon))
        }
    }

    override fun enableCompass(enable: Boolean) {
        map().setCompassEnable(enable)
    }

    override fun setZoomLevel(zoomLevel: Float) {
        map().animateMapStatus(MapStatusUpdateFactory.zoomTo(zoomLevel))
    }

    override fun setMaxAndMinZoomLevel(maxZoomLevel: Float, minZoomLevel: Float) {
        map().setMaxAndMinZoomLevel(maxZoomLevel, minZoomLevel)
    }

    override fun enableMapScaleControl(enable: Boolean) {
        mapView().showZoomControls(enable)
    }

    override fun enableRotate(enable: Boolean) {
        map().uiSettings.isRotateGesturesEnabled = enable
    }

    override fun setMapCenter(padding: Map<String, Int>, animate: Boolean, vararg latLng: STLatLng?) {
        val latLngBoundsBuilder = LatLngBounds.Builder()
        latLng.forEach { item ->
            item?.let {
                latLngBoundsBuilder.include(LatLng(it.latitude, it.longitude))
            }
        }

        val mapStatus = MapStatusUpdateFactory.newLatLngBounds(latLngBoundsBuilder.build(), width, height)
        if (animate) {
            map().animateMapStatus(mapStatus)
        } else {
            map().setMapStatus(mapStatus)
        }
    }

    private fun convertBaiduLatLngToSTLatLng(latLng: LatLng): STLatLng = STLatLng(latLng.latitude, latLng.longitude, STLatLngType.BD09)
    private fun convertBaiduBoundsToSTLatLngBounds(latLngBounds: LatLngBounds): STLatLngBounds {
        return STLatLngBounds(convertBaiduLatLngToSTLatLng(latLngBounds.southwest), convertBaiduLatLngToSTLatLng(latLngBounds.northeast))
    }

    override fun getCurrentMapRadius(): Double = getDistanceByBaiduLatLng(map().mapStatus.bound.northeast, map().mapStatus.target)

    override fun getCurrentMapZoomLevel(): Float = map().mapStatus.zoom
    override fun getCurrentMapCenterLatLng(): STLatLng = convertBaiduLatLngToSTLatLng(map().mapStatus.target)
    override fun getCurrentMapLatLngBounds(): STLatLngBounds = convertBaiduBoundsToSTLatLngBounds(map().mapStatus.bound)

    override fun setMapCenter(animate: Boolean, zoomLevel: Float, latLng: STLatLng?) {
        latLng ?: return
        val mapStatus = MapStatusUpdateFactory.newLatLngZoom(LatLng(latLng.latitude, latLng.longitude), zoomLevel)
        if (animate) {
            map().animateMapStatus(mapStatus)
        } else {
            map().setMapStatus(mapStatus)
        }
    }

    override fun setMapCenter(padding: Map<String, Int>, animate: Boolean, swLatLng: STLatLng?, neLatLng: STLatLng?) {
        swLatLng ?: return
        neLatLng ?: return

        val latLngBoundsBuilder = LatLngBounds.Builder()
        latLngBoundsBuilder.include(LatLng(swLatLng.latitude, swLatLng.longitude))
        latLngBoundsBuilder.include(LatLng(neLatLng.latitude, neLatLng.longitude))

        val mapStatus = MapStatusUpdateFactory.newLatLngBounds(latLngBoundsBuilder.build(), width, height)
        if (animate) {
            map().animateMapStatus(mapStatus)
        } else {
            map().setMapStatus(mapStatus)
        }
    }

    override fun clear() {
        map().clear()
    }

    override fun removeMarker(marker: STMarker?) {
        marker?.remove()
    }

    override fun isLatLngInScreen(latLng: STLatLng?, callback: (result: Boolean) -> Unit) {
        callback(latLng != null && map().mapStatus?.bound?.contains(LatLng(latLng.latitude, latLng.longitude)) == true)
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
            val baiduLatLng = map().projection?.fromScreenLocation(Point(point.x.toDouble().roundToInt(), point.y.toDouble().roundToInt()))
            if (baiduLatLng != null) {
                latLng = STLatLng(baiduLatLng.latitude, baiduLatLng.longitude, STLatLngType.BD09)
            }
        }
        callback(latLng)
    }

    override fun switchTheme() {
        setMapTheme(mapView(), ++mapThemeIndex)
    }

    companion object {

        @JvmStatic
        fun wrapBaiduZoomLevel(baiduZoomLevel: Float): Float = min(max(baiduZoomLevel, 4f), 21f)

        @JvmStatic
        fun getDistanceByBaiduLatLng(startLatLng: LatLng?, endLatLng: LatLng?): Double {
            return DistanceUtil.getDistance(startLatLng, endLatLng)
        }

        private val mapThemeList: List<Array<String>> = listOf(
                arrayOf("茶田", "map_config_chatian.json"),
                arrayOf("朱砂痣", "map_config_zhushazhi.json"),
                arrayOf("绿野仙踪", "map_config_lvyexianzong.json"),
                arrayOf("青花瓷", "map_config_qinghuaci.json"),
                arrayOf("一蓑烟雨", "map_config_yisuoyanyu.json"),
                arrayOf("眼眸", "map_config_yanmou.json"),
                arrayOf("Candy", "map_config_candy.json"),
                arrayOf("OKR", "map_config_okr.json"),
                arrayOf("赛博朋克", "map_config_saibopengke.json"),
                arrayOf("物流", "map_config_wuliu.json"),
                arrayOf("出行", "map_config_chuxing.json"),
                arrayOf("中秋", "map_config_zhongqiu.json")
        )

        private var mapThemeIndex: Int = STPreferencesUtil.getInt("map_baidu_theme", 0)
            set(value) {
                if (field < 0 || value >= mapThemeList.size) {
                    field = 0
                } else {
                    field = value
                }
                STPreferencesUtil.putInt("map_baidu_theme", field)
            }
            get() {
                if (field < 0 || field >= mapThemeList.size) {
                    field = 0
                    STPreferencesUtil.putInt("map_baidu_theme", field)
                }
                return field
            }

        @JvmStatic
        private fun setMapTheme(mapView: TextureMapView, themeIndex: Int = mapThemeIndex) {
            // 获取json文件路径
            val customStyleFilePath = getCustomStyleFilePath(mapView.context, mapThemeList[themeIndex][1])
            // 设置个性化地图样式文件的路径和加载方式
            mapView.setMapCustomStylePath(customStyleFilePath)
            // 动态设置个性化地图样式是否生效
            mapView.setMapCustomStyleEnable(true)
        }

        @JvmStatic
        private fun createMapView(context: Context?, initLatLon: STLatLng, initZoomLevel: Float): TextureMapView {
            val options = BaiduMapOptions()
            options.mapType(BaiduMap.MAP_TYPE_NORMAL)
            val mapStatusBuilder = MapStatus.Builder();

            val bdLatLng = initLatLon.convertTo(STLatLngType.BD09)
            if (bdLatLng?.isValid() == true) {
                mapStatusBuilder.target(LatLng(bdLatLng.latitude, bdLatLng.longitude))
            }
            mapStatusBuilder.zoom(initZoomLevel)
            val mapStatus: MapStatus = mapStatusBuilder.build()

            options.mapStatus(mapStatus)
            options.compassEnabled(false)
            options.logoPosition(LogoPosition.logoPostionleftBottom)
            options.overlookingGesturesEnabled(false) // 地图俯视（3D）
            options.rotateGesturesEnabled(false) // 地图旋转
            options.scaleControlEnabled(false) // 比例尺
            options.scrollGesturesEnabled(true) // 地图平移手势
            options.zoomGesturesEnabled(true) // 地图缩放控制手势
            options.zoomControlsEnabled(false) // 地图缩放控制按钮

            val mapView = TextureMapView(context, options)
            initMapView(mapView)
            return mapView
        }

        @JvmStatic
        private fun initMapView(mapView: TextureMapView): TextureMapView = mapView.apply {
            logoPosition = LogoPosition.logoPostionleftBottom
            showZoomControls(false)             // 缩放按钮
            showScaleControl(false)             // 比例尺

            setMapTheme(this)

            map.apply {
                changeLocationLayerOrder(true)     // 定位图层位于 marker 之下
                mapType = BaiduMap.MAP_TYPE_NORMAL // 普通地图（包含3D地图）
                isTrafficEnabled = false            // 开启交通图
                isBaiduHeatMapEnabled = false      // 百度城市热力图
                setViewPadding(0, 0, 0, 0)         // 设置地图操作区距屏幕的距离
                setMaxAndMinZoomLevel(21f, 3f)     // 限制缩放等级
                showMapPoi(true)                   // 隐藏底图标注（控制地图POI显示）

                uiSettings.apply {
                    isScrollGesturesEnabled = true          // 地图平移
                    isZoomGesturesEnabled = true            // 地图缩放
                    isOverlookingGesturesEnabled = false    // 地图俯视（3D）
                    isRotateGesturesEnabled = false         // 地图旋转
                    // setAllGesturesEnabled(true)          // 禁止所有手势
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
                inputStream = context.assets.open("map_style/baidu/$customStyleFileName")
                val buffer = ByteArray(inputStream.available())
                inputStream.read(buffer)

                val mapStyleDir = STCacheManager.getChildDir(STCacheManager.getFilesDir(), "map_style")
                val baiduMapStyleDir = STCacheManager.getChildDir(mapStyleDir, "baidu")
                parentPath = baiduMapStyleDir.absolutePath

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
                SDKInitializer.initialize(context)
                isInitialized = true
            }
        }
    }
}
