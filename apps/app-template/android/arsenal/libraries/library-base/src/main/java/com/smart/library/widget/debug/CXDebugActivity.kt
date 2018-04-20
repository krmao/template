package com.smart.library.widget.debug

import android.os.Bundle
import android.widget.FrameLayout
import com.smart.library.base.CXBaseActivity
import com.smart.library.util.CXLogUtil

open class CXDebugActivity : CXBaseActivity() {

    @Suppress("PrivatePropertyName", "PropertyName")
    protected val TAG: String = CXDebugActivity::class.java.name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(FrameLayout(this))
        supportFragmentManager.beginTransaction().add(android.R.id.content, CXDebugFragment(), CXDebugFragment::class.java.name).commitAllowingStateLoss()
    }

    override fun onStart() {
        CXLogUtil.w(TAG, "onStart:$taskId, $this")
        super.onStart()
    }

    override fun onRestart() {
        super.onRestart()
        CXLogUtil.w(TAG, "onRestart:$taskId, $this")
    }

    override fun onResume() {
        super.onResume()
        CXLogUtil.w(TAG, "onResume:$taskId, $this")
    }

    override fun onPause() {
        super.onPause()
        CXLogUtil.w(TAG, "onPause:$taskId, $this")
    }

    override fun onStop() {
        super.onStop()
        CXLogUtil.w(TAG, "onStop:$taskId, $this")
    }
}
