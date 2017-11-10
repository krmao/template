package com.smart.library.bundle

import android.os.Parcel
import android.os.Parcelable

@Suppress("MemberVisibilityCanPrivate", "unused", "UNCHECKED_CAST")
open class HKHybirdModuleConfiguration() : Parcelable {
    var moduleVersion = 1 //只分当前版本与线上最新版本
    var moduleName = ""
    var moduleSchemeUrls = HashMap<String, String>()
    var moduleDownloadUrl = ""

    /**
     * state : 1=加载每一个页面都需要实时检测是否需要更新
     * state : 0=使用native文件(默认策略)
     * 如果是1 , 覆盖 moduleRoutesUpdateStrategy, 否则以 moduleRoutesUpdateStrategy 为主
     */
    var moduleUpdateStrategy = 0
    var moduleRoutesUpdateStrategy = HashMap<String, String>()
    var moduleZipMd5 = ""
    var moduleFilesMd5 = HashMap<String, String>()

    constructor(parcel: Parcel) : this() {
        moduleVersion = parcel.readInt()
        moduleName = parcel.readString()
        moduleSchemeUrls = parcel.readHashMap(null) as HashMap<String, String>
        moduleDownloadUrl = parcel.readString()
        moduleUpdateStrategy = parcel.readInt()
        moduleRoutesUpdateStrategy = parcel.readHashMap(null) as HashMap<String, String>
        moduleZipMd5 = parcel.readString()
        moduleFilesMd5 = parcel.readHashMap(null) as HashMap<String, String>
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(moduleVersion)
        parcel.writeString(moduleName)
        parcel.writeMap(moduleSchemeUrls)
        parcel.writeString(moduleDownloadUrl)
        parcel.writeInt(moduleUpdateStrategy)
        parcel.writeMap(moduleRoutesUpdateStrategy)
        parcel.writeString(moduleZipMd5)
        parcel.writeMap(moduleFilesMd5)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<HKHybirdModuleConfiguration> {
        override fun createFromParcel(parcel: Parcel): HKHybirdModuleConfiguration = HKHybirdModuleConfiguration(parcel)
        override fun newArray(size: Int): Array<HKHybirdModuleConfiguration?> = arrayOfNulls(size)
    }

    override fun toString(): String =
        "HKHybirdModuleConfiguration(moduleVersion=$moduleVersion, moduleName='$moduleName', moduleSchemeUrls=$moduleSchemeUrls, moduleDownloadUrl='$moduleDownloadUrl', moduleUpdateStrategy=$moduleUpdateStrategy, moduleRoutesUpdateStrategy=$moduleRoutesUpdateStrategy, moduleZipMd5='$moduleZipMd5', moduleFilesMd5=$moduleFilesMd5)"
}