package com.receipts.utils.multichoice

import com.receipts.utils.entities.Receipt
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


class SimpleReceiptsMultiChoiceHandler : MultiChoiceHandler<Receipt>, MultiChoiceState<Receipt> {

    private val checkedIds = HashSet<Long>()
    private var items: List<Receipt> = emptyList()
    private val stateFlow = MutableStateFlow(OnChanged())

    override fun setItemsFlow(coroutineScope: CoroutineScope, itemsFlow: Flow<List<Receipt>>) {
        coroutineScope.launch {
            itemsFlow.collectLatest { list ->
                items = list
                removeDeletedCats(list)
                notifyUpdates()
            }
        }
    }

    override fun listen(): Flow<MultiChoiceState<Receipt>> {
        return stateFlow.map { this }
    }

    override fun isChecked(item: Receipt): Boolean {
        return checkedIds.contains(item.id)
    }

    override fun toggle(item: Receipt) {
        if (isChecked(item)) {
            clear(item)
        } else {
            check(item)
        }
    }

    override fun check(item: Receipt) {
        if (!exists(item)) return

        checkedIds.add(item.id)

        notifyUpdates()
    }

    override fun clear(item: Receipt) {
        if (!exists(item)) return

        checkedIds.remove(item.id)

        notifyUpdates()
    }

    override fun selectAll() {
        checkedIds.addAll(items.map { it.id })
        notifyUpdates()
    }

    override fun clearAll() {
        checkedIds.clear()
        notifyUpdates()
    }

    override val totalCheckedCount: Int
        get() = checkedIds.size

    private fun exists(item: Receipt): Boolean {
        return items.any { it.id == item.id }
    }

    private fun removeDeletedCats(cats: List<Receipt>) {
        val existingIds = HashSet(cats.map { it.id })
        val iterator = checkedIds.iterator()
        while (iterator.hasNext()) {
            val id = iterator.next()
            if (!existingIds.contains(id)) {
                iterator.remove()
            }
        }
    }

    private fun notifyUpdates() {
        stateFlow.value = OnChanged()
    }

    private class OnChanged
}