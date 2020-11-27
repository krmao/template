package com.smart.library.util.okhttp

import com.smart.library.STInitializer
import com.smart.library.util.STLogUtil
import com.smart.library.util.cache.STCacheManager
import com.smart.library.util.rx.RxBus
import okhttp3.*
import okio.Okio
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit


@Suppress("unused")
object STOkHttpManager {
    private var CONNECT_TIMEOUT_SECONDS = 20
    private var READ_TIMEOUT_SECONDS = 20
    private var WRITE_TIMEOUT_SECONDS = 20
    private val CACHE_SIZE_BYTES = 1024 * 1024 * 10L

    val client: OkHttpClient by lazy {
        val sslParams = STHttpsManager.getSslSocketFactory(null, null, null)

        OkHttpClient.Builder()
            .cache(Cache(STCacheManager.getCacheDir() ?: File(""), CACHE_SIZE_BYTES))
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
                    // .header("accessToken", STUserManager.accessToken)
                    .build()
                chain.proceed(request)
            }
            .addNetworkInterceptor(
                STOkHttpProgressInterceptor { requestUrl, current, total ->
                    RxBus.post(STOkHttpProgressResponseBody.OnProgressEvent(requestUrl, current, total))
                })
            .addInterceptor(
                STHttpLoggingInterceptor(
                    if (STInitializer.debug()) STHttpLoggingInterceptor.Level.BODY else STHttpLoggingInterceptor.Level.NONE,
                    object : STHttpLoggingInterceptor.Logger {
                        override fun log(message: String) {
                            if (message.length <= 10000)
                                STLogUtil.j("[OKHTTP]", message)
                            else
                                STLogUtil.j("[OKHTTP]", "content is too large and don't print at console ...")
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
    fun doGet(url: String?, readTimeoutMS: Long? = null, writeTimeoutMS: Long? = null, connectTimeoutMS: Long? = null, callback: (content: String?) -> Unit) {
        var result: String? = null
        var okHttpClient = client

        if (url.isNullOrBlank()) {
            callback.invoke(result)
            return
        }

        if (readTimeoutMS != null) okHttpClient = okHttpClient.newBuilder().readTimeout(readTimeoutMS, TimeUnit.MILLISECONDS).build()
        if (writeTimeoutMS != null) okHttpClient = okHttpClient.newBuilder().writeTimeout(writeTimeoutMS, TimeUnit.MILLISECONDS).build()
        if (connectTimeoutMS != null) okHttpClient = okHttpClient.newBuilder().connectTimeout(connectTimeoutMS, TimeUnit.MILLISECONDS).build()

        okHttpClient.newCall(Request.Builder().url(url).get().build()).enqueue(object : Callback {
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

    fun doGetFile(url: String, filePath: String, callback: (file: File?) -> Unit) {
        doGet(url, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.invoke(null)
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body()
                if (responseBody != null) {
                    val toFile = File(filePath)
                    if (toFile.exists()) toFile.delete() // delete old
                    val sink = Okio.buffer(Okio.sink(toFile)) // https://stackoverflow.com/a/29012988/4348530
                    sink.writeAll(responseBody.source())
                    sink.close()
                    callback.invoke(toFile)
                } else {
                    callback.invoke(null)
                }
            }
        })
    }
}
