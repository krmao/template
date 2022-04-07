package com.smart.library.base

import androidx.annotation.Keep
import com.smart.library.user.STUserManager

//@Keep
class STBaseLoginActivityDelegateImpl : STBaseLoginActivityDelegate {

    override fun onLoginSuccess(dataJson: String) {
        STUserManager.setUserData(dataJson)
        STUserManager.setUserLogin(true)
    }

    override fun onLoginFailure() {
        STUserManager.setUserData(null)
        STUserManager.setUserLogin(false)
    }


    override fun getCallback(): ((isLogin: Boolean) -> Unit?)? {
        return STUserManager.getLoginCallback()
    }

    override fun onDestroy() {
        val loginCallback = getCallback()
        if (loginCallback != null) {
            STUserManager.removeLoginCallback()
            loginCallback.invoke(STUserManager.isUserLogin())
        }
    }

}
