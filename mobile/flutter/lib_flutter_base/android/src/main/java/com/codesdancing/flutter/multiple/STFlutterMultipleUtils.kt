package com.codesdancing.flutter.multiple

import android.content.Context
import android.net.Uri
import android.text.TextUtils
import com.smart.library.util.STLogUtil

@Suppress("MemberVisibilityCanBePrivate", "unused")
internal object STFlutterMultipleUtils {

    /**
     * @param schemaUrl "smart://template/flutter?page=flutter_order&params=jsonString"
     */
    @JvmStatic
    fun openNewFlutterActivityBySchemaUrl(context: Context?, schemaUrl: String?) {
        STLogUtil.d("openFlutterPageBySchema schemaUrl=$schemaUrl")

        val uri: Uri? = if (TextUtils.isEmpty(schemaUrl)) null else Uri.parse(schemaUrl)
        val lastPath = schemaUrl?.substringBefore("?")?.substringAfterLast("/")
        if (schemaUrl?.startsWith("smart://template/flutter") == true && lastPath == "flutter") {
            val pageName: String? = uri?.getQueryParameter("page")
            // val pageParamsJson: String? = uri?.getQueryParameter("params")
            // val uniqueId: String? = uri?.getQueryParameter("uniqueId")
            if (pageName == null) {
                STLogUtil.e("openFlutterPageBySchema schemaUrl=$schemaUrl pageName is null!!!")
            } else {
                openNewFlutterActivityByName(context, pageName)
            }
        }
    }

    @JvmStatic
    fun openNewFlutterActivityByName(context: Context?, initialRoute: String = "/", enableMultiEnginesWithSingleRoute: Boolean = true) {
        STLogUtil.d("openNewFlutterActivityByName", "initialRoute=$initialRoute, enableMultiEnginesWithSingleRoute=$enableMultiEnginesWithSingleRoute")
        val dartEntrypointFunctionName = if (!enableMultiEnginesWithSingleRoute || initialRoute == "/") "main" else "main$initialRoute"
        val finalInitialRoute = if (!enableMultiEnginesWithSingleRoute) initialRoute else "/"
        STFlutterMultipleActivity.startActivity(context, dartEntrypointFunctionName, finalInitialRoute)
    }

    @JvmStatic
    fun openNewFlutterHomeActivityByName(context: Context?, initialRoute: String = "/", enableMultiEnginesWithSingleRoute: Boolean = true) {
        STLogUtil.d("openNewFlutterHomeActivityByName", "initialRoute=$initialRoute, enableMultiEnginesWithSingleRoute=$enableMultiEnginesWithSingleRoute")
        val dartEntrypointFunctionName = if (!enableMultiEnginesWithSingleRoute || initialRoute == "/") "main" else "main$initialRoute"
        val finalInitialRoute = if (!enableMultiEnginesWithSingleRoute) initialRoute else "/"
        STFlutterMultipleHomeActivity.startActivity(context, dartEntrypointFunctionName, finalInitialRoute)
    }
}