package com.smart.library.util.retrofit.exception


import android.util.Log
import com.google.gson.JsonIOException
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.smart.library.util.STLogUtil
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.ParseException

object STRetrofitException {
    private const val TAG = "[ok http]"

    private const val UNAUTHORIZED = 401
    private const val FORBIDDEN = 403
    private const val NOT_FOUND = 404
    private const val REQUEST_TIMEOUT = 408
    private const val INTERNAL_SERVER_ERROR = 500
    private const val BAD_GATEWAY = 502
    private const val SERVICE_UNAVAILABLE = 503
    private const val GATEWAY_TIMEOUT = 504

    fun handleException(throwable: Throwable): STRetrofitApiException {
        STLogUtil.e(TAG, "网络请求遇到错误", throwable)

        val apiException: STRetrofitApiException
        if (throwable is HttpException) {
            apiException = STRetrofitApiException(throwable, STRetrofitError.HTTP_ERROR)
            when (throwable.code()) {
                UNAUTHORIZED, FORBIDDEN, NOT_FOUND, REQUEST_TIMEOUT, GATEWAY_TIMEOUT, INTERNAL_SERVER_ERROR, BAD_GATEWAY, SERVICE_UNAVAILABLE -> {
                    apiException.displayMessage = "网络连接错误"
                    Log.e(TAG, "网络连接错误")
                }
                else -> {
                    apiException.displayMessage = "网络不给力"
                    Log.e(TAG, "网络连接错误")
                }
            }
            return apiException
        } else if (throwable is STRetrofitServerException) {
            apiException = STRetrofitApiException(throwable, throwable.code)
            apiException.displayMessage = throwable.msg
            Log.e(TAG, throwable.msg)
            return apiException
        } /*else if (throwable is com.alibaba.fastjson.JSONException
                || throwable is JSONException
                || throwable is ParseException) {
            apiException = STRetrofitApiException(throwable, STRetrofitError.PARSE_ERROR)
            apiException.displayMessage = "数据解析错误"
            return apiException
        }*/ else if (throwable is JsonParseException
            || throwable is JsonSyntaxException
            || throwable is JsonIOException
            || throwable is JSONException
            || throwable is ParseException) {
            apiException = STRetrofitApiException(throwable, STRetrofitError.PARSE_ERROR)
            apiException.displayMessage = "网络不给力"
            Log.e(TAG, "数据解析错误")
            return apiException
        } else if (throwable is ConnectException
            || throwable is SocketTimeoutException
            || throwable is UnknownHostException) {
            apiException = STRetrofitApiException(throwable, STRetrofitError.NETWORK_ERROR)
            apiException.displayMessage = "网络不给力"
            Log.e(TAG, "网络连接错误")
            return apiException
        } else {
            apiException = STRetrofitApiException(throwable, STRetrofitError.UNKNOWN)
            apiException.displayMessage = "网络不给力"
            Log.e(TAG, "未知错误")
            return apiException
        }
    }
}
