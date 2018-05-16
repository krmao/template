package com.smart.template.tab

import android.os.Bundle
import android.os.Looper
import android.view.KeyEvent
import android.widget.FrameLayout
import com.smart.library.base.CXBaseActivity
import com.smart.template.module.rn.ReactManager

class HomeTabActivity : CXBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableSwipeBack = false
        enableExitWithDoubleBackPressed = true
        super.onCreate(savedInstanceState)
        setContentView(FrameLayout(this))
        supportFragmentManager.beginTransaction().add(android.R.id.content, HomeTabFragment(), HomeTabFragment::javaClass.name).commitAllowingStateLoss()

        Looper.myQueue().addIdleHandler {
            ReactManager.initialize(this@HomeTabActivity)

            false
        }
    }

    override fun onDestroy() {
        ReactManager.release()
        super.onDestroy()
    }
}
