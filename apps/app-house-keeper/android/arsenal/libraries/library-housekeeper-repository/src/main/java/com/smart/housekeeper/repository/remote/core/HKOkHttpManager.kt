package com.smart.housekeeper.repository.remote.core

import com.smart.library.base.HKBaseApplication
import com.smart.library.util.HKLogUtil
import com.smart.library.util.cache.HKCacheManager
import com.smart.library.util.rx.RxBus
import okhttp3.*
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


    @JvmStatic
    @JvmOverloads
    fun doGetSync(url: String, readTimeoutMS: Long? = null, writeTimeoutMS: Long? = null, connectTimeoutMS: Long? = null): String? {
        var result: String? = null
        var response: Response? = null
        var okhttpClient = client

        if (readTimeoutMS != null) okhttpClient = okhttpClient.newBuilder().readTimeout(readTimeoutMS, TimeUnit.MILLISECONDS).build()
        if (writeTimeoutMS != null) okhttpClient = okhttpClient.newBuilder().writeTimeout(writeTimeoutMS, TimeUnit.MILLISECONDS).build()
        if (connectTimeoutMS != null) okhttpClient = okhttpClient.newBuilder().connectTimeout(connectTimeoutMS, TimeUnit.MILLISECONDS).build()

        try {
            response = okhttpClient.newCall(Request.Builder().url(url).get().build()).execute()
            if (response != null && response.isSuccessful) result = response.body()?.string()
        } catch (exception: Exception) {
            exception.printStackTrace()
        } finally {
            response?.close()
        }
        return result
    }

    @JvmStatic
    fun doGet(url: String, callback: Callback) {
        client.newCall(Request.Builder().url(url).get().build()).enqueue(callback)
    }
}
