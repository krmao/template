package com.smart.library.util

import java.math.BigDecimal
import java.text.DecimalFormat

@Suppress("unused", "MemberVisibilityCanBePrivate", "ReplaceCallWithBinaryOperator")
object CXValueUtil {
    private val TAG = CXValueUtil::class.java.name

    /**
     * 是否 A 等于 B
     */
    @JvmStatic
    fun isAEqualB(valueA: BigDecimal, valueB: BigDecimal): Boolean = valueA.compareTo(valueB) == 0

    /**
     * 是否 A 小于 B
     */
    @JvmStatic
    fun isALessThanB(valueA: BigDecimal, valueB: BigDecimal): Boolean = valueA.compareTo(valueB) == -1

    /**
     * 是否 A 小于或等于 B
     */
    @JvmStatic
    fun isALessThanOrEqualB(valueA: BigDecimal, valueB: BigDecimal): Boolean = valueA.compareTo(valueB) <= 0

    /**
     * 是否 A 大于 B
     */
    @JvmStatic
    fun isAGreaterThanB(valueA: BigDecimal, valueB: BigDecimal): Boolean = valueA.compareTo(valueB) == 1

    /**
     * 是否 A 大于或等于 B
     */
    @JvmStatic
    fun isAGreaterThanOrEqualB(valueA: BigDecimal, valueB: BigDecimal): Boolean = valueA.compareTo(valueB) >= 0

    @JvmStatic
    fun toDoubleOrNull(value: String?): Double? = value?.toDoubleOrNull()

    @JvmOverloads
    @JvmStatic
    fun toDouble(value: String?, defaultValue: Double = 0.0): Double = value?.toDoubleOrNull() ?: defaultValue

    @JvmStatic
    fun toIntOrNull(value: String?): Int? = value?.toIntOrNull()

    @JvmOverloads
    @JvmStatic
    fun toInt(value: String?, defaultValue: Int = 0): Int = value?.toIntOrNull() ?: defaultValue

    @JvmStatic
    fun toFloatOrNull(value: String?): Float? = value?.toFloatOrNull()

    @JvmOverloads
    @JvmStatic
    fun toInt(value: String?, defaultValue: Float = 0f): Float = value?.toFloatOrNull() ?: defaultValue

    @JvmOverloads
    @JvmStatic
    fun formatDecimal(value: Double?, decimalLength: Int, defaultValue: String = "0.00"): String = formatDecimalOrNull(value, decimalLength) ?: defaultValue

    @JvmOverloads
    @JvmStatic
    fun formatDecimal(value: String?, decimalLength: Int, defaultValue: String = "0.00"): String = formatDecimalOrNull(value, decimalLength) ?: defaultValue

    /**
     * 四舍五入 decimalLength：小数位数
     */
    @JvmStatic
    fun formatDecimalOrNull(value: Double?, decimalLength: Int): String? {
        var formatValue: String? = null

        value?.let {
            try {
                formatValue = fillDecimalLengthWithZero(BigDecimal(value).setScale(decimalLength, BigDecimal.ROUND_HALF_UP).toDouble(), decimalLength)
            } catch (e: NumberFormatException) {
                CXLogUtil.e(TAG, e)
            }
        }

        return formatValue
    }

    /**
     * 四舍五入 decimalLength：小数位数
     */
    @JvmStatic
    fun formatDecimalOrNull(value: String?, decimalLength: Int): String? {
        var formatValue: String? = null

        value?.let {
            try {
                formatValue = fillDecimalLengthWithZero(BigDecimal(value).setScale(decimalLength, BigDecimal.ROUND_HALF_UP).toDouble(), decimalLength)
            } catch (e: NumberFormatException) {
                CXLogUtil.e(TAG, e)
            }
        }

        return formatValue
    }

    /**
     * 小数点位数达不到指定位数，右补0
     */
    @JvmStatic
    private fun fillDecimalLengthWithZero(value: Double, decimalLength: Int): String {
        var format = "0"
        if (decimalLength > 0) {
            format = "0."
            (0 until decimalLength).forEach { format += "0" }
        }
        return DecimalFormat(format).format(value)
    }

}
