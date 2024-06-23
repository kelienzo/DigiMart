package com.digimart.presentation.product.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.digimart.data.local.entities.Product
import com.digimart.presentation.product.repository.ProductRepository
import com.digimart.presentation.product.ui.AddEditProductEvent
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
class AddProductVM @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _addProductUiState = Channel<Long>()
    val addProductUiState = _addProductUiState.receiveAsFlow()

    private val _singleProductUiState = MutableStateFlow<Product?>(null)
    val singleProductUiState = _singleProductUiState.asStateFlow()

    fun onEvent(event: AddEditProductEvent) {
        when (event) {
            is AddEditProductEvent.OnAddProduct -> addProduct(event.product)
            is AddEditProductEvent.OnGetSingleProduct -> getSingleProduct(event.productId)
            else -> Unit
        }
    }

    private fun addProduct(product: Product) {
        viewModelScope.launch {
            _addProductUiState.trySend(productRepository.createProduct(product))
        }
    }

    private fun getSingleProduct(productId: Long) {
        productRepository.getSingleProduct(productId)
            .onEach {
                _singleProductUiState.value = it
            }.launchIn(viewModelScope)
    }
}