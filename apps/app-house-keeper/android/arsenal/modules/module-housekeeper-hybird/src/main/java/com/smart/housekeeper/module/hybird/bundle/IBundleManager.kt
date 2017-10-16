package com.smart.housekeeper.module.hybird.bundle

interface IBundleManager {

    fun verify(): Boolean

    fun installWithVerify(callback: ((success: Boolean) -> Unit)? = null)
}
