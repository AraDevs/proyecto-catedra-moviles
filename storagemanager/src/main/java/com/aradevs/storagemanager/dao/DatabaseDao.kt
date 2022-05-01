package com.aradevs.storagemanager.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.aradevs.storagemanager.MedicineEntity

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

}