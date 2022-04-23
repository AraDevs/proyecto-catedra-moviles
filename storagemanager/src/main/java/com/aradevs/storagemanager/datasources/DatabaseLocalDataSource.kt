package com.aradevs.storagemanager.datasources

import com.aradevs.domain.Medicine
import com.aradevs.domain.Notification
import com.aradevs.domain.coroutines.Status

interface DatabaseLocalDataSource {
    suspend fun saveMedicine(medicine: Medicine): Status<Unit>
    suspend fun getMedicines(): Status<List<Medicine>>
    suspend fun updateMedicine(medicine: Medicine): Status<Unit>
    suspend fun deleteMedicine(medicineId: Int): Status<Unit>
    suspend fun saveNotification(notification: Notification): Status<Unit>
    suspend fun getNotifications(): Status<List<Notification>>
    suspend fun deactivatePreviousNotifications(medicineId: Int): Status<Unit>
    suspend fun deleteCurrentNotifications(medicineId: Int): Status<Unit>
}