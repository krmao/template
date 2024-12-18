package com.smart.library.flutter

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.text.TextUtils
import com.idlefish.flutterboost.containers.BoostFlutterActivity
import com.smart.library.util.STJsonUtil
import com.smart.library.util.STLogUtil

/**
 * @see {@link "https://github.com/alibaba/flutter_boost/blob/master/INTEGRATION.md"}
 * 比如启动App后首页点击open flutter page按钮实际执行的是PageRouter.openPageByUrl(this, PageRouter.FLUTTER_PAGE_URL,params)，然后走openPageByUrl方法第一个if是Flutter页面的映射,
 * 然后pageName.get(path)这里path是sample://flutterPage，这样pageName.get(path)获取到的是flutterPage，对应的就是main.dart里registerPageBuilders注册的flutterPage对应的FlutterRouteWidget(params: params)，
 * 进入这个页面同时把params传到这个页面
 *
 * @param url 为 Native和Flutter的 page 映射，通过Boost提供的open方法在Flutter打开Native和Flutter页面并传参，或者通过openPageByUrl方法在Native打开Native和Flutter页面并传参。一定要确保Flutter端registerPageBuilders里注册的路由的key和这里能够一一映射，否则会报page != null的红屏错误
 */
@Suppress("MemberVisibilityCanBePrivate", "unused", "UNUSED_PARAMETER")
object STFlutterRouter {
    /**
     * bug 如果 android backgroundMode(BoostFlutterActivity.BackgroundMode.transparent) 则 SafeArea 不起作用
     * https://github.com/flutter/flutter/issues/46060
     */
    private fun openNewFlutterActivity(context: Context, pageName: String, pageParams: MutableMap<String, Any>, requestCode: Int, exts: MutableMap<String, Any>? = null) {
        val intent = STFlutterBoostActivity.withNewEngine().url(pageName).params(pageParams).backgroundMode(BoostFlutterActivity.BackgroundMode.opaque).build(context)
        if (context is Activity) context.startActivityForResult(intent, requestCode) else context.startActivity(intent)
    }

    private fun openHomeFlutterActivity(context: Context, pageName: String, pageParams: MutableMap<String, Any>, requestCode: Int, exts: MutableMap<String, Any>? = null) {
        val intent = STFlutterBoostHomeActivity.withNewEngine().url(pageName).params(pageParams).backgroundMode(BoostFlutterActivity.BackgroundMode.opaque).build(context)
        if (context is Activity) context.startActivityForResult(intent, requestCode) else context.startActivity(intent)
    }

    /**
     * @param schemaUrl "smart://template/flutter?page=flutter_order&params=jsonString"
     */
    @JvmStatic
    @JvmOverloads
    fun openBySchema(context: Context?, schemaUrl: String?, requestCode: Int = 0, exts: MutableMap<String, Any>? = null) {
        STLogUtil.d("openBySchema schemaUrl=$schemaUrl, requestCode$requestCode, exts=$exts")

        val uri: Uri? = if (TextUtils.isEmpty(schemaUrl)) null else Uri.parse(schemaUrl)
        val lastPath = schemaUrl?.substringBefore("?")?.substringAfterLast("/")
        if (context != null && schemaUrl?.startsWith("smart://template/flutter") == true && lastPath == "flutter") {
            val pageName: String? = uri?.getQueryParameter("page")
            val pageParamsJson: String? = uri?.getQueryParameter("params")
            openByName(context, pageName, STJsonUtil.toMapOrNull(pageParamsJson) ?: hashMapOf(), requestCode, exts)
        }
    }

    @JvmStatic
    @JvmOverloads
    fun openByName(context: Context?, pageName: String?, pageParams: MutableMap<String, Any>? = null, requestCode: Int = 0, exts: MutableMap<String, Any>? = null) {
        STLogUtil.d("openByName pageName=$pageName, params$pageName, requestCode$requestCode, exts=$exts")
        if (context != null) {
            openNewFlutterActivity(context, pageName ?: "", pageParams ?: hashMapOf(), requestCode, exts)
        }
    }

    @JvmStatic
    @JvmOverloads
    fun openHomeByName(context: Context?, pageName: String?, pageParams: MutableMap<String, Any>? = null, requestCode: Int = 0, exts: MutableMap<String, Any>? = null) {
        STLogUtil.d("openHomeByName pageName=$pageName, params$pageName, requestCode$requestCode, exts=$exts")
        if (context != null) {
            openHomeFlutterActivity(context, pageName ?: "", pageParams ?: hashMapOf(), requestCode, exts)
        }
    }
}