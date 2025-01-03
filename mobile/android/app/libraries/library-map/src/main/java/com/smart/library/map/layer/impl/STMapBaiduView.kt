package com.smart.library.map.layer.impl

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.baidu.mapapi.SDKInitializer
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import com.baidu.mapapi.model.LatLngBounds
import com.baidu.mapapi.utils.DistanceUtil
import com.smart.library.STInitializer
import com.smart.library.map.R
import com.smart.library.map.layer.STIMap
import com.smart.library.map.layer.STMapOptions
import com.smart.library.map.layer.STMapView
import com.smart.library.map.location.STLocation
import com.smart.library.map.location.STLocationManager
import com.smart.library.map.location.impl.STLocationBaiduSensor
import com.smart.library.map.model.*
import com.smart.library.util.STLogUtil
import com.smart.library.util.STPreferencesUtil
import com.smart.library.util.STSystemUtil
import com.smart.library.util.cache.STCacheManager
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import kotlin.math.ln
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

@Suppress("unused", "CanBeParameter", "CanBeParameter")
class STMapBaiduView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, private val initMapOptions: STMapOptions = STMapOptions()) : FrameLayout(context, attrs, defStyleAttr), STIMap, View.OnClickListener, View.OnLongClickListener {

    init {
        if (!isInEditMode) {
            initialize(STInitializer.application())
        }
        addView(createMapView(context, initMapOptions))
    }

    private val mapView: TextureMapView by lazy { getChildAt(0) as TextureMapView }
    private val map: BaiduMap by lazy { mapView().map }

    private fun map(): BaiduMap = map
    override fun mapView(): TextureMapView = mapView
    override fun latestLatLon(): STLatLng? = latestLatLon

    private var locationBaiduSensor: STLocationBaiduSensor? = null

    private var onLocationChanged: ((STLatLng?) -> Unit)? = null
    override fun setOnLocationChangedListener(onLocationChanged: (STLatLng?) -> Unit) {
        this.onLocationChanged = onLocationChanged
    }

    override fun onCreate(context: Context?, savedInstanceState: Bundle?) {
        mapView().onCreate(context, savedInstanceState)
        locationBaiduSensor = STLocationBaiduSensor(context, map()) { location: STLocation ->
            val stLatLng = STLatLng(location.latitude, location.longitude, STLatLngType.BD09)
            if (stLatLng.isValid()) {
                latestLatLon = stLatLng
                onLocationChanged?.invoke(latestLatLon)
            }
            STLogUtil.d("location", "onSensorCallback location=$location, latestLatLon=$latestLatLon")
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

    private var latestLatLon: STLatLng? = null
    override fun onLocationButtonClickedListener(): OnClickListener = this
    override fun onLocationButtonLongClickedListener(): OnLongClickListener = this
    override fun onLongClick(view: View?): Boolean {
        if (latestLatLon?.isValid() == true) {
            setMapCenter(latestLatLon, STMapView.defaultBaiduZoomLevel, true)
        }
        return true
    }

    override fun onClick(locationButtonView: View?) {
        STLogUtil.d("location", "onClick latestLatLon=$latestLatLon")
        if (latestLatLon?.isValid() == true) {
            setMapCenter(latestLatLon, true)
        }
    }

    override fun enableCompass(enable: Boolean) {
        map().setCompassEnable(enable)
    }

    override fun enableTraffic(enable: Boolean) {
        map().isTrafficEnabled = enable
    }

    override fun isTrafficEnabled(): Boolean = map().isTrafficEnabled

    override fun setZoomLevel(zoomLevel: Float, animate: Boolean) {
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

    /**
     * 使用官方的比较好, 虽然误差差不多, 但毕竟是官方的
     * 根据圆心经纬度和半径计算地图 zoom level
     * @see https://stackoverflow.com/questions/18383236/determine-a-reasonable-zoom-level-for-google-maps-given-location-accuracy
     */
    override fun calculateZoomLevel(radius: Double): Float {
        // 赤道长度
        val equatorLength = 40075004.0

        val distance: Double = radius * 2.0

        val screenSize = Math.min(STSystemUtil.screenWidth(), STSystemUtil.screenHeight())
        // The meters per pixel required to show the whole area the user might be located in
        val requiredMpp = distance / screenSize

        // Calculate the zoom level
        return (ln(equatorLength / (256.0 * requiredMpp)) / ln(2.0) + 1 - 2.7617).toFloat() // 2.7617 为高德地图偏移量
    }

    override fun setMapCenter(latLng: STLatLng?, animate: Boolean) {
        val bd09LatLng: STLatLng? = latLng?.convertTo(STLatLngType.BD09)
        if (bd09LatLng?.isValid() == true) {
            val mapStatus = MapStatusUpdateFactory.newLatLng(LatLng(bd09LatLng.latitude, bd09LatLng.longitude))
            if (animate) {
                map().animateMapStatus(mapStatus)
            } else {
                map().setMapStatus(mapStatus)
            }
        }
    }

    override fun setMapCenter(latLng: STLatLng?, zoomLevel: Float, animate: Boolean) {
        val bd09LatLng: STLatLng? = latLng?.convertTo(STLatLngType.BD09)
        if (bd09LatLng?.isValid() == true) {
            val mapStatus = MapStatusUpdateFactory.newLatLngZoom(LatLng(bd09LatLng.latitude, bd09LatLng.longitude), zoomLevel)
            if (animate) {
                map().animateMapStatus(mapStatus)
            } else {
                map().setMapStatus(mapStatus)
            }
        }
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

    override fun getCurrentMapOptions(): STMapOptions {
        return STMapOptions(
            map().mapType,
            map().isTrafficEnabled,
            map().isBuildingsEnabled,
            initMapOptions.showMapPoi,
            getCurrentMapCenterLatLng(),
            getCurrentMapZoomLevel()
        )
    }

    override fun showMapPoi(showMapPoi: Boolean) {
        initMapOptions.showMapPoi = showMapPoi
        map().showMapPoi(initMapOptions.showMapPoi)
    }

    override fun isShowMapPoi(): Boolean = initMapOptions.showMapPoi
    override fun showBuildings(isBuildingsEnabled: Boolean) {
        initMapOptions.isBuildingsEnabled = isBuildingsEnabled
        map().isBuildingsEnabled = initMapOptions.isBuildingsEnabled
    }

    override fun isBuildingsEnabled(): Boolean = initMapOptions.isBuildingsEnabled
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
        private fun createMapView(context: Context?, initMapOptions: STMapOptions): TextureMapView {
            val options = BaiduMapOptions()
            options.mapType(initMapOptions.mapType)
            val mapStatusBuilder = MapStatus.Builder()

            val bdLatLng: STLatLng? = initMapOptions.initCenterLatLng.convertTo(STLatLngType.BD09)
            if (bdLatLng?.isValid() == true) {
                mapStatusBuilder.target(LatLng(bdLatLng.latitude, bdLatLng.longitude))
            }
            mapStatusBuilder.zoom(initMapOptions.initZoomLevel)
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
            initMapView(mapView, initMapOptions)
            return mapView
        }

        @JvmStatic
        private fun initMapView(mapView: TextureMapView, initMapOptions: STMapOptions = STMapOptions()): TextureMapView = mapView.apply {
            logoPosition = LogoPosition.logoPostionleftBottom
            showZoomControls(false)             // 缩放按钮
            showScaleControl(false)             // 比例尺

            setMapTheme(this)

            map.setMyLocationConfiguration(MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, BitmapDescriptorFactory.fromView(LayoutInflater.from(context).inflate(R.layout.st_location_sensor_layout, null, false)), Color.parseColor("#1A0099FF"), Color.TRANSPARENT))
            // map.setMyLocationConfiguration(MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, BitmapDescriptorFactory.fromResource(R.drawable.st_map_location_with_sensor), Color.parseColor("#1A0099FF"), Color.TRANSPARENT))

            map.apply {
                changeLocationLayerOrder(true)     // 定位图层位于 marker 之下
                mapType = initMapOptions.mapType // 普通地图（包含3D地图）
                isTrafficEnabled = initMapOptions.isTrafficEnabled            // 开启交通图
                isBaiduHeatMapEnabled = false      // 百度城市热力图
                setViewPadding(0, 0, 0, 0)         // 设置地图操作区距屏幕的距离
                setMaxAndMinZoomLevel(STMapView.defaultBaiduMaxZoomLevel, STMapView.defaultBaiduMinZoomLevel)     // 限制缩放等级
                showMapPoi(initMapOptions.showMapPoi)                   // 隐藏底图标注（控制地图POI显示）
                isBuildingsEnabled = initMapOptions.isBuildingsEnabled

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
                parentPath = baiduMapStyleDir?.absolutePath

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
