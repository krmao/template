package com.smart.library.bundle

interface HKIBundleManager {

    fun verify(): Boolean

    fun installWithVerify(callback: ((success: Boolean, rootDir: String) -> Unit)? = null)
}
