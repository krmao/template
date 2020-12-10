package com.smart.library.util.retrofit

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import com.smart.library.bundle.model.STHybirdModuleConfigModel
import com.smart.library.util.okhttp.STOkHttpProgressResponseBody
import com.smart.library.util.rx.RxBus
import io.reactivex.Observable
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import okio.*
import retrofit2.Response
import retrofit2.http.*
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.util.*

/*
// 使用案例

// repo/model/FinalRequestUserInfo.kt
class FinalRequestUserInfo {
    data class RequestUserData(var userId: String? = null)
    data class ResponseUserData(var userName: String? = null)
}

// repo/FinalApi.kt
interface FinalApi : STApi {

    @GET("/test")
    fun testGetUserInfo(request: FinalRequestUserInfo.RequestUserData): Observable<FinalResponse<FinalRequestUserInfo.ResponseUserData>>

    data class FinalResponse<T>(
        var errorCode: Int? = -1,
        var errorMessage: String? = "",
        var result: T? = null,
    ) {
        override fun toString(): String = STJsonUtil.toJson(this)
    }

    companion object {
        private fun <T> transformerDataFromResponse(): ObservableTransformer<FinalResponse<T>?, T?> {
            return STApiManager.transformerDataFromResponse { response ->
                if (response != null && response.errorCode == 0 && response.result != null) {
                    response.result
                } else {
                    throw STRetrofitServerException(response?.errorCode ?: -1, response?.errorMessage ?: "网络异常")
                }
            }
        }

        fun testGetUserInfo(request: FinalRequestUserInfo.RequestUserData): Observable<FinalRequestUserInfo.ResponseUserData?> {
            return STApiManager.getApi(FinalApi::class.java).testGetUserInfo(request).compose(transformerDataFromResponse())
        }
    }
}

override fun onDestroy() {
    super.onDestroy()
    disposable?.dispose()
    disposable = null
    Log.w(TAG, "onDestroy")
}

private var disposable: Disposable? = null
private fun request() {
    disposable = FinalApi.testGetUserInfo(FinalRequestUserInfo.RequestUserData()).subscribe(
        { data ->
            STLogUtil.d("[retrofit]", "onNext")
            if (data != null) {
                STLogUtil.d("[retrofit]", "onNext data=$data")
            } else {
                STLogUtil.d("[retrofit]", "onNext data=null")
            }
        },
        { error ->
            STLogUtil.d("[retrofit]", "onError $error")
        },
        {
            STLogUtil.d("[retrofit]", "onComplete")
        }
    )
}

*/
@Suppress("MemberVisibilityCanBePrivate", "unused")
interface STApi {

    @GET("/bundle")
    fun getBundleConfig(): Observable<String>

    @GET
    fun downloadHybirdModuleConfiguration(@Url url: String): Observable<STHybirdModuleConfigModel>

    @GET
    fun downloadHybirdAllModuleConfigurations(@Url url: String): Observable<MutableList<STHybirdModuleConfigModel>>

    @Streaming
    @GET
    fun downloadFile(@Url url: String): Observable<Response<ResponseBody>>

    @GET
    fun downloadString(@Url url: String): Observable<Response<ResponseBody>>

    @POST
    fun upload(@Url url: String, @Body body: RequestBody): Observable<Any>

    class ProgressInfo(val bytesRead: Long, val contentLength: Long)
    interface ProgressListener {
        fun onProgress(progressInfo: ProgressInfo)
    }

    /**
     * 装饰者模式
     * 扩展OkHttp的请求体，实现上传时的进度提示
     * [Retrofit2文件上传下载及其进度显示](http://blog.csdn.net/huyongl1989/article/details/52619236)
     * [ProgressRequestBody](https://github.com/JessYanCoding/ProgressManager/blob/master/progress/src/main/java/me/jessyan/progressmanager/body/ProgressRequestBody.java)
     */
    private class ProgressRequestBody(private val requestBody: RequestBody, private val progressListener: ProgressListener?) : RequestBody() {

        @Throws(IOException::class)
        override fun contentLength(): Long = requestBody.contentLength()

        override fun contentType(): MediaType? = requestBody.contentType()

        @Throws(IOException::class)
        override fun writeTo(sink: BufferedSink) {
            val bufferedSink = Okio.buffer(sink(sink))
            requestBody.writeTo(bufferedSink)
            bufferedSink.flush()
        }

        /**
         * 写入，回调进度接口
         * @param sink Sink
         * @return Sink
         */
        private fun sink(sink: Sink): Sink {
            return object : ForwardingSink(sink) {
                var bytesWritten = 0L //当前写入字节数
                var contentLength = 0L //总字节长度，避免多次调用contentLength()方法

                @Throws(IOException::class)
                override fun write(source: Buffer, byteCount: Long) {
                    super.write(source, byteCount)
                    if (contentLength == 0L) {
                        contentLength = contentLength()
                    }
                    bytesWritten += byteCount
                    progressListener?.onProgress(ProgressInfo(bytesWritten, contentLength))
                }
            }
        }

        companion object {
            val handler = Handler(Looper.getMainLooper())
        }
    }

    /**
     * 装饰者模式, 扩展OkHttp的请求体，实现上传时的进度提示
     * <a href="http://blog.csdn.net/huyongl1989/article/details/52619236">Retrofit2文件上传下载及其进度显示</a>
     * <a href="https://github.com/JessYanCoding/ProgressManager/blob/master/progress/src/main/java/me/jessyan/progressmanager/body/ProgressRequestBody.java">ProgressRequestBody</a>
     */
    companion object {
        fun getBundleConfig(): Observable<String> {
            return STApiManager.getApi(STApi::class.java).getBundleConfig()
        }

        @SuppressLint("CheckResult")
        fun downloadFile(url: String, onProgress: ((current: Long, total: Long) -> Unit?)? = null): Observable<InputStream> {
            if (onProgress != null) {
                RxBus.toObservable(STOkHttpProgressResponseBody.OnProgressEvent::class.java).subscribe { onProgressEvent ->
                    if (url == onProgressEvent.requestUrl) onProgress.invoke(onProgressEvent.current, onProgressEvent.total)
                }
            }
            return STApiManager.getApi(STApi::class.java).downloadFile(url).compose(STApiManager.transformerInputStreamFromResponse())
        }

        fun downloadString(url: String): Observable<String?> {
            return STApiManager.getApi(STApi::class.java).downloadString(url).compose(STApiManager.transformerStringFromResponse())
        }

        fun downloadHybirdModuleConfiguration(url: String): Observable<STHybirdModuleConfigModel> {
            return STApiManager.getApi(STApi::class.java).downloadHybirdModuleConfiguration(url).compose(STApiManager.transformerException())
        }

        fun downloadHybirdAllModuleConfigurations(url: String): Observable<MutableList<STHybirdModuleConfigModel>> {
            return STApiManager.getApi(STApi::class.java).downloadHybirdAllModuleConfigurations(url).compose(STApiManager.transformerException())
        }

        /**
         * 组成 POST 请求的 RequestBody
         * @param file 需上传的文件
         * @param progressListener 进度回调, 为 null 则不回调
         */
        fun createBody(file: File, progressListener: ProgressListener?): RequestBody {
            val rawBody = MultipartBody.Builder().addFormDataPart("uploadFile", file.toString().toLowerCase(Locale.getDefault()), MultipartBody.create(MultipartBody.FORM, file)).build()
            return progressListener?.let { ProgressRequestBody(rawBody, it) } ?: rawBody
        }

        /**
         * 上传, 例: STRepository.getApi(STApi.class).upload(url, file, new STApi.ProgressListener() {...})).subscribe(...);
         * @param url 全路径, 含 baseUrl
         * @param file 指定文件的sd卡上传路径
         * @param progressListener 上传进度回调
         * @return STResponseModel<Any>
         */
        fun upload(url: String, file: File, progressListener: ProgressListener?): Observable<Any> {
            return STApiManager.getApi(STApi::class.java).upload(url, createBody(file, progressListener)).compose(STApiManager.transformerException())
        }
    }
}
