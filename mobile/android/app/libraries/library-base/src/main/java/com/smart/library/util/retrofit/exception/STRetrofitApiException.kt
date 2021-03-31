package com.smart.library.util.retrofit.exception

import androidx.annotation.Keep

@Keep
class STRetrofitApiException(throwable: Throwable, val code: Int) : Exception(throwable) {
    var displayMessage: String? = null

    override fun toString(): String {
        return "STRetrofitApiException(code=$code, displayMessage=$displayMessage)"
    }
}
