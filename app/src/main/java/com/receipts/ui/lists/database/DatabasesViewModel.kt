package com.receipts.ui.lists.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.receipts.di.DatabasesRepositoryM
import com.receipts.models.dbs.DbsRepository
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
    }

    fun selectOrAdd(name: String) {
        with(_stateLiveData) {
            value?.let {
                if (!it.contains(name)) {
                    value = it.plus(name)
                }
            }
        }
        dbsRepository.selectOrAdd(name)
    }

    fun delete(name: String) {
        with(_stateLiveData) {
            value?.let {
                if (it.contains(name)) {
                    value = it.minus(name)
                }
            }
        }
        dbsRepository.delete(name)
    }

    fun deleteAll() {
        _stateLiveData.value = listOf()
        dbsRepository.deleteAll()
    }
}