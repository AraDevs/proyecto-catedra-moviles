package com.aradevs.storagemanager

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.aradevs.storagemanager.dao.DatabaseDao
import kotlinx.coroutines.DelicateCoroutinesApi

@Database(
    entities = [MedicineEntity::class, NotificationEntity::class],
    version = 1
)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getDatabaseDao(): DatabaseDao
    companion object {
        private const val DATABASE_NAME = "app_db"

        @Synchronized
        fun getDatabase(context: Context): AppDatabase = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }
}