package com.smart.library.base

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDex
import com.smart.library.util.STLogUtil
import com.smart.library.util.STSystemUtil
import com.smart.library.util.network.STNetworkChangedReceiver
import com.smart.library.util.rx.RxBus

open class STBaseApplication : Application() {

    companion object {

        @SuppressLint("StaticFieldLeak")
        @JvmStatic
        lateinit var INSTANCE: STBaseApplication
            private set

        @JvmStatic
        val DEBUG: Boolean by lazy { (STSystemUtil.getAppMetaData("DEBUG") ?: false) as Boolean }

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
                    STLogUtil.e("applicationLifeCycle", "系统监测到应用程序 正在 从 ${if (field) "前台" else "后台"} 切换到 ${if (value) "前台" else "后台"} ")
                    field = value
                    RxBus.post(STApplicationVisibleChangedEvent(value))
                }
            }
    }

    /**
     * https://www.cnblogs.com/muouren/p/11741309.html
     * https://juejin.im/post/5d95f4a4f265da5b8f10714b#heading-10
     * http://androidxref.com/4.4.4_r1/xref/libcore/dalvik/src/main/java/dalvik/system/DexPathList.java
     * http://androidxref.com/4.4.4_r1/xref/dalvik/libdex/DexFile.cpp
     * https://github.com/lanshifu/MultiDexTest
     */
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        val startTime = System.currentTimeMillis()
        MultiDex.install(base)
        println("template MultiDex.install 耗时:${System.currentTimeMillis() - startTime}ms")
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this

        STLogUtil.w("config", "config: debug:$DEBUG")
        STLogUtil.w("config", "config: versionCode:" + STSystemUtil.versionCode)
        STLogUtil.w("config", "config: versionName:" + STSystemUtil.versionName)

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true) //selector vector support

        // Simply add the handler, and that's it! No need to add any code
        // to every activity. Everything is contained in MActivityLifecycleCallbacks
        // with just a few lines of code. Now *that's* nice.
        registerActivityLifecycleCallbacks(object : STActivityLifecycleCallbacks() {
            override fun onActivityStarted(activity: Activity) {
                super.onActivityStarted(activity)
                // https://gist.github.com/pyricau/4df64341cc978a7de414 # Use onActivityStarted(Activity) instead of onActivityCreated(Activity, Bundle)
                // STIMMLeaksUtil.fixFocusedViewLeak(activity)
            }
        })

        STNetworkChangedReceiver.register(this)

    }
}
