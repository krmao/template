package com.smart.library.base

import android.app.Activity
import android.app.Application
import android.support.v7.app.AppCompatDelegate
import com.smart.library.util.HKLogUtil
import com.smart.library.util.HKSystemUtil
import com.smart.library.util.compat.HKIMMLeaksUtil
import com.smart.library.util.rx.RxBus

open class HKBaseApplication : Application() {

    companion object {

        @JvmStatic
        lateinit var INSTANCE: HKBaseApplication
            private set

        @JvmStatic
        val DEBUG: Boolean by lazy { (HKSystemUtil.getAppMetaData("DEBUG") ?: false) as Boolean }

        @JvmStatic
        var activityStartedCount: Int = 0
            internal set
        @JvmStatic
        var activityStoppedCount: Int = 0
            internal set
        @JvmStatic
        var isApplicationVisible: Boolean = false
            internal set(value) {
                if (field != value) {
                    HKLogUtil.e("applicationLifeCycle", "系统监测到应用程序 正在 从 ${if (field) "前台" else "后台"} 切换到 ${if (value) "前台" else "后台"} ")
                    field = value
                    RxBus.post(HKApplicationVisibleChangedEvent(value))
                }
            }
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this

        HKLogUtil.w("config", "config: debug:" + DEBUG)
        HKLogUtil.w("config", "config: versionCode:" + HKSystemUtil.versionCode)
        HKLogUtil.w("config", "config: versionName:" + HKSystemUtil.versionName)

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true) //selector vector support

        // Simply add the handler, and that's it! No need to add any code
        // to every activity. Everything is contained in MActivityLifecycleCallbacks
        // with just a few lines of code. Now *that's* nice.
        registerActivityLifecycleCallbacks(object : HKActivityLifecycleCallbacks() {
            override fun onActivityStarted(activity: Activity) {
                super.onActivityStarted(activity)
                // https://gist.github.com/pyricau/4df64341cc978a7de414 # Use onActivityStarted(Activity) instead of onActivityCreated(Activity, Bundle)
                HKIMMLeaksUtil.fixFocusedViewLeak(activity)
            }
        })
    }
}
