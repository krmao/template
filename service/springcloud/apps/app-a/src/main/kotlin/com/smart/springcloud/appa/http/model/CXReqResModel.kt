package com.smart.springcloud.appa.http.model

import com.smart.springcloud.appa.base.config.config.CXConfig
import org.springframework.http.HttpStatus

data class HKRequest<T>(
    /**
     * 这个版本默认是1
     */
    var version: Int = CXConfig.VERSION,
    /**
     * 1=ANDROID, 2=IOS, 3=WECHAT, 4=PC
     */
    var platform: Int? = null,
    var data: T? = null
)

@Suppress("MemberVisibilityCanPrivate", "unused")
data class HKResponse<T>(
    /**
     * 响应码 200 ok
     */
    var code: Int = HKCode.OK.code,
    var message: String = HKCode.OK.message,
    var data: T  //data 保证不为 NULL
) {
    constructor(statusCode: HKCode = HKCode.OK, data: T) : this(statusCode.code, statusCode.message, data)

    constructor(data: T) : this(statusCode = HKCode.OK, data = data)

    fun setCode(statusCode: HKCode): HKResponse<T> {
        this.code = statusCode.code
        this.message = statusCode.message
        return this
    }

    fun setData(data: T): HKResponse<T> {
        this.data = data
        return this
    }

    companion object {
        @JvmStatic
        fun ok(): HKResponse<Any> {
            return ok(Any())
        }

        @JvmStatic
        fun <T> ok(data: T): HKResponse<T> {
            return HKResponse(HKCode.OK, data)
        }

        @JvmStatic
        fun error(): HKResponse<Any> {
            return error(Any())
        }

        @JvmStatic
        fun <T> error(data: T): HKResponse<T> {
            return HKResponse(HKCode.ERROR, data)
        }
    }
}

enum class HKCode(var code: Int, var message: String) {
    OK(HttpStatus.OK.value(), HttpStatus.OK.reasonPhrase),

    ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase),
    ERROR_UNAUTHORIZED(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.reasonPhrase),
    ERROR_FORBIDDEN(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.reasonPhrase),
    ERROR_TOO_MANY_REQUESTS(HttpStatus.TOO_MANY_REQUESTS.value(), HttpStatus.TOO_MANY_REQUESTS.reasonPhrase),

    ERROR_PARAMS(10000, "Params Error");

    fun setMessage(message: String?): HKCode {
        if (message?.isNotBlank() == true) this.message = message
        return this
    }

    fun <T> response(data: T): HKResponse<T> = HKResponse(this, data = data)

    @JvmOverloads
    inline fun <reified T> response(message: String? = null): HKResponse<T> = HKResponse(this.setMessage(message), data = T::class.java.newInstance())
}

class HKServerException : Exception()
class HKParamsException : Exception()
