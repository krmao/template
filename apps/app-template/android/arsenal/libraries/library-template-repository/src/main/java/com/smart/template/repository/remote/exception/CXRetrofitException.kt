package com.smart.template.repository.remote.exception


import android.util.Log
import com.google.gson.JsonIOException
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.ParseException

object CXRetrofitException {
    private val TAG: String = CXRetrofitException::class.java.name

    private val UNAUTHORIZED = 401
    private val FORBIDDEN = 403
    private val NOT_FOUND = 404
    private val REQUEST_TIMEOUT = 408
    private val INTERNAL_SERVER_ERROR = 500
    private val BAD_GATEWAY = 502
    private val SERVICE_UNAVAILABLE = 503
    private val GATEWAY_TIMEOUT = 504

    fun handleException(throwable: Throwable): CXRetrofitApiException {
        val apiException: CXRetrofitApiException
        if (throwable is HttpException) {
            apiException = CXRetrofitApiException(throwable, CXRetrofitError.HTTP_ERROR)
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
        } else if (throwable is CXRetrofitServerException) {
            val resultException = throwable
            apiException = CXRetrofitApiException(resultException, resultException.code)
            apiException.displayMessage = resultException.msg
            Log.e(TAG, resultException.msg)
            return apiException
        } /*else if (throwable is com.alibaba.fastjson.JSONException
                || throwable is JSONException
                || throwable is ParseException) {
            apiException = CXRetrofitApiException(throwable, CXRetrofitError.PARSE_ERROR)
            apiException.displayMessage = "数据解析错误"
            return apiException
        }*/ else if (throwable is JsonParseException
            || throwable is JsonSyntaxException
            || throwable is JsonIOException
            || throwable is JSONException
            || throwable is ParseException) {
            apiException = CXRetrofitApiException(throwable, CXRetrofitError.PARSE_ERROR)
            apiException.displayMessage = "网络不给力"
            Log.e(TAG, "数据解析错误")
            return apiException
        } else if (throwable is ConnectException
            || throwable is SocketTimeoutException
            || throwable is UnknownHostException) {
            apiException = CXRetrofitApiException(throwable, CXRetrofitError.NETWORK_ERROR)
            apiException.displayMessage = "网络不给力"
            Log.e(TAG, "网络连接错误")
            return apiException
        } else {
            apiException = CXRetrofitApiException(throwable, CXRetrofitError.UNKNOWN)
            apiException.displayMessage = "网络不给力"
            Log.e(TAG, "未知错误")
            return apiException
        }
    }
}
