package com.receipts.di

import com.receipts.models.Repositories
import com.receipts.models.dbs.DbsRepository
import com.receipts.models.receipts.ReceiptsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DatabasesRepositoryM

@Module
@InstallIn(ViewModelComponent::class)
class DatabasesRepositoryModule {
    @Provides
    @DatabasesRepositoryM
    fun provideRepository(): DbsRepository {
        return Repositories.dbsRepository
    }
}


