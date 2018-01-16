package com.smart.template.repository.remote.exception

class CXRetrofitApiException(throwable: Throwable, val code: Int) : Exception(throwable) {
    var displayMessage: String? = null

    override fun toString(): String {
        return "CXRetrofitApiException(code=$code, displayMessage=$displayMessage)"
    }
}
