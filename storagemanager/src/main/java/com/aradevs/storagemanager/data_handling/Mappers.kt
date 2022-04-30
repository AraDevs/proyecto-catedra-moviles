package com.aradevs.storagemanager.data_handling

import com.aradevs.domain.Medicine
import com.aradevs.storagemanager.MedicineEntity

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
