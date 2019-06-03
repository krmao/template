package com.smart.template.repository.remote.core

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

open class STOkHttpProgressInterceptor(private val onProgress: ((url: String, current: Long, total: Long) -> Unit?)? = null) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val originalResponse = chain.proceed(request)
        return if (originalResponse.body() == null)
            originalResponse
        else
            originalResponse.newBuilder().body(STOkHttpProgressResponseBody(request.url().toString(), originalResponse.body()!!, onProgress)).build()
    }
}
