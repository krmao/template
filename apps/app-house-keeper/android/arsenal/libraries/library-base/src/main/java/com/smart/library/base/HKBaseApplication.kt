package com.smart.library.base

import android.app.Application
import android.support.v7.app.AppCompatDelegate
import com.smart.library.util.HKLogUtil
import com.smart.library.util.HKSystemUtil

open class HKBaseApplication : Application() {

    companion object {
        lateinit var INSTANCE: HKBaseApplication
        val DEBUG: Boolean by lazy { HKSystemUtil.getAppMetaData("DEBUG") as Boolean }
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this

        HKLogUtil.w("config", "config: debug:" + DEBUG)
        HKLogUtil.w("config", "config: versionCode:" + HKSystemUtil.versionCode)
        HKLogUtil.w("config", "config: versionName:" + HKSystemUtil.versionName)

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);//selector vector support
        // Simply add the handler, and that's it! No need to add any code
        // to every activity. Everything is contained in MActivityLifecycleCallbacks
        // with just a few lines of code. Now *that's* nice.
        //registerActivityLifecycleCallbacks(HKActivityLifecycleCallbacks())
    }
}