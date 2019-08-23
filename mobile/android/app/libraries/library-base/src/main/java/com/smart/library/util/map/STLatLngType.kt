package com.smart.library.util.map

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
@Suppress("unused")
enum class STLatLngType {
    UNKNOWN, GCJ02, WGS84, BD09
}