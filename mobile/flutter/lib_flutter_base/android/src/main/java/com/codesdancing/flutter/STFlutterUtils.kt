package com.codesdancing.flutter

import android.net.Uri
import android.text.TextUtils
import com.idlefish.flutterboost.FlutterBoost
import com.idlefish.flutterboost.containers.FlutterBoostActivity.CachedEngineIntentBuilder
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
@Suppress("MemberVisibilityCanBePrivate", "unused")
object STFlutterUtils {
    /**
     * bug 如果 android backgroundMode(BoostFlutterActivity.BackgroundMode.transparent) 则 SafeArea 不起作用
     * https://github.com/flutter/flutter/issues/46060
     */
    private fun openNewFlutterActivity(pageName: String, uniqueId: String, pageParams: HashMap<String, String>) {
        val intent = CachedEngineIntentBuilder(STFlutterActivity::class.java, FlutterBoost.ENGINE_ID)
                .backgroundMode(io.flutter.embedding.android.FlutterActivityLaunchConfigs.BackgroundMode.opaque)
                .destroyEngineWithActivity(false)
                .uniqueId(uniqueId)
                .url(pageName)
                .urlParams(pageParams)
                .build(FlutterBoost.instance().currentActivity())
        FlutterBoost.instance().currentActivity().startActivity(intent)
    }

    private fun openNewFlutterHomeActivity(pageName: String, uniqueId: String, pageParams: HashMap<String, String>) {
        val intent = CachedEngineIntentBuilder(STFlutterHomeActivity::class.java, FlutterBoost.ENGINE_ID)
                .backgroundMode(io.flutter.embedding.android.FlutterActivityLaunchConfigs.BackgroundMode.opaque)
                .destroyEngineWithActivity(false)
                .uniqueId(uniqueId)
                .url(pageName)
                .urlParams(pageParams)
                .build(FlutterBoost.instance().currentActivity())
        FlutterBoost.instance().currentActivity().startActivity(intent)
    }

    /**
     * @param schemaUrl "smart://template/flutter?page=flutter_order&params=jsonString"
     */
    @JvmStatic
    fun openFlutterPageBySchema(schemaUrl: String?) {
        STLogUtil.d("openFlutterPageBySchema schemaUrl=$schemaUrl")

        val uri: Uri? = if (TextUtils.isEmpty(schemaUrl)) null else Uri.parse(schemaUrl)
        val lastPath = schemaUrl?.substringBefore("?")?.substringAfterLast("/")
        if (schemaUrl?.startsWith("smart://template/flutter") == true && lastPath == "flutter") {
            val pageName: String? = uri?.getQueryParameter("page")
            val pageParamsJson: String? = uri?.getQueryParameter("params")
            val uniqueId: String? = uri?.getQueryParameter("uniqueId")
            @Suppress("UNCHECKED_CAST")
            openFlutterPageByName(pageName, uniqueId, STJsonUtil.toMapOrNull(pageParamsJson) as? HashMap<String, String> ?: hashMapOf())
        }
    }

    @JvmStatic
    @JvmOverloads
    fun openFlutterPageByName(pageName: String?, uniqueId: String?, pageParams: HashMap<String, String>? = null) {
        STLogUtil.d("openFlutterPageByName pageName=$pageName, params$pageName")
        openNewFlutterActivity(pageName ?: "", uniqueId ?: "", pageParams ?: hashMapOf())
    }

    @JvmStatic
    @JvmOverloads
    fun openFlutterHomePageByName(pageName: String?, uniqueId: String?, pageParams: HashMap<String, String>? = null) {
        STLogUtil.d("openFlutterHomePageByName pageName=$pageName, params$pageName")
        openNewFlutterHomeActivity(pageName ?: "", uniqueId ?: "", pageParams ?: hashMapOf())
    }
}