package com.xixi.fruitshop.android.module.mine

import android.app.Application
import android.util.Log

/**
 * 仅仅用于第一次安装bundle时执行的初始化代码，不能用作上下文application
 */
class MineApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.e("krmao", "MineApplication:onCreate")
    }
}
