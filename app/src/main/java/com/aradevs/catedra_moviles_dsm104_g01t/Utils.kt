package com.aradevs.catedra_moviles_dsm104_g01t

import com.c3rberuss.androidutils.toDayMonthYearHour
import timber.log.Timber
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.ceil

fun Date.weekOfYear(): Int {
    val calendar: Calendar = Calendar.getInstance()
    calendar.time = this
    return calendar.get(Calendar.WEEK_OF_YEAR)
}

fun Date.isWithinNextHour(timeSpan: Int): Boolean {
    val calendar: Calendar = Calendar.getInstance()
    calendar.time = Date()
    calendar.add(Calendar.MINUTE, 60)
    val next24HDates = this.requireDatesOfNext24H(timeSpan)
    val nextHourDate: Date? = next24HDates.firstOrNull {
        it.before(calendar.time) && it.after(Date())
    }
    nextHourDate?.let {
        Timber.d("parsed"+it.toString())
    }
    return nextHourDate != null
}

fun Date.getClosestDate(timeSpan: Int): Date {
    val calendar: Calendar = Calendar.getInstance()
    calendar.time = Date()
    calendar.add(Calendar.MINUTE, 60)
    val next24HDates = this.requireDatesOfNext24H(timeSpan)
    val nextHourDate: Date? = next24HDates.firstOrNull {
        it.after(Date()) && it.before(calendar.time)
    }
    return nextHourDate ?: Date()
}

fun Date.requireDatesOfNext24H(timeSpan: Int): List<Date> {
    val calendar: Calendar = Calendar.getInstance()
    calendar.time = this
    Timber.d(calendar.time.toString())
    val timesToRepeat = ceil(24.0 / timeSpan)
    val listOfDates: MutableList<Date> = mutableListOf()
    var counter = 0
    calendar.add(Calendar.DAY_OF_YEAR,-1)
    while (counter <= timesToRepeat) {
        calendar.add(Calendar.MINUTE, 60 * timeSpan)
        Timber.d(calendar.time.toString())
        listOfDates.add(calendar.time)
        counter++
    }
    Timber.d("dates$listOfDates")
    return listOfDates
}

fun Date.requireDatesOfNextWeek(timeSpan: Int): List<Date> {
    val calendar: Calendar = Calendar.getInstance()
    calendar.time = this
    Timber.d(calendar.time.toString())
    val timesToRepeat = ceil(168.0 / timeSpan)
    val listOfDates: MutableList<Date> = mutableListOf()
    var counter = 0
    calendar.add(Calendar.DAY_OF_YEAR,-1)
    while (counter <= timesToRepeat) {
        calendar.add(Calendar.MINUTE, 60 * timeSpan)
        Timber.d(calendar.time.toString())
        listOfDates.add(calendar.time)
        counter++
    }
    Timber.d("dates$listOfDates")
    return listOfDates
}

fun Date.setAsTodayDate(): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    val timeFormat = SimpleDateFormat("HH:mm:s", Locale.US)
    val fullTimeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:s", Locale.US)
    val currentTimeAsString = "${Date().toYearMonthDay()} ${timeFormat.format(this)}"
    Timber.d((fullTimeFormat.parse(currentTimeAsString) ?: Date()).toDayMonthYearHour())
    return fullTimeFormat.parse(currentTimeAsString) ?: Date()
}

fun Date.toYearMonthDay(): String {
    val formatter: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    return formatter.format(this)
}