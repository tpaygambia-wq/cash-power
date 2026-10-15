package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TokenReceiptDao {
    @Query("SELECT * FROM token_receipts ORDER BY timestamp DESC")
    fun getAllReceipts(): Flow<List<TokenReceiptEntity>>

    @Query("SELECT * FROM token_receipts WHERE meterNumber = :meterNumber ORDER BY timestamp DESC")
    fun getReceiptsForMeter(meterNumber: String): Flow<List<TokenReceiptEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReceipt(receipt: TokenReceiptEntity)

    @Query("UPDATE token_receipts SET isLoaded = :isLoaded WHERE id = :id")
    suspend fun updateLoadedStatus(id: String, isLoaded: Boolean)

    @Query("DELETE FROM token_receipts WHERE id = :id")
    suspend fun deleteReceipt(id: String)
}
