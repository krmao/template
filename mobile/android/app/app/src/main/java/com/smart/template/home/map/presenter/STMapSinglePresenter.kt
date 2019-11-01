package com.smart.template.home.map.presenter

import com.smart.template.home.map.model.STMapFromMode

/**
 * 单点模式(无搜索该区域, 不可拖拽滑动)
 *      1: 从POI列表/城市榜单列表/热点榜单列表进入单点模式     (需要状态恢复)
 *      2: 从POI详情进入单点模式     (需要状态恢复)
 *      3: 从探索附近模式进入单点模式 (不需要状态恢复)
 */
@Suppress("MemberVisibilityCanBePrivate", "unused")
class STMapSinglePresenter(val fromMode: STMapFromMode) {

    private val needRestoreStatusAfterNextEnter: Boolean = false

    /**
     * 进入单点模式
     */
    fun enter(onExitCallback: () -> Unit) {
        when (fromMode) {
            STMapFromMode.NEARBY -> TODO()
            STMapFromMode.SINGLE -> TODO()
            STMapFromMode.CITY -> TODO()
            STMapFromMode.MULTI_HORIZONTAL -> TODO()
            STMapFromMode.MULTI_VERTICAL -> TODO()
            STMapFromMode.FROM_NEARBY_SWITCH -> {
                // 1: 显示单点面板, 隐藏其它面板
                // 2: 请求显示 单点 poi 详细信息 并展示在地图上
            }
            STMapFromMode.FROM_NEARBY_EXIT -> TODO()
        }
    }

    /**
     * 点击 探索附近 切换为附近模式
     */
    fun switchToNearByModel() {

    }

    /**
     * 清空地图并删除所有该模式存在期间产生的任何数据
     */
    fun exit() {

    }
}