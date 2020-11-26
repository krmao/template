package com.smart.library.base

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.smart.library.util.STLogUtil
import com.smart.library.util.rx.RxBus
import java.lang.ref.WeakReference
import java.util.*

@Suppress("MemberVisibilityCanBePrivate")
open class STActivityLifecycleCallbacks : Application.ActivityLifecycleCallbacks {

    companion object {
        const val TAG = "[activityLifecycle]"
    }

    private val activityList: MutableList<WeakReference<Activity?>?> = LinkedList()
    var activityStartedCount: Int = 0
        private set

    var activityStoppedCount: Int = 0
        private set

    var isApplicationVisible: Boolean = false
        private set(value) {
            if (field != value) {
                STLogUtil.e("applicationLifeCycle", "系统监测到应用程序 正在 从 ${if (field) "前台" else "后台"} 切换到 ${if (value) "前台" else "后台"} ")
                field = value
                RxBus.post(STApplicationVisibleChangedEvent(value))
            }
        }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        activityList.add(WeakReference(activity))
    }

    override fun onActivityDestroyed(activity: Activity) {
        activityList.removeAll { it?.get() == activity }
    }

    @Suppress("MemberVisibilityCanBePrivate")
    fun finishAllActivity() {
        STLogUtil.w(TAG, "finishAllActivity start activityList=${activityList.size}")
        activityList.forEach { it?.get()?.finish() }
        activityList.clear()
        STLogUtil.w(TAG, "finishAllActivity end activityList=${activityList.size}")
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) = Unit

    override fun onActivityPaused(activity: Activity?) = Unit

    override fun onActivityResumed(activity: Activity?) = Unit

    override fun onActivityStarted(activity: Activity) {
        isApplicationVisible = ++activityStartedCount > activityStoppedCount
    }

    override fun onActivityStopped(activity: Activity) {
        isApplicationVisible = activityStartedCount > ++activityStoppedCount
    }
}
