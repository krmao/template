package com.smart.library.widget.webview.client

import android.net.Uri
import android.webkit.GeolocationPermissions
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.annotation.Keep
import com.smart.library.util.STLogUtil
import com.smart.library.util.hybird.STHybirdBridge

//@Keep
open class STWebChromeClient : WebChromeClient() {

    override fun onReceivedTitle(view: WebView?, title: String?) {
        STLogUtil.v(STHybirdBridge.TAG, "onReceivedTitle: $title")
        super.onReceivedTitle(view, title)
    }

    override fun onGeolocationPermissionsShowPrompt(origin: String?, callback: GeolocationPermissions.Callback?) {
        STLogUtil.v(STHybirdBridge.TAG, "onGeolocationPermissionsShowPrompt:allow=true: $origin")
        //callback?.invoke(origin, true, true)
        super.onGeolocationPermissionsShowPrompt(origin, callback)
    }

    override fun onShowFileChooser(webView: WebView?, filePathCallback: ValueCallback<Array<Uri>>?, fileChooserParams: FileChooserParams?): Boolean {
        STLogUtil.v(STHybirdBridge.TAG, "onShowFileChooser")
        return super.onShowFileChooser(webView, filePathCallback, fileChooserParams)
    }
}
