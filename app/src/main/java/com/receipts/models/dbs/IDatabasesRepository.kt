package com.receipts.models.dbs

interface IDatabasesRepository {
    fun selectOrAdd(name: String)
    fun delete(name: String)
    fun deleteAll()
}