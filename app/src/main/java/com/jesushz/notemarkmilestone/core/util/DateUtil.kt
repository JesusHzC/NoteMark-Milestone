package com.jesushz.notemarkmilestone.core.util

import java.time.Instant
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale


/**
 * Convert millis to ISO 8601 duration format.
 */
fun Long.toISO8601Duration(): String {
    return Instant.ofEpochMilli(this)
        .let { DateTimeFormatter.ISO_INSTANT.format(it) }
}

/**
 * Convert an ISO 8601 date string to a formatted string with day and month.
 * Example: "2023-10-05T14:30:00Z" -> "05 OCT"
 */
fun String.toDayMonthFormat(): String {
    val parsedDate = OffsetDateTime.parse(this)
    val formatter = DateTimeFormatter.ofPattern("dd MMM", Locale.getDefault())
    return parsedDate.format(formatter).uppercase()
}
