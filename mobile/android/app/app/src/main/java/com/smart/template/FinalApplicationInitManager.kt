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
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers

@Suppress("unused")
object FinalApplicationInitManager {

    private var isInitialized: Boolean = false

    @JvmStatic
    fun isInitialized(): Boolean = isInitialized

    @JvmStatic
    fun initialize(callback: ((key: String, success: Boolean) -> Unit)? = null) {
        STLogUtil.e("application", "initialize start isInitialized=${isInitialized()}, thread:${Thread.currentThread().name}")

        if (isInitialized()) return

        STConfig.NOTIFICATION_ICON_SMALL = R.mipmap.ic_notification
        STConfig.CLASS_ACTIVITY_MAIN = HomeTabActivity::class.java

        STTitleBar.DEFAULT_BACKGROUND_COLOR = Color.BLACK
        STTitleBar.DEFAULT_TEXT_COLOR = Color.WHITE
        STTitleBar.DEFAULT_TEXT_SIZE = 16f

        Thread.setDefaultUncaughtExceptionHandler { t, e -> STFileUtil.saveUncaughtException(t, e) }

        // todo FlutterBusHandler exception, startInitialization must be called on the main thread Flowable.fromCallable { asyncInitialize(callback) }.subscribeOn(Schedulers.newThread()).subscribe()
        asyncInitialize(callback)

        isInitialized = true
        STLogUtil.e("application", "initialize end isInitialized =$ { isInitialized() }")
    }

    private fun asyncInitialize(callback: ((key: String, success: Boolean) -> Unit)? = null) {
        STBusManager.initOnce(STBaseApplication.INSTANCE, hashMapOf(
                "reactnative" to "com.smart.library.reactnative.RNBusHandler",
                "flutter" to "com.smart.library.flutter.FlutterBusHandler",
                "livestreaming" to "com.smart.library.livestreaming.LiveStreamingBusHandler",
                "livestreamingpush" to "com.smart.library.livestreaming.push.LiveStreamingPushBusHandler"
        ), callback)

        // init global location
        // STLocationManager.initialize(STLocationGaodeClient())

        // init global repository
        STRepository.init()

        val frescoConfig = STImageFrescoHandler.getConfigBuilder(STBaseApplication.DEBUG, STOkHttpManager.client).build()
        STImageManager.initialize(STImageFrescoHandler(frescoConfig))

        // h5 初始化
        STHybirdBridge.addScheme("") { webView: WebView?, _: WebViewClient?, url: String?, _: (() -> Unit?)? ->
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
