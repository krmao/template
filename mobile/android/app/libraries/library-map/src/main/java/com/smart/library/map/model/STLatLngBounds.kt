package com.smart.library.map.model

data class STLatLngBounds(val swLatLng: STLatLng, val neLatLng: STLatLng) {

    operator fun contains(otherLatLng: STLatLng?): Boolean {
        if (otherLatLng == null) {
            return false
        } else {
            return otherLatLng.latitude in this.swLatLng.latitude..this.neLatLng.latitude
                    && otherLatLng.longitude >= this.swLatLng.longitude
                    && otherLatLng.longitude <= this.neLatLng.longitude
        }
    }

    fun getCenter(): STLatLng {
        val latitude: Double = (this.neLatLng.latitude - this.swLatLng.latitude) / 2.0 + this.swLatLng.latitude
        val longitude: Double = (this.neLatLng.longitude - this.swLatLng.longitude) / 2.0 + this.swLatLng.longitude
        return STLatLng(latitude, longitude, swLatLng.type)
    }
    
}
