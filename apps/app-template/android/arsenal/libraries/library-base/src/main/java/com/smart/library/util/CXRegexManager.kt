package com.smart.library.util

import android.support.v4.util.PatternsCompat
import java.util.regex.Pattern

object CXRegexManager {

    @JvmStatic
    fun isValidIPPort(content: String?): Boolean {
        if (content.isNullOrBlank()) return false
        return Pattern.compile("${PatternsCompat.IP_ADDRESS.toRegex().pattern}:\\d+").matcher(content).matches()
    }

    @JvmStatic
    fun isValidIP(content: String?): Boolean {
        if (content.isNullOrBlank()) return false
        return PatternsCompat.IP_ADDRESS.matcher(content).matches()
    }

}