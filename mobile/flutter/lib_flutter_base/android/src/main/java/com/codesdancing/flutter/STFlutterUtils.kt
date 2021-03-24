package com.codesdancing.flutter

import android.content.Context
import com.codesdancing.flutter.boost.STFlutterBoostUtils
import com.codesdancing.flutter.multiple.STFlutterMultipleUtils
import com.smart.library.util.STJsonUtil
import org.json.JSONObject

/**
 * @see {@link "https://github.com/alibaba/flutter_boost/blob/master/INTEGRATION.md"}
 * 比如启动App后首页点击open flutter page按钮实际执行的是PageRouter.openPageByUrl(this, PageRouter.FLUTTER_PAGE_URL,params)，然后走openPageByUrl方法第一个if是Flutter页面的映射,
 * 然后pageName.get(path)这里path是sample://flutterPage，这样pageName.get(path)获取到的是flutterPage，对应的就是main.dart里registerPageBuilders注册的flutterPage对应的FlutterRouteWidget(params: params)，
 * 进入这个页面同时把params传到这个页面
 *
 * @param 'url' 为 Native和Flutter的 page 映射，通过Boost提供的open方法在Flutter打开Native和Flutter页面并传参，或者通过openPageByUrl方法在Native打开Native和Flutter页面并传参。一定要确保Flutter端registerPageBuilders里注册的路由的key和这里能够一一映射，否则会报page != null的红屏错误
 */
@Suppress("MemberVisibilityCanBePrivate", "unused")
object STFlutterUtils {
    /**
     * @param schemaUrl "smart://template/flutter?page=flutter_order&params=jsonString"
     */
    @JvmStatic
    fun openFlutterPageBySchema(context: Context?, schemaUrl: String?) {
        if (STFlutterInitializer.enableMultiple) {
            STFlutterMultipleUtils.openNewFlutterActivityBySchemaUrl(context, schemaUrl)
        } else {
            STFlutterBoostUtils.openFlutterPageBySchema(schemaUrl)
        }
    }

    @JvmStatic
    @JvmOverloads
    fun openNewFlutterActivityByName(context: Context?, pageName: String, boostPageUniqueId: String? = null, argumentsJsonString: String? = null) {
        if (STFlutterInitializer.enableMultiple) {
            STFlutterMultipleUtils.openNewFlutterActivityByName(context, pageName, argumentsJsonString = argumentsJsonString)
        } else {
            STFlutterBoostUtils.openFlutterPageByName(pageName, boostPageUniqueId, (if (argumentsJsonString != null) STJsonUtil.toMap(JSONObject(argumentsJsonString)) else null) as HashMap<String, String>?)
        }
    }

    @JvmStatic
    @JvmOverloads
    fun openNewFlutterHomeActivityByName(context: Context?, pageName: String, boostPageUniqueId: String? = null, argumentsJsonString: String? = null) {
        if (STFlutterInitializer.enableMultiple) {
            STFlutterMultipleUtils.openNewFlutterHomeActivityByName(context, pageName, argumentsJsonString = argumentsJsonString)
        } else {
            STFlutterBoostUtils.openFlutterHomePageByName(pageName, boostPageUniqueId, (if (argumentsJsonString != null) STJsonUtil.toMap(JSONObject(argumentsJsonString)) else null) as HashMap<String, String>?)
        }
    }
}