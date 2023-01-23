package com.receipts.ui.lists.receipts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.receipts.R
import com.receipts.di.ReceiptMultiChoice
import com.receipts.di.ReceiptsRepositoryM
import com.receipts.models.receipts.ReceiptsRepository
import com.receipts.utils.entities.Receipt
import com.receipts.utils.multichoice.MultiChoiceHandler
import com.receipts.utils.multichoice.MultiChoiceState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReceiptsViewModel @Inject constructor(
    @ReceiptsRepositoryM private var receiptsRepository: ReceiptsRepository,
    @ReceiptMultiChoice private val multiChoiceHandler: MultiChoiceHandler<Receipt>
) : ViewModel() {
    private val _stateLiveData = MutableLiveData<State>()
    val stateLiveData: LiveData<State> = _stateLiveData

    init {
        viewModelScope.launch {
            multiChoiceHandler.setItemsFlow(viewModelScope, receiptsRepository.getReceipts())
            val combinedFlow = combine(
                receiptsRepository.getReceipts(),
                multiChoiceHandler.listen(),
                ::merge
            )
            combinedFlow.collectLatest {
                _stateLiveData.value = it
            }
        }
    }

    fun addReceipt(receipt: Receipt) {
        receiptsRepository.add(receipt)
    }

    fun deleteReceipt(receipt: ReceiptListItem) {
        receiptsRepository.delete(receipt.originReceipt)
    }

    fun toggleSelection(receipt: ReceiptListItem) {
        multiChoiceHandler.toggle(receipt.originReceipt)
    }

    fun selectOrClearAll() {
        stateLiveData.value?.selectAllOperation?.operation?.invoke()
    }

    fun deleteSelectedReceipts() {
        viewModelScope.launch {
            val currentMultiChoiceState = multiChoiceHandler.listen().first()
            receiptsRepository.deleteSelectedReceipts(currentMultiChoiceState)
        }
    }

    fun updateDateSelectedReceipts(date: String) {
        viewModelScope.launch {
            val currentMultiChoiceState = multiChoiceHandler.listen().first()
            receiptsRepository.updateDateSelectedReceipts(date, currentMultiChoiceState)
        }
    }

    private fun merge(receipts: List<Receipt>, multiChoiceState: MultiChoiceState<Receipt>): State {
        return State(
            receipts = receipts.map { receipt ->
                ReceiptListItem(receipt, multiChoiceState.isChecked(receipt))
            },
            totalCount = receipts.size,
            totalCheckedCount = multiChoiceState.totalCheckedCount,
            selectAllOperation = if (multiChoiceState.totalCheckedCount < receipts.size) {
                SelectAllOperation(R.string.select_all, multiChoiceHandler::selectAll)
            } else {
                SelectAllOperation(R.string.clear_all, multiChoiceHandler::clearAll)
            }
        )
    }

    fun update(receipt: Receipt) {
        Thread {
            receiptsRepository.update(receipt)
        }.start()
    }

    class SelectAllOperation(
        val titleRes: Int,
        val operation: () -> Unit
    )

    class State(
        val totalCount: Int,
        val totalCheckedCount: Int,
        val receipts: List<ReceiptListItem>,
        val selectAllOperation: SelectAllOperation
    )
}