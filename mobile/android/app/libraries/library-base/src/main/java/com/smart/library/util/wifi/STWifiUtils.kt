package com.smart.library.util.wifi

import androidx.annotation.Keep

@Suppress("MemberVisibilityCanBePrivate")
//@Keep
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