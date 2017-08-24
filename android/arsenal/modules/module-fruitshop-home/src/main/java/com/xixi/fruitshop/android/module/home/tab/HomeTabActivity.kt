package com.xixi.fruitshop.android.module.home.tab

import android.os.Bundle
import android.support.v4.app.Fragment
import android.widget.FrameLayout
import com.jude.swipbackhelper.SwipeBackHelper
import com.xixi.fruitshop.android.module.home.HomeFragment
import com.xixi.library.android.base.FSBaseActivity

class HomeTabActivity : FSBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableSwipeBack = false
        enableExitWithDoubleBackPressed = true
        super.onCreate(savedInstanceState)
        setContentView(FrameLayout(this))
        supportFragmentManager.beginTransaction().add(android.R.id.content, HomeTabFragment(), HomeTabFragment::javaClass.name).commitAllowingStateLoss()
    }
}
