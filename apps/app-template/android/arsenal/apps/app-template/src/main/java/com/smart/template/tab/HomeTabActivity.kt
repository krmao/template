package com.smart.template.tab

import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import com.smart.library.base.CXBaseActivity

class HomeTabActivity : CXBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableSwipeBack = false
        enableExitWithDoubleBackPressed = true
        super.onCreate(savedInstanceState)
        setContentView(FrameLayout(this))
        supportFragmentManager.beginTransaction().add(android.R.id.content, HomeTabFragment(), HomeTabFragment::javaClass.name).commitAllowingStateLoss()
    }

    override fun onStart() {
        Log.e("HomeTabActivity", "onStart:$taskId:$this")
        super.onStart()
    }

    override fun onRestart() {
        super.onRestart()
        Log.e("HomeTabActivity", "onRestart:$taskId:$this")
    }

    override fun onResume() {
        super.onResume()
        Log.e("HomeTabActivity", "onResume:$taskId:$this")
    }

    override fun onPause() {
        super.onPause()
        Log.e("HomeTabActivity", "onPause:$taskId:$this")
    }

    override fun onStop() {
        super.onStop()
        Log.e("HomeTabActivity", "onStop:$taskId:$this")
    }

    override fun onDestroy() {
        Log.e("HomeTabActivity", "onDestroy:$taskId:$this")
        super.onDestroy()
    }
}
