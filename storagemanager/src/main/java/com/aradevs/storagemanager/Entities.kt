package com.aradevs.storagemanager

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "medicine_info")
data class MedicineEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    @ColumnInfo(name = "doctor_name")
    val doctorName: String,
    @ColumnInfo(name = "start_date")
    val startDate: Date,
    @ColumnInfo(name = "repeat_interval")
    val repeatInterval: String,
    val color: String,
    val status: Boolean,
    @ColumnInfo(name = "requires_notification")
    val requiresNotification: Boolean,
)

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val content: String,
    val date: Date,
)

