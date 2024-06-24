package com.digimart.product.repository

import com.digimart.data.local.entities.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {

    fun getAllProducts(category: String?): Flow<List<Product>>

    fun getSingleProduct(productId: Long): Flow<Product?>

    suspend fun createProduct(product: Product): Long

    suspend fun deleteProduct(product: Product): Int

    suspend fun updateProduct(productId: Long, quantityBought: Int, orderCount: Int)
}