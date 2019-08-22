package com.smart.template.home.tab

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.FrameLayout
import com.smart.library.base.STBaseActivity
import com.smart.library.util.STLogUtil
import com.smart.library.util.rx.RxBus

class HomeTabActivity : STBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        STLogUtil.d("home", "activity: onCreate")
        enableSwipeBack = false
        enableExitWithDoubleBackPressed = true
        super.onCreate(savedInstanceState)
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
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        STLogUtil.d("home", "activity: onNewIntent")
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putString("name", "da mao ge ge")
        STLogUtil.d("home", "activity: onSaveInstanceState")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        STLogUtil.d("home", "activity: onRestoreInstanceState ${savedInstanceState?.getString("name")}")
    }

    /**
     * 如果 activity 配置了 android:configChanges="orientation" (注意这里只有 orientation, 网上都是骗人的)
     * 则生命周期只会走 onConfigurationChanged, 其它任何生命周期都不会调用
     *
     * 如果没有配置, 则生命周期会依次执行
     * onPause -> onSaveInstanceState -> onDestroy -> onCreate -> onStart -> onRestoreInstanceState -> onResume
     */
    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        STLogUtil.d("home", "activity: onConfigurationChanged")
        RxBus.post(ConfigurationEvent(newConfig))
    }

    class ConfigurationEvent(val newConfig: Configuration?)
}
