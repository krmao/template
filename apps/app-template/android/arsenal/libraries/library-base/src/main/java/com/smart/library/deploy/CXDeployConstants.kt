package com.smart.library.deploy

@Suppress("MemberVisibilityCanBePrivate")
object CXDeployConstants {

    const val DIR_NAME_APPLY = "apply"              // 已经成功应用的目录
    const val DIR_NAME_APPLY_UNZIP = "apply-%d"     // 解压目录名称, %d为版本号
    const val DIR_NAME_TEMP = "temp"                // 合并差分包临时目录
    const val DIR_NAME_BASE = "base"                // 基础包存放目录
    const val DIR_NAME_BASE_UNZIP = "base-%d"       // 基础包解压目录, %d为版本号

}