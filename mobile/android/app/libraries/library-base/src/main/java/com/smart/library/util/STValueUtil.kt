package com.smart.library.util

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import org.jetbrains.anko.AnkoAsyncContext
import java.math.BigDecimal
import java.text.DecimalFormat
import kotlin.math.cos
import kotlin.math.sin

@Suppress("unused", "MemberVisibilityCanBePrivate", "ReplaceCallWithBinaryOperator")
object STValueUtil {
    private val TAG = STValueUtil::class.java.name

    @Volatile
    private var lastClickedTime: Long = 0L

    @JvmStatic
    @JvmOverloads
    fun isDoubleClicked(interval: Int = 200): Boolean {
        val isDoubleClicked = (System.currentTimeMillis() - lastClickedTime) <= interval // double check
        lastClickedTime = System.currentTimeMillis()
        return isDoubleClicked
    }

    @JvmStatic
    fun isValid(context: Context?): Boolean {
        if (context != null) {
            if (context is Activity) {
                if (!context.isFinishing) return true
            } else {
                return true
            }
        }
        return false
    }

    @JvmStatic
    fun isValid(context: AnkoAsyncContext<*>?): Boolean = context?.weakRef?.get() != null

    @JvmStatic
    fun isValid(fragment: Fragment?): Boolean = fragment != null && !fragment.isDetached

    @Suppress("DEPRECATION")
    @JvmStatic
    fun isValid(fragment: android.app.Fragment?): Boolean = fragment != null && !fragment.isDetached

    /**
     * 是否 A 等于 B
     */
    @JvmStatic
    fun isAEqualB(valueA: Double?, valueB: Double?): Boolean = if (valueA == null || valueB == null) false else isAEqualB(BigDecimal.valueOf(valueA), BigDecimal.valueOf(valueB))

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
    fun toDouble(value: String?, defaultValue: Double = 0.0): Double = value?.toDoubleOrNull()
        ?: defaultValue

    @JvmStatic
    fun toIntOrNull(value: String?): Int? = value?.toIntOrNull()

    @JvmStatic
    @JvmOverloads
    fun wrap0Left(intValue: Any, fullLength: Int = 2): String {
        return when (intValue) {
            is String -> {
                String.format("%0${fullLength}d", intValue.toIntOrNull() ?: 0)
            }
            is Number -> {
                String.format("%0${fullLength}d", intValue.toInt())
            }
            else -> {
                String.format("%0${fullLength}d", 0)
            }
        }
    }

    @JvmOverloads
    @JvmStatic
    fun toInt(value: String?, defaultValue: Int = 0): Int = value?.toIntOrNull() ?: defaultValue

    @JvmStatic
    fun toFloatOrNull(value: String?): Float? = value?.toFloatOrNull()

    @JvmOverloads
    @JvmStatic
    fun toInt(value: String?, defaultValue: Float = 0f): Float = value?.toFloatOrNull()
        ?: defaultValue

    @JvmOverloads
    @JvmStatic
    fun formatDecimal(value: Double?, decimalLength: Int, defaultValue: String = "0.00"): String = formatDecimalOrNull(value, decimalLength)
        ?: defaultValue

    @JvmOverloads
    @JvmStatic
    fun formatDecimal(value: String?, decimalLength: Int, defaultValue: String = "0.00"): String = formatDecimalOrNull(value, decimalLength)
        ?: defaultValue

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
                STLogUtil.e(TAG, e)
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
                STLogUtil.e(TAG, e)
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
            (0 until decimalLength).forEach { _ -> format += "0" }
        }
        try {
            return DecimalFormat(format).format(value)
        } catch (e: Exception) {
            STLogUtil.e(TAG, e)
            return "$value"
        }
    }

    /**
     * @param progress 进度 [0, 0.5, 1]
     * @return [0, 1, 0]
     */
    @JvmStatic
    fun getSinValue(progress: Float): Double = sin(Math.toRadians(180.0 * progress))

    /**
     * @param progress 进度 [0, 0.5, 1]
     * @return [1, 0, -1]
     */
    @JvmStatic
    fun getCosValue(progress: Float): Double = cos(Math.toRadians(180.0 * progress))
}
