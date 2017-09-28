package com.smart.library.util.hybird

import android.webkit.WebView
import com.google.common.base.Splitter
import com.smart.library.util.HKLogUtil
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

object HKHybirdManager {
    private val callBackMap: ConcurrentMap<String, ((result: String?) -> Unit?)?> = ConcurrentHashMap()

    fun callJsFunction(webView: WebView?, javascript: String, callback: ((result: String?) -> Unit?)? = null) {
        val jsString = javascript.replace("javascript:", "")
        val callbackHashCode: String? = callback?.hashCode()?.toString() + '-' + System.currentTimeMillis()
        HKLogUtil.e("mmCode:" + callbackHashCode + " , size=" + callBackMap.size)
        if (callback != null)
            callBackMap.put(callbackHashCode, callback)

        val wrappedJavascript = """
                (function (){
                    console.log("\n[wrap] -----------------------------------------------------------------\n[wrap] before :\n");

                    var result=$jsString;

                    console.log("\n[wrap] after : result= "+result+"\n[wrap] -----------------------------------------------------------------\n");

                    window.location.href="schema:bridge//callback?hashcode=$callbackHashCode&result="+result
                })();
            """.trimIndent()

        webView?.loadUrl("javascript:$wrappedJavascript")
    }

    fun shouldOverrideUrlLoading(url: String?): Boolean {
        if (url?.startsWith("schema:bridge//callback") == true) {
            val tmpArrays = url.split("?")
            if (tmpArrays.size == 2) {
                val params = tmpArrays[1]
                val paramsMap: Map<String, String> = Splitter.on('&').trimResults().withKeyValueSeparator("=").split(params)
                val callbackHashCode = paramsMap["hashcode"]
                callBackMap[callbackHashCode]?.invoke(paramsMap["result"])
                callBackMap.remove(callbackHashCode)
                return true
            }
        }
        return false
    }

}
