package com.smart.template.module.rn

import com.facebook.react.bridge.CatalystInstanceImpl
import com.facebook.react.bridge.JSBundleLoader
import com.smart.library.util.CXLogUtil

@Suppress("MemberVisibilityCanBePrivate")
class ReactBundle(val bundlePath: String, private val loader: JSBundleLoader) {
    @Volatile
    var isLoaded = false
        private set

    fun loadScript(catalystInstance: CatalystInstanceImpl?): ReactBundle {
        CXLogUtil.w(ReactManager.TAG, "bundlePath=$bundlePath, isLoaded=$isLoaded")

        if (!isLoaded) {
            catalystInstance?.let {
                synchronized(isLoaded) {
                    if (!isLoaded) {
                        isLoaded = true
                        loader.loadScript(it)
                        CXLogUtil.w(ReactManager.TAG, "bundlePath=$bundlePath, isLoaded=$isLoaded, load bundle success")
                    }
                }
            }
        }
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (other === this) return true
        if (other != null && other is ReactBundle) return !bundlePath.isBlank() && other.bundlePath == bundlePath
        return false
    }

    override fun hashCode(): Int {
        return bundlePath.hashCode()
    }
}
