package com.codesdancing.flutterexample

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import com.smart.library.base.STActivity
import com.smart.library.util.STJsonUtil
import com.smart.library.util.STLogUtil
import com.smart.library.util.STPreferencesUtil
import com.smart.library.util.STToastUtil
import com.smart.library.util.bus.STBusManager
import com.smart.library.widget.webview.STWebFragment
import org.json.JSONObject

object FinalBridgeCommunication {

    @JvmStatic
    fun handleBridge(activity: Activity?, functionName: String?, params: String?, callbackId: String? = null, callback: ((callbackId: String?, resultJsonString: String?) -> Unit)? = null) {
        STLogUtil.d("handleBridge", "functionName=$functionName, params=$params")
        if (activity == null || activity.isFinishing) {
            val result = JSONObject()
            result.put("result", "context is invalid")
            callback?.invoke(callbackId, result.toString())
            return
        }
        val bridgeParamsJsonObject = STJsonUtil.toJSONObjectOrNull(params)
        val result = JSONObject()

        activity.runOnUiThread {
            when (functionName) {
                "open" -> {
                    val url = bridgeParamsJsonObject?.optString("url")
                    if (url?.startsWith("smart://template/flutter") == true) {
                        if (url.isNotBlank()) {
                            val uri: Uri? = if (TextUtils.isEmpty(url)) null else Uri.parse(url)
                            val pageName: String? = uri?.getQueryParameter("page")
                            val pageParamsJson: String? = uri?.getQueryParameter("params")
                            val paramsMap = STJsonUtil.toMapOrNull(pageParamsJson) ?: hashMapOf()
                            STLogUtil.d("bus call", "pageName=$pageName, paramsMap=$paramsMap")
                            STBusManager.call(activity, "flutter/open", url)
                            result.put("result", true)
                        } else {
                            result.put("result", false)
                        }
                    } else if (url?.startsWith("smart://template/rn") == true) {
                        val uri = Uri.parse(url)
                        val component: String? = uri.getQueryParameter("component")
                        val page: String? = uri.getQueryParameter("page")
                        val rnParams: String? = uri.getQueryParameter("params")
                        if (component?.isNotBlank() == true && page?.isNotBlank() == true) {
                            STBusManager.call(activity, "reactnative/open", page, hashMapOf("params" to rnParams), component)
                            result.put("result", true)
                        } else {
                            result.put("result", false)
                        }
                    } else if (url?.startsWith("smart://template/h5") == true) {
                        val uri = Uri.parse(url)
                        val h5Url: String? = uri.getQueryParameter("url")
                        if (h5Url?.isNotBlank() == true) {
                            STActivity.startActivity(activity, STWebFragment::class.java, Bundle().apply { putString("url", h5Url) })
                            result.put("result", true)
                        } else {
                            result.put("result", false)
                        }
                    } else if (url?.startsWith("http") == true) {
                        STActivity.startActivity(activity, STWebFragment::class.java, Bundle().apply { putString("url", url) })
                        result.put("result", true)
                    } else {
                        result.put("result", false)
                    }
                }
                "close" -> {
                    activity.setResult(Activity.RESULT_OK, Intent().apply { putExtra("result", params) })
                    activity.finish()
                    result.put("result", true)
                }
                "showToast" -> {
                    STToastUtil.show(bridgeParamsJsonObject?.optString("message"))
                    result.put("result", true)
                }
                "put" -> {
                    val key = bridgeParamsJsonObject?.optString("key")
                    val value = bridgeParamsJsonObject?.optString("value")
                    if (key?.isNotBlank() == true && value != null) {
                        STPreferencesUtil.putString(key, value)
                        result.put("result", true)
                    } else {
                        result.put("result", false)
                    }
                }
                "get" -> {
                    val key = bridgeParamsJsonObject?.optString("key")
                    if (key?.isNotBlank() == true) {
                        result.put("result", STPreferencesUtil.getString(key))
                    } else {
                        result.put("result", "")
                    }
                }
                "getUserInfo" -> {
                    val userJsonObject = JSONObject()
                    userJsonObject.put("name", "smart")
                    result.put("result", userJsonObject)
                }
                "getLocationInfo" -> {
                    val userJsonObject = JSONObject()
                    userJsonObject.put("currentLatLng", "121.10,31.22")
                    userJsonObject.put("currentCity", "上海")
                    result.put("result", userJsonObject)
                }
                "getDeviceInfo" -> {
                    val userJsonObject = JSONObject()
                    userJsonObject.put("platform", "android")
                    result.put("result", userJsonObject)
                }
                else -> {
                    result.put("result", "native 暂不支持 $functionName")
                }
            }
            callback?.invoke(callbackId, result.toString())
        }
    }
}