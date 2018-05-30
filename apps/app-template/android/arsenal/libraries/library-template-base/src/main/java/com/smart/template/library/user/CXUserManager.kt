package com.smart.template.library.user

import android.content.Context
import android.content.Intent
import com.smart.library.base.CXConfig
import com.smart.library.util.CXLogUtil
import com.smart.library.util.CXPreferencesUtil
import com.smart.library.util.cache.CXCacheManager
import com.smart.library.util.rx.RxBus


@Suppress("MemberVisibilityCanPrivate", "MemberVisibilityCanBePrivate", "unused", "PrivatePropertyName")
object CXUserManager {

    private const val KEY_USER = "KEY_USER"
    private val TAG: String = CXUserManager::class.java.name

    var userModel: CXUserModel? = CXPreferencesUtil.getEntity(KEY_USER, CXUserModel::class.java)
        set(value) {
            field = value
            CXPreferencesUtil.putEntity(KEY_USER, value)
            RxBus.post(CXLoginOrLogoutEvent(isLogin()))
        }

    var accessToken: String = ""
        get() = userModel?.accessToken ?: ""

    fun isLogin(): Boolean {
        return userModel != null && userModel!!.id > 0
    }

    fun logout() {
        userModel = null
        CXLogUtil.w(TAG, "已退出登录, isLogin:${isLogin()}")
    }

    fun checkLogin(context: Context?, callback: (isLogin: Boolean) -> Unit?) {
        if (isLogin()) {
            callback.invoke(true)
        } else {
            goToLogin(context, callback)
        }
    }

    fun goToLogin(context: Context?, callback: ((isLogin: Boolean) -> Unit?)? = null) {
        CXConfig.CLASS_ACTIVITY_LOGIN?.let {

            if (callback != null) {
                CXCacheManager.put(it.name, "callback", callback)
            }

            context?.startActivity(Intent(context, it))

            Unit
        }
    }
}
