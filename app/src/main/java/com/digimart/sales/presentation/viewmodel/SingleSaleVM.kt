package com.digimart.sales.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.digimart.data.local.entities.Sales
import com.digimart.sales.presentation.ui.SingleSaleEvent
import com.digimart.sales.usecase.SalesUseCase
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
    private val salesUseCase: SalesUseCase
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
        salesUseCase.getSingleSale(saleId)
            .onEach {
                _singleSaleUiState.value = it
            }.launchIn(viewModelScope)
    }

    private fun updateSingleSale(sales: Sales) {
        viewModelScope.launch { _saleUpdateUiState.trySend(salesUseCase.insertSale(sales)) }
    }
}