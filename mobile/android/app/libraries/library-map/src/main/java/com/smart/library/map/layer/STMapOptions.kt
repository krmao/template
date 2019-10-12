package com.smart.library.map.layer

import android.os.Parcel
import android.os.Parcelable
import com.smart.library.map.model.STLatLng

@Suppress("unused")
data class STMapOptions @JvmOverloads constructor(
        var mapType: Int = MAP_TYPE_NORMAL,
        var isTrafficEnabled: Boolean = false,
        var initCenterLatLng: STLatLng = STMapView.defaultLatLngTianAnMen,
        var initZoomLevel: Float = STMapView.defaultBaiduZoomLevel

) : Parcelable {
    constructor(source: Parcel) : this(
            source.readInt(),
            1 == source.readInt(),
            source.readSerializable() as STLatLng,
            source.readFloat()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(mapType)
        writeInt((if (isTrafficEnabled) 1 else 0))
        writeSerializable(initCenterLatLng)
        writeFloat(initZoomLevel)
    }

    companion object {
        const val MAP_TYPE_NORMAL = 1
        const val MAP_TYPE_SATELLITE = 2

        @JvmField
        val CREATOR: Parcelable.Creator<STMapOptions> = object : Parcelable.Creator<STMapOptions> {
            override fun createFromParcel(source: Parcel): STMapOptions = STMapOptions(source)
            override fun newArray(size: Int): Array<STMapOptions?> = arrayOfNulls(size)
        }
    }
}