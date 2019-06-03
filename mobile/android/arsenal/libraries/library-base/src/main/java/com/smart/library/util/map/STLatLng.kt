package com.smart.library.util.map

import com.smart.library.util.STValueUtil


/**
 *  地球坐标 (WGS84)
 *      国际标准，从专业GPS 设备中取出的数据的坐标系
 *      国际地图提供商使用的坐标系
 *      谷歌地图采用的是WGS84地理坐标系（中国范围除外)
 *  火星坐标 (GCJ-02)也叫国测局坐标系
 *      中国标准，从国行移动设备中定位获取的坐标数据使用这个坐标系
 *      国家规定： 国内出版的各种地图系统（包括电子形式），必须至少采用GCJ-02对地理位置进行首次加密。
 *      谷歌中国地图和搜搜中国地图采用的是GCJ02地理坐标系
 *  百度坐标 (BD-09)
 *      百度标准，百度 SDK，百度地图，geoCoding 使用 (百度在火星坐标上来个二次加密)
 */
@Suppress("MemberVisibilityCanBePrivate", "unused")
data class STLatLng(var latitude: Double = 0.0, var longitude: Double = 0.0) {

    companion object {
        @JvmStatic
        val NOT_VALID = STLatLng()
    }


    fun isNotValid(): Boolean = !isValid()
    fun isValid(): Boolean = STMapUtil.isValidLatLng(latitude, longitude)

    override fun equals(other: Any?): Boolean = this === other || (other is STLatLng && STValueUtil.isAEqualB(latitude, other.latitude) && STValueUtil.isAEqualB(longitude, other.longitude))

    override fun hashCode(): Int {
        var result = latitude.hashCode()
        result = 31 * result + longitude.hashCode()
        return result
    }
}
