package com.smart.library.bundle.strategy

/**
 * 检查本地文件是否已经准备充分,可以进行加载操作
 */
enum class HKHybirdCheckStrategy {
    //已经准备充分,不执行重复检验本地文件的操作
    READY,
    //无论是否准备充分,都需要再进行一次重复检验本地文件的操作
    ALL
}
