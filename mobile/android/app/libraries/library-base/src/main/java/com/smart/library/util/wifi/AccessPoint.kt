package com.smart.library.util.wifi

import android.net.wifi.ScanResult
import android.net.wifi.WifiConfiguration
import com.smart.library.util.STReflectUtil
import com.smart.library.util.STWifiUtil
import com.smart.library.util.wifi.WifiUtils.getSecurity
import com.smart.library.util.wifi.WifiUtils.isPasspointNetwork
import com.smart.library.util.wifi.WifiUtils.removeDoubleQuotes

@Suppress("DEPRECATION")
class AccessPoint(val scanResult: ScanResult) {
    /**
     * For applications targeting {@link android.os.Build.VERSION_CODES#Q} or above, this API will return null
     */
    private val wifiConfiguration: WifiConfiguration? by lazy { STWifiUtil.getWifiConfiguration(scanResult = scanResult) }
    val isPasspointNetwork: Boolean by lazy { if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) scanResult.isPasspointNetwork else isPasspointNetwork(wifiConfiguration) }
    val providerFriendlyName: String by lazy { if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) scanResult.operatorFriendlyName.toString() else "" }
    val isCarrierAp: Boolean by lazy { STReflectUtil.getFieldValue(scanResult, "isCarrierAp") as? Boolean ?: false }
    val carrierApEapType: Int by lazy { STReflectUtil.getFieldValue(scanResult, "carrierApEapType") as? Int ?: 0 }
    val carrierName: String by lazy { STReflectUtil.getFieldValue(scanResult, "carrierName") as? String ?: "" }
    val ssidStr: String by lazy { removeDoubleQuotes(scanResult.SSID) }
    val hiddenSSID: Boolean by lazy { wifiConfiguration?.hiddenSSID ?: false }
    val isSaved: Boolean by lazy { false }
    val networkId: Int by lazy { wifiConfiguration?.networkId ?: -1 }
    val security: Int by lazy { getSecurity(scanResult) }
}