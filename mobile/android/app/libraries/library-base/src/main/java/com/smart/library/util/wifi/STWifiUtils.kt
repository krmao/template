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
import com.smart.library.util.STLogUtil
import com.smart.library.util.STWifiUtil

@Suppress("MemberVisibilityCanBePrivate")
object STWifiUtils {
    const val TAG = "[wifi]"
    /**
     * These values are matched in string arrays -- changes must be kept in sync
     */
    const val SECURITY_NONE = 0
    const val SECURITY_WEP = 1
    const val SECURITY_PSK = 2
    const val SECURITY_EAP = 3


}