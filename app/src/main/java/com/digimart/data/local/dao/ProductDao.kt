package com.digimart.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.digimart.data.local.entities.Product
import com.digimart.utils.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Query("SELECT * FROM Product ORDER BY date DESC")
    fun getAllProducts(): Flow<List<Product>>

    @Query("SELECT * FROM Product WHERE category = :category ORDER BY date DESC")
    fun getProductsByCategory(category: String): Flow<List<Product>>

    @Query("SELECT * FROM Product WHERE productId = :productId LIMIT 1")
    fun getSingleProduct(productId: Long): Flow<Product?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Product): Long

    @Delete
    suspend fun deleteProduct(product: Product): Int

    @Query("UPDATE Product SET quantity = quantity - :quantityBought, orderCount = orderCount + :orderCount, quantityOrdered = quantityOrdered +:quantityBought WHERE productId = :productId")
    suspend fun updateQuantityAndOrderCount(productId: Long, quantityBought: Int, orderCount: Int)
}