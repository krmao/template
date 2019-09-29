package com.smart.library.map.navigation

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import com.smart.library.util.*

@Suppress("unused", "MemberVisibilityCanBePrivate")
object STNavigationManager {
    private const val KEY_LAST_SELECTED_MAP_TYPE = "KEY_LAST_SELECTED_MAP_TYPE"

    @JvmStatic
    var lastSelectedMapType: MapType = MapType.valueOf(STPreferencesUtil.getString(KEY_LAST_SELECTED_MAP_TYPE, MapType.AMAP.name))
        private set(value) {
            field = value
            STPreferencesUtil.putString(KEY_LAST_SELECTED_MAP_TYPE, value.name)
        }

    enum class MapSupportScope {
        CHINA,
        OVERSEA,
        ALL
    }

    enum class MapType(val displayName: String, val packageName: String, val mapSupportScope: MapSupportScope) {
        AMAP("高德地图", "com.autonavi.minimap", MapSupportScope.CHINA),
        BMAP("百度地图", "com.baidu.BaiduMap", MapSupportScope.CHINA),
        GOOGLE("系统地图", "com.google.android.apps.maps", MapSupportScope.OVERSEA);

        fun isAppInstalled(): Boolean = STSystemUtil.isAppInstalled(packageName)

        /**
         * AMAP/BMAP 使用火星坐标系 GCJ02
         * GOOGLE 国内使用 火星坐标系 GCJ02, 国外使用全球地理坐标系 WGS84
         */
        @SuppressLint("DefaultLocale")
        fun open(context: Context?, fromGCJ02Lat: String, fromGCJ02Lon: String, fromWGS84Lat: String? = null, fromWGS84Lon: String? = null, fromAddressName: String?, toGCJ02Lat: String, toGCJ02Lon: String, toWGS84Lat: String? = null, toWGS84Lon: String? = null, toAddressName: String?, navigateMode: String = "driving") {
            when (this) {
                AMAP -> {
                    try {
                        val versionNo = STSystemUtil.getAppVersionCode("com.autonavi.minimap")
                        STLogUtil.d("versionNo:$versionNo")
                        if (versionNo < 153) {
                            STToastUtil.show("您当前地图版本可能不支持导航功能")
                            return
                        }
                        val intentUri = StringBuffer()
                        intentUri.append("androidamap://route")
                        intentUri.append("?sourceApplication=${STSystemUtil.appName}&slat=$fromGCJ02Lat&slon=$fromGCJ02Lon&sname=${if (TextUtils.isEmpty(fromAddressName)) "起点" else fromAddressName}\"")
                        intentUri.append("&dlat=$toGCJ02Lat&dlon=$toGCJ02Lon&dname=${if (TextUtils.isEmpty(toAddressName)) "终点" else toAddressName}&dev=0&m=0")
                        var mode = 2
                        when (navigateMode.toLowerCase()) {
                            "transit" -> mode = 1
                            "walking" -> mode = 4
                        }
                        intentUri.append("&t=$mode")
                        val intent = Intent("android.intent.action.VIEW", Uri.parse(intentUri.toString()))
                        intent.addCategory("android.intent.category.DEFAULT")
                        intent.`package` = "com.autonavi.minimap"
                        context?.startActivity(intent)
                    } catch (e: Exception) {
                        STLogUtil.e("start auto naviMap exception:$e")
                        STToastUtil.show("您当前地图版本可能不支持导航功能")
                    }
                }
                BMAP -> { // http://lbsyun.baidu.com/index.php?title=uri/api/android
                    try {
                        val intentUri = StringBuffer()
                        intentUri.append("intent://map/direction?coord_type=gcj02&&origin=latlng:$fromGCJ02Lat,$fromGCJ02Lon${if (fromAddressName.isNullOrBlank()) "|name:起点" else "|name:$fromAddressName"}")
                        intentUri.append("&destination=latlng:$toGCJ02Lat,$toGCJ02Lon${if (toAddressName.isNullOrEmpty()) "|" + "name:" + "终点" else "|name:$toAddressName"}")
                        intentUri.append(String.format("&mode=%s", if (navigateMode.isBlank()) "driving" else navigateMode))
                        intentUri.append("&coord_type=bd09ll&src=smart#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end")
                        val intent = Intent.parseUri(intentUri.toString(), 0)
                        intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        context?.startActivity(intent)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        STToastUtil.show("您当前地图版本可能不支持导航功能")
                    }
                }
                GOOGLE -> {
                    try {
                        val intentUri = StringBuffer()
                        intentUri.append("http://maps.google.com/maps?saddr=$fromWGS84Lat,$fromWGS84Lon&daddr=$toWGS84Lat,$toWGS84Lon")
                        if (!TextUtils.isEmpty(navigateMode)) intentUri.append(String.format("&directionsmode=%s", navigateMode))
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(intentUri.toString()))
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK and Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity")
                        context?.startActivity(intent)
                    } catch (e: Exception) {
                        STLogUtil.e("start google map exception:$e")
                        STToastUtil.show("您当前地图版本可能不支持导航功能")
                    }
                }
            }
        }
    }

    /**
     * @param mapSupportScope 传 null 则所有地图全部显示
     */
    @JvmStatic
    fun getInstalledMapTypeList(mapSupportScope: MapSupportScope?, enableUserBehavior: Boolean = true): MutableList<MapType> {
        val installedMapTypeList = arrayListOf<MapType>()
        if (enableUserBehavior) {
            if (lastSelectedMapType.isAppInstalled()) installedMapTypeList.add(lastSelectedMapType)
            installedMapTypeList.addAll(MapType.values().filter { it != lastSelectedMapType && it.isAppInstalled() })
        } else {
            installedMapTypeList.addAll(MapType.values())
        }
        mapSupportScope?.let {
            installedMapTypeList.removeAll { it.mapSupportScope != mapSupportScope }
        }
        return installedMapTypeList
    }

    /**
     * @param mapSupportScope 传 null 则所有地图全部显示
     * @param enableUserBehavior true 表示上一次选中的地图类型会默认排到最上面
     */
    @JvmStatic
    fun show(mapSupportScope: MapSupportScope? = MapSupportScope.CHINA, enableUserBehavior: Boolean = true, dialogProxy: (installedMapTypeList: MutableList<MapType>) -> Unit?) {
        dialogProxy.invoke(getInstalledMapTypeList(mapSupportScope, enableUserBehavior))
    }

    /**
     * @param mapSupportScope 传 null 则所有地图全部显示
     * @param enableUserBehavior true 表示上一次选中的地图类型会默认排到最上面
     * @param navigateMode = driving/walking/transit
     */
    @JvmStatic
    fun showDialog(context: Context?, mapSupportScope: MapSupportScope? = MapSupportScope.CHINA, enableUserBehavior: Boolean = true, fromGCJ02Lat: String, fromGCJ02Lon: String, fromWGS84Lat: String? = null, fromWGS84Lon: String? = null, fromAddressName: String?, toGCJ02Lat: String, toGCJ02Lon: String, toWGS84Lat: String? = null, toWGS84Lon: String? = null, toAddressName: String?, navigateMode: String = "driving"): Dialog? {
        val installedMapTypeList = getInstalledMapTypeList(mapSupportScope, enableUserBehavior)

        if (installedMapTypeList.isEmpty()) {
            when (mapSupportScope) {
                MapSupportScope.CHINA -> STToastUtil.show("请安装地图应用")
                MapSupportScope.OVERSEA -> STToastUtil.show("请安装Google地图应用")
                MapSupportScope.ALL -> STToastUtil.show("请安装地图应用")
                null -> STToastUtil.show("请安装地图应用")
            }
            return null
        }

        var dialog: Dialog? = null

        val names = installedMapTypeList.map { it.displayName }.toTypedArray()
        context?.let {
            if (STValueUtil.isValid(context)) {
                dialog = AlertDialog.Builder(it)
                        .setItems(names) { _, which ->
                            val selectedType = MapType.values().firstOrNull { it.displayName == names[which] }
                            if (selectedType != null) {
                                lastSelectedMapType = selectedType
                                selectedType.open(context, fromGCJ02Lat, fromGCJ02Lon, fromWGS84Lat, fromWGS84Lon, fromAddressName, toGCJ02Lat, toGCJ02Lon, toWGS84Lat, toWGS84Lon, toAddressName, navigateMode)
                            }
                        }
                        .setTitle("本地已安装地图应用")
                        .setCancelable(true)
                        .create()
                dialog?.show()
            }
        }
        return dialog
    }
}
