package com.smart.library.map.location

interface STILocationClient {

    fun getLastKnownLocation(): STLocation

    fun startLocation(timeout: Long, onSuccess: ((location: STLocation) -> Unit?)?, onFailure: ((errorCode: Int, errorMessage: String) -> Unit?)?)

    fun startLocationLoop(interval: Long, onSuccess: ((location: STLocation) -> Unit?)?, onFailure: ((errorCode: Int, errorMessage: String) -> Unit?)?)

    fun startLocationLoop(interval: Long, ensurePermissions: ((callback: (allPermissionsGranted: Boolean) -> Unit?) -> Unit?)?, onSuccess: ((location: STLocation) -> Unit?)?, onFailure: ((errorCode: Int, errorMessage: String) -> Unit?)?)

    fun stopLocation()

    fun stopLocationLoop()

}