@file:Suppress("unused")

package com.smart.springcloud.library.common.http.model

import com.smart.springcloud.library.common.base.config.config.STConfig
import org.springframework.http.HttpStatus

data class STRequest<T>(
    /**
     * 这个版本默认是1
     */
    var version: Int = STConfig.VERSION,
    /**
     * 1=ANDROID, 2=IOS, 3=WECHAT, 4=PC
     */
    var platform: Int? = null,
    var data: T? = null
)

data class STResponse<T>(
    /**
     * 响应码 200 ok
     */
    var code: Int = STCode.OK.code,
    var message: String = STCode.OK.message,
    var data: T  //data 保证不为 NULL
) {
    constructor(statusCode: STCode = STCode.OK, data: T) : this(statusCode.code, statusCode.message, data)

    constructor(data: T) : this(statusCode = STCode.OK, data = data)

    fun setCode(statusCode: STCode): STResponse<T> {
        this.code = statusCode.code
        this.message = statusCode.message
        return this
    }

    fun setData(data: T): STResponse<T> {
        this.data = data
        return this
    }

    companion object {
        @JvmStatic
        fun ok(): STResponse<Any> {
            return ok(Any())
        }

        @JvmStatic
        fun <T> ok(data: T): STResponse<T> {
            return STResponse(STCode.OK, data)
        }

        @JvmStatic
        fun error(): STResponse<Any> {
            return error(Any())
        }

        @JvmStatic
        fun <T> error(data: T): STResponse<T> {
            return STResponse(STCode.ERROR, data)
        }
    }
}

enum class STCode(var code: Int, var message: String) {
    OK(HttpStatus.OK.value(), HttpStatus.OK.reasonPhrase),

    ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase),
    ERROR_UNAUTHORIZED(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.reasonPhrase),
    ERROR_FORBIDDEN(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.reasonPhrase),
    ERROR_TOO_MANY_REQUESTS(HttpStatus.TOO_MANY_REQUESTS.value(), HttpStatus.TOO_MANY_REQUESTS.reasonPhrase),

    ERROR_PARAMS(10000, "Params Error");

    fun setMessage(message: String?): STCode {
        if (message?.isNotBlank() == true) this.message = message
        return this
    }

    fun <T> response(data: T): STResponse<T> = STResponse(this, data = data)

    @JvmOverloads
    inline fun <reified T> response(message: String? = null): STResponse<T> = STResponse(this.setMessage(message), data = T::class.java.newInstance())
}

class STServerException : Exception()

class STParamsException : Exception()
