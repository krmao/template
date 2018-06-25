package com.saike.library.util.location.client.impl

import android.location.Location
import com.saike.library.util.map.CXLatLng
import com.saike.library.util.location.client.CXILocationClient

/**
 * 百度定位
 */
@Suppress("MemberVisibilityCanBePrivate", "unused", "PrivatePropertyName")
internal class CXBMapLocationClient(val ensurePermissions: (() -> Unit?)? = null, val refreshCache: ((CXLatLng) -> Unit?)? = null) : CXILocationClient {
    override fun startLocation(timeout: Long, onSuccess: ((location: Location) -> Unit?)?, onFailure: ((errorCode: Int, errorMessage: String) -> Unit?)?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun startLocationLoop(interval: Long, onSuccess: ((location: Location) -> Unit?)?, onFailure: ((errorCode: Int, errorMessage: String) -> Unit?)?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun stopLocation() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun stopLocationLoop() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}