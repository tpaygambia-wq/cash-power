package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedMeterDao {
    @Query("SELECT * FROM saved_meters ORDER BY lastUsedTimestamp DESC")
    fun getAllSavedMeters(): Flow<List<SavedMeterEntity>>

    @Query("SELECT * FROM saved_meters WHERE meterNumber = :meterNumber LIMIT 1")
    suspend fun getMeter(meterNumber: String): SavedMeterEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateMeter(meter: SavedMeterEntity)

    @Delete
    suspend fun deleteMeter(meter: SavedMeterEntity)

    @Query("DELETE FROM saved_meters WHERE meterNumber = :meterNumber")
    suspend fun deleteByMeterNumber(meterNumber: String)
}
