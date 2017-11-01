package com.smart.housekeeper.repository.remote.core

import com.smart.library.base.HKBaseApplication
import com.smart.library.util.HKBigDecimalUtil
import com.smart.library.util.HKLogUtil
import com.smart.library.util.rx.RxBus
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

object HKOkHttpManager {
    private var CONNECT_TIMEOUT_SECONDS = 20
    private var READ_TIMEOUT_SECONDS = 20
    private var WRITE_TIMEOUT_SECONDS = 20

    val client: OkHttpClient by lazy {
        val sslParams = HKHttpsManager.getSslSocketFactory(null, null, null)
        OkHttpClient.Builder()
            .sslSocketFactory(sslParams.ssLSocketFactory, sslParams.trustManager)
            .readTimeout(READ_TIMEOUT_SECONDS.toLong(), TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT_SECONDS.toLong(), TimeUnit.SECONDS)
            .connectTimeout(CONNECT_TIMEOUT_SECONDS.toLong(), TimeUnit.SECONDS)
            .addNetworkInterceptor(HKOkHttpProgressInterceptor { requestUrl, current, total ->
                RxBus.post(HKOkHttpProgressResponseBody.OnProgressEvent(requestUrl, current, total))
            })
//            .addInterceptor(HKOkHttpLoggingInterceptor(object : HKOkHttpLoggingInterceptor.Logger {
//                override fun log(chain: Interceptor.Chain, message: String) {
//                    HKLogUtil.j(getTag(chain.request().url()), message)
//                }
//
//                fun getTag(url: HttpUrl?): String = if (url == null) "[OKHTTP]" else "[OKHTTP]:" + url.uri().path
//
//            }, if (HKBaseApplication.DEBUG) HKOkHttpLoggingInterceptor.Level.BODY else HKOkHttpLoggingInterceptor.Level.BASIC))
            .build()
    }
}
