package com.smart.library.util

import java.math.BigDecimal

@Suppress("unused")
object HKBigDecimalUtil {

    //四舍五入 decimalLength：小数位数
    fun formatValue(value: Double, decimalLength: Int): Double =
        BigDecimal(value).setScale(decimalLength, BigDecimal.ROUND_HALF_UP).toDouble()

    //四舍五入 decimalLength：小数位数
    fun formatValue(value: Float, decimalLength: Int): Float =
        BigDecimal(value.toDouble()).setScale(decimalLength, BigDecimal.ROUND_HALF_UP).toFloat()
}
