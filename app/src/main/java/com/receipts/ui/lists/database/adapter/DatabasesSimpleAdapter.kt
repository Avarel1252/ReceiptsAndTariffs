package com.receipts.ui.lists.database.adapter

import com.elveum.elementadapter.getString
import com.elveum.elementadapter.simpleAdapter
import com.receipts.R
import com.receipts.databinding.ItemDbBinding

fun databasesAdapter(listener: DatabasesAdapterListener) =
    simpleAdapter<String, ItemDbBinding> {
        areItemsSame = { old, new -> old == new }

        bind { name ->
            tvName.text = getString(R.string.name_holder, name)
        }

        listeners {
            btnTrashCan.onClick { listener.delete(it) }
            root.onClick { listener.click(it) }
        }
    }