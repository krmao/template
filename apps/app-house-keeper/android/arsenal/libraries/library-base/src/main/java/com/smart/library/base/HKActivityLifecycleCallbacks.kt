package com.smart.library.base

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.smart.library.util.HKLogUtil
import com.smart.library.util.rx.RxBus
import kotlin.properties.Delegates

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
    }

    override fun onActivityPaused(activity: Activity) {
        ++paused
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
            RxBus.post(ForegroundEvent(isApplicationInForeground))
        }
        private var paused by Delegates.observable(0) { _, _, _ ->
            RxBus.post(ForegroundEvent(isApplicationInForeground))
        }
        private var started by Delegates.observable(0) { _, _, _ ->
            RxBus.post(VisibleEvent(isApplicationVisible))
        }
        private var stopped by Delegates.observable(0) { _, _, _ ->
            RxBus.post(VisibleEvent(isApplicationVisible))
        }

        // And these two public static functions
        val isApplicationVisible: Boolean
            get() = started > stopped

        val isApplicationInForeground: Boolean
            get() = resumed > paused
    }

    class ForegroundEvent(val isApplicationInForeground: Boolean)
    class VisibleEvent(val isApplicationVisible: Boolean)
}
