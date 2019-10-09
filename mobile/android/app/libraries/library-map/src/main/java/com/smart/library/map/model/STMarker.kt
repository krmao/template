package com.smart.library.map.model

import com.baidu.mapapi.map.Marker

class STMarker(private val mapType: STMapType, private val marker: Any) {

    fun remove() {
        when (mapType) {
            STMapType.BAIDU -> (marker as? Marker)?.remove()
            else -> Unit
        }
    }

}
