package com.smart.library.bundle.strategy

/**
 * 选择哪一种初始化策略
 */
enum class CXHybirdInitStrategy {

    //本地没有随app打包的 hybird 模块包,需要第一次启动app 去下载
    DOWNLOAD,

    //本地app包含hybird 模块包,app启动从本地初始化
    LOCAL

}
