package com.smart.housekeeper.repository.remote.core

import com.smart.library.base.HKBaseApplication
import com.smart.library.util.HKLogUtil
import com.smart.library.util.cache.HKCacheManager
import com.smart.library.util.rx.RxBus
import okhttp3.Cache
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit


object HKOkHttpManager {
    private var CONNECT_TIMEOUT_SECONDS = 20
    private var READ_TIMEOUT_SECONDS = 20
    private var WRITE_TIMEOUT_SECONDS = 20
    private val CACHE_SIZE_BYTES = 1024 * 1024 * 10L

    val client: OkHttpClient by lazy {
        val sslParams = HKHttpsManager.getSslSocketFactory(null, null, null)

        OkHttpClient.Builder()
            .cache(Cache(HKCacheManager.getCacheDir(), CACHE_SIZE_BYTES))
            .sslSocketFactory(sslParams.ssLSocketFactory, sslParams.trustManager)
            .readTimeout(READ_TIMEOUT_SECONDS.toLong(), TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT_SECONDS.toLong(), TimeUnit.SECONDS)
            .connectTimeout(CONNECT_TIMEOUT_SECONDS.toLong(), TimeUnit.SECONDS)
            .addNetworkInterceptor(
                HKOkHttpProgressInterceptor { requestUrl, current, total ->
                    RxBus.post(HKOkHttpProgressResponseBody.OnProgressEvent(requestUrl, current, total))
                })
            .addInterceptor(
                HKHttpLoggingInterceptor(
                    if (HKBaseApplication.DEBUG) HKHttpLoggingInterceptor.Level.BODY else HKHttpLoggingInterceptor.Level.NONE,
                    object : HKHttpLoggingInterceptor.Logger {
                        override fun log(message: String) {
                            if (message.length <= 10000)
                                HKLogUtil.j("[OKHTTP]", message)
                            else
                                HKLogUtil.j("[OKHTTP]", "content is too large and don't print at console ...")
                        }
                    }
                )
            )
            .build()
    }
}
