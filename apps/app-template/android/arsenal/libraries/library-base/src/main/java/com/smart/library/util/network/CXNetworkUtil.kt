package com.smart.library.util.network

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.support.annotation.RequiresPermission
import android.telephony.TelephonyManager
import android.text.TextUtils
import com.smart.library.base.CXBaseApplication
import java.net.Inet4Address
import java.net.NetworkInterface
import java.util.*

@Suppress("MemberVisibilityCanBePrivate", "unused")
object CXNetworkUtil {

    enum class Type {
        G2, G3, G4, NONE, WIFI;

        var provider = ""

        val isMobile: Boolean
            get() = this == G2 || this == G3 || this == G4

        override fun toString(): String {
            if (isMobile) {
                return if (TextUtils.isEmpty(provider)) "" else provider + ":" + super.toString()
            } else
                return super.toString()
        }
    }

    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    fun isNetworkAvailable(): Boolean = getNetworkInfo()?.isAvailable ?: false

    @JvmStatic
    @JvmOverloads
    fun getNetworkInfo(connectivityManager: ConnectivityManager? = CXBaseApplication.INSTANCE.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?): NetworkInfo? = connectivityManager?.activeNetworkInfo

    @JvmStatic
    fun getIPAddress(useIPv4: Boolean): String? {
        try {
            for (networkInterface in Collections.list(NetworkInterface.getNetworkInterfaces())) {
                for (address in Collections.list(networkInterface.inetAddresses)) {
                    if (!address.isLoopbackAddress) {
                        val hostAddress = address.hostAddress?.toUpperCase()
                        val isIPv4 = address is Inet4Address
                        if (useIPv4) {
                            if (isIPv4) return hostAddress
                        } else {
                            if (!isIPv4) {
                                val endIndex = hostAddress?.indexOf('%') ?: 0
                                return if (endIndex < 0) hostAddress else hostAddress?.substring(0, endIndex)
                            }
                        }
                    }
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return null
    }

    @JvmStatic
    @JvmOverloads
    @RequiresPermission(android.Manifest.permission.READ_PHONE_STATE)
    fun getType(connectivityManager: ConnectivityManager? = CXBaseApplication.INSTANCE.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?): Type {
        var networkType = Type.NONE
        val networkInfo = getNetworkInfo(connectivityManager)
        if (networkInfo != null && networkInfo.isConnected && networkInfo.isAvailable) {
            networkType = updateNetProvider(networkInfo.type)
        }
        return networkType
    }

    @SuppressLint("HardwareIds")
    @RequiresPermission(android.Manifest.permission.READ_PHONE_STATE)
    private fun updateNetProvider(type: Int): Type {
        var networkType = Type.NONE
        val application = CXBaseApplication.INSTANCE
        val tempType = getSwitchedType(type)
        val telephonyManager = application.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager?
        var subscriberId: String? = null
        var netType = 0
        if (telephonyManager != null) {
            subscriberId = telephonyManager.subscriberId
            netType = telephonyManager.networkType
        }
        val provide = getNetworkProvider(subscriberId)
        if (tempType == ConnectivityManager.TYPE_WIFI) {
            networkType = Type.WIFI
        } else if (tempType == ConnectivityManager.TYPE_MOBILE) {
            when (netType) {
                TelephonyManager.NETWORK_TYPE_GPRS, TelephonyManager.NETWORK_TYPE_EDGE, TelephonyManager.NETWORK_TYPE_CDMA, TelephonyManager.NETWORK_TYPE_1xRTT, TelephonyManager.NETWORK_TYPE_IDEN -> networkType = Type.G2
                TelephonyManager.NETWORK_TYPE_UMTS, TelephonyManager.NETWORK_TYPE_EVDO_0, TelephonyManager.NETWORK_TYPE_EVDO_A, TelephonyManager.NETWORK_TYPE_HSDPA, TelephonyManager.NETWORK_TYPE_HSUPA, TelephonyManager.NETWORK_TYPE_HSPA, TelephonyManager.NETWORK_TYPE_EVDO_B, TelephonyManager.NETWORK_TYPE_EHRPD, TelephonyManager.NETWORK_TYPE_HSPAP -> networkType = Type.G3
                TelephonyManager.NETWORK_TYPE_LTE -> networkType = Type.G4
            }
        }
        networkType.provider = provide
        return networkType
    }

    private fun getSwitchedType(type: Int): Int {
        @Suppress("DEPRECATION")
        when (type) {
            ConnectivityManager.TYPE_MOBILE, ConnectivityManager.TYPE_MOBILE_DUN, ConnectivityManager.TYPE_MOBILE_HIPRI, ConnectivityManager.TYPE_MOBILE_MMS, ConnectivityManager.TYPE_MOBILE_SUPL -> return ConnectivityManager.TYPE_MOBILE
            else -> return type
        }
    }

    private fun getNetworkProvider(subscriberId: String?): String {
        var provide = "未知"
        if (subscriberId != null) {
            if (subscriberId.startsWith("46000") || subscriberId.startsWith("46002") || subscriberId.startsWith("46007")) {
                provide = "移动"
            } else if (subscriberId.startsWith("46001")) {
                provide = "联通"
            } else if (subscriberId.startsWith("46003")) {
                provide = "电信"
            }
        }
        return provide
    }
}

