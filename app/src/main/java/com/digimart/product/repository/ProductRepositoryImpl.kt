package com.digimart.product.repository

import com.digimart.data.local.DigiMartDatabase
import com.digimart.data.local.entities.Product
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(private val db: DigiMartDatabase): ProductRepository {

    override fun getAllProducts(category: String?): Flow<List<Product>> {
        return category?.let {
            db.getProductDao().getProductsByCategory(it)
        } ?: db.getProductDao().getAllProducts()
    }

    override fun getSingleProduct(productId: Long): Flow<Product?> {
        return db.getProductDao().getSingleProduct(productId)
    }

    override suspend fun createProduct(product: Product): Long {
        return db.getProductDao().insertProduct(product)
    }

    override suspend fun deleteProduct(product: Product): Int {
        return db.getProductDao().deleteProduct(product)
    }

    override suspend fun updateProduct(productId: Long, quantityBought: Int, orderCount: Int) {
        db.getProductDao().updateQuantityAndOrderCount(productId, quantityBought, orderCount)
    }
}