package com.smart.template.module.rn

import com.facebook.react.bridge.*
import com.facebook.react.modules.core.DeviceEventManagerModule
import com.smart.library.util.CXLogUtil
import com.smart.library.util.CXSystemUtil

@Suppress("unused")
class ReactBridge(reactContext: ReactApplicationContext?) : ReactContextBaseJavaModule(reactContext) {


    override fun getName(): String {
        return "NativeManager"
    }

    override fun getConstants(): MutableMap<String, Any?> {
        return mutableMapOf(
            "SDK_INT" to CXSystemUtil.SDK_INT,
            "versionCode" to CXSystemUtil.versionCode,
            "versionName" to CXSystemUtil.versionName,
            "appName" to CXSystemUtil.appName,
            "screenWidth" to CXSystemUtil.screenWidth,
            "screenHeight" to CXSystemUtil.screenHeight,
            "isSdCardExist" to CXSystemUtil.isSdCardExist,
            "statusBarHeight" to CXSystemUtil.statusBarHeight
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
        CXLogUtil.d(ReactManager.TAG, "callNative start[threadName=${Thread.currentThread().name}]:functionName=$functionName, data=$data")

        try {
            ReactManager.onCallNativeListener?.invoke(currentActivity, functionName, data, promise)
        } catch (e: Exception) {
            CXLogUtil.e(ReactManager.TAG, "callNative exception:functionName=$functionName, data=$data")
            promise?.reject("0", "callNative exception::functionName=$functionName, data=$data", e)
        }
    }

    companion object {
        /**
         * 向 react native 发送数据
         */
        @JvmStatic
        fun callReact(reactContext: ReactContext?, eventName: String, data: Any?) {
            CXLogUtil.w(ReactManager.TAG, "callReact:$data")
            reactContext?.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)?.emit(eventName, data)
        }
    }
}

