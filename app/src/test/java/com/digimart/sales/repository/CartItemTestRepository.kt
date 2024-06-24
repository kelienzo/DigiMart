package com.digimart.sales.repository

import com.digimart.data.local.entities.CartItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CartItemTestRepository : CartItemRepository {

    private val cartItems = mutableListOf<CartItem>()
    override fun getAllCartItems(): Flow<List<CartItem>> {
        return flow { emit(cartItems) }
    }

    override fun getSingleCartItem(productId: Long): Flow<CartItem?> {
        return flow { emit(cartItems.find { it.productId == productId }) }
    }

    override suspend fun addToCart(cartItem: CartItem): Long {
        cartItems.add(cartItem)
        return 1
    }

    override suspend fun deleteFromCart(cartItem: CartItem): Int {
        cartItems.remove(cartItem)
        return 1
    }

    override suspend fun clearCartItems() {
        cartItems.clear()
    }
}