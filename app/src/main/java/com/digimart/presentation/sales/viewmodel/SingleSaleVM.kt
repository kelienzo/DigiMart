package com.digimart.presentation.sales.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.digimart.data.local.entities.Sales
import com.digimart.presentation.sales.repository.SalesRepository
import com.digimart.presentation.sales.ui.SingleSaleEvent
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
class SingleSaleVM @Inject constructor(
    private val salesRepository: SalesRepository
) : ViewModel() {

    private val _singleSaleUiState = MutableStateFlow<Sales?>(null)
    val singleSaleUiState = _singleSaleUiState.asStateFlow()

    private val _saleUpdateUiState = Channel<Long?>()
    val saleUpdateUiState = _saleUpdateUiState.receiveAsFlow()


    fun onEvent(event: SingleSaleEvent) {
        when (event) {
            is SingleSaleEvent.GetSingleSale -> getSingleSale(event.saleId)
            is SingleSaleEvent.OnCancelSingleSale -> updateSingleSale(event.sale)
            else -> Unit
        }
    }


    private fun getSingleSale(saleId: Long) {
        salesRepository.getSingleSale(saleId)
            .onEach {
                _singleSaleUiState.value = it
            }.launchIn(viewModelScope)
    }

    private fun updateSingleSale(sales: Sales) {
        viewModelScope.launch { _saleUpdateUiState.trySend(salesRepository.insertSale(sales)) }
    }
}