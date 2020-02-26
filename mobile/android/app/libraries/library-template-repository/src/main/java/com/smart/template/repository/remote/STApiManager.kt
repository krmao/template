package com.smart.template.repository.remote

import android.annotation.SuppressLint
import com.smart.library.base.STApplicationVisibleChangedEvent
import com.smart.library.base.STBaseApplication
import com.smart.library.util.STLogUtil
import com.smart.library.util.STToastUtil
import com.smart.library.util.cache.STCacheFileManager
import com.smart.library.util.okhttp.STOkHttpManager
import com.smart.library.util.rx.RxBus
import com.smart.library.widget.debug.STDebugFragment
import com.smart.template.repository.remote.exception.STRetrofitException
import com.smart.template.repository.remote.exception.STRetrofitServerException
import com.smart.template.repository.remote.model.STResponseModel
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
internal object STApiManager {

    @SuppressLint("CheckResult")
    @JvmStatic
    fun init() {
        if (STBaseApplication.DEBUG) {
            STURLManager.Environments.values().forEach { environment: STURLManager.Environments ->
                STDebugFragment.addHost(
                    environment.name, environment.map[STURLManager.KEY_HOST]
                        ?: "", STURLManager.curEnvironment == environment
                )
            }
            RxBus.toObservable(STDebugFragment.HostChangeEvent::class.java)
                .subscribe { changeEvent ->
                    STURLManager.curEnvironment =
                        STURLManager.Environments.valueOf(changeEvent.hostModel.label)
                    STToastUtil.show("检测到环境切换(${changeEvent.hostModel.label})\n已切换到:${STURLManager.curEnvironment.name}")
                }

            STDebugFragment.showDebugNotification()
            RxBus.toObservable(STApplicationVisibleChangedEvent::class.java)
                .subscribe { changeEvent ->
                    if (changeEvent.isApplicationVisible)
                        STDebugFragment.showDebugNotification()
                    else
                        STDebugFragment.cancelDebugNotification()
                }
        }
    }

    fun <T> composeWithResponseModel(): ObservableTransformer<STResponseModel<T>, T> {
        return ObservableTransformer { observable ->
            observable
                .map { responseModel: STResponseModel<T>? ->
                    if (responseModel != null && responseModel.errorCode == 0 && responseModel.result != null) {
                        responseModel.result!!
                    } else {
                        throw STRetrofitServerException(
                            responseModel?.errorCode
                                ?: -1, responseModel?.errorMessage ?: "网络不给力"
                        )
                    }
                }
                .onErrorResumeNext { throwable: Throwable ->
                    Observable.error(STRetrofitException.handleException(throwable))
                }
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

    fun <T> compose(): ObservableTransformer<T, T> {
        return ObservableTransformer { observable ->
            observable
                .onErrorResumeNext { throwable: Throwable ->
                    Observable.error(STRetrofitException.handleException(throwable))
                }
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

    /**
     * 普通的 ResponseBody 格式(非json), 处理 404 等 errorCode
     */
    fun <T> composeWithoutResponse(): ObservableTransformer<T, T> {
        return ObservableTransformer { observable ->
            observable.onErrorResumeNext { throwable: Throwable ->
                Observable.error(STRetrofitException.handleException(throwable))
            }
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

    fun composeWithDownloadString(): ObservableTransformer<Response<ResponseBody>, String> {
        return ObservableTransformer { observable ->
            observable
                .map { response: Response<ResponseBody> ->
                    printHeaders(response.headers())
                    val result = response.body()?.string() ?: ""
                    STLogUtil.w("download success :$result")
                    result
                }
                .onErrorResumeNext { throwable: Throwable ->
                    Observable.error(STRetrofitException.handleException(throwable))
                }
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

    fun composeWithDownloadFile(): ObservableTransformer<Response<ResponseBody>, InputStream> {
        return ObservableTransformer { observable ->
            observable
                .map { response: Response<ResponseBody> ->
                    printHeaders(response.headers())
                    if (response.body() == null)
                        Observable.error<InputStream>(
                            STRetrofitException.handleException(
                                Exception(
                                    "文件下载失败"
                                )
                            )
                        )
                    response.body()!!.byteStream()
                }
                .onErrorResumeNext { throwable: Throwable ->
                    Observable.error(STRetrofitException.handleException(throwable))
                }
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

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
                            max-age             :   ${headers.get("Cache-Control")?.split("=")?.getOrNull(
                1
            )}
                            Content-Disposition :   ${headers.get("Content-Disposition")}
                            filename            :   ${headers.get("Content-Disposition")?.split("=")?.getOrNull(
                1
            )
                ?: "temp_file"})
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
    fun <T> cache(key: String): ObservableTransformer<in T, out T>? {
        return ObservableTransformer { observable ->
            Observable.create<T> { emitter ->
                // 1.检查缓存数据, 若有则先显示缓存
                val localData = STCacheFileManager.get(STBaseApplication.INSTANCE).getAsObject(key)
                if (localData != null)
                    emitter.onNext(localData as T)

                // 2.回调网络数据, 若成功则覆盖先前的缓存数据
                observable.subscribe({ data ->
                    emitter.onNext(data)

                    // 3.拉取数据成功, 缓存数据到本体
                    STCacheFileManager.get(STBaseApplication.INSTANCE)
                        .put(key, data as Serializable)

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

    private val builder: Retrofit.Builder by lazy {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())

    }

    @Synchronized
    fun <T> getApi(clazz: Class<T>, baseUrl: String = STURLManager.curHost): T =
        builder.baseUrl(baseUrl).client(STOkHttpManager.client).build().create(clazz)
}
