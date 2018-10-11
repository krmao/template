package com.smart.library.base

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import android.support.v7.app.AppCompatDelegate
import com.smart.library.util.CXLogUtil
import com.smart.library.util.CXSystemUtil
import com.smart.library.util.compat.CXIMMLeaksUtil
import com.smart.library.util.network.CXNetworkChangedReceiver
import com.smart.library.util.rx.RxBus

open class CXBaseApplication : Application() {

    companion object {

        @SuppressLint("StaticFieldLeak")
        @JvmStatic
        lateinit var INSTANCE: CXBaseApplication
            private set

        @JvmStatic
        val DEBUG: Boolean by lazy { (CXSystemUtil.getAppMetaData("DEBUG") ?: false) as Boolean }

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
                    CXLogUtil.e("applicationLifeCycle", "系统监测到应用程序 正在 从 ${if (field) "前台" else "后台"} 切换到 ${if (value) "前台" else "后台"} ")
                    field = value
                    RxBus.post(CXApplicationVisibleChangedEvent(value))
                }
            }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(base)
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this

        CXLogUtil.w("config", "config: debug:" + DEBUG)
        CXLogUtil.w("config", "config: versionCode:" + CXSystemUtil.versionCode)
        CXLogUtil.w("config", "config: versionName:" + CXSystemUtil.versionName)

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true) //selector vector support

        // Simply add the handler, and that's it! No need to add any code
        // to every activity. Everything is contained in MActivityLifecycleCallbacks
        // with just a few lines of code. Now *that's* nice.
        registerActivityLifecycleCallbacks(object : CXActivityLifecycleCallbacks() {
            override fun onActivityStarted(activity: Activity) {
                super.onActivityStarted(activity)
                // https://gist.github.com/pyricau/4df64341cc978a7de414 # Use onActivityStarted(Activity) instead of onActivityCreated(Activity, Bundle)
                CXIMMLeaksUtil.fixFocusedViewLeak(activity)
            }
        })

        CXNetworkChangedReceiver.register(this)

    }
}
