package com.receipts.models.receipts

import com.receipts.utils.entities.Receipt
import com.receipts.utils.multichoice.MultiChoiceState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlin.coroutines.coroutineContext


class ReceiptsRepository(private val dbDao: Dao) : IReceiptsRepository {
    override fun getReceipts(): Flow<List<Receipt>> {
        return dbDao.getAllReceipts()
    }

    override fun delete(receipt: Receipt) {
        Thread { dbDao.delete(receipt) }.start()
    }

    override fun add(receipt: Receipt) {
        Thread { dbDao.insert(receipt) }.start()
    }

    override suspend fun deleteSelectedReceipts(multiChoiceState: MultiChoiceState<Receipt>) {
        dbDao.getAllReceipts().first().map { receipt ->
            if (multiChoiceState.isChecked(receipt)) {
                Thread { dbDao.delete(receipt) }.start()
            }
        }
    }

    override suspend fun updateDateSelectedReceipts(
        date: String,
        multiChoiceState: MultiChoiceState<Receipt>
    ) {
        dbDao.getAllReceipts().first().map { receipt ->
            if (multiChoiceState.isChecked(receipt)) {
                receipt.date = date
                Thread { dbDao.update(receipt) }.start()
            }
        }
    }

    override fun update(receipt: Receipt) {
        Thread { dbDao.update(receipt) }.start()
    }
}