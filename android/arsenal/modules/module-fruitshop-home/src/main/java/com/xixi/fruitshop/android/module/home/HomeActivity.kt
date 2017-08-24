package com.xixi.fruitshop.android.module.home

import android.os.Bundle
import android.support.v4.app.Fragment
import android.widget.FrameLayout
import com.xixi.library.android.base.FSBaseActivity

class HomeActivity : FSBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableSwipeBack = false
        enableExitWithDoubleBackPressed = true
        super.onCreate(savedInstanceState)

        setContentView(FrameLayout(this))
        val fragment: Fragment = HomeFragment()
        supportFragmentManager.beginTransaction().add(android.R.id.content, fragment, HomeFragment::javaClass.name).commitAllowingStateLoss()
    }
}
