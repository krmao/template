package com.smart.library.util.okhttp

import androidx.annotation.Keep
import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.*
import java.io.IOException

//@Keep
class STOkHttpProgressResponseBody(private var requestUrl: String? = null, private val responseBody: ResponseBody, private val onProgress: ((url: String, current: Long, total: Long) -> Unit?)? = null) : ResponseBody() {

    class OnProgressEvent(val requestUrl: String, val current: Long, val total: Long)

    private var bufferedSource: BufferedSource? = null

    override fun contentType(): MediaType? = responseBody.contentType()

    override fun contentLength(): Long = responseBody.contentLength()

    override fun source(): BufferedSource {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()))
        }
        return bufferedSource!!
    }

    private fun source(source: Source): Source {
        return object : ForwardingSource(source) {
            var bytesRead: Long = 0
            @Throws(IOException::class)
            override fun read(sink: Buffer, byteCount: Long): Long {
                val bytesRead = super.read(sink, byteCount)
                this.bytesRead += if (bytesRead == -1L) 0 else bytesRead
                onProgress?.invoke(requestUrl ?: "", this.bytesRead, contentLength())
                return bytesRead
            }
        }
    }
}
