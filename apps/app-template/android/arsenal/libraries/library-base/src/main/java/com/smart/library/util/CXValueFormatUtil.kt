package com.smart.library.util

import java.math.BigDecimal

@Suppress("unused", "MemberVisibilityCanBePrivate")
object CXValueFormatUtil {
    private val TAG = CXValueFormatUtil::class.java.name

    //四舍五入 decimalLength：小数位数
    @JvmStatic
    fun toDecimalOrNull(value: Double?, decimalLength: Int): Double? {
        var formatValue: Double? = null

        value?.let {
            try {
                formatValue = BigDecimal(value).setScale(decimalLength, BigDecimal.ROUND_HALF_UP).toDouble()
            } catch (e: NumberFormatException) {
                CXLogUtil.e(TAG, e)
            }
        }

        return formatValue
    }

    @JvmOverloads
    @JvmStatic
    fun toDecimal(value: Double?, decimalLength: Int, defaultValue: Double = 0.0): Double {
        return toDecimalOrNull(value, decimalLength) ?: defaultValue
    }

    @JvmStatic
    fun toDoubleOrNull(value: String?): Double? {
        return value?.toDoubleOrNull()
    }

    @JvmOverloads
    @JvmStatic
    fun toDouble(value: String?, defaultValue: Double = 0.0): Double {
        return value?.toDoubleOrNull() ?: defaultValue
    }

    @JvmStatic
    fun toIntOrNull(value: String?): Int? {
        return value?.toIntOrNull()
    }

    @JvmOverloads
    @JvmStatic
    fun toInt(value: String?, defaultValue: Int = 0): Int {
        return value?.toIntOrNull() ?: defaultValue
    }

    @JvmStatic
    fun toFloatOrNull(value: String?): Float? {
        return value?.toFloatOrNull()
    }

    @JvmOverloads
    @JvmStatic
    fun toInt(value: String?, defaultValue: Float = 0f): Float {
        return value?.toFloatOrNull() ?: defaultValue
    }
}
