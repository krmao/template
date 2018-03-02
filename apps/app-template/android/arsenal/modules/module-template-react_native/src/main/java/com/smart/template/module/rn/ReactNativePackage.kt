package com.smart.template.module.rn

import android.view.View
import com.facebook.react.ReactPackage
import com.facebook.react.bridge.JavaScriptModule
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.ReactShadowNode
import com.facebook.react.uimanager.ViewManager

class ReactNativePackage : ReactPackage {
    override fun createJSModules(): MutableList<Class<out JavaScriptModule>> {
        return arrayListOf()
    }

    override fun createNativeModules(reactContext: ReactApplicationContext?): MutableList<NativeModule> {
        return arrayListOf(
            ReactToastUtil(reactContext),
            ReactBridge(reactContext)
        )
    }

    override fun createViewManagers(reactContext: ReactApplicationContext?): MutableList<ViewManager<View, ReactShadowNode>> {
        return arrayListOf()
    }
}
