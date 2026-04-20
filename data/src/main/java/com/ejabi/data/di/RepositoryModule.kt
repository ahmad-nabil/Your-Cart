package com.ejabi.data.di

import com.ejabi.data.Repository.ProductRepositoryImpl
import com.ejabi.domain.repository.ProductRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindProductRepository(
        impl: ProductRepositoryImpl
    ): ProductRepository
}