package com.app.co_opilot.util

import android.os.Build
import java.time.OffsetDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoUnit
import java.util.Date

fun parseDate(date: String): Date {
    val formatter = DateTimeFormatterBuilder()
        .appendPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
        .optionalStart()
        .appendOffsetId()
        .optionalEnd()
        .toFormatter()

    // Use ZonedDateTime or OffsetDateTime
    val dt = OffsetDateTime.parse(date, formatter)

    return Date.from(dt.toInstant())
}


fun formatDateDisplay(date: Date): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    return date.toInstant()
        .atZone(java.time.ZoneId.systemDefault())
        .format(formatter)
}