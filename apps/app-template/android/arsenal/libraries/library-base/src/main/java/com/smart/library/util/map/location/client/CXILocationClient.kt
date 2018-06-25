package com.saike.library.util.location.client

import android.location.Location

interface CXILocationClient {

    fun startLocation(timeout: Long, onSuccess: ((location: Location) -> Unit?)?, onFailure: ((errorCode: Int, errorMessage: String) -> Unit?)?)

    fun startLocationLoop(interval: Long, onSuccess: ((location: Location) -> Unit?)?, onFailure: ((errorCode: Int, errorMessage: String) -> Unit?)?)

    fun stopLocation()

    fun stopLocationLoop()

}