package com.smart.library.util

import android.graphics.Color
import androidx.annotation.Keep
import java.util.*
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

/**
 * <pre>
 * author : smart
 * e-mail : smart@smart.com
 * desc   : 获取随机的测试图片地址以及随机的颜色值
 * </pre>
 */
@Suppress("unused", "MemberVisibilityCanPrivate")
@Keep
object STRandomUtil {
    /**
     * must be start>0 end>0
     */
    fun getRandom(begin: Int, end: Int): Int {
        val absBegin: Int = abs(begin)
        val absEnd: Int = abs(end)
        return (Math.random() * abs(absBegin - absEnd) + if (absBegin > absEnd) absEnd else absBegin).toInt()
    }

    /**
     * @param position range must be in [1,1002]
     */
    fun getImageUrl(position: Int): String = "https://cdn.mom1.cn/img/mom2018%20(${min(1002, max(1, position + 1))}).jpg"

    /**
     * random in [1,1002]
     */
    val randomImage: String
        get() = String.format(Locale.getDefault(), "https://cdn.mom1.cn/img/mom2018%20(%d).jpg", getRandom(1, 1002))

    val randomColor: Int
        get() {
            val color00 = java.lang.Long.parseLong("ff000000", 16).toInt()
            val colorFF = java.lang.Long.parseLong("ffffffff", 16).toInt()
            val hexColorStr = Integer.toHexString((-(Math.random() * (abs(color00) - abs(colorFF)) + abs(colorFF))).toInt())
            return Color.parseColor(String.format("#%s", hexColorStr))
        }
}
