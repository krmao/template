package com.smart.library.base

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.smart.library.util.HKLogUtil
import com.smart.library.util.rx.RxBus
import kotlin.properties.Delegates

@Suppress("unused", "MemberVisibilityCanPrivate")
open class HKActivityLifecycleCallbacks : Application.ActivityLifecycleCallbacks {
    // I use four separate variables here. You can, of course, just use two and
    // increment/decrement them instead of using four and incrementing them all.
    /*private int resumed;
    private int paused;
    private int started;
    private int stopped;*/

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) = Unit

    override fun onActivityDestroyed(activity: Activity) = Unit

    override fun onActivityResumed(activity: Activity) {
        ++resumed
        ++applicationVisibleChanged
    }

    override fun onActivityPaused(activity: Activity) {
        ++paused
        --applicationVisibleChanged
        HKLogUtil.w("test", "application is in foreground: " + (resumed > paused))
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) = Unit

    override fun onActivityStarted(activity: Activity) {
        ++started
    }

    override fun onActivityStopped(activity: Activity) {
        ++stopped
        HKLogUtil.w("test", "application is visible: " + (started > stopped))
    }

    companion object {

        private var resumed = 0
        private var paused = 0
        private var started = 0
        private var stopped = 0

        @Suppress("UNUSED_ANONYMOUS_PARAMETER")
        private var applicationVisibleChanged by Delegates.observable(0) { property, oldValue, newValue ->
            if (oldValue != newValue)
                RxBus.post(ApplicationVisibleChangedEvent(isApplicationVisible))
        }

        val isApplicationVisible: Boolean
            get() = applicationVisibleChanged > 0
    }

    class ApplicationVisibleChangedEvent(val isApplicationVisible: Boolean)
}
