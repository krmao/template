package com.smart.library.util.hybird

import android.content.Context
import android.net.Uri
import android.webkit.WebView
import com.smart.library.util.HKLogUtil
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

@Suppress("MemberVisibilityCanPrivate", "unused")
object HKHybirdManager {

    val TAG = "template-hybird"
    private val schemeMap: ConcurrentHashMap<String, (context: Context, uri: Uri?) -> Boolean> = ConcurrentHashMap()


    /**
     *  scheme://host:port/path?k=v
     *
     *  schema  :   http            Gets the scheme of this URI. Example: "http"
     *  host    :   baidu.com       if the authority is "bob@google.com", this method will return "google.com".
     *  port    :   7777            if the authority is "google.com:80", this method will return 80.
     *  path    :   index.html
     */
    fun addScheme(scheme: String, host: String, port: Int, intercept: (context: Context, uri: Uri?) -> Boolean) {
        val schemePrefix = "$scheme://$host:$port"
        schemeMap.put(schemePrefix, intercept)
    }

    /**
     *  scheme://host:port/path?k=v
     *
     *  schema  :   http            Gets the scheme of this URI. Example: "http"
     *  host    :   baidu.com       if the authority is "bob@google.com", this method will return "google.com".
     *  port    :   7777            if the authority is "google.com:80", this method will return 80.
     *  path    :   index.html
     */
    fun addScheme(schemeUriString: String, intercept: (context: Context, uri: Uri?) -> Boolean) {
        schemeMap.put(schemeUriString, intercept)
    }

    fun removeScheme(schemeUriString: String) {
        schemeMap.remove(schemeUriString)
    }

    fun removeScheme(scheme: String, host: String, port: Int) {
        val schemePrefix = "$scheme://$host:$port"
        schemeMap.remove(schemePrefix)
    }

    fun shouldOverrideUrlLoading(context: Context, uriString: String?): Boolean {
        val uri = Uri.parse(uriString)
        val schemePrefix = "${uri?.scheme}://${uri?.host}:${uri?.port}"

        HKLogUtil.d(TAG, "get uri : $uri")
        HKLogUtil.d(TAG, "get schemePrefix : $schemePrefix")
        HKLogUtil.d(TAG, "do intercept ? ${schemeMap.containsKey(schemePrefix)}")

        return schemeMap[schemePrefix]?.invoke(context, uri) ?: false
    }

    //========================================================================================================================
    // async callback
    //========================================================================================================================

    private val callbackMap: ConcurrentMap<String, ((result: String?) -> Unit?)?> = ConcurrentHashMap()
    private val callbackSchemaPrefix: String = "hybird://hybird:${(-System.currentTimeMillis().toInt())}"
    fun addSchemeForCallback() {
        addScheme(callbackSchemaPrefix) { _: Context, uri: Uri? ->
            val hashCode = uri?.getQueryParameter("hashcode")
            callbackMap[hashCode]?.invoke(uri?.getQueryParameter("result"))
            callbackMap.remove(hashCode)
            HKLogUtil.e(HKHybirdManager.TAG, "callbackMap.size[after callback]:" + callbackMap.size)
            true
        }
    }

    fun callJsFunction(webView: WebView?, javascript: String, callback: ((result: String?) -> Unit?)? = null) {
        if (!schemeMap.containsKey(callbackSchemaPrefix))
            addSchemeForCallback()

        val jsString = javascript.replace("javascript:", "")
        val callbackHashCode: String? = callback?.hashCode()?.toString() + System.currentTimeMillis()
        if (callback != null)
            callbackMap.put(callbackHashCode, callback)

        HKLogUtil.e(HKHybirdManager.TAG, "callbackMap.size[before callback]:" + callbackMap.size)

        val wrappedJavascript = """
                (function (){
                    console.log(" [wrap(________)] 　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　");
                    console.log(" [wrap(start___)] >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

                    var result = '';

                    try {
                        result=$jsString;
                        console.log(" [wrap(execute_)] result= "+result);
                    } catch(error) {
                        result = 'error';
                        console.log(" [wrap(execute_)] result= "+result+" error= "+error);
                    }

                    var jumpUrl = "$callbackSchemaPrefix/callback?hashcode=$callbackHashCode&result="+result;
                    console.log("\n[wrap(callback)] jumpUrl=" + jumpUrl);

                    (function (_jumpUrl) {
                        var c = document.createElement("div");
                        c.innerHTML = '<iframe style="display: none;" src="' + _jumpUrl + '"/>';
                        document.querySelector("body").appendChild(c);
                        setTimeout(function () {
                            document.querySelector("body").removeChild(c);
                        }, 1000);
                    })(jumpUrl);

                    console.log(" [wrap(end_____)] <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< \n");
                })();
            """.trimIndent()

        webView?.loadUrl("javascript:$wrappedJavascript")
    }
}
