package com.smart.template.home.tab

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.FrameLayout
import com.smart.library.base.STBaseActivity
import com.smart.library.util.STLogUtil
import com.smart.library.util.bus.STBusManager
import com.smart.library.util.rx.RxBus
import java.lang.ref.WeakReference


class HomeTabActivity : STBaseActivity() {

    var sRef: WeakReference<Activity>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        STLogUtil.d("home", "activity: onCreate")
        enableSwipeBack = false
        enableExitWithDoubleBackPressed = true
        super.onCreate(savedInstanceState)
        sRef = WeakReference(this)
        STBusManager.homeActivity = sRef
        setContentView(FrameLayout(this))
        supportFragmentManager.beginTransaction().add(android.R.id.content, HomeTabFragment(), HomeTabFragment::javaClass.name).commitAllowingStateLoss()
    }

    override fun onRestart() {
        super.onRestart()
        STLogUtil.d("home", "activity: onRestart")
    }

    override fun onStart() {
        super.onStart()
        STLogUtil.d("home", "activity: onStart")
    }

    override fun onResume() {
        super.onResume()
        STLogUtil.d("home", "activity: onResume")
    }

    override fun onPause() {
        super.onPause()
        STLogUtil.d("home", "activity: onPause")
    }

    override fun onDestroy() {
        super.onDestroy()
        STLogUtil.d("home", "activity: onDestroy")

        sRef?.clear()
        sRef = null
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        STLogUtil.d("home", "activity: onNewIntent")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("name", "da mao ge ge")
        STLogUtil.d("home", "activity: onSaveInstanceState")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        STLogUtil.d("home", "activity: onRestoreInstanceState ${savedInstanceState.getString("name")}")
    }

    /**
     * minSdkVersion = 16
     * targetSdkVersion = 25
     * compileSdkVersion = 28
     * buildToolsVersion = "28.0.3"
     * supportLibraryVersion = "28.0.0"
     *
     * activity 配置 android:configChanges="orientation"
     *
     * 华为 p20 手机 android 版本为 9 执行的生命周期顺序为
     * onConfigurationChanged 其它任何生命周期都不会调用
     *
     * 1+ 5T/小米4 6.0.1 手机 android 版本为 9 执行的生命周期顺序为(与华为系统同样的代码)
     * onPause -> onSaveInstanceState -> onDestroy -> onCreate -> onStart -> onRestoreInstanceState -> onResume
     *
     * ------------------------------------------------
     * activity 配置 android:configChanges="orientation|screenSize"
     * 则 华为 p20/1+ 5T/小米4 6.0.1 手机 执行的生命周期顺序为
     * onConfigurationChanged 其它任何生命周期都不会调用
     *
     * keyboardHidden 是否配置并没有影响实验结果
     */
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        STLogUtil.d("home", "activity: onConfigurationChanged")
        RxBus.post(ConfigurationEvent(newConfig))
    }

    @Suppress("unused")
    class ConfigurationEvent(val newConfig: Configuration?)
}
