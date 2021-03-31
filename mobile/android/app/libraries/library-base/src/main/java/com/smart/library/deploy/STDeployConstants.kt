package com.smart.library.deploy

import androidx.annotation.Keep

@Suppress("MemberVisibilityCanBePrivate")
@Keep
object STDeployConstants {

    const val DIR_NAME_APPLY = "apply"              // 已经成功应用的目录
    const val DIR_NAME_APPLY_UNZIP = "apply-%d"     // 解压目录名称, %d为版本号
    const val DIR_NAME_TEMP = "temp"                // 合并差分包临时目录
    const val DIR_NAME_BASE = "base"                // 基础包存放目录
    const val FILE_NAME_PATCH = "patch-%d-%d.patch" // patch 文件, 基础版本-目标版本
    const val FILE_NAME_BASE_ZIP = "base-%d.zip"    // 基础压缩包, %d为版本号
    const val FILE_NAME_TEMP_ZIP = "temp-%d.zip"    // 合并压缩包, %d为版本号
    const val FILE_NAME_APPLY_ZIP = "apply-%d.zip"  // 目标压缩包, %d为版本号
    const val DIR_NAME_BASE_UNZIP = "base-%d"       // 基础包解压目录, %d为版本号
}