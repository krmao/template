package com.smart.template.http.model

import com.smart.template.base.config.config.CXConfig

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
    OK(200, "响应成功"),
    SUCCESS_LOGIN(OK.code, "登录成功"),
    SUCCESS_REGISTER(OK.code, "注册成功"),

    ERROR(20000, "响应失败"),
    ERROR_UNAUTHORIZED(401, "身份验证错误"),
    ERROR_ACCESS_DENIED(403, "权限不足"),
    ERROR_SERVER(500, "服务端错误"),

    ERROR_PARAMS(10000, "参数错误"),

    ERROR_SQL(10010, "SQL相关错误"),
    ERROR_SQL_SYNTAX(10011, "SQL语法错误"),
    ERROR_SQL_INSERT(10012, "SQL Field '***' doesn't have a default value"),

    ERROR_VERIFICATION_CODE(10100, "验证码错误"),
    ERROR_REFRESH_TOKEN(100101, "刷新Token失败");

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
