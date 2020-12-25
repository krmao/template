@file:Suppress("DEPRECATION")

package com.smart.library.util

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.telephony.TelephonyManager
import android.text.TextUtils
import com.smart.library.STInitializer
import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicBoolean

@Suppress("unused", "DEPRECATION")
object STNetworkUtil {

    private var registerNetworkFlag = AtomicBoolean()
    private val networkChangeListeners: MutableList<NetworkChangeListener> = CopyOnWriteArrayList()

    private const val NETWORK_TYPE_WIFI = "WIFI"
    private const val NETWORK_TYPE_2G = "2G"
    private const val NETWORK_TYPE_3G = "3G"
    private const val NETWORK_TYPE_4G = "4G"
    private const val NETWORK_TYPE_5G = "5G"
    private const val NETWORK_TYPE_Unknown = "Unknown"
    private const val NETWORK_TYPE_None = "None"
    private var networkInfo: NetworkInfo? = null
    private var networkType = -1

    @JvmStatic
    fun getWifiManager(application: Application? = STInitializer.application()): WifiManager? = application?.getSystemService(Context.WIFI_SERVICE) as? WifiManager

    @JvmStatic
    fun getConnectivityManager(application: Application? = STInitializer.application()): ConnectivityManager? = application?.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

    /**
     * 判断是否有网络
     */
    @JvmStatic
    fun checkNetworkState(): Boolean {
        val connectivityManager = getConnectivityManager() ?: return true
        var networkInfo: NetworkInfo? = null
        try {
            networkInfo = connectivityManager.activeNetworkInfo
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return networkInfo != null && networkInfo.isConnected && networkInfo.isAvailable
    }

    @JvmStatic
    fun addNetworkChangeListener(ctNetworkChangeListener: NetworkChangeListener?) {
        registerNetWorkChangeBroadcast()
        if (ctNetworkChangeListener != null) {
            networkChangeListeners.add(ctNetworkChangeListener)
        }
    }

    @JvmStatic
    fun removeNetworkChangeListener(ctNetworkChangeListener: NetworkChangeListener?) {
        if (ctNetworkChangeListener != null) {
            networkChangeListeners.remove(ctNetworkChangeListener)
        }
    }

    private fun registerNetWorkChangeBroadcast() {
        val application = STInitializer.application()
        if (application == null || registerNetworkFlag.get()) {
            return
        }
        try {
            val filter = IntentFilter()
            filter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
            application.registerReceiver(object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    if (ConnectivityManager.CONNECTIVITY_ACTION == intent.action) {
                        try {
                            val connectManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                            val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                            networkInfo = connectManager.activeNetworkInfo
                            networkType = telephonyManager.networkType
                            if (networkType == 0 && networkInfo != null) {
                                //部分机型兼容， RedmiNote7Pro telephonyManager.getNetworkType() 获取到0， Unknown
                                networkType = networkInfo!!.subtype
                            }
                            val networkTypeInfo = getNetworkTypeInfo(networkInfo, networkType)
                            val available = networkInfo != null && networkInfo?.isConnected == true && networkInfo?.isAvailable == true
                            for (networkChangeListener in networkChangeListeners) {
                                networkChangeListener.onChange(networkTypeInfo, available)
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }, filter)
            registerNetworkFlag.set(true)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @JvmStatic
    fun isIPv4Address(inetAddress: InetAddress): Boolean {
        return !inetAddress.isLoopbackAddress && inetAddress is Inet4Address
    }

    /**
     * 获取IP地址
     */
    @JvmStatic
    fun getIPAddress(useIPv4: Boolean): String {
        try {
            for (networkInterface in Collections.list(NetworkInterface.getNetworkInterfaces())) {
                val inetAddressList: List<InetAddress> = Collections.list(networkInterface.inetAddresses)
                for (inetAddress in inetAddressList) {
                    if (!inetAddress.isLoopbackAddress) {
                        val hostAddress = inetAddress.hostAddress.toUpperCase(Locale.getDefault())
                        val isIPv4 = isIPv4Address(inetAddress)
                        if (useIPv4) {
                            if (isIPv4) {
                                return hostAddress
                            }
                        } else {
                            if (!isIPv4) {
                                val index = hostAddress.indexOf('%')
                                return if (index < 0) hostAddress else hostAddress.substring(0, index)
                            }
                        }
                    }
                }
            }
        } catch (ex: Exception) {
        }
        return ""
    }

    /**
     * 获取网络类型
     */
    @JvmStatic
    fun getNetworkTypeInfo(): String {
        if (networkInfo == null) {
            val context: Context? = STInitializer.application()
            val connectivityManager = context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            networkInfo = connectivityManager.activeNetworkInfo
        }
        return getNetworkTypeInfo(networkInfo, getNetworkType())
    }

    @JvmStatic
    fun getNetworkType(): Int {
        if (networkType == -1) {
            val context: Context? = STInitializer.application()
            val telephonyManager = context!!.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            networkType = telephonyManager.networkType
            registerNetWorkChangeBroadcast()
        }
        return networkType
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

    /**
     * 获取网络类型
     */
    @JvmStatic
    fun getNetworkTypeInfo(networkInfo: NetworkInfo?, networkType: Int): String {
        var netTypeStr = NETWORK_TYPE_Unknown
        if (networkInfo == null) {
            return netTypeStr
        }
        try {
            if (networkInfo.isConnected && networkInfo.isAvailable) {
                val type = networkInfo.type
                val tempType = getSwitchedType(type)
                if (tempType == ConnectivityManager.TYPE_WIFI) {
                    netTypeStr = NETWORK_TYPE_WIFI
                } else if (tempType == ConnectivityManager.TYPE_MOBILE) {
                    when (networkType) {
                        TelephonyManager.NETWORK_TYPE_GPRS, TelephonyManager.NETWORK_TYPE_EDGE, TelephonyManager.NETWORK_TYPE_CDMA, TelephonyManager.NETWORK_TYPE_1xRTT, TelephonyManager.NETWORK_TYPE_IDEN -> netTypeStr = NETWORK_TYPE_2G
                        TelephonyManager.NETWORK_TYPE_UMTS, TelephonyManager.NETWORK_TYPE_EVDO_0, TelephonyManager.NETWORK_TYPE_EVDO_A, TelephonyManager.NETWORK_TYPE_HSDPA, TelephonyManager.NETWORK_TYPE_HSUPA, TelephonyManager.NETWORK_TYPE_HSPA, TelephonyManager.NETWORK_TYPE_EVDO_B, TelephonyManager.NETWORK_TYPE_EHRPD, TelephonyManager.NETWORK_TYPE_HSPAP -> netTypeStr = NETWORK_TYPE_3G
                        TelephonyManager.NETWORK_TYPE_LTE, TelephonyManager.NETWORK_TYPE_IWLAN, 19 -> netTypeStr = NETWORK_TYPE_4G
                        20 -> netTypeStr = NETWORK_TYPE_5G
                    }
                    if (networkType > 20) {
                        netTypeStr = NETWORK_TYPE_5G
                    }
                }
            } else {
                netTypeStr = NETWORK_TYPE_None
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return netTypeStr
    }

    @JvmStatic
    fun getCarrierName(): String {
        return networkProvider()
    }

    @JvmStatic
    fun isChinaCarrier(): Boolean {
        return !TextUtils.isEmpty(getCarrierName())
    }

    private fun getSwitchedType(type: Int): Int {
        return when (type) {
            ConnectivityManager.TYPE_MOBILE, ConnectivityManager.TYPE_MOBILE_DUN, ConnectivityManager.TYPE_MOBILE_HIPRI, ConnectivityManager.TYPE_MOBILE_MMS, ConnectivityManager.TYPE_MOBILE_SUPL -> ConnectivityManager.TYPE_MOBILE
            else -> type
        }
    }// 中国电信// 中国移动

    @JvmStatic
    fun getTelephonyService(application: Application? = STInitializer.application()) = application?.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager

    // 中国移动
    @JvmStatic
    fun getNetworkProviderIndex(): Int {
        val telephonyManager = getTelephonyService()
        val operator = telephonyManager?.networkOperator
        var provide = -1
        if (operator != null && operator.length > 3) {
            val mnc = operator.substring(3)
            if (mnc.equals("00", ignoreCase = true) // 中国移动
                || mnc.equals("02", ignoreCase = true)
                || mnc.equals("08", ignoreCase = true)
                || mnc.equals("07", ignoreCase = true)
            ) {
                provide = 1
            } else if (mnc.equals("01", ignoreCase = true) // 中国移动
                || mnc.equals("06", ignoreCase = true)
                || mnc.equals("09", ignoreCase = true)
            ) {
                provide = 2
            } else if (mnc.equals("03", ignoreCase = true) // 中国电信
                || mnc.equals("05", ignoreCase = true)
                || mnc.equals("11", ignoreCase = true)
            ) {
                provide = 3
            }
        }
        return provide
    }

    @JvmStatic
    private fun networkProvider(): String {
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

    interface NetworkChangeListener {
        fun onChange(networkType: String?, isAvailable: Boolean)
    }
}