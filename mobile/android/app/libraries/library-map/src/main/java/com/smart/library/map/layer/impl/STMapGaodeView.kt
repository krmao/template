package com.smart.library.map.layer.impl

import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import com.amap.api.maps.*
import com.amap.api.maps.model.*
import com.smart.library.base.STBaseApplication
import com.smart.library.map.layer.STIMap
import com.smart.library.map.layer.STMapView
import com.smart.library.map.model.*
import com.smart.library.util.STPreferencesUtil
import com.smart.library.util.cache.STCacheManager
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt


/**
 * 注意: zoomLevel
 *
 * 百度地图 4-21, 高德地图 3-20
 * 一切级别以百度为准, 高德对缩放级别 +1, 则高德逻辑范围为 (4-21), 对应百度真实范围 (4-21)
 *
 * 通过 wrapGaodeZoomLevelFromBaidu/wrapGaodeZoomLevelToBaidu 使得输入输出皆为 百度 zoomLevel, 方便客户端统一缩放级别
 */
internal class STMapGaodeView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, initLatLon: STLatLng = STMapView.defaultLatLngTianAnMen, initZoomLevel: Float = STMapView.defaultBaiduZoomLevel) : FrameLayout(context, attrs, defStyleAttr), STIMap, View.OnClickListener, View.OnLongClickListener {

    init {
        if (!isInEditMode) {
            initialize(STBaseApplication.INSTANCE)
        }
        addView(createMapView(context, initLatLon, initZoomLevel))
    }

    private val mapView: TextureMapView by lazy { getChildAt(0) as TextureMapView }
    private val map: AMap by lazy { mapView().map }
    override fun latestLatLon(): STLatLng? = latestLatLon

    private fun map(): AMap = map
    override fun mapView(): TextureMapView = mapView

    private var onLocationChanged: ((STLatLng?) -> Unit)? = null

    /**
     * 5.0.0 版本之后, 地图自己实现了定位, 通过该方法监听
     */
    @Suppress("unused")
    fun setOnLocationChangedListener(onLocationChanged: (STLatLng?) -> Unit) {
        this.onLocationChanged = onLocationChanged
    }

    override fun onCreate(context: Context?, savedInstanceState: Bundle?) {
        mapView().onCreate(savedInstanceState)
        map().setOnMyLocationChangeListener {
            // https://lbs.amap.com/faq/android/android-location/24/
            // 高德地图 Android 定位 SDK 支持返回高德坐标系（GCJ-02坐标系），可以完美显示在高德地图、高德地图SDK生成的图面上，并支持将GPS/Mapbar/Baidu坐标系转换到高德坐标系。

            val stLatLng = STLatLng(it.latitude, it.longitude, STLatLngType.GCJ02)
            if (stLatLng.isValid()) {
                latestLatLon = stLatLng
                onLocationChanged?.invoke(latestLatLon)
            }
        }

        // 5.0.0 版本之前, 自己实现定位 https://lbs.amap.com/api/android-sdk/guide/create-map/mylocation/
        // map().setLocationSource(STLocationGaodeClient(false))
    }

    override fun setOnMapLoadedCallback(onMapLoaded: () -> Unit) {
        map().setOnMapLoadedListener {
            onMapLoaded()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) = mapView().onSaveInstanceState(outState)

    override fun onResume() = mapView().onResume()
    override fun onPause() = mapView().onPause()
    override fun onDestroy() = mapView().onDestroy()

    override fun mapType(): STMapType = STMapType.GAODE

    private var latestLatLon: STLatLng? = null
    override fun onLocationButtonClickedListener(): OnClickListener = this
    override fun onLocationButtonLongClickedListener(): OnLongClickListener = this
    override fun onLongClick(view: View?): Boolean {
        if (latestLatLon?.isValid() == true) {
            setMapCenter(latestLatLon, wrapGaodeZoomLevelFromBaidu(STMapView.defaultBaiduZoomLevel), true)
        }
        return true
    }

    override fun onClick(locationButtonView: View?) {
        if (latestLatLon?.isValid() == true) {
            setMapCenter(latestLatLon, true)
        }
    }

    override fun enableCompass(enable: Boolean) {
        map().uiSettings.isCompassEnabled = enable
    }

    override fun setZoomLevel(zoomLevel: Float, animate: Boolean) {
        val mapStatus = CameraUpdateFactory.zoomTo(wrapGaodeZoomLevelFromBaidu(zoomLevel))
        if (animate) {
            map().animateCamera(mapStatus)
        } else {
            map().moveCamera(mapStatus)
        }
    }

    override fun setMaxAndMinZoomLevel(maxZoomLevel: Float, minZoomLevel: Float) {
    }

    override fun enableMapScaleControl(enable: Boolean) {
        map().uiSettings.isScaleControlsEnabled = enable
    }

    override fun enableRotate(enable: Boolean) {
    }

    override fun setMapCenter(latLng: STLatLng?, animate: Boolean) {
        val gcj02LatLng: STLatLng? = latLng?.convertTo(STLatLngType.GCJ02)
        if (gcj02LatLng?.isValid() == true) {

            val mapStatus = CameraUpdateFactory.newLatLng(LatLng(gcj02LatLng.latitude, gcj02LatLng.longitude))
            if (animate) {
                map().animateCamera(mapStatus)
            } else {
                map().moveCamera(mapStatus)
            }
        }
    }

    override fun setMapCenter(latLng: STLatLng?, zoomLevel: Float, animate: Boolean) {
        val gcj02LatLng: STLatLng? = latLng?.convertTo(STLatLngType.GCJ02)
        if (gcj02LatLng?.isValid() == true) {

            val mapStatus = CameraUpdateFactory.newLatLngZoom(LatLng(gcj02LatLng.latitude, gcj02LatLng.longitude), zoomLevel)
            if (animate) {
                map().animateCamera(mapStatus)
            } else {
                map().moveCamera(mapStatus)
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

        val mapStatus = CameraUpdateFactory.newLatLngBounds(latLngBoundsBuilder.build(), width, height, padding["left"] ?: 0)
        if (animate) {
            map().animateCamera(mapStatus)
        } else {
            map().moveCamera(mapStatus)
        }
    }

    override fun setMapCenter(animate: Boolean, zoomLevel: Float, latLng: STLatLng?) {
        latLng ?: return
        val mapStatus = CameraUpdateFactory.newLatLngZoom(LatLng(latLng.latitude, latLng.longitude), wrapGaodeZoomLevelFromBaidu(zoomLevel))
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

    private fun convertGaodeLatLngToSTLatLng(latLng: LatLng): STLatLng = STLatLng(latLng.latitude, latLng.longitude, STLatLngType.BD09)
    private fun convertGaodeBoundsToSTLatLngBounds(latLngBounds: LatLngBounds): STLatLngBounds {
        return STLatLngBounds(convertGaodeLatLngToSTLatLng(latLngBounds.southwest), convertGaodeLatLngToSTLatLng(latLngBounds.northeast))
    }

    override fun getCurrentMapRadius(): Double = getDistanceByGaodeLatLng(map().projection.visibleRegion.latLngBounds.northeast, map().cameraPosition.target)

    override fun getCurrentMapZoomLevel(): Float = wrapGaodeZoomLevelToBaidu(map().cameraPosition.zoom)
    override fun getCurrentMapCenterLatLng(): STLatLng = convertGaodeLatLngToSTLatLng(map().cameraPosition.target)
    override fun getCurrentMapLatLngBounds(): STLatLngBounds = convertGaodeBoundsToSTLatLngBounds(map().projection.visibleRegion.latLngBounds)

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

    override fun switchTheme() {
        setMapTheme(mapView(), ++mapThemeIndex)
    }


    companion object {

        @JvmStatic
        fun wrapGaodeZoomLevelFromBaidu(baiduZoomLevel: Float): Float = min(max(baiduZoomLevel - 1f, 3f), 20f)

        @JvmStatic
        fun wrapGaodeZoomLevelToBaidu(gaodeZoomLevel: Float): Float = min(max(gaodeZoomLevel + 1f, 3f), 20f)

        @JvmStatic
        fun getDistanceByGaodeLatLng(startLatLng: LatLng?, endLatLng: LatLng?): Double {
            return AMapUtils.calculateLineDistance(startLatLng, endLatLng).toDouble()
        }

        private val mapThemeList: List<Array<String>> = listOf(
                arrayOf("远山黛", "yuanshandai"),
                arrayOf("极夜蓝", "jiyelan"),
                arrayOf("草色青", "caoseqing"),
                arrayOf("涂鸦", "tuya"),
                arrayOf("酱籽", "jiangzi")
        )

        private var mapThemeIndex: Int = STPreferencesUtil.getInt("map_gaode_theme", 0)
            set(value) {
                if (field < 0 || value >= mapThemeList.size) {
                    field = 0
                } else {
                    field = value
                }
                STPreferencesUtil.putInt("map_gaode_theme", field)
            }
            get() {
                if (field < 0 || field >= mapThemeList.size) {
                    field = 0
                    STPreferencesUtil.putInt("map_gaode_theme", field)
                }
                return field
            }

        @JvmStatic
        private fun setMapTheme(mapView: TextureMapView, themeIndex: Int = mapThemeIndex) {
            val customMapStyleOptions: CustomMapStyleOptions = CustomMapStyleOptions()
                    .setEnable(true)
                    .setStyleDataPath(getCustomStyleFilePath(mapView.context, "${mapThemeList[themeIndex][1]}_style.data"))
                    .setStyleExtraPath(getCustomStyleFilePath(mapView.context, "${mapThemeList[themeIndex][1]}_style_extra.data"))

            mapView.map.setCustomMapStyle(customMapStyleOptions)
        }

        @JvmStatic
        private fun createMapView(context: Context?, initLatLon: STLatLng, initZoomLevel: Float): TextureMapView {
            val options = AMapOptions()

            options.logoPosition(AMapOptions.LOGO_POSITION_BOTTOM_LEFT)
            options.mapType(AMap.MAP_TYPE_NORMAL)
            options.rotateGesturesEnabled(false)    // 倾斜
            options.scrollGesturesEnabled(true)     // 地图平移手势
            options.scaleControlsEnabled(false)     // 比例尺
            options.zoomControlsEnabled(false)      // 地图缩放控制按钮
            options.tiltGesturesEnabled(false)      // 旋转
            options.zoomGesturesEnabled(true)       // 地图缩放控制手势
            options.compassEnabled(false)
            options.zOrderOnTop(true)

            val gdLatLng: STLatLng? = initLatLon.convertTo(STLatLngType.GCJ02)
            if (gdLatLng?.isValid() == true) {
                options.camera(CameraPosition.fromLatLngZoom(LatLng(gdLatLng.latitude, gdLatLng.longitude), wrapGaodeZoomLevelFromBaidu(initZoomLevel)))
            }

            val mapView = TextureMapView(context, options)

            initMapView(mapView)
            return mapView
        }

        @JvmStatic
        private fun initMapView(mapView: TextureMapView): TextureMapView = mapView.apply {
            map.apply {

                setMapTheme(mapView)

                mapType = AMap.MAP_TYPE_NORMAL // 普通地图（包含3D地图）
                isTrafficEnabled = false            // 开启交通图


                val locationStyle = MyLocationStyle()   //初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
                locationStyle.interval(1000)            //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒
                locationStyle.showMyLocation(true)
                locationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER) //连续定位、蓝点不会移动到地图中心点，定位点依照设备方向旋转，并且蓝点会跟随设备移动
                myLocationStyle = locationStyle         //设置定位蓝点的Style
                isMyLocationEnabled = true              // 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false

                uiSettings.apply {
                    isScaleControlsEnabled = true
                    isZoomControlsEnabled = true
                    isCompassEnabled = true
                    isMyLocationButtonEnabled = false // 设置默认定位按钮是否显示，非必需设置。
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
                inputStream = context.assets.open("map_style/gaode/$customStyleFileName")
                val buffer = ByteArray(inputStream.available())
                inputStream.read(buffer)

                val mapStyleDir = STCacheManager.getChildDir(STCacheManager.getFilesDir(), "map_style")
                val gaodeMapStyleDir = STCacheManager.getChildDir(mapStyleDir, "gaode")
                parentPath = gaodeMapStyleDir.absolutePath

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
