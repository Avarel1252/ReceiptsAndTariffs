package com.receipts.ui.lists.database

import android.content.Context
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.receipts.di.DatabasesRepositoryM
import com.receipts.models.dbs.DbsRepository
import com.receipts.ui.lists.database.adapter.databasesAdapter
import com.receipts.utils.RepositoryChangeListener
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
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