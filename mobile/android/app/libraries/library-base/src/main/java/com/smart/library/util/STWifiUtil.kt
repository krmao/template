package com.smart.library.util

import android.Manifest.permission
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.WIFI_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.MacAddress
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.*
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import com.smart.library.STInitializer
import com.smart.library.util.rx.permission.RxPermissions
import java.util.*

/**
 * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
 * <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
 * <uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
 * <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
 */
@Suppress("unused", "MemberVisibilityCanBePrivate", "DEPRECATION")
object STWifiUtil {
    const val TAG = "[wifi]"

    const val SECURITY_WEP: String = "WEP"
    const val SECURITY_PSK: String = "PSK"
    const val SECURITY_EAP: String = "EAP"
    const val SECURITY_NONE: String = "OPEN"

    const val LOCATION_AVAILABLE = 1000
    const val LOCATION_NONE = 1111
    const val LOCATION_DISABLED = 1112

    // android9.0以上需要申请定位权限
    // android10.0需要申请新添加的隐私权限ACCESS_FINE_LOCATION详情见android官方10.0重大隐私权变更，如果还需要后台获取或者使用wifi api则还需要申请后台使用定位权限ACCESS_BACKGROUND_LOCATION
    // https://developer.android.google.cn/about/versions/10/privacy/
    @JvmStatic
    fun ensurePermissions(activity: Activity?, callback: (allPermissionsGranted: Boolean) -> Unit) {
        RxPermissions.ensurePermissions(activity, callback, permission.ACCESS_WIFI_STATE, permission.CHANGE_WIFI_STATE, permission.ACCESS_FINE_LOCATION)
    }


    private fun registerReceiver(context: Context?, receiver: BroadcastReceiver?, filter: IntentFilter) {
        STLogUtil.w(TAG, "registerReceiver ${receiver?.javaClass?.name}")
        if (receiver != null) {
            try {
                context?.registerReceiver(receiver, filter)
            } catch (ignored: Exception) {
            }
        }
    }

    private fun unregisterReceiver(context: Context?, receiver: BroadcastReceiver?) {
        STLogUtil.w(TAG, "unregisterReceiver ${receiver?.javaClass?.name}")
        if (receiver != null) {
            try {
                context?.unregisterReceiver(receiver)
            } catch (ignored: IllegalArgumentException) {
            }
        }
    }

    private val wifiStateCallback: WifiStateCallback = object : WifiStateCallback {
        @SuppressLint("MissingPermission")
        override fun onWifiEnabled() {
            STLogUtil.w(TAG, "wifiStateCallback->onWifiEnabled")
            unregisterReceiver(STInitializer.application()?.applicationContext, wifiStateReceiver)
            scanWifi()
        }
    }
    private val wifiScanCallback: WifiScanCallback = object : WifiScanCallback {
        @SuppressLint("MissingPermission")
        override fun onScanResultsReady() {
            STLogUtil.w(TAG, "wifiScanCallback->onScanResultsReady")
            unregisterReceiver(STInitializer.application()?.applicationContext, wifiStateReceiver)
            val scanResultList: MutableList<ScanResult> = getWifiManager()?.scanResults ?: mutableListOf()
            scanResultsSetListener.forEach {
                it.onScanResults(scanResultList)
            }
            scanResultsSetListener.clear()
        }
    }

    private val scanResultsSetListener: MutableSet<ScanResultsListener> = mutableSetOf()
    private val wifiStateSetListener: MutableSet<WifiStateListener> = mutableSetOf()

    private val wifiScanReceiver: WifiScanReceiver by lazy { WifiScanReceiver(wifiScanCallback) }
    private val wifiStateReceiver: WifiStateReceiver by lazy { WifiStateReceiver(wifiStateCallback) }

    @JvmStatic
    fun checkLocationAvailability(context: Context?): Int {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val packageManager = context?.packageManager
            if (packageManager?.hasSystemFeature(PackageManager.FEATURE_LOCATION) == true) {
                if (!isLocationEnabled(context)) {
                    return LOCATION_DISABLED
                }
            } else {
                return LOCATION_NONE
            }
        }
        return LOCATION_AVAILABLE
    }

    @JvmStatic
    fun checkIfNeedOpenLocationSettings(application: Application? = STInitializer.application()): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            if (checkLocationAvailability(application) == LOCATION_DISABLED) {
                openSystemLocationSettings(application)
                return true
            }
        }
        return false
    }

    private fun isLocationEnabled(context: Context?): Boolean {
        val locationManager: LocationManager? = context?.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
        return locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER) == true || locationManager?.isProviderEnabled(LocationManager.NETWORK_PROVIDER) == true
    }

    @JvmStatic
    fun isAndroidQOrLater(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

    /**
     * android.util.AndroidRuntimeException: Calling startActivity() from outside of an Activity  context requires the FLAG_ACTIVITY_NEW_TASK flag.
     * @see {@link https://stackoverflow.com/questions/25678216/android-internet-connectivity-change-listener}
     */
    @JvmStatic
    fun openSystemWifiSettings(context: Context?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            context?.startActivity(Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY).apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) })
        } else {
            context?.startActivity(Intent(Settings.ACTION_WIFI_SETTINGS).apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) })
        }
    }

    /**
     * android.util.AndroidRuntimeException: Calling startActivity() from outside of an Activity  context requires the FLAG_ACTIVITY_NEW_TASK flag.
     */
    @JvmStatic
    fun openSystemLocationSettings(context: Context?) = context?.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) })

    @JvmStatic
    fun getWifiManager(application: Application? = STInitializer.application()): WifiManager? = application?.getSystemService(WIFI_SERVICE) as? WifiManager

    @JvmStatic
    fun getConnectivityManager(application: Application? = STInitializer.application()): ConnectivityManager? = application?.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

    @JvmStatic
    @RequiresPermission(allOf = [permission.ACCESS_NETWORK_STATE, permission.ACCESS_WIFI_STATE, permission.CHANGE_WIFI_STATE, permission.ACCESS_FINE_LOCATION])
    fun scanWifi(application: Application? = STInitializer.application(), wifiManager: WifiManager? = getWifiManager(application), scanResultsListener: ScanResultsListener? = null) {
        unregisterReceiver(STInitializer.application()?.applicationContext, wifiStateReceiver)
        STLogUtil.w(TAG, "scanWifi startScan ...")
        if (wifiManager?.startScan() == true) {
            STLogUtil.w(TAG, "scanWifi startScan success")
            if (scanResultsListener != null) scanResultsSetListener.add(scanResultsListener)
            registerReceiver(STInitializer.application(), wifiScanReceiver, IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
        } else {
            STLogUtil.w(TAG, "scanWifi startScan failure")
            scanResultsListener?.onScanResults(mutableListOf())
        }
    }

    @JvmStatic
    fun isWifiEnabled(application: Application? = STInitializer.application(), wifiManager: WifiManager? = getWifiManager(application)): Boolean {
        return wifiManager?.isWifiEnabled ?: false
    }

    /**
     * @deprecated Starting with Build.VERSION_CODES#Q, applications are not allowed to
     */
    @Suppress("DEPRECATION")
    @JvmStatic
    @JvmOverloads
    @Deprecated("Starting with Build.VERSION_CODES#Q, applications are not allowed to")
    @RequiresPermission(allOf = [permission.ACCESS_NETWORK_STATE, permission.ACCESS_WIFI_STATE, permission.CHANGE_WIFI_STATE, permission.ACCESS_FINE_LOCATION])
    fun setWifiEnabledPreAndroidQ(application: Application? = STInitializer.application(), enable: Boolean, wifiManager: WifiManager? = getWifiManager(application), wifiStateListener: WifiStateListener? = null) {
        if (!isAndroidQOrLater()) {
            wifiManager ?: return
            if (wifiManager.isWifiEnabled) {
                wifiStateCallback.onWifiEnabled()
            } else {
                unregisterReceiver(STInitializer.application()?.applicationContext, wifiStateReceiver)
                if (wifiManager.setWifiEnabled(true)) {
                    registerReceiver(application?.applicationContext, wifiStateReceiver, IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION))
                }
            }
        }
    }

    @JvmStatic
    @JvmOverloads
    @RequiresPermission(allOf = [permission.ACCESS_NETWORK_STATE, permission.ACCESS_WIFI_STATE, permission.CHANGE_WIFI_STATE, permission.ACCESS_FINE_LOCATION])
    fun setWifiEnabled(application: Application? = STInitializer.application(), enable: Boolean, wifiManager: WifiManager? = getWifiManager(application), wifiStateListener: WifiStateListener? = null) {
        if (!isAndroidQOrLater()) {
            setWifiEnabledPreAndroidQ(application, enable, wifiManager, wifiStateListener)
        } else {
            openSystemWifiSettings(application)
        }
    }

    @Suppress("DEPRECATION")
    @JvmStatic
    @JvmOverloads
    fun disconnectWifi(application: Application? = STInitializer.application(), wifiManager: WifiManager? = getWifiManager(application), networkId: Int? = null, ssid: String? = null, connectivityManager: ConnectivityManager? = getConnectivityManager(application), networkCallback: ConnectivityManager.NetworkCallback? = null, removeWifi: Boolean = true) {
        if (!isAndroidQOrLater()) {
            wifiManager?.disconnect()
            if (removeWifi && networkId != null) {
                wifiManager?.removeNetwork(networkId)
            }
        } else {
            if (networkCallback != null) {
                disconnectWifiAndroidQ(application, connectivityManager = connectivityManager, networkCallback = networkCallback)
            }
        }
        if (removeWifi) {
            removeWifi(wifiManager, ssid)
        }
    }

    @JvmStatic
    fun removeWifi(wifiManager: WifiManager?, ssid: String? = null): Boolean {
        val wifiConfiguration: WifiConfiguration? = getWifiConfiguration(wifiManager, ssid)
        return cleanPreviousConfiguration(wifiManager, wifiConfiguration)
    }

    @JvmStatic
    @RequiresPermission(allOf = [permission.ACCESS_FINE_LOCATION, permission.ACCESS_WIFI_STATE])
    private fun cleanPreviousConfiguration(wifiManager: WifiManager?, scanResult: ScanResult): Boolean {
        wifiManager ?: return false
        //On Android 6.0 (API level 23) and above if my app did not create the configuration in the first place, it can not remove it either.
        val config: WifiConfiguration = getWifiConfiguration(wifiManager, scanResult) ?: return true
        if (wifiManager.removeNetwork(config.networkId)) {
            wifiManager.saveConfiguration()
            return true
        }
        return false
    }

    @JvmStatic
    private fun cleanPreviousConfiguration(wifiManager: WifiManager?, config: WifiConfiguration?): Boolean {
        //On Android 6.0 (API level 23) and above if my app did not create the configuration in the first place, it can not remove it either.
        wifiManager ?: return false
        config ?: return true
        if (wifiManager.removeNetwork(config.networkId)) {
            wifiManager.saveConfiguration()
            return true
        }
        return false
    }

    /**
     * @return networkId
     */
    @Suppress("DEPRECATION")
    @JvmStatic
    @RequiresPermission(allOf = [permission.ACCESS_NETWORK_STATE, permission.ACCESS_WIFI_STATE, permission.CHANGE_WIFI_STATE, permission.ACCESS_FINE_LOCATION])
    fun connectWifi(application: Application? = STInitializer.application(), wifiManager: WifiManager? = getWifiManager(application), config: WifiConfiguration? = null, networkRequest: NetworkRequest? = null, networkCallback: ConnectivityManager.NetworkCallback? = null): Int? {
        return if (!isAndroidQOrLater()) {
            wifiManager?.disconnect()
            val networkId: Int? = wifiManager?.addNetwork(config)
            wifiManager?.saveConfiguration()
            if (networkId != null) wifiManager.enableNetwork(networkId, true)
            wifiManager?.reconnect()
            networkId
        } else {
            connectWifiAndroidQ(application, networkRequest = networkRequest, networkCallback = networkCallback)
            null
        }
    }

    /**
     * https://stackoverflow.com/questions/58769623/android-10-api-29-how-to-connect-the-phone-to-a-configured-network
     */
    @RequiresApi(Build.VERSION_CODES.Q)
    @JvmStatic
    @RequiresPermission(allOf = [permission.ACCESS_NETWORK_STATE, permission.ACCESS_WIFI_STATE, permission.CHANGE_WIFI_STATE, permission.ACCESS_FINE_LOCATION])
    fun connectWifiAndroidQ(application: Application? = STInitializer.application(), connectivityManager: ConnectivityManager? = getConnectivityManager(application), networkRequest: NetworkRequest?, networkCallback: ConnectivityManager.NetworkCallback?) {
        if (isAndroidQOrLater() && networkRequest != null && networkCallback != null) {
            connectivityManager?.requestNetwork(networkRequest, networkCallback)
        }
    }

    /**
     * https://stackoverflow.com/questions/58769623/android-10-api-29-how-to-connect-the-phone-to-a-configured-network
     */
    @JvmStatic
    @JvmOverloads
    fun disconnectWifiAndroidQ(application: Application? = STInitializer.application(), connectivityManager: ConnectivityManager? = getConnectivityManager(application), networkCallback: ConnectivityManager.NetworkCallback) {
        if (isAndroidQOrLater()) {
            connectivityManager?.unregisterNetworkCallback(networkCallback)
        }
    }

    @JvmStatic
    @RequiresApi(Build.VERSION_CODES.Q)
    @RequiresPermission(allOf = [permission.ACCESS_NETWORK_STATE, permission.ACCESS_WIFI_STATE, permission.CHANGE_WIFI_STATE, permission.ACCESS_FINE_LOCATION])
    fun addNetworkSuggestionsAndroidQ(application: Application? = STInitializer.application(), wifiManager: WifiManager? = getWifiManager(application), networkSuggestions: List<WifiNetworkSuggestion?>): Int {
        return if (isAndroidQOrLater()) wifiManager?.addNetworkSuggestions(networkSuggestions) ?: -1 else -1
    }

    @JvmStatic
    @RequiresApi(Build.VERSION_CODES.Q)
    @RequiresPermission(allOf = [permission.ACCESS_NETWORK_STATE, permission.ACCESS_WIFI_STATE, permission.CHANGE_WIFI_STATE, permission.ACCESS_FINE_LOCATION])
    fun removeNetworkSuggestionsAndroidQ(application: Application? = STInitializer.application(), wifiManager: WifiManager? = getWifiManager(application), networkSuggestions: List<WifiNetworkSuggestion?>): Int {
        return if (isAndroidQOrLater()) wifiManager?.removeNetworkSuggestions(networkSuggestions) ?: -1 else -1
    }

    @JvmStatic
    @RequiresApi(Build.VERSION_CODES.Q)
    fun createNetworkRequestBuilderAndroidQ(ssid: String, bssid: String, passwordString: String): NetworkRequest.Builder {
        return NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI) //创建的是WIFI网络
            .addCapability(NetworkCapabilities.NET_CAPABILITY_NOT_RESTRICTED) //网络不受限
            .addCapability(NetworkCapabilities.NET_CAPABILITY_TRUSTED) //信任网络，增加这个连个参数让设备连接wifi之后还联网
            .removeCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .setNetworkSpecifier(
                WifiNetworkSpecifier.Builder()
                    .setSsid(ssid)
                    .setBssid(MacAddress.fromString(bssid))
                    .setWpa2Passphrase(passwordString)
                    .build()
            )
    }

    @Suppress("DEPRECATION")
    @JvmStatic
    @RequiresPermission(allOf = [permission.ACCESS_FINE_LOCATION, permission.ACCESS_WIFI_STATE])
    fun createWifiConfigurationPreAndroidQ(application: Application? = STInitializer.application(), wifiManager: WifiManager? = getWifiManager(application), scanResult: ScanResult, password: String): WifiConfiguration {
        val security: String = getSecurity(scanResult)
        val config: WifiConfiguration = getWifiConfiguration(wifiManager, scanResult) ?: WifiConfiguration()
        config.SSID = convertSsidToQuotedString(scanResult.SSID)
        config.BSSID = scanResult.BSSID
        setupSecurity(config, security, password)
        return config
    }

    /**
     * @param signalStrength
     * @see getSignalStrength
     */
    @JvmStatic
    fun getSignalStrengthDesc(signalStrength: Int): String? {
        return when (signalStrength) {
            2 -> "网络质量好"
            1 -> "网络质量一般"
            0 -> "网络质量不佳"
            else -> "网络质量未知"
        }
    }

    /**
     * @param scanResult
     * @see getSignalStrength
     */
    @JvmStatic
    fun getSignalStrengthDescByScanResult(scanResult: ScanResult): String? = getSignalStrengthDesc((getSignalStrength(scanResult)))

    /**
     * 0-2
     * @param rssi scanResult.level, The detected signal level in dBm, also known as the RSSI.
     * @see {@link https://github.com/paladinzh/decompile-hw/blob/4c3efd95f3e997b44dd4ceec506de6164192eca3/decompile/app/SystemUI/src/main/res/values-zh-rCN/strings.xml}
     */
    @JvmStatic
    fun getSignalStrength(rssi: Int): Int = WifiManager.calculateSignalLevel(rssi, 3)

    @JvmStatic
    fun getSignalStrength(scanResult: ScanResult): Int = WifiManager.calculateSignalLevel(scanResult.level, 3)

    @JvmStatic
    fun getSecurity(scanResult: ScanResult, includeWPS: Boolean = true): String = getSecurity(scanResult.capabilities, includeWPS)

    @JvmStatic
    @JvmOverloads
    fun getSecurity(capabilities: String, includeWPS: Boolean = true): String {
        return (when {
            capabilities.contains(SECURITY_WEP) -> SECURITY_WEP
            capabilities.contains(SECURITY_PSK) -> SECURITY_PSK
            capabilities.contains(SECURITY_EAP) -> SECURITY_EAP
            else -> SECURITY_NONE
        }) + (if (includeWPS && canUseWPS(capabilities)) "(CAN USE WPS)" else "")
    }

    @Suppress("DEPRECATION")
    @JvmStatic
    fun getSecurity(config: WifiConfiguration): String {
        var security = SECURITY_NONE
        val securities: MutableCollection<String> = ArrayList()
        if (config.allowedKeyManagement[WifiConfiguration.KeyMgmt.NONE]) {
            // If we never set group ciphers, wpa_supplicant puts all of them.
            // For open, we don't set group ciphers.
            // For WEP, we specifically only set WEP40 and WEP104, so CCMP
            // and TKIP should not be there.
            security = if (config.wepKeys[0] != null) {
                SECURITY_WEP
            } else {
                SECURITY_NONE
            }
            securities.add(security)
        }
        if (config.allowedKeyManagement[WifiConfiguration.KeyMgmt.WPA_EAP] ||
            config.allowedKeyManagement[WifiConfiguration.KeyMgmt.IEEE8021X]
        ) {
            security = SECURITY_EAP
            securities.add(security)
        }
        if (config.allowedKeyManagement[WifiConfiguration.KeyMgmt.WPA_PSK]) {
            security = SECURITY_PSK
            securities.add(security)
        }
        return security
    }

    @JvmStatic
    fun canUseWPS(capabilities: String): Boolean = capabilities.contains("WPS")

    /**
     * Fill in the security fields of WifiConfiguration config.
     *
     * @param config   The object to fill.
     * @param security If is OPEN, password is ignored.
     * @param password Password of the network if security is not OPEN.
     */
    @JvmStatic
    @Suppress("DEPRECATION")
    @RequiresPermission(allOf = [permission.ACCESS_FINE_LOCATION, permission.ACCESS_WIFI_STATE])
    fun setupSecurity(config: WifiConfiguration, security: String, password: String) {
        config.allowedAuthAlgorithms.clear()
        config.allowedGroupCiphers.clear()
        config.allowedKeyManagement.clear()
        config.allowedPairwiseCiphers.clear()
        config.allowedProtocols.clear()
        when (security) {
            SECURITY_NONE -> {
                config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE)
                config.allowedProtocols.set(WifiConfiguration.Protocol.RSN)
                config.allowedProtocols.set(WifiConfiguration.Protocol.WPA)
                config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP)
                config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP)
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40)
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104)
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP)
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP)
            }
            SECURITY_WEP -> {
                config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE)
                config.allowedProtocols.set(WifiConfiguration.Protocol.RSN)
                config.allowedProtocols.set(WifiConfiguration.Protocol.WPA)
                config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN)
                config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED)
                config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP)
                config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP)
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40)
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104)

                fun isHexWepKey(wepKey: String?): Boolean {
                    val passwordLen = wepKey?.length ?: 0
                    return (passwordLen == 10 || passwordLen == 26 || passwordLen == 58) && wepKey?.matches(Regex("[0-9A-Fa-f]*")) == true
                }

                val passwordLen: Int = password.length
                val isHexWepKey: Boolean = (passwordLen == 10 || passwordLen == 26 || passwordLen == 58) && password.matches(Regex("[0-9A-Fa-f]*"))

                // WEP-40, WEP-104, and 256-bit WEP (WEP-232?)
                if (isHexWepKey) {
                    config.wepKeys[0] = password
                } else {
                    config.wepKeys[0] = convertSsidToQuotedString(password)
                }
            }
            SECURITY_PSK -> {
                config.allowedProtocols.set(WifiConfiguration.Protocol.RSN)
                config.allowedProtocols.set(WifiConfiguration.Protocol.WPA)
                config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK)
                config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP)
                config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP)
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40)
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104)
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP)
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP)
                if (password.matches(Regex("[0-9A-Fa-f]{64}"))) {
                    config.preSharedKey = password
                } else {
                    config.preSharedKey = convertSsidToQuotedString(password)
                }
            }
            SECURITY_EAP -> {
                config.allowedProtocols.set(WifiConfiguration.Protocol.RSN)
                config.allowedProtocols.set(WifiConfiguration.Protocol.WPA)
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40)
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104)
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP)
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP)
                config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP)
                config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP)
                config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_EAP)
                config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.IEEE8021X)
                config.preSharedKey = convertSsidToQuotedString(password)
            }
            else -> {
            }
        }
    }

    @JvmStatic
    @RequiresPermission(allOf = [permission.ACCESS_FINE_LOCATION, permission.ACCESS_WIFI_STATE])
    fun getWifiConfiguration(wifiMgr: WifiManager, configToFind: WifiConfiguration): WifiConfiguration? {
        val ssid = configToFind.SSID
        if (ssid == null || ssid.isEmpty()) {
            return null
        }
        val bssid = if (configToFind.BSSID != null) configToFind.BSSID else ""
        val security = getSecurity(configToFind)
        val configurations = wifiMgr.configuredNetworks ?: return null
        for (config in configurations) {
            if (bssid == config.BSSID || ssid == config.SSID) {
                val configSecurity = getSecurity(config)
                if (security == configSecurity) {
                    return config
                }
            }
        }
        return null
    }

    @JvmStatic
    @Suppress("DEPRECATION")
    fun getWifiConfiguration(wifiManager: WifiManager?, ssid: String? = null): WifiConfiguration? {
        ssid ?: return null
        val configuredNetworks = wifiManager?.configuredNetworks
        if (configuredNetworks != null) {
            val findSSID = '"'.toString() + ssid + '"'
            for (wifiConfiguration in configuredNetworks) {
                if (wifiConfiguration.SSID == findSSID) {
                    return wifiConfiguration
                }
            }
        }
        return null
    }

    @JvmStatic
    @Suppress("DEPRECATION")
    fun getWifiConfiguration(wifiManager: WifiManager?, scanResult: ScanResult): WifiConfiguration? {
        if (scanResult.BSSID == null || scanResult.SSID == null || scanResult.SSID.isEmpty() || scanResult.BSSID.isEmpty()) {
            return null
        }
        val ssid: String = convertSsidToQuotedString(scanResult.SSID)
        val bssid = scanResult.BSSID
        val security = getSecurity(scanResult)
        val configurations = wifiManager?.configuredNetworks ?: return null
        for (config in configurations) {
            if (bssid == config.BSSID || ssid == config.SSID) {
                val configSecurity = getSecurity(config)
                if (security == configSecurity) {
                    return config
                }
            }
        }
        return null
    }

    @JvmStatic
    fun convertSsidToQuotedString(ssid: String): String {
        if (TextUtils.isEmpty(ssid)) return ""
        val lastPos = ssid.length - 1
        return if (lastPos < 0 || ssid[0] == '"' && ssid[lastPos] == '"') ssid else "\"" + ssid + "\""
    }

    interface WifiScanCallback {
        fun onScanResultsReady()
    }

    interface ScanResultsListener {
        fun onScanResults(scanResults: MutableList<ScanResult>)
    }

    interface WifiStateCallback {
        fun onWifiEnabled()
    }

    interface WifiStateListener {
        fun onWifiEnabled(enabled: Boolean)
    }

    class WifiScanReceiver(private val callback: WifiScanCallback) : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            STLogUtil.w(TAG, "WifiScanReceiver->onReceive")
            callback.onScanResultsReady()
        }
    }

    class WifiStateReceiver(val callbacks: WifiStateCallback) : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0)) {
                WifiManager.WIFI_STATE_ENABLED -> callbacks.onWifiEnabled()
                WifiManager.WIFI_STATE_ENABLING -> {
                }
                WifiManager.WIFI_STATE_DISABLING -> {
                }
                WifiManager.WIFI_STATE_DISABLED -> {
                }
            }
        }
    }
}