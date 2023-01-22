package com.receipts.models.receipts

import com.receipts.utils.entities.Receipt
import com.receipts.utils.multichoice.MultiChoiceState
import kotlinx.coroutines.flow.Flow

interface IReceiptsRepository {
    fun getReceipts(): Flow<List<Receipt>>
    fun delete(receipt: Receipt)
    fun add(receipt: Receipt)
    suspend fun deleteSelectedReceipts(multiChoiceState: MultiChoiceState<Receipt>)
    suspend fun updateDateSelectedReceipts(date: String, multiChoiceState: MultiChoiceState<Receipt>)
    fun update(receipt: Receipt)
}