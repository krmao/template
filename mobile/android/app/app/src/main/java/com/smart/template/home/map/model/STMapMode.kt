package com.smart.template.home.map.model

enum class STMapMode {
    NEARBY,
    SINGLE,

    /**
     * 从 nearby 模式尚未退出 进入的
     * 不许与还原状态
     */
    FROM_NEARBY_SWITCH,
    /**
     * 从 nearby 模式已经退出 进入的
     */
    FROM_NEARBY_EXIT,

    CITY,
    MULTI_HORIZONTAL,
    MULTI_VERTICAL
}