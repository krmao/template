package com.smart.template.repository

import android.annotation.SuppressLint
import com.smart.library.bundle.model.STHybirdModuleConfigModel
import com.smart.library.util.okhttp.STOkHttpProgressResponseBody
import com.smart.library.util.rx.RxBus
import com.smart.template.repository.remote.STApiManager
import com.smart.template.repository.remote.api.STApi
import io.reactivex.Observable
import java.io.File
import java.io.InputStream


/**
 * 对外统一的数据仓库
 * 重要： 接口接收Observable 需要实现onError，否则 网络异常时会出错
 */
@Suppress("MemberVisibilityCanPrivate", "unused")
object STRepository {

    fun getBundleConfig(): Observable<String> =
        STApiManager.getApi(STApi::class.java).getBundleConfig()


    @SuppressLint("CheckResult")
    fun downloadFile(url: String, onProgress: ((current: Long, total: Long) -> Unit?)? = null): Observable<InputStream> {
        if (onProgress != null) {
            RxBus.toObservable(STOkHttpProgressResponseBody.OnProgressEvent::class.java).subscribe { onProgressEvent ->
                if (url == onProgressEvent.requestUrl) onProgress.invoke(onProgressEvent.current, onProgressEvent.total)
            }
        }
        return STApiManager.getApi(STApi::class.java).downloadFile(url).compose(STApiManager.composeWithDownloadFile())
    }

    fun downloadString(url: String): Observable<String> {
        return STApiManager.getApi(STApi::class.java).downloadString(url).compose(STApiManager.composeWithDownloadString())
    }

    fun downloadHybirdModuleConfiguration(url: String): Observable<STHybirdModuleConfigModel> {
        return STApiManager.getApi(STApi::class.java).downloadHybirdModuleConfiguration(url).compose(STApiManager.compose())
    }

    fun downloadHybirdAllModuleConfigurations(url: String): Observable<MutableList<STHybirdModuleConfigModel>> {
        return STApiManager.getApi(STApi::class.java).downloadHybirdAllModuleConfigurations(url).compose(STApiManager.compose())
    }

    /**
     * 上传, 例:
     *
     *      STRepository.getApi(STApi.class)
     *              .upload(url, file, new STApi.ProgressListener() {...}))
     *              .subscribe(...);
     *
     * @param url 全路径, 含 baseUrl
     * @param file 指定文件的sd卡上传路径
     * @param progressListener 上传进度回调
     * @return STResponseModel<Any>
     */
    fun upload(url: String, file: File, progressListener: STApi.ProgressListener?): Observable<Any> =
        STApiManager.getApi(STApi::class.java).upload(url, STApi.createBody(file, progressListener)).compose(STApiManager.composeWithResponseModel())
}
