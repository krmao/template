package com.xixi.library.android.util

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import com.xixi.library.android.base.FSActivity
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@Suppress("unused")
object FSRouteManager {
    private val KEY_ID_CALLBACK = "id_callback"

    private val callbackMap: MutableMap<String, ((bundle: Bundle?) -> Unit?)?> = ConcurrentHashMap()

    private val interceptors: MutableSet<(activity: Activity?, uri: Uri?, bundle: Bundle?) -> Boolean> = Collections.synchronizedSet(HashSet())

    fun addInterceptor(interceptor: (activity: Activity?, uri: Uri?, bundle: Bundle?) -> Boolean) {
        interceptors.add(interceptor)
    }

    @JvmOverloads
    @Synchronized
    fun goToWeb(activity: Activity?, uri: Uri?, callback: ((bundle: Bundle?) -> Unit?)? = null) {
        if (activity == null || activity.isFinishing || uri == null) {
            FSLogUtil.e("goToActivity failed, activity or activityName is invalid !")
            if (callback != null)
                callback(null)
            return
        }
        //val paramBundle = Bundle()
        //uri?.query?.split("&")?.map { it.split("=") }?.filter { it.size == 2 }?.forEach { paramBundle.putString(it[0], it[1]) }
        //return interceptors.any { it.invoke(activity, uri, paramBundle) }
    }

    @JvmOverloads
    @Synchronized
    fun goToActivity(activity: Activity?, activityName: String?, bundle: Bundle? = null, callback: ((bundle: Bundle?) -> Unit?)? = null) {
        if (activity == null || activity.isFinishing || TextUtils.isEmpty(activityName)) {
            FSLogUtil.e("goToActivity failed, activity or activityName is invalid !")
            if (callback != null)
                callback(null)
            return
        }
        val intent = Intent().setClassName(activity, activityName).putExtras(bundle ?: Bundle())
        if (callback != null) {
            val id = activityName + ":" + System.currentTimeMillis()
            callbackMap.put(id, callback)
            intent.putExtra(KEY_ID_CALLBACK, id)
        }
        FSLogUtil.v("callback size:" + callbackMap.size + " : " + callbackMap.keys)
        activity.startActivity(intent)
    }

    @JvmOverloads
    @Synchronized
    fun goToFragment(activity: Activity?, fragmentName: String?, bundle: Bundle? = null, callback: ((bundle: Bundle?) -> Unit?)? = null) {
        if (activity == null || activity.isFinishing || TextUtils.isEmpty(fragmentName)) {
            FSLogUtil.e("goToFragment failed, activity or fragmentName is invalid !")
            if (callback != null)
                callback(null)
            return
        }
        val _bundle = bundle ?: Bundle()
        if (callback != null) {
            val id = fragmentName + ":" + System.currentTimeMillis()
            _bundle.putString(KEY_ID_CALLBACK, id)
            callbackMap.put(id, callback)
        }
        FSLogUtil.v("callback size:" + callbackMap.size + " : " + callbackMap.keys)
        FSActivity.start(activity, fragmentName, _bundle)
    }

    @Synchronized
    fun getCallback(fragment: Fragment?): ((bundle: Bundle?) -> Unit?)? = callbackMap[fragment?.arguments?.getString(KEY_ID_CALLBACK)]

    @Synchronized
    fun getCallback(activity: Activity?): ((bundle: Bundle?) -> Unit?)? = callbackMap[activity?.intent?.getStringExtra(KEY_ID_CALLBACK)]

    @Synchronized
    fun removeCallback(fragment: Fragment?) {
        val key = fragment?.arguments?.getString(KEY_ID_CALLBACK)
        if (!TextUtils.isEmpty(key))
            callbackMap.remove(key)
        FSLogUtil.v("callback size:" + callbackMap.size + " : " + callbackMap.keys)
    }

    @Synchronized
    fun removeCallback(activity: Activity?) {
        val key = activity?.intent?.getStringExtra(KEY_ID_CALLBACK)
        if (!TextUtils.isEmpty(key))
            callbackMap.remove(key)
        FSLogUtil.v("callback size:" + callbackMap.size + " : " + callbackMap.keys)
    }

    fun testGoToFragment(activity: Activity, fragmentName: String, bundle: Bundle? = null, callback: ((bundle: Bundle?) -> Unit?)? = null) {
        goToFragment(activity, fragmentName, bundle)
        goToFragment(activity, fragmentName, bundle, callback)
        goToFragment(activity, fragmentName, bundle) { _: Bundle? ->

        }
    }

    fun testGoToActivity(activity: Activity, activityName: String, bundle: Bundle? = null, callback: ((bundle: Bundle?) -> Unit?)? = null) {
        goToActivity(activity, activityName, bundle)
        goToActivity(activity, activityName, bundle, callback)
        goToActivity(activity, activityName, bundle) { _: Bundle? ->

        }
    }
}