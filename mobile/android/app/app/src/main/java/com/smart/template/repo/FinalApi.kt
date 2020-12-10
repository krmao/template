package com.smart.template.repo

import com.smart.library.util.STJsonUtil
import com.smart.library.util.retrofit.STApi
import com.smart.library.util.retrofit.STApiManager
import com.smart.library.util.retrofit.exception.STRetrofitServerException
import com.smart.template.repo.model.FinalRequestUserInfo
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import retrofit2.http.GET

@Suppress("unused")
interface FinalApi : STApi {

    @GET("/test")
    fun testGetUserInfo(request: FinalRequestUserInfo.RequestUserData): Observable<FinalResponse<FinalRequestUserInfo.ResponseUserData>>

    data class FinalResponse<T>(
        var errorCode: Int? = -1,
        var errorMessage: String? = "",
        var result: T? = null,
    ) {
        override fun toString(): String = STJsonUtil.toJson(this)
    }

    companion object {
        private fun <T> transformerDataFromResponse(): ObservableTransformer<FinalResponse<T>?, T?> {
            return STApiManager.transformerDataFromResponse { response ->
                if (response != null && response.errorCode == 0 && response.result != null) {
                    response.result
                } else {
                    throw STRetrofitServerException(response?.errorCode ?: -1, response?.errorMessage ?: "网络异常")
                }
            }
        }

        fun testGetUserInfo(request: FinalRequestUserInfo.RequestUserData): Observable<FinalRequestUserInfo.ResponseUserData?> {
            return STApiManager.getApi(FinalApi::class.java).testGetUserInfo(request).compose(transformerDataFromResponse())
        }
    }
}