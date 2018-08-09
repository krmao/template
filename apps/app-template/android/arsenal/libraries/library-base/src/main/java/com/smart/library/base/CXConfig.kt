package com.smart.library.base

import android.app.Activity
import com.smart.library.R
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
    var ENABLE_TRACE_DEBUG = false //开启埋点调试开关，执行 toast 提示 以及 log 打印, 只有debug包可用

    @JvmStatic
    var ENABLE_ACTIVITY_INFO_DEBUG = false //开启页面信息提示开关, 只有debug包可用

    @JvmStatic
    val NOTIFICATION_DEFAULT_DEBUG_CHANNEL_ID: Int = 100000000
    @JvmStatic
    var NOTIFICATION_ICON_SMALL: Int = R.drawable.cx_emo_im_happy
    @JvmStatic
    val NOTIFICATION_DEFAULT_SUMMARY_GROUP_KEY: String = "NOTIFICATION_DEFAULT_GROUP_KEY"
    @JvmStatic
    val NOTIFICATION_DEFAULT_SUMMARY_GROUP_TEXT: String = "SETTINGS"
    @JvmStatic
    val NOTIFICATION_DEFAULT_SUMMARY_GROUP_ID: Int = 200000000
    @JvmStatic
    val NOTIFICATION_DEFAULT_CHANNEL_GROUP_ID: String = NOTIFICATION_DEFAULT_SUMMARY_GROUP_ID.toString()
    @JvmStatic
    val NOTIFICATION_DEFAULT_CHANNEL_GROUP_NAME: String = "DEBUG"

    @JvmStatic
    var CLASS_ACTIVITY_MAIN: Class<out Activity>? = null
    @JvmStatic
    var CLASS_ACTIVITY_LOGIN: Class<out Activity>? = null
}
