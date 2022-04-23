package com.aradevs.storagemanager

import androidx.room.TypeConverter
import com.c3rberuss.androidutils.dateTimeFormat
import java.text.SimpleDateFormat
import java.util.*

class DateConverter {

    private val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:s", Locale.US)

    @TypeConverter
    fun toDate(date: String?): Date? {
        return date?.let {
            SimpleDateFormat(date.dateTimeFormat, Locale.US).run {
                parse(it)
            }
        }
    }

    @TypeConverter
    fun fromDate(date: Date?): String? {
        return date?.let {
            sdf.format(it)
        }
    }
}