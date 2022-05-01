package com.aradevs.storagemanager.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.aradevs.storagemanager.MedicineEntity
import com.aradevs.storagemanager.NotificationEntity

@Dao
interface DatabaseDao {
    @Insert
    suspend fun saveMedicine(medicineEntity: MedicineEntity)

    @Query("select * from medicine_info where status = 1")
    suspend fun getMedicines(): List<MedicineEntity>

    @Query("select * from medicine_info")
    suspend fun getAllMedicines(): List<MedicineEntity>

    @Query("update medicine_info set status = 0 where id = :medicineId")
    suspend fun deactivateMedicine(medicineId: Int)

    @Query("delete from medicine_info where id = :medicineId")
    suspend fun deleteMedicine(medicineId: Int)

    @Insert
    suspend fun saveNotification(notificationEntity: NotificationEntity)

    @Query("select * from notifications")
    suspend fun getAllNotifications(): List<NotificationEntity>

    @Query("delete from notifications where id=:notificationId")
    suspend fun deleteNotification(notificationId: Int)

    @Query("delete from notifications")
    suspend fun deleteNotifications()

}