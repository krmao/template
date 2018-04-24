package com.smart.library.widget.debug

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.widget.FrameLayout
import com.smart.library.R
import com.smart.library.base.CXBaseActivity
import com.smart.library.base.CXBaseApplication
import com.smart.library.base.CXConfig
import com.smart.library.util.CXLogUtil
import com.smart.library.util.CXSystemUtil

open class CXDebugActivity : CXBaseActivity() {

    @Suppress("PrivatePropertyName", "PropertyName")
    protected val TAG: String = CXDebugActivity::class.java.name

    override fun onCreate(savedInstanceState: Bundle?) {
        enableSwipeBack = false
        enableImmersionStatusBar = true
        enableImmersionStatusBarWithDarkFont = true
        super.onCreate(savedInstanceState)
        setContentView(FrameLayout(this))
        supportFragmentManager.beginTransaction().add(android.R.id.content, CXDebugFragment(), CXDebugFragment::class.java.name).commitAllowingStateLoss()
    }

    override fun onStart() {
        CXLogUtil.w(TAG, "onStart:$taskId, $this , isTaskRoot=$isTaskRoot")
        super.onStart()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            // 当前 activity 算一个
            if (CXBaseApplication.activityStartedCount - CXBaseApplication.activityStoppedCount <= 1) {
                // 只有这一个 activity 显示在前台
                if (CXBaseApplication.activityStartedCount <= 1) {
                    // 整个应用只有这一个 activity 初始化了
                    CXConfig.CLASS_ACTIVITY_MAIN?.let {
                        startActivity(Intent(this, it))
                        overridePendingTransition(R.anim.cx_fade_in, R.anim.cx_fade_out)
                    }
                } else {
                    // 其它的 activity 都在后台
                    CXSystemUtil.bringAppToFront(this)
                }
            } else {
                val result = super.onKeyDown(keyCode, event)
                overridePendingTransition(R.anim.cx_fade_in, R.anim.cx_fade_out)
                return result
            }
        }
        return super.onKeyDown(keyCode, event)
    }
}
