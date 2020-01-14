package com.smart.library.util

import androidx.core.util.PatternsCompat
import java.util.regex.Pattern

object STRegexManager {

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