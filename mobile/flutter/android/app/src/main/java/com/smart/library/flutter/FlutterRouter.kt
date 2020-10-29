package com.smart.library.flutter

import android.app.Activity
import android.content.Context
import com.smart.library.flutter.test.MineActivity

@Suppress("MemberVisibilityCanBePrivate", "unused")
enum class FlutterRouter(val url: String, val goTo: (context: Context?, containerParams: MutableMap<String, Any>?, requestCode: Int, exts: MutableMap<String, Any>?) -> Boolean) {

    URL_MINE("flutter://native/mine", { context: Context?, containerParams: MutableMap<String, Any>?, requestCode: Int, exts: MutableMap<String, Any>? ->
        context?.startActivity(android.content.Intent(context, MineActivity::class.java))
        false
    }),
    URL_ORDER("flutter://flutter/order", { context: Context?, containerParams: MutableMap<String, Any>?, requestCode: Int, exts: MutableMap<String, Any>? ->
        FlutterActivity.startForResult(context as? Activity, URL_ORDER.url, containerParams, requestCode)
        true
    }),
    URL_SETTINGS("flutter://flutter/settings", { context: Context?, containerParams: MutableMap<String, Any>?, requestCode: Int, exts: MutableMap<String, Any>? ->
        FlutterActivity.startForResult(context as? Activity, URL_SETTINGS.url, containerParams, requestCode)
        true
    });

    companion object {
        fun find(url: String?): FlutterRouter? {
            return try {
                values().find { it.url == url }
            } catch (e: Exception) {
                null
            }
        }

        fun openContainer(context: Context?, url: String?, urlParams: MutableMap<String, Any>?, requestCode: Int, exts: MutableMap<String, Any>?) {
            find(url)?.goTo?.invoke(context, urlParams, requestCode, exts)
        }

    }

}