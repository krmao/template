package com.smart.template.module.react

import com.facebook.react.bridge.*
import com.facebook.react.modules.core.DeviceEventManagerModule
import com.smart.library.base.CXBaseApplication
import com.smart.library.util.CXLogUtil
import com.smart.library.util.CXSystemUtil

class ReactBridge(reactContext: ReactApplicationContext?) : ReactContextBaseJavaModule(reactContext) {

    override fun getName(): String {
        return "ReactBridge"
    }


    override fun getConstants(): MutableMap<String, Any> {
        return mutableMapOf(
            "DEBUG" to CXBaseApplication.DEBUG,
            "isApplicationVisible" to CXBaseApplication.isApplicationVisible,
            "SDK_INT" to CXSystemUtil.SDK_INT,
            "versionCode" to CXSystemUtil.versionCode,
            "versionName" to CXSystemUtil.versionName,
            "appIcon" to CXSystemUtil.appIcon,
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
    fun callNative(data: String?, promise: Promise?) {
        CXLogUtil.e("callNative", "系统检测到 react 正在调用 native 参数为:$data")

        try {
            promise?.resolve(data)
        } catch (e: Exception) {
            CXLogUtil.e("callNative", "react 调用 native 出错 参数为:$data", e)
            promise?.reject("0", "react 调用 native 出错 参数为:$data", e)
        }
    }

    companion object {

        /**
         * 向 react native 发送数据
         */
        @JvmStatic
        fun callReact(reactContext: ReactContext?, eventName: String, data: Any?) {
            CXLogUtil.w("callReact", "$data")
            reactContext?.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)?.emit(eventName, data)
        }
    }
}

