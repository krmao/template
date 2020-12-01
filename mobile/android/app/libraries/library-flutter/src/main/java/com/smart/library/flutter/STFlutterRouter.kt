package com.smart.library.flutter

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.text.TextUtils
import com.idlefish.flutterboost.containers.BoostFlutterActivity
import com.smart.library.flutter.test.MineActivity
import com.smart.library.util.STJsonUtil

/**
 * @see {@link "https://github.com/alibaba/flutter_boost/blob/master/INTEGRATION.md"}
 * 比如启动App后首页点击open flutter page按钮实际执行的是PageRouter.openPageByUrl(this, PageRouter.FLUTTER_PAGE_URL,params)，然后走openPageByUrl方法第一个if是Flutter页面的映射,
 * 然后pageName.get(path)这里path是sample://flutterPage，这样pageName.get(path)获取到的是flutterPage，对应的就是main.dart里registerPageBuilders注册的flutterPage对应的FlutterRouteWidget(params: params)，
 * 进入这个页面同时把params传到这个页面
 *
 * @param url 为 Native和Flutter的 page 映射，通过Boost提供的open方法在Flutter打开Native和Flutter页面并传参，或者通过openPageByUrl方法在Native打开Native和Flutter页面并传参。一定要确保Flutter端registerPageBuilders里注册的路由的key和这里能够一一映射，否则会报page != null的红屏错误
 */
@Suppress("MemberVisibilityCanBePrivate", "unused")
enum class STFlutterRouter(val url: String, val goTo: (context: Context?, containerParams: MutableMap<String, Any>?, requestCode: Int, exts: MutableMap<String, Any>?) -> Boolean) {

    URL_MINE("native_mine", { context: Context?, _: MutableMap<String, Any>?, _: Int, _: MutableMap<String, Any>? ->
        context?.startActivity(android.content.Intent(context, MineActivity::class.java))
        true
    }),
    URL_ORDER("flutter_order", { context: Context?, containerParams: MutableMap<String, Any>?, requestCode: Int, _: MutableMap<String, Any>? ->
        STFlutterBoostFragment.startForResult(context as? Activity, URL_ORDER.url, containerParams, requestCode)
        true
    }),
    URL_SETTINGS("flutter_settings", { context: Context?, containerParams: MutableMap<String, Any>?, requestCode: Int, _: MutableMap<String, Any>? ->
        // 这直接用的Boost提供的Activity作为Flutter的容器，也可以继承BoostFlutterActivity后做一些自定义的行为
        if (context != null) {
            val intent = STFlutterBoostActivity
                .withNewEngine()
                .url(URL_SETTINGS.url)
                .params(containerParams ?: HashMap<String, Any?>())
                .backgroundMode(BoostFlutterActivity.BackgroundMode.opaque)
                .build(context)
            if (context is Activity) {
                context.startActivityForResult(intent, requestCode)
            } else {
                context.startActivity(intent)
            }
        }
        true
    });

    companion object {
        private fun find(url: String?): STFlutterRouter? {
            return try {
                values().find { it.url == url }
            } catch (e: Exception) {
                null
            }
        }

        /**
         * @param schemaUrl "smart://template/flutter?page=flutter_order&params=jsonString"
         */
        @JvmStatic
        @JvmOverloads
        fun openContainerBySchema(context: Context?, schemaUrl: String?, requestCode: Int = 0, exts: MutableMap<String, Any>? = null) {
            com.smart.library.util.STLogUtil.d("schemaUrl=$schemaUrl, requestCode$requestCode, exts=$exts")

            val uri: Uri? = if (TextUtils.isEmpty(schemaUrl)) null else Uri.parse(schemaUrl)
            val lastPath = schemaUrl?.substringBefore("?")?.substringAfterLast("/")
            if (schemaUrl?.startsWith("smart://template/flutter") == true && lastPath == "flutter") {
                val pageName: String? = uri?.getQueryParameter("page")
                val pageParamsJson: String? = uri?.getQueryParameter("params")
                find(pageName)?.goTo?.invoke(context, STJsonUtil.toMapOrNull(pageParamsJson), requestCode, exts)
            }
        }

        @JvmStatic
        @JvmOverloads
        fun openContainer(context: Context?, pageName: String?, pageParams: MutableMap<String, Any>? = null, requestCode: Int = 0, exts: MutableMap<String, Any>? = null) {
            com.smart.library.util.STLogUtil.d("pageName=$pageName, params$pageName, requestCode$requestCode, exts=$exts")

            find(pageName)?.goTo?.invoke(context, pageParams, requestCode, exts)
        }
    }
}