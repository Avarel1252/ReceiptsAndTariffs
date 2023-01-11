package com.receipts.di

import com.receipts.utils.entities.Receipt
import com.receipts.utils.multichoice.MultiChoiceHandler
import com.receipts.utils.multichoice.SimpleReceiptsMultiChoiceHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ReceiptMultiChoice

@Module
@InstallIn(ViewModelComponent::class)
class CatsMultiChoiceModule {

    @Provides
    @ReceiptMultiChoice
    fun provideMultiChoiceHandler(): MultiChoiceHandler<Receipt> {
        return SimpleReceiptsMultiChoiceHandler()
    }

}