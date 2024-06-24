package com.digimart.sales.repository

import com.digimart.data.local.DigiMartDatabase
import com.digimart.data.local.entities.CartItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CartItemRepositoryImpl @Inject constructor(private val db: DigiMartDatabase) :
    CartItemRepository {

    override fun getAllCartItems(): Flow<List<CartItem>> {
        return db.getCartItemDao().getAllCartItem()
    }

    override fun getSingleCartItem(productId: Long): Flow<CartItem?> {
        return db.getCartItemDao().getSingleCartItem(productId)
    }

    override suspend fun addToCart(cartItem: CartItem): Long {
        return db.getCartItemDao().addToCart(cartItem)
    }

    override suspend fun deleteFromCart(cartItem: CartItem): Int {
        return db.getCartItemDao().deleteFromCart(cartItem)
    }

    override suspend fun clearCartItems() {
        db.getCartItemDao().clearCart()
    }
}