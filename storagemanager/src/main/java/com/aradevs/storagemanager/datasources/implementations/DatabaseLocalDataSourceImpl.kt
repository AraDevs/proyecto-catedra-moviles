package com.aradevs.storagemanager.datasources.implementations

import com.aradevs.domain.Medicine
import com.aradevs.domain.coroutines.Status
import com.aradevs.storagemanager.AppDatabase
import com.aradevs.storagemanager.data_handling.toDomain
import com.aradevs.storagemanager.data_handling.toEntity
import com.aradevs.storagemanager.datasources.DatabaseLocalDataSource

class DatabaseLocalDataSourceImpl(private val db: AppDatabase) : DatabaseLocalDataSource {

    override suspend fun saveMedicine(medicine: Medicine): Status<Unit> {
        return try {
            db.getDatabaseDao().saveMedicine(medicine.toEntity())
            Status.Success(Unit)
        } catch (e: Exception) {
            Status.Error(e)
        }
    }

    override suspend fun getMedicines(): Status<List<Medicine>> {
        return try {
            val results = db.getDatabaseDao().getMedicines()
            Status.Success(results.map { it.toDomain() })
        } catch (e: Exception) {
            Status.Error(e)
        }
    }

    override suspend fun getAllMedicine(): Status<List<Medicine>> {
        return try {
            val results = db.getDatabaseDao().getAllMedicines()
            Status.Success(results.map { it.toDomain() })
        } catch (e: Exception) {
            Status.Error(e)
        }
    }

    override suspend fun updateMedicine(medicine: Medicine): Status<Unit> {
        return try {
            db.getDatabaseDao().updateMedicine(medicine.toEntity())
            Status.Success(Unit)
        } catch (e: Exception) {
            Status.Error(e)
        }
    }

    override suspend fun deleteMedicine(medicineId: Int): Status<Unit> {
        return try {
            db.getDatabaseDao().deleteMedicine(medicineId)
            Status.Success(Unit)
        } catch (e: Exception) {
            Status.Error(e)
        }
    }
}