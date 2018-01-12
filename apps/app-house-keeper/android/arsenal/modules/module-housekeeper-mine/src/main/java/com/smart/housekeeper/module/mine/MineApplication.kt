package com.smart.housekeeper.module.mine

import android.app.Application
import android.util.Log

/**
 * 仅仅用于第一次安装bundle时执行的初始化代码，不能用作上下文application
 */
object MineApplication {
    fun init() {
        Log.e("krmao", "MineApplication:init")
    }
}
