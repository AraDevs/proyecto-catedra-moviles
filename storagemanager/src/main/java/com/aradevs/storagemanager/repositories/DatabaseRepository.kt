package com.aradevs.storagemanager.repositories

import com.aradevs.domain.Medicine
import com.aradevs.storagemanager.datasources.DatabaseLocalDataSource

class DatabaseRepository(private val databaseLocalDataSource: DatabaseLocalDataSource) {
    suspend fun saveMedicine(medicine: Medicine) =
        databaseLocalDataSource.saveMedicine(medicine)

    suspend fun getMedicines() = databaseLocalDataSource.getMedicines()

    suspend fun getAllMedicines() = databaseLocalDataSource.getAllMedicine()

    suspend fun deactivateMedicine(medicine: Medicine) =
        databaseLocalDataSource.deactivateMedicine(medicine)

    suspend fun deleteMedicine(medicineId: Int) = databaseLocalDataSource.deleteMedicine(medicineId)
}