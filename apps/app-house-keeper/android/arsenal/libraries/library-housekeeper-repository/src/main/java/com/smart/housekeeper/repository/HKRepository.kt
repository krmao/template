package com.smart.housekeeper.repository

import com.smart.housekeeper.repository.remote.HKApiManager
import com.smart.housekeeper.repository.remote.api.HKApi
import com.smart.housekeeper.repository.remote.core.HKOkHttpProgressResponseBody
import com.smart.library.bundle.model.HKHybirdConfigModel
import com.smart.library.util.rx.RxBus
import io.reactivex.Observable
import java.io.File
import java.io.InputStream


/**
 * 对外统一的数据仓库
 * 重要： 接口接收Observable 需要实现onError，否则 网络异常时会出错
 */
@Suppress("MemberVisibilityCanPrivate", "unused")
object HKRepository {

    fun init() {
        HKApiManager.init()
    }

    fun getBundleConfig(): Observable<String> =
        HKApiManager.getApi(HKApi::class.java).getBundleConfig()


    fun downloadFile(url: String, onProgress: ((current: Long, total: Long) -> Unit?)? = null): Observable<InputStream> {
        if (onProgress != null) {
            RxBus.toObservable(HKOkHttpProgressResponseBody.OnProgressEvent::class.java).subscribe { onProgressEvent ->
                if (url == onProgressEvent.requestUrl) onProgress.invoke(onProgressEvent.current, onProgressEvent.total)
            }
        }
        return HKApiManager.getApi(HKApi::class.java).downloadFile(url).compose(HKApiManager.composeWithDownloadFile())
    }

    fun downloadString(url: String): Observable<String> {
        return HKApiManager.getApi(HKApi::class.java).downloadString(url).compose(HKApiManager.composeWithDownloadString())
    }

    fun downloadHybirdModuleConfiguration(url: String): Observable<HKHybirdConfigModel> {
        return HKApiManager.getApi(HKApi::class.java).downloadHybirdModuleConfiguration(url).compose(HKApiManager.compose())
    }

    /**
     * 上传, 例:
     *
     *      HKRepository.getApi(HKApi.class)
     *              .upload(url, file, new HKApi.ProgressListener() {...}))
     *              .subscribe(...);
     *
     * @param url 全路径, 含 baseUrl
     * @param file 指定文件的sd卡上传路径
     * @param progressListener 上传进度回调
     * @return HKResponseModel<Any>
     */
    fun upload(url: String, file: File, progressListener: HKApi.ProgressListener?): Observable<Any> =
        HKApiManager.getApi(HKApi::class.java).upload(url, HKApi.createBody(file, progressListener)).compose(HKApiManager.composeWithResponseModel())
}
