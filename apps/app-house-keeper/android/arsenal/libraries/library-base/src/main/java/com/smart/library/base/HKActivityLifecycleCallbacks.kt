package com.smart.library.base

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.smart.library.util.HKLogUtil
import com.smart.library.util.rx.RxBus
import kotlin.properties.Delegates
import kotlin.reflect.KProperty

@Suppress("unused", "MemberVisibilityCanPrivate")
class HKActivityLifecycleCallbacks : Application.ActivityLifecycleCallbacks {
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

        // If you want a static function you can use to check if your application is
        // foreground/background, you can use the following:

        // Replace the four variables above with these four
        private var resumed by Delegates.observable(0) { _, _, _ ->
        }
        private var paused by Delegates.observable(0) { _, _, _ ->
        }
        private var started by Delegates.observable(0) { _, _, _ ->
        }
        private var stopped by Delegates.observable(0) { _, _, _ ->
        }

        @Suppress("UNUSED_ANONYMOUS_PARAMETER")
        private var applicationVisibleChanged by Delegates.observable(0) { property, oldValue, newValue ->
            if (oldValue != newValue)
                RxBus.post(ApplicationVisibleChangedEvent(isApplicationVisible))
        }

        // And these two public static functions
        val isApplicationVisible: Boolean
            get() = applicationVisibleChanged > 0 //started > stopped
    }

    class ApplicationVisibleChangedEvent(val isApplicationVisible: Boolean)
}
