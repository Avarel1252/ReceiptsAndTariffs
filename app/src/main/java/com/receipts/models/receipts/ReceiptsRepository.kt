package com.receipts.models.receipts

import com.receipts.utils.entities.Receipt
import com.receipts.utils.multichoice.MultiChoiceState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class ReceiptsRepository(private val dbDao: Dao) : IReceiptsRepository {
    override fun getReceipts(): Flow<List<Receipt>> {
        return dbDao.getAllReceipts()
    }

    override fun delete(receipt: Receipt) {
        dbDao.delete(receipt)
    }

    override fun add(receipt: Receipt) {
        dbDao.insert(receipt)
    }

    override fun deleteSelectedReceipts(multiChoiceState: MultiChoiceState<Receipt>) {
        dbDao.getAllReceipts().map { list ->
            list.forEach { receipt ->
                if (multiChoiceState.isChecked(receipt)) {
                    dbDao.delete(receipt)
                }
            }
        }
    }

    override fun update(receipt: Receipt) {
        dbDao.update(receipt)
    }
}