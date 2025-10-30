package com.app.co_opilot.util

import android.os.Build
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Date

fun parseDate(date: String): Date {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    val dateTime = ZonedDateTime.parse(date, formatter)
    return Date.from(dateTime.toInstant())
}

fun formatDateDisplay(date: Date): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    return date.toInstant()
        .atZone(java.time.ZoneId.systemDefault())
        .format(formatter)
}