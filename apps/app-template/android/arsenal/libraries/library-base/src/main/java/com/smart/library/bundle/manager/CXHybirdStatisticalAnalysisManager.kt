package com.smart.library.bundle.manager

import android.util.Log
import android.util.SparseArray
import android.webkit.WebViewClient
import com.smart.library.bundle.CXHybird
import com.smart.library.bundle.model.CXHybirdStatisticalUrlModel
import com.smart.library.util.CXJsonUtil
import com.smart.library.util.CXLogUtil

object CXHybirdStatisticalAnalysisManager {

    internal val TAG: String = CXHybird.TAG + ":statistical-analysis"

    private val urlMap: MutableMap<String, CXHybirdStatisticalUrlModel> = mutableMapOf()

    @JvmStatic
    fun onPageStarted(webViewClient: WebViewClient?, url: String?) {
        if (webViewClient == null || url == null) return

        val urlStatisticalUrlModel = urlMap[url] ?: CXHybirdStatisticalUrlModel()
        urlStatisticalUrlModel.onPageStarted()

        urlMap.put(url, urlStatisticalUrlModel)
    }

    @JvmStatic
    fun onPageFinished(webViewClient: WebViewClient?, url: String?) {
        if (webViewClient == null || url == null) return

        val urlStatisticalUrlModel = urlMap[url] ?: CXHybirdStatisticalUrlModel()
        urlStatisticalUrlModel.onPageFinished()

        urlMap.put(url, urlStatisticalUrlModel)


        CXLogUtil.d(CXHybirdStatisticalAnalysisManager.TAG, "url:$url")
        CXLogUtil.j(Log.DEBUG, CXHybirdStatisticalAnalysisManager.TAG, CXJsonUtil.toJson(urlStatisticalUrlModel.timeConsumingList))
    }
}
