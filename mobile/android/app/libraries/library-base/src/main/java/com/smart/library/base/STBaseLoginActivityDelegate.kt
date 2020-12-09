package com.smart.library.base

interface STBaseLoginActivityDelegate {

    fun onLoginSuccess(dataJson: String)

    fun onLoginFailure()

    fun getCallback(): ((isLogin: Boolean) -> Unit?)?

    fun onDestroy()
}
