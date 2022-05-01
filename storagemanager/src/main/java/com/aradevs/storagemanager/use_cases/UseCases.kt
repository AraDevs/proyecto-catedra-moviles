package com.aradevs.storagemanager.use_cases

import com.aradevs.domain.Medicine
import com.aradevs.domain.Notification
import com.aradevs.storagemanager.repositories.DatabaseRepository

class SaveMedicineUseCase(private val repository: DatabaseRepository) {
    suspend operator fun invoke(medicine: Medicine) =
        repository.saveMedicine(medicine)
}

class GetMedicinesUseCase(private val repository: DatabaseRepository) {
    suspend operator fun invoke() = repository.getMedicines()
}

class GetAllMedicinesUseCase(private val repository: DatabaseRepository) {
    suspend operator fun invoke() = repository.getAllMedicines()
}

class DeactivateMedicineUseCase(private val repository: DatabaseRepository) {
    suspend operator fun invoke(medicine: Medicine) = repository.deactivateMedicine(medicine)
}

class DeleteMedicineUseCase(private val repository: DatabaseRepository) {
    suspend operator fun invoke(medicineId: Int) = repository.deleteMedicine(medicineId)
}

class SaveNotificationUseCase(private val repository: DatabaseRepository) {
    suspend operator fun invoke(notification: Notification) =
        repository.saveNotification(notification)
}

class GetNotificationsUseCase(private val repository: DatabaseRepository) {
    suspend operator fun invoke() = repository.getNotifications()
}

class DeleteNotificationUseCase(private val repository: DatabaseRepository) {
    suspend operator fun invoke(notificationId: Int) = repository.deleteNotification(notificationId)
}

class DeleteNotificationsUseCase(private val repository: DatabaseRepository) {
    suspend operator fun invoke() = repository.deleteNotifications()
}



