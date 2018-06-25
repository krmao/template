package com.smart.library.util.map.location.client.impl

import android.location.Location
import com.smart.library.util.map.CXLatLng
import com.smart.library.util.map.location.client.CXILocationClient

/**
 * 百度定位
 */
@Suppress("MemberVisibilityCanBePrivate", "unused", "PrivatePropertyName")
internal class CXBMapLocationClient(val ensurePermissions: (() -> Unit?)? = null, val refreshCache: ((CXLatLng) -> Unit?)? = null) : CXILocationClient {
    override fun startLocation(timeout: Long, onSuccess: ((location: Location) -> Unit?)?, onFailure: ((errorCode: Int, errorMessage: String) -> Unit?)?) {
    }

    override fun startLocationLoop(interval: Long, onSuccess: ((location: Location) -> Unit?)?, onFailure: ((errorCode: Int, errorMessage: String) -> Unit?)?) {
    }

    override fun stopLocation() {
    }

    override fun stopLocationLoop() {
    }
}