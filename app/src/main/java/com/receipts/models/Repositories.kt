package com.receipts.models

import android.content.Context
import com.receipts.utils.Constants

object Repositories {
    private lateinit var applicationContext: Context
    val receiptsRepository by lazy { ReceiptsRepository(db.getDao()) }
    private val db by lazy {
        MainDb.getDb(
            applicationContext,
            applicationContext.getSharedPreferences("LastPref", Context.MODE_PRIVATE)
                .getString(Constants.LAST_DATABASE_KEY, "firstDatabase")!!
        )
    }

    fun init(context: Context) {
        applicationContext = context
    }

}