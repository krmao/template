package com.smart.template.home.map.util

import com.smart.template.home.map.model.STIMapModePresenter
import java.util.*

/**
 * 地图模式栈, 可以看成页面栈
 */
object STMapModeStackManager {
    private val modeStack: Stack<STIMapModePresenter> = Stack()

    fun pushMapMode(mapModePresenter: STIMapModePresenter) {
        modeStack.push(mapModePresenter)
    }

    fun pop() {
        modeStack.pop()
    }

    fun currentMapMode(): STIMapModePresenter {
        return modeStack.peek()
    }

    fun preMapMode(): STIMapModePresenter? {
        return modeStack.getOrNull(modeStack.size - 2)
    }
}