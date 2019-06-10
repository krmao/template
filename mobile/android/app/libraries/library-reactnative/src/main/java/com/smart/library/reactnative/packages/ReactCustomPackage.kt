package com.smart.library.reactnative.packages

import android.view.View
import com.facebook.react.ReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.ReactShadowNode
import com.facebook.react.uimanager.ViewManager
import com.smart.library.reactnative.ReactBridge
import com.smart.library.reactnative.components.ReactToastComponent

class ReactCustomPackage : ReactPackage {

    override fun createNativeModules(reactContext: ReactApplicationContext?): MutableList<NativeModule> {
        return arrayListOf(
                ReactToastComponent(reactContext),
                ReactBridge(reactContext)
        )
    }

    override fun createViewManagers(reactContext: ReactApplicationContext?): MutableList<ViewManager<View, ReactShadowNode<*>>> {
        return arrayListOf()
    }
}
