package com.digimart.product.repository

import com.digimart.data.local.entities.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ProductTestRepository : ProductRepository {

    private val products = mutableListOf<Product>()

    override fun getAllProducts(category: String?): Flow<List<Product>> {
        return flow {
            emit(category?.let {
                products.filter {
                    it.category == category
                }
            } ?: products
            )
        }
    }

    override fun getSingleProduct(productId: Long): Flow<Product?> {
        return flow { emit(products.find { it.productId == productId }) }
    }

    override suspend fun createProduct(product: Product): Long {
        products.add(product)
        return 1
    }

    override suspend fun deleteProduct(product: Product): Int {
        products.remove(product)
        return 1
    }

    override suspend fun updateProduct(productId: Long, quantityBought: Int, orderCount: Int) {
        val prodToUpdateIndex = products.indexOfFirst { it.productId == productId }
        val productToUpdate = products.find { it.productId == productId }?.copy(
            orderCount = orderCount,
            quantityOrdered = quantityBought
        )
        products.add(prodToUpdateIndex, productToUpdate!!)
    }
}