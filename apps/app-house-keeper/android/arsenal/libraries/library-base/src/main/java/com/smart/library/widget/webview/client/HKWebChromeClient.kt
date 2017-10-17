package com.smart.library.widget.webview.client

import android.webkit.GeolocationPermissions
import android.webkit.WebChromeClient
import android.webkit.WebView
import com.smart.library.util.HKLogUtil
import com.smart.library.util.hybird.HKHybirdManager

open class HKWebChromeClient : WebChromeClient() {

    override fun onReceivedTitle(view: WebView?, title: String?) {
        HKLogUtil.v(HKHybirdManager.TAG, "onReceivedTitle: $title")
        super.onReceivedTitle(view, title)
    }

    override fun onGeolocationPermissionsShowPrompt(origin: String?, callback: GeolocationPermissions.Callback?) {
        HKLogUtil.v(HKHybirdManager.TAG, "onGeolocationPermissionsShowPrompt:allow=true: $origin")
        callback?.invoke(origin, true, true)
        super.onGeolocationPermissionsShowPrompt(origin, callback)
    }
}
