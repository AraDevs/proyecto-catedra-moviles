package com.aradevs.storagemanager.datasources.implementations

import com.aradevs.domain.Medicine
import com.aradevs.domain.Notification
import com.aradevs.domain.coroutines.Status
import com.aradevs.storagemanager.AppDatabase
import com.aradevs.storagemanager.data_handling.toDomain
import com.aradevs.storagemanager.data_handling.toEntity
import com.aradevs.storagemanager.datasources.DatabaseLocalDataSource
import timber.log.Timber

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

    override suspend fun deactivateMedicine(medicine: Medicine): Status<Unit> {
        return try {
            db.getDatabaseDao().deactivateMedicine(medicine.id)
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

    override suspend fun saveNotification(notification: Notification): Status<Unit> {
        return try {
            db.getDatabaseDao().saveNotification(notification.toEntity())
            Status.Success(Unit)
        } catch (e: Exception) {
            Timber.d(e)
            Status.Error(e)
        }
    }

    override suspend fun getNotifications(): Status<List<Notification>> {
        return try {
            val results = db.getDatabaseDao().getAllNotifications()
            Status.Success(results.map { it.toDomain() })
        } catch (e: Exception) {
            Status.Error(e)
        }
    }

    override suspend fun deleteNotification(notificationId: Int): Status<Unit> {
        return try {
            db.getDatabaseDao().deleteNotification(notificationId)
            Status.Success(Unit)
        } catch (e: Exception) {
            Status.Error(e)
        }
    }

    override suspend fun deleteNotifications(): Status<Unit> {
        return try {
            db.getDatabaseDao().deleteNotifications()
            Status.Success(Unit)
        } catch (e: Exception) {
            Status.Error(e)
        }
    }
}