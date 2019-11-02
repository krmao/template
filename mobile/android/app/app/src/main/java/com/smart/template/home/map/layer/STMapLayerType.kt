package com.smart.template.home.map.layer

/**
 * 地图所有的层级
 * @param needResume 当从 layerA 跳转到 layerB 然后回退到 layerA的时候, layerA 是否需要恢复到 进入 layerB 之前的状态
 */
@Suppress("unused", "MemberVisibilityCanBePrivate")
enum class STMapLayerType(val needResume: Boolean) {
    MAIN(true),                   // 初始化地图的第一个层，存放固定的不会被隐藏和清空的数据
    CITY(true),                   // 城市层/心愿单
    MULTI_HORIZONTAL(true),       // 水平列表/心愿单
    MULTI_VERTICAL(true),         // 垂直列表/poi 列表/榜单
    SINGLE(true),                 // 单点页面/列表页面进入的非附近进入的单点模式(需要恢复状态)
    NEARBY_MULTI(false),          // 附近列表页(不需要恢复状态)
    NEARBY_SINGLE(false);         // 附近单点页面(不需要恢复状态)
}