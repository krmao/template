package com.smart.template

import android.app.Activity
import android.graphics.Color
import android.net.Uri
import android.webkit.WebView
import android.webkit.WebViewClient
import com.smart.library.base.STBaseApplication
import com.smart.library.base.STConfig
import com.smart.library.util.STFileUtil
import com.smart.library.util.STLogUtil
import com.smart.library.util.bus.STBusManager
import com.smart.library.util.hybird.STHybirdBridge
import com.smart.library.util.image.STImageManager
import com.smart.library.util.image.impl.STImageFrescoHandler
import com.smart.library.util.okhttp.STOkHttpManager
import com.smart.library.widget.titlebar.STTitleBar
import com.smart.template.home.tab.HomeTabActivity
import com.smart.template.library.STBridgeCommunication
import com.smart.template.repository.STRepository

@Suppress("unused")
class FinalApplication : STBaseApplication() {

    override fun onCreate() {
        // init before application onCreate
        STConfig.NOTIFICATION_ICON_SMALL = R.mipmap.ic_notification
        STConfig.CLASS_ACTIVITY_MAIN = HomeTabActivity::class.java

        STTitleBar.DEFAULT_BACKGROUND_COLOR = Color.BLACK
        STTitleBar.DEFAULT_TEXT_COLOR = Color.WHITE
        STTitleBar.DEFAULT_TEXT_SIZE = 16f

        super.onCreate()

        //if (STBaseApplication.DEBUG) {
        Thread.setDefaultUncaughtExceptionHandler { t, e -> STFileUtil.saveUncaughtException(t, e) }
        //}

        // init global location
        // STLocationManager.initialize(STLocationGaodeClient())

        // init global repository
        STRepository.init()

        STBusManager.initOnce(this, hashMapOf(
                "reactnative" to "com.smart.library.reactnative.ReactBusHandler",
                "flutter" to "com.smart.library.flutter.FlutterBusHandler",
                "livestreaming" to "com.smart.library.livestreaming.LiveStreamingBusHandler",
                "livestreamingpush" to "com.smart.library.livestreaming.push.LiveStreamingPushBusHandler"
        ))

        val frescoConfig = STImageFrescoHandler.getConfigBuilder(DEBUG, STOkHttpManager.client).build()
        STImageManager.initialize(STImageFrescoHandler(frescoConfig))

        // h5 初始化
        STHybirdBridge.addScheme("") { webView: WebView?, _: WebViewClient?, url: String?, callback: (() -> Unit?)? ->
            val uri = Uri.parse(url)
            if (uri != null && url?.startsWith("smart://hybird/bridge/") == true) {

                val functionName: String? = uri.lastPathSegment
                val params: String? = uri.getQueryParameter("params")
                val callbackId: String? = uri.getQueryParameter("callbackId")
                STBridgeCommunication.handleBridge(webView?.context as? Activity?, functionName, params, callbackId) { _callbackId: String?, resultJsonString: String? ->
                    STHybirdBridge.callJsFunction(webView, "javascript:window.bridge.onCallback($_callbackId, '$resultJsonString')") { result: String? ->
                        STLogUtil.v("[hybird]", "executeJs result = $result")
                    }
                }
                true
            } else {
                false
            }
        }
    }
}
