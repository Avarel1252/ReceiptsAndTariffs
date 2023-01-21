package com.receipts.utils

interface RepositoryChangeListener {
    fun onChanged(databasesList : List<String>)
}