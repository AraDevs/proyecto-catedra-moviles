package com.aradevs.storagemanager

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
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
)

@Entity(
    tableName = "notifications",
    foreignKeys = [
        ForeignKey(
            entity = MedicineEntity::class,
            parentColumns = ["id"],
            childColumns = ["medicine_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.NO_ACTION
        )
    ],
)
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "medicine_id")
    val medicineId: Int,
    val name: String,
    @ColumnInfo(name = "start_date")
    val startDate: Date,
    @ColumnInfo(name = "repeat_interval")
    val repeatInterval: String,
    val current: Boolean,
    @ColumnInfo(name = "end_date")
    val endDate: Date?,
)
