package com.jivosite.sdk.support.utils

import java.text.SimpleDateFormat
import java.util.*

fun Long.after() = this > System.currentTimeMillis()

fun convertTimeMillisToDateFormat(date: Long, pattern: String = "HH:mm:ss dd.MM.yy"): String {
    val dateFormat = SimpleDateFormat(pattern, Locale("en"))
    return dateFormat.format(Date(date))
}