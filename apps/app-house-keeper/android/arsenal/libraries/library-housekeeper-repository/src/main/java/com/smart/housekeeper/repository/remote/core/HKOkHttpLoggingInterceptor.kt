package com.smart.housekeeper.repository.remote.core

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import okhttp3.internal.http.HttpHeaders
import okhttp3.internal.platform.Platform
import okhttp3.internal.platform.Platform.INFO
import okio.Buffer
import java.io.EOFException
import java.io.IOException
import java.nio.charset.Charset
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * An OkHttp interceptor which logs request and response information. Can be applied as an
 * [application interceptor][OkHttpClient.interceptors] or as a [ ][OkHttpClient.networkInterceptors].
 *
 * The format of the logs created by
 * this class should not be considered stable and may change slightly between releases. If you need
 * a stable logging format, use your own interceptor.
 */
@Suppress("MemberVisibilityCanPrivate")
class HKOkHttpLoggingInterceptor(private val logger: Logger = Logger.DEFAULT, var level: Level = Level.NONE) : Interceptor {

    enum class Level {
        /**
         * No logs.
         */
        NONE,
        /**
         * Logs request and response lines.
         *
         *
         *
         * Example:
         * <pre>`--> POST /greeting http/1.1 (3-byte body)
         *
         * <-- 200 OK (22ms, 6-byte body)
        `</pre> *
         */
        BASIC,
        /**
         * Logs request and response lines and their respective headers.
         *
         *
         *
         * Example:
         * <pre>`--> POST /greeting http/1.1
         * Host: example.com
         * Content-Type: plain/text
         * Content-Length: 3
         * --> END POST
         *
         * <-- 200 OK (22ms)
         * Content-Type: plain/text
         * Content-Length: 6
         * <-- END HTTP
        `</pre> *
         */
        HEADERS,
        /**
         * Logs request and response lines and their respective headers and bodies (if present).
         *
         *
         *
         * Example:
         * <pre>`--> POST /greeting http/1.1
         * Host: example.com
         * Content-Type: plain/text
         * Content-Length: 3
         *
         * Hi?
         * --> END POST
         *
         * <-- 200 OK (22ms)
         * Content-Type: plain/text
         * Content-Length: 6
         *
         * Hello!
         * <-- END HTTP
        `</pre> *
         */
        BODY
    }

    interface Logger {
        fun log(chain: Interceptor.Chain, message: String)

        companion object {
            /**
             * A [Logger] defaults output appropriate for the current platform.
             */
            val DEFAULT: Logger = object : Logger {
                override fun log(chain: Interceptor.Chain, message: String) {
                    Platform.get().log(INFO, message, null)
                }
            }
        }
    }

    init {
        this.level = level
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val level = this.level

        val request = chain.request()
        if (level == Level.NONE) {
            return chain.proceed(request)
        }

        val logBody = level == Level.BODY
        val logHeaders = logBody || level == Level.HEADERS

        val requestBody = request.body()
        val hasRequestBody = requestBody != null

        val connection = chain.connection()
        val protocol = if (connection != null) connection.protocol() else Protocol.HTTP_1_1
        var requestStartMessage = "--> " + request.method() + ' ' + request.url() + ' ' + protocol
        if (!logHeaders && hasRequestBody) {
            requestStartMessage += " (" + requestBody!!.contentLength() + "-byte body)"
        }
        logger.log(chain, requestStartMessage)

        if (logHeaders) {

            val headerParams = HashMap<String, Any>()
            val headers = request.headers()
            var i = 0
            val count = headers.size()
            while (i < count) {
                val name = headers.name(i)
                var valueMap: Map<String, String>? = null
                try {
                    valueMap = gson.fromJson<Map<String, String>>(headers.value(i), object : TypeToken<Map<String, String>>() {

                    }.type)
                } catch (e: Exception) {

                }

                if (valueMap == null) {
                    headerParams.put(name, headers.value(i))
                } else {
                    headerParams.put(name, valueMap)
                }
                i++
            }
            logger.log(chain, gson.toJson(headerParams))

            if (!logBody || !hasRequestBody) {
                logger.log(chain, "--> END " + request.method())
            } else if (bodyEncoded(request.headers())) {
                logger.log(chain, "--> END " + request.method() + " (encoded body omitted)")
            } else {
                val buffer = Buffer()
                requestBody!!.writeTo(buffer)

                var charset: Charset? = UTF8
                val contentType = requestBody.contentType()
                if (contentType != null) {
                    charset = contentType.charset(UTF8)
                }

                logger.log(chain, "")
                if (isPlaintext(buffer)) {
                    logger.log(chain, buffer.readString(charset!!))
                    logger.log(chain, "--> END " + request.method()
                        + " (" + requestBody.contentLength() + "-byte body)")
                } else {
                    logger.log(chain, "--> END " + request.method() + " (binary "
                        + requestBody.contentLength() + "-byte body omitted)")
                }
            }
        }

        val startNs = System.nanoTime()
        val response: Response
        try {
            response = chain.proceed(request)
        } catch (e: Exception) {
            logger.log(chain, "<-- HTTP FAILED: " + e)
            throw e
        }

        val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)

        val responseBody = response.body()
        val contentLength = responseBody!!.contentLength()

        val bodySize = if (contentLength != -1L) contentLength.toString() + "-byte" else "unknown-length"

        logger.log(chain, "<-- " + response.code() + ' ' + response.message() + ' '
            + response.request().url() + " (" + tookMs + "ms" + (if (!logHeaders)
            ", "
                + bodySize + " body"
        else
            "") + ')')

        if (logHeaders) {
            val headerParams = HashMap<String, Any>()
            val headers = response.headers()
            var i = 0
            val count = headers.size()
            while (i < count) {
                headerParams.put(headers.name(i), headers.value(i))
                i++
            }
            logger.log(chain, gson.toJson(headerParams))

            if (!logBody || !HttpHeaders.hasBody(response)) {
                logger.log(chain, "<-- END HTTP")
            } else if (bodyEncoded(response.headers())) {
                logger.log(chain, "<-- END HTTP (encoded body omitted)")
            } else {
                val source = responseBody.source()
                source.request(java.lang.Long.MAX_VALUE) // Buffer the entire body.
                val buffer = source.buffer()

                var charset: Charset? = UTF8
                val contentType = responseBody.contentType()
                if (contentType != null) {
                    charset = contentType.charset(UTF8)
                }

                if (!isPlaintext(buffer)) {
                    logger.log(chain, "")
                    logger.log(chain, "<-- END HTTP (binary " + buffer.size() + "-byte body omitted)")
                    return response
                }

                if (contentLength != 0L) {
                    logger.log(chain, "")
                    logger.log(chain, buffer.clone().readString(charset!!))
                }

                logger.log(chain, "<-- END HTTP (" + buffer.size() + "-byte body)")
            }
        }

        return response
    }

    private fun bodyEncoded(headers: Headers): Boolean {
        val contentEncoding = headers.get("Content-Encoding")
        return contentEncoding != null && !contentEncoding.equals("identity", ignoreCase = true)
    }

    companion object {
        private val UTF8 = Charset.forName("UTF-8")
        private val gson = Gson()

        /**
         * Returns true if the body in question probably contains human readable text. Uses a small sample
         * of code points to detect unicode control characters commonly used in binary file signatures.
         */
        internal fun isPlaintext(buffer: Buffer): Boolean {
            try {
                val prefix = Buffer()
                val byteCount = if (buffer.size() < 64) buffer.size() else 64
                buffer.copyTo(prefix, 0, byteCount)
                for (i in 0..15) {
                    if (prefix.exhausted()) {
                        break
                    }
                    val codePoint = prefix.readUtf8CodePoint()
                    if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                        return false
                    }
                }
                return true
            } catch (e: EOFException) {
                return false // Truncated UTF-8 sequence.
            }

        }
    }
}
