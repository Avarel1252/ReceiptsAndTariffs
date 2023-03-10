package com.receipts.di

import com.receipts.models.receipts.ReceiptsRepository
import com.receipts.models.Repositories
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ReceiptsRepositoryM

@Module
@InstallIn(ViewModelComponent::class)
class ReceiptsRepositoryModule {

    @Provides
    @ReceiptsRepositoryM
    fun provideRepository(): ReceiptsRepository {
        return Repositories.newReceiptsRepository()
    }
}