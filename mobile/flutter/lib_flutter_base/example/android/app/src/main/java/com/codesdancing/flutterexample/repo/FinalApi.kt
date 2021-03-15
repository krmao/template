package com.codesdancing.flutterexample.repo

import com.smart.library.util.STJsonUtil
import com.smart.library.util.retrofit.STApi
import com.smart.library.util.retrofit.STApiManager
import com.smart.library.util.retrofit.exception.STRetrofitServerException
import com.codesdancing.flutterexample.repo.model.FinalRequestUserInfo
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import retrofit2.http.Body
import retrofit2.http.POST

/*
// examples:

disposables().add(FinalApi.testGetUserInfo(FinalRequestUserInfo.RequestUserData()).subscribe(
    { data ->
        STLogUtil.d("[retrofit]", "onNext")
        if (data != null) {
            STLogUtil.d("[retrofit]", "onNext data=$data")

            adapter.add((0..10).toMutableList())

            //region process loading views
            if (refresh) {
                binding.refreshLayout.finishRefresh(0, true, dataList.isEmpty())
            } else {
                binding.refreshLayout.finishLoadMore(0, true, dataList.isEmpty())
            }
            binding.frameLoading.hideAll()
            if (dataList.isEmpty()) {
                binding.frameLoading.showView(STFrameLoadingLayout.ViewType.FAILURE)
            }
            //endregion
        } else {
            throw com.smart.library.util.retrofit.exception.STRetrofitServerException(-1, "data is null")
        }
    },
    { error ->
        if (requestCount++ <= 0) {
            STLogUtil.d("[retrofit]", "onError $error")
            if (refresh) {
                binding.refreshLayout.finishRefresh(false)
            } else {
                binding.refreshLayout.finishLoadMore(false)
            }

            binding.frameLoading.hideAll()
            if (dataList.isEmpty()) {
                binding.frameLoading.showView(STFrameLoadingLayout.ViewType.FAILURE)
            }
        } else {
            //region test true
            adapter.add((0..10).toMutableList())

            //region process loading views
            if (refresh) {
                binding.refreshLayout.finishRefresh(0, true, dataList.isEmpty())
            } else {
                binding.refreshLayout.finishLoadMore(0, true, dataList.isEmpty())
            }
            binding.frameLoading.hideAll()
            if (dataList.isEmpty()) {
                binding.frameLoading.showView(STFrameLoadingLayout.ViewType.FAILURE)
            }
            //endregion
        }
    }
))

*/
@Suppress("unused")
interface FinalApi : STApi {

    @POST("/test")
    fun testGetUserInfo(@Body request: FinalRequestUserInfo.RequestUserData): Observable<FinalResponse<FinalRequestUserInfo.ResponseUserData>>

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