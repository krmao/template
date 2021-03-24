package com.codesdancing.flutter.plugins

import com.codesdancing.flutter.plugins.STFlutterBridgeChannel.ERROR_BUSINESS_ERROR
import com.smart.library.util.STThreadUtils
import io.flutter.plugin.common.MethodChannel

@Suppress("MemberVisibilityCanBePrivate", "unused", "SameParameterValue")
abstract class STFlutterBasePlugin {

    private var flutterBridgeChannel: STFlutterBridgeChannel? = null

    fun setFlutterBridgeChannel(flutterBridgeChannel1: STFlutterBridgeChannel?) {
        this.flutterBridgeChannel = flutterBridgeChannel1
    }

    fun flutterBridgeChannel(): STFlutterBridgeChannel? = this.flutterBridgeChannel

    abstract fun getPluginName(): String?

    protected fun callbackSuccess(result: MethodChannel.Result, successData: Any? = null) {
        if (STThreadUtils.isMainThread()) {
            result.success(successData)
        } else {
            STThreadUtils.post(Runnable { result.success(successData) })
        }
    }

    protected fun callbackFail(result: MethodChannel.Result, errorMsg: String?, failData: Any?) {
        callbackFail(result, ERROR_BUSINESS_ERROR, errorMsg, failData)
    }

    protected fun callbackFail(result: MethodChannel.Result, errorCode: String?, errorMsg: String?, failData: Any?) {
        if (STThreadUtils.isMainThread()) {
            result.error(errorCode, errorMsg, failData)
        } else {
            STThreadUtils.post(Runnable { result.error(errorCode, errorMsg, failData) })
        }
    }
}