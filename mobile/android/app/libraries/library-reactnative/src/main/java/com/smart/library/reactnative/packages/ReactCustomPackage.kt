package com.smart.library.reactnative.packages

import com.facebook.react.ReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.ViewManager
import com.smart.library.reactnative.ReactBridge
import com.smart.library.reactnative.components.RCTRecyclerViewManager
import com.smart.library.reactnative.components.ReactToastComponent

class ReactCustomPackage : ReactPackage {

    override fun createNativeModules(reactContext: ReactApplicationContext): List<NativeModule> {
        return arrayListOf(
                ReactToastComponent(reactContext),
                ReactBridge(reactContext)
        )
    }

    override fun createViewManagers(reactContext: ReactApplicationContext): List<ViewManager<*, *>> {
        return arrayListOf(
                RCTRecyclerViewManager()
        )
    }
}
