package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [SavedMeterEntity::class, TokenReceiptEntity::class],
    version = 1,
    exportSchema = false
)
abstract class CashPowerDatabase : RoomDatabase() {
    abstract fun savedMeterDao(): SavedMeterDao
    abstract fun tokenReceiptDao(): TokenReceiptDao

    companion object {
        @Volatile
        private var INSTANCE: CashPowerDatabase? = null

        fun getDatabase(context: Context): CashPowerDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CashPowerDatabase::class.java,
                    "gambia_cashpower.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
