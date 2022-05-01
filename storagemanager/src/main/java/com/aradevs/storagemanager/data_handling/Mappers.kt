package com.aradevs.storagemanager.data_handling

import com.aradevs.domain.Medicine
import com.aradevs.domain.Notification
import com.aradevs.storagemanager.MedicineEntity
import com.aradevs.storagemanager.NotificationEntity

fun MedicineEntity.toDomain(): Medicine =
    Medicine(id, name, doctorName, startDate, repeatInterval, color, status, requiresNotification)

fun Medicine.toEntity(): MedicineEntity =
    MedicineEntity(id,
        name,
        doctorName,
        startDate,
        repeatInterval,
        color,
        status,
        requiresNotification)

fun NotificationEntity.toDomain(): Notification =
    Notification(id, content, date)

fun Notification.toEntity(): NotificationEntity =
    NotificationEntity(id, content, date)