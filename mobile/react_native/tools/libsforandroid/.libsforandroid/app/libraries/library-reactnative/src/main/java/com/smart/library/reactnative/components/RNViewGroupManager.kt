package com.smart.library.reactnative.components

import android.content.Context
import android.view.View
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.ViewGroupManager
import com.facebook.react.views.view.ReactViewGroup
import com.smart.library.util.STLogUtil

@Suppress("unused", "MemberVisibilityCanBePrivate", "UNCHECKED_CAST")
class RNViewGroupManager : ViewGroupManager<RNViewGroupManager.RCTViewGroup>() {

    private val tag: String get() = name

    override fun createViewInstance(reactContext: ThemedReactContext): RCTViewGroup {
        STLogUtil.d(tag, "createViewInstance thread=${Thread.currentThread().name}")
        return RCTViewGroup(reactContext)
    }

    override fun addView(parent: RCTViewGroup?, child: View?, index: Int) {
        super.addView(parent, child, index)
        STLogUtil.w("RCTViewGroup", "addView index=$index, name=${child?.javaClass?.simpleName}")
    }

    override fun getName(): String = "RCTViewGroup"

    class RCTViewGroup(context: Context) : ReactViewGroup(context)
}