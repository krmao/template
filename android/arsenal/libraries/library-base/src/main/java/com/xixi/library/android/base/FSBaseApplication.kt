package com.xixi.library.android.base

import android.app.Application
import android.support.v7.app.AppCompatDelegate
import com.xixi.library.android.util.FSLogUtil
import com.xixi.library.android.util.FSSystemUtil

open class FSBaseApplication : Application() {

    companion object {
        lateinit var INSTANCE: FSBaseApplication
        val DEBUG: Boolean by lazy { FSSystemUtil.getAppMetaData("DEBUG") as Boolean }
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this

        FSLogUtil.w("config", "config: debug:" + DEBUG)
        FSLogUtil.w("config", "config: versionCode:" + FSSystemUtil.versionCode)
        FSLogUtil.w("config", "config: versionName:" + FSSystemUtil.versionName)

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);//selector vector support
        // Simply add the handler, and that's it! No need to add any code
        // to every activity. Everything is contained in MActivityLifecycleCallbacks
        // with just a few lines of code. Now *that's* nice.
        //registerActivityLifecycleCallbacks(FSActivityLifecycleCallbacks())
    }
}