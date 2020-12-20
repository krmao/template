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
import android.net.*
import android.net.wifi.*
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import com.smart.library.R
import com.smart.library.STInitializer
import com.smart.library.util.rx.permission.RxPermissions
import com.smart.library.util.wifi.STWifiUtils

/**
 *
 * SDK >= Q, This {@link NetworkRequest} will live until released via {@link #unregisterNetworkCallback(NetworkCallback)} or the calling application exits.
 *
 * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
 * <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
 * <uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
 * <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
 */
@Suppress("unused", "MemberVisibilityCanBePrivate", "DEPRECATION", "KDocUnresolvedReference")
object STWifiUtil {

    const val TAG = "[wifi]"

    private val WIFI_PIE: IntArray by lazy {
        intArrayOf(
            R.drawable.ic_wifi_signal_0,
            R.drawable.ic_wifi_signal_1,
            R.drawable.ic_wifi_signal_2,
            R.drawable.ic_wifi_signal_3,
            R.drawable.ic_wifi_signal_4
        )
    }

    /**
     * The number of distinct wifi levels.
     *
     * Must keep in sync with [R.array.wifi_signal] and [WifiManager.RSSI_LEVELS].
     */
    const val SIGNAL_LEVELS = 5
    const val SECURITY_NONE = 0
    const val SECURITY_WEP = 1
    const val SECURITY_PSK = 2
    const val SECURITY_EAP = 3
    const val LOCATION_AVAILABLE = 1000
    const val LOCATION_NONE = 1111
    const val LOCATION_DISABLED = 1112
    const val PSK_UNKNOWN = 0
    const val PSK_WPA = 1
    const val PSK_WPA2 = 2
    const val PSK_WPA_WPA2 = 3

    /**
     * Lower bound on the 2.4 GHz (802.11b/g/n) WLAN channels
     */
    const val LOWER_FREQ_24GHZ = 2400

    /**
     * Upper bound on the 2.4 GHz (802.11b/g/n) WLAN channels
     */
    const val HIGHER_FREQ_24GHZ = 2500

    /**
     * Lower bound on the 5.0 GHz (802.11a/h/j/n/ac) WLAN channels
     */
    const val LOWER_FREQ_5GHZ = 4900

    /**
     * Upper bound on the 5.0 GHz (802.11a/h/j/n/ac) WLAN channels
     */
    const val HIGHER_FREQ_5GHZ = 5900

    private val wifiStateSetListener: MutableSet<WifiStateListener> = mutableSetOf()
    private val scanResultsSetListener: MutableSet<ScanResultsListener> = mutableSetOf()
    private val wifiScanReceiver: WifiScanReceiver by lazy { WifiScanReceiver(wifiScanCallback) }
    private val wifiStateReceiver: WifiStateReceiver by lazy { WifiStateReceiver(wifiStateCallback) }

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

    @JvmStatic
    private fun checkLocationAvailability(context: Context?): Int {
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
    @JvmOverloads
    fun checkIfNeedOpenLocationSettings(application: Application? = STInitializer.application(), autoOpen: Boolean = true): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            if (checkLocationAvailability(application) == LOCATION_DISABLED) {
                if (autoOpen) openSystemLocationSettings(application)
                return true
            }
        }
        return false
    }

    /**
     * Returns the Wifi icon resource for a given RSSI level.
     *
     * @param level The number of bars to show (0-4)
     * @return -1 if an invalid RSSI level is given.
     */
    @JvmStatic
    fun getWifiIconResource(level: Int): Int {
        if (level < 0 || level >= WIFI_PIE.size) {
            STLogUtil.e(TAG, "No Wifi icon found for level: $level")
            return -1
        }
        return WIFI_PIE[level]
    }

    /**
     * AP -> 无线路由器
     * passpoint -> 传统 wifi 由于受信号强度限制只能在一定范围内使用，当用户到另一区域时，需要重新选择和连接 AP, 而 passpoint 实现了可以从一个 AP 漫游无缝切换到另一个 AP, 无需输入密码, 类似手机SIM使用的蜂窝网络一样
     * Identify(确定) if this configuration represents a PassPoint network
     */
    @JvmStatic
    fun isPasspointNetwork(config: WifiConfiguration?): Boolean = isPasspointNetwork(config?.enterpriseConfig?.eapMethod)

    @JvmStatic
    fun isPasspointNetwork(eapMethod: Int?): Boolean {
        val isPasspointNetwork = eapMethod != null && eapMethod != WifiEnterpriseConfig.Eap.NONE
        STLogUtil.w(TAG, "isPasspointNetwork=$isPasspointNetwork")
        return isPasspointNetwork
    }

    // android9.0以上需要申请定位权限
    // android10.0需要申请新添加的隐私权限ACCESS_FINE_LOCATION详情见android官方10.0重大隐私权变更，如果还需要后台获取或者使用wifi api则还需要申请后台使用定位权限ACCESS_BACKGROUND_LOCATION
    // https://developer.android.google.cn/about/versions/10/privacy/
    @JvmStatic
    fun ensurePermissions(activity: Activity?, callback: (allPermissionsGranted: Boolean) -> Unit) {
        RxPermissions.ensurePermissions(activity, callback, permission.ACCESS_WIFI_STATE, permission.CHANGE_WIFI_STATE, permission.ACCESS_FINE_LOCATION)
    }

    @JvmStatic
    private fun registerReceiver(context: Context?, receiver: BroadcastReceiver?, filter: IntentFilter) {
        STLogUtil.w(TAG, "registerReceiver ${receiver?.javaClass?.name}")
        if (receiver != null) {
            try {
                context?.registerReceiver(receiver, filter)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    @JvmStatic
    private fun unregisterReceiver(context: Context?, receiver: BroadcastReceiver?) {
        STLogUtil.w(TAG, "unregisterReceiver ${receiver?.javaClass?.name}")
        if (receiver != null) {
            try {
                context?.unregisterReceiver(receiver)
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            }
        }
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
    @Deprecated("Starting with Build.VERSION_CODES#Q, applications are not allowed to")
    @RequiresPermission(allOf = [permission.ACCESS_NETWORK_STATE, permission.ACCESS_WIFI_STATE, permission.CHANGE_WIFI_STATE, permission.ACCESS_FINE_LOCATION])
    private fun setWifiEnabledPreAndroidQ(application: Application? = STInitializer.application(), enable: Boolean, wifiManager: WifiManager? = getWifiManager(application), wifiStateListener: WifiStateListener? = null) {
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

    /**
     * https://stackoverflow.com/questions/58769623/android-10-api-29-how-to-connect-the-phone-to-a-configured-network
     */
    @Suppress("DEPRECATION")
    @JvmStatic
    @JvmOverloads
    fun disconnectWifi(application: Application? = STInitializer.application(), connectResult: ConnectResult?, removeWifi: Boolean = true) {
        STLogUtil.d(TAG, "disconnectWifi start connectResult=$connectResult")
        if (!isAndroidQOrLater()) {
            val wifiManager: WifiManager? = getWifiManager(application)
            val networkId: Int? = connectResult?.networkId

            if (wifiManager != null) {
                wifiManager.disconnect()
                STLogUtil.d(TAG, "disconnectWifi disconnect success!!!")

                if (removeWifi && networkId != null) {
                    wifiManager.removeNetwork(networkId)
                    STLogUtil.d(TAG, "disconnectWifi removeNetwork success!!!")
                }
            }
            STLogUtil.d(TAG, "disconnectWifi wifiManager=$wifiManager, networkId=$networkId, removeWifi=$removeWifi")
        } else {
            val connectivityManager: ConnectivityManager? = getConnectivityManager(application)
            val networkCallback: ConnectivityManager.NetworkCallback? = connectResult?.networkCallback
            if (connectivityManager != null && networkCallback != null) {
                connectivityManager.unregisterNetworkCallback(networkCallback)
                STLogUtil.d(TAG, "disconnectWifi success !!!")
            }
            STLogUtil.d(TAG, "disconnectWifi connectivityManager=$connectivityManager, networkCallback=$networkCallback")
        }
        STLogUtil.d(TAG, "disconnectWifi end")
    }

    @JvmStatic
    @RequiresPermission(allOf = [permission.ACCESS_NETWORK_STATE, permission.ACCESS_WIFI_STATE, permission.CHANGE_WIFI_STATE, permission.ACCESS_FINE_LOCATION])
    fun connectWifi(application: Application? = STInitializer.application(), scanResult: ScanResult, password: String? = null, identity: String? = null, anonymousIdentity: String? = null, eapMethod: Int? = null, phase2Method: Int? = null, networkCallback: ConnectivityManager.NetworkCallback?): ConnectResult {
        STLogUtil.w(TAG, "connectWifi scanResult=$scanResult, password=$password, identity=$identity, anonymousIdentity=$anonymousIdentity, eapMethod=$eapMethod, phase2Method=$phase2Method")
        return if (!isAndroidQOrLater()) {
            connectWifiPreAndroidQ(application, scanResult, password, identity, anonymousIdentity, eapMethod, phase2Method)
        } else {
            connectWifiAndroidQ(application, scanResult, password, identity, anonymousIdentity, eapMethod, phase2Method, networkCallback)
        }
    }

    @JvmStatic
    @RequiresPermission(allOf = [permission.ACCESS_FINE_LOCATION, permission.ACCESS_WIFI_STATE])
    private fun connectWifiPreAndroidQ(application: Application? = STInitializer.application(), scanResult: ScanResult, password: String?, identity: String?, anonymousIdentity: String?, eapMethod: Int?, phase2Method: Int?): ConnectResult {
        return if (!isAndroidQOrLater()) {
            ConnectResult(networkId = connectWifiPreAndroidQ(application, createWifiConfiguration(application, scanResult, password, identity, anonymousIdentity, eapMethod, phase2Method)))
        } else {
            STLogUtil.e(TAG, "connectWifiPreAndroidQ -> must preAndroidQ !!!")
            return ConnectResult()
        }
    }

    @JvmStatic
    private fun connectWifiPreAndroidQ(application: Application? = STInitializer.application(), wifiConfiguration: WifiConfiguration?): Int {
        if (!isAndroidQOrLater()) {
            val wifiManager: WifiManager? = getWifiManager(application)
            if (wifiManager == null) {
                STLogUtil.e(TAG, "connectWifiPreAndroidQ -> wifiManager == null !!!")
                return -1
            }

            STLogUtil.w(TAG, "connectWifiPreAndroidQ -> disconnect")
            wifiManager.disconnect()
            // remove last configuration
            wifiConfiguration?.networkId?.let { if (wifiManager.removeNetwork(it)) wifiManager.saveConfiguration() }
            // reAdd wifiConfiguration
            STLogUtil.w(TAG, "connectWifiPreAndroidQ -> addNetwork")
            val networkId: Int = wifiManager.addNetwork(wifiConfiguration)
            STLogUtil.w(TAG, "connectWifiPreAndroidQ -> saveConfiguration")
            wifiManager.saveConfiguration()
            STLogUtil.w(TAG, "connectWifiPreAndroidQ -> enableNetwork")
            wifiManager.enableNetwork(networkId, true)
            STLogUtil.w(TAG, "connectWifiPreAndroidQ -> reconnect")
            wifiManager.reconnect()
            return networkId
        } else {
            STLogUtil.e(TAG, "connectWifiPreAndroidQ -> must preAndroidQ !!!")
            return -1
        }
    }

    /**
     * https://stackoverflow.com/questions/58769623/android-10-api-29-how-to-connect-the-phone-to-a-configured-network
     */
    @RequiresApi(Build.VERSION_CODES.Q)
    @JvmStatic
    private fun connectWifiAndroidQ(application: Application? = STInitializer.application(), scanResult: ScanResult, password: String?, identity: String?, anonymousIdentity: String?, eapMethod: Int?, phase2Method: Int?, networkCallback: ConnectivityManager.NetworkCallback?): ConnectResult {
        val connectivityManager: ConnectivityManager? = getConnectivityManager(application)
        if (connectivityManager != null) {
            if (isAndroidQOrLater()) {
                val networkRequest: NetworkRequest = createNetworkRequestBuilderAndroidQ(
                    security = getSecurity(scanResult),
                    ssid = scanResult.SSID,
                    password = password,
                    identity = identity,
                    anonymousIdentity = anonymousIdentity,
                    eapMethod = eapMethod,
                    phase2Method = phase2Method
                ).build()
                STLogUtil.w(TAG, "connectWifiAndroidQ requestNetwork start")
                connectivityManager.requestNetwork(networkRequest, networkCallback ?: object : ConnectivityManager.NetworkCallback() {
                    override fun onAvailable(network: Network) {
                        super.onAvailable(network)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            connectivityManager.bindProcessToNetwork(network)
                        } else {
                            ConnectivityManager.setProcessDefaultNetwork(network)
                        }
                        STLogUtil.w(TAG, "connectWifiAndroidQ -> onAvailable")
                    }

                    override fun onUnavailable() {
                        super.onUnavailable()
                        STLogUtil.w(TAG, "connectWifiAndroidQ -> onUnavailable")
                    }

                    override fun onBlockedStatusChanged(network: Network, blocked: Boolean) {
                        super.onBlockedStatusChanged(network, blocked)
                        STLogUtil.w(TAG, "connectWifiAndroidQ -> onBlockedStatusChanged")
                    }

                    override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
                        super.onCapabilitiesChanged(network, networkCapabilities)
                        STLogUtil.w(TAG, "connectWifiAndroidQ -> onCapabilitiesChanged")
                    }

                    override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
                        super.onLinkPropertiesChanged(network, linkProperties)
                        STLogUtil.w(TAG, "connectWifiAndroidQ -> onLinkPropertiesChanged")
                    }

                    override fun onLosing(network: Network, maxMsToLive: Int) {
                        super.onLosing(network, maxMsToLive)
                        STLogUtil.w(TAG, "connectWifiAndroidQ -> onLosing")
                    }

                    override fun onLost(network: Network) {
                        super.onLost(network)
                        STLogUtil.w(TAG, "connectWifiAndroidQ -> onLost")
                    }
                })
                STLogUtil.w(TAG, "connectWifiAndroidQ requestNetwork end")
            } else {
                STLogUtil.e(TAG, "connectWifiAndroidQ must be preAndroidQ !!!")
            }
        } else {
            STLogUtil.e(TAG, "connectWifiAndroidQ connectivityManager must not be null !!!")
        }
        return ConnectResult(networkCallback = networkCallback)
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

    @Suppress("DEPRECATION")
    @JvmStatic
    @RequiresPermission(allOf = [permission.ACCESS_FINE_LOCATION, permission.ACCESS_WIFI_STATE])
    private fun createWifiConfiguration(application: Application? = STInitializer.application(), scanResult: ScanResult, password: String?, identity: String? = null, anonymousIdentity: String? = null, eapMethod: Int?, phase2Method: Int?): WifiConfiguration {
        val wifiConfiguration: WifiConfiguration = getWifiConfiguration(application, getWifiManager(application), scanResult) ?: WifiConfiguration()
        wifiConfiguration.SSID = this.convertToQuotedString(scanResult.SSID)
        setupSecurity(
            config = wifiConfiguration,
            security = getSecurity(scanResult),
            password = password ?: "",
            identity = identity,
            anonymousIdentity = anonymousIdentity,
            eapMethod = eapMethod,
            phase2Method = phase2Method
        )
        return wifiConfiguration
    }

    @JvmStatic
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun createNetworkRequestBuilderAndroidQ(security: Int, ssid: String, password: String?, identity: String?, anonymousIdentity: String?, eapMethod: Int?, phase2Method: Int?): NetworkRequest.Builder {
        val finalSsid = removeDoubleQuotes(ssid)
        val finalPassword = removeDoubleQuotes(password)

        STLogUtil.w(TAG, "==> security=$security, finalSsid=$finalSsid, finalPassword=$finalPassword, identity=$identity, anonymousIdentity=$anonymousIdentity, eapMethod=$eapMethod, phase2Method=$phase2Method")
        val specifierBuilder: WifiNetworkSpecifier.Builder = WifiNetworkSpecifier.Builder().setSsid(finalSsid)
        if (security == SECURITY_EAP/* || (eapMethod != null && eapMethod != WifiEnterpriseConfig.Eap.NONE)*/) {
            specifierBuilder.setWpa2EnterpriseConfig(createWifiEnterpriseConfig(identity, anonymousIdentity, finalPassword, eapMethod, phase2Method))
        } else {
            try {
                specifierBuilder.setWpa2Passphrase(finalPassword)
            } catch (exception: IllegalArgumentException) {
                exception.printStackTrace()
            }
        }

        return NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI) //创建的是WIFI网络
            .addCapability(NetworkCapabilities.NET_CAPABILITY_NOT_RESTRICTED) //网络不受限
            .addCapability(NetworkCapabilities.NET_CAPABILITY_TRUSTED) //信任网络，增加这个连个参数让设备连接wifi之后还联网
            .removeCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .setNetworkSpecifier(specifierBuilder.build())
    }

    /**
     * @param rssi scanResult.level, The detected signal level in dBm, also known as the RSSI.
     * @see {@link https://github.com/paladinzh/decompile-hw/blob/4c3efd95f3e997b44dd4ceec506de6164192eca3/decompile/app/SystemUI/src/main/res/values-zh-rCN/strings.xml}
     */
    @JvmStatic
    private fun getSignalStrength(rssi: Int?): Int = WifiManager.calculateSignalLevel(rssi ?: -100, SIGNAL_LEVELS)

    @JvmStatic
    fun getSignalStrength(scanResult: ScanResult?): Int = getSignalStrength(scanResult?.level)

    @JvmStatic
    @JvmOverloads
    fun getSecurityString(scanResult: ScanResult, concise: Boolean = false): String? = getSecurityString(concise = concise, security = getSecurity(scanResult), pskType = getPskType(scanResult))

    @JvmStatic
    private fun getSecurityString(security: Int, pskType: Int, concise: Boolean): String? {
        val context: Context? = STInitializer.application()
        context ?: return null
        return when (security) {
            STWifiUtils.SECURITY_EAP -> if (concise) context.getString(R.string.wifi_security_short_eap) else context.getString(R.string.wifi_security_eap)
            STWifiUtils.SECURITY_PSK -> when (pskType) {
                PSK_WPA -> if (concise) context.getString(R.string.wifi_security_short_wpa) else context.getString(R.string.wifi_security_wpa)
                PSK_WPA2 -> if (concise) context.getString(R.string.wifi_security_short_wpa2) else context.getString(R.string.wifi_security_wpa2)
                PSK_WPA_WPA2 -> if (concise) context.getString(R.string.wifi_security_short_wpa_wpa2) else context.getString(R.string.wifi_security_wpa_wpa2)
                PSK_UNKNOWN -> if (concise) context.getString(R.string.wifi_security_short_psk_generic) else context.getString(R.string.wifi_security_psk_generic)
                else -> if (concise) context.getString(R.string.wifi_security_short_psk_generic) else context.getString(R.string.wifi_security_psk_generic)
            }
            STWifiUtils.SECURITY_WEP -> if (concise) context.getString(R.string.wifi_security_short_wep) else context.getString(R.string.wifi_security_wep)
            STWifiUtils.SECURITY_NONE -> if (concise) "" else context.getString(R.string.wifi_security_none)
            else -> if (concise) "" else context.getString(R.string.wifi_security_none)
        }
    }

    @JvmStatic
    fun getSecurity(result: ScanResult?): Int {
        return when {
            result != null && result.capabilities.contains("PSK") -> STWifiUtils.SECURITY_PSK
            result != null && result.capabilities.contains("EAP") -> STWifiUtils.SECURITY_EAP
            result != null && result.capabilities.contains("WEP") -> STWifiUtils.SECURITY_WEP
            else -> STWifiUtils.SECURITY_NONE
        }
    }

    @JvmStatic
    fun getSecurity(config: WifiConfiguration?): Int {
        config ?: return STWifiUtils.SECURITY_NONE

        if (config.allowedKeyManagement[WifiConfiguration.KeyMgmt.WPA_PSK]) {
            return STWifiUtils.SECURITY_PSK
        }
        if (config.allowedKeyManagement[WifiConfiguration.KeyMgmt.WPA_EAP] || config.allowedKeyManagement[WifiConfiguration.KeyMgmt.IEEE8021X]) {
            return STWifiUtils.SECURITY_EAP
        }
        return if (config.wepKeys[0] != null) STWifiUtils.SECURITY_WEP else STWifiUtils.SECURITY_NONE
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
    private fun setupSecurity(config: WifiConfiguration, security: Int, password: String, identity: String?, anonymousIdentity: String?, eapMethod: Int?, phase2Method: Int?) {
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
                    config.wepKeys[0] = this.convertToQuotedString(password)
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
                    config.preSharedKey = this.convertToQuotedString(password)
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
                config.preSharedKey = this.convertToQuotedString(password)
                config.enterpriseConfig = createWifiEnterpriseConfig(identity, anonymousIdentity, password, eapMethod, phase2Method)
            }
            else -> {
            }
        }
    }

    @JvmStatic
    private fun createWifiEnterpriseConfig(identity: String?, anonymousIdentity: String?, password: String, eapMethod: Int?, phase2Method: Int?): WifiEnterpriseConfig {
        return WifiEnterpriseConfig().also { enterpriseConfig ->
            enterpriseConfig.identity = identity
            enterpriseConfig.password = password
            if (eapMethod != null) {
                try {
                    enterpriseConfig.eapMethod = eapMethod
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            if (phase2Method != null) {
                try {
                    enterpriseConfig.phase2Method = phase2Method
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            enterpriseConfig.anonymousIdentity = anonymousIdentity
            enterpriseConfig.caCertificate = null
        }
    }

    @JvmStatic
    @Suppress("DEPRECATION")
    fun getWifiConfiguration(application: Application? = STInitializer.application(), wifiManager: WifiManager? = getWifiManager(application), scanResult: ScanResult?): WifiConfiguration? {
        if (wifiManager == null || scanResult == null || scanResult.SSID.isNullOrBlank() || scanResult.BSSID.isNullOrBlank()) {
            STLogUtil.e(TAG, "getWifiConfiguration return null !!! wifiManager == null || scanResult.SSID.isNullOrBlank() || scanResult.BSSID.isNullOrBlank()")
            return null
        }
        val ssid: String = this.convertToQuotedString(scanResult.SSID)
        val bssid = scanResult.BSSID
        val security = getSecurity(scanResult)
        // configuredNetworks -> For applications targeting {@link android.os.Build.VERSION_CODES#Q} or above, this API will return an empty list
        val configurations: MutableList<WifiConfiguration>? = wifiManager.configuredNetworks

        if (configurations.isNullOrEmpty()) {
            STLogUtil.e(TAG, "getWifiConfiguration return null !!! configurations isNullOrEmpty !!! configurations=$configurations")
            return null
        }
        STLogUtil.d(TAG, "getWifiConfiguration scanResult=$scanResult \nconfigurations=$configurations")
        for (config: WifiConfiguration in configurations) {
            STLogUtil.d(TAG, "getWifiConfiguration item config=$config")
            if (bssid == config.BSSID || ssid == config.SSID) {
                val configSecurity: Int = getSecurity(config)
                if (security == configSecurity) {
                    STLogUtil.w(TAG, "getWifiConfiguration return success !!! config = $config")
                    return config
                }
            }
        }
        STLogUtil.w(TAG, "getWifiConfiguration return failure !!! config = null")
        return null
    }

    /**
     * @param signalStrength
     * @see getSignalStrength
     */
    @JvmStatic
    private fun getSpeed(signalStrength: Int): Speed {
        return when (signalStrength) {
            4 -> Speed.VERY_FAST
            3 -> Speed.FAST
            2 -> Speed.MODERATE
            1 -> Speed.SLOW
            0 -> Speed.NONE
            else -> Speed.NONE
        }
    }

    @JvmStatic
    fun getSpeed(scanResult: ScanResult): Speed = getSpeed(getSignalStrength(scanResult = scanResult))

    @JvmStatic
    fun getSpeedLabel(context: Context?, speed: Speed?): String? {
        return when {
            context != null && speed == Speed.VERY_FAST -> context.getString(R.string.speed_label_very_fast)
            context != null && speed == Speed.FAST -> context.getString(R.string.speed_label_fast)
            context != null && speed == Speed.MODERATE -> context.getString(R.string.speed_label_okay)
            context != null && speed == Speed.SLOW -> context.getString(R.string.speed_label_slow)
            context != null && speed == Speed.NONE -> null
            else -> null
        }
    }

    @JvmStatic
    fun getSpeedLabel(context: Context?, scanResult: ScanResult): String? = getSpeedLabel(context, getSpeed(scanResult))

    @JvmStatic
    fun getPskType(result: ScanResult): Int {
        val wpa: Boolean = result.capabilities.contains("WPA-PSK")
        val wpa2: Boolean = result.capabilities.contains("WPA2-PSK")
        return if (wpa2 && wpa) {
            PSK_WPA_WPA2
        } else if (wpa2) {
            PSK_WPA2
        } else if (wpa) {
            PSK_WPA
        } else {
            STLogUtil.w(TAG, "Received abnormal flag string: " + result.capabilities)
            PSK_UNKNOWN
        }
    }

    /**
     * 在字符串前后加上双引号
     */
    @JvmStatic
    fun convertToQuotedString(string: String): String {
        if (TextUtils.isEmpty(string)) return ""
        val lastPosition: Int = string.length - 1
        return if (lastPosition < 0 || string[0] == '"' && string[lastPosition] == '"') string else ("\"" + string + "\"")
    }

    /**
     * 如果该字符串第一个和最后一个是双引号, 则删除双引号
     */
    @JvmStatic
    fun removeDoubleQuotes(string: String?): String {
        if (string == null || TextUtils.isEmpty(string)) return ""
        val length: Int = string.length
        return if (length > 1 && string[0] == '"' && string[length - 1] == '"') string.substring(1, length - 1) else string
    }

    @JvmStatic
    fun getSummary(context: Context, ssid: String?, state: NetworkInfo.DetailedState?, isEphemeral: Boolean, passpointProvider: String?): String? {
        if (state == NetworkInfo.DetailedState.CONNECTED && ssid == null) {
            if (!TextUtils.isEmpty(passpointProvider)) {
                return String.format(context.getString(R.string.connected_via_passpoint), passpointProvider)
            } else if (isEphemeral) {
                return context.getString(R.string.connected_via_network_scorer_default)
            }
        }
        // Case when there is wifi connected without internet connectivity.
        if (state == null) {
            Log.w(TAG, "state is null, returning empty summary")
            return ""
        }
        val formats = context.resources.getStringArray(if (ssid == null) R.array.wifi_status else R.array.wifi_status_with_ssid)
        val index = state.ordinal
        return if (index >= formats.size || formats[index].isEmpty()) "" else String.format(formats[index], ssid)
    }

    @JvmStatic
    fun getSummary(context: Context, state: NetworkInfo.DetailedState?, isEphemeral: Boolean, passpointProvider: String?): String? = getSummary(context, null, state, isEphemeral, passpointProvider)

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

    data class ConnectResult(val networkId: Int = -1, val networkCallback: ConnectivityManager.NetworkCallback? = null)

    enum class Speed {
        NONE,

        /**
         * Constant value representing a slow speed network connection.
         */
        SLOW,

        /**
         * Constant value representing a medium speed network connection.
         */
        MODERATE,

        /**
         * Constant value representing a fast speed network connection.
         */
        FAST,

        /**
         * Constant value representing a very fast speed network connection.
         */
        VERY_FAST;
    }
}