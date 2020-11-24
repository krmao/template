package com.smart.template

import com.smart.library.util.STLogUtil

@Suppress("unused")
object FinalApplicationInitManager {

    private var isInitialized: Boolean = false

    @JvmStatic
    fun isInitialized(): Boolean = isInitialized

    @JvmStatic
    fun initialize(callback: ((key: String, success: Boolean) -> Unit)? = null) {
        STLogUtil.e("application", "initialize start isInitialized=${isInitialized()}, thread:${Thread.currentThread().name}")

        if (isInitialized()) return

        // todo FlutterBusHandler exception, startInitialization must be called on the main thread Flowable.fromCallable { asyncInitialize(callback) }.subscribeOn(Schedulers.newThread()).subscribe()
        isInitialized = true
        STLogUtil.e("application", "initialize end isInitialized =$ { isInitialized() }")
    }

}
