package com.smart.library.reactnative.packages

import com.facebook.react.ReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.ViewManager
import com.smart.library.reactnative.RNBridge
import com.smart.library.reactnative.components.RNRecyclerViewManager
import com.smart.library.reactnative.components.RNViewGroupManager
import com.smart.library.reactnative.components.RNToastComponent

class RNCustomPackage : ReactPackage {

    override fun createNativeModules(reactContext: ReactApplicationContext): List<NativeModule> {
        return arrayListOf(
                RNToastComponent(reactContext),
                RNBridge(reactContext)
        )
    }

    override fun createViewManagers(reactContext: ReactApplicationContext): List<ViewManager<*, *>> {
        return arrayListOf(
                RNRecyclerViewManager(),
                RNViewGroupManager()
        )
    }
}
