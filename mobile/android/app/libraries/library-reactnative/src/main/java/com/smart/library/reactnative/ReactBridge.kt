package com.smart.library.reactnative

import com.facebook.react.bridge.*
import com.facebook.react.modules.core.DeviceEventManagerModule
import com.smart.library.base.toDpFromPx
import com.smart.library.util.STLogUtil
import com.smart.library.util.STSystemUtil

@Suppress("unused")
class ReactBridge(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {


    override fun getName(): String {
        return "ReactBridge"
    }

    override fun getConstants(): MutableMap<String, Any?> {
        return mutableMapOf(
                "SDK_INT" to STSystemUtil.SDK_INT,
                "versionCode" to STSystemUtil.versionCode,
                "versionName" to STSystemUtil.versionName,
                "appName" to STSystemUtil.appName,
                "screenWidth" to STSystemUtil.screenWidth,
                "screenHeight" to STSystemUtil.screenHeight,
                "isSdCardExist" to STSystemUtil.isSdCardExist,
                "statusBarHeight" to STSystemUtil.statusBarHeight.toDpFromPx()
        )
    }

    override fun canOverrideExistingModule(): Boolean {
        return true
    }

    /**
     * 也可以使用回调 Callback, 不过 Promise 更加的简洁
     * @see com.facebook.react.bridge.Callback
     */
    @ReactMethod
    fun callNative(functionName: String?, data: String?, promise: Promise?) {
        STLogUtil.d(ReactManager.TAG, "callNative start[threadName=${Thread.currentThread().name}]:functionName=$functionName, data=$data")

        try {
            ReactManager.onCallNativeListener?.invoke(currentActivity, functionName, data, promise)
        } catch (e: Exception) {
            STLogUtil.e(ReactManager.TAG, "callNative exception:functionName=$functionName, data=$data")
            promise?.reject("0", "callNative exception::functionName=$functionName, data=$data", e)
        }
    }

    companion object {
        /**
         * 向 react native 发送数据
         */
        @JvmStatic
        fun callReact(reactContext: ReactContext?, eventName: String, data: Any?) {
            STLogUtil.w(ReactManager.TAG, "callReact:$data")
            reactContext?.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)?.emit(eventName, data)
        }
    }
}

