package com.smart.housekeeper.repository.remote.exception

class HKRetrofitApiException(throwable: Throwable, val code: Int) : Exception(throwable) {
    var displayMessage: String? = null

    override fun toString(): String {
        return "HKRetrofitApiException(code=$code, displayMessage=$displayMessage)"
    }
}
