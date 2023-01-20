package com.receipts.models.dbs

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.RoomMasterTable
import androidx.room.RoomOpenHelper
import androidx.room.RoomSQLiteQuery
import com.mifmif.common.regex.Main
import com.receipts.App
import com.receipts.models.Repositories
import com.receipts.models.receipts.MainDb
import com.receipts.models.receipts.MainDb_Impl
import com.receipts.utils.Constants
import com.receipts.utils.entities.Receipt
import com.receipts.utils.entities.Tariff
import okhttp3.internal.closeQuietly
import okhttp3.internal.notifyAll
import okhttp3.internal.threadName
import java.io.File

class DbsRepository(private val context: Context) : IDatabasesRepository {


    val databasesList = startDatabasesList()

    private fun startDatabasesList() =
        filterNotDb(context.applicationContext.databaseList().toList())

    private fun filterNotDb(list: List<String>) =
        list.filter { it.matches("[^.]*.db".toRegex()) }.toMutableList()


    var database = MainDb.getDb(
        context.applicationContext,
        context.getSharedPreferences(Constants.SHARED_NAME, MODE_PRIVATE)
            .getString(Constants.LAST_DATABASE_KEY, Constants.DEFAULT_DATABASE)
            ?: Constants.DEFAULT_DATABASE
    )

    override fun selectOrAdd(name: String) {
        databasesList.add(name)
        context.getSharedPreferences(Constants.SHARED_NAME, MODE_PRIVATE).edit()
            .putString(Constants.LAST_DATABASE_KEY, name).apply()
        database = MainDb.getDb(context.applicationContext, name).apply {
            openHelper.readableDatabase.apply {
                beginTransaction()
                endTransaction()
            }
        }
    }

    override fun delete(name: String) {
        databasesList.remove(name)
        context.deleteDatabase(name)
    }


    override fun deleteAll() {
        databasesList.forEach { el ->
            context.deleteDatabase(el)
        }
        databasesList.removeAll(databasesList)
        context.getSharedPreferences(Constants.SHARED_NAME, MODE_PRIVATE).edit().clear().apply()
    }
}