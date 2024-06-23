package com.digimart.presentation.sales.repository

import com.digimart.data.local.DigiMartDatabase
import com.digimart.data.local.entities.CartItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CartItemRepository @Inject constructor(private val db: DigiMartDatabase) {

    fun getAllCartItems(): Flow<List<CartItem>> {
        return db.getCartItemDao().getAllCartItem()
    }

    fun getSingleCartItem(productId: Long): Flow<CartItem?> {
        return db.getCartItemDao().getSingleCartItem(productId)
    }

    suspend fun addToCart(cartItem: CartItem): Long {
        return db.getCartItemDao().addToCart(cartItem)
    }

    suspend fun deleteFromCart(cartItem: CartItem): Int {
        return db.getCartItemDao().deleteFromCart(cartItem)
    }

    suspend fun clearCartItems() {
        db.getCartItemDao().clearCart()
    }
}