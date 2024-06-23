package com.digimart.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.digimart.data.local.entities.CartItem
import kotlinx.coroutines.flow.Flow

@Dao
interface CartItemDao {
    @Query("SELECT * FROM CartItem ORDER BY timeStamp DESC ")
    fun getAllCartItem(): Flow<List<CartItem>>

    @Query("SELECT * FROM CartItem WHERE productId = :productId LIMIT 1")
    fun getSingleCartItem(productId: Long): Flow<CartItem?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllToCart(cartItem: List<CartItem>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToCart(cartItem: CartItem): Long

    @Delete
    suspend fun deleteFromCart(cartItem: CartItem): Int

    @Query("DELETE FROM CartItem")
    suspend fun clearCart()
}