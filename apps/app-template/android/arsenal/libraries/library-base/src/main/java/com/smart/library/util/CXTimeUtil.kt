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
@Suppress("unused", "MemberVisibilityCanPrivate", "FunctionName")
object CXTimeUtil {

    fun yMdHmsS(): String = yMdHmsS(Date())
    fun yMdHmsS(date: Long): String = yMdHmsS(Date(date))
    fun yMdHmsS(date: Date): String = format("yyyy-MM-dd HH:mm:ss SSS", date)

    fun HmsS(): String = HmsS(Date())
    fun HmsS(date: Long): String = HmsS(Date(date))
    fun HmsS(date: Date): String = format("HH:mm:ss SSS", date)

    fun Hms(): String = Hms(Date())
    fun Hms(date: Long): String = Hms(Date(date))
    fun Hms(date: Date): String = format("HH:mm:ss", date)

    fun format(pattern: String, date: Date): String =
        SimpleDateFormat(pattern, Locale.getDefault()).format(date)

    /**
     * @see okhttp3.internal.http.HttpDate.parse
     */
    fun parseHttp(httpValue: String): Date = HttpDate.parse(httpValue)

    fun parse(pattern: String, value: String): Date = SimpleDateFormat(pattern, Locale.getDefault()).parse(value)
}
