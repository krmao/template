@file:Suppress("DEPRECATION")

package com.smart.library.util.network

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import android.telephony.TelephonyManager
import com.smart.library.STInitializer
import com.smart.library.util.STWifiUtil
import java.net.Inet4Address
import java.net.NetworkInterface
import java.util.*

@Suppress("MemberVisibilityCanBePrivate", "unused", "DEPRECATION")
object STNetworkUtil {
    @JvmStatic
    fun isNetworkAvailable(): Boolean = getNetworkInfo()?.isAvailable ?: false

    @JvmStatic
    fun isInternetAvailable(application: Application? = STInitializer.application()): Boolean {
        var result = false
        val connectivityManager: ConnectivityManager? = STWifiUtil.getConnectivityManager(application = application)
        if (connectivityManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val network: Network = connectivityManager.activeNetwork ?: return false
                val networkCapabilities: NetworkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
                result = when {
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            } else {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }

                }
            }
        }
        return result
    }

    /**
     * getActiveNetwork has been deprecated in API 29, so this is the best solution
     *
     * @param type ConnectivityManager.TYPE_WIFI
     */
    @JvmStatic
    @JvmOverloads
    fun getNetworkInfo(application: Application? = STInitializer.application(), connectivityManager: ConnectivityManager? = application?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?): NetworkInfo? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network: Network? = connectivityManager?.activeNetwork
            connectivityManager?.getNetworkInfo(network)
        } else {
            connectivityManager?.activeNetworkInfo
        }
    }

    /**
     * getActiveNetwork has been deprecated in API 29, so this is the best solution
     *
     * @param type ConnectivityManager.TYPE_WIFI
     */
    @JvmStatic
    @JvmOverloads
    fun getNetworkInfoByType(application: Application? = STInitializer.application(), connectivityManager: ConnectivityManager? = application?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?, type: Int? = null): NetworkInfo? {
        return connectivityManager?.allNetworks?.mapNotNull {
            val networkInfo: NetworkInfo? = connectivityManager.getNetworkInfo(it)
            if (networkInfo != null && (type == null || (networkInfo.type == type))) networkInfo else null
        }?.firstOrNull()
    }


    @JvmStatic
    fun getIPAddress(useIPv4: Boolean): String? {
        try {
            for (networkInterface in Collections.list(NetworkInterface.getNetworkInterfaces())) {
                for (address in Collections.list(networkInterface.inetAddresses)) {
                    if (!address.isLoopbackAddress) {
                        val hostAddress = address.hostAddress?.toUpperCase(Locale.getDefault())
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
    fun getNetworkType(application: Application? = STInitializer.application()): Int {
        return getTelephonyService(application)?.networkType ?: TelephonyManager.NETWORK_TYPE_UNKNOWN
    }

    @JvmStatic
    fun getTelephonyService(application: Application? = STInitializer.application()) = application?.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager

    private fun getNetworkProviderIndex(application: Application? = STInitializer.application()): Int {
        val telephonyManager = getTelephonyService(application)
        val operator = telephonyManager?.networkOperator
        var provide = -1
        if (operator != null && operator.length > 3) {
            val mnc = operator.substring(3)
            if (!mnc.equals("00", ignoreCase = true) && !mnc.equals("02", ignoreCase = true) && !mnc.equals("08", ignoreCase = true) && !mnc.equals("07", ignoreCase = true)) {
                if (!mnc.equals("01", ignoreCase = true) && !mnc.equals("06", ignoreCase = true) && !mnc.equals("09", ignoreCase = true)) {
                    if (mnc.equals("03", ignoreCase = true) || mnc.equals("05", ignoreCase = true) || mnc.equals("11", ignoreCase = true)) {
                        provide = 3
                    }
                } else {
                    provide = 2
                }
            } else {
                provide = 1
            }
        }
        return provide
    }

    @JvmStatic
    fun getNetworkProvider(): String? {
        var provide = ""
        when (getNetworkProviderIndex()) {
            1 -> {
                provide = "移动"
            }
            2 -> {
                provide = "联通"
            }
            3 -> {
                provide = "电信"
            }
        }
        return provide
    }
}

