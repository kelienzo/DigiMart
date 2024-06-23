package com.digimart.presentation.sales.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.digimart.data.local.entities.Sales
import com.digimart.presentation.sales.repository.SalesRepository
import com.digimart.presentation.sales.ui.AllSalesEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class AllSalesVM @Inject constructor(
    private val salesRepository: SalesRepository
) : ViewModel() {

    private val _status = MutableStateFlow<String?>(null)
    val allSales = _status.flatMapLatest {
        getAllSales(it)
    }

    fun onEvent(event: AllSalesEvent) {
        when (event) {
            is AllSalesEvent.GetAllSales -> _status.update { event.status }
            is AllSalesEvent.OnDeleteFromCart -> deleteSale(event.sales)
            else -> Unit
        }
    }


    private fun getAllSales(status: String?) = salesRepository.getAllSales(status)

    private fun deleteSale(sales: Sales) {
        viewModelScope.launch { salesRepository.deleteSale(sales) }
    }
}