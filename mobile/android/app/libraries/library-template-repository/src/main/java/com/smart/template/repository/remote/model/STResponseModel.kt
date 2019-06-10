package com.smart.template.repository.remote.model

import com.google.gson.Gson

class STResponseModel<T> {
    companion object Initializer {
        private val gson: Gson by lazy { Gson() }
    }

    var errorCode = -1
    var errorMessage = ""
    var result: T? = null

    override fun toString(): String = gson.toJson(this)
}
