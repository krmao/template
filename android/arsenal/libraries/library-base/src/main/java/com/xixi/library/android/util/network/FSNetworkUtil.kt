package com.xixi.library.android.util.network

import android.content.Context
import android.net.ConnectivityManager
import android.telephony.TelephonyManager
import android.text.TextUtils
import com.xixi.library.android.base.CXBaseApplication
import java.net.Inet4Address
import java.net.NetworkInterface
import java.util.*

object CXNetworkUtil {

    fun isNetworkAvailable(): Boolean {
        var isNetworkAvailable = false
        val application = CXBaseApplication.INSTANCE
        val conManager = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val networkInfo = conManager?.activeNetworkInfo
        if (networkInfo != null && networkInfo.isConnectedOrConnecting) {
            isNetworkAvailable = true
        }
        return isNetworkAvailable
    }

    fun getIPAddress(useIPv4: Boolean): String {
        try {
            val interfaces = Collections.list(NetworkInterface.getNetworkInterfaces())
            for (intf in interfaces) {
                val addrs = Collections.list(intf.inetAddresses)
                for (addr in addrs) {
                    if (!addr.isLoopbackAddress) {
                        val sAddr = addr.hostAddress.toUpperCase()
                        val isIPv4 = addr is Inet4Address
                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr
                        } else {
                            if (!isIPv4) {
                                val delim = sAddr.indexOf('%')
                                return if (delim < 0) sAddr else sAddr.substring(0, delim)
                            }
                        }
                    }
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        return ""
    }

    fun getNetType(): MNetworkType {
        return getNetType(CXBaseApplication.INSTANCE.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
    }

    fun getNetType(connectivityManager: ConnectivityManager?): MNetworkType {
        var networkType = MNetworkType.NONE
        if (connectivityManager != null) {
            val networkInfo = connectivityManager.activeNetworkInfo
            if (networkInfo != null && networkInfo.isConnected && networkInfo.isAvailable) {
                networkType = updateNetProvider(networkInfo.type)
            }
        }
        return networkType
    }

    enum class MNetworkType {
        G2, G3, G4, NONE, WIFI;

        //提供商
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


    private fun updateNetProvider(type: Int): MNetworkType {
        var networkType = MNetworkType.NONE
        val application = CXBaseApplication.INSTANCE
        val tempType = getSwitchedType(type)
        val telephonyManager = application.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager?
        var IMSI: String? = null
        var netType = 0
        if (telephonyManager != null) {
            IMSI = telephonyManager.subscriberId
            netType = telephonyManager.networkType
        }
        val provide = getNetworkProvider(IMSI)

        if (tempType == ConnectivityManager.TYPE_WIFI) {
            networkType = MNetworkType.WIFI
        } else if (tempType == ConnectivityManager.TYPE_MOBILE) {
            when (netType) {
                TelephonyManager.NETWORK_TYPE_GPRS, TelephonyManager.NETWORK_TYPE_EDGE, TelephonyManager.NETWORK_TYPE_CDMA, TelephonyManager.NETWORK_TYPE_1xRTT, TelephonyManager.NETWORK_TYPE_IDEN -> networkType = MNetworkType.G2
                TelephonyManager.NETWORK_TYPE_UMTS, TelephonyManager.NETWORK_TYPE_EVDO_0, TelephonyManager.NETWORK_TYPE_EVDO_A, TelephonyManager.NETWORK_TYPE_HSDPA, TelephonyManager.NETWORK_TYPE_HSUPA, TelephonyManager.NETWORK_TYPE_HSPA, TelephonyManager.NETWORK_TYPE_EVDO_B, TelephonyManager.NETWORK_TYPE_EHRPD, TelephonyManager.NETWORK_TYPE_HSPAP -> networkType = MNetworkType.G3
                TelephonyManager.NETWORK_TYPE_LTE -> networkType = MNetworkType.G4
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

    private fun getNetworkProvider(IMSI: String?): String {
        var provide = "未知"
        if (IMSI != null) {
            if (IMSI.startsWith("46000") || IMSI.startsWith("46002") || IMSI.startsWith("46007")) {
                provide = "移动"
            } else if (IMSI.startsWith("46001")) {
                provide = "联通"
            } else if (IMSI.startsWith("46003")) {
                provide = "电信"
            }
        }
        return provide
    }
}

