package com.receipts.models

import com.github.javafaker.Faker
import com.receipts.utils.entities.Receipt
import com.receipts.utils.entities.Tariff
import com.receipts.utils.multichoice.MultiChoiceState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReceiptsRepository @Inject constructor() {

    private val random = Random(42)
    private val faker = Faker.instance(random)

    private val receiptsFlow = MutableStateFlow(
        List(100) { index -> randomReceipt(id = index + 1L) }
    )

    fun getReceipts(): Flow<List<Receipt>> {
        return receiptsFlow
    }

    fun getReceiptById(receiptId: Long): Flow<Receipt?> {
        return getReceipts().map { receipt -> receipt.firstOrNull { it.id == receiptId } }
    }

    fun delete(receiptId: Long) {
        receiptsFlow.update { oldList ->
            oldList.filter { it.id != receiptId }
        }
    }
    fun add(receipt:Receipt){

    }

    fun deleteSelectedReceipts(multiChoiceState: MultiChoiceState<Receipt>) {
        receiptsFlow.update { oldList ->
            oldList.filter { receipt -> !multiChoiceState.isChecked(receipt) }
        }
    }

    private fun randomReceipt(id: Long): Receipt = Receipt(
        id = id,
        name = faker.cat().name(),
        date = "00.00.0000",
        tariff = Tariff()
    )

    fun update(receipt: Receipt) {

    }

}