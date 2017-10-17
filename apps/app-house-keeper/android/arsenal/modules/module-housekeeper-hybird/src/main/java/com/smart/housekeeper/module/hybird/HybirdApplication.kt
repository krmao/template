package com.smart.housekeeper.module.hybird

import android.app.Application
import android.util.Log
import com.smart.library.bundle.imp.HKBundleManager

/**
 * 仅仅用于第一次安装bundle时执行的初始化代码，不能用作上下文application
 */
class HybirdApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.e("krmao", "HybirdApplication:onCreate")
        HKBundleManager.installWithVerify()
    }
}
