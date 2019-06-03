package com.smart.template.library

//import com.meituan.android.walle.WalleChannelReader
import com.smart.library.base.STBaseApplication

@Suppress("unused")
open class STApplication : STBaseApplication() {

    companion object {
        lateinit var INSTANCE: STApplication
    }

    //val channel: String by lazy { WalleChannelReader.getChannel(this) ?: "" }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }
}
