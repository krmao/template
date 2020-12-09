package com.smart.library.base

import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.smart.library.R
import com.smart.library.util.STSystemUtil

@Suppress("MemberVisibilityCanBePrivate")
open class STBaseLoginActivity : AppCompatActivity(), STBaseLoginActivityDelegate {

    protected var loginActivityDelegate: STBaseLoginActivityDelegate? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        // 代码设置可以看到状态栏动画, theme.xml 中设置全屏比较突兀
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.decorView.setBackgroundResource(R.drawable.st_launch)
        super.onCreate(null)
        STSystemUtil.setActivityFullScreenAndExpandLayout(this)
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

}
