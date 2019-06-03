package com.smart.library.base

import android.app.Activity
import android.app.Application
import android.os.Bundle

open class STActivityLifecycleCallbacks : Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) = Unit

    override fun onActivityDestroyed(activity: Activity) = Unit

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) = Unit

    override fun onActivityPaused(activity: Activity?) = Unit

    override fun onActivityResumed(activity: Activity?) = Unit

    override fun onActivityStarted(activity: Activity) {
        STBaseApplication.isApplicationVisible = ++STBaseApplication.activityStartedCount > STBaseApplication.activityStoppedCount
    }

    override fun onActivityStopped(activity: Activity) {
        STBaseApplication.isApplicationVisible = STBaseApplication.activityStartedCount > ++STBaseApplication.activityStoppedCount
    }
}
