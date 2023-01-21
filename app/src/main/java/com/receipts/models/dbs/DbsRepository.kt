package com.receipts.models.dbs

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.receipts.models.receipts.MainDb
import com.receipts.utils.Constants
import com.receipts.utils.RepositoryChangeListener

class DbsRepository(private val context: Context) : IDatabasesRepository {

    private lateinit var repositoryChangeListener: RepositoryChangeListener

    fun setRepositoryChangeListener(listener: RepositoryChangeListener) {
        repositoryChangeListener = listener
    }

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
        if (!databasesList.contains(name)) {
            databasesList.add(name)
        }
        context.getSharedPreferences(Constants.SHARED_NAME, MODE_PRIVATE).edit()
            .putString(Constants.LAST_DATABASE_KEY, name).apply()
        database = MainDb.getDb(context.applicationContext, name).apply {
            openHelper.readableDatabase.apply {
                beginTransaction()
                endTransaction()
            }
        }
        repositoryChangeListener.onChanged(databasesList)
    }

    override fun delete(name: String) {
        databasesList.remove(name)
        context.deleteDatabase(name)
        repositoryChangeListener.onChanged(databasesList)
        if (databasesList.isNotEmpty()) {
            selectOrAdd(databasesList[0])
        }
    }


    override fun deleteAll() {
        databasesList.forEach { el ->
            context.deleteDatabase(el)
        }
        databasesList.removeAll(databasesList)
        context.getSharedPreferences(Constants.SHARED_NAME, MODE_PRIVATE).edit().clear().apply()
        repositoryChangeListener.onChanged(databasesList)
    }
}