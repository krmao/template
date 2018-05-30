package com.smart.template.repository.remote.core

import com.smart.library.base.CXBaseApplication
import com.smart.library.util.CXLogUtil
import com.smart.library.util.cache.CXCacheManager
import com.smart.library.util.rx.RxBus
import okhttp3.*
import java.io.IOException
import java.util.concurrent.TimeUnit


@Suppress("unused")
object CXOkHttpManager {
    private var CONNECT_TIMEOUT_SECONDS = 20
    private var READ_TIMEOUT_SECONDS = 20
    private var WRITE_TIMEOUT_SECONDS = 20
    private val CACHE_SIZE_BYTES = 1024 * 1024 * 10L

    val client: OkHttpClient by lazy {
        val sslParams = CXHttpsManager.getSslSocketFactory(null, null, null)

        OkHttpClient.Builder()
            .cache(Cache(CXCacheManager.getCacheDir(), CACHE_SIZE_BYTES))
            .sslSocketFactory(sslParams.ssLSocketFactory, sslParams.trustManager)
            .readTimeout(READ_TIMEOUT_SECONDS.toLong(), TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT_SECONDS.toLong(), TimeUnit.SECONDS)
            .connectTimeout(CONNECT_TIMEOUT_SECONDS.toLong(), TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val origRequest = chain.request()
                val request: Request = origRequest.newBuilder()
                    .header("Accept-Language", "en,zh-CN,zh")
                    .header("Accept-Charset", "utf-8")
                    .header("Content-type", "application/json")
                    .header("Connection", "close")
                    // .header("accessToken", CXUserManager.accessToken)
                    .build()
                chain.proceed(request)
            }
            .addNetworkInterceptor(
                CXOkHttpProgressInterceptor { requestUrl, current, total ->
                    RxBus.post(CXOkHttpProgressResponseBody.OnProgressEvent(requestUrl, current, total))
                })
            .addInterceptor(
                CXHttpLoggingInterceptor(
                    if (CXBaseApplication.DEBUG) CXHttpLoggingInterceptor.Level.BODY else CXHttpLoggingInterceptor.Level.NONE,
                    object : CXHttpLoggingInterceptor.Logger {
                        override fun log(message: String) {
                            if (message.length <= 10000)
                                CXLogUtil.j("[OKHTTP]", message)
                            else
                                CXLogUtil.j("[OKHTTP]", "content is too large and don't print at console ...")
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
    @JvmOverloads
    fun doGet(url: String, readTimeoutMS: Long? = null, writeTimeoutMS: Long? = null, connectTimeoutMS: Long? = null, callback: (content: String?) -> Unit?) {
        var result: String? = null
        var okhttpClient = client

        if (readTimeoutMS != null) okhttpClient = okhttpClient.newBuilder().readTimeout(readTimeoutMS, TimeUnit.MILLISECONDS).build()
        if (writeTimeoutMS != null) okhttpClient = okhttpClient.newBuilder().writeTimeout(writeTimeoutMS, TimeUnit.MILLISECONDS).build()
        if (connectTimeoutMS != null) okhttpClient = okhttpClient.newBuilder().connectTimeout(connectTimeoutMS, TimeUnit.MILLISECONDS).build()

        okhttpClient.newCall(Request.Builder().url(url).get().build()).enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                callback.invoke(result)
            }

            override fun onResponse(call: Call?, response: Response?) {
                result = response?.body()?.string()
                callback.invoke(result)
            }
        })
    }

    @JvmStatic
    fun doGet(url: String, callback: Callback) {
        client.newCall(Request.Builder().url(url).get().build()).enqueue(callback)
    }
}
