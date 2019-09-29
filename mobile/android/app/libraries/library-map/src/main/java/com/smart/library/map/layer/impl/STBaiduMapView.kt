package com.smart.library.map.layer.impl

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng

class STBaiduMapView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

    companion object {
        //        const val mapAnimateDuration = 500
        const val defaultZoomLevel = 14f
        //        const val defaultSingleMarkerZoomLevel = defaultZoomLevel
        val defaultTargetLatLng = LatLng(31.2451050000, 121.5063770000) // 东方明珠塔
//        var currentLatLng: LatLng = LatLng(LocationManager.instance.cacheLatLng?.latitude ?: 0.0, LocationManager.instance.cacheLatLng?.longitude ?: 0.0)
//        private val infoWindowOffset: Int = (STSystemUtil.getPxFromDp(32f) * 1.35f).toInt()

        @JvmStatic
        fun createMapView(context: Context, initLatLon: LatLng, initZoomLevel: Float): MapView {
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

            map.apply {
                changeLocationLayerOrder(true)     // 定位图层位于 marker 之下
                mapType = BaiduMap.MAP_TYPE_NORMAL // 普通地图（包含3D地图）
                isTrafficEnabled = false           // 开启交通图
                isBaiduHeatMapEnabled = false      // 百度城市热力图
                setViewPadding(0, 0, 0, 0)         // 设置地图操作区距屏幕的距离
                setMaxAndMinZoomLevel(21f, 3f)     // 限制缩放等级
                showMapPoi(true)                   // 隐藏底图标注（控制地图POI显示）
                uiSettings.apply {
                    isScrollGesturesEnabled = true          // 地图平移
                    isZoomGesturesEnabled = true            // 地图缩放
                    isOverlookingGesturesEnabled = false    // 地图俯视（3D）
                    isRotateGesturesEnabled = false         // 地图旋转
                    // setAllGesturesEnabled(true)            // 禁止所有手势
                }
            }
        }
    }

    fun initialize(initLatLon: LatLng = defaultTargetLatLng, initZoomLevel: Float = defaultZoomLevel) {
        addView(createMapView(context, initLatLon, initZoomLevel))
    }
}