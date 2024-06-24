package com.digimart.sales.repository

import com.digimart.data.local.entities.CartItem
import kotlinx.coroutines.flow.Flow

interface CartItemRepository {

    fun getAllCartItems(): Flow<List<CartItem>>

    fun getSingleCartItem(productId: Long): Flow<CartItem?>

    suspend fun addToCart(cartItem: CartItem): Long

    suspend fun deleteFromCart(cartItem: CartItem): Int

    suspend fun clearCartItems()
}