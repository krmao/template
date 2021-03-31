package com.smart.library.util.retrofit.exception

import androidx.annotation.Keep

@Keep
class STRetrofitServerException(val code: Int, val msg: String) : RuntimeException()
