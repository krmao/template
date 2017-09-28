package com.smart.module.test

import android.util.Log

object HKRouteManager {
    val TAG: String = javaClass.name

    private val moduleMap: MutableMap<String, String> = hashMapOf(
            "hkj://native//test:" to "com.smart.module.test.TestRouteManager",
            "hkj://native//other:" to "com.smart.module.other.OtherRouteManager"
    )

    init {
        moduleMap.entries.forEach {
            try {
                Class.forName(it.value)
            } catch (exception: ClassNotFoundException) {
                Log.e(TAG, "${it.key},${it.value}", exception)
            }
        }
    }
}