package com.smart.housekeeper.repository.remote.core

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

open class HKOkHttpProgressInterceptor(private val onProgress: ((url: String, current: Long, total: Long) -> Unit?)? = null) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val originalResponse = chain.proceed(request)
        return if (originalResponse.body() == null)
            originalResponse
        else
            originalResponse.newBuilder().body(HKOkHttpProgressResponseBody(request.url().toString(), originalResponse.body()!!, onProgress)).build()
    }
}
