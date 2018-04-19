package com.smart.template.repository

import com.smart.template.repository.remote.CXApiManager
import com.smart.template.repository.remote.api.CXApi
import com.smart.template.repository.remote.core.CXOkHttpProgressResponseBody
import com.smart.library.bundle.model.CXHybirdModuleConfigModel
import com.smart.library.util.rx.RxBus
import io.reactivex.Observable
import java.io.File
import java.io.InputStream


/**
 * 对外统一的数据仓库
 * 重要： 接口接收Observable 需要实现onError，否则 网络异常时会出错
 */
@Suppress("MemberVisibilityCanPrivate", "unused")
object CXRepository {

    fun init(smallIcon: Int) {
        CXApiManager.init(smallIcon)
    }

    fun getBundleConfig(): Observable<String> =
        CXApiManager.getApi(CXApi::class.java).getBundleConfig()


    fun downloadFile(url: String, onProgress: ((current: Long, total: Long) -> Unit?)? = null): Observable<InputStream> {
        if (onProgress != null) {
            RxBus.toObservable(CXOkHttpProgressResponseBody.OnProgressEvent::class.java).subscribe { onProgressEvent ->
                if (url == onProgressEvent.requestUrl) onProgress.invoke(onProgressEvent.current, onProgressEvent.total)
            }
        }
        return CXApiManager.getApi(CXApi::class.java).downloadFile(url).compose(CXApiManager.composeWithDownloadFile())
    }

    fun downloadString(url: String): Observable<String> {
        return CXApiManager.getApi(CXApi::class.java).downloadString(url).compose(CXApiManager.composeWithDownloadString())
    }

    fun downloadHybirdModuleConfiguration(url: String): Observable<CXHybirdModuleConfigModel> {
        return CXApiManager.getApi(CXApi::class.java).downloadHybirdModuleConfiguration(url).compose(CXApiManager.compose())
    }

    fun downloadHybirdAllModuleConfigurations(url: String): Observable<MutableList<CXHybirdModuleConfigModel>> {
        return CXApiManager.getApi(CXApi::class.java).downloadHybirdAllModuleConfigurations(url).compose(CXApiManager.compose())
    }

    /**
     * 上传, 例:
     *
     *      CXRepository.getApi(CXApi.class)
     *              .upload(url, file, new CXApi.ProgressListener() {...}))
     *              .subscribe(...);
     *
     * @param url 全路径, 含 baseUrl
     * @param file 指定文件的sd卡上传路径
     * @param progressListener 上传进度回调
     * @return CXResponseModel<Any>
     */
    fun upload(url: String, file: File, progressListener: CXApi.ProgressListener?): Observable<Any> =
        CXApiManager.getApi(CXApi::class.java).upload(url, CXApi.createBody(file, progressListener)).compose(CXApiManager.composeWithResponseModel())
}
