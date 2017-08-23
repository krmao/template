package com.xixi.fruitshop.android.library

import com.meituan.android.walle.WalleChannelReader
import com.xixi.library.android.base.CXBaseApplication

/**
 * <pre>
 *     author : maokangren
 *     e-mail : maokangren@chexiang.com
 *     desc   :
 * </pre>
 */
@Suppress("unused")
open class FSApplication : CXBaseApplication() {

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