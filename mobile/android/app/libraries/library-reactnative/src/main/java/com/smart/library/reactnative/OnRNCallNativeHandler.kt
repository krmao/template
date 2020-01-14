package com.smart.library.reactnative

import android.app.Activity
import com.facebook.react.bridge.Promise
import com.smart.template.library.STBridgeCommunication

/**
 * react native call native processors
 */
@Suppress("UNUSED_ANONYMOUS_PARAMETER", "UNUSED_VARIABLE")
class OnRNCallNativeHandler : Function4<Activity?, String?, String?, Promise?, Unit> {

    /**
     * @param functionName to native functions
     *
     * @param data
     *                  pageName    :String
     *                  requestCode :Int?   must be in [0, 65535]
     *                  params      :HashMap<String, String | Number>?
     *
     * @param promise
     *                  promise?.resolve(RNResult.successJson())
     *                  promise?.reject("0", "functionName not found !")
     */
    override fun invoke(currentActivity: Activity?, functionName: String?, data: String?, promise: Promise?) {

        STBridgeCommunication.handleBridge(currentActivity, functionName, data, null) { _callbackId: String?, resultJsonString: String? ->
            promise?.resolve(resultJsonString)
        }
    }
}