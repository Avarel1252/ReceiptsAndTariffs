package com.receipts.models

import android.content.Context
import com.receipts.models.dbs.DbsRepository
import com.receipts.models.receipts.ReceiptsRepository

object Repositories {
    private lateinit var applicationContext: Context
    val dbsRepository by lazy { DbsRepository(applicationContext) }
    fun newReceiptsRepository() =
        ReceiptsRepository(
            dbsRepository.database.getDao()
        )


    fun init(context: Context) {
        applicationContext = context
    }

}