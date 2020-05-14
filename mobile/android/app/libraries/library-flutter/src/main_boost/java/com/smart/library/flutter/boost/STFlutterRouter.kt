package com.smart.library.flutter.boost

import android.app.Activity
import android.content.Context
import com.smart.library.flutter.boost.test.MineActivity

@Suppress("MemberVisibilityCanBePrivate", "unused")
enum class STFlutterRouter(val url: String, val goTo: (context: Context?, containerParams: MutableMap<String, Any>?, requestCode: Int, exts: MutableMap<String, Any>?) -> Boolean) {

    URL_MINE("flutter://native/mine", { context: Context?, containerParams: MutableMap<String, Any>?, requestCode: Int, exts: MutableMap<String, Any>? ->
        context?.startActivity(android.content.Intent(context, MineActivity::class.java))
        false
    }),
    URL_ORDER("flutter://flutter/order", { context: Context?, containerParams: MutableMap<String, Any>?, requestCode: Int, exts: MutableMap<String, Any>? ->
        STFlutterFragment.startForResult(context as? Activity, URL_ORDER.url, containerParams, requestCode)
        true
    }),
    URL_SETTINGS("flutter://flutter/settings", { context: Context?, containerParams: MutableMap<String, Any>?, requestCode: Int, exts: MutableMap<String, Any>? ->
        STFlutterFragment.startForResult(context as? Activity, URL_SETTINGS.url, containerParams, requestCode)
        true
    });

    companion object {
        fun find(url: String?): STFlutterRouter? {
            return try {
                values().find { it.url == url }
            } catch (e: Exception) {
                null
            }
        }

        @JvmStatic
        @JvmOverloads
        fun openContainer(context: Context?, url: String?, urlParams: MutableMap<String, Any>? = null, requestCode: Int = 0, exts: MutableMap<String, Any>? = null) {
            com.smart.library.util.STLogUtil.d("---------------->")
            com.smart.library.util.STLogUtil.d("url" + "flutter://native/mine")
            com.smart.library.util.STLogUtil.d("params$url")
            com.smart.library.util.STLogUtil.d("requestCode$requestCode")
            com.smart.library.util.STLogUtil.d("<----------------")
            find(url)?.goTo?.invoke(context, urlParams, requestCode, exts)
        }

    }

}