package com.xixi.library.android.util

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

object FSRouteManager {

    enum class Type {
        native,
        hybird
    }

    private val routeMap: ConcurrentMap<String, Set<(activity: Activity?, uri: Uri?, bundle: Bundle?) -> Boolean>> = ConcurrentHashMap()
    private val interceptors: Set<(activity: Activity?, uri: Uri?, bundle: Bundle?) -> Boolean> = Collections.synchronizedSet(java.util.HashSet())

    fun addInterceptor(route: String, interceptor: (activity: Activity?, uri: Uri?, bundle: Bundle?) -> Boolean) {
        routeMap.put(route, (routeMap[route] ?: Collections.synchronizedSet(java.util.HashSet())).plus(interceptor))
    }

    fun processJump(activity: Activity?, uri: Uri?): Boolean {
        val paramBundle: Bundle = Bundle()
        uri?.query?.split("&")?.map { it.split("=") }?.filter { it.size == 2 }?.forEach { paramBundle.putString(it[0], it[1]) }
        return interceptors.any { it.invoke(activity, uri, paramBundle) }
    }

    fun goTo(activity: Activity, uri: Uri, a: Int) {
        goTo(activity, uri)
        goTo(activity, uri, { jumpSuccessful ->
            if (jumpSuccessful) {
                Bundle()
            } else {
                Bundle()
            }
        })
    }

    fun goTo(activity: Activity, uri: Uri, callback: ((jumpSuccessful: Boolean) -> Bundle?)? = null) {

    }
}