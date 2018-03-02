package com.smart.template.tab

import android.os.Bundle
import android.widget.FrameLayout
import com.smart.template.module.hybird.HybirdApplication
import com.smart.library.base.CXBaseActivity

class HomeTabActivity : CXBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableSwipeBack = false
        enableExitWithDoubleBackPressed = true
        super.onCreate(savedInstanceState)
        setContentView(FrameLayout(this))
        supportFragmentManager.beginTransaction().add(android.R.id.content, HomeTabFragment(), HomeTabFragment::javaClass.name).commitAllowingStateLoss()

        Thread(Runnable { HybirdApplication.init() }).start()
    }
}
