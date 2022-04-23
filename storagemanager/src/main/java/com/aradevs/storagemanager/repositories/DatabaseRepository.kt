package com.aradevs.storagemanager.repositories

import com.aradevs.domain.Medicine
import com.aradevs.domain.Notification
import com.aradevs.storagemanager.datasources.DatabaseLocalDataSource

class DatabaseRepository(private val databaseLocalDataSource: DatabaseLocalDataSource) {
    suspend fun saveMedicine(medicine: Medicine) =
        databaseLocalDataSource.saveMedicine(medicine)

    suspend fun getMedicines() = databaseLocalDataSource.getMedicines()

    suspend fun updateMedicine(medicine: Medicine) =
        databaseLocalDataSource.updateMedicine(medicine)

    suspend fun deleteMedicine(medicineId: Int) = databaseLocalDataSource.deleteMedicine(medicineId)

    suspend fun saveNotification(notification: Notification) =
        databaseLocalDataSource.saveNotification(notification)

    suspend fun getNotifications() = databaseLocalDataSource.getNotifications()

    suspend fun deactivatePreviousNotifications(medicineId: Int) =
        databaseLocalDataSource.deactivatePreviousNotifications(medicineId)

    suspend fun deleteCurrentNotifications(medicineId: Int) =
        databaseLocalDataSource.deleteCurrentNotifications(medicineId)
}