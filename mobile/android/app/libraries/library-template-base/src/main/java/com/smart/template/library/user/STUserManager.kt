package com.smart.template.library.user

import android.content.Context
import android.content.Intent
import com.smart.library.STInitializer
import com.smart.library.util.STLogUtil
import com.smart.library.util.STPreferencesUtil
import com.smart.library.util.cache.STCacheManager
import com.smart.library.util.rx.RxBus


@Suppress("MemberVisibilityCanPrivate", "MemberVisibilityCanBePrivate", "unused", "PrivatePropertyName")
object STUserManager {

    private const val KEY_USER = "KEY_USER"
    private val TAG: String = STUserManager::class.java.name

    var userModel: STUserModel? = STPreferencesUtil.getEntity(KEY_USER, STUserModel::class.java)
        set(value) {
            field = value
            STPreferencesUtil.putEntity(KEY_USER, value)
            RxBus.post(STLoginOrLogoutEvent(isLogin()))
        }

    val accessToken: String
        get() = userModel?.accessToken ?: ""

    fun isLogin(): Boolean {
        return userModel != null && userModel!!.id > 0
    }

    fun logout() {
        userModel = null
        STLogUtil.w(TAG, "已退出登录, isLogin:${isLogin()}")
    }

    fun checkLogin(context: Context?, callback: (isLogin: Boolean) -> Unit?) {
        if (isLogin()) {
            callback.invoke(true)
        } else {
            goToLogin(context, callback)
        }
    }

    fun goToLogin(context: Context?, callback: ((isLogin: Boolean) -> Unit?)? = null) {
        STInitializer.loginClass()?.let {

            if (callback != null) {
                STCacheManager.put(it.name, "callback", callback)
            }

            context?.startActivity(Intent(context, it))

            Unit
        }
    }
}
