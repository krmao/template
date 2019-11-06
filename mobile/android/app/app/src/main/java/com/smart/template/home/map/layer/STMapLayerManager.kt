package com.smart.template.home.map.layer

import java.util.*

/**
 * 地图模式栈, 可以看成页面栈
 */
@Suppress("MemberVisibilityCanBePrivate", "unused")
object STMapLayerManager {
    private val layerStack: Stack<STIMapLayer> = Stack()

    /**
     * 1: 隐藏/退出上一个层
     * 2: 负责压栈并初始化新的层
     *
     * MAIN->SINGLE->(NEARBY_MULTI<=> NEARBY_SINGLE)                            // poi详情
     *
     * MAIN->MULTI_VERTICAL->SINGLE->(NEARBY_MULTI<=> NEARBY_SINGLE)            // poi列表/城市榜单列表/热点榜单列表
     *
     * MAIN->MULTI_HORIZONTAL->SINGLE->(NEARBY_MULTI<=> NEARBY_SINGLE)          // 心愿单列表
     * MAIN->CITY->MULTI_HORIZONTAL->SINGLE->(NEARBY_MULTI<=> NEARBY_SINGLE)    // 只有心愿单列表有城市层
     *
     */
    fun push(mapLayer: STIMapLayer) {
        // 先处理当前层是退出还是隐藏
        when (peek()?.layerType() ?: STMapLayerType.MAIN) {
            STMapLayerType.MAIN -> {
            }
            STMapLayerType.NEARBY_MULTI, STMapLayerType.NEARBY_SINGLE -> {
                layerStack.pop()?.onDestroy() //退出上一个层
            }
            else -> {
                layerStack.peek()?.onPause() // 隐藏上一个层
            }
        }
        // 再添加并初始化新的层
        layerStack.push(mapLayer)
        mapLayer.onCreate()
    }

    /**
     * 1: 负责出栈并清理当前层
     * 2: 恢复上一个层
     */
    fun pop() {
        layerStack.pop()?.onDestroy()
        layerStack.peek()?.onResume()
    }

    fun peek(): STIMapLayer? {
        return layerStack.peek()
    }

    fun prePeek(): STIMapLayer? {
        return layerStack.getOrNull(layerStack.size - 2)
    }
}