package com.xixi.library.android.base

import java.text.SimpleDateFormat
import java.util.*

/**
 * <pre>
 * author : maokangren
 * e-mail : maokangren@chexiang.com
 * desc   : 基础类库全局配置文件
 * </pre>
 */
object CXConfig {
    var NAME_SHARED_PREFERENCES = "com.xixi.shared_preferences"

    val NAME_LOG_DIR: String = "LOG"

    val NAME_NEW_LOG: String
        get() = "Log_" + SimpleDateFormat("yyyyMMdd_HHmmssSSS", Locale.getDefault()).format(Date(System.currentTimeMillis())) + ".txt"

}
