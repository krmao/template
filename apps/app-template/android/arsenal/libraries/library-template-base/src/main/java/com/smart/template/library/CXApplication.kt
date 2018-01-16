package com.smart.template.library

import com.meituan.android.walle.WalleChannelReader
import com.smart.library.base.CXBaseApplication

@Suppress("unused")
open class CXApplication : CXBaseApplication() {

    companion object {
        lateinit var INSTANCE: CXApplication
    }

    val channel: String by lazy { WalleChannelReader.getChannel(this) ?: "" }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        CXInitializer.init()
    }
}