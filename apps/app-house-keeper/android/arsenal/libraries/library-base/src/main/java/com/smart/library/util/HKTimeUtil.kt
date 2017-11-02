package com.smart.library.util

import java.text.SimpleDateFormat
import java.util.*

@Suppress("unused", "MemberVisibilityCanPrivate")
object HKTimeUtil {

    fun formatNow(): String = format(Date())

    fun format(date: Date): String =
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS", Locale.getDefault()).format(date)

}
