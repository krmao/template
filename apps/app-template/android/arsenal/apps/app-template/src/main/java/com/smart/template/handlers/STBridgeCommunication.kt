package com.smart.template.handlers

import android.app.Activity

object STBridgeCommunication {

    @JvmStatic
    fun handleBridge(activity: Activity?, functionName: String?, params: String?, callbackId: String?, callback: (callbackId: String?, resultJsonString: String?) -> Unit) {
        if (activity == null || activity.isFinishing || functionName == null || functionName.isBlank()) {
            callback.invoke(callbackId, "xxx")
            return
        }
        when (functionName) {
            "open" -> {
                //xxx

                callback.invoke(callbackId, "xxx")
            }
            else -> {
                callback.invoke(callbackId, "xxx")
            }
        }
    }

}