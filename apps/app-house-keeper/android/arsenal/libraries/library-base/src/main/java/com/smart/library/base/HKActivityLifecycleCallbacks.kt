package com.smart.library.base

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.smart.library.util.HKLogUtil
import com.smart.library.util.rx.RxBus
import kotlin.properties.Delegates

@Suppress("unused", "MemberVisibilityCanPrivate", "UNUSED_ANONYMOUS_PARAMETER")
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
        HKLogUtil.w("applicationLifeCycle", "application is in foreground: " + (resumed > paused))
    }

    override fun onActivityPaused(activity: Activity) {
        ++paused
        HKLogUtil.w("applicationLifeCycle", "application is in foreground: " + (resumed > paused))
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) = Unit

    override fun onActivityStarted(activity: Activity) {
        ++started
        HKLogUtil.w("applicationLifeCycle", "application is visible " + (resumed > paused))
    }

    override fun onActivityStopped(activity: Activity) {
        ++stopped
        HKLogUtil.w("applicationLifeCycle", "application is visible: " + (started > stopped))
    }

    companion object {

        private var resumed: Int by Delegates.observable(0) { _, _, newValue ->
        }

        private var paused: Int by Delegates.observable(0) { _, _, newValue ->
        }

        private var started: Int by Delegates.observable(0) { _, _, newValue ->
            isApplicationVisible = newValue > stopped
        }

        private var stopped: Int by Delegates.observable(0) { _, _, newValue ->
            isApplicationVisible = started > newValue
        }

        @Suppress("UNUSED_ANONYMOUS_PARAMETER")
        var isApplicationVisible: Boolean by Delegates.observable(false) { property, oldValue, newValue ->
            HKLogUtil.w("applicationLifeCycle", "(isApplicationVisible : oldValue=$oldValue , newValue=$newValue)")
            HKLogUtil.w("applicationLifeCycle", "(resumed=$resumed , paused=$paused)")
            HKLogUtil.w("applicationLifeCycle", "(started=$started , stopped=$stopped)")
            if (oldValue != newValue) {
                HKLogUtil.e("applicationLifeCycle", "系统监测到应用程序 正在 从 ${if (oldValue) "前台" else "后台"} 切换到 ${if (newValue) "前台" else "后台"} ")
                RxBus.post(ApplicationVisibleChangedEvent(newValue))
            }
        }
    }

    class ApplicationVisibleChangedEvent(val isApplicationVisible: Boolean)
}
