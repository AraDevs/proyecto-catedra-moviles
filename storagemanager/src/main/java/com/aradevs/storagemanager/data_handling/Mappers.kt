package com.aradevs.storagemanager.data_handling

import com.aradevs.domain.Medicine
import com.aradevs.domain.Notification
import com.aradevs.storagemanager.MedicineEntity
import com.aradevs.storagemanager.NotificationEntity

fun MedicineEntity.toDomain(): Medicine =
    Medicine(id, name, doctorName, startDate, repeatInterval, color)

fun Medicine.toEntity(): MedicineEntity =
    MedicineEntity(id, name, doctorName, startDate, repeatInterval, color)

fun NotificationEntity.toDomain(): Notification =
    Notification(id, medicineId, name, startDate, repeatInterval, current, endDate)

fun Notification.toEntity(): NotificationEntity =
    NotificationEntity(id, medicineId, name, startDate, repeatInterval, current, endDate)