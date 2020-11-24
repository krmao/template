package com.smart.library.util

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
object STTimeUtil {

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

    @JvmStatic
    fun getTimeSlotDescription(pattern: String, value: String): Date? = SimpleDateFormat(pattern, Locale.getDefault()).parse(value)

    //-----------------------------------------------------------------------------------------------------------
    // Calendar start
    //-----------------------------------------------------------------------------------------------------------
    /**
     * 哪一年
     */
    fun getYear(timeInMillis: Long): Int {
        val calendar: Calendar = GregorianCalendar.getInstance(Locale.getDefault())
        calendar.timeInMillis = timeInMillis
        return calendar[Calendar.YEAR]
    }

    /**
     * 哪个月
     */
    fun getMonth(timeInMillis: Long): Int {
        val calendar: Calendar = GregorianCalendar.getInstance(Locale.getDefault())
        calendar.timeInMillis = timeInMillis
        return calendar[Calendar.MONTH]
    }

    /**
     * 星期几
     */
    fun getDayOfWeek(timeInMillis: Long): Int {
        val calendar: Calendar = GregorianCalendar.getInstance(Locale.getDefault())
        calendar.timeInMillis = timeInMillis
        return calendar[Calendar.DAY_OF_WEEK]
    }

    /**
     * 获取星期的中文描述
     */
    fun getDayOfWeekStr(dayOfWeek: Int): String? = when (dayOfWeek) {
        1 -> "星期天"
        2 -> "星期一"
        3 -> "星期二"
        4 -> "星期三"
        5 -> "星期四"
        6 -> "星期五"
        7 -> "星期六"
        else -> null
    }

    /**
     * 今天的最小时间
     * ----
     * 假如现在是        2020/02/21 11:48:22 456
     * 则今天的最小时间   2020/02/21 00:00:00 000
     */
    val minTodayInMillis: Long
        get() {
            val calendar: Calendar = Calendar.getInstance(Locale.getDefault())
            calendar[Calendar.HOUR_OF_DAY] = 0
            calendar[Calendar.MINUTE] = 0
            calendar[Calendar.SECOND] = 0
            calendar[Calendar.MILLISECOND] = 0
            return calendar.timeInMillis
        }

    /**
     * 今天的最大时间
     * ----
     * 假如现在是        2020/02/21 11:48:22 456
     * 则今天的最小时间   2020/02/21 23:59:59 999
     */
    val maxTodayInMillis: Long
        get() {
            val calendar: Calendar = Calendar.getInstance(Locale.getDefault())
            calendar[Calendar.HOUR_OF_DAY] = 23
            calendar[Calendar.MINUTE] = 59
            calendar[Calendar.SECOND] = 59
            calendar[Calendar.MILLISECOND] = 999
            return calendar.timeInMillis
        }


    /**
     * 昨天的最小时间
     * ----
     * 比如当前时间是     2020/02/21 11:48:22 456
     * 则昨天的最小时间   2020/02/20 00:00:00 000
     */
    val minYesterdayInMillis: Long
        get() {
            val calendar: Calendar = GregorianCalendar.getInstance(Locale.getDefault())
            calendar[Calendar.DAY_OF_YEAR] = calendar[Calendar.DAY_OF_YEAR] - 1
            calendar[Calendar.HOUR_OF_DAY] = 0
            calendar[Calendar.MINUTE] = 0
            calendar[Calendar.SECOND] = 0
            calendar[Calendar.MILLISECOND] = 0
            return calendar.timeInMillis
        }

    /**
     * 当前周的第一天的最小时间
     * ----
     * 比如当前时间是          2020/02/21 11:48:22 456
     * 则当前周的第一天的时间   2020/02/16 00:00:00 000  02/16是星期天 这星期的最小值
     */
    val minDayOfCurrentWeekInMillis: Long
        get() {
            val calendar: Calendar = GregorianCalendar.getInstance(Locale.getDefault())
            calendar[Calendar.DAY_OF_WEEK] = calendar.getMinimum(Calendar.DAY_OF_WEEK)
            calendar[Calendar.HOUR_OF_DAY] = 0
            calendar[Calendar.MINUTE] = 0
            calendar[Calendar.SECOND] = 0
            calendar[Calendar.MILLISECOND] = 0
            return calendar.timeInMillis
        }

    /**
     * 当前月的第一天的最小时间
     * 比如当前时间是             2020/02/21 11:48:22 456
     * 则当前月的第一天的最小时间   2020/02/01 00:00:00 000
     */
    val minDayOfCurrentMonthInMillis: Long
        get() {
            val calendar: Calendar = GregorianCalendar.getInstance(Locale.getDefault())
            calendar[Calendar.DAY_OF_MONTH] = calendar.getMinimum(Calendar.DAY_OF_MONTH)
            calendar[Calendar.HOUR_OF_DAY] = 0
            calendar[Calendar.MINUTE] = 0
            calendar[Calendar.SECOND] = 0
            calendar[Calendar.MILLISECOND] = 0
            return calendar.timeInMillis
        }

    /**
     * 今年的第一天的最小时间
     * ----
     * 比如当前时间是             2020/02/21 11:48:22 456
     * 则当前年的第一天的最小时间   2020/01/01 00:00:00 000
     */
    val minDayOfCurrentYearInMillis: Long
        get() {
            val calendar: Calendar = GregorianCalendar.getInstance(Locale.getDefault())
            calendar[Calendar.DAY_OF_YEAR] = calendar.getMinimum(Calendar.DAY_OF_YEAR)
            calendar[Calendar.HOUR_OF_DAY] = 0
            calendar[Calendar.MINUTE] = 0
            calendar[Calendar.SECOND] = 0
            calendar[Calendar.MILLISECOND] = 0
            return calendar.timeInMillis
        }

    /**
     * 获取指定时间的描述
     * ----
     * 今天/昨天/星期几/这个月/几月/几年
     * 可用于本地图片的时间归类
     */
    fun getTimeSlotDescription(timeInMillis: Long): String? = when {
        timeInMillis in minTodayInMillis..maxTodayInMillis -> { //[2020/02/21 00:00:00 - 2020/02/21 11:59:59]
            "今天"
        }
        timeInMillis in minYesterdayInMillis until minTodayInMillis -> { //[2020/02/20 00:00:00 - 2020/02/21 00:00:00)
            "昨天"
        }
        timeInMillis in minDayOfCurrentWeekInMillis until minYesterdayInMillis -> { //[2020/02/16 00:00:00 - 2020/02/20 00:00:00) 02/16是星期天 这星期的最小值
            getDayOfWeekStr(getDayOfWeek(timeInMillis))
        }
        timeInMillis in minDayOfCurrentMonthInMillis until minDayOfCurrentWeekInMillis -> { //[2020/02/01 00:00:00 - 2020/02/16 00:00:00)
            "这个月"
        }
        timeInMillis in minDayOfCurrentYearInMillis until minDayOfCurrentMonthInMillis -> { //[2020/01/01 00:00:00 - 2020/02/16 00:00:00)
            "${getMonth(timeInMillis) + 1} 月"
        }
        timeInMillis < minDayOfCurrentYearInMillis -> {//[timeInMillis - 2020/01/01 00:00:00)
            "${getYear(timeInMillis)} 年"
        }
        else -> null
    }

    //-----------------------------------------------------------------------------------------------------------
    // Calendar end
    //-----------------------------------------------------------------------------------------------------------
}
