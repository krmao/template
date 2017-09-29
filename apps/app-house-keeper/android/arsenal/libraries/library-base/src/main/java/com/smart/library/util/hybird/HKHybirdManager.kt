package com.smart.library.util.hybird

import android.net.Uri
import android.webkit.WebView
import com.google.common.base.Splitter
import com.smart.library.util.HKLogUtil
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import javax.xml.validation.Schema

@Suppress("MemberVisibilityCanPrivate")
object HKHybirdManager {

    val TAG = "[hybird]"
    private val schemaMap: ConcurrentMap<String, (uri: Uri?) -> Boolean> = ConcurrentHashMap()


    /**
     *  scheme://host:port/path?k=v
     *
     *  schema  :   http            Gets the scheme of this URI. Example: "http"
     *  host    :   baidu.com       if the authority is "bob@google.com", this method will return "google.com".
     *  port    :   7777            if the authority is "google.com:80", this method will return 80.
     *  path    :   index.html
     */
    fun addScheme(scheme: String, host: String, port: Int, intercept: (uri: Uri?) -> Boolean) {
        val _schemaPrefix = "$scheme://$host:$port"
        schemaMap.put(_schemaPrefix, intercept)
    }

    /**
     *  scheme://host:port/path?k=v
     *
     *  schema  :   http            Gets the scheme of this URI. Example: "http"
     *  host    :   baidu.com       if the authority is "bob@google.com", this method will return "google.com".
     *  port    :   7777            if the authority is "google.com:80", this method will return 80.
     *  path    :   index.html
     */
    fun addScheme(schemeUriString: String, intercept: (uri: Uri?) -> Boolean) {
        schemaMap.put(schemeUriString, intercept)
    }

    fun removeScheme(schemeUriString: String) {
        schemaMap.remove(schemeUriString)
    }

    fun removeScheme(scheme: String, host: String, port: Int) {
        val _schemaPrefix = "$scheme://$host:$port"
        schemaMap.remove(_schemaPrefix)
    }

    fun shouldOverrideUrlLoading(uriString: String?): Boolean {
        val uri = Uri.parse(uriString)
        val _schemaPrefix = "${uri?.scheme}://${uri?.host}:${uri?.port}"

        HKLogUtil.d(TAG, "get uri : $uri")
        HKLogUtil.d(TAG, "get schemePrefix : $_schemaPrefix")
        HKLogUtil.d(TAG, "do intercept ? ${schemaMap.containsKey(_schemaPrefix)}")

        return schemaMap[_schemaPrefix]?.invoke(uri) ?: false
    }

    //========================================================================================================================
    // async callback
    //========================================================================================================================

    private val callbackMap: ConcurrentMap<String, ((result: String?) -> Unit?)?> = ConcurrentHashMap()
    private val callbackSchemaPrefix: String = "hybird://hybird:${(-System.currentTimeMillis().toInt())}"
    fun addSchemeForCallback() {
        addScheme(callbackSchemaPrefix) { uri: Uri? ->
            val hashCode = uri?.getQueryParameter("hashcode")
            callbackMap[hashCode]?.invoke(uri?.getQueryParameter("result"))
            callbackMap.remove(hashCode)
            true
        }
    }

    fun callJsFunction(webView: WebView?, javascript: String, callback: ((result: String?) -> Unit?)? = null) {
        HKLogUtil.e(HKHybirdManager.TAG, "callbackMap[0]:" + callbackMap.size + "\t" + callbackMap)

        if (!schemaMap.containsKey(callbackSchemaPrefix))
            addSchemeForCallback()

        val jsString = javascript.replace("javascript:", "")
        val callbackHashCode: String? = callback?.hashCode()?.toString() + System.currentTimeMillis()
        if (callback != null)
            callbackMap.put(callbackHashCode, callback)

        HKLogUtil.e(HKHybirdManager.TAG, "callbackMap[1]:" + callbackMap.size + "\t" + callbackMap)


        val sss="""

            var c = document.createElement("div")
			c.innerHTML = '<iframe style="display: none;" src="' + url + '"/>'
			document.querySelector("body").appendChild(c)
			setTimeout(function() {
				document.querySelector("body").removeChild(c)
			}, 3000)

        """

        val wrappedJavascript = """
                (function (){
                    console.log("\n[wrap] -----------------------------------------------------------------\n[wrap] before :\n");

                    var result = '';

                    try {
                        result=$jsString;
                        console.log("\n[wrap] after : result= "+result+"\n[wrap] -----------------------------------------------------------------\n");
                    } catch(error) {
                        result = 'error';
                        console.log("\n[wrap] error : result= "+result+" error= "+error+"\n[wrap] -----------------------------------------------------------------\n");
                    }

                    var jumpUrl = "$callbackSchemaPrefix/callback?hashcode=$callbackHashCode&result="+result;
                    console.log("\n[wrap] jumpUrl=" + jumpUrl + "\n[wrap] -----------------------------------------------------------------\n");

                    (function (_jumpUrl) {
                        var c = document.createElement("div");
                        c.innerHTML = '<iframe style="display: none;" src="' + _jumpUrl + '"/>';
                        document.querySelector("body").appendChild(c);
                        setTimeout(function () {
                            document.querySelector("body").removeChild(c);
                        }, 3000);
                    })(jumpUrl);

                    console.log("\n[wrap] end\n[wrap] -----------------------------------------------------------------\n")

                })();
            """.trimIndent()

        webView?.loadUrl("javascript:$wrappedJavascript")
    }
}
