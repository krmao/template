package com.smart.library.util

import android.graphics.Color
import java.util.*
import kotlin.math.max

/**
 * <pre>
 * author : smart
 * e-mail : smart@smart.com
 * desc   : 获取随机的测试图片地址以及随机的颜色值
 * </pre>
 */
@Suppress("unused", "MemberVisibilityCanPrivate")
object STRandomUtil {
    /**
     * must be start>0 end>0
     */
    fun getRandom(begin: Int, end: Int): Int {
        val absBegin = Math.abs(begin)
        val absEnd = Math.abs(end)
        return (Math.random() * Math.abs(absBegin - absEnd) + if (absBegin > absEnd) absEnd else absBegin).toInt()
    }

    fun getImageUrl(position: Int): String = "https://cdn.mom1.cn/img/mom2018%20(${max(1, position)}).jpg"

    val randomImage: String
        get() = String.format(Locale.getDefault(), "https://cdn.mom1.cn/img/mom2018%20(%d).jpg", getRandom(1, 1000))

    val randomColor: Int
        get() {
            val color00 = java.lang.Long.parseLong("ff000000", 16).toInt()
            val colorFF = java.lang.Long.parseLong("ffffffff", 16).toInt()
            val hexColorStr = Integer.toHexString((-(Math.random() * (Math.abs(color00) - Math.abs(colorFF)) + Math.abs(colorFF))).toInt())
            return Color.parseColor(String.format("#%s", hexColorStr))
        }
}
