package com.smart.library.util

import android.os.CountDownTimer
import androidx.annotation.Keep
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
@Keep
@Suppress("unused", "MemberVisibilityCanPrivate", "FunctionName", "MemberVisibilityCanBePrivate")
object STTimeUtil {

    /**
     * yyyyMMddHHmmssSSS
     */
    @JvmStatic
    @JvmOverloads
    fun yMdHmsSWithoutSeparator(date: Long = System.currentTimeMillis()): String = format("yyyyMMddHHmmssSSS", Date(date))

    @JvmStatic
    @JvmOverloads
    fun yMdHmsS(calendar: Calendar, includeMillisecond: Boolean = true): String = yMdHmsS(getDateFromCalendar(calendar), includeMillisecond)

    @JvmStatic
    fun getDateFromCalendar(calendar: Calendar): Date = calendar.time

    @JvmStatic
    fun getCalendarFromDate(date: Date): Calendar = Calendar.getInstance().apply { time = date }

    /**
     * yyyy-MM-dd HH:mm:ss SSS
     */
    @JvmStatic
    @JvmOverloads
    fun yMdHmsS(date: Long = System.currentTimeMillis(), includeMillisecond: Boolean = true): String = yMdHmsS(Date(date), includeMillisecond)

    @JvmStatic
    @JvmOverloads
    fun yMdHmsS(date: Date, includeMillisecond: Boolean = true): String = format("yyyy-MM-dd HH:mm:ss" + (if (includeMillisecond) " SSS" else ""), date)

    @JvmStatic
    fun yMdHms(date: Date): String = yMdHmsS(date, false)

    @JvmStatic
    fun yMdHms(calendar: Calendar): String = yMdHmsS(getDateFromCalendar(calendar), false)

    @JvmStatic
    @JvmOverloads
    fun yMdHms(date: Long = System.currentTimeMillis()): String = yMdHmsS(Date(date), false)

    @JvmStatic
    @JvmOverloads
    fun HmsS(calendar: Calendar, includeMillisecond: Boolean = true): String = HmsS(getDateFromCalendar(calendar), includeMillisecond)

    @JvmStatic
    @JvmOverloads
    fun HmsS(date: Long = System.currentTimeMillis(), includeMillisecond: Boolean = true): String = HmsS(Date(date), includeMillisecond)

    @JvmStatic
    fun HmsS(date: Date, includeMillisecond: Boolean = true): String = format("HH:mm:ss" + (if (includeMillisecond) " SSS" else ""), date)

    @JvmStatic
    fun Hms(calendar: Calendar): String = Hms(getDateFromCalendar(calendar))

    @JvmStatic
    @JvmOverloads
    fun Hms(date: Long = System.currentTimeMillis()): String = Hms(Date(date))

    @JvmStatic
    fun Hms(date: Date): String = HmsS(date, false)

    @JvmStatic
    @JvmOverloads
    fun wrap0Left(intValue: Any, fullLength: Int = 2): String = STValueUtil.wrap0Left(intValue, fullLength)

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
    @JvmStatic
    @JvmOverloads
    fun getYear(timeInMillis: Long = System.currentTimeMillis()): Int {
        val calendar: Calendar = GregorianCalendar.getInstance(Locale.getDefault())
        calendar.timeInMillis = timeInMillis
        return calendar[Calendar.YEAR]
    }

    /**
     * 哪个月
     */
    @JvmStatic
    @JvmOverloads
    fun getMonth(timeInMillis: Long = System.currentTimeMillis()): Int {
        val calendar: Calendar = GregorianCalendar.getInstance(Locale.getDefault())
        calendar.timeInMillis = timeInMillis
        return calendar[Calendar.MONTH]
    }

    @JvmStatic
    @JvmOverloads
    fun ymdCHINA(date: Long = System.currentTimeMillis()): String {
        return format("yyyy年MM月dd日", Date(date))
    }

    @JvmStatic
    @JvmOverloads
    fun getDayOfMonth(timeInMillis: Long = System.currentTimeMillis()): Int {
        val calendar: Calendar = GregorianCalendar.getInstance(Locale.getDefault())
        calendar.timeInMillis = timeInMillis
        return calendar[Calendar.DAY_OF_MONTH]
    }

    /**
     * 星期几
     */
    @JvmStatic
    @JvmOverloads
    fun getDayOfWeek(timeInMillis: Long = System.currentTimeMillis()): Int {
        val calendar: Calendar = GregorianCalendar.getInstance(Locale.getDefault())
        calendar.timeInMillis = timeInMillis
        return calendar[Calendar.DAY_OF_WEEK]
    }

    /**
     * 获取星期的中文描述
     */
    @JvmStatic
    @JvmOverloads
    fun getDayOfWeekStr(dayOfWeek: Int = getDayOfWeek()): String? = when (dayOfWeek) {
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
     * 获取星座
     */
    @JvmStatic
    @JvmOverloads
    fun getHoroscopeCHINA(month: Int = getMonth(), dayOfMonth: Int = getDayOfMonth()): String? {
        return if (month == 3 && dayOfMonth >= 21 || month == 4 && dayOfMonth <= 19) {
            "白羊座"
        } else if (month == 4 && dayOfMonth >= 20 || month == 5 && dayOfMonth <= 20) {
            "金牛座"
        } else if (month == 5 && dayOfMonth >= 21 || month == 6 && dayOfMonth <= 21) {
            "双子座"
        } else if (month == 6 && dayOfMonth >= 22 || month == 7 && dayOfMonth <= 22) {
            "巨蟹座"
        } else if (month == 7 && dayOfMonth >= 23 || month == 8 && dayOfMonth <= 22) {
            "狮子座"
        } else if (month == 8 && dayOfMonth >= 23 || month == 9 && dayOfMonth <= 22) {
            "处女座"
        } else if (month == 9 && dayOfMonth >= 23 || month == 10 && dayOfMonth <= 23) {
            "天秤座"
        } else if (month == 10 && dayOfMonth >= 24 || month == 11 && dayOfMonth <= 22) {
            "天蝎座"
        } else if (month == 11 && dayOfMonth >= 23 || month == 12 && dayOfMonth <= 21) {
            "射手座"
        } else if (month == 12 && dayOfMonth >= 22 || month == 1 && dayOfMonth <= 19) {
            "摩羯座"
        } else if (month == 1 && dayOfMonth >= 20 || month == 2 && dayOfMonth <= 18) {
            "水瓶座"
        } else {
            "双鱼座"
        }
    }

    /**
     * 倒计时
     * @param millisInFuture 总时长
     * @param countDownInterval 倒计时间隔
     * @param onTick 倒计时监听, 主线程回调
     * @example
     *
     * ---
     *
     *   private var countDownTimer: CountDownTimer? = null
     *
     * ---
     *   countDownTimer = STTimeUtil.countDown(
     *       millisInFuture = (data.overview?.left_time ?: 0) * 1000L,
     *       countDownInterval = 1000L,
     *       onTick = { millisUntilFinished ->
     *           if (activity?.isFinishing != true) {
     *           val day: Long = millisUntilFinished / (1000 * 24 * 60 * 60)
     *           val hour: Long = (millisUntilFinished - day * (1000 * 24 * 60 * 60)) / (1000 * 60 * 60)
     *           val minute: Long = (millisUntilFinished - day * (1000 * 24 * 60 * 60) - hour * (1000 * 60 * 60)) / (1000 * 60)
     *           val second: Long = (millisUntilFinished - day * (1000 * 24 * 60 * 60) - hour * (1000 * 60 * 60) - minute * (1000 * 60)) / 1000
     *           binding.leftTimeTv.text = "倒计时: ${STTimeUtil.wrap0Left(hour)}:${STTimeUtil.wrap0Left(minute)}:${STTimeUtil.wrap0Left(second)}"
     *       }
     *       },
     *       onFinish = {}
     *   )
     *   countDownTimer?.start()
     *
     * ---
     *
     *   override fun onDestroy() {
     *       super.onDestroy()
     *       countDownTimer?.cancel()
     *       countDownTimer = null
     *   }
     *
     * ---
     */
    @JvmStatic
    @JvmOverloads
    fun countDown(millisInFuture: Long = 30 * 1000L, countDownInterval: Long = 1000L, onTick: (millisUntilFinished: Long) -> Unit = {}, onFinish: () -> Unit = {}): CountDownTimer {
        return object : CountDownTimer(millisInFuture, countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                onTick(millisUntilFinished)
            }

            override fun onFinish() {
                onFinish()
            }
        }
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
