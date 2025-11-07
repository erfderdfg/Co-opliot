package com.app.co_opilot.util

import android.os.Build
import java.time.OffsetDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField
import java.time.temporal.ChronoUnit
import java.util.Date

fun parseDate(date: String): Date {
    val formatter: DateTimeFormatter = DateTimeFormatterBuilder()
        .appendPattern("yyyy-MM-dd'T'HH:mm:ss")
        .optionalStart()
        .appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true) // 0–9 fractional digits
        .optionalEnd()
        .optionalStart()
        .appendOffsetId() // e.g. +00:00, -05:30, Z
        .optionalEnd()
        .toFormatter()

    val odt = OffsetDateTime.parse(date, formatter)
    return Date.from(odt.toInstant())
}


fun formatDateDisplay(date: Date): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    return date.toInstant()
        .atZone(java.time.ZoneId.systemDefault())
        .format(formatter)
}