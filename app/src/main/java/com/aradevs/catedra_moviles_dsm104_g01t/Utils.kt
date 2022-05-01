package com.aradevs.catedra_moviles_dsm104_g01t

import com.aradevs.domain.SpanType
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.ceil

fun Date.isWithinNextHour(timeSpan: Int): Boolean {
    return getClosestDate(timeSpan) != null
}

fun Date.getClosestDate(timeSpan: Int): Date? {
    val calendar: Calendar = Calendar.getInstance()
    calendar.time = Date()
    calendar.add(Calendar.MINUTE, 60)
    val next24HDates = this.requireFutureDates(timeSpan, SpanType.DAY)
    val nextHourDate: Date? = next24HDates.firstOrNull {
        it.before(calendar.time) && it.after(Date())
    }
    return nextHourDate
}

fun Date.requireFutureDates(timeSpan: Int, spanType: SpanType): List<Date> {
    val calendar: Calendar = Calendar.getInstance()
    calendar.time = this
    val hoursValue: Double = when (spanType) {
        SpanType.DAY -> 24.0
        SpanType.WEEK -> 168.0
    }
    val timesToRepeat = ceil(hoursValue / timeSpan)
    val listOfDates: MutableList<Date> = mutableListOf()
    var counter = 0
    calendar.add(Calendar.DAY_OF_YEAR, -1)
    while (counter <= timesToRepeat + 1) {
        calendar.add(Calendar.MINUTE, 60 * timeSpan)
        listOfDates.add(calendar.time)
        counter++
    }
    return listOfDates
}

fun Date.setAsTodayDate(): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    val timeFormat = SimpleDateFormat("HH:mm:s", Locale.US)
    val fullTimeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:s", Locale.US)
    val currentTimeAsString = "${Date().toYearMonthDay()} ${timeFormat.format(this)}"
    return fullTimeFormat.parse(currentTimeAsString) ?: Date()
}

fun Date.setAsProvidedDate(providedDate: Date): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    val timeFormat = SimpleDateFormat("HH:mm:s", Locale.US)
    val fullTimeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:s", Locale.US)
    val currentTimeAsString = "${providedDate.toYearMonthDay()} ${timeFormat.format(this)}"
    return fullTimeFormat.parse(currentTimeAsString) ?: providedDate
}

fun Date.toYearMonthDay(): String {
    val formatter: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    return formatter.format(this)
}