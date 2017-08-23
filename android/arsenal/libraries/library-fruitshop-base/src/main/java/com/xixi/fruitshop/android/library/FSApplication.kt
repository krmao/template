package com.xixi.fruitshop.android.library

import com.meituan.android.walle.WalleChannelReader
import com.xixi.library.android.base.FSBaseApplication

/**
 * <pre>
 *     author : maokangren
 *     e-mail : maokangren@chexiang.com
 *     desc   :
 * </pre>
 */
@Suppress("unused")
open class FSApplication : FSBaseApplication() {

    companion object {
        lateinit var INSTANCE: FSApplication
    }

    val channel: String by lazy { WalleChannelReader.getChannel(this) ?: "" }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        FSInitializer.init()
    }
}