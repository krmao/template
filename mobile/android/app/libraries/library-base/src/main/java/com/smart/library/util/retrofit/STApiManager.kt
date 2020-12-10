package com.smart.library.util.retrofit

import com.smart.library.STInitializer
import com.smart.library.util.STLogUtil
import com.smart.library.util.STURLManager
import com.smart.library.util.cache.STCacheFileManager
import com.smart.library.util.okhttp.STOkHttpManager
import com.smart.library.util.retrofit.exception.STRetrofitException
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.Headers
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.InputStream
import java.io.Serializable

@Suppress("UNCHECKED_CAST", "unused")
object STApiManager {

    @JvmStatic
    fun <Response, T> transformerDataFromResponse(transformerDataFromResponseOrThrowRetrofitException: (response: Response?) -> T?): ObservableTransformer<Response?, T?> {
        return ObservableTransformer { changeObservable(it, transformerDataFromResponseOrThrowRetrofitException) }
    }

    @JvmStatic
    fun <Response, T> changeObservable(observable: Observable<Response?>, transformerDataFromResponseOrThrowRetrofitException: (response: Response?) -> T?): Observable<T?> {
        return wrapObservable(observable.map { transformerDataFromResponseOrThrowRetrofitException.invoke(it) })
    }

    @JvmStatic
    fun <T> wrapObservable(observable: Observable<T?>): Observable<T?> {
        return observable.onErrorResumeNext { throwable: Throwable -> Observable.error(STRetrofitException.handleException(throwable)) }.subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    @JvmStatic
    fun <T> transformerException(): ObservableTransformer<T?, T?> {
        return ObservableTransformer { wrapObservable(it) }
    }

    @JvmStatic
    fun transformerStringFromResponse(): ObservableTransformer<Response<ResponseBody>?, String?> {
        return transformerDataFromResponse({ response ->
            if (response == null) {
                null
            } else {
                printHeaders(response.headers())
                val result: String? = response.body()?.string()
                STLogUtil.w("download success :$result")
                result
            }
        })
    }

    @JvmStatic
    fun transformerInputStreamFromResponse(): ObservableTransformer<Response<ResponseBody>, InputStream> {
        return ObservableTransformer { observable ->
            observable
                .map { response: Response<ResponseBody> ->
                    printHeaders(response.headers())
                    if (response.body() == null)
                        Observable.error<InputStream>(STRetrofitException.handleException(Exception("文件下载失败")))
                    response.body()?.byteStream()
                }
                .onErrorResumeNext { throwable: Throwable ->
                    Observable.error(STRetrofitException.handleException(throwable))
                }
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

    @JvmStatic
    private fun printHeaders(headers: Headers) {
        STLogUtil.w(
            """
                            -----------
                            Content-Type        :   ${headers.get("Content-Type")}
                            ETag                :   ${headers.get("ETag")?.toLongOrNull()}
                            Last-Modified       :   ${headers.getDate("Last-Modified")}
                            Date                :   ${headers.getDate("Date")}
                            Expires             :   ${headers.getDate("Expires")}
                            Cache-Control       :   ${headers.get("Cache-Control")}
                            max-age             :   ${headers.get("Cache-Control")?.split("=")?.getOrNull(1)}
                            Content-Disposition :   ${headers.get("Content-Disposition")}
                            filename            :   ${
                headers.get("Content-Disposition")?.split("=")?.getOrNull(1) ?: "temp_file"
            })
                            -----------
            """
        )
    }

    /**
     * 接口数据缓存策略:
     *      1.检查缓存数据, 若有则先显示缓存
     *      2.回调网络数据, 若成功则覆盖先前的缓存数据
     *      3.拉取数据成功, 缓存数据到本体
     *
     * @param key 接口标识, 如: "接口名" + "请求参数(Json格式)"
     */
    @JvmStatic
    fun <T> cache(key: String): ObservableTransformer<in T, out T>? {
        return ObservableTransformer { observable ->
            Observable.create<T> { emitter ->
                // 1.检查缓存数据, 若有则先显示缓存
                val localData = STCacheFileManager.get(STInitializer.application()).getAsObject(key)
                if (localData != null) emitter.onNext(localData as T)

                // 2.回调网络数据, 若成功则覆盖先前的缓存数据
                observable.subscribe({ data ->
                    emitter.onNext(data)
                    // 3.拉取数据成功, 缓存数据到本体
                    STCacheFileManager.get(STInitializer.application()).put(key, data as Serializable)
                }, { error ->
                    emitter.onError(error)
                }, {
                    emitter.onComplete()
                })
            }
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

    private val builder: Retrofit.Builder by lazy { Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory(RxJava2CallAdapterFactory.create()) }

    @JvmStatic
    @Synchronized
    fun <T> getApi(clazz: Class<T>, baseUrl: String = STURLManager.curHost): T {
        return builder.baseUrl(baseUrl).client(STOkHttpManager.client).build().create(clazz)
    }
}
