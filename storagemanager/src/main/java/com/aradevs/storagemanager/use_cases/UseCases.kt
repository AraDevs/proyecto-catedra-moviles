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

class UpdateMedicineUseCase(private val repository: DatabaseRepository) {
    suspend operator fun invoke(medicine: Medicine) = repository.updateMedicine(medicine)
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

class DeactivatePreviousNotificationsUseCase(private val repository: DatabaseRepository) {
    suspend operator fun invoke(medicineId: Int) =
        repository.deactivatePreviousNotifications(medicineId)
}

class DeleteCurrentNotificationsUseCase(private val repository: DatabaseRepository) {
    suspend operator fun invoke(medicineId: Int) = repository.deleteCurrentNotifications(medicineId)
}



