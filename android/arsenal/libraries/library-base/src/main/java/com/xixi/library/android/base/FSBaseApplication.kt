package com.xixi.library.android.base

import android.app.Application
import android.support.v7.app.AppCompatDelegate
import com.xixi.library.android.util.CXLogUtil
import com.xixi.library.android.util.CXSystemUtil

open class CXBaseApplication : Application() {

    companion object {
        lateinit var INSTANCE: CXBaseApplication
        val DEBUG: Boolean by lazy { CXSystemUtil.getAppMetaData("DEBUG") as Boolean }
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this

        CXLogUtil.w("config", "config: debug:" + DEBUG)
        CXLogUtil.w("config", "config: versionCode:" + CXSystemUtil.versionCode)
        CXLogUtil.w("config", "config: versionName:" + CXSystemUtil.versionName)

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);//selector vector support
        // Simply add the handler, and that's it! No need to add any code
        // to every activity. Everything is contained in MActivityLifecycleCallbacks
        // with just a few lines of code. Now *that's* nice.
        registerActivityLifecycleCallbacks(CXActivityLifecycleCallbacks())
    }
}