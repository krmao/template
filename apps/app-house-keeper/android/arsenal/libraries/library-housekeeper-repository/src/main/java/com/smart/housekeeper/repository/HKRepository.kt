package com.smart.housekeeper.repository

import com.smart.housekeeper.repository.remote.HKApiManager
import com.smart.housekeeper.repository.remote.api.HKApi
import com.smart.housekeeper.repository.remote.core.HKOkHttpProgressResponseBody
import com.smart.library.util.HKLogUtil
import com.smart.library.util.rx.RxBus
import io.reactivex.Observable
import okhttp3.ResponseBody
import java.io.File


/**
 * 对外统一的数据仓库
 * 重要： 接口接收Observable 需要实现onError，否则 网络异常时会出错
 */
@Suppress("MemberVisibilityCanPrivate", "unused")
object HKRepository {

    fun init() {
        HKApiManager.init()
    }

    /*
        HKRepository.download("http://10.47.12.176:8080/view/Android/job/cxj-toc-android-release/lastSuccessfulBuild/artifact/arsenal/apps/app-chexiangjia/build/outputs/channels/app-chexiangjia-CXB0000-release-v4.3-175-20170913-174408.apk", { current, total ->
            HKLogUtil.d("download:progress", "current:$current/total:$total==${HKBigDecimalUtil.formatValue((current.toFloat() / total.toFloat() * 100).toDouble(), 2)}%")
        })
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .doOnNext {
                HKFileUtil.copy(it.byteStream(), File(HKCacheManager.getCacheDir(), "test.zip").path) { current, total ->
                    HKLogUtil.w("copy:progress", "current:$current/total:$total==${HKBigDecimalUtil.formatValue((current.toFloat() / total.toFloat() * 100).toDouble(), 2)}%")
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { _: ResponseBody ->
                    HKLogUtil.w("progress", "onNext")
                },
                { error: Throwable ->
                    HKLogUtil.w("progress", "onError", error)
                },
                {
                    HKLogUtil.w("progress", "onComplete")
                }
            )
     */
    /**
     * 下载, 例:
     *
     *      HKRepository.getApi(XXApi.class)
     *              .download(url, file)
     *              .subscribe(
     *                  {progressInfo -> ...},  // 下载进度
     *                  {throwable -> ...},     // error
     *                  {() -> ...}             // 下载完成
     *              );
     *
     * @param url  全路径, 含 baseUrl
     * @param file 指定文件的sd卡下载路径
     * @return ResponseBody
     */
    fun download(url: String, onProgress: ((current: Long, total: Long) -> Unit?)? = null): Observable<ResponseBody> {
        RxBus.toObservable(HKOkHttpProgressResponseBody.OnProgressEvent::class.java).subscribe { onProgressEvent ->
            if (url == onProgressEvent.requestUrl) onProgress?.invoke(onProgressEvent.current, onProgressEvent.total)
        }
        return HKApiManager.getApi(HKApi::class.java).download(url)
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
        HKApiManager.getApi(HKApi::class.java).upload(url, HKApi.createBody(file, progressListener)).compose(HKApiManager.compose())
}
