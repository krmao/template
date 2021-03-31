package com.smart.library.base

import androidx.annotation.Keep

@Keep
interface STBaseLoginActivityDelegate {

    fun onLoginSuccess(dataJson: String)

    fun onLoginFailure()

    fun getCallback(): ((isLogin: Boolean) -> Unit?)?

    fun onDestroy()
}
