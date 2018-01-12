package com.smart.housekeeper.module.setting

import android.util.Log

/**
 * 仅仅用于第一次安装bundle时执行的初始化代码，不能用作上下文application
 */
class SettingApplication {
    fun init() {
        Log.e("krmao", "SettingApplication:init")
    }
}
