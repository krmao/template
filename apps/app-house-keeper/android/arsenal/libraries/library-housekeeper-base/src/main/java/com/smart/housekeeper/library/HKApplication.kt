package com.smart.housekeeper.library

import com.meituan.android.walle.WalleChannelReader
import com.smart.library.base.HKBaseApplication

@Suppress("unused")
open class HKApplication : HKBaseApplication() {

    companion object {
        lateinit var INSTANCE: HKApplication
    }

    val channel: String by lazy { WalleChannelReader.getChannel(this) ?: "" }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        HKInitializer.init()
    }
}