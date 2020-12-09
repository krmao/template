package com.smart.library.user

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.smart.library.STInitializer
import com.smart.library.util.STJsonUtil
import com.smart.library.util.STLogUtil
import com.smart.library.util.STPreferencesUtil
import com.smart.library.util.cache.STCacheManager
import com.smart.library.util.rx.RxBus
import org.json.JSONObject


@Suppress("MemberVisibilityCanPrivate", "MemberVisibilityCanBePrivate", "unused", "PrivatePropertyName")
object STUserManager {

    private const val TAG: String = "[user]"
    private const val KEY_USER_DATA = "KEY_USER_DATA"
    private const val KEY_USER_LOGIN = "KEY_USER_LOGIN"

    /**
     * 不要存放密码等重要数据
     * call before setUserLogin
     */
    @JvmStatic
    fun setUserData(userDataJsonString: String?) {
        STPreferencesUtil.putString(KEY_USER_DATA, userDataJsonString)
        RxBus.post(STLoginStateChangedEvent(isUserLogin()))
    }

    @JvmStatic
    fun getUserData(): JSONObject? {
        return STJsonUtil.toJSONObjectOrNull(STPreferencesUtil.getString(KEY_USER_DATA))
    }

    /**
     * call after setUserData
     */
    @JvmStatic
    fun setUserLogin(isLogin: Boolean) {
        val oldLoginStatus: Boolean = isUserLogin()
        STPreferencesUtil.putBoolean(KEY_USER_LOGIN, isLogin)
        if (oldLoginStatus != isLogin) RxBus.post(STLoginStateChangedEvent(isUserLogin()))
    }

    @JvmStatic
    fun isUserLogin(): Boolean {
        return STPreferencesUtil.getBoolean(KEY_USER_LOGIN, false) ?: false
    }

    @JvmStatic
    fun logout() {
        setUserData(null)
        setUserLogin(false)
        STLogUtil.w(TAG, "did logout! isLogin:${isUserLogin()}")
    }

    @JvmStatic
    fun login(context: Context?, callback: ((isLogin: Boolean) -> Unit)? = null) {
        if (isUserLogin()) {
            callback?.invoke(true)
        } else {
            if (context == null) {
                STLogUtil.w(TAG, "context is null! isLogin:${isUserLogin()}")
                callback?.invoke(isUserLogin())
                return
            }
            goToLogin(context, callback)
        }
    }

    @JvmStatic
    fun setLoginCallback(callback: ((isLogin: Boolean) -> Unit?)? = null) {
        val loginActivityClass: Class<out Activity> = requireNotNull(STInitializer.configClass?.loginClass)
        if (callback != null) STCacheManager.put(loginActivityClass.name, "login_callback", callback)
    }

    @JvmStatic
    fun removeLoginCallback() {
        val loginActivityClass: Class<out Activity> = requireNotNull(STInitializer.configClass?.loginClass)
        STCacheManager.remove(loginActivityClass.name, "login_callback")
    }

    @JvmStatic
    fun getLoginCallback(): ((isLogin: Boolean) -> Unit?)? {
        val loginActivityClass: Class<out Activity> = requireNotNull(STInitializer.configClass?.loginClass)
        return STCacheManager.get(loginActivityClass.name, "login_callback")
    }

    private fun goToLogin(context: Context, callback: ((isLogin: Boolean) -> Unit?)? = null) {
        setLoginCallback(callback)
        val loginActivityClass: Class<out Activity> = requireNotNull(STInitializer.configClass?.loginClass)
        context.startActivity(Intent(context, loginActivityClass))
    }

    data class LoginStateChangedEvent(val isLogin: Boolean)
}
