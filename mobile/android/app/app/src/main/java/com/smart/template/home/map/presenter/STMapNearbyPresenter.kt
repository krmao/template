package com.smart.template.home.map.presenter

import com.smart.template.home.map.model.STMapFromMode

/**
 * 探索附近模式(无需恢复状态)
 * ----------
 * 1) 当 详情页/横向列表/竖向列表 进入探索附近模式
 *      1:原有的地图上的列表数据不带入附近模式, 只带入选中的那个poi, 地图清空并使用单点 marker(红色) 重新绘制
 *      2:请求附近数据添加到具有加载更多功能的横向滚动列表, 并展示到地图上, 默认选中第一个标签, 滚动到底部自动加载更多附近数据到地图上
 *          此时再次点击附近里面的单点, 进入单点模式并可以再次点击附近无限循环切换, 状态不做恢复
 *
 * 2) 退出探索附近模式 还原之前列表页/详情页的状态
 */
@Suppress("MemberVisibilityCanBePrivate", "unused")
class STMapNearbyPresenter(val fromMode: STMapFromMode) {

    /**
     * 进入附近模式
     */
    fun enter(onExitCallback: () -> Unit) {
        when (fromMode) {
            STMapFromMode.NEARBY -> TODO()
            STMapFromMode.SINGLE -> TODO()
            STMapFromMode.CITY -> TODO()
            STMapFromMode.MULTI_HORIZONTAL -> TODO()
            STMapFromMode.MULTI_VERTICAL -> TODO()
        }
    }

    /**
     * 切换到附近 单个 poi 单点模式
     */
    fun switchTo() {

    }

    /**
     * 退出附近模式
     * 清空地图并删除所有数据
     */
    fun exit() {

    }
}