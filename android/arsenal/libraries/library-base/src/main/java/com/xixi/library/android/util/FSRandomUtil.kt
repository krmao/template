package com.xixi.library.android.util

import android.graphics.Color
import java.util.*

/**
 * <pre>
 * author : maokangren
 * e-mail : maokangren@chexiang.com
 * desc   : 获取随机的测试图片地址以及随机的颜色值
 * </pre>
 */
@Suppress("unused")
object FSRandomUtil {
    /**
     * must be start>0 end>0
     */
    fun getRandom(begin: Int, end: Int): Int {
        val absBegin = Math.abs(begin)
        val absEnd = Math.abs(end)
        return (Math.random() * Math.abs(absBegin - absEnd) + if (absBegin > absEnd) absEnd else absBegin).toInt()
    }

    val randomTransparentAvatar: String
        get() = String.format(Locale.getDefault(), "http://odw6aoxik.bkt.clouddn.com/avatar_tranparent_%d.png-200x200", getRandom(1, 15))

    val randomCartoonAvatar: String
        get() = String.format(Locale.getDefault(), "http://odw6aoxik.bkt.clouddn.com/avatar_cartoon_%d.jpg-200x200", getRandom(1, 50))

    val randomColor: Int
        get() {
            val color00 = java.lang.Long.parseLong("ff000000", 16).toInt()
            val colorFF = java.lang.Long.parseLong("ffffffff", 16).toInt()
            val hexColorStr = Integer.toHexString((-(Math.random() * (Math.abs(color00) - Math.abs(colorFF)) + Math.abs(colorFF))).toInt())
            return Color.parseColor(String.format("#%s", hexColorStr))
        }
}
