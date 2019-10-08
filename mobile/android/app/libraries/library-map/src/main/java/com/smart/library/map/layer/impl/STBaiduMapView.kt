package com.smart.library.map.layer.impl

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.baidu.mapapi.SDKInitializer
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import com.smart.library.map.R
import com.smart.library.map.location.STLocationManager
import com.smart.library.map.location.STLocationSensorManager
import com.smart.library.map.model.STLatLng
import com.smart.library.util.STLogUtil
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream


class STBaiduMapView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

    companion object {
        val configMap: HashMap<String, String> = hashMapOf(
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

        //        const val mapAnimateDuration = 500
        const val defaultZoomLevel = 14f
        //        const val defaultSingleMarkerZoomLevel = defaultZoomLevel
        val defaultTargetLatLng = LatLng(31.2451050000, 121.5063770000) // 东方明珠塔
//        var currentLatLng: LatLng = LatLng(LocationManager.instance.cacheLatLng?.latitude ?: 0.0, LocationManager.instance.cacheLatLng?.longitude ?: 0.0)
//        private val infoWindowOffset: Int = (STSystemUtil.getPxFromDp(32f) * 1.35f).toInt()

        @JvmStatic
        fun createMapView(context: Context?, initLatLon: LatLng, initZoomLevel: Float): MapView {
            val options = BaiduMapOptions()
            options.mapType(BaiduMap.MAP_TYPE_NORMAL)
            options.mapStatus(MapStatus.Builder().target(initLatLon).zoom(initZoomLevel).build())
            options.compassEnabled(false)
            options.logoPosition(LogoPosition.logoPostionleftBottom)
            options.overlookingGesturesEnabled(false) // 地图俯视（3D）
            options.rotateGesturesEnabled(false) // 地图旋转
            options.scaleControlEnabled(false) // 比例尺
            options.scrollGesturesEnabled(true) // 地图平移手势
            options.zoomGesturesEnabled(true) // 地图缩放控制手势
            options.zoomControlsEnabled(false) // 地图缩放控制按钮

            val mapView = MapView(context, options)
            initMapView(mapView)
            return mapView
        }

        @JvmStatic
        fun initMapView(mapView: MapView): MapView = mapView.apply {
            logoPosition = LogoPosition.logoPostionleftBottom
            showZoomControls(false)             // 缩放按钮
            showScaleControl(false)             // 比例尺

            // 获取json文件路径
            val customStyleFilePath = getCustomStyleFilePath(mapView.context, configMap["青花瓷"])
            // 设置个性化地图样式文件的路径和加载方式
            setMapCustomStylePath(customStyleFilePath)
            // 动态设置个性化地图样式是否生效
            setMapCustomStyleEnable(true)

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

        @JvmStatic
        fun initialize(context: Context?) {
            SDKInitializer.initialize(context)
        }
    }

    private var mapView: MapView? = null
    private var locationSensorManager: STLocationSensorManager? = null
    private var currentLatLon: LatLng? = null

    @SuppressLint("InflateParams")
    fun initialize(initLatLon: LatLng = defaultTargetLatLng, initZoomLevel: Float = defaultZoomLevel) {
        if (this.mapView == null) {
            val innerMapView: MapView = createMapView(context, initLatLon, initZoomLevel)
            addView(innerMapView)
            locationSensorManager = STLocationSensorManager(context, innerMapView.map) {
                if (STLatLng.isValidLatLng(it.latitude, it.longitude)) {
                    currentLatLon = it
                }
            }

            LayoutInflater.from(context).inflate(R.layout.st_map_view_control_layout, this, true)
            findViewById<View>(R.id.locationBtn).setOnClickListener(locationSensorManager)
            findViewById<View>(R.id.settingsBtn).setOnClickListener {
            }
            this.mapView = innerMapView
        }
    }

    fun onResume() {
        mapView?.onResume()

        STLogUtil.d("location", "ensurePermissions ...")
        STLocationManager.ensurePermissionsWithoutHandling(context as? Activity) {
            STLogUtil.d("location", "ensurePermissions:$it")
            if (it) {
                locationSensorManager?.startLocation()
            }
        }
    }

    fun onPause() {
        mapView?.onPause()
        locationSensorManager?.stopLocation()
    }

    fun onDestroy() {
        locationSensorManager?.stopLocation()
        mapView?.map?.isMyLocationEnabled = false
        mapView?.onDestroy()
        mapView = null
    }
}