package com.receipts.models

import androidx.room.Insert
import androidx.room.Query
import com.receipts.utils.entities.Receipt
import kotlinx.coroutines.flow.Flow

@androidx.room.Dao
interface Dao {
    @Insert
    fun insert(receipt: Receipt)

    @Query("SELECT * FROM firstTable")
    fun getAllReceipts(): Flow<List<Receipt>>


}