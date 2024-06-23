package com.digimart.presentation.product.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.digimart.data.local.entities.Product
import com.digimart.presentation.product.repository.ProductRepository
import com.digimart.presentation.product.ui.AllProductEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class AllProductsVM @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _category = MutableStateFlow<String?>(null)

    //    private val _allProductsUiState = MutableStateFlow<List<Product>>(emptyList())
    val allProductList = _category.flatMapLatest {
        getProducts(it)
    }

    fun onEvent(event: AllProductEvent) {
        when (event) {
            is AllProductEvent.GetAllProducts -> _category.update { event.category }
            is AllProductEvent.OnDeleteProduct -> deleteProduct(event.product)
            else -> Unit
        }
    }


    private fun getProducts(category: String?) = productRepository.getAllProducts(category)

    private fun deleteProduct(product: Product) {
        viewModelScope.launch {
            productRepository.deleteProduct(product)
        }
    }
//    {
//        productRepository.getAllProducts(category)
//            .onEach {
//                _allProductsUiState.value = it
//            }.launchIn(viewModelScope)
//    }
}