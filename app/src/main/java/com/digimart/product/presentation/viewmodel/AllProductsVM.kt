package com.digimart.product.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.digimart.data.local.entities.Product
import com.digimart.product.presentation.ui.AllProductEvent
import com.digimart.product.usecase.ProductUseCase
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
    private val productUseCase: ProductUseCase
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


    private fun getProducts(category: String?) = productUseCase.getAllProducts(category)

    private fun deleteProduct(product: Product) {
        viewModelScope.launch {
            productUseCase.deleteProduct(product)
        }
    }
//    {
//        productRepository.getAllProducts(category)
//            .onEach {
//                _allProductsUiState.value = it
//            }.launchIn(viewModelScope)
//    }
}