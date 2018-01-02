package com.smart.housekeeper.repository.remote.api

import android.os.Handler
import android.os.Looper
import com.smart.housekeeper.repository.remote.core.HKResponseModel
import com.smart.library.bundle.model.HKHybirdConfigModel
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

interface HKApi {

    @GET("/bundle")
    fun getBundleConfig(): Observable<String>

    @GET
    fun downloadHybirdModuleConfiguration(@Url url: String): Observable<HKHybirdConfigModel>

    @Streaming
    @GET
    fun downloadFile(@Url url: String): Observable<Response<ResponseBody>>

    @GET
    fun downloadString(@Url url: String): Observable<Response<ResponseBody>>


    /**
     * 上传, 例:
     *
     *      HKApiManager.getApi(HKApi.class)
     *              .upload(url, HKApi.Companion.createBody(file, new ProgressListener() {...}))
     *              .subscribe(...);
     *
     * @param url 全路径, 含 baseUrl
     * @param body POST 请求体
     * @return HKResponseModel<Any>
     */
    @POST
    fun upload(@Url url: String, @Body body: RequestBody): Observable<HKResponseModel<Any>>

    /**
     * 装饰者模式
     *
     * 扩展OkHttp的请求体，实现上传时的进度提示
     *
     * <a href="http://blog.csdn.net/huyongl1989/article/details/52619236">Retrofit2文件上传下载及其进度显示</a>
     * <a href="https://github.com/JessYanCoding/ProgressManager/blob/master/progress/src/main/java/me/jessyan/progressmanager/body/ProgressRequestBody.java">ProgressRequestBody</a>
     */
    companion object {

        /**
         * 组成 POST 请求的 RequestBody
         *
         * @param file 需上传的文件
         * @param progressListener 进度回调, 为 null 则不回调
         */
        fun createBody(file: File, progressListener: ProgressListener?): RequestBody {
            val rawBody = MultipartBody.Builder()
                .addFormDataPart("uploadFile", file.toString().toLowerCase(), MultipartBody.create(MultipartBody.FORM, file))
                .build()

            return progressListener?.let { ProgressRequestBody(rawBody, it) } ?: rawBody
        }

        /**
         * 装饰者模式
         * 扩展OkHttp的请求体，实现上传时的进度提示
         * [Retrofit2文件上传下载及其进度显示](http://blog.csdn.net/huyongl1989/article/details/52619236)
         * [ProgressRequestBody](https://github.com/JessYanCoding/ProgressManager/blob/master/progress/src/main/java/me/jessyan/progressmanager/body/ProgressRequestBody.java)
         */
        private class ProgressRequestBody(private val requestBody: RequestBody, private val progressListener: ProgressListener?) : RequestBody() {
            companion object {
                val handler = Handler(Looper.getMainLooper())
            }

            @Throws(IOException::class)
            override fun contentLength(): Long = requestBody.contentLength()

            override fun contentType(): MediaType? = requestBody.contentType()

            @Throws(IOException::class)
            override fun writeTo(sink: BufferedSink) {
                // TODO: 加上 "if判断" 会复用 输出流(bufferedSink), 导致requestBody被重复写入两次
                // if (bufferedSink == null) {
                val bufferedSink = Okio.buffer(sink(sink))
                // }
                requestBody.writeTo(bufferedSink)

                bufferedSink.flush()//必须调用flush，否则最后一部分数据可能不会被写入
            }

            /**
             * 写入，回调进度接口
             * @param sink Sink
             * @return Sink
             */
            private fun sink(sink: Sink): Sink {
                return object : ForwardingSink(sink) {
                    internal var bytesWritten = 0L //当前写入字节数
                    internal var contentLength = 0L //总字节长度，避免多次调用contentLength()方法

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
        }
    }

    class ProgressInfo(val bytesRead: Long, val contentLength: Long)
    interface ProgressListener {
        fun onProgress(progressInfo: ProgressInfo)
    }
}
