package com.smart.library.util

import okhttp3.internal.http.HttpDate
import java.text.SimpleDateFormat
import java.util.*

/**
 * 因为SimpleDateFormat和DateFormat类不是线程安全的,所以不将SimpleDateFormat作为静态变量共享使用
 *
 * Synchronization：
 * Date formats are not synchronized.
 * It is recommended to create separate format instances for each thread.
 * If multiple threads access a format concurrently, it must be synchronized externally.
 *
 * <p>
 *       G 年代标志符
 *       y 年
 *       M 月
 *       d 日
 *       h 时 在上午或下午 (1~12)
 *       H 时 在一天中 (0~23)
 *       m 分
 *       s 秒
 *       S 毫秒
 *       E 星期
 *       D 一年中的第几天
 *       F 一月中第几个星期几
 *       w 一年中第几个星期
 *       W 一月中第几个星期
 *       a 上午 / 下午 标记符
 *       k 时 在一天中 (1~24)
 *       K 时 在上午或下午 (0~11)
 *       z 时区
 * </p>
 */
@Suppress("unused", "MemberVisibilityCanPrivate", "FunctionName", "MemberVisibilityCanBePrivate")
object CXTimeUtil {

    /**
     * yyyyMMddHHmmssSSS
     */
    @JvmStatic
    @JvmOverloads
    fun yMdHmsSWithoutSeparator(date: Long = System.currentTimeMillis()): String = format("yyyyMMddHHmmssSSS", Date(date))

    /**
     * yyyy-MM-dd HH:mm:ss SSS
     */
    @JvmStatic
    @JvmOverloads
    fun yMdHmsS(date: Long = System.currentTimeMillis()): String = yMdHmsS(Date(date))

    @JvmStatic
    fun yMdHmsS(date: Date): String = format("yyyy-MM-dd HH:mm:ss SSS", date)

    @JvmStatic
    @JvmOverloads
    fun HmsS(date: Long = System.currentTimeMillis()): String = HmsS(Date(date))

    @JvmStatic
    fun HmsS(date: Date): String = format("HH:mm:ss SSS", date)

    @JvmStatic
    @JvmOverloads
    fun Hms(date: Long = System.currentTimeMillis()): String = Hms(Date(date))

    @JvmStatic
    fun Hms(date: Date): String = format("HH:mm:ss", date)

    /**
     * H 时 在一天中 (0~23)
     */
    @JvmStatic
    fun HH(date: Date): String = format("HH", date)

    @JvmStatic
    @JvmOverloads
    fun HH(date: Long = System.currentTimeMillis()): String = HH(Date(date))

    @JvmStatic
    fun format(pattern: String, date: Date): String = SimpleDateFormat(pattern, Locale.getDefault()).format(date)

    /**
     * @see okhttp3.internal.http.HttpDate.parse
     */
    @JvmStatic
    fun parseHttp(httpValue: String): Date = HttpDate.parse(httpValue)

    @JvmStatic
    fun parse(pattern: String, value: String): Date = SimpleDateFormat(pattern, Locale.getDefault()).parse(value)
}
