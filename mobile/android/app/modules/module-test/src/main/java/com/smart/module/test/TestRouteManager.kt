package com.smart.module.test

import android.app.Activity
import android.util.Log
import com.smart.library.util.rx.RxBus
import com.smart.library.util.rx.RxRouteEvent

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