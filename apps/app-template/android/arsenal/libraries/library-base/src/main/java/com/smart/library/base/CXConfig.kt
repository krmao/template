package com.smart.library.base

import java.text.SimpleDateFormat
import java.util.*

/**
 * <pre>
 * author : smart
 * e-mail : smart@smart.com
 * desc   : 基础类库全局配置文件
 * </pre>
 */
object CXConfig {
    @JvmStatic
    var NAME_SHARED_PREFERENCES = "com.smart.shared_preferences"

    @JvmStatic
    val NAME_CACHE_WEB_DIR: String = "cache_web"

    @JvmStatic
    val NAME_LOG_DIR: String = "log"

    @JvmStatic
    val NAME_NEW_LOG: String
        get() = "log_" + SimpleDateFormat("yyyyMMdd_HHmmssSSS", Locale.getDefault()).format(Date(System.currentTimeMillis())) + ".txt"

    @JvmStatic
    var ENABLE_TRACE_DEBUG = false //开启埋点调试开关，执行 toast 提示 以及 log 打印
}
