package com.digimart.product.usecase

import com.digimart.data.local.entities.Product
import com.digimart.product.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {

    fun getAllProducts(category: String?): Flow<List<Product>> =
        productRepository.getAllProducts(category)

    fun getSingleProduct(productId: Long): Flow<Product?> =
        productRepository.getSingleProduct(productId)

    suspend fun createProduct(product: Product): Long = productRepository.createProduct(product)

    suspend fun deleteProduct(product: Product): Int = productRepository.deleteProduct(product)

    suspend fun updateProduct(productId: Long, quantityBought: Int, orderCount: Int) =
        productRepository.updateProduct(productId, quantityBought, orderCount)
}