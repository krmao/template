package com.smart.template.library

import android.app.Application
import com.smart.library.base.STBaseApplication
import com.smart.library.util.STReflectUtil

@Suppress("unused")
open class STApplication : STBaseApplication() {

    companion object {
        lateinit var INSTANCE: STApplication
    }

    val channel: String by lazy {
        STReflectUtil.invokeJavaStaticMethod("com.meituan.android.walle.WalleChannelReader", "getChannel", arrayOf(Application::class.java), arrayOf(this)) as? String ?: ""
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }
}
