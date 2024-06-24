package com.digimart.sales.usecase

import com.digimart.data.local.entities.CartItem
import com.digimart.sales.repository.CartItemTestRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class CartItemUseCaseTest {

    private lateinit var cartItemUseCase: CartItemUseCase
    private lateinit var cartItemTestRepository: CartItemTestRepository

    @Before
    fun setUp() {
        cartItemTestRepository = CartItemTestRepository()
        cartItemUseCase = CartItemUseCase(cartItemTestRepository)

        val cartItemToInsert = mutableListOf<CartItem>()
        ('a'..'z').forEachIndexed { index, item ->
            cartItemToInsert.add(
                CartItem(
                    productId = index.toLong(),
                    productName = item.toString(),
                    productPrice = 213.4,
                    totalAmount = 2312.4,
                    totalQuantity = 56,
                    timeStamp = (0..9999).random().toLong()
                )
            )
        }

        runBlocking {
            cartItemToInsert.forEach { cartItemTestRepository.addToCart(it) }
        }
    }

    @Test
    fun `Get all items in the cart and it should not be empty`() = runBlocking {
        val cartItems = cartItemUseCase.getAllCartItems().first()
        assertThat(cartItems).isNotEmpty()
    }

    @Test
    fun `Get a single cart item and it should not be null`() = runBlocking {
        val singleCartItem = cartItemUseCase.getSingleCartItem(productId = 5).first()
        assertThat(singleCartItem).isNotNull()
    }

    @Test
    fun `Delete a cart item and getting it should be null`() = runBlocking {
        val cartItemToDelete = cartItemUseCase.getSingleCartItem(productId = 4).first()
        cartItemUseCase.deleteFromCart(cartItemToDelete!!)
        val recentlyDeletedCartItem = cartItemUseCase.getSingleCartItem(productId = 4).first()
        assertThat(recentlyDeletedCartItem).isNull()
    }

    @Test
    fun `Cart items are all cleared from the list`() = runBlocking {
        cartItemUseCase.clearCartItems()
        val cartItems = cartItemUseCase.getAllCartItems().first()
        assertThat(cartItems).isEmpty()
    }
}