package com.aradevs.storagemanager.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.aradevs.storagemanager.MedicineEntity
import com.aradevs.storagemanager.NotificationEntity

@Dao
interface DatabaseDao {
    @Insert
    suspend fun saveMedicine(medicineEntity: MedicineEntity)

    @Query("select * from medicine_info")
    suspend fun getMedicines(): List<MedicineEntity>

    @Update
    suspend fun updateMedicine(medicineEntity: MedicineEntity)

    @Query("delete from medicine_info where id = :medicineId")
    suspend fun deleteMedicine(medicineId: Int)

    @Insert
    suspend fun saveNotification(notificationEntity: NotificationEntity)

    @Query("select * from notifications")
    suspend fun getNotifications(): List<NotificationEntity>

    @Query("update notifications set current = 0 where medicine_id = :medicineId")
    suspend fun deactivatePreviousNotifications(medicineId: Int)

    @Query("delete from notifications where medicine_id = :medicineId and current = 1")
    suspend fun deleteCurrentNotifications(medicineId: Int)
}