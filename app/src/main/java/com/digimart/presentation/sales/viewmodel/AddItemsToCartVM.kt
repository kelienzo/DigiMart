package com.digimart.presentation.sales.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.digimart.data.local.entities.CartItem
import com.digimart.presentation.product.repository.ProductRepository
import com.digimart.presentation.sales.repository.CartItemRepository
import com.digimart.presentation.sales.ui.AddItemsToCartEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class AddItemsToCartVM @Inject constructor(
    private val productRepository: ProductRepository,
    private val cartItemRepository: CartItemRepository
) : ViewModel() {

    private val _category = MutableStateFlow<String?>(null)
    val allProducts = _category.flatMapLatest {
        getProducts(it)
    }

    private val _allCartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val allCartItems = _allCartItems.asStateFlow()

    private val _singleCartItem = MutableStateFlow<CartItem?>(null)
    val singleCartItem = _singleCartItem.asStateFlow()

    private val _onAddUpdateCartUiState = Channel<Long?>()
    val onAddUpdateCartUiState = _onAddUpdateCartUiState.receiveAsFlow()

    fun onEvent(event: AddItemsToCartEvent) {
        when (event) {
            AddItemsToCartEvent.GetAllCartItem -> getAllCartItem()
            is AddItemsToCartEvent.GetAllProducts -> _category.update { event.category }
            is AddItemsToCartEvent.OnAddItemToCart -> onAddUpdateCart(event.cartItem)
            is AddItemsToCartEvent.OnGetSingleCartItem -> getSingleCartItem(event.productId)
            else -> Unit
        }
    }

    private fun getProducts(category: String?) = productRepository.getAllProducts(category)

    private fun getAllCartItem() {
        cartItemRepository.getAllCartItems()
            .onEach {
                _allCartItems.value = it
            }.launchIn(viewModelScope)
    }

    private fun onAddUpdateCart(cartItem: CartItem) {
        viewModelScope.launch {
            _onAddUpdateCartUiState.trySend(cartItemRepository.addToCart(cartItem))
        }
    }

    private fun getSingleCartItem(productId: Long) {
        cartItemRepository.getSingleCartItem(productId)
            .onEach {
                _singleCartItem.value = it
            }.launchIn(viewModelScope)
    }
}