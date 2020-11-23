package com.smart.library.widget.debug

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.widget.FrameLayout
import com.smart.library.R
import com.smart.library.base.STBaseActivity
import com.smart.library.base.STBaseApplication
import com.smart.library.base.STConfig
import com.smart.library.util.STLogUtil
import com.smart.library.util.STSystemUtil

open class STDebugActivity : STBaseActivity() {

    @Suppress("PrivatePropertyName", "PropertyName")
    protected val TAG: String = STDebugActivity::class.java.name

    override fun onCreate(savedInstanceState: Bundle?) {
        enableSwipeBack = false
        enableImmersionStatusBar = true
        enableImmersionStatusBarWithDarkFont = true
        super.onCreate(savedInstanceState)
        setContentView(FrameLayout(this))
        supportFragmentManager.beginTransaction().add(android.R.id.content, STDebugFragment(), STDebugFragment::class.java.name).commitAllowingStateLoss()
    }

    override fun onStart() {
        STLogUtil.w(TAG, "onStart:$taskId, $this , isTaskRoot=$isTaskRoot")
        super.onStart()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            // 当前 activity 算一个
            if (STBaseApplication.activityStartedCount - STBaseApplication.activityStoppedCount <= 1) {
                // 只有这一个 activity 显示在前台
                if (STBaseApplication.activityStartedCount <= 1) {
                    // 整个应用只有这一个 activity 初始化了
                    STConfig.CLASS_ACTIVITY_MAIN?.let {
                        startActivity(Intent(this, it))
                        overridePendingTransition(R.anim.st_anim_fade_in, R.anim.st_anim_fade_out)
                    }
                } else {
                    // 其它的 activity 都在后台
                    STSystemUtil.bringAppToFront(this)
                }
            } else {
                val result = super.onKeyDown(keyCode, event)
                overridePendingTransition(R.anim.st_anim_fade_in, R.anim.st_anim_fade_out)
                return result
            }
        }
        return super.onKeyDown(keyCode, event)
    }
}
