package com.smart.library.util.hybird

import android.app.Activity
import android.net.Uri
import android.text.TextUtils
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import com.smart.library.STInitializer
import com.smart.library.util.STLogUtil
import com.smart.library.util.STReflectUtil
import java.lang.reflect.InvocationTargetException
import java.net.URLDecoder
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.declaredFunctions

@Suppress("MemberVisibilityCanPrivate", "unused", "MoveLambdaOutsideParentheses", "ReplacePutWithAssignment")
object STHybirdBridge {
    val TAG = "[hybird]"

    private val classMap: HashMap<String, KClass<*>> = hashMapOf()
    private val schemeMap: HashMap<String, (webView: WebView?, webViewClient: WebViewClient?, url: String?, callback: (() -> Unit?)?) -> Boolean> = hashMapOf()
    private val requestMap: HashMap<String, (webView: WebView?, url: String?) -> WebResourceResponse?> = hashMapOf()

    @Throws(RuntimeException::class, IllegalAccessException::class, IllegalArgumentException::class, InvocationTargetException::class, NullPointerException::class, ExceptionInInitializerError::class)
    fun callNativeMethod(className: String?, methodName: String?, vararg params: Any?): Any? {
        if (!classMap.containsKey(className)) throw RuntimeException("[callNativeMethod] class not register :$className")
        return STReflectUtil.invokeKotlinCompanionObjectDeclaredFunctions(classMap[className], methodName, params)
    }

    /**
     * hybird://native/className/methodName?params=1,2,3,4,5&hashcode=123445
     */
    @Throws(RuntimeException::class, IllegalAccessException::class, IllegalArgumentException::class, InvocationTargetException::class, NullPointerException::class, ExceptionInInitializerError::class)
    fun addNativeClass(scheme: String, className: String, kClass: KClass<*>?) {
        if (TextUtils.isEmpty(className) || kClass == null) {
            throw RuntimeException("[addNativeClass] className:$className or kClass:$kClass is null")
        }
        if (!classMap.containsKey(className)) {
            val methods = HashMap<String, KFunction<*>>()
            kClass.java.kotlin.companionObject?.declaredFunctions?.forEach { methods.put(it.name, it) }
            classMap.put(className, kClass)
        }

        schemeMap.put(scheme, { webView: WebView?, _: WebViewClient?, schemeUrlString: String?, _: (() -> Unit?)? ->
            val schemeUrl = Uri.parse(schemeUrlString)
            val pathSegments = schemeUrl?.pathSegments ?: arrayListOf()
            if (pathSegments.size >= 2) {
                val clazzName = pathSegments[0]
                val methodName = pathSegments[1]

                val queryMap: HashMap<String?, String?> = HashMap()
                schemeUrl?.encodedQuery?.split("&")?.forEach { it.split("=").takeIf { it.isNotEmpty() }?.apply { queryMap.put(this.getOrNull(0), this.getOrNull(1)) } }
                val hashcode = queryMap["hashcode"]  // val hashcode = url?.getQueryParameter("hashcode") // getQueryParameter 默认 decode
                val params = queryMap["params"]

                val paramArray = params?.split(",")?.map { URLDecoder.decode(it, "UTF-8") }?.toTypedArray()

                STLogUtil.d(TAG, "clazzName:$clazzName , methodName:$methodName , params:size:${paramArray?.size}:($params) , hashCode:$hashcode")
                STLogUtil.w(TAG, "native.invoke start --> [${kClass.java.name}.$methodName($params)]")

                var result: Any? = null
                try {
                    result = callNativeMethod(className, methodName, *paramArray ?: arrayOf())
                } catch (e: Exception) {
                    STLogUtil.e(TAG, "native.invoke exception", e)
                }

                STLogUtil.w(TAG, "native.invoke end , result:$result")
                if (!TextUtils.isEmpty(hashcode)) {
                    callJsFunction(webView, "javascript:window.hybird.onCallback($hashcode, $result)")
                }
                true
            } else {
                STLogUtil.e(TAG, "schemaUrl:$schemeUrl 格式定义错误，请参照 hybird://native/className/methodName?params=1,2,3,4,5&hashcode=123445")
                false
            }
        })
    }

    fun addRequest(host: String, intercept: (webView: WebView?, url: String?) -> WebResourceResponse?) {
        requestMap.put(host, intercept)
    }

    fun removeRequest(host: String) {
        requestMap.remove(host)
    }

    fun shouldInterceptRequest(webView: WebView?, url: String?): WebResourceResponse? = requestMap.entries.filter { entry ->
        url?.contains(entry.key) == true
    }.getOrNull(0)?.value?.invoke(webView, url)


    //return requestMap[Uri.parse(url).host]?.invoke(webView, url)

    /**
     *  scheme://host:port/path?k=v
     *
     *  schema  :   http            Gets the scheme of this URI. Example: "http"
     *  host    :   baidu.com       if the authority is "bob@google.com", this method will return "google.com".
     *  port    :   7777            if the authority is "google.com:80", this method will return 80.
     *  path    :   index.html
     */
    /*fun addScheme(scheme: String, host: String, port: Int, intercept: (webView: WebView?, url: String?) -> Boolean) {
        val schemePrefix = "$scheme://$host:$port"
        schemeMap.put(schemePrefix, intercept)
    }*/

    /**
     *  scheme://host:port/path?k=v
     *
     *  schema  :   http            Gets the scheme of this URI. Example: "http"
     *  host    :   baidu.com       if the authority is "bob@google.com", this method will return "google.com".
     *  port    :   7777            if the authority is "google.com:80", this method will return 80.
     *  path    :   index.html
     */
    fun addScheme(schemeUrlString: String, intercept: (webView: WebView?, webViewClient: WebViewClient?, url: String?, callback: (() -> Unit?)?) -> Boolean) {
        schemeMap.put(schemeUrlString, intercept)
    }

    fun removeScheme(schemeUrlString: String) {
        schemeMap.remove(schemeUrlString)
    }

    /*fun removeScheme(scheme: String, host: String, port: Int) {
        val schemePrefix = "$scheme://$host:$port"
        schemeMap.remove(schemePrefix)
    }*/

    fun shouldOverrideUrlLoading(webView: WebView?, webViewClient: WebViewClient?, uriString: String?, callback: (() -> Unit?)? = null): Boolean {
        if (uriString == null)
            return false
        val list = schemeMap.filterKeys { uriString.contains(it) }.entries

        //根据 key 字符串的长度，越长的越先匹配
        for (entry in list.sortedByDescending { it.key.length }) {
            STLogUtil.v(TAG, "shouldOverrideUrlLoading [检测开始] 检测本URL是否被拦截, 匹配: ${entry.key}")
            val intercept = entry.value.invoke(webView, webViewClient, uriString, callback)
            STLogUtil.v(TAG, "shouldOverrideUrlLoading [检测结束] 本URL ${if (intercept) "被拦截 " else "未被拦截 "}")
            if (intercept) {
                return true
            } else {
                continue
            }
        }
        return false
    }

//    fun shouldOverrideUrlLoadingV2(context: Context, uriString: String?): Boolean {
//        val uri = Uri.parse(uriString)
//
//        val port = uri?.port ?: -1
//        val portString = if (port == -1) "" else ":$port"
//
//        val schemePrefix = "${uri?.scheme}://${uri?.host}$portString"
//
//        STLogUtil.d(TAG, "get uri : $uri")
//        STLogUtil.d(TAG, "get schemePrefix : $schemePrefix")
//        STLogUtil.d(TAG, "do intercept ? ${schemeMap.containsKey(schemePrefix)}")
//
//        return schemeMap[schemePrefix]?.invoke(context, uri) ?: false
//    }

    //========================================================================================================================
    // async callback
    //========================================================================================================================

    private val callbackMap: ConcurrentMap<String, ((result: String?) -> Unit?)?> = ConcurrentHashMap()
    private val callbackSchemaPrefix: String = "hybird://hybird:${(-System.currentTimeMillis().toInt())}"
    fun addSchemeForCallback() = addScheme(callbackSchemaPrefix) { _: WebView?, _: WebViewClient?, urlString: String?, _: (() -> Unit?)? ->
        val url = Uri.parse(urlString)
        val hashCode = url?.getQueryParameter("hashcode")
        callbackMap[hashCode]?.invoke(url?.getQueryParameter("result"))
        callbackMap.remove(hashCode)
        STLogUtil.e(TAG, "callbackMap.size[after callback]:" + callbackMap.size)
        true
    }

    fun callJsFunction(webView: WebView?, javascript: String, callback: ((result: String?) -> Unit?)? = null) {
        if (!schemeMap.containsKey(callbackSchemaPrefix))
            addSchemeForCallback()

        val jsString = javascript.replace("javascript:", "")
        val callbackHashCode: String? = callback?.hashCode()?.toString() + System.currentTimeMillis()
        if (callback != null)
            callbackMap.put(callbackHashCode, callback)

        STLogUtil.e(TAG, "callbackMap.size[before callback]:" + callbackMap.size + " :$javascript")

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

                    var jumpUrl = "${callbackSchemaPrefix}/callback?hashcode=$callbackHashCode&result="+result;
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

    init {
        addNativeClass("hybird://hybird:1234", "native", STHybirdMethods::class)

        // 默认拦截 smart://hybird/bridge/
        addScheme("smart://hybird/bridge/") { webView: WebView?, _: WebViewClient?, url: String?, _: (() -> Unit?)? ->
            val uri = Uri.parse(url)
            if (uri != null) {
                val functionName: String? = uri.lastPathSegment
                val params: String? = uri.getQueryParameter("params")
                val callbackId: String? = uri.getQueryParameter("callbackId")

                STInitializer.bridgeHandler()?.handleBridge(webView?.context as? Activity?, functionName, params, callbackId, object : STInitializer.BridgeHandlerCallback {
                    override fun onCallback(callbackId: String?, resultJsonString: String?) {
                        callJsFunction(webView, "javascript:window.bridge.onCallback($callbackId, '$resultJsonString')") { result: String? ->
                            STLogUtil.v("[hybird]", "executeJs result = $result")
                        }
                    }
                })
                true
            } else {
                false
            }
        }
    }
}
