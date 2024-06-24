package com.digimart.di

import com.digimart.product.repository.ProductRepository
import com.digimart.product.repository.ProductRepositoryImpl
import com.digimart.sales.repository.CartItemRepository
import com.digimart.sales.repository.CartItemRepositoryImpl
import com.digimart.sales.repository.SalesRepository
import com.digimart.sales.repository.SalesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BindingModule {

    @Singleton
    @Binds
    abstract fun bindProductRepository(productRepositoryImpl: ProductRepositoryImpl): ProductRepository

    @Singleton
    @Binds
    abstract fun bindCartItemRepository(cartItemRepositoryImpl: CartItemRepositoryImpl): CartItemRepository

    @Singleton
    @Binds
    abstract fun bindSalesRepository(salesRepositoryImpl: SalesRepositoryImpl): SalesRepository
}