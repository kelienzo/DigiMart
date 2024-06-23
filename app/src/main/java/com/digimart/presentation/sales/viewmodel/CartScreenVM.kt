package com.digimart.presentation.sales.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.digimart.data.local.entities.CartItem
import com.digimart.data.local.entities.Sales
import com.digimart.data.local.entities.toSaleItems
import com.digimart.presentation.sales.repository.CartItemRepository
import com.digimart.presentation.sales.repository.SalesRepository
import com.digimart.presentation.sales.ui.CartEvent
import com.digimart.utils.SaleStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartScreenVM @Inject constructor(
    private val cartItemRepository: CartItemRepository,
    private val salesRepository: SalesRepository
) : ViewModel() {

    private val _allCartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val allCartItems = _allCartItems.asStateFlow()

    private val _singleCartItem = MutableStateFlow<CartItem?>(null)
    val singleCartItem = _singleCartItem.asStateFlow()

    private val _updateCartUiState = Channel<Long?>()
    val updateCartUiState = _updateCartUiState.receiveAsFlow()

    private val _createSaleUiState = Channel<Long?>()
    val createSaleUiState = _createSaleUiState.receiveAsFlow()

    fun onEvent(event: CartEvent) {
        when (event) {
            is CartEvent.OnGetSingleCartItem -> getSingleCartItem(event.productId)
            is CartEvent.OnUpdateCart -> updateCart(event.cartItem)
            CartEvent.OnGetAllCartItems -> getAllCartItem()
            is CartEvent.OnDeleteFromCart -> deleteFromCart(event.cartItem)
            is CartEvent.OnCreateSale -> {
                val newSale = Sales(
                    saleDate = System.currentTimeMillis(),
                    status = SaleStatus.PENDING.name,
                    salesItems = event.cartItems.map { it.toSaleItems() }
                )
                createSale(newSale)
            }

            else -> Unit
        }
    }

    private fun updateCart(cartItem: CartItem) {
        viewModelScope.launch { _updateCartUiState.trySend(cartItemRepository.addToCart(cartItem)) }
    }

    private fun getAllCartItem() {
        cartItemRepository.getAllCartItems()
            .onEach {
                _allCartItems.value = it
            }.launchIn(viewModelScope)
    }

    private fun getSingleCartItem(productId: Long) {
        cartItemRepository.getSingleCartItem(productId)
            .onEach {
                _singleCartItem.value = it
            }.launchIn(viewModelScope)
    }

    private fun deleteFromCart(cartItem: CartItem) {
        viewModelScope.launch { cartItemRepository.deleteFromCart(cartItem) }
    }

    private fun createSale(sales: Sales) {
        viewModelScope.launch { _createSaleUiState.trySend(salesRepository.insertSale(sales)) }
    }

    private fun clearCart() {
        viewModelScope.launch { cartItemRepository.clearCartItems() }
    }
}