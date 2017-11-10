package com.smart.library.widget.webview.client

import android.net.Uri
import android.webkit.GeolocationPermissions
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import com.smart.library.util.HKLogUtil
import com.smart.library.util.hybird.HKHybirdBridge

open class HKWebChromeClient : WebChromeClient() {

    override fun onReceivedTitle(view: WebView?, title: String?) {
        HKLogUtil.v(HKHybirdBridge.TAG, "onReceivedTitle: $title")
        super.onReceivedTitle(view, title)
    }

    override fun onGeolocationPermissionsShowPrompt(origin: String?, callback: GeolocationPermissions.Callback?) {
        HKLogUtil.v(HKHybirdBridge.TAG, "onGeolocationPermissionsShowPrompt:allow=true: $origin")
        callback?.invoke(origin, true, true)
        super.onGeolocationPermissionsShowPrompt(origin, callback)
    }

    override fun onShowFileChooser(webView: WebView?, filePathCallback: ValueCallback<Array<Uri>>?, fileChooserParams: FileChooserParams?): Boolean {
        HKLogUtil.v(HKHybirdBridge.TAG, "onShowFileChooser")
        return super.onShowFileChooser(webView, filePathCallback, fileChooserParams)
    }

}
