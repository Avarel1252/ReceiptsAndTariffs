package com.receipts.ui.lists.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.receipts.di.DatabasesRepositoryM
import com.receipts.models.dbs.DbsRepository
import com.receipts.utils.RepositoryChangeListener
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DatabasesViewModel @Inject constructor(
    @DatabasesRepositoryM private var dbsRepository: DbsRepository,
) : ViewModel() {
    private val _stateLiveData = MutableLiveData<List<String>>()
    val stateLiveData: LiveData<List<String>> = _stateLiveData

    init {
        _stateLiveData.value = dbsRepository.databasesList
        dbsRepository.setRepositoryChangeListener(object : RepositoryChangeListener {
            override fun onChanged(databasesList: List<String>) {
                _stateLiveData.value = databasesList
            }
        })
    }

    fun selectOrAdd(name: String) {
        dbsRepository.selectOrAdd(name)
    }

    fun delete(name: String) {
        dbsRepository.delete(name)
    }

    fun deleteAll() {
        dbsRepository.deleteAll()
    }
}