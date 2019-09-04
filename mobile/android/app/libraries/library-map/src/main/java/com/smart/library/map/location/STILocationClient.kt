package com.smart.library.map.location

import android.location.Location

interface STILocationClient {

    fun getLastKnownLocation(): Location

    fun startLocation(timeout: Long, onSuccess: ((location: Location) -> Unit?)?, onFailure: ((errorCode: Int, errorMessage: String) -> Unit?)?)

    fun startLocationLoop(interval: Long, onSuccess: ((location: Location) -> Unit?)?, onFailure: ((errorCode: Int, errorMessage: String) -> Unit?)?)

    fun startLocationLoop(interval: Long, ensurePermissions: ((callback: (allPermissionsGranted: Boolean) -> Unit?) -> Unit?)?, onSuccess: ((location: Location) -> Unit?)?, onFailure: ((errorCode: Int, errorMessage: String) -> Unit?)?)

    fun stopLocation()

    fun stopLocationLoop()

}