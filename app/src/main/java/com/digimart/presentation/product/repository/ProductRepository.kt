package com.digimart.presentation.product.repository

import com.digimart.data.local.DigiMartDatabase
import com.digimart.data.local.entities.Product
import com.digimart.utils.Category
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductRepository @Inject constructor(private val db: DigiMartDatabase) {

    fun getAllProducts(category: String?): Flow<List<Product>> {
        return category?.let {
            db.getProductDao().getProductsByCategory(it)
        } ?: db.getProductDao().getAllProducts()
    }

    fun getSingleProduct(productId: Long): Flow<Product?> {
        return db.getProductDao().getSingleProduct(productId)
    }

    suspend fun createProduct(product: Product): Long {
        return db.getProductDao().insertProduct(product)
    }

    suspend fun deleteProduct(product: Product): Int {
        return db.getProductDao().deleteProduct(product)
    }

    suspend fun updateProduct(productId: Long, quantityBought: Int, orderCount: Int) {
        db.getProductDao().updateQuantityAndOrderCount(productId, quantityBought, orderCount)
    }
}