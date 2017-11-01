package com.smart.housekeeper.repository.remote.exception

object HKRetrofitError {
    //未知错误
    val UNKNOWN = 1000
    //解析错误
    val PARSE_ERROR = 1001
    //网络错误
    val NETWORK_ERROR = 1002
    //协议出错
    val HTTP_ERROR = 1003
}
