package com.ejabi.data.di

import com.ejabi.domain.repository.ProductRepository
import com.ejabi.domain.usecase.Products.GetProductsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @Provides
    fun provideGetProductsUseCase(
        repository: ProductRepository
    ): GetProductsUseCase {
        return GetProductsUseCase(repository)
    }
}