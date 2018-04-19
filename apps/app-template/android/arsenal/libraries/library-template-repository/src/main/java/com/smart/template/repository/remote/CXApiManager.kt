package com.smart.template.repository.remote

import com.smart.template.repository.remote.core.CXOkHttpManager
import com.smart.template.repository.remote.core.CXResponseModel
import com.smart.template.repository.remote.exception.CXRetrofitException
import com.smart.template.repository.remote.exception.CXRetrofitServerException
import com.smart.library.base.CXApplicationVisibleChangedEvent
import com.smart.library.base.CXBaseApplication
import com.smart.library.util.CXLogUtil
import com.smart.library.util.CXToastUtil
import com.smart.library.util.cache.CXCacheFileManager
import com.smart.library.util.rx.RxBus
import com.smart.library.widget.debug.CXDebugFragment
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
internal object CXApiManager {

    fun init(smallIcon: Int) {
        if (CXBaseApplication.DEBUG) {
            CXURLManager.Environments.values().forEach { environment: CXURLManager.Environments ->
                CXDebugFragment.addHost(environment.name, environment.map[CXURLManager.KEY_HOST]
                    ?: "", CXURLManager.curEnvironment == environment)
            }
            RxBus.toObservable(CXDebugFragment.HostChangeEvent::class.java).subscribe { changeEvent ->
                CXURLManager.curEnvironment = CXURLManager.Environments.valueOf(changeEvent.hostModel.label)
                CXToastUtil.show("检测到环境切换(${changeEvent.hostModel.label})\n已切换到:${CXURLManager.curEnvironment.name}")
            }


            val notificationId = 999999
            CXDebugFragment.showDebugNotification(notificationId, smallIcon = smallIcon)
            RxBus.toObservable(CXApplicationVisibleChangedEvent::class.java).subscribe { changeEvent ->
                if (changeEvent.isApplicationVisible)
                    CXDebugFragment.showDebugNotification(notificationId, smallIcon = smallIcon)
                else
                    CXDebugFragment.cancelDebugNotification(notificationId)
            }
        }
    }

    fun <T> composeWithResponseModel(): ObservableTransformer<CXResponseModel<T>, T> {
        return ObservableTransformer { observable ->
            observable
                .map { responseModel: CXResponseModel<T>? ->
                    if (responseModel != null && responseModel.errorCode == 0 && responseModel.result != null) {
                        responseModel.result!!
                    } else {
                        throw CXRetrofitServerException(responseModel?.errorCode
                            ?: -1, responseModel?.errorMessage ?: "网络不给力")
                    }
                }
                .onErrorResumeNext { throwable: Throwable ->
                    Observable.error(CXRetrofitException.handleException(throwable))
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
                    Observable.error(CXRetrofitException.handleException(throwable))
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
                Observable.error(CXRetrofitException.handleException(throwable))
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
                    CXLogUtil.w("download success :$result")
                    result
                }
                .onErrorResumeNext { throwable: Throwable ->
                    Observable.error(CXRetrofitException.handleException(throwable))
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
                        Observable.error<InputStream>(CXRetrofitException.handleException(Exception("文件下载失败")))
                    response.body()!!.byteStream()
                }
                .onErrorResumeNext { throwable: Throwable ->
                    Observable.error(CXRetrofitException.handleException(throwable))
                }
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

    fun printHeaders(headers: Headers) {
        CXLogUtil.w("""
                            -----------
                            Content-Type        :   ${headers.get("Content-Type")}
                            ETag                :   ${headers.get("ETag")?.toLongOrNull()}
                            Last-Modified       :   ${headers.getDate("Last-Modified")}
                            Date                :   ${headers.getDate("Date")}
                            Expires             :   ${headers.getDate("Expires")}
                            Cache-Control       :   ${headers.get("Cache-Control")}
                            max-age             :   ${headers.get("Cache-Control")?.split("=")?.getOrNull(1)}
                            Content-Disposition :   ${headers.get("Content-Disposition")}
                            filename            :   ${headers.get("Content-Disposition")?.split("=")?.getOrNull(1)
            ?: "temp_file"})
                            -----------
                        """)
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
                val localData = CXCacheFileManager.get(CXBaseApplication.INSTANCE).getAsObject(key)
                if (localData != null)
                    emitter.onNext(localData as T)

                // 2.回调网络数据, 若成功则覆盖先前的缓存数据
                observable.subscribe({ data ->
                    emitter.onNext(data)

                    // 3.拉取数据成功, 缓存数据到本体
                    CXCacheFileManager.get(CXBaseApplication.INSTANCE).put(key, data as Serializable)

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

    private val builder: Retrofit.Builder  by lazy {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())

    }

    @Synchronized
    fun <T> getApi(clazz: Class<T>, baseUrl: String = CXURLManager.curHost): T =
        builder.baseUrl(baseUrl).client(CXOkHttpManager.client).build().create(clazz)
}
