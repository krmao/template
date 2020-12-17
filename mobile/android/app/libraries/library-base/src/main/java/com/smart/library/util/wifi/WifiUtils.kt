package com.smart.library.util.wifi

import android.content.Context
import android.net.NetworkInfo.DetailedState
import android.net.wifi.ScanResult
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiConfiguration.KeyMgmt
import android.net.wifi.WifiEnterpriseConfig
import android.net.wifi.WifiManager
import android.text.TextUtils
import android.util.Log
import com.smart.library.R
import com.smart.library.util.STWifiUtil

@Suppress("MemberVisibilityCanBePrivate")
object WifiUtils {
    const val TAG = "[wifi]"

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

    /**
     * These values are matched in string arrays -- changes must be kept in sync
     */
    const val SECURITY_NONE = 0
    const val SECURITY_WEP = 1
    const val SECURITY_PSK = 2
    const val SECURITY_EAP = 3

    const val PSK_UNKNOWN = 0
    const val PSK_WPA = 1
    const val PSK_WPA2 = 2
    const val PSK_WPA_WPA2 = 3

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

    /**
     * The number of distinct wifi levels.
     *
     *
     * Must keep in sync with [R.array.wifi_signal] and [WifiManager.RSSI_LEVELS].
     */
    const val SIGNAL_LEVELS = 5

    const val INVALID_NETWORK_ID = -1

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
    fun getSpeedLabel(context: Context?, scanResult: ScanResult): String? {
        return getSpeedLabel(context, getSpeed(scanResult))
    }

    @JvmStatic
    fun getSpeed(scanResult: ScanResult): Speed {
        return STWifiUtil.getSpeed(STWifiUtil.getSignalStrength(scanResult = scanResult))
    }


    @JvmStatic
    fun getKey(result: ScanResult): String? {
        val builder = StringBuilder()
        if (TextUtils.isEmpty(result.SSID)) {
            builder.append(result.BSSID)
        } else {
            builder.append(result.SSID)
        }
        builder.append(',').append(getSecurity(result))
        return builder.toString()
    }

    @JvmStatic
    fun getKey(config: WifiConfiguration): String? {
        val builder = StringBuilder()
        if (TextUtils.isEmpty(config.SSID)) {
            builder.append(config.BSSID)
        } else {
            builder.append(removeDoubleQuotes(config.SSID))
        }
        builder.append(',').append(getSecurity(config))
        return builder.toString()
    }

    @JvmStatic
    fun getPskType(result: ScanResult): Int {
        val wpa = result.capabilities.contains("WPA-PSK")
        val wpa2 = result.capabilities.contains("WPA2-PSK")
        return if (wpa2 && wpa) {
            PSK_WPA_WPA2
        } else if (wpa2) {
            PSK_WPA2
        } else if (wpa) {
            PSK_WPA
        } else {
            Log.w(TAG, "Received abnormal flag string: " + result.capabilities)
            PSK_UNKNOWN
        }
    }

    @JvmStatic
    fun getLevel(rssi: Int): Int {
        return WifiManager.calculateSignalLevel(rssi, SIGNAL_LEVELS)
    }

    @JvmStatic
    fun getSecurity(result: ScanResult): Int {
        return when {
            result.capabilities.contains("WEP") -> {
                SECURITY_WEP
            }
            result.capabilities.contains("PSK") -> {
                SECURITY_PSK
            }
            result.capabilities.contains("EAP") -> {
                SECURITY_EAP
            }
            else -> SECURITY_NONE
        }
    }

    @JvmStatic
    fun getSecurity(config: WifiConfiguration): Int {
        if (config.allowedKeyManagement[KeyMgmt.WPA_PSK]) {
            return SECURITY_PSK
        }
        if (config.allowedKeyManagement[KeyMgmt.WPA_EAP] || config.allowedKeyManagement[KeyMgmt.IEEE8021X]) {
            return SECURITY_EAP
        }
        return if (config.wepKeys[0] != null) SECURITY_WEP else SECURITY_NONE
    }

    @JvmStatic
    fun securityToString(security: Int, pskType: Int): String? {
        if (security == SECURITY_WEP) {
            return "WEP"
        } else if (security == SECURITY_PSK) {
            if (pskType == PSK_WPA) {
                return "WPA"
            } else if (pskType == PSK_WPA2) {
                return "WPA2"
            } else if (pskType == PSK_WPA_WPA2) {
                return "WPA_WPA2"
            }
            return "PSK"
        } else if (security == SECURITY_EAP) {
            return "EAP"
        }
        return "NONE"
    }


    //region 双引号处理

    //region 双引号处理
    /**
     * 在字符串前后加上双引号
     */
    @JvmStatic
    fun convertToQuotedString(string: String): String? {
        return "\"" + string + "\""
    }

    /**
     * 如果该字符串第一个和最后一个是双引号, 则删除双引号
     */
    @JvmStatic
    fun removeDoubleQuotes(string: String?): String {
        if (string == null || TextUtils.isEmpty(string)) return ""
        val length = string.length
        return if (length > 1 && string[0] == '"' && string[length - 1] == '"') string.substring(1, length - 1) else string
    }

    //endregion

    @JvmStatic
    fun getSummary(context: Context, ssid: String?, state: DetailedState?, isEphemeral: Boolean, passpointProvider: String?): String? {
        if (state == DetailedState.CONNECTED && ssid == null) {
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
    fun getSummary(context: Context, state: DetailedState?, isEphemeral: Boolean, passpointProvider: String?): String? {
        return getSummary(context, null, state, isEphemeral, passpointProvider)
    }

    @JvmStatic
    fun getSecurityString(context: Context, concise: Boolean, security: Int, pskType: Int): String? {
        return when (security) {
            SECURITY_EAP -> if (concise) context.getString(R.string.wifi_security_short_eap) else context.getString(R.string.wifi_security_eap)
            SECURITY_PSK -> when (pskType) {
                PSK_WPA -> if (concise) context.getString(R.string.wifi_security_short_wpa) else context.getString(R.string.wifi_security_wpa)
                PSK_WPA2 -> if (concise) context.getString(R.string.wifi_security_short_wpa2) else context.getString(R.string.wifi_security_wpa2)
                PSK_WPA_WPA2 -> if (concise) context.getString(R.string.wifi_security_short_wpa_wpa2) else context.getString(R.string.wifi_security_wpa_wpa2)
                PSK_UNKNOWN -> if (concise) context.getString(R.string.wifi_security_short_psk_generic) else context.getString(R.string.wifi_security_psk_generic)
                else -> if (concise) context.getString(R.string.wifi_security_short_psk_generic) else context.getString(R.string.wifi_security_psk_generic)
            }
            SECURITY_WEP -> if (concise) context.getString(R.string.wifi_security_short_wep) else context.getString(R.string.wifi_security_wep)
            SECURITY_NONE -> if (concise) "" else context.getString(R.string.wifi_security_none)
            else -> if (concise) "" else context.getString(R.string.wifi_security_none)
        }
    }

    /**
     * Identify if this configuration represents a PassPoint network
     */
    @JvmStatic
    fun isPasspointNetwork(config: WifiConfiguration?): Boolean {
        return config != null && (!TextUtils.isEmpty(config.FQDN) && config.enterpriseConfig != null && config.enterpriseConfig.eapMethod != WifiEnterpriseConfig.Eap.NONE)
    }

}