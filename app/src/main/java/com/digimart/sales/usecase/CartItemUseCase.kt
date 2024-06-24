package com.digimart.sales.usecase

import com.digimart.data.local.entities.CartItem
import com.digimart.sales.repository.CartItemRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CartItemUseCase @Inject constructor(
    private val cartItemRepository: CartItemRepository
) {

    fun getAllCartItems(): Flow<List<CartItem>> = cartItemRepository.getAllCartItems()

    fun getSingleCartItem(productId: Long): Flow<CartItem?> =
        cartItemRepository.getSingleCartItem(productId)

    suspend fun addToCart(cartItem: CartItem): Long = cartItemRepository.addToCart(cartItem)

    suspend fun deleteFromCart(cartItem: CartItem): Int =
        cartItemRepository.deleteFromCart(cartItem)

    suspend fun clearCartItems() = cartItemRepository.clearCartItems()
}