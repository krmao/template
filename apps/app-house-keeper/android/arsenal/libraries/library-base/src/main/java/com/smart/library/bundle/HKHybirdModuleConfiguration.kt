package com.smart.library.bundle

import android.os.Parcel
import android.os.Parcelable

@Suppress("MemberVisibilityCanPrivate", "unused", "UNCHECKED_CAST")
open class HKHybirdModuleConfiguration() : Parcelable {
    var moduleVersion = "" //只分当前版本与线上最新版本
    var moduleName = ""
    var moduleMainUrl = HashMap<String, String>()
    var moduleScriptUrl = HashMap<String, String>()
    var moduleConfigUrl = ""
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
        moduleVersion = parcel.readString()
        moduleName = parcel.readString()
        moduleScriptUrl = parcel.readHashMap(null) as HashMap<String, String>
        moduleConfigUrl = parcel.readString()
        moduleDownloadUrl = parcel.readString()
        moduleUpdateStrategy = parcel.readInt()
        moduleRoutesUpdateStrategy = parcel.readHashMap(null) as HashMap<String, String>
        moduleZipMd5 = parcel.readString()
        moduleFilesMd5 = parcel.readHashMap(null) as HashMap<String, String>
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(moduleVersion)
        parcel.writeString(moduleName)
        parcel.writeMap(moduleScriptUrl)
        parcel.writeString(moduleConfigUrl)
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

    override fun equals(other: Any?): Boolean = other is HKHybirdModuleConfiguration && moduleVersion == other.moduleVersion

    override fun hashCode(): Int {
        var result = moduleVersion.hashCode()
        result = 31 * result + moduleName.hashCode()
        result = 31 * result + moduleMainUrl.hashCode()
        result = 31 * result + moduleScriptUrl.hashCode()
        result = 31 * result + moduleConfigUrl.hashCode()
        result = 31 * result + moduleDownloadUrl.hashCode()
        result = 31 * result + moduleUpdateStrategy
        result = 31 * result + moduleRoutesUpdateStrategy.hashCode()
        result = 31 * result + moduleZipMd5.hashCode()
        result = 31 * result + moduleFilesMd5.hashCode()
        return result
    }

    override fun toString(): String = "HKHybirdModuleConfiguration(moduleVersion=$moduleVersion, moduleName='$moduleName', moduleScriptUrl=$moduleScriptUrl, moduleConfigUrl='$moduleConfigUrl', moduleDownloadUrl='$moduleDownloadUrl', moduleUpdateStrategy=$moduleUpdateStrategy, moduleRoutesUpdateStrategy=$moduleRoutesUpdateStrategy, moduleZipMd5='$moduleZipMd5', moduleFilesMd5=$moduleFilesMd5)"
}
