package com.xixi.module.test

import android.app.Activity
import android.util.Log
import com.xixi.library.android.util.rx.RxBus
import com.xixi.library.android.util.rx.RxRouteEvent

/**
 * <pre>
 *     author : maokangren
 *     e-mail : maokangren@chexiang.com
 *     desc   :
 * </pre>
 */
object TestRouteManager {
    init {
        Log.w("TestRouteManager", "init success")
        RxBus.toObservable(RxRouteEvent::class.java).subscribe { routeEvent ->
            Log.w("TestRouteManager", "RxRouteEvent::subscribe:" + Thread.currentThread().name + "\n$routeEvent")
            if (routeEvent.targetUrl == "test://detail")
                if (routeEvent.activity != null)
                    goToTest(routeEvent.activity!!)
        }
    }

    fun goToTest(activity: Activity) {
        TestFragment.goTo(activity)
    }
}