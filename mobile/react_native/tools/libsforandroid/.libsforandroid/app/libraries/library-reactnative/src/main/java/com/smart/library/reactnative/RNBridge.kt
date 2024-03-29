package com.smart.library.reactnative

import com.facebook.react.bridge.*
import com.facebook.react.modules.core.DeviceEventManagerModule
import com.smart.library.STInitializer
import com.smart.library.base.toDpFromPx
import com.smart.library.util.STLogUtil
import com.smart.library.util.STSystemUtil

@Suppress("unused")
class RNBridge(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {


    override fun getName(): String {
        return "ReactBridge"
    }

    override fun getConstants(): MutableMap<String, Any?> {
        return mutableMapOf(
                "SDK_INT" to STSystemUtil.SDK_INT,
                "versionCode" to STSystemUtil.getAppVersionCode(STInitializer.application()),
                "versionName" to STSystemUtil.getAppVersionName(STInitializer.application()),
                "appName" to STSystemUtil.getAppName(STInitializer.application()),
                "screenWidth" to STSystemUtil.screenWidth(),
                "screenHeight" to STSystemUtil.screenHeight(),
                "isSdCardExist" to STSystemUtil.isSDCardExist(),
                "statusBarHeight" to STSystemUtil.statusBarHeight(),
                "statusBarHeightByDensity" to STSystemUtil.statusBarHeight().toDpFromPx(),
                "density" to STSystemUtil.displayMetrics(STInitializer.application())?.density
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
        STLogUtil.d(RNInstanceManager.TAG, "callNative start[threadName=${Thread.currentThread().name}]:functionName=$functionName, data=$data")

        try {
            RNInstanceManager.onCallNativeListener?.invoke(currentActivity, functionName, data, promise)
        } catch (e: Exception) {
            STLogUtil.e(RNInstanceManager.TAG, "callNative exception:functionName=$functionName, data=$data")
            promise?.reject("0", "callNative exception::functionName=$functionName, data=$data", e)
        }
    }

    companion object {
        /**
         * 向 react native 发送数据
         */
        @JvmStatic
        fun callReact(reactContext: ReactContext?, eventName: String, data: Any?) {
            STLogUtil.w(RNInstanceManager.TAG, "callReact:$data")
            reactContext?.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)?.emit(eventName, data)
        }
    }
}

