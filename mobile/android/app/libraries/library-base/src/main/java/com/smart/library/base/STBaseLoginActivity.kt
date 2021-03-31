package com.smart.library.base

import android.os.Bundle
import androidx.annotation.Keep
import com.smart.library.R

@Suppress("MemberVisibilityCanBePrivate", "LeakingThis")
@Keep
open class STBaseLoginActivity : STBaseActivity(), STBaseLoginActivityDelegate {

    protected var loginActivityDelegate: STBaseLoginActivityDelegate? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(null)
        loginActivityDelegate = STBaseLoginActivityDelegateImpl()
    }

    override fun onLoginSuccess(dataJson: String) {
        loginActivityDelegate?.onLoginSuccess(dataJson)
    }

    override fun onLoginFailure() {
        loginActivityDelegate?.onLoginFailure()
    }

    override fun getCallback(): ((isLogin: Boolean) -> Unit?)? {
        return loginActivityDelegate?.getCallback()
    }

    override fun onDestroy() {
        loginActivityDelegate?.onDestroy()
        super.onDestroy()
    }

    companion object {
        const val TAG = "[login]"
    }

    init {
        enableActivityFeatureNoTitle(false)
        activityDecorViewBackgroundResource(R.drawable.st_launch)
        enableActivityFullScreenAndExpandLayout(false)
        enableSwipeBack(false)
        enableImmersionStatusBar(true)
        enableImmersionStatusBarWithDarkFont(true)
        enableExitWithDoubleBackPressed(false)
    }
}
